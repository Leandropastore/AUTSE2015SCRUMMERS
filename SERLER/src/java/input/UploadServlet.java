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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Andy Li
 */
public class UploadServlet extends MyServlet {

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

    private String authors, journal, year, researchLv;
    private String rater, rating, reason;
    private Member member;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Upload Article");
        

        title = request.getParameter("title");
        authors = request.getParameter("authors");
        journal = request.getParameter("journal");
        year = request.getParameter("year");
        researchLv = request.getParameter("researchLv");
        rater = member.getName();
        rating = request.getParameter("credibility");
        reason = request.getParameter("reason");

        if (title == null || rating == null
                || title.trim().length() == 0
                || authors.trim().length() == 0
                || journal.trim().length() == 0
                || researchLv.trim().length() == 0
                || rater.trim().length() == 0
                || reason.trim().length() == 0) {
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                printBeforeContent(out);

                out.println("Submiting an Evidence Source By: " + rater + "<br/>");
                printForm(out);

                printAfterContent(out);
            }
        } else {
            updateDatabase();
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                printBeforeContent(out);
                out.println("Your article is added into the system.");
                printAfterContent(out);
            }
        }
//        
//        try {
//            stmt = myDB.getConn().prepareStatement("");
//            stmt = myDB.getConn().prepareStatement("INSERT INTO AllArticles (Title, Location, Status)"
//                    + "VALUES (?, ?, ?)");
//            stmt.setString(1, title);
//            stmt.setString(2, location);
//            stmt.setString(3, "new");
//            System.out.println(stmt);
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            System.err.println(ex.getMessage());
//        }

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
        out.println("<div><form action=\"UploadServlet\">");
        out.println("<fieldset><div style=\"text-align: justify\">");
        out.println("<label>Bibliographic Info: </label><br /><br />");
        out.println("<label>Title: </label><input type=\"text\" name=\"title\" value=\"\"/><br /><br />");
        out.println("<label>Authors: </label><input type=\"text\" name=\"authors\" value=\"\"/><br /><br />");
        out.println("<label>Journal: </label><input type=\"text\" name=\"journal\" value=\"\"/><br /><br />");
        out.println("<label>Year: </label><input type=\"number\" name=\"year\" value=\"\"/><br /><br />");
        
        out.println("<label>Research level: </label>");
        out.println("<select name=\"researchLv\">");
        out.println("<option value=\"Level 1\">Level 1</option>");
        out.println("<option value=\"Level 2\">Level 2</option>");
        out.println("<option value=\"Level 3\">Level 3</option>");
        out.println("<option value=\"Level 4\">Level 4</option>");
        out.println("</select><br /><br />");
        
        out.println("<label>Credibility rating: </label><br /><br />");
//        out.println("<label>Your Name: </label><input type=\"text\" name=\"name\" value=\"\"/><br /><br />");
        out.println("<label>Your Rating: </label>");
        out.println("<input type=\"radio\" name=\"credibility\" value=\"1\" />1&emsp;");
        out.println("<input type=\"radio\" name=\"credibility\" value=\"2\" />2&emsp;");
        out.println("<input type=\"radio\" name=\"credibility\" value=\"3\" />3&emsp;");
        out.println("<input type=\"radio\" name=\"credibility\" value=\"4\" />4&emsp;");
        out.println("<input type=\"radio\" name=\"credibility\" value=\"5\" />5<br /><br />");
        out.println("<label>Your Reason: </label><input type=\"text\" name=\"reason\" value=\"\"/><br /><br /><br />");
        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" name=\"btnSend\" value=\"Add Article\"/>");
        out.println("</div></fieldset></form></div>");

    }

    private void updateDatabase() {
        try {
            stmt = myDB.getConn().prepareStatement("INSERT INTO AllArticles "
                    + "(Title, Authors, Journal, YearOfPublish, ResearchLv, Status, Contributor)"
                    + "VALUES (?, ?, ?, ?, ?, ?,?)");
            stmt.setString(1, title);
            stmt.setString(2, authors);
            stmt.setString(3, journal);
            stmt.setString(4, year);
            stmt.setString(5, researchLv);
            stmt.setString(6, "new");
            stmt.setString(7, rater);
            System.out.println(stmt);
            stmt.executeUpdate();
            
            id = getId(title);
            stmt = myDB.getConn().prepareStatement("INSERT INTO CredibilityTable VALUES(?, ?, ?,?)");
            stmt.setString(1, id);
            stmt.setString(2, rater);
            stmt.setString(3, rating);
            stmt.setString(4, reason);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private String getId(String title) {
        String result = "";
         try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE Title = ?");
            stmt.setString(1, title);
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                result = rs.getString("ArticleID");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
         return result;
    }

}
