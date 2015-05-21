/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import classes.MyDatabase;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private String journal, year, level;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        myDB = new MyDatabase();
        id = request.getParameter("id");
        title = request.getParameter("title");
        journal = request.getParameter("journal");
        year = request.getParameter("year");
        level = request.getParameter("level");
        if (journal == null || journal == ""
                || year == null || year == ""
                || level == null || level == "") {
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                printBeforeContent(out);
                out.println("Editing the basic info of: <br/>&emsp;" + title);
                printForm(out);

                printAfterContent(out);
            }
        } else {
            addBasic();
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
        out.println("The artitcle is published in which journal?<br/><input type=\"text\" name=\"journal\" value=\"\"/><br />");
        out.println("The artitcle is published in which year?<br/><input type=\"text\" name=\"year\" value=\"\"/><br />");
        out.println("What is the research level?<br/><input type=\"text\" name=\"level\" value=\"\"/><br />");
        out.println("<input type=\"submit\" value=\"Add\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

        out.println("<a href=\"ShowArticleDetail?id=" + id + "\">Cancel</a>");
    }

    private void addBasic() {
        try {
            stmt = myDB.getConn().prepareStatement("INSERT INTO PorcessedDetails VALUES (?, ?, ?, ?)"
                    + "ON DUPLICATE KEY UPDATE"
                    + "  Journal     = VALUES(Journal),"
                    + "  YearOfPublish = VALUES(YearOfPublish),"
                    + "  ResearchLv = VALUES(ResearchLv);");

            stmt.setString(1, id);
            stmt.setString(2, journal);
            stmt.setString(3, year);
            stmt.setString(4, level);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
