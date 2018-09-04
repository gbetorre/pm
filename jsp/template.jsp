<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>HTML5, for Fun &amp; Work</title>
</head>
<body>
<h1>over here: <c:out value="${pageContext.request.queryString}" escapeXml="false" /></h1>
<a href="/almalaurea?q=1">link</a><br />
<h1>over there</h1>
<p>FetchSize is: <c:out value="${requestScope.courses.size()}" /></p>
<dl>
<c:forEach var="oi" items="${requestScope.courses}" varStatus="loop">
  <dt><c:out value="${oi.misura}" /></dt>
  <dd><c:out value="${oi.codiceInsegnamento}" /></dd>
  <dd><c:out value="${oi.codiceCs}" /></dd>
</c:forEach>
</dl>
</body>
</html>