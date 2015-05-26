/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import classes.MyDatabase;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andy Li
 */
public class CredibilityRating extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String rater, rating, reason;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        id = request.getParameter("id");
        title = request.getParameter("title");
        rater = request.getParameter("rater");
        rating = request.getParameter("rating");
        reason = request.getParameter("reason");

        if (rater == null || rater.trim().length() == 0
                || reason == null || reason.trim().length() == 0
                || rating == null) {
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                printBeforeContent(out);
                out.println("Giving the Credibility Rating to:<br/>&emsp;" + title);
                printForm(out);
                printAfterContent(out);
            }
        } else {
                    System.out.println((rater == null) +"__"+ (rater.trim().length() < 0)
                +"__"+ (reason == null) +"__"+ (reason.trim().length() < 0)
                +"__"+ (rating == null));
            addRating();
            request.setAttribute("id", id);
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/ShowArticleDetail");
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

    private void printForm(PrintWriter out) {
        System.out.println(rater);
        System.out.println(rating);
        System.out.println(reason);
        out.println("<form ACTION=\"CredibilityRating\" id =\"form1\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"title\" value=\"" + title + "\"/>");

        out.println("<label>Your Name:</label> <input type=\"text\" name=\"rater\" value=\"" + ((rater == null) ? "" : rater) + "\"/><br />");
        out.println("<label>Your Rating: </label> &emsp;"
                + "<input type=\"radio\" name=\"rating\" value=\"1\" checked/>1&emsp;"
                + "<input type=\"radio\" name=\"rating\" value=\"2\" />2&emsp;"
                + "<input type=\"radio\" name=\"rating\" value=\"3\" />3&emsp;"
                + "<input type=\"radio\" name=\"rating\" value=\"4\" />4&emsp;"
                + "<input type=\"radio\" name=\"rating\" value=\"5\" />5<br />");
        out.println("<label>Your Reason:</label> <br />");
        out.println("<textarea rows=\"3\" cols=\"40\" name=\"reason\" form=\"form1\"></textarea>" + ((reason == null) ? "" : reason) + "<br />");
        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Rate\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

        out.println("<a href=\"ShowArticleDetail?id=" + id + "\">Cancel</a>");
    }

    private void addRating() {
        myDB = new MyDatabase();
        try {
            stmt = myDB.getConn().prepareStatement("");
            stmt = myDB.getConn().prepareStatement("INSERT INTO CredibilityTable VALUES (?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, rater);
            stmt.setString(3, rating);
            stmt.setString(4, reason);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
