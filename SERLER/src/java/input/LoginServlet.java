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
import java.sql.PreparedStatement;
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
public class LoginServlet extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String name, password, type;
    private boolean customerFound, correctPassword;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setPageTitle("Log in");

        name = request.getParameter("name");
        password = request.getParameter("password");

        checkLogin();

        if (name == null || password == null) {
            try (PrintWriter out = response.getWriter()) {
                printBeforeContent(out);
                printForm(out);
                printAfterContent(out);
            }
        } else {
            checkLogin();

            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                if (correctPassword) {
                    if (type == null) {
                        name = "guest";
                        type = "non-member";
                    }
                    member = new Member(name, type);
                    HttpSession session = request.getSession(true);
                    session.setAttribute("member", member);
                    RequestDispatcher dispatcher = getServletContext().
                            getRequestDispatcher("/HomeServlet");
                    dispatcher.forward(request, response);
                } else {
                    if (!customerFound) {
                        printBeforeContent(out);
                        out.println("The account name is not registed yet, please create your account");
                        printForm(out);

                        printAfterContent(out);
                    } else {
                        printBeforeContent(out);
                        out.println("Wrong password, please try again");
                        printForm(out);

                        printAfterContent(out);
                    }
                }
            }

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

    private void checkLogin() {
        correctPassword = false;
        customerFound = false;
        try {
            synchronized (this) // synchronize access to stmt
            {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM ACCOUNTS WHERE accountName = ?");
                stmt.setString(1, name);
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
                customerFound = rs.next();//true if there is a record
                if (customerFound) {
                    correctPassword = (password.equals(rs.getString("password")));
                    type = rs.getString("accountType");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during query: " + e);
            customerFound = false;
        }
    }

    private void printForm(PrintWriter out) {

        out.println("<form ACTION=\"LoginServlet\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<label>Account Name: </label>&emsp;<input type=\"text\" name=\"name\" value=\"" + ((name == null) ? "" : name) + "\"/><br /><br />");
        out.println("<label>Password: </label>&emsp;<input type=\"password\" name=\"password\" value=\"\"/><br /><br />");

        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Login\"/>"
                + "<button type=\"reset\" name=\"btnClean\">Clean</button>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

        out.println("<br /><a href=\"HomeServlet\">Cancel</a><br />");
    }

}
