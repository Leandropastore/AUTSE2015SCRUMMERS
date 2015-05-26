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
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.StringTokenizer;
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
    protected HashSet<Integer> result = new HashSet<>();
    protected DecimalFormat df = new DecimalFormat("####0.0");
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keywords = request.getParameter("keywords");
        String field = request.getParameter("field");
        String test = request.getParameter("test");

        try (PrintWriter out = response.getWriter()) {
            printBeforeContent(out);
            printSearchBar(out);
            if (keywords == null || keywords.trim().length() == 0) {
                out.println("<br /><br /><h3>Not search yet</h3><br />");
//                out.println(test);
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

//        out.println("<input type=\"radio\" name=\"test\" value=\"33\"/>33<br />");
//        out.println("<input type=\"radio\" name=\"test\" value=\"44\"/>44<br />");

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
        result = new HashSet<>();
        myDB = new MyDatabase();
        String column = "";
        HashSet<String> wordSet = new HashSet<>();
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
                    column = "AName";
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
                int rID = Integer.parseInt(rs.getString("ArticleID"));
                for (String word : wordSet) {
                    if (data.contains(word)) {
                        result.add(rID);
                    }
                }
            }
            printResult(out);

        } catch (SQLException ex) {
            System.out.println("Error: \n" + ex);
        }

    }

    protected void printResult(PrintWriter out) {
        try {

            if (result.size() > 0) {
                out.println("<style>table, th, td {border: 1px solid black;}</style>");
                out.println("<table>");
                out.println("<tr><th>&emsp;ID</th><th>&emsp;Title</th><th>&emsp;Arthors</th><th>&emsp;Year of Publish</th><th>&emsp;Journal</th><th>&emsp;Rating</th></tr>");
                String rTitle = "";
                String rAuthors = "";
                String rYear = "";
                String rJournal = "";
                String rRating = "";

                for (Integer i : result) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM porcesseddetails WHERE ArticleId = ?");
                    stmt.setString(1, i.toString());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {

                        rJournal = rs.getString("Journal");
                        rYear = rs.getString("YearOfPublish");
                        String id0 = rs.getString("ArticleId");
                        rTitle = getTitle(id0);
                        rAuthors = getAuthors(id0);
                        rRating = df.format(getRating(id0));
                        out.println("<tr><th>&emsp;" + "<a href=\"ShowArticleDetail?id=" + id0 + "\">" + id0 + "</a>"
                                + "&emsp;</th><th>&emsp;<a href=\"ShowArticleDetail?id=" + id0 + "\">" + rTitle + "</a>"
                                + "&emsp;</th><th>&emsp;" + rAuthors
                                + "&emsp;</th><th>&emsp;" + rYear
                                + "&emsp;</th><th>&emsp;" + rJournal
                                + "&emsp;</th><th>&emsp;" + rRating
                                + "&emsp;</th></tr>");
                    }

                }
                out.println("</table>");
            } else {
                out.println("<br /><br /><h3>No Article found</h3><br />");
            }

        } catch (SQLException ex) {
            System.out.println("Error: \n" + ex);

        }
    }

    protected String getAuthors(String id) {
        String authorsList = "";
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AuthorTable WHERE ArticleID = ?");
            stmt.setString(1, id);
            System.out.println(stmt);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                authorsList += result.getString("AName") + ", ";
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return authorsList;
    }

    protected double getRating(String id) {

        double sum = 0;
        int count = 0;
        
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM credibilitytable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                sum += Integer.parseInt(rs.getString("Rating"));
                count++;
            }
        } catch (SQLException ex) {

            System.out.println(ex);
        }
        return (sum / count);
    }

    protected String getTitle(String id) {
        String s = "unknow";
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleID = ?");
            stmt.setString(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                s = result.getString("Title");
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return s;
    }

}
