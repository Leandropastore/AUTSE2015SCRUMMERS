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
public class EditBasic extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String journal, year, level, authors;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Bibliographic Info");
        
        id = request.getParameter("id");
        title = request.getParameter("title");
        authors = request.getParameter("authors");
        journal = request.getParameter("journal");
        year = request.getParameter("year");
        level = request.getParameter("level");
        if (request.getParameter("update") == null) {
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                printBeforeContent(out);
                out.println("Editing the Bibliographic Info of: <br/>&emsp;" + title);
                printForm(out);

                printAfterContent(out);
            }
        } else {
            updataDatabase();
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
        out.println("<form ACTION=\"EditBasic\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"title\" value=\"" + title + "\"/>");
        out.println("<input type=\"hidden\" name=\"update\" value=\"yes\"/>");
        out.println("<label>Authors: </label>&emsp;<input type=\"text\" name=\"authors\" value=\"" + authors + "\"/><br /><br />");
        out.println("<label>Journal: </label>&emsp;<input type=\"text\" name=\"journal\" value=\"" + journal + "\"/><br /><br />");
        out.println("<label>Year: </label>&emsp;<input type=\"number\" name=\"year\" value=\"" + year + "\"/><br /><br />");

        out.println("<label>Research level: </label>&emsp;");
        out.println("<select name=\"level\">");
        out.println("<option value=\"Level 1\" " + ((level != null && level.equalsIgnoreCase("level 1")) ? "selected" : "") + ">Level 1</option>");
        out.println("<option value=\"Level 2\" " + ((level != null && level.equalsIgnoreCase("level 2")) ? "selected" : "") + ">Level 2</option>");
        out.println("<option value=\"Level 3\" " + ((level != null && level.equalsIgnoreCase("level 3")) ? "selected" : "") + ">Level 3</option>");
        out.println("<option value=\"Level 3\" " + ((level != null && level.equalsIgnoreCase("level 4")) ? "selected" : "") + ">Level 4</option>");
        out.println("</select><br /><br />");

        out.println("<input type=\"submit\" value=\"Update\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

        out.println("<br /><a href=\"ShowArticleDetail?id=" + id + "\">Cancel</a><br />");
    }

    private void updataDatabase() {
        try {
            stmt = myDB.getConn().prepareStatement("Update allarticles SET "
                    + "authors = ?, "
                    + "Journal = ? , "
                    + "YearOfPublish = ?, "
                    + "ResearchLv = ? "
                    + "WHERE ArticleId = ?; ");
            stmt.setString(1, authors);
            stmt.setString(2, journal);
            stmt.setString(3, year);
            stmt.setString(4, level);
            stmt.setString(5, id);
            System.out.println("+++++++++++++++++++++++++++++++++++++");
            System.out.println(stmt);
            System.out.println("+++++++++++++++++++++++++++++++++++++");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
