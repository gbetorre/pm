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
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <!-- Latest compiled CSS, just in case -->
    <%--base href="${requestScope.baseHref}" /--%>
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />pol.css" type="text/css" />
    <%--link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />bootstrap.css" type="text/css" />--%>
    <!-- <link href="<c:out value="${initParam.urlDirectoryStili}" />style.css" rel="stylesheet" type="text/css" />  -->
    <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
    <script>
      window.jQuery || document.write('<script src="javascript/jquery-1.11.3.min.js"><\/script>');
    </script>
  </head>
  <body>
    <!-- Header -->
    <div style="background:#ccff66">
      <%@ include file="header.jspf"%>
    </div>
    <!-- Corpo pagina -->
    <jsp:include page="${fileJsp}" />
    <!-- Footer -->
    <div style="background:#99ff99">
    <%@ include file="footer.jspf" %>
    </div>
    <script>
      function modify(){
        $('textarea').prop('readonly', false);
        //$('#aName').prop('readonly', false);
        //$('#pcv-obiettivi').prop('readonly', false);
        //$('#pcv-minacce').prop('readonly', false);
        $('input[type=\'text\']').prop('readonly', false);
        $('input[type=\'checkbox\']').prop('disabled', false);
        $('select').prop('disabled', false);
        $('button').prop('disabled', false);
      }
    </script>
  </body>
</html>