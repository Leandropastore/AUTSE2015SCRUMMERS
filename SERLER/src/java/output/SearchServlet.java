/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import classes.MyDatabase;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andy Li
 */
public class SearchServlet extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String keywords = request.getParameter("keywords");
        String field = request.getParameter("field");

        try (PrintWriter out = response.getWriter()) {
            printBeforeContent(out);
            printSearchBar(out);
            if (keywords == null || keywords == "") {
                out.println("<br /><br /><h3>Not search yet</h3><br />");
            } else {
                doSearch(out, keywords, field);
            }
            printAfterContent(out);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void printSearchBar(PrintWriter out) {

        out.println("<form ACTION=\"SearchServlet\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"text\" name=\"keywords\" value=\"\"/><br />");
        out.println("<select name = \"field\">");
        out.println("<option value=\"title\">by title</option>");
        out.println("<option value=\"author\">by author</option>");
        out.println("<option value=\"methodology\">by methodology</option>");
        out.println("<option value=\"practice\">by practice</option>");
        out.println("</select><br />");
        out.println("<input type=\"submit\" value=\"Search\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

    }

    private void doSearch(PrintWriter out, String keywords, String field) {
        myDB = new MyDatabase();
        String column = "";
        HashSet<Integer> result = new HashSet<Integer>();
        HashSet<String> wordSet = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(keywords, " ");
        while (st.hasMoreTokens()) {
            wordSet.add(st.nextToken().toLowerCase());
        }

        try {
            switch (field) {
                case "title":
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                    column = "Title";
                    break;
                case "author":
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AuthorTable");
                    column = "Title";
                    break;
                case "methodology":
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM MethodologyTable");
                    column = "M_Name";
                    break;
                case "practice":
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM PracticeTable");
                    column = "M_Name";
                    break;
            }
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String data = rs.getString(column).toLowerCase();
                int id = Integer.parseInt(rs.getString("ArticleID"));
                for (String word : wordSet) {
                    if (data.contains(word)) {
                        result.add(id);
                    }
                }

            }

            if (result.size() > 0) {
                out.println("<style>table, th, td {border: 1px solid black;}</style>");
                out.println("<table>");
                out.println("<tr><th>&emsp;ID</th><th>&emsp;Title</th><th>&emsp;Status</th></tr>");
                for (Integer i : result) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleId = ?");
                    stmt.setString(1, i.toString());
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        String id = rs.getString("ArticleId");
                        String title = rs.getString("Title");
                        String location = rs.getString("Location");
                        String status = rs.getString("Status");
                        out.println("<tr><th>&emsp;" + "<a href=\"ShowArticleDetail?id=" + id + "\">" + id
                                + "</a>&emsp;</th><th>" + "&emsp;<a href=\"" + location + "\">" + title + "</a>&emsp;"
                                + "&emsp;</th><th>&emsp;" + status
                                + "&emsp;</th></tr>");
                    }

                }
            out.println("</table>");
            }else{
                out.println("<br /><br /><h3>No Article found</h3><br />");
            }


        } catch (SQLException ex) {
            Logger.getLogger(DisplayAll.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
