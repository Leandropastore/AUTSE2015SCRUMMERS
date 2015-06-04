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
public class MethodologyServlet extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String mName, mDescription;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        id = request.getParameter("id");
        title = request.getParameter("title");
        mName = request.getParameter("mName");
        mDescription = request.getParameter("mDescription");
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("new user", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Methodology");
        try (PrintWriter out = response.getWriter()) {
            if (request.getParameter("update") == null
                    ||mName == null || mName.trim().length() == 0
                    || mDescription == null || mDescription.trim().length() == 0) {
                printBeforeContent(out);
                out.println("Editing the Methodology for<br />&emsp;" + title);
                printFrom(out);
                printAfterContent(out);
            } else {
                updateDatabase();
                request.setAttribute("id", id);
                RequestDispatcher dispatcher = getServletContext().
                        getRequestDispatcher("/ShowArticleDetail");
                dispatcher.forward(request, response);
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

    private void printFrom(PrintWriter out) {
        out.println("<form ACTION=\"MethodologyServlet\" id =\"form0\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"title\" value=\"" + title + "\"/>");
        out.println("<input type=\"hidden\" name=\"update\" value=\"yes\"/>");

        out.println("<label>Methodology:</label>&emsp; <select name = \"mName\">"
                + "<option value=\"Methodology A\"" + ((mName != null && mName.equalsIgnoreCase("Methodology A")) ? "selected" : "") + ">Methodology A</option>"
                + "<option value=\"Methodology B\"" + ((mName != null && mName.equalsIgnoreCase("Methodology B")) ? "selected" : "") + ">Methodology B</option>"
                + "<option value=\"Methodology C\"" + ((mName != null && mName.equalsIgnoreCase("Methodology C")) ? "selected" : "") + ">Methodology C</option>"
                + "<option value=\"Methodology D\"" + ((mName != null && mName.equalsIgnoreCase("Methodology D")) ? "selected" : "") + ">Methodology D</option>"
                + "<option value=\"Other\"" + ((mName != null && mName.equalsIgnoreCase("Other")) ? "selected" : "") + ">Other</option>"
                + "</select><br />");
        out.println("<label>Description:</label><br />"
                + "&emsp;<textarea rows=\"5\" cols=\"50\" name=\"mDescription\" form=\"form0\">"
                + ((mDescription == null) ? "" : mDescription)
                + "</textarea><br />");

        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Submit\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");
        
        out.println("<br /><a href=\"ShowArticleDetail?id=" + id + "\">Cancel</a><br />");
    }

    private void updateDatabase() {
       try {
            stmt = myDB.getConn().prepareStatement("INSERT INTO methodologytable VALUES  (?, ?, ?)"
                    + "ON DUPLICATE KEY UPDATE"
                    + "  M_Name = VALUES(M_Name),"
                    + "  Description = VALUES(Description);");
            stmt.setString(1, id);
            stmt.setString(2, mName);
            stmt.setString(3, mDescription);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
