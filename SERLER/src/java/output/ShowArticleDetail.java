/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import classes.Member;
import classes.MyServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    private String authors, journal, year, level, status;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        id = request.getParameter("id");
        System.out.println("id = " + id);
//        title = request.getParameter("title");
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("new user", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Article Details");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            printBeforeContent(out);
            out.println("<h3>Bibliographic Info:</h3><br />");
            printBasic(out);
            out.println("<br /><br /><h3>Credibility:</h3><br />");
            printCredibility(out);

            out.println("<br /><br /><h3>The software development methodology used:</h3><br />");
            printMethodology(out);

            out.println("<br /><br /><h3>The Practice being investigated:</h3><br />");
            printPractice(out);

            out.println("<br /><br /><h3>Evidence Item:</h3><br />");
            printItem(out);

            out.println("<br /><br /><h3>Research Design:</h3><br />");
            printResearch(out);

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
            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleId = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            String reason = "";
            String moderatedBy = "";
            if (rs.next()) {
                title = rs.getString("Title");
                authors = rs.getString("Authors");
                journal = rs.getString("Journal");
                year = rs.getString("YearOfPublish");
                level = rs.getString("ResearchLv");
                status = rs.getString("Status");
                reason = rs.getString("rejectedReason");
                moderatedBy = rs.getString("moderatedBy");
                out.println("Title: <br/>&emsp;" + title + "<br/><br/>");
                out.println("Author(s): <br />&emsp;" + authors + "<br/><br/>");
                out.println("Journal: <br/>&emsp;" + journal + "<br/><br/>");
                out.println("Year of Publish: <br/>&emsp;" + year + "<br/><br/>");
                out.println("Research Level: <br/>&emsp;" + level + "<br/><br/>");
//                out.println("< a href=\" " + link + "\">" + title + "</a>");
            }

            if (status.equalsIgnoreCase("rejected")) {
//                if(reason)
                out.println("<br />------This article is rejected by:&emsp;"+moderatedBy);
                out.println("<br />------Reason of rejection:&emsp;"+reason);
            }
            if (member.getType().equalsIgnoreCase("administrator") || member.getType().equalsIgnoreCase("analyst")) {
                out.println("<a href=\"EditBasic?"
                        + "id=" + id
                        + "&title=" + title
                        + "&authors=" + authors
                        + "&journal=" + journal
                        + "&level=" + level
                        + "&year=" + year
                        + "\">------Edit Basic Info</a><br/>");
            }
            if (member.getType().equalsIgnoreCase("moderator") && status.equalsIgnoreCase("new")) {
                String link = "\"ModerationServlet?id=" + id + "&status=" + status + "\"";
                out.println("<a href=" + link + ">------This is a new article, please moderate it...</a><br/>");
            }

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }

    }

    private void printCredibility(PrintWriter out) {
        try {

            String AverageRating = getAverage("credibilitytable");
            out.println("Average Rating:&emsp;" + AverageRating);

            out.println("<br/>More Details:");
            out.println("<style>table, th, td {border: 1px solid black;}</style>");
            out.println("<table>");
            out.println("<tr><th>&emsp;Rater&emsp;</th><th>&emsp;Reason&emsp;</th><th>&emsp;Rating&emsp;</th></tr>");
            stmt = myDB.getConn().prepareStatement("SELECT * FROM credibilitytable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                out.println("<tr><th>&emsp;" + rs.getString("Rater")
                        + "&emsp;</th><th>&emsp;" + rs.getString("Reason")
                        + "&emsp;</th><th>&emsp;" + rs.getString("Rating")
                        + "&emsp;</th></tr>");
            }
            out.println("</table>");

            if (!member.getType().equalsIgnoreCase("Non-member")) {
                out.println("<br/><a href=\"CredibilityRating?id=" + id + "&title=" + title + "\">------Rate this article</a><br/>");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    private void printMethodology(PrintWriter out) {
        String mName = "";
        String mDescription = "";
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM methodologytable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                mName = rs.getString("M_Name");
                mDescription = rs.getString("Description");
                out.println("Methodology Name:&emsp;" + mName + "<br/>"
                        + "Description:<br/>&emsp;" + mDescription + "<br/>");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        if (member.getType().equalsIgnoreCase("administrator") || member.getType().equalsIgnoreCase("analyst")) {
            out.println("<a href=\"MethodologyServlet?"
                    + "id=" + id
                    + "&title=" + title
                    + "&mName=" + mName
                    + "&mDescrition=" + mDescription
                    + "\">------Edit Methodology</a><br/>");
        }

    }

    private void printPractice(PrintWriter out) {

        String pName = "";
        String pDescription = "";
        try {
            stmt = myDB.getConn().prepareStatement("SELECT * FROM practicetable WHERE ArticleId = ?");
            stmt.setString(1, id);
            System.out.println("stmt = " + stmt);
            ResultSet rs = stmt.executeQuery();
            rs.beforeFirst();
            while (rs.next()) {
                pName = rs.getString("M_Name");
                pDescription = rs.getString("Description");
                out.println("Practice Name:&emsp;" + pName + "<br/>"
                        + "Description:<br/>&emsp;" + pDescription + "<br/>");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        if (member.getType().equalsIgnoreCase("administrator") || member.getType().equalsIgnoreCase("analyst")) {
            out.println("<a href=\"PracticeServlet?"
                    + "id=" + id
                    + "&title=" + title
                    + "&pName=" + pName
                    + "&pDescrition=" + pDescription
                    + "\">------Edit Practice</a><br/>");
        }
    }

    private void printItem(PrintWriter out) {
        boolean hasItem = false;
        String iName = "";
        String iBenefit = "";
        String iWho = "";
        String iWhat = "";
        String iWhere = "";
        String iWhen = "";
        String iHow = "";
        String iWhy = "";
        String iResult = "";
        String iIntegrity = "";
        try {
            ResultSet rs;
            stmt = myDB.getConn().prepareStatement("SELECT * FROM evidenceitemtable WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                hasItem = true;
                iName = rs.getString("iName");
                iBenefit = rs.getString("iBenefit");
                iWho = rs.getString("iWho");
                iWhat = rs.getString("iWhat");
                iWhere = rs.getString("iWhere");
                iWhen = rs.getString("iWhen");
                iHow = rs.getString("iHow");
                iWhy = rs.getString("iWhy");
                iResult = rs.getString("iResult");
                iIntegrity = rs.getString("iIntegrity");
                out.println("Item Name:&emsp;" + iName + "<br/>");
                out.println("Benefit: <br/>&emsp;" + iBenefit + "<br/>");
                out.println("Who:&emsp;" + iWho + "<br/>");
                out.println("What:&emsp;" + iWhat + "<br/>");
                out.println("Where:&emsp;" + iWhere + "<br/>");
                out.println("When:&emsp;" + iWhen + "<br/>");
                out.println("How:&emsp;" + iHow + "<br/>");
                out.println("Why:&emsp;" + iWhy + "<br/>");
                out.println("Result: <br/>&emsp;" + iResult + "<br/>");
                out.println("Integrity: <br/>&emsp;" + iIntegrity + "<br/><br/>");
            }

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        if (member.getType().equalsIgnoreCase("administrator") || member.getType().equalsIgnoreCase("analyst")) {
            out.println("<a href=\"EvidenceItemServlet?"
                    + "id=" + id
                    + "&title=" + title
                    + "&iName=" + iName
                    + "&iBenefit=" + iBenefit
                    + "&iWho=" + iWho
                    + "&iWhat=" + iWhat
                    + "&iWhere=" + iWhere
                    + "&iWhen=" + iWhen
                    + "&iHow=" + iHow
                    + "&iWhy=" + iWhy
                    + "&iResult=" + iResult
                    + "&iIntegrity=" + iIntegrity
                    + "\">------Edit Practice</a><br/>");
        }
        if (hasItem) {
            try {
                String AverageRating = getAverage("ConfidenceTable");
                out.println("<br/>Average Confidence Rating:&emsp;" + AverageRating);
                out.println("<br/>More Details:");
                out.println("<style>table, th, td {border: 1px solid black;}</style>");
                out.println("<table>");
                out.println("<tr><th>&emsp;Rater&emsp;</th><th>&emsp;Reason&emsp;</th><th>&emsp;Rating&emsp;</th></tr>");
                stmt = myDB.getConn().prepareStatement("SELECT * FROM ConfidenceTable WHERE ArticleId = ?");
                stmt.setString(1, id);
                System.out.println("stmt = " + stmt);
                ResultSet rs = stmt.executeQuery();
                rs.beforeFirst();
                while (rs.next()) {
                    out.println("<tr><th>&emsp;" + rs.getString("Rater")
                            + "&emsp;</th><th>&emsp;" + rs.getString("Reason")
                            + "&emsp;</th><th>&emsp;" + rs.getString("Rating")
                            + "&emsp;</th></tr>");
                }
                out.println("</table>");
                if (!member.getType().equalsIgnoreCase("Non-member")) {
                    out.println("<br/><a href=\"ConfidenceServlet?"
                            + "id=" + id
                            + "&title=" + title
                            + "&iName=" + iName
                            + "\">------Rate this Evidence Item</a><br/>");
                }
            } catch (SQLException ex) {
                System.out.println("ERROR: " + ex);
            }
        }
    }

    private void printResearch(PrintWriter out) {
        String qName = "";
        String qDescription = "";
        String methodName = "";
        String methodDescription = "";
        String metricsName = "";
        String metricsDescription = "";
        String participants = "";

        try {
            ResultSet rs;
            stmt = myDB.getConn().prepareStatement("SELECT * FROM researchdesigntable WHERE ArticleID = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    qName = rs.getString("qName");
                    qDescription = rs.getString("qDescription");
                    methodName = rs.getString("methodName");
                    methodDescription = rs.getString("methodDescription");
                    metricsName = rs.getString("metricsName");
                    metricsDescription = rs.getString("metricsDescription");
                    participants = rs.getString("participants");

                    out.println("Research Question:&emsp;" + qName + "<br/>&emsp;" + qDescription + "<br/>");
                    out.println("<br/>Method:&emsp;" + methodName + "<br/>&emsp;" + methodDescription + "<br/>");
                    out.println("<br/>Metric;" + metricsName + "<br/>&emsp;" + metricsDescription + "<br/>");
                    out.println("<br/>Nature of the Participants:&emsp" + qName + "<br/>&emsp;" + qDescription + "<br/>");
                }
            }
            if (member.getType().equalsIgnoreCase("administrator") || member.getType().equalsIgnoreCase("analyst")) {
                out.println("<a href=\"ResearchServlet?"
                        + "id=" + id
                        + "&title=" + title
                        + "&qName=" + qName
                        + "&qDescription=" + qDescription
                        + "&methodName=" + methodName
                        + "&methodDescription=" + methodDescription
                        + "&metricsName=" + metricsName
                        + "&metricsDescription=" + metricsDescription
                        + "&participants=" + participants
                        + "\">------Edit Research Design</a><br/>");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
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
            System.out.println("ERROR: " + ex);
        }
        if (sum == 0) {
            return "not avaliable";
        } else {
            return df.format(sum / count);
        }
    }

}
