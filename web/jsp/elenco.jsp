<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>HTML5, for Fun &amp; Work</title>
  </head>
  <body>
  <ul>
  <li><a href="#toAdd">LISTA PULITA DUPLICATI DA AGGIUNGERE</a></li>
  <li><a href="#merged">PERIODI UNITI</a></li>
  <li><a href="#samekey">STESSA CHIAVE PERIODO DIVERSO</a></li>
  <li><a href="#cleanduplicates">DUPLICATI PULITI</a></li>
  <li><a href="#doubleduplicates">DUPLICATI DOPPI</a></li>
  <li><a href="#butdiplicates">DUPLICATI TOLTI</a></li>
  <li><a href="#all">ELENCO COMPLETO</a></li>
  </ul>
  
    <!-- Vector -->
    <a name="allbutmedical" />
    <h1>Elenco AD solo di Medicina</h1>
    <p>FetchSize is: <c:out value="${requestScope.adMedicina.size()}" /></p>
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
        <c:forEach var="ad" items="${requestScope.adMedicina}" varStatus="loop">
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
    <a name="toJoin" />
    <h1>DUPLICATI RESIDUI PERIODI UNITI</h1>
    <p>FetchSize is: <c:out value="${requestScope.duplicatiResiduiPeriodiUniti.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.duplicatiResiduiPeriodiUniti}" varStatus="loop">
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
    <a name="residual" />
    <h1>DUPLICATI RESIDUI</h1>
    <p>FetchSize is: <c:out value="${requestScope.duplicatiResidui.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.duplicatiResidui}" varStatus="loop">
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
    <a name="toAdd" />
    <h1>DUPLICATI DA AGGIUNGERE</h1>
    <p>FetchSize is: <c:out value="${requestScope.duplicatiDaAggiungere.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.duplicatiDaAggiungere}" varStatus="loop">
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
            <c:out value="${oi.coordinatore}" /> 
            <c:out value="${oi.inizioPerDid}" />
            <c:out value="${oi.finePerDid}" />
            <c:out value="${oi.creditiTotali}" />
            <c:out value="${oi.creditiLezione}" />
          </td>
        </tr>
        </c:forEach>
      </tbody>
    </table>
  
    <!-- Vector -->
    <a name="butdiplicates" />
    <h1>Non di medicina e CFU &gt; 3</h1>
    <p>FetchSize is: <c:out value="${requestScope.candidati.size()}" /></p>
    <table border="1" style="background:#ffcccc;">
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
        <c:forEach var="ad" items="${requestScope.candidati}" varStatus="loop">
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