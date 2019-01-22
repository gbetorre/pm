<%@ page contentType="text/html;" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="head" value="${requestScope.header}" scope="page" />
<c:set var="foot" value="${requestScope.footer}" scope="page" />
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
    <script src="http://code.jquery.com/jquery-3.3.1.js"></script>
    <script>
      window.jQuery || document.write('<script src="${initParam.urlDirectoryScrypt}/jquery/jquery-3.3.1.js"><\/script>');
    </script>
    <!-- Include jQuery validate Form elements from CDN or from local hosted copy -->
    <script src="<c:out value="${initParam.urlDirectoryScript}" />jquery/plugin/jquery.validate-1.17.0.js"></script>
    <script src="<c:out value="${initParam.urlDirectoryScript}" />jquery/plugin/additional-methods-1.17.0.js"></script>
    <script src="<c:out value="${initParam.urlDirectoryScript}" />jquery/plugin/jquery-ui.js"></script>
    <script src="<c:out value="${initParam.urlDirectoryScript}" />jquery/plugin/jquery.textarea_autosize.js"></script>
    <!-- <script src="<c:out value="${initParam.urlDirectoryScript}" />bootstrap/bootstrap.js"></script> 
    <script src="<c:out value="${initParam.urlDirectoryScript}" />bootstrap/plugin/bootstrap.bundle.js"></script> -->
    <script src="<c:out value="${initParam.urlDirectoryScript}" />bootstrap/plugin/grayscale.js"></script>
    <script src="<c:out value="${initParam.urlDirectoryScript}" />cookie/cookieconsent.min.js"></script>
    <!-- Begin Cookie Consent plugin by Silktide - http://silktide.com/cookieconsent -->
    <script>
      window.addEventListener("load", function(){
      window.cookieconsent.initialise({
        "palette": {
          "popup": {
            "background": "#000"
          },
          "button": {
            "background": "#f1d600"
          }
        },
        "theme": "classic",
        "position": "bottom-right",
        "content": {
          "message": "Questo sito o gli strumenti terzi da questo utilizzati si avvalgono di cookie necessari al funzionamento ed utili alle finalità illustrate nella <a class=\"cc-link\" href=\"https://www.univr.it/it/privacy\" target=\"_blank\">cookie policy</a>. Se vuoi saperne di più o negare il consenso a tutti o ad alcuni cookie, consulta la <a class=\"cc-link\" href=\"https://www.univr.it/it/privacy\" target=\"_blank\">cookie policy</a>. Cliccando su \'Acconsento\' dai l\'autorizzazione all\'utilizzo dei cookie.",
          "dismiss": "Acconsento",
          "link": "Informazioni",
          "href": "https://www.univr.it/it/privacy"
        }
      })});
    </script>
    <!-- End Cookie Consent plugin -->
    <%--base href="${requestScope.baseHref}" /--%>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.css">
    <!-- Latest compiled CSS, just in case -->
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />bootstrap/bootstrap.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />jquery/jquery-ui.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />bootstrap/plugin/grayscale.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />pol.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />style.css" type="text/css" />
    <link rel="stylesheet" href="<c:out value="${initParam.urlDirectoryStili}" />cookie/cookieconsent.min.css" type="text/css" />
  </head>
  <body>
  <c:catch var="exception">
    <c:if test="${not empty head and head}">
    <!-- Header -->
    <div id="idHeader">
      <%@ include file="header.jspf"%>
    </div>
    </c:if>
    <!-- Corpo pagina -->
    <div class="page">
      <jsp:include page="${fileJsp}" />
    </div>
    <br /><br /><br /><br />
    <c:if test="${not empty foot and foot}">    
      <!-- Footer -->
      <footer>
        <%@ include file="footer.jspf" %>
      </footer>    
    </c:if>
  </c:catch>
  <c:out value="${exception}" />
    <script src="<c:out value="${initParam.urlDirectoryScript}" />pol.js"></script>
  </body>
</html>