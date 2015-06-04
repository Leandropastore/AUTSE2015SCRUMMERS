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
public class ResearchServlet extends MyServlet {

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
    private String qName, qDescription, methodName, methodDescription, metricsName, metricsDescription, participants;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("new user", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Research Design");

        id = request.getParameter("id");
        title = request.getParameter("title");

        qName = request.getParameter("qName");
        qDescription = request.getParameter("qDescription");
        methodName = request.getParameter("methodName");
        methodDescription = request.getParameter("methodDescription");
        metricsName = request.getParameter("metricsName");
        metricsDescription = request.getParameter("metricsDescription");
        participants = request.getParameter("participants");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if (request.getParameter("update") == null
                    || qName == null || qDescription == null
                    || methodName == null || methodDescription == null
                    || metricsName == null || metricsDescription == null
                    || participants == null
                    || qName.trim().length() == 0 || qDescription.trim().length() == 0
                    || methodName.trim().length() == 0 || methodDescription.trim().length() == 0
                    || metricsName.trim().length() == 0 || metricsDescription.trim().length() == 0
                    || participants.trim().length() == 0) {

                printBeforeContent(out);

                out.println("Editing the Research Design for<br />&emsp;" + title);
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
        //qName, qDescription, methodName, methodDescription, metricsName, metricsDescription, participants;
        try {
            stmt = myDB.getConn().prepareStatement("INSERT INTO ResearchDesignTable "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    + "ON DUPLICATE KEY UPDATE"
                    + "  qName = VALUES(qName),"
                    + "  qDescription = VALUES(qDescription),"
                    + "  methodName = VALUES(methodName),"
                    + "  methodDescription = VALUES(methodDescription),"
                    + "  metricsName = VALUES(metricsName),"
                    + "  metricsDescription = VALUES(metricsDescription),"
                    + "  participants = VALUES(participants);");
            stmt.setString(1, id);
            stmt.setString(2, qName);
            stmt.setString(3, qDescription);
            stmt.setString(4, methodName);
            stmt.setString(5, methodDescription);
            stmt.setString(6, metricsName);
            stmt.setString(7, metricsDescription);
            stmt.setString(8, participants);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void printFrom(PrintWriter out) {
        //qName, qDescription, methodName, methodDescription, metricsName, metricsDescription, participants;

        out.println("<form ACTION=\"ResearchServlet\" id =\"form0\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");
        out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\"/>");
        out.println("<input type=\"hidden\" name=\"title\" value=\"" + title + "\"/>");
        out.println("<input type=\"hidden\" name=\"update\" value=\"yes\"/>");

        out.println("<label>Research Question: </label>&emsp; <input type=\"text\" name=\"qName\" value=\""
                + ((qName == null) ? "" : qName)
                + "\"/><br /><br />");
        out.println("<label>Description: </label><br />"
                + "&emsp; <textarea rows=\"3\" cols=\"50\" name=\"qDescription\" form=\"form0\">"
                + ((qDescription == null) ? "" : qDescription)
                + "</textarea><br /><br />");
        out.println("<label>Method:</label>&emsp; <select name = \"methodName\">"
                + "<option value=\"XP\"" + ((methodName != null && methodName.equalsIgnoreCase("XP")) ? "selected" : "") + ">XP</option>"
                + "<option value=\"Scrum\"" + ((methodName != null && methodName.equalsIgnoreCase("Scrum")) ? "selected" : "") + ">Scrum</option>"
                + "<option value=\"Waterfall\"" + ((methodName != null && methodName.equalsIgnoreCase("Waterfall")) ? "selected" : "") + ">Waterfall</option>"
                + "<option value=\"Spiral\"" + ((methodName != null && methodName.equalsIgnoreCase("Spiral")) ? "selected" : "") + ">Spiral</option>"
                + "<option value=\"Other\"" + ((methodName != null && methodName.equalsIgnoreCase("Other")) ? "selected" : "") + ">Other</option>"
                + "</select><br />");
        out.println("<label>Method Description:</label><br />"
                + "&emsp; <textarea rows=\"5\" cols=\"50\" name=\"methodDescription\" form=\"form0\">"
                + ((methodDescription == null) ? "" : methodDescription)
                + "</textarea><br /><br />");
        out.println("<label>Metric: </label>&emsp; <input type=\"text\" name=\"metricsName\" value=\""
                + ((metricsName == null) ? "" : metricsName)
                + "\"/><br />");
        out.println("<label>Metric Description: </label><br />"
                + "&emsp; <textarea rows=\"3\" cols=\"50\" name=\"metricsDescription\" form=\"form0\">"
                + ((metricsDescription == null) ? "" : metricsDescription)
                + "</textarea><br /><br />");
        
        out.println("<label>Nature of the Participants:</label>&emsp; <select name = \"participants\">"
                + "<option value=\"Professor\"" + ((participants != null && participants.equalsIgnoreCase("Professor")) ? "selected" : "") + ">Professor</option>"
                + "<option value=\"Professors\"" + ((participants != null && participants.equalsIgnoreCase("Professors")) ? "selected" : "") + ">Professors</option>"
                + "<option value=\"Professor and Students\"" + ((participants != null && participants.equalsIgnoreCase("Professor and Students")) ? "selected" : "") + ">Professor and Students</option>"
                + "<option value=\"Professors and Students\"" + ((participants != null && participants.equalsIgnoreCase("Professors and Students")) ? "selected" : "") + ">Professors and Students</option>"
                + "<option value=\"Other\"" + ((participants != null && participants.equalsIgnoreCase("Other")) ? "selected" : "") + ">Other</option>"
                + "</select><br />");
        
        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Submit\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");
    }

}
