<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title><c:out value="${requestScope.tP}" escapeXml="false" /></title>
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
    <meta name="language" content="Italian" />
    <meta name="description" content="${requestScope.advice}" />
    <meta name="creator" content="Universita' degli studi di Verona" />
    <meta name="author" content="Giovanroberto Torre, giovanroberto.torre@univr.it" />
    <link rev="made" href="giovanroberto.torre@univr.it" />
    <base href="${requestScope.baseHref}" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />bootstrap.css" type="text/css" />
  </head>
  <body>
    <!-- Header -->
    <div style="background:#ffff99">
      <%@ include file="header.jspf"%>
    </div>
    <!-- Corpo pagina -->
    <jsp:include page="${fileJsp}" />
    <!-- Footer -->
    <div style="background:#ffcccc">
    <%@ include file="footer.jspf" %>
    </div>
  </body>
</html>