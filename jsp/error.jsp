<%@ taglib prefix="uol" uri="uolTagLib.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isErrorPage="true" %>
<%@ page session="false" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="javax.mail.*" %>
<%@ page import="javax.mail.internet.*" %>
<%@ page import="java.io.*" %>
<%@ page import="it.univr.di.uol.*" %>
<%@ page import="it.univr.di.uol.bean.*" %>
<jsp:useBean id="dataUrl" class="it.univr.di.uol.DataUrl" scope="request"/>
<jsp:useBean id="urlErrori" class="java.util.HashSet" scope="application" />
<c:set var="isErrorPage" value="true" scope="request" />
<c:set var="messaggio" value="${requestScope.messaggio}" scope="page" />
<%
        Lingue.setLanguages(request);
        // Costruisce qui il valore del <flagsUrl> per cambiare lingua ai contenuti
        String flagsUrl = (request.getAttribute("queryString") != null) ? ((String) request.getAttribute("queryString") ): request.getQueryString();
        if (flagsUrl == null) 
            flagsUrl = new String();                        // In error.jsp QueryString è nulla
        flagsUrl = flagsUrl.replaceAll("&?lang=[^&]*", ""); // Toglie eventuale "lang=xx"
        flagsUrl = flagsUrl.replaceAll("&", "&amp;");       // Sostituisce tutti gli & con &amp;
        flagsUrl = (flagsUrl.length()>0) ? "main?" + flagsUrl + "&amp;lang=" : "?lang="; // Su fol il valore dell'ultima assegnazione sarebbe : "main?lang="
        // Setta nella request il valore del <flagsUrl>
        request.setAttribute("flagsUrl", flagsUrl);
%>
<c:if test="${!empty param.lang}">
  <fmt:setLocale value="${param.lang}" scope="request" />
  <c:set var="paramLang" value="?lang=${param.lang}" scope="request" />
</c:if>
<html>
  <head>
    <title>
      <fmt:message key="ErroreFol" />   
    </title>
	  <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
	  <base href="${baseUrl}">
    <!--webfonts-->
    <link href='http://fonts.googleapis.com/css?family=Lato|Sorts+Mill+Goudy|Montserrat' rel='stylesheet' type='text/css'>
    <!-- Bootstrap core CSS-->
    <link href="style/bootstrap.min.css" rel="stylesheet">
    <!-- Flaticon-educative Fonts CSS-->
    <link href="style/flaticon_educative.css" rel="stylesheet">  

    <!--  FONT-AWESOME ICON FONTS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.2/css/font-awesome.min.css">

	  <link href="style/dol.css" rel="stylesheet">
    <link href="style/header.css" rel="stylesheet">
    <link href="style/home-dol.css" rel="stylesheet">
    <link href="style/responsive-slider.css" rel="stylesheet">
    <%-- File css specifico del dipartimento in base all'acronimo --%>
    <link href="style/dol-${acronimoDipartimento}.css" rel="stylesheet">
	  <link rel="SHORTCUT ICON" href="image/logoUnivr.ico">
	  <meta name="robots" content="noindex,nofollow" />

    <!--[if lte IE 6]></base><![endif]-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/custom-dol.js"></script>
    <%-- File javascript specifico del dipartimento in base all'acronimo --%>
    <script src="js/custom-dol-${acronimoDipartimento}.js"></script>
  </head>
  <body>
		<!--  Header  -->
  <%@ include file="/jsp/header.jspf"%>
  
  <!--  Titolo -->
  <div id="titoloPagina">
    <div class="container">
      <h1 class="h1-small"><fmt:message key="PaginaNonEsist" /></h1>
    </div>
  </div>
  <div class="section" id="pageContent">
    <div class="container"> 
      <div class="row">
        <!--  Colonna SX Menu -->
        <div class="col-sm-3 hidden-xs" id="colonna-menu">
          <h2 class="label">
            <a href="${paramLang}">Home page</a>
          </h2>         
        </div>
        

        <div class="col-sm-9" id="contenutoPagina">
				    <h2><fmt:message key="RichiestaNonCorr" /></h2>
				    <c:choose>
	            <c:when test="${!empty messaggio}">
							  <c:choose>
							    <c:when test="${messaggio.class.isInstance('')}"> <%-- 'messaggio' e' una Stringa --%>
                    <h3>
                      <c:out value="${messaggio}" escapeXml="false" />
                    </h3>
							    </c:when>
							    <c:otherwise> <%-- 'messaggio' e' una VoceMenuBean --%>
                    <c:if test="${!empty messaggio.nome}">
                    <br />
	                    <h3>
	                      <c:out value="${messaggio.nome}" escapeXml="false" />
	                    </h3>
                    </c:if>
							      <c:choose>
							        <c:when test="${not empty messaggio.url}">
								        <p>
										      <a href="${messaggio.url}">
										        <c:out value="${messaggio.labelWeb}" escapeXml="false" /></a>
                          (<span id="countdown">10</span>)
										    </p>
									      <script type="text/javascript">
										      var ss = 10;
										      function countdown() {
											      ss = ss-1;
											      if (ss<0) {
  											      window.location="${messaggio.url}".replace("&amp;", "&");
											      }
											      else {
												      document.getElementById("countdown").innerHTML=ss;
												      window.setTimeout("countdown()", 1000);
											      }
										      }
										      // Avvia il countdown
										      countdown();
									      </script>								
							        </c:when>
							        <c:otherwise>
  							        <c:out value="${messaggio.labelWeb}" escapeXml="false" />
							        </c:otherwise>
							      </c:choose>
							    </c:otherwise>
							  </c:choose>
	            </c:when>
	            <c:otherwise>
                <p><fmt:message key="CausaErrore" /></p>
	            </c:otherwise>
				    </c:choose>
				    <p><fmt:message key="ScuseErrore" /></p><br />
            <p>
              <fmt:message key="PaginaErrata" />:
              <span style="background:#ff0">
                <%= request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/?" + request.getQueryString() %>
              </span>
            </p>
            <% if (!("/?" + request.getQueryString()).equals("/?ent=" + request.getParameter("ent"))) { %>
            <p>
              <uol:lang getLocalizedText="457" defaultValue="Forse cercavi:" />
              <a href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/?ent=" + request.getParameter("ent") %>">
                <%= request.getScheme() + "://" + request.getServerName() + request.getContextPath() + "/?ent=" + request.getParameter("ent") %>
              </a>
            </p>
            <% } %>
			  </div>

<%      if (exception!=null) {
            StringWriter writer = new StringWriter();
            exception.printStackTrace(new PrintWriter(writer));
            String stack = writer.toString();
            if (stack.indexOf("Main.java") != -1) {
                stack = stack.substring( 0, stack.indexOf("Main.java") );
            }
            stack.replaceAll("\n","\n<br>");
            stack.replaceAll("\"","'");
%>
          <input id="stack" type="hidden" name="stack" value="<%=stack%>">
<%      }%>

    </div>
  </div>
  </div>
  <%-- Footer --%>
  <%@ include file="/jsp/footer.jspf"%>
 </body>
</html>
