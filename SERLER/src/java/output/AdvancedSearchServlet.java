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
public class AdvancedSearchServlet extends SearchServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String author, journal, year, level, credibility, methodology, practice, evidenceItem, confidence, researchDesign;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        title = request.getParameter("title");
        author = request.getParameter("author");
        journal = request.getParameter("journal");
        year = request.getParameter("year");
        level = request.getParameter("level");
        credibility = request.getParameter("credibility");
        methodology = request.getParameter("methodology");
        practice = request.getParameter("practice");
        evidenceItem = request.getParameter("evidenceItem");
        confidence = request.getParameter("confidence");
        researchDesign = request.getParameter("researchDesign");

        try (PrintWriter out = response.getWriter()) {
            printBeforeContent(out);
            printSearchBar(out);
            if (title == null
                    || author == null
                    || journal == null
                    || year == null
                    || level == null
                    || credibility == null
                    || methodology == null
                    || practice == null
                    || evidenceItem == null
                    || confidence == null
                    || researchDesign == null) {
                out.println("<br /><br /><h3>Not search yet</h3><br />");
            } else {
                doSearch(out);
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

    private void doSearch(PrintWriter out) {
        title = title.trim().toLowerCase();
        author = author.trim().toLowerCase();
        journal = journal.trim().toLowerCase();
        year = year.trim().toLowerCase();
        level = level.trim().toLowerCase();
        credibility = credibility.trim().toLowerCase();
        methodology = methodology.trim().toLowerCase();
        practice = practice.trim().toLowerCase();
        evidenceItem = evidenceItem.trim().toLowerCase();
        confidence = confidence.trim().toLowerCase();
        researchDesign = researchDesign.trim().toLowerCase();

        myDB = new MyDatabase();
        boolean searched = false;
        String column = "";
        HashSet<Integer> result = new HashSet<>();
        HashSet<String> wordSet = new HashSet<>();
        ResultSet rs;

        try {
            if (title.length() != 0) {
                searched = true;
                wordSet.clear();
                column = "Title";
                StringTokenizer st = new StringTokenizer(title, " ");
                while (st.hasMoreTokens()) {
                    wordSet.add(st.nextToken().toLowerCase());
                }
                myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString(column).toLowerCase();
                    int id = Integer.parseInt(rs.getString("ArticleID"));
                    for (String word : wordSet) {
                        if (data.contains(word)) {
                            result.add(id);
                        }
                    }
                }
            }
            
            if (author.length() != 0) {
                searched = true;
                wordSet.clear();
                column = "AName";
                StringTokenizer st = new StringTokenizer(author, " ");
                while (st.hasMoreTokens()) {
                    wordSet.add(st.nextToken().toLowerCase());
                }
                myDB.getConn().prepareStatement("SELECT * FROM AuthorTable");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString(column).toLowerCase();
                    int id = Integer.parseInt(rs.getString("ArticleID"));
                    for (String word : wordSet) {
                        if (data.contains(word)) {
                            result.add(id);
                        }
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
            } else {
                out.println("<br /><br /><h3>No Article found</h3><br />");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisplayAll.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
