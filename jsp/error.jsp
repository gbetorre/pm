<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isErrorPage="true" %>
<%@ page session="false" %>
<c:set var="isErrorPage" value="true" scope="request" />
<c:set var="messaggio" value="${requestScope.messaggio}" scope="page" />
<html>
  <head>
    <title>
      Errore Folle
    </title>

 </body>
</html>
