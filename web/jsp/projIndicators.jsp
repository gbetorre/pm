<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="pcURL.jspf" %>
    <h4>
      Indicatori dell'obiettivo strategico <br />
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
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Indicatori</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Misurazioni</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${repInd}${p.id}">Report</a></li>
    </ul>
    <hr class="separatore" />
  <c:choose>
    <c:when test="${not empty requestScope.indicatori}">
    <table class="table table-bordered table-hover" id="listInd">
      <thead class="thead-light">
        <tr>
          <th scope="col" width="20%">Azione (Progetto)</th>
          <th scope="col" width="*">Nome Indicatore</th>
          <th scope="col" width="5%">Baseline</th>
          <th scope="col" width="10%">Data Baseline</th>
          <th scope="col" width="5%">Target</th>
          <th scope="col" width="10%">Data Target</th>
          <th scope="col" width="5%">Tipo Indicatore</th>
          <th scope="col" width="5%"><div class="text-center">Misurato</div></th>
        </tr>
      </thead>
      <tbody>
      <c:set var="status" value="" scope="page" />
      <c:forEach var="ind" items="${requestScope.indicatori}" varStatus="loop">
        <c:set var="status" value="${loop.index}" scope="page" />
        <fmt:formatDate var="lastModified" value="${ind.dataUltimaModifica}" pattern="dd/MM/yyyy" />
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
          <td scope="row" id="nameColumn" class="success bgAct${ind.tipo.id} bgFade">
            <a href="${modInd}${p.id}&idi=${ind.id}" title="Modificato:${lastModified} ${fn:substring(ind.oraUltimaModifica,0,5)}">
              <c:out value="${ind.nome}"/>
            </a>
            <c:if test="${ind.idStato eq 4}">
            <span class="badge badge-secondary">Chiuso</span>
            </c:if>
          </td>
          <td scope="row">
            <c:out value="${ind.baseline}" />
          </td>
          <td scope="row">
            <fmt:formatDate value="${ind.dataBaseline}" pattern="dd/MM/yyyy" />
          </td>
          <td scope="row">
          <c:choose>
            <c:when test="${empty ind.targetRivisto}">
              <c:out value="${ind.target}" />
            </c:when>
            <c:otherwise>
            <fmt:formatDate var="lastReview" value="${ind.dataRevisione}" pattern="dd/MM/yyyy" />
            <span class="badge badge-warning" title="Attenzione: il target &egrave; stato modificato il ${lastReview} da ${ind.autoreUltimaRevisione}!">
              <c:out value="${ind.targetRivisto}" />
            </span>
            </c:otherwise>
          </c:choose>
            <a href="<c:out value="${extInd}${p.id}&idi=${ind.id}" escapeXml="false" />" id='btn-tar'>
              <button type="button" class="btn btn-sm btn-outline-primary">Modifica</button>
            </a>
          </td>
          <td scope="row">
            <fmt:formatDate value="${ind.dataTarget}" pattern="dd/MM/yyyy" />
          </td>
          <td scope="row">
            <c:out value="${ind.tipo.nome}"/>
          </td>
        <c:choose>
          <c:when test="${ind.totMisurazioni gt 0}">
            <td scope="row" class="bgcolorgreen">
              <div class="form-check text-center">
                <span>
                  SI
                  <span class="badge badge-dark">
                    <c:out value="${ind.totMisurazioni}" />
                  </span>
                </span>
              </div>
            </td>
          </c:when>
          <c:otherwise>
            <td scope="row" class="bgcolorred">
              <div class="form-check text-center">
                <span>NO</span>
              </div>
            </td>
          </c:otherwise>
        </c:choose>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <div class="avvisiTot"><c:out value="${fn:length(requestScope.indicatori)} risultati" /></div>
    </c:when>
    <c:otherwise>
    <div class="alert alert-danger">
      <p>
        Non &egrave; stato trovato alcun indicatore gi&agrave; associato all'obiettivo strategico.<br />
        Per problemi o necessit&agrave; di aggiornamento dati, si prega di rivolgersi al <a href="mailto:stefano.fedeli@univr.it">PMO di Ateneo</a>.
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
        $('#listInd').DataTable({
          "columnDefs": [
            { /*"orderable": false, "targets": -1*/ }
          ]
        });
      });
    </script>