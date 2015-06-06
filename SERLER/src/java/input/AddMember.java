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
public class AddMember extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String name, password, email, type;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Account Info");

        name = request.getParameter("name");
        password = request.getParameter("password");
        email = request.getParameter("email");
        if (member.getType().equalsIgnoreCase("administrator")) {
            type = request.getParameter("type");
        } else {
            type = "contributor";
        }

        try (PrintWriter out = response.getWriter()) {
            if (name == null || password == null || email == null) {
                printBeforeContent(out);

                printForm(out);

                printAfterContent(out);

            } else {
                if (name.trim().length() == 0
                        || password.trim().length() == 0
                        || email.trim().length() == 0) {
                    printBeforeContent(out);
                    out.println("Please enter:");
                    if (name.trim().length() == 0) {
                        out.println(" a valid account name,");
                    }
                    if (password.trim().length() == 0) {
                        out.println(" a valid password,");
                    }
                    if (email.trim().length() == 0) {
                        out.println(" a valid email");
                    }
                    printForm(out);
                    printAfterContent(out);
                } else {
                    if (checkName()) {

                        printBeforeContent(out);

                        out.println(" The account name is already existed");
                        printForm(out);

                        printAfterContent(out);
                    } else {
                        updateDatabase();
                        if (member.getType().equalsIgnoreCase("administrator")) {
                            RequestDispatcher dispatcher = getServletContext().
                                    getRequestDispatcher("/MemberList");
                            dispatcher.forward(request, response);
                        } else {
                            member = new Member(name, type);
                            session.setAttribute("member", member);
                            RequestDispatcher dispatcher = getServletContext().
                                    getRequestDispatcher("/HomeServlet");
                            dispatcher.forward(request, response);
                        }
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

    private void printForm(PrintWriter out) {
        out.println("<form ACTION=\"AddMember\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");

        out.println("<label>Account Name: </label>&emsp;<input type=\"text\" name=\"name\" value=\"" + ((name == null) ? "" : name) + "\"/><br /><br />");
        out.println("<label>Password: </label>&emsp;<input type=\"text\" name=\"password\" value=\"" + ((password == null) ? "" : password) + "\"/><br /><br />");
        out.println("<label>Email: </label>&emsp;<input type=\"email\" name=\"email\" value=\"" + ((email == null) ? "" : email) + "\"/><br /><br />");

        if (member.getType().equalsIgnoreCase("administrator")) {
            out.println("<label>Account Type: </label>&emsp;");
            out.println("<select name=\"type\">");
            out.println("<option value=\"administrator\" " + ((type != null && type.equalsIgnoreCase("administrator")) ? "selected" : "") + ">administrator</option>");
            out.println("<option value=\"moderator\" " + ((type != null && type.equalsIgnoreCase("moderator")) ? "selected" : "") + ">moderator</option>");
            out.println("<option value=\"analyst\" " + ((type != null && type.equalsIgnoreCase("analyst")) ? "selected" : "") + ">analyst</option>");
            out.println("<option value=\"contributor\" " + ((type != null && type.equalsIgnoreCase("level 4")) ? "selected" : "") + ">contributor</option>");
            out.println("</select><br /><br />");
        }

        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Submit\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

        if (member.getType().equalsIgnoreCase("administrator")) {
            out.println("<br /><a href=\"MemberList\">Cancel</a><br />");
        } else {

            out.println("<br /><a href=\"HomeServlet\">Cancel</a><br />");
        }
    }

    private boolean checkName() {
        boolean customerFound = false;
        try {
            synchronized (this) // synchronize access to stmt
            {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM ACCOUNTS WHERE accountName = ?");
                stmt.setString(1, name);
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery();
                customerFound = rs.next();//true if there is a record
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception during query: " + e);
            customerFound = false;
        }
        return customerFound;
    }

    private void updateDatabase() {
        try {
            stmt = myDB.getConn().prepareStatement("INSERT INTO Accounts VALUES(?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, type);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
