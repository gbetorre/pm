<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="pcURL.jspf" %>
    <h4>
      Misurazioni dell'obiettivo strategico <br />
      <strong><c:out value="${p.titolo}" /></strong>
    </h4>
    <hr class="separatore" />
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${ind}${p.id}">Indicatori</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Misurazioni</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${repInd}${p.id}">Report</a></li>
    </ul>
    <hr class="separatore" />
  <c:choose>
    <c:when test="${not empty requestScope.misurazioni}">
    <table class="table table-bordered table-hover" id="listMes">
      <thead class="thead-light">
        <tr>
          <th scope="col" width="15%">Azione (Progetto)</th>
          <th scope="col" width="20%">Indicatore</th>
          <th scope="col" width="10%">Target</th>
          <th scope="col" width="10%">Valore Misurazione</th>
          <th scope="col" width="*">Risultati</th>
          <th scope="col" width="8%">Data Misurazione</th>
          <th scope="col" width="5%">Ultima Misurazione</th>
        </tr>
      </thead>
      <tbody>
      <c:set var="status" value="" scope="page" />
      <c:forEach var="mis" items="${requestScope.misurazioni}" varStatus="loop">
        <c:set var="ind" value="${mis.indicatore}" scope="page" />
        <c:set var="status" value="${loop.index}" scope="page" />
        <fmt:formatDate var="lastModified" value="${mis.dataUltimaModifica}" pattern="dd/MM/yyyy" />
        <input type="hidden" id="ind-id${status}" name="ind-id${status}" value="<c:out value="${ind.id}"/>">
        <tr>
          <td scope="row">
        <c:choose>
          <c:when test="${not empty ind.wbs}">
            <a href="${modWbs}${p.id}&idw=${ind.wbs.id}">
              <c:out value="${ind.wbs.nome}"/>
            </a>
          </c:when>
          <c:otherwise>
            Nessuna azione associata
          </c:otherwise>
        </c:choose>
          </td>
          <td scope="row" id="nameColumn" class="success bgAct${ind.tipo.id}">
            <a href="${modInd}${p.id}&idi=${ind.id}" title="Modificato:${lastModified} ${fn:substring(ind.oraUltimaModifica,0,5)}">
              <c:out value="${ind.nome}"/>
            </a>
            <c:if test="${ind.idStato eq 4}">
            <span class="badge badge-secondary">Chiuso</span>
            </c:if>
          </td>
          <td scope="row" class="bgAct${ind.tipo.id} bgFade">
          <c:choose>
            <c:when test="${empty ind.targetRivisto}">
              <span class="badge-light small" title="Target originale">
                <c:out value="${ind.target}" />
              </span>
            </c:when>
            <c:otherwise>
            <fmt:formatDate var="lastReview" value="${ind.dataRevisione}" pattern="dd/MM/yyyy" />
            <span class="badge badge-warning" title="Il target originale ('${ind.target}') &egrave; stato modificato il ${lastReview} da ${ind.autoreUltimaRevisione}">
              <c:out value="${ind.targetRivisto}" />
            </span>
            </c:otherwise>
          </c:choose>
            <small>
              <br />
              (<fmt:formatDate value="${ind.dataTarget}" pattern="dd/MM/yyyy" />)
            </small>
          </td>
          <td scope="row">
            <a href="${monInd}${p.id}&idm=${mis.id}">
              <c:out value="${mis.descrizione}" />
            </a>
          </td>
          <td scope="row" class="small">
            <small><c:out value="${mis.informativa}" /></small>
          </td>
          <td scope="row">
            <fmt:formatDate value="${mis.dataMisurazione}" pattern="dd/MM/yyyy" />
          </td>
        <c:choose>
          <c:when test="${mis.ultimo}">
            <td scope="row" class="bgcolor1">
              <div class="form-check text-center">
                <span class="badge badge-success">
                  SI
                </span>
              </div>
            </td>
          </c:when>
          <c:otherwise>
            <td scope="row" class="bgcolor2">
              <div class="form-check text-center">
                <span class="badge badge-light">
                  NO
                </span>
              </div>
            </td>
          </c:otherwise>
        </c:choose>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <div class="avvisiTot"><c:out value="${fn:length(requestScope.misurazioni)} misurazioni" /></div>
    </c:when>
    <c:otherwise>
    <div class="alert alert-danger">
      <p>
        Non &egrave; stata trovata alcuna misurazione gi&agrave; associata ad indicatori dell'obiettivo strategico.<br />
        Per problemi o necessit&agrave; di aggiornamento dati, si prega di rivolgersi al <a href="mailto:reporting@ateneo.univr.it">PMO di Ateneo</a>.
      </p>
    </div>
    </c:otherwise>
  </c:choose>
    <div id="container-fluid">
      <div class="row">
        <div class="col-2">
          <span class="float-left">
            <a class="btn btnNav" href="${project}">
              <i class="fas fa-home"></i>
              Progetti
            </a>
          </span>
        </div>
        <div class="col-8 text-center">
          <%@ include file="subPanel.jspf" %>
        </div>
      </div>
    </div>
    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript">
      $(document).ready(function() {
        $('#listMes').DataTable({
          "columnDefs": [
            { /*"orderable": false, "targets": -1*/ }
          ]
        });
      });
    </script>