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
import java.sql.PreparedStatement;
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
public class EvidenceItemServlet extends MyServlet {

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
    private String iName, iBenefit, iWho, iWhat, iWhere, iWhen, iHow, iWhy, iResult, iIntegrity;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Evidence Item");

        id = request.getParameter("id");
        title = request.getParameter("title");

        iName = request.getParameter("iName");
        iBenefit = request.getParameter("iBenefit");
        iWho = request.getParameter("iWho");
        iWhat = request.getParameter("iWhat");
        iWhere = request.getParameter("iWhere");
        iWhen = request.getParameter("iWhen");
        iHow = request.getParameter("iHow");
        iWhy = request.getParameter("iWhy");
        iResult = request.getParameter("iResult");
        iIntegrity = request.getParameter("iIntegrity");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if (request.getParameter("update") == null
                    || iName == null || iBenefit == null
                    || iWho == null || iWhat == null
                    || iWhere == null || iWhen == null
                    || iHow == null || iWhy == null
                    || iResult == null || iIntegrity == null
                    || iName.trim().length() == 0 || iBenefit.trim().length() == 0
                    || iWho.trim().length() == 0 || iWhat.trim().length() == 0
                    || iWhere.trim().length() == 0 || iWhen.trim().length() == 0
                    || iHow.trim().length() == 0 || iWhy.trim().length() == 0
                    || iResult.trim().length() == 0 || iIntegrity.trim().length() == 0) {
                
                printBeforeContent(out);

                out.println("Editing the Evidence Item for<br />&emsp;" + title);
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

    private void updateDatabase() {
        try {
            stmt = myDB.getConn().prepareStatement("INSERT INTO EvidenceItemTable "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    + "ON DUPLICATE KEY UPDATE"
                    + "  iName = VALUES(iName),"
                    + "  iBenefit = VALUES(iBenefit),"
                    + "  iWho = VALUES(iWho),"
                    + "  iWhat = VALUES(iWhat),"
                    + "  iWhere = VALUES(iWhere),"
                    + "  iWhen = VALUES(iWhen),"
                    + "  iHow = VALUES(iHow),"
                    + "  iWhy = VALUES(iWhy),"
                    + "  iResult = VALUES(iResult),"
                    + "  iIntegrity = VALUES(iIntegrity);");
            stmt.setString(1, id);
            stmt.setString(2, iName);
            stmt.setString(3, iBenefit);
            stmt.setString(4, iWho);
            stmt.setString(5, iWhat);
            stmt.setString(6, iWhere);
            stmt.setString(7, iWhen);
            stmt.setString(8, iHow);
            stmt.setString(9, iWhy);
            stmt.setString(10, iResult);
            stmt.setString(11, iIntegrity);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void printFrom(PrintWriter out) {
        //iName, iBenefit, iWho, iWhat, iWhere, iWhen, iHow, iWhy, iResult, iIntegrity;

        out.println("<form ACTION=\"EvidenceItemServlet\" id =\"form0\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"title\" value=\"" + title + "\"/>");
        out.println("<input type=\"hidden\" name=\"update\" value=\"yes\"/>");

        out.println("<label>Evidence Item Name: </label>&emsp;<input type=\"text\" name=\"iName\" value=\""
                + ((iName == null) ? "" : iName)
                + "\"/><br /><br />");
        out.println("<label>Benefit: </label><br />"
                + "&emsp;<textarea rows=\"3\" cols=\"50\" name=\"iBenefit\" form=\"form0\">"
                + ((iBenefit == null) ? "" : iBenefit)
                + "</textarea><br /><br />");
        out.println("<label>Who: </label>&emsp;<input type=\"text\" name=\"iWho\" value=\""
                + ((iWho == null) ? "" : iWho)
                + "\"/><br /><br />");
        out.println("<label>What: </label>&emsp;<input type=\"text\" name=\"iWhat\" value=\""
                + ((iWhat == null) ? "" : iWhat)
                + "\"/><br /><br />");
        out.println("<label>Where: </label>&emsp;<input type=\"text\" name=\"iWhere\" value=\""
                + ((iWhere == null) ? "" : iWhere)
                + "\"/><br /><br />");
        out.println("<label>When: </label>&emsp;<input type=\"text\" name=\"iWhen\" value=\""
                + ((iWhen == null) ? "" : iWhen)
                + "\"/><br /><br />");
        out.println("<label>How: </label>&emsp;<input type=\"text\" name=\"iHow\" value=\""
                + ((iHow == null) ? "" : iHow)
                + "\"/><br /><br />");
        out.println("<label>Why: </label>&emsp;<input type=\"text\" name=\"iWhy\" value=\""
                + ((iWhy == null) ? "" : iWhy)
                + "\"/><br /><br />");
        out.println("<label>Result: </label><br />"
                + "&emsp;<textarea rows=\"3\" cols=\"50\" name=\"iResult\" form=\"form0\">"
                + ((iResult == null) ? "" : iResult)
                + "</textarea><br /><br />");
        out.println("<label>Integrity: </label><br />"
                + "&emsp;<textarea rows=\"3\" cols=\"50\" name=\"iIntegrity\" form=\"form0\">"
                + ((iIntegrity == null) ? "" : iIntegrity)
                + "</textarea><br /><br />");

        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Submit\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");
    }

}
