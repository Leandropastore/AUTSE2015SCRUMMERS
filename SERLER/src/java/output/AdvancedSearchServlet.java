/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import classes.Member;
import classes.MyDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Andy Li
 */
public class AdvancedSearchServlet extends SearchServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String author, journal, year1, year2, level, credibility, methodology, practice, evidenceItem, researchDesign;
    private String mode;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        member = (Member) session.getAttribute("member");
        if (member == null) {
            member = new Member("guest", "Non-member");
        }
        setControlPanel(member.getType());
        setPageTitle("Advanced Search");
        
        mode = request.getParameter("mode");
        title = request.getParameter("title");
        author = request.getParameter("author");
        journal = request.getParameter("journal");
        year1 = request.getParameter("year1");
        year2 = request.getParameter("year2");
        level = request.getParameter("level");
        credibility = request.getParameter("credibility");
        methodology = request.getParameter("methodology");
        practice = request.getParameter("practice");
        evidenceItem = request.getParameter("evidenceItem");
        researchDesign = request.getParameter("researchDesign");

        try (PrintWriter out = response.getWriter()) {
            printBeforeContent(out);
            printSearchBar(out);
            if (title == null /*
                     || author == null
                     || journal == null
                     || year1 == null
                     || year2 == null
                     || level == null
                     || credibility == null
                     || methodology == null
                     || practice == null
                     || evidenceItem == null
                     || confidence == null
                     || researchDesign == null
                     */) {
                out.println("<br /><br /><h3>Not search yet</h3><br />");
            } else {
                doSearch(out);
            }
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

    private void printSearchBar(PrintWriter out) {
        /*
         journal = request.getParameter("journal");
         year1 = request.getParameter("year1");
         year2 = request.getParameter("year2");
         level = request.getParameter("level");
         credibility = request.getParameter("credibility");
         methodology = request.getParameter("methodology");
         practice = request.getParameter("practice");
         evidenceItem = request.getParameter("evidenceItem");
         researchDesign = request.getParameter("researchDesign");
         */

        out.println("<form ACTION=\"AdvancedSearchServlet\">");
        out.println("<fieldset>");
        out.println("<div style=\"text-align: justify\">");

        out.println("<label>Search Mode:</label> "
                + "<input type=\"radio\" name=\"mode\" value=\"and\"/>AND&emsp;"
                + "<input type=\"radio\" name=\"mode\" value=\"or\" checked />OR<br />");
        out.println("<label>Title:</label> <input type=\"text\" name=\"title\" value=\"" + ((title == null) ? "" : title) + "\"/><br />");
        out.println("<label>Author:</label> <input type=\"text\" name=\"author\" value=\"" + ((author == null) ? "" : author) + "\"/><br />");
        out.println("<label>Journal:</label> <input type=\"text\" name=\"journal\" value=\"" + ((journal == null) ? "" : journal) + "\"/><br />");
        out.println("<label>Year:</label> Form <input type=\"number\" name=\"year1\" value=\"" + ((year1 == null) ? "" : year1) + "\"/>"
                + " to <input type=\"number\" name=\"year2\" value=\"" + ((year2 == null) ? "" : year2) + "\"/><br />");
        out.println("<label>Research Level:</label> <input type=\"text\" name=\"level\" value=\"" + ((level == null) ? "" : level) + "\"/><br />");
        out.println("<label>Credibility: >= </label> "
                + "<input type=\"radio\" name=\"credibility\" value=\"1\" />1&emsp;"
                + "<input type=\"radio\" name=\"credibility\" value=\"2\" />2&emsp;"
                + "<input type=\"radio\" name=\"credibility\" value=\"3\" />3&emsp;"
                + "<input type=\"radio\" name=\"credibility\" value=\"4\" />4&emsp;"
                + "<input type=\"radio\" name=\"credibility\" value=\"5\" />5<br />");
        out.println("<label>Methodology:</label> <input type=\"text\" name=\"methodology\" value=\"" + ((methodology == null) ? "" : methodology) + "\"/><br />");
        out.println("<label>Practice:</label> <input type=\"text\" name=\"practice\" value=\"" + ((practice == null) ? "" : practice) + "\"/><br />");
        out.println("<label>Evidence Item:</label> <input type=\"text\" name=\"evidenceItem\" value=\"" + ((practice == null) ? "" : practice) + "\"/><br />");
        out.println("<label>Research Design:</label> <input type=\"text\" name=\"researchDesign\" value=\"" + ((practice == null) ? "" : practice) + "\"/><br />");
        out.println("</div><div style=\"text-align: center\"><br />");
        out.println("<input type=\"submit\" value=\"Search\"/>");
        out.println("</div>");
        out.println("</fieldset>");
        out.println("</form>");

    }

    private void doSearch(PrintWriter out) {

        result.clear();
        myDB = new MyDatabase();

        title = title.trim().toLowerCase();
        author = author.trim().toLowerCase();
        journal = journal.trim().toLowerCase();
        methodology = methodology.trim().toLowerCase();
        practice = practice.trim().toLowerCase();
        evidenceItem = evidenceItem.trim().toLowerCase();
        researchDesign = researchDesign.trim().toLowerCase();

        switch (mode) {
            case "and":
                andSearch();
                break;
            case "or":
                orSearch();
                break;
        }
        printResult(out);

    }

    private void andSearch() {
        HashSet<String> wordSet = new HashSet<>();
        ResultSet rs;
        System.out.println("Doing AND search");
        boolean searching = true;
        try {
            if (title.length() > 0 && searching) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                StringTokenizer st = new StringTokenizer(title, " ");
                while (st.hasMoreTokens()) {
                    wordSet.add(st.nextToken().toLowerCase());
                }
                while (rs.next()) {
                    String data = rs.getString("Title").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    for (String word : wordSet) {
                        if (data.contains(word)) {
                            result.add(rID);
                        }
                    }
                }
            }
            if (author.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    StringTokenizer st = new StringTokenizer(author, " ");
                    while (st.hasMoreTokens()) {
                        wordSet.add(st.nextToken().toLowerCase());
                    }
                    while (rs.next()) {
                        String data = rs.getString("Authors").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        for (String word : wordSet) {
                            if (data.contains(word)) {
                                result.add(rID);
                            }
                        }
                    }
                } else {
                    System.out.println("Checking aritcle------" + result);
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        StringTokenizer st = new StringTokenizer(author, " ");
                        while (st.hasMoreTokens()) {
                            wordSet.add(st.nextToken().toLowerCase());
                        }
                        while (rs.next()) {
                            String data = rs.getString("Authors").toLowerCase();
                            for (String word : wordSet) {
                                if (data.contains(word)) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if (journal.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String data = rs.getString("Journal").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (data.equalsIgnoreCase(journal)) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            found = true;
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if ((year1.length() > 0 || year2.length() > 0) && searching) {

                int yearBegin = 0, yearEnd = 0;
                try {
                    yearBegin = Integer.parseInt(year1);
                } catch (NumberFormatException e) {
                    yearBegin = 0;
                }
                try {
                    yearEnd = Integer.parseInt(year2);
                } catch (NumberFormatException e) {
                    yearEnd = 0;
                }
                if (yearEnd < yearBegin) {
                    yearBegin = yearEnd;
                }

                if (yearBegin > 0 && yearBegin <= yearEnd) {
                    if (result.isEmpty()) {
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                        System.out.println(stmt);
                        System.out.println("from " + yearBegin + " to " + yearEnd);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            int data = Integer.parseInt(rs.getString("YearOfPublish"));
                            int rID = Integer.parseInt(rs.getString("ArticleID"));
                            if (yearBegin <= data && data <= yearEnd) {
                                result.add(rID);
                            }
                        }
                    } else {
                        HashSet<Integer> searchingSet = new HashSet<>();
                        for (Integer i : result) {
                            searchingSet.add(i);
                        }
                        for (Integer i : searchingSet) {
                            boolean found = false;
                            String currentID = "" + i;
                            stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleID = ?");
                            stmt.setString(1, currentID);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                int data = Integer.parseInt(rs.getString("YearOfPublish"));
                                if (yearBegin <= data && data <= yearEnd) {
                                    found = true;
                                }
                            }
                            if (!found) {
                                result.remove(i);
                            }
                        }
                        if (result.isEmpty()) {
                            searching = false;
                        }
                    }
                }
            }
            if (level.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String data = rs.getString("ResearchLv").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (data.equalsIgnoreCase(level)) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            String data = rs.getString("ResearchLv").toLowerCase();
                            int rID = Integer.parseInt(rs.getString("ArticleID"));
                            if (data.equalsIgnoreCase(level)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if (credibility != null && searching) {
                int rating = Integer.parseInt(credibility);
                System.out.println("searching rating----"+rating);
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        double avgRating = getRating(rs.getString("ArticleID"));
                        if (avgRating >= rating) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        double avgRating = getRating(currentID);
                        if (avgRating >= rating) {
                            found = true;
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if (methodology.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM MethodologyTable");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String data = rs.getString("M_Name").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (data.equalsIgnoreCase(methodology)) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM MethodologyTable WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            String data = rs.getString("M_Name").toLowerCase();
                            if (data.equalsIgnoreCase(methodology)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if (practice.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM PracticeTable");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String data = rs.getString("M_Name").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (data.equalsIgnoreCase(practice)) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM PracticeTable WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            String data = rs.getString("M_Name").toLowerCase();
                            if (data.equalsIgnoreCase(practice)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if (evidenceItem.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM EvidenceItemTable");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String data = rs.getString("ItemName").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (data.equalsIgnoreCase(evidenceItem)) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM EvidenceItemTable WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            String data = rs.getString("ItemName").toLowerCase();
                            if (data.equalsIgnoreCase(evidenceItem)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
            if (researchDesign.length() > 0 && searching) {
                if (result.isEmpty()) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM ResearchDesignTable");
                    System.out.println(stmt);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String data = rs.getString("R_Name").toLowerCase();
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (data.equalsIgnoreCase(practice)) {
                            result.add(rID);
                        }
                    }
                } else {
                    HashSet<Integer> searchingSet = new HashSet<>();
                    for (Integer i : result) {
                        searchingSet.add(i);
                    }
                    for (Integer i : searchingSet) {
                        boolean found = false;
                        String currentID = "" + i;
                        stmt = myDB.getConn().prepareStatement("SELECT * FROM ResearchDesignTable WHERE ArticleID = ?");
                        stmt.setString(1, currentID);
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            String data = rs.getString("R_Name").toLowerCase();
                            if (data.equalsIgnoreCase(practice)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            result.remove(i);
                        }
                    }
                    if (result.isEmpty()) {
                        searching = false;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: \n" + ex);
        }
    }

    private void orSearch() {
        HashSet<String> wordSet = new HashSet<>();
        ResultSet rs;
        try {
            if (title.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                StringTokenizer st = new StringTokenizer(title, " ");
                while (st.hasMoreTokens()) {
                    wordSet.add(st.nextToken().toLowerCase());
                }
                while (rs.next()) {
                    String data = rs.getString("Title").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    for (String word : wordSet) {
                        if (data.contains(word)) {
                            result.add(rID);
                        }
                    }
                }
            }
            if (author.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                StringTokenizer st = new StringTokenizer(author, " ");
                while (st.hasMoreTokens()) {
                    wordSet.add(st.nextToken().toLowerCase());
                }
                while (rs.next()) {
                    String data = rs.getString("Authors").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    for (String word : wordSet) {
                        if (data.contains(word)) {
                            
                            result.add(rID);
                            System.out.println(result+"===="+ data + "=Contains="+word);
                        }
                    }
                }
            }
            if (journal.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString("Journal").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    if (data.equalsIgnoreCase(journal)) {
                        result.add(rID);
                    }
                }
            }
            if (year1.length() > 0 || year2.length() > 0) {
                int yearBegin = 0, yearEnd = 0;
                try {
                    yearBegin = Integer.parseInt(year1);
                } catch (NumberFormatException e) {
                    yearBegin = 0;
                }
                try {
                    yearEnd = Integer.parseInt(year2);
                } catch (NumberFormatException e) {
                    yearEnd = 0;
                }
                if (yearEnd < yearBegin) {
                    yearBegin = yearEnd;
                }

                if (yearBegin > 0 && yearBegin <= yearEnd) {
                    stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                    System.out.println(stmt);
                    System.out.println("from " + yearBegin + " to " + yearEnd);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        int data = Integer.parseInt(rs.getString("YearOfPublish"));
                        int rID = Integer.parseInt(rs.getString("ArticleID"));
                        if (yearBegin <= data && data <= yearEnd) {
                            result.add(rID);
                        }
                    }
                }
            }
            if (level.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString("ResearchLv").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    if (data.equalsIgnoreCase(level)) {
                        result.add(rID);
                    }
                }
            }
            if (credibility != null) {
                int rating = Integer.parseInt(credibility);
                stmt = myDB.getConn().prepareStatement("SELECT * FROM AllArticles");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    double avgRating = getRating(rs.getString("ArticleID"));
                    if (avgRating > rating) {
                        result.add(rID);
                    }
                }
            }
            if (methodology.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM MethodologyTable");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString("M_Name").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    if (data.equalsIgnoreCase(level)) {
                        result.add(rID);
                    }
                }
            }
            if (practice.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM PracticeTable");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString("M_Name").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    if (data.equalsIgnoreCase(practice)) {
                        result.add(rID);
                    }
                }
            }
            if (evidenceItem.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM EvidenceItemTable");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString("ItemName").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    if (data.equalsIgnoreCase(practice)) {
                        result.add(rID);
                    }
                }
            }
            if (researchDesign.length() > 0) {
                stmt = myDB.getConn().prepareStatement("SELECT * FROM ResearchDesignTable");
                System.out.println(stmt);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String data = rs.getString("R_Name").toLowerCase();
                    int rID = Integer.parseInt(rs.getString("ArticleID"));
                    if (data.equalsIgnoreCase(practice)) {
                        result.add(rID);
                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error: \n" + ex);
        }
    }
}
