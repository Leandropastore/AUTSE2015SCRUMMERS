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
public class MemberList extends MyServlet {

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
        setControlPanel(member.getType());
        setPageTitle("Manage Accouts");

        if (member.getType().equalsIgnoreCase("administrator")) {
            
            try (PrintWriter out = response.getWriter()) {
                printBeforeContent(out);
                AddMember(out);
                printAccounts(out);

                printAfterContent(out);

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

    private void printAccounts(PrintWriter out) {
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM Accounts");
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();

            out.println("<style>table, th, td {border: 1px solid black;}</style>");
            out.println("<table>");
            out.println("<tr><th>&emsp;Member Name </th><th>&emsp;Email</th><th>&emsp;Account Type</th><th>&emsp;&emsp;</th></tr>");
            while (rs.next()) {
                System.out.println(stmt + "-----has rs");
                String name = rs.getString("AccountName");
                String password = rs.getString("Password");
                String email = rs.getString("Email");
                String type = rs.getString("AccountType");
                String link = "\"EditMember?name=" + name 
                        + "&password=" + password
                        + "&email=" + email
                        + "&type=" + type
                        + "\"";
//                if (member != null && member.getType().equalsIgnoreCase("moderator")) {
//                    link = "\"ModerationServlet?id=" + id + "&status=" + status + "\"";
//                }
                out.println("<tr><th>&emsp;" + name
                        + "&emsp;</th><th>&emsp;" + email
                        + "&emsp;</th><th>&emsp;" + type
                        + "&emsp;</th><th>&emsp;<a href=" + link + ">Edit</a>"
                        + "&emsp;</th></tr>");
            }
            out.println("</table>");

        } catch (SQLException ex) {
            System.out.println("Error: \n" + ex);
        }
    }

    private void AddMember(PrintWriter out) {
        out.println("<br /><a href=\"AddMember\">Add New Mamber</a>");
        out.println("<br /><br />");
    }
}
