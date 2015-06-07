/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import classes.Member;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
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
public class ModerationServlet extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String status, result, reason;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Moderation");
        
        
        id = request.getParameter("id");
        status = request.getParameter("status");

        result = request.getParameter("result");
        reason = request.getParameter("reason");

        try (PrintWriter out = response.getWriter()) {
            printBeforeContent(out);
            switch (status) {
                case "new":
                    if (result == null) {
                        printForm(out);
                    } else if (result.equalsIgnoreCase("rejected") && reason.trim().length() == 0) {
                        printForm(out);
                    } else {
                        updateDatabase();
                        RequestDispatcher dispatcher = getServletContext().
                                getRequestDispatcher("/DisplayAll");
                        dispatcher.forward(request, response);
                    }
                    break;
                case "accepted":
                    out.println("<br/><H3>Sorry "+member.getName()+"</H3><BR/>");
                    out.println("<br/><H3>The article is already ACCEPTED!</H3><BR/>");
                    break;
                case "rejected":
                    out.println("<br/><H3>Sorry "+member.getName()+"</H3><BR/>");
                    out.println("<br/><H3>The article is already REJECTED!</H3><BR/>");
                    break;
                case "analysed":
                    out.println("<br/><H3>Sorry "+member.getName()+"</H3><BR/>");
                    out.println("<br/><H3>The article is already ANALYSED!</H3><BR/>");
                    break;
                case "released":
                    out.println("<br/><H3>Sorry "+member.getName()+"</H3><BR/>");
                    out.println("<br/><H3>The article is already RELEASED!</H3><BR/>");
                    break;
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

    private void printForm(PrintWriter out) {
        out.println("<form ACTION=\"ModerationServlet\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"status\" value=\"" + status + "\"/>");
        out.println("<br /><label>Status: </label>&emsp;");
        out.println("<input type=\"radio\" name=\"result\" value=\"new\" checked/>Unchange&emsp;");
        out.println("<input type=\"radio\" name=\"result\" value=\"accepted\" />Accept&emsp;");
        out.println("<input type=\"radio\" name=\"result\" value=\"rejected\" />Reject&emsp;<br />");
        out.println("<br /><label>Your Reason: </label><input type=\"text\" name=\"reason\" value=\"\" placeholder=\"Enter the reason for rejection\"/><br /><br /><br />");
        out.println("<div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" name=\"btnSend\" value=\"Submit\"/><br />");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");
        
        out.println("<br /><a href=\"ShowArticleDetail?id=" + id + "\">Cancel</a><br />");
    }

    private void updateDatabase() {
        System.out.println("++++++++++++++++++++updating");
        try {
            stmt = myDB.getConn().prepareStatement("Update allarticles SET "
                    + "Status = ? ,"
                    + "RejectedReason = ? ,"
                    + "ModeratedBy = ? "
                    + "WHERE ArticleId = ?; ");
            stmt.setString(1, result);
            stmt.setString(2, reason);
            stmt.setString(3, member.getName());
            stmt.setString(4, id);
            System.out.println("+++++++++++++++++++++++++++++++++++++");
            System.out.println(stmt);
            System.out.println("+++++++++++++++++++++++++++++++++++++");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
