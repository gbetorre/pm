<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>HTML5, for Fun &amp; Work</title>
  </head>
  <body>
  <ul>
  <li><a href="#merged">PERIODI UNITI</a></li>
  <li><a href="#samekey">STESSA CHIAVE PERIODO DIVERSO</a></li>
  <li><a href="#cleanduplicates">DUPLICATI PULITI</a></li>
  <li><a href="#doubleduplicates">DUPLICATI DOPPI</a></li>
  <li><a href="#butdiplicates">DUPLICATI TOLTI</a></li>
  <li><a href="#all">ELENCO COMPLETO</a></li>
  </ul>
      <!-- Vector -->
    <a name="merged" />
    <h1>PERIODI UNITI</h1>
    <p>FetchSize is: <c:out value="${requestScope.periodiUniti.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.periodiUniti}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td>
            <c:out value="${oi.codiceCdSUGOV}" />
            <c:out value="${oi.nomeInsegnamento}" />
            <c:out value="${oi.codiceADUGOV}" />
            <c:out value="${oi.cognomeDocente}" />
            <c:out value="${oi.nomeDocente}" />
            <c:out value="${oi.codiceFiscaleDocente}" />
<%--             <c:out value="${oi.coordinatore}" /> --%>
            <c:out value="${oi.inizioPerDid}" />
            <c:out value="${oi.finePerDid}" />
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  
      <!-- Vector -->
      <a name="samekey" />
    <h1>STESSA CHIAVE PERIODO DIVERSO</h1>
    <p>FetchSize is: <c:out value="${requestScope.doppi.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.doppi}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td>
            <c:out value="${oi.codiceCdSUGOV}" />
            <c:out value="${oi.nomeInsegnamento}" />
            <c:out value="${oi.codiceADUGOV}" />
            <c:out value="${oi.cognomeDocente}" />
            <c:out value="${oi.nomeDocente}" />
            <c:out value="${oi.codiceFiscaleDocente}" />
<%--             <c:out value="${oi.coordinatore}" /> --%>
            <c:out value="${oi.inizioPerDid}" />
            <c:out value="${oi.finePerDid}" />
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  
      <!-- Vector -->
      <a name="cleanduplicates" />
    <h1>DUPLICATI PULITI</h1>
    <p>FetchSize is: <c:out value="${requestScope.doppioni.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.doppioni}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td>
            <c:out value="${oi.codiceCdSUGOV}" />
            <c:out value="${oi.nomeInsegnamento}" />
            <c:out value="${oi.codiceADUGOV}" />
            <c:out value="${oi.cognomeDocente}" />
            <c:out value="${oi.nomeDocente}" />
            <c:out value="${oi.codiceFiscaleDocente}" />
<%--             <c:out value="${oi.coordinatore}" /> --%>
            <c:out value="${oi.inizioPerDid}" />
            <c:out value="${oi.finePerDid}" />
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  <%--
  <h1>Duplicati puliti: <c:out value="${requestScope.doppioni.size()}" /></h1>
  <ul>
  <c:forEach var="oi" items="${requestScope.doppioni}" varStatus="loop">
    <li>
       ${loop.index} -  
      <c:out value="${oi.id}" />
      <c:out value="${oi.codiceCdSUGOV}" />
      <c:out value="${oi.nomeInsegnamento}" />
      <c:out value="${oi.codiceADUGOV}" />
<%--       <c:out value="${oi.creditiTotali}" /> --%>
<%--       <c:out value="${oi.creditiLezione}" /> --%>
<%--       <c:out value="${oi.ore}" /> --%>
<%--       <c:out value="${oi.oreLezione}" /> --%>
<%--       <c:out value="${oi.cognomeDocente}" />
      <c:out value="${oi.nomeDocente}" />
      <c:out value="${oi.codiceFiscaleDocente}" />
      <c:out value="${oi.coordinatore}" />
      <c:out value="${oi.inizioPerDid}" />
      <c:out value="${oi.finePerDid}" />
    </li>
  </c:forEach>
  </ul>
  --%>
  <a name="doubleduplicates" />
    <h1>DUPLICATI DOPPI</h1>
    <p>FetchSize is: <c:out value="${requestScope.duplicati.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.duplicati}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td>
            <c:out value="${oi.codiceCdSUGOV}" />
            <c:out value="${oi.nomeInsegnamento}" />
            <c:out value="${oi.codiceADUGOV}" />
            <c:out value="${oi.cognomeDocente}" />
            <c:out value="${oi.nomeDocente}" />
            <c:out value="${oi.codiceFiscaleDocente}" />
<%--             <c:out value="${oi.coordinatore}" /> --%>
            <c:out value="${oi.inizioPerDid}" />
            <c:out value="${oi.finePerDid}" />
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  
  <%--
  <h1>Duplicati: <c:out value="${pageContext.request.queryString}" escapeXml="false" /></h1>
  <p>FetchSize is: <c:out value="${requestScope.duplicati.size()}" /></p>
  <ul>
    <c:forEach var="oi" items="${requestScope.duplicati}" varStatus="loop">
    <li>
      ${loop.index} -
      <c:out value="${oi.id}" />
      <c:out value="${oi.codiceCdSUGOV}" />
      <c:out value="${oi.nomeInsegnamento}" />
      <c:out value="${oi.codiceADUGOV}" />
<%--       <c:out value="${oi.creditiTotali}" /> --%>
<%--       <c:out value="${oi.creditiLezione}" /> --%>
<%--       <c:out value="${oi.ore}" /> --%>
<%--       <c:out value="${oi.oreLezione}" /> --%>
<%--       <c:out value="${oi.cognomeDocente}" />
      <c:out value="${oi.nomeDocente}" />
      <c:out value="${oi.codiceFiscaleDocente}" />
      <c:out value="${oi.coordinatore}" />
      <c:out value="${oi.inizioPerDid}" />
      <c:out value="${oi.finePerDid}" />
    </li>
    </c:forEach>
  </ul>
    <!-- HashMap -->
    <h1>Elenco HashMap</h1>
    <c:set var="list" value="${requestScope.lista}" scope="page" />
    <p>FetchSize is: <c:out value="${list.size()}" /></p>
    <c:if test="${list.size() gt 0}">
    <c:set var= "keys" value ="${list.keySet()} " scope ="page" />
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
        <c:forEach var="key" items="${keys}" varStatus="loop">
        <p><c:out value="${key}"></c:out></p>
        <p><c:set var="oi2" value="${list.get(key)}" scope="page" /></p>
        <c:forEach var="ad" items="${list.get(key)}"><c:out value="==${ad}=="></c:out></c:forEach>
        <c:forEach var="oi" items="${list.values()}">
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
        
        </c:forEach>
      </tbody>
    </table>    
    </c:if>
 --%>
    <!-- Vector -->
    <a name="butdiplicates" />
    <h1>Tolti duplicati</h1>
    <p>FetchSize is: <c:out value="${requestScope.senzaDuplicati.size()}" /></p>
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
        <c:forEach var="ad" items="${requestScope.senzaDuplicati}" varStatus="loop">
        <tr>
          <td><c:out value="${ad.id}" /></td>
          <td><c:out value="${ad.codiceCdSUGOV}" /></td>
          <td><c:out value="${ad.nomeInsegnamento}" /></td>
          <td><c:out value="${ad.codiceADUGOV}" /></td>
          <td><c:out value="${ad.creditiTotali}" /></td>
          <td><c:out value="${ad.creditiLezione}" /></td>
          <td><c:out value="${ad.ore}" /></td>
          <td><c:out value="${ad.oreLezione}" /></td>
          <td><c:out value="${ad.cognomeDocente}" /></td>
          <td><c:out value="${ad.nomeDocente}" /></td>
          <td><c:out value="${ad.codiceFiscaleDocente}" /></td>
          <td><c:out value="${ad.coordinatore}" /></td>
          <td><c:out value="${ad.inizioPerDid}" /></td>
          <td><c:out value="${ad.finePerDid}" /></td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
    <!-- Vector -->
    <a name="all" />
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
        <c:forEach var="ad" items="${requestScope.elenco}" varStatus="loop">
        <tr>
          <td><c:out value="${ad.id}" /></td>
          <td><c:out value="${ad.codiceCdSUGOV}" /></td>
          <td><c:out value="${ad.nomeInsegnamento}" /></td>
          <td><c:out value="${ad.codiceADUGOV}" /></td>
          <td><c:out value="${ad.creditiTotali}" /></td>
          <td><c:out value="${ad.creditiLezione}" /></td>
          <td><c:out value="${ad.ore}" /></td>
          <td><c:out value="${ad.oreLezione}" /></td>
          <td><c:out value="${ad.cognomeDocente}" /></td>
          <td><c:out value="${ad.nomeDocente}" /></td>
          <td><c:out value="${ad.codiceFiscaleDocente}" /></td>
          <td><c:out value="${ad.coordinatore}" /></td>
          <td><c:out value="${ad.inizioPerDid}" /></td>
          <td><c:out value="${ad.finePerDid}" /></td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
   
  </body>
</html>