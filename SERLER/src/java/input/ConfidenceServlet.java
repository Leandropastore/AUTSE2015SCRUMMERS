/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import classes.MyDatabase;
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
public class ConfidenceServlet extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String item = request.getParameter("item");
        String rater = request.getParameter("name");
        String rating = request.getParameter("rating");
        String reason = request.getParameter("reason");
        
        
        MyDatabase myDB = new MyDatabase();

        try {
            stmt = myDB.getConn().prepareStatement("");
            stmt = myDB.getConn().prepareStatement("INSERT INTO ConfidenceTable "
                    + "VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, item);
            stmt.setString(3, rater);
            stmt.setString(4, rating);
            stmt.setString(5, reason);
            System.out.println(stmt);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>All Articles</title>");
            out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            out.println("<meta http-equiv=\"refresh\" content=\"5; url = http://localhost:8080/SERLER/DisplayAll\"/>");            

            out.println("<link type=\"text/css\" href=\"css/ui-lightness/jquery-ui-1.8.23.custom.css\" rel=\"stylesheet\" />");
            out.println("<link type=\"text/css\" href=\"css/common.css\" rel=\"stylesheet\"/>");

            out.println("</head>");
            out.println("<body>");

            out.println("<div align=\"center\" id=\"layout\">");
            out.println("<div style=\"border:1px solid black;text-align:center;width:700px;\">");
            out.println("<h1>Amazing Agile</h1>");
            out.println("</div>");
            out.println("<div style=\"border:1px solid black;text-align:center;width:700px;\">");
            out.println("<p>");
            out.println("<a href=\"home_page.html\">Home</a>&emsp;&emsp;&emsp;");
            out.println("<a href=\"login.html\">Login</a>&emsp;&emsp;&emsp;");
            out.println("<a href=\"sign_up.html\">Create Account</a>&emsp;&emsp;&emsp;");
            out.println("<a href=\"DisplayAll\">Articles</a></p>");
            out.println("</div>");
            out.println("<div style=\"border:1px solid black;text-align:left;width:700px;\">");
            out.println("<br /><br /><br /><br /><br /><br />");
            
            out.println("Your rating is add into the system.");

            out.println("<br /><br /><br /><br /><br /><br />");
            out.println("</div>");
            out.println("<div style=\"border:1px solid black;text-align:center;width:700px;\">");
            out.println("<p>");
            out.println("<a href=\"about_us.html\">About Us</a>&emsp;&emsp;&emsp;");
            out.println("<a href=\"contact_us.html\">Contact Us</a>");
            out.println("</p>");
            out.println("</div>");
            out.println("</div>");

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

}
