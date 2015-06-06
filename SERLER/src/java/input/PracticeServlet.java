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
public class PracticeServlet extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String pName, pDescription;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Practice");
        
        id = request.getParameter("id");
        title = request.getParameter("title");
        pName = request.getParameter("pName");
        pDescription = request.getParameter("pDescription");
        try (PrintWriter out = response.getWriter()) {
            if (request.getParameter("update") == null
                    ||pName == null || pName.trim().length() == 0
                    || pDescription == null || pDescription.trim().length() == 0) {
                printBeforeContent(out);
                out.println("Editing the Practice for<br />&emsp;" + title);
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
        out.println("<form ACTION=\"PracticeServlet\" id =\"form0\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"title\" value=\"" + title + "\"/>");
        out.println("<input type=\"hidden\" name=\"update\" value=\"yes\"/>");

        out.println("<label>Practice:</label>&emsp; <select name = \"pName\">"
                + "<option value=\"Practice A\"" + ((pName != null && pName.equalsIgnoreCase("Practice A")) ? "selected" : "") + ">Practice A</option>"
                + "<option value=\"Practice B\"" + ((pName != null && pName.equalsIgnoreCase("Practice B")) ? "selected" : "") + ">Practice B</option>"
                + "<option value=\"Practice C\"" + ((pName != null && pName.equalsIgnoreCase("Practice C")) ? "selected" : "") + ">Practice C</option>"
                + "<option value=\"Practice D\"" + ((pName != null && pName.equalsIgnoreCase("Practice D")) ? "selected" : "") + ">Practice D</option>"
                + "<option value=\"Refactoring\"" + ((pName != null && pName.equalsIgnoreCase("Refactoring")) ? "selected" : "") + ">Refactoring</option>"
                + "<option value=\"Cloud Computing\"" + ((pName != null && pName.equalsIgnoreCase("Cloud Computing")) ? "selected" : "") + ">Cloud Computing</option>"
                + "<option value=\"Other\"" + ((pName != null && pName.equalsIgnoreCase("Other")) ? "selected" : "") + ">Other</option>"
                + "</select><br />");
        out.println("<label>Description:</label><br />"
                + "<br /><textarea rows=\"5\" cols=\"50\" name=\"pDescription\" form=\"form0\">"
                + ((pDescription == null) ? "" : pDescription)
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
            stmt = myDB.getConn().prepareStatement("INSERT INTO Practicetable VALUES  (?, ?, ?)"
                    + "ON DUPLICATE KEY UPDATE"
                    + "  M_Name = VALUES(M_Name),"
                    + "  Description = VALUES(Description);");
            stmt.setString(1, id);
            stmt.setString(2, pName);
            stmt.setString(3, pDescription);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
