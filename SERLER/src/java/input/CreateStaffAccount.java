/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import classes.Member;
import classes.MyDatabase;
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
 * @author Leandro Pastore
 */
public class CreateStaffAccount extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String aType,accountName,email,password;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        accountName = request.getParameter("accountName");
        aType = request.getParameter("aType");
        email = request.getParameter("email");
        password = request.getParameter("password");
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("new user", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Account Creation");

        if ( accountName == null || password == null
                || email == null) {
            try (PrintWriter out = response.getWriter()) {
                printBeforeContent(out);
                printForm(out);
                printAfterContent(out);
            }
        } else {
            createAccount();
            RequestDispatcher dispatcher = getServletContext().
                    getRequestDispatcher("/CreateStaffAccount");
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
        out.println("<form ACTION=\"AdminCreateAccount\" id =\"form1\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");

        out.println("<label>Account name: </label> &emsp;"
                + "<input type=\"text\" name=\"accountName\" value=\"\" />"
                + "<br /><br />");
        out.println("<label>Email: </label> &emsp;"
                + "<input type=\"text\" name=\"email\" value=\"\" />"
                + "<br /><br />");
        out.println("<label>Account type:</label> &emsp; <select name = \"aType\">"
                + "<option value=\"Moderator\"" + ((aType != null && aType.equalsIgnoreCase("Moderator")) ? "selected" : "") + ">Moderator</option>"
                + "<option value=\"Analyst\"" + ((aType != null && aType.equalsIgnoreCase("Analyst")) ? "selected" : "") + ">Analyst</option>"
                + "<option value=\"Administrator\"" + ((aType != null && aType.equalsIgnoreCase("Administrator")) ? "selected" : "") + ">Administrator</option>"
                + "</select><br /><br />");
        out.println("<label>Password: </label> &emsp;");
        out.println("<input type=\"text\" name=\"password\" value=\"\" /><br />");
        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Create\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");
    }

    private void createAccount() {
        myDB = new MyDatabase();
        try {
            stmt = myDB.getConn().prepareStatement("");
            stmt = myDB.getConn().prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?)");
            stmt.setString(1, accountName);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, aType);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
