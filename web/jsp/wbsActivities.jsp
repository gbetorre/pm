<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="pcURL.jspf" %>
    <c:set var="wp" value="" scope="page" />
    <c:forEach var="workPackage" items="${requestScope.wbs}" begin="0" end="0">
      <c:set var="wp" value="${workPackage}" scope="page" />
    </c:forEach>
    <h4>
      Attivit&agrave; della WBS
      <a href="${modWbs}${p.id}&idw=${wp.id}"> 
        <c:out value="${wp.nome}" />
      </a>
    </h4>
    <span class="float-right">
      <a class="btn btnNav" href="${urlWbs}${p.id}">
        <i class="fas fa-undo"></i> WBS
      </a>
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${urlWbs}${p.id}">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafico}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#" id="show_act">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${timelines}${p.id}">Timelines workpackage</a></li>
    </ul>
    <hr class="separatore" />
    <form id="editWbsAct_form" action="#" method="post">
      <c:choose>
        <c:when test="${not empty requestScope.attivita}">
          <table class="table table-bordered table-hover" id="actOnWbs">
            <thead class="thead-light">
            <tr>
              <th scope="col" width="*">Nome</th>
              <%-- <th scope="col" width="20%">WBS</th> --%>
              <th scope="col" width="10%">Data inizio</th>
              <th scope="col" width="10%">Data fine</th>
              <th scope="col" width="20%">Stato effettivo</th>
              <th scope="col" width="4%"><div class="text-center">Milestone</div></th>
              <th scope="col" width="4%"><div class="text-center">Funzioni</div></th>
            </tr>
            </thead>
            <tbody>
            <c:set var="status" value="" scope="page" />
            <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
              <c:set var="status" value="${loop.index}" scope="page" />
              <input type="hidden" id="act-id${status}" name="act-id${status}" value="<c:out value="${act.id}"/>">
              <tr>
                <td scope="row" id="nameColumn" class="bgAct${act.stato.id} bgFade">
                  <a href="${modAct}${p.id}&ida=${act.id}&idw=${wp.id}">
                    <c:out value="${act.nome}"/>
                  </a>
                </td>
                <td scope="row">
                  <fmt:formatDate value='${act.dataInizio}' pattern='dd/MM/yyyy' />
                </td>
                <td scope="row">
                  <fmt:formatDate value='${act.dataFine}' pattern='dd/MM/yyyy' />
                </td>
                <td scope="row" id="stateWbsActCell${act.id}">
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
                <td scope="row">
                  <c:set var="pauseico" value="/ico-pause.png" scope="page" />
                  <c:set var="pausetxt" value="Sospendi" scope="page" />
                  <c:set var="pauseurl" value="${susAct}${p.id}&ida=${act.id}&idw=${wp.id}" scope="page" />
                  <c:if test="${act.idStato eq 11}">
                    <c:set var="pauseico" value="/ico-start.png" scope="page" />
                    <c:set var="pausetxt" value="Riprendi" scope="page" />
                    <c:set var="pauseurl" value="javascript:DoPost(${act.id},${wp.id})" scope="page" />
                  </c:if>
                  <a href="${pauseurl}" id="sus-act" class="ico">
                    <img id="aboutSus${act.id}" src="${initParam.urlDirectoryImmagini}${pauseico}" class="btn-del" alt="Gestione sospensione attivita" title="${pausetxt} Attivit&agrave;" />
                  </a>
                  <a href="${delAct}${p.id}&ida=${act.id}&idw=${wp.id}" id="del-act" class="ico">
                    <img id="aboutDel${act.id}" src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Gestione eliminazione attivita" title="Elimina Attivit&agrave;" />
                  </a>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <div class="avvisiTot"><c:out value="${fn:length(requestScope.attivita)} risultati" /></div>
        </c:when>
        <c:otherwise>
          <div class="alert alert-danger">
            <p>Non &egrave; stata trovata alcuna attivit&agrave; associata a questa WBS.</p>
          </div>
        </c:otherwise>
      </c:choose>
      <div id="container-fluid">
        <div class="row">
          <div class="col-4">
            <span class="float-left">
              <a class="btn btnNav" href="${urlWbs}${p.id}">
                <i class="fas fa-undo"></i> WBS
              </a>
              <a class="btn btnNav" href="${project}">
                <i class="fas fa-home"></i>
                Progetti
              </a>
            </span>
          </div>
          <div class="col-4 text-center">
            <a href="${addAct}${p.id}&idw=${wp.id}" class="btn btn-success"><i class="fas fa-plus"></i> Aggiungi</a>
          </div>
        </div>
      </div>
    </form>
    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript">
      $(document).ready(function() {
        $("#actOnWbs").DataTable({
          "columnDefs": [
            { "orderable": false, "targets": -1 }
          ]
        });
      });
      
      function DoPost(idAct, idWbs){
        $("a#helpAct"+idAct).removeClass("helpInfo");
        $("a#helpAct"+idAct).removeAttr("href");
        $("a#helpAct"+idAct).html("Attivit&agrave; riattivata");
        $("td#stateWbsActCell"+idAct).addClass("bgSts22");
        $("#aboutSus"+idAct).attr("src","${initParam.urlDirectoryImmagini}/spacer.gif");
        $.post("${resAct}${p.id}&ida="+idAct+"&idw="+idWbs, { id: "${p.id}", ida: idAct } );  // Values must be in JSON format
      }
    </script>