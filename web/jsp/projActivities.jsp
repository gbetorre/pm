<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="pcURL.jspf" %>
<c:choose>
  <c:when test="${param['p'] eq 'bin'}">
    <h4>Attivit&agrave; eliminate del sotto progetto <strong><c:out value="${p.titolo}" /></strong></h4>
    <c:set var="isTrash" value="${true}" scope="page" />
  </c:when>
  <c:otherwise>
    <h4>Attivit&agrave; del sotto progetto <strong><c:out value="${p.titolo}" /></strong></h4>
    <c:set var="isTrash" value="${false}" scope="page" />
  </c:otherwise>
</c:choose>
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
  <c:choose>
    <c:when test="${isTrash}">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${act}${p.id}">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafAct}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Cestino</a></li>
    </c:when>
    <c:otherwise>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafAct}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${binAct}${p.id}">Cestino</a></li>
    </c:otherwise>
  </c:choose>
    </ul>
    <hr class="separatore" />
  <c:choose>
    <c:when test="${not empty requestScope.attivita}">
    <table class="table table-bordered table-hover" id="listAct">
      <thead class="thead-light">
        <tr>
          <th scope="col" width="*">Nome</th>
          <th scope="col" width="20%">WBS</th>
          <th scope="col" width="10%">Data inizio</th>
          <th scope="col" width="10%">Data fine</th>
          <th scope="col" width="20%">Stato effettivo</th>
          <th scope="col" width="4%"><div class="text-center">Milestone</div></th>
        <c:if test="${not isTrash}">
          <th scope="col" width="4%"><div class="text-center">Funzioni</div></th>
        </c:if>
        </tr>
      </thead>
      <tbody>
      <c:set var="status" value="" scope="page" />
      <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
        <c:set var="status" value="${loop.index}" scope="page" />
        <input type="hidden" id="act-id${status}" name="act-id${status}" value="<c:out value="${act.id}"/>">
        <tr>
          <td scope="row" id="nameColumn" class="success bgAct${act.stato.id} bgFade">
        <c:choose>
          <c:when test="${isTrash}">
            <c:out value="${act.nome}"/>
          </c:when>
          <c:otherwise>
            <a href="${modAct}${p.id}&ida=${act.id}">
              <c:out value="${act.nome}"/>
            </a>
          </c:otherwise>
        </c:choose>
          </td>
          <td scope="row">
            <a href="${modWbs}${p.id}&idw=${act.wbs.id}">
              <c:out value="${act.wbs.nome}"/>
            </a>
          </td>
          <td scope="row">
            <fmt:formatDate value='${act.dataInizio}' pattern='dd/MM/yyyy' />
          </td>
          <td scope="row">
            <fmt:formatDate value='${act.dataFine}' pattern='dd/MM/yyyy' />
          </td>
          <td scope="row" id="stateActCell${act.id}">
            <a href="javascript:popupWindow('Note','popup1',true,'${act.stato.informativa}');" class="helpInfo" id="helpAct${act.id}">
              <c:out value="${act.stato.nome}" escapeXml="false" />
            </a>
          </td>
          <td scope="row">
            <input type="hidden" value="${act.milestone}" />
            <c:choose>
              <c:when test="${act.milestone}">
                <div class="form-check text-center">
                  <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" checked disabled>
                </div>
              </c:when>
              <c:otherwise>
                <div class="form-check text-center">
                  <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" disabled>
                </div>
              </c:otherwise>
            </c:choose>
          </td>
        <c:if test="${not isTrash}">
          <td scope="row">
          <c:set var="pauseico" value="/ico-pause.png" scope="page" />
          <c:set var="pausetxt" value="Sospendi" scope="page" />
          <c:set var="pauseurl" value="${susAct}${p.id}&ida=${act.id}" scope="page" />
          <c:if test="${act.idStato eq 11}">
            <c:set var="pauseico" value="/ico-start.png" scope="page" />
            <c:set var="pausetxt" value="Riprendi" scope="page" />
            <c:set var="pauseurl" value="javascript:DoPost(${act.id})" scope="page" />
          </c:if>
            <a href="${pauseurl}" id="sus-act" class="ico">
              <img id="aboutSus${act.id}" src="${initParam.urlDirectoryImmagini}${pauseico}" class="btn-del" alt="Gestione sospensione attivita" title="${pausetxt} Attivit&agrave;" />
            </a>
            <a href="${delAct}${p.id}&ida=${act.id}" id="del-act" class="ico">
              <img id="aboutDel${act.id}" src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Gestione eliminazione attivita" title="Elimina Attivit&agrave;" />
            </a>
          </td>
        </c:if>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <div class="avvisiTot"><c:out value="${fn:length(requestScope.attivita)} risultati" /></div>
    </c:when>
    <c:otherwise>
    <div class="alert alert-danger">
      <p>Non &egrave; stata trovata alcuna attivit&agrave; <c:if test="${isTrash}">eliminata</c:if> associata al sotto progetto.</p>
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
      <c:if test="${not isTrash}">
        <div class="col-8 text-center">
          <%@ include file="subPanel.jspf" %>
        </div>
      </c:if>
      </div>
    </div>
    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript"> 
      function DoPost(idAct){
        $("a#helpAct"+idAct).removeClass("helpInfo");
        $("a#helpAct"+idAct).removeAttr("href");
        $("a#helpAct"+idAct).html("Attivit&agrave; riattivata");
        $("td#stateActCell"+idAct).addClass("bgSts22");
        $("#aboutSus"+idAct).attr("src","${initParam.urlDirectoryImmagini}/spacer.gif");
        $.post("${resAct}${p.id}&ida="+idAct, { id: "${p.id}", ida: idAct } );  // Values must be in JSON format
      }
    </script>
    <script type="text/javascript">
      $(document).ready(function() {
        $('#listAct').DataTable({
          "columnDefs": [
            { "orderable": false, "targets": -1 }
          ]
        });
      });
    </script>