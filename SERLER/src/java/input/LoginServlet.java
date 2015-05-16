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
    private PreparedStatement stmt;
    private MyDatabase myDB;
    private String name, type;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        name = request.getParameter("name");
        String password = request.getParameter("password");
        MyDatabase myDB = new MyDatabase();

        boolean customerFound;
        boolean correctPassword = false;
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            printBeforeContent(out);

            if (!customerFound) {
                out.println("You are not registed yet, please create your account");
            } else {
                if (correctPassword) {
                    out.println("Welcome back " + name + ".");
                    toControlPanel(request, response);
                } else {
                    out.println("Wrong password, try again");
                }
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

    private void toControlPanel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Member member = new Member(name, type);
        HttpSession session = request.getSession(true);
        session.setAttribute("member", member);
        String link = "/login_failed.html";
        type = type.toLowerCase();
        System.out.println("type = "+type);
        switch(type){
            case "administrator":
                link = "/admin_ctrl_pnl.html";
                break;
            case "moderator":
                link = "/moderator_ctrl_pnl.html";
                break;
            case "analyst":
                link = "/analyst_ctrl_pnl.html";
                break;
            case "contributor":
                link = "/contributor_ctrl_pnl.html";
                break;
            default:
                break;
        }
        RequestDispatcher dispatcher = getServletContext().
                getRequestDispatcher(link);
        dispatcher.forward(request, response);

    }
}
