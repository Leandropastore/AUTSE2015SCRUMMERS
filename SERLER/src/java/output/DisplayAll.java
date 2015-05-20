/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import classes.GeneralArticle;
import classes.MyDatabase;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andy Li
 */
public class DisplayAll extends MyServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private MyDatabase myDB;
    private PreparedStatement stmt;
    private List<GeneralArticle> articleList;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            printBeforeContent(out);

            printContent(out);

            out.println("<br /><br /><br /><br /><br /><br />");
            
            // adding links for testing
            addArticle(out);
            addEvidenceItem(out);
            ConfidenceRating(out);
            ResearchDesign(out);
            EvidenceSource(out);
            

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

    private void printContent(PrintWriter out) {

        MyDatabase myDB = new MyDatabase();
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();

//                GeneralArticle article = new GeneralArticle(
//                        rs.getString("A_ID"),
//                        rs.getString("Title"),
//                        rs.getString("Status"),
//                        rs.getString("Location"));
                out.println("<style>table, th, td {border: 1px solid black;}</style>");
            out.println("<table>");
            out.println("<tr><th>&emsp;ID</th><th>&emsp;Title</th><th>&emsp;Status</th></tr>");
            while (rs.next()) {
                System.out.println(stmt+"-----has rs");
                String id = rs.getString("ArticleId");
                String title = rs.getString("Title");
                String location = rs.getString("Location");
                String status = rs.getString("Status");
                out.println("<tr><th>&emsp;" + "<a href=\"ShowArticleDetail?id="+id+"\">"+id
                        + "</a>&emsp;</th><th>" + "&emsp;<a href=\""+location+"\">"+title+"</a>&emsp;"
                        + "&emsp;</th><th>&emsp;" + status 
                        + "&emsp;</th></tr>");
            }
            out.println("</table>");

        } catch (SQLException ex) {
            Logger.getLogger(DisplayAll.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addArticle(PrintWriter out) {
        out.println("<a href=\"upload_article.html\">Upload an Article</a>");
        out.println("<br /><br />");
    }
        private void addEvidenceItem(PrintWriter out) {
        out.println("<a href=\"evidence_item.html\">Add an Evidence Item</a>");
        out.println("<br /><br />");
    }
        private void ConfidenceRating(PrintWriter out) {
        out.println("<a href=\"confidence_rating.html\">Rate an Evidence Item</a>");
        out.println("<br /><br />");
    }
        private void ResearchDesign(PrintWriter out) {
        out.println("<a href=\"research_design.html\">Add Research Design</a>");
        out.println("<br /><br />");
    }
        private void EvidenceSource(PrintWriter out) {
        out.println("<a href=\"evidence_source.html\">Add Evidence Source</a>");
        out.println("<br /><br />");
        }

    
}
