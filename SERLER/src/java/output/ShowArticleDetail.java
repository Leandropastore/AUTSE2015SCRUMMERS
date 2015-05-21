/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import classes.MyDatabase;
import classes.MyServlet;
import input.UploadServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Andy Li
 */
public class ShowArticleDetail extends MyServlet {

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
    private String id, title;
    MyDatabase myDB;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        id = request.getParameter("id");
        System.out.println("id = " + id);
//        title = request.getParameter("title");
        myDB = new MyDatabase();

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            printBeforeContent(out);
            out.println("<h3>Basic Info:</h3><br />");
            printBasic(out);
            out.println("<br /><br /><h3>Credibility:</h3><br />");
            printCredibility(out);

            out.println("<br /><br /><h3>Methodology:</h3><br />");
            printMethodology(out);

            out.println("<br /><br /><h3>Practice:</h3><br />");
            printPractice(out);

            out.println("<br /><br /><h3>Evidence Item:</h3><br />");
            printItem(out);

            out.println("<br /><br /><h3>Research Design:</h3><br />");
            printResearch(out);

            out.println("<br /><br /><h3>Evidence Source:</h3><br />");
            // This method is not working as there is not evidencesourcetable in the database
//            printEvidence(out);

//            out.println("<br /><br />Practice:<br />");
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

    private void printBasic(PrintWriter out) {
        try {
            String link = "";
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleId = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                link = rs.getString("Location");
                title = rs.getString("Title");
                out.println("Title: <br/>");
                out.println("<a href=\"" + link + "\">" + title + "</a><br/>");
//                out.println("< a href=\" " + link + "\">" + title + "</a>");
            }
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AuthorTable WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            out.println("<br/>Author(s): <br />");
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    out.println("&emsp;" + rs.getString("AName") + "<br/>");
                }
            } else {
                out.println("&emsp;Unknow<br/>");
            }
            out.println("<a href=\"EditAuthors?id=" + id + "&title="+title+"\">------Add Author</a><br/>");
            
            stmt = myDB.getConn().prepareStatement("SELECT * FROM porcesseddetails WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            String journal = "Unknow";
            String year = "Unknow";
            String level = "Unknow";
            if (rs.isBeforeFirst()) {
                if (rs.next()) {
                    journal = rs.getString("Journal");
                    year = rs.getString("YearOfPublish");
                    level = rs.getString("ResearchLv");
                }
            } else {                
                out.println("<br />Not analysed yet<br />");
            }
            out.println("<br/>Journal: <br/>&emsp;" + journal  + "<br/>");
            out.println("Year of Publish: <br/>&emsp;" + year  + "<br/>");
            out.println("Research Level: <br/>&emsp;" + level  + "<br/>");
            
            out.println("<a href=\"EditBasic?id=" + id + "&title="+title+"\">------Edit Basic Info</a><br/>");

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void printCredibility(PrintWriter out) {
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM credibilitytable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            String AverageRating = getAverage("credibilitytable");
            out.println("Average Rating:&emsp;" + AverageRating);

            out.println("<br/><br/>More Details:");
            out.println("<style>table, th, td {border: 1px solid black;}</style>");
            out.println("<table>");
            out.println("<tr><th>&emsp;Rater&emsp;</th><th>&emsp;Reason&emsp;</th><th>&emsp;Rating&emsp;</th></tr>");
            rs.beforeFirst();
            while (rs.next()) {
                out.println("<tr><th>&emsp;" + rs.getString("Rater")
                        + "&emsp;</th><th>&emsp;" + rs.getString("Reason")
                        + "&emsp;</th><th>&emsp;" + rs.getString("Rating")
                        + "&emsp;</th></tr>");
            }
            out.println("</table>");

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printMethodology(PrintWriter out) {
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM methodologytable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                out.println("Methodology Name:&emsp;" + rs.getString("M_Name") + "<br/>"
                        + "Description:<br/>&emsp;" + rs.getString("Description") + "<br/>");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printPractice(PrintWriter out) {
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM practicetable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                out.println("Practice Name:&emsp;" + rs.getString("M_Name") + "<br/>"
                        + "Description:<br/>&emsp;" + rs.getString("Description") + "<br/>");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printItem(PrintWriter out) {
        try {
            ResultSet rs;
            stmt = myDB.getConn().prepareStatement("SELECT * FROM evidenceitemtable WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    out.println("Item Name:&emsp;");
                    out.println(rs.getString("ItemName") + "<br/>");
                    out.println("Benefit: <br/>&emsp;");
                    out.println(rs.getString("Benefit") + "<br/>");
                    out.println("Who:&emsp;");
                    out.println(rs.getString("I_Who") + "<br/>");
                    out.println("What:&emsp;");
                    out.println(rs.getString("I_What") + "<br/>");
                    out.println("Where:&emsp;");
                    out.println(rs.getString("I_Where") + "<br/>");
                    out.println("When:&emsp;");
                    out.println(rs.getString("I_When") + "<br/>");
                    out.println("How:&emsp;");
                    out.println(rs.getString("I_How") + "<br/>");
                    out.println("Why:&emsp;");
                    out.println(rs.getString("I_Why") + "<br/>");
                    out.println("Result: <br/>&emsp;");
                    out.println(rs.getString("I_result") + "<br/>");
                    out.println("Integrity: <br/>&emsp;");
                    out.println(rs.getString("I_Integrity") + "<br/>");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printResearch(PrintWriter out) {
        try {
            ResultSet rs;
            stmt = myDB.getConn().prepareStatement("SELECT * FROM researchdesigntable WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                /*
                 ArticleID int NOT NULL,
                 R_Name varchar(255) NOT NULL,
                 Queation varchar(255),
                 R_Method varchar(255),
                 Nature varchar(255),
                 PRIMARY KEY (ArticleID, R_Name)
                 */
                while (rs.next()) {
                    out.println("Research Name:&emsp;");
                    out.println(rs.getString("R_Name") + "<br/>");
                    out.println("Method:<br/>&emsp;");
                    out.println(rs.getString("R_Method") + "<br/>");
                    out.println("Metric(s):&emsp;");
                    printMetrics(out);
                    out.println("Nature of the Participants:<br/>&emsp;");
                    out.println(rs.getString("Nature") + "<br/>");
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void printEvidence(PrintWriter out) {
        try {
            ResultSet rs;
            stmt = myDB.getConn().prepareStatement("SELECT * FROM evidencesourcetable WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                /*
                 ArticleID int NOT NULL,
                 R_Name varchar(255) NOT NULL,
                 Queation varchar(255),
                 R_Method varchar(255),
                 Nature varchar(255),
                 PRIMARY KEY (ArticleID, R_Name)
                 */
                while (rs.next()) {
                    out.println("Title:<br/>&emsp;");
                    out.println(rs.getString("E_Title") + "<br/>");
                    out.println("Authors:<br/>&emsp;");
                    out.println(rs.getString("E_Authors") + "<br/>");
                    out.println("Journal:<br/>&emsp;");
                    out.println(rs.getString("E_Journal") + "<br/>");
                    out.println("Year:<br/>&emsp;");
                    out.println(rs.getString("E_Year") + "<br/>");
                    out.println("<tr><th>&emsp;" + rs.getString("Rater")
                            + "&emsp;</th><th>&emsp;" + rs.getString("Rating")
                            + "&emsp;</th><th>&emsp;" + rs.getString("Reason")
                            + "&emsp;</th></tr>");

                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printMetrics(PrintWriter out) {
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM metrictable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            out.println("<table>");
            out.println("<tr><th>&emsp;Name&emsp;</th><th>&emsp;Description&emsp;</th></tr>");
            rs.beforeFirst();
            while (rs.next()) {
                out.println("<tr><th>&emsp;" + rs.getString("M_Name")
                        + "&emsp;</th><th>&emsp;" + rs.getString("Description")
                        + "&emsp;</th></tr>");
            }
            out.println("</table>");

        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getAverage(String table) throws SQLException {

        double sum = 0;
        int count = 0;
        DecimalFormat df = new DecimalFormat("####0.0");
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM " + table + " WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                sum += Integer.parseInt(rs.getString("Rating"));
                count++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return df.format(sum / count);
    }

}
