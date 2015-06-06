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
public class EditMember extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String mode, name, password, email, type;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Edit Member");
        
        name = request.getParameter("name");
        password = request.getParameter("password");
        email = request.getParameter("email");
        type = request.getParameter("type");

        if (member.getType().equalsIgnoreCase("administrator")) {
            if (request.getParameter("update") == null
                    || password.trim().length() == 0
                    || email.trim().length() == 0) {

                try (PrintWriter out = response.getWriter()) {
                    printBeforeContent(out);

                    printForm(out);

                    printAfterContent(out);

                }
            } else {
                updateDatabase();
                RequestDispatcher dispatcher = getServletContext().
                        getRequestDispatcher("/MemberList");
                dispatcher.forward(request, response);
            }

        } else {
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/home_page.html");
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
        out.println("<form ACTION=\"EditMember\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"name\" value=\"" + name + "\"/>");
        out.println("<input type=\"hidden\" name=\"update\" value=\"yes\"/>");
        out.println("<label>Member Name: </label>&emsp; " + ((name == null) ? "" : name) + "<br /><br />");
        out.println("<label>Password: </label>&emsp;<input type=\"text\" name=\"password\" value=\"" + ((password == null) ? "" : password) + "\"/><br /><br />");
        out.println("<label>Email: </label>&emsp;<input type=\"email\" name=\"email\" value=\"" + ((email == null) ? "" : email) + "\"/><br /><br />");

        out.println("<label>Account Type: </label>&emsp;");
        out.println("<select name=\"type\">");
        out.println("<option value=\"administrator\" " + ((type != null && type.equalsIgnoreCase("administrator")) ? "selected" : "") + ">administrator</option>");
        out.println("<option value=\"moderator\" " + ((type != null && type.equalsIgnoreCase("moderator")) ? "selected" : "") + ">moderator</option>");
        out.println("<option value=\"analyst\" " + ((type != null && type.equalsIgnoreCase("analyst")) ? "selected" : "") + ">analyst</option>");
        out.println("<option value=\"contributor\" " + ((type != null && type.equalsIgnoreCase("level 4")) ? "selected" : "") + ">contributor</option>");
        out.println("</select><br /><br />");
        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Update\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

        out.println("<br /><a href=\"MemberList\">Cancel</a><br />");
    }

    private void updateDatabase() {
        try {
            stmt = myDB.getConn().prepareStatement("Update accounts SET "
                    + "Password = ?, "
                    + "Email = ? , "
                    + "AccountType = ? "
                    + "WHERE AccountName = ?; ");
            stmt.setString(1, password);
            stmt.setString(2, email);
            stmt.setString(3, type);
            stmt.setString(4, name);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
