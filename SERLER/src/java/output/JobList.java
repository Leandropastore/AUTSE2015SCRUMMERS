/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import classes.Member;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Andy Li
 */
public class JobList extends MyServlet {

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
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        if (member.getType().equalsIgnoreCase("moderator") || member.getType().equalsIgnoreCase("analyst")) {
            setControlPanel(member.getType());
            if (member.getType().equalsIgnoreCase("moderator")) {
                setPageTitle("Articles for Moderate");
            } else if (member.getType().equalsIgnoreCase("analyst")) {
                setPageTitle("Articles for Analyse");
            }
            try (PrintWriter out = response.getWriter()) {
                printBeforeContent(out);

                printList(out);

                printAfterContent(out);

            }

        } else {
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/DisplayAll");
            dispatcher.forward(request, response);
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

    private void printList(PrintWriter out) {
        try {

            boolean isEmpty = true;
            out.println("<style>table, th, td {border: 1px solid black;}</style>");
            out.println("<table>");
            out.println("<tr><th>&emsp;ID</th><th>&emsp;Title</th><th>&emsp;Arthors</th><th>&emsp;Year of Publish</th><th>&emsp;Journal</th></tr>");
            String rTitle = "";
            String rAuthors = "";
            String rYear = "";
            String rJournal = "";

            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if ((member.getType().equalsIgnoreCase("moderator") && rs.getString("Status").equalsIgnoreCase("new"))
                        || (member.getType().equalsIgnoreCase("analyst") && rs.getString("Status").equalsIgnoreCase("accepted"))) {
                    isEmpty = false;
                    rJournal = rs.getString("Journal");
                    rYear = rs.getString("YearOfPublish");
                    String id0 = rs.getString("ArticleId");
                    rTitle = rs.getString("Title");
                    rAuthors = rs.getString("Authors");
                    out.println("<tr><th>&emsp;" + "<a href=\"ShowArticleDetail?id=" + id0 + "\">" + id0 + "</a>"
                            + "&emsp;</th><th>&emsp;<a href=\"ShowArticleDetail?id=" + id0 + "\">" + rTitle + "</a>"
                            + "&emsp;</th><th>&emsp;" + rAuthors
                            + "&emsp;</th><th>&emsp;" + rYear
                            + "&emsp;</th><th>&emsp;" + rJournal
                            + "&emsp;</th></tr>");
                }
            }
            out.println("</table>");
            if(isEmpty){
                    out.println("<br/><H3>There is No article for you to work on.</H3><BR/>");
            }

        } catch (SQLException ex) {
            System.out.println("Error: \n" + ex);

        }
    }

}
