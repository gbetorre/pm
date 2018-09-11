<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>HTML5, for Fun &amp; Work</title>
  </head>
  <body>
  <h1>Duplicati: <c:out value="${pageContext.request.queryString}" escapeXml="false" /></h1>
  <p>FetchSize is: <c:out value="${requestScope.duplicati.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Crediti Totali</th>
          <th>Crediti Lezione</th>
          <th>Ore Totali</th>
          <th>Ore Lezione</th>
          <th>Cognome Docente</th>
          <th>Nome Docente</th>
          <th>CF Docente</th>
          <th>Coord.</th>
          <th>Data Inizio Perido</th>
          <th>Data Fine Periodo</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.duplicati}" varStatus="loop">
        <tr>
          <td><c:out value="${oi.id}" /></td>
          <td><c:out value="${oi.codiceCdSUGOV}" /></td>
          <td><c:out value="${oi.nomeInsegnamento}" /></td>
          <td><c:out value="${oi.codiceADUGOV}" /></td>
          <td><c:out value="${oi.creditiTotali}" /></td>
          <td><c:out value="${oi.creditiLezione}" /></td>
          <td><c:out value="${oi.ore}" /></td>
          <td><c:out value="${oi.oreLezione}" /></td>
          <td><c:out value="${oi.cognomeDocente}" /></td>
          <td><c:out value="${oi.nomeDocente}" /></td>
          <td><c:out value="${oi.codiceFiscaleDocente}" /></td>
          <td><c:out value="${oi.coordinatore}" /></td>
          <td><c:out value="${oi.inizioPerDid}" /></td>
          <td><c:out value="${oi.finePerDid}" /></td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
    <h1>Elenco completo</h1>
    <a href="/almalaurea?q=1">link</a><br />
    <p>FetchSize is: <c:out value="${requestScope.elenco.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Crediti Totali</th>
          <th>Crediti Lezione</th>
          <th>Ore Totali</th>
          <th>Ore Lezione</th>
          <th>Cognome Docente</th>
          <th>Nome Docente</th>
          <th>CF Docente</th>
          <th>Coord.</th>
          <th>Data Inizio Perido</th>
          <th>Data Fine Periodo</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.elenco}" varStatus="loop">
        <tr>
          <td><c:out value="${oi.id}" /></td>
          <td><c:out value="${oi.codiceCdSUGOV}" /></td>
          <td><c:out value="${oi.nomeInsegnamento}" /></td>
          <td><c:out value="${oi.codiceADUGOV}" /></td>
          <td><c:out value="${oi.creditiTotali}" /></td>
          <td><c:out value="${oi.creditiLezione}" /></td>
          <td><c:out value="${oi.ore}" /></td>
          <td><c:out value="${oi.oreLezione}" /></td>
          <td><c:out value="${oi.cognomeDocente}" /></td>
          <td><c:out value="${oi.nomeDocente}" /></td>
          <td><c:out value="${oi.codiceFiscaleDocente}" /></td>
          <td><c:out value="${oi.coordinatore}" /></td>
          <td><c:out value="${oi.inizioPerDid}" /></td>
          <td><c:out value="${oi.finePerDid}" /></td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>
</html>