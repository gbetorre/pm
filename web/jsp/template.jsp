<%@ page contentType="text/html;" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title><c:out value="${requestScope.tP}" escapeXml="false" /></title>
    <meta charset="utf-8" />
    <meta name="language" content="Italian" />
    <meta name="description" content="${requestScope.advice}" />
    <meta name="creator" content="Universita' degli studi di Verona" />
    <meta name="author" content="Giovanroberto Torre, giovanroberto.torre@univr.it" />
    <link rev="made" href="giovanroberto.torre@univr.it" />
    <meta http-equiv='cache-control' content='no-cache'>
    <!-- Include jQuery from CDN or from local hosted copy --> 
    <script src="http://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script>
      window.jQuery || document.write('<script src="${initParam.urlDirectoryScrypt}/jquery-3.3.1.js"><\/script>');
    </script>
    <!-- Include jQuery validate Form elements from CDN or from local hosted copy -->
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.17.0/jquery.validate.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.17.0/additional-methods.js"></script>
    <script src="<c:out value="${initParam.urlDirectoryScript}" />jquery-ui.js"></script>
    <%--base href="${requestScope.baseHref}" /--%>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.css">
    <!-- Latest compiled CSS, just in case -->
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />bootstrap.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />pol.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />style.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />jquery-ui.css" type="text/css" />
  </head>
  <body>
    <!-- Header -->
    <div id="idHeader">
      <%@ include file="header.jspf"%>
    </div>
    <!-- Corpo pagina -->
    <div class="page">
      <jsp:include page="${fileJsp}" />
    </div>
    <!-- Footer -->
    <div id="idFooter" class="footer">
    <%@ include file="footer.jspf" %>
    </div>
    <script>
      function modify(){
        $('textarea').prop('readonly', false);
        $('input[type=\'text\']').prop('readonly', false);
        $('input[type=\'checkbox\']').prop('disabled', false);
        $('select').prop('disabled', false);
        $('button').prop('disabled', false);
      }
    </script>
  </body>
</html>