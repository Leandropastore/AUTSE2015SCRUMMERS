<%-- 
    Document   : upload
    Created on : May 10, 2015, 1:10:13 PM
    Author     : Andy Li
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Upload Article</title>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.23.custom.css" rel="stylesheet" />
        <link type="text/css" href="css/common.css" rel="stylesheet"/>


    </head>
    <body>
        <div align="center" id="layout">

            <div  id="bodytopbottombox">
                <div>
                    <form>

                            <FORM ACTION="UploadServlet">
                                <div style="text-align: justify">
                                    <label title="lbl_article_link">Article Title: </label><br />
                                    <input type="text" name="title" value=""/><br /><br />
                                    <label title="lbl_article_link">Article Link: </label><br />
                                    <input type="text" name="location" value=""/><br /><br />
                                </div>
                                <div style="text-align: center">
                                    <br />
                                    <input type="submit" name="btnSend" value="Send"/>&nbsp;
                                    <button type="reset" name="btnClean">Clean</button>
                            </FORM>
                    </form>
                </div>

            </div>
    </body>
</html>
