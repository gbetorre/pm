<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <h3><cite>Sommario AD Semplici</cite></h3>
  <ul>
  <h4>Area NON Medica</h4>
  <li><a href="?q=val#toAdd">LISTA PULITA DUPLICATI DA AGGIUNGERE</a></li>
  <li><a href="?q=val#medicalduplicates">LISTA DUPLICATI SOLO DI MEDICINA</a></li>
  <li><a href="?q=val#medicalduplicatescleaned">CAMPIONI TENUTI DAI DUPLICATI SCARTANDO I DOPPIONI</a></li>
  <li><a href="?q=val#merged">NON MY E DEPURATE DAI DOPPI MA CON I PERIODI ANCORA DA UNIRE</a> [${requestScope.ripuliti.size()}]</li>
  <li><a href="?q=val#duplicates">NON MY MULTIPLE DA DEPURARE</a> [${requestScope.duplicati.size()}]</li>
  <li><a href="?q=val#toExamine">NON MY E CFU &ge; 4</a> [${requestScope.candidati.size()}]</li>
  <br />
  <h4>Area Medica</h4>
  <li><a href="?q=val#medicalFinal">LISTA FINALE AD DI MEDICINA DA ACCENDERE</a> [${requestScope.adMYFinal.size()}]</li>
  <li><a href="?q=val#medicalToMerge">DUPLICATI DI AREA MEDICA CON PERIODI DA UNIRE</a> [${requestScope.adMYDaUnire.size()}]</li>
  <li><a href="?q=val#medicalsamples">RIGHE DI AREA MEDICA DA TENERE, MA CON I PERIODI ANCORA DA UNIRE</a> [${requestScope.ripulitiMY.size()}]</li>
  <li><a href="?q=val#medicalduplicates">DUPLICATI DI MEDICINA E SCIENZE MOTORIE</a> [${requestScope.duplicatiMY.size()}]</li>
  <li><a href="?q=val#allmedical">SOLO DI AREA MEDICA E SCIENZE MOTORIE</a> [${requestScope.adMY.size()}]</li>
  <br />
  <h4>Elenco Completo</h4>
  <li><a href="?q=val#all">ELENCO COMPLETO</a> [${requestScope.elenco.size()}]</li>
  </ul>
  <hr />
    

    
      <!-- Vector -->
      <a name="final" />
      <hr /><br />
    <h1>Lista definitiva di AD DA ACCENDERE</h1>
    <p>FetchSize is: <c:out value="${requestScope.adFinal.size()}" /></p>
    <table border="1" style="background:#d7ffd7">
      <thead>
        <tr>
        <tr>
          <th>Riga</th>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.adFinal}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index + 1}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td><c:out value="${oi.codiceCdSUGOV}" /></td>
          <td><c:out value="${oi.nomeInsegnamento}" /></td>
          <td><c:out value="${oi.codiceADUGOV}" /></td>
          <td><c:out value="${oi.discriminante}" /></td>
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

  <a name="toMerge" />
  <hr /><br />
    <h1>Duplicati di Area NON Medica/Motorie con periodi da unire</h1>
    <p>FetchSize is: <c:out value="${requestScope.adDaUnire.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.adDaUnire}" varStatus="loop">
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
  
    <!-- TUTTE AD NON MEDICHE CAMPIONATE, cioè tenuta una sola di 2 o 2+ -->
    <a name="samples" />
    <hr /><br />
    <h1>
      Righe di Area NON Medica/Motorie da tenere 
      (ripulite dalle occorrenze doppie, triple, etc.) 
      - ma con i periodi ancora da unire!
    </h1>
    <p>FetchSize is: <c:out value="${requestScope.ripuliti.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>Riga</th>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
        <c:forEach var="oi" items="${requestScope.ripuliti}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index + 1}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td><c:out value="${oi.codiceCdSUGOV}" /></td>
          <td><c:out value="${oi.nomeInsegnamento}" /></td>
          <td><c:out value="${oi.codiceADUGOV}" /></td>
          <td><c:out value="${oi.discriminante}" /></td>
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
  
    <!-- TUTTE AD NON MEDICHE DOPPIE, DA CAMPIONARE -->
    <a name="duplicates" />
    <hr /><br />
    <h1>AD di Area non Medica, doppie o multiple (triple, quadruple, etc.) - <cite style="background:#00ccff">da depurare</cite></h1>
    <p>FetchSize is: <c:out value="${requestScope.duplicati.size()}" /></p>
    <table border="1" style="background:#00ccff">
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
            <c:out value="${oi.discriminante}" />
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
      <a name="medicalFinal" />
      <hr /><br />
    <h1>Lista definitiva di AD di area Medica/Motorie DA ACCENDERE</h1>
    <p>FetchSize is: <c:out value="${requestScope.adMYFinal.size()}" /></p>
    <table border="1" style="background:#d7ffd7">
      <thead>
        <tr>
        <tr>
          <th>Riga</th>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.adMYFinal}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index + 1}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td><c:out value="${oi.codiceCdSUGOV}" /></td>
          <td><c:out value="${oi.nomeInsegnamento}" /></td>
          <td><c:out value="${oi.codiceADUGOV}" /></td>
          <td><c:out value="${oi.discriminante}" /></td>
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

  <a name="medicalToMerge" />
  <hr /><br />
    <h1>Duplicati di Area Medica/Motorie con periodi da unire</h1>
    <p>FetchSize is: <c:out value="${requestScope.adMYDaUnire.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.adMYDaUnire}" varStatus="loop">
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
  
    <!-- TUTTE AD MEDICHE CAMPIONATE, cioè tenuta una sola di 2 o 2+ -->
    <a name="medicalsamples" />
    <hr /><br />
    <h1>
      Righe di Area Medica/Motorie da tenere 
      (ripulite dalle occorrenze doppie, triple, etc.) 
      - ma con i periodi ancora da unire!
    </h1>
    <p>FetchSize is: <c:out value="${requestScope.ripulitiMY.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>Riga</th>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
        <c:forEach var="oi" items="${requestScope.ripulitiMY}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index + 1}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td><c:out value="${oi.codiceCdSUGOV}" /></td>
          <td><c:out value="${oi.nomeInsegnamento}" /></td>
          <td><c:out value="${oi.codiceADUGOV}" /></td>
          <td><c:out value="${oi.discriminante}" /></td>
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
  
    <!-- TUTTE AD MEDICHE DOPPIE, DA CAMPIONARE -->
    <a name="medicalduplicates" />
    <hr /><br />
    <h1>AD di Area Medica, doppie o multiple (triple, quadruple, etc.) - <cite style="background:#00ccff">da depurare</cite></h1>
    <p>FetchSize is: <c:out value="${requestScope.duplicatiMY.size()}" /></p>
    <table border="1" style="background:#00ccff">
      <thead>
        <tr>
          <th>N.</th>
          <th>Id</th>
          <th>Key</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="oi" items="${requestScope.duplicatiMY}" varStatus="loop">
        <tr>
          <td><c:out value="${loop.index}" /></td>
          <td><c:out value="${oi.id}" /></td>
          <td>
            <c:out value="${oi.codiceCdSUGOV}" />
            <c:out value="${oi.nomeInsegnamento}" />
            <c:out value="${oi.codiceADUGOV}" />
            <c:out value="${oi.discriminante}" />
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
  
    <!-- TUTTE AD AREA SCIENZE VITA E SALUTE DA VAGLIARE -->
    <a name="allmedical" />
    <hr /><br />
    <h1>Elenco AD solo di Area Scienze della Vita e della Salute (Medicina e Scienze Motorie) - <cite style="background:#fcc">da elaborare</cite></h1>
    <p>FetchSize is: <c:out value="${requestScope.adMY.size()}" /></p>
    <table border="1" style="background:#ffcccc">
      <thead>
        <tr>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
        <c:forEach var="ad" items="${requestScope.adMY}" varStatus="loop">
        <tr>
          <td><c:out value="${ad.id}" /></td>
          <td><c:out value="${ad.codiceCdSUGOV}" /></td>
          <td><c:out value="${ad.nomeInsegnamento}" /></td>
          <td><c:out value="${ad.codiceADUGOV}" /></td>
          <td><c:out value="${ad.discriminante}" /></td>
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
  
    <!-- Vector non di medicina da considerare -->
    <a name="toExamine" />
    <br /><hr />
    <h1>Non di medicina e CFU &gt; 3</h1>
    <p>FetchSize is: <c:out value="${requestScope.candidati.size()}" /></p>
    <table border="1" style="background:#ffffcc;">
      <thead>
        <tr>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
          <td><c:out value="${ad.discriminante}" /></td>
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
    
    <!-- TUTTE AD Semplici -->
    <a name="all" />
    <br /><hr />
    <h1>Elenco completo</h1>
    <p>FetchSize is: <c:out value="${requestScope.elenco.size()}" /></p>
    <table border="1">
      <thead>
        <tr>
          <th>N.</th>
          <th>Codice CdS</th>
          <th>Nome Insegnamento</th>
          <th>Codice AD</th>
          <th>Discriminante</th>
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
          <td><c:out value="${ad.discriminante}" /></td>
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
