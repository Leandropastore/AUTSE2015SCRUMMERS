/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andy Li
 */
public class MyServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected MyDatabase myDB = new MyDatabase();
    protected PreparedStatement stmt;
    protected String id, title;
    protected String pageTitle = "Amazing Articles";
    protected String ctrlPanel = "<a href=\"HomeServlet\">Home</a>&emsp;&emsp;&emsp;"
            + "<a href=\"LoginServlet\">Login</a>&emsp;&emsp;&emsp;"
            + "<a href=\"AddMember\">Create Account</a>&emsp;&emsp;&emsp;"
            + "<a href=\"SearchServlet\">Search</a>&emsp;&emsp;&emsp;"
            + "<a href=\"DisplayAll\">Articles</a>";

    protected Member member;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LayoutServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LayoutServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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

    public void destroy() {
        super.destroy();
        // close database connection
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (myDB != null) {
                myDB.closeConn();
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception while closing: " + e);
        }
    }

    public void printBeforeContent(PrintWriter out) {
        out.println("<!DOCTYPE html>");

        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + pageTitle + "</title>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        out.println("<link type=\"text/css\" href=\"css/ui-lightness/jquery-ui-1.8.23.custom.css\" rel=\"stylesheet\" />");
        out.println("<link type=\"text/css\" href=\"css/common.css\" rel=\"stylesheet\"/>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div align=\"center\" id=\"layout\">");
        out.println("<div id=\"bodytopbottombox\">");
        out.println("<h1>Amazing Articles</h1>");
        out.println("</div>");
        out.println("<div id=\"bodytopbottombox\">");
        out.println("<p>");
        out.println(ctrlPanel);
        out.println("</p></div>");
        out.println("<div id=\"bodycontentbox\">");
        out.println("<br /><br />");

    }

    public void printAfterContent(PrintWriter out) {

        out.println("<br /><br />");
        out.println("</div>");
        out.println("<div id=\"bodytopbottombox\">");
        out.println("<p>");
        out.println("<a href=\"about_us.html\">About Us</a>&emsp;&emsp;&emsp;");
        out.println("<a href=\"contact_us.html\">Contact Us</a>");
        out.println("</p>");
        out.println("</div>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void setPageTitle(String pt) {
        pageTitle = pt;
    }

    protected void setControlPanel(String type) {

        switch (type) {
            case "administrator":
                ctrlPanel = "<a href=\"HomeServlet\">Home</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"LogoutServlet\">Log Out</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"MemberList\">Manage Accounts</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"AddMember\">Create Staff Account</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"AdvancedSearchServlet\">Advanced Search</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"DisplayAll\">All Articles</a>";
                break;
            case "moderator":
                ctrlPanel = "<a href=\"HomeServlet\">Home</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"LogoutServlet\">Log Out</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"JobList\">Articles for moderate</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"AdvancedSearchServlet\">Advanced Search</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"DisplayAll\">All Articles</a>";
                break;
            case "analyst":
                ctrlPanel = "<a href=\"HomeServlet\">Home</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"LogoutServlet\">Log Out</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"JobList\">Articles for Analyse</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"AdvancedSearchServlet\">Advanced Search</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"DisplayAll\">All Articles</a>";
                break;
            case "contributor":
                ctrlPanel = "<a href=\"HomeServlet\">Home</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"LogoutServlet\">Log Out</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"UploadServlet\">Upload Article</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"AdvancedSearchServlet\">Advanced Search</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"DisplayAll\">All Articles</a>";
                break;
            default:
                ctrlPanel = "<a href=\"HomeServlet\">Home</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"LoginServlet\">Login</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"AddMember\">Create Account</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"SearchServlet\">Search</a>&emsp;&emsp;&emsp;"
                        + "<a href=\"DisplayAll\">Articles</a>";

        }
    }
}
