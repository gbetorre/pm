<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="pcURL.jspf" %>
    <h2>Attivit&agrave; del sotto progetto <c:out value="${p.titolo}" /></h2>
    <hr class="separatore" />
    <form id="editAct_form" action="" method="post">
    <c:choose>
      <c:when test="${not empty requestScope.attivita}">
      <table class="table table-bordered table-hover">
        <thead class="thead-light">
        <tr>
          <th scope="col" width="2%"></th>
          <th scope="col" width="*">Nome</th>
          <th scope="col" width="20%">WBS</th>
          <th scope="col" width="10%">Data inizio</th>
          <th scope="col" width="10%">Data fine</th>
          <th scope="col" width="20%">Stato effettivo</th>
          <th scope="col" width="4%"><div class="text-center">Milestone</div></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="status" value="" scope="page" />
        <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <input type="hidden" id="act-id${status}" name="act-id${status}" value="<c:out value="${act.id}"/>">
          <tr class="bgAct${act.stato.id}">
            <td scope="row" id="radioColumn" class="success">
              <input type="radio" id="act-${act.id}" name="act-select" value="${act.id}">
            </td>
            <td scope="row" id="nameColumn" class="success">
              <a href="${modAct}${p.id}&ida=${act.id}">
                <c:out value="${act.nome}"/>
              </a>
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
            <td scope="row">
            <c:choose>
              <c:when test="${act.stato.id ne 1}">
              <a href="javascript:popupWindow('Note','popup1',true,'${act.stato.informativa}');" class="helpInfo">
                <c:out value="${act.stato.nome}" escapeXml="false" />
              </a>
              </c:when>
              <c:otherwise>
                <c:out value="${act.stato.nome}" escapeXml="false" />
              </c:otherwise>
            </c:choose>
            </td>
            <td scope="row">
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
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <div class="avvisiTot"><c:out value="${fn:length(requestScope.attivita)} risultati" /></div>
      </c:when>
      <c:otherwise>
      <div class="alert alert-danger">
        <p>Non &egrave; stata trovata alcuna attivit&agrave; associata al progetto.</p>
      </div>
      </c:otherwise>
    </c:choose>
      <div id="container-fluid">
        <div class="row">
          <div class="col-2">
            <span class="float-left">
              <a class="btn btnNav" href="${project}">Chiudi</a>
            </span>
          </div>
          <div class="col-8 text-center">
            <%@ include file="subPanel.jspf" %>
          </div>
        </div>
      </div>
    </form>
    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript">
      $(document).ready(function() {
        $("#radioColumn, #nameColumn").click(function () {
          $('.selected').removeClass('selected');
          var trElement = $(this).parent();
          $(this).parent().addClass('selected');
          var tdElement = $("td", $(this).parent());
          $("input[name='act-select']", tdElement).attr("checked", true);
          var $radioValue = $("input[name='act-select']:checked").val();
          var $modActUrl = '<c:out value="${modAct}${p.id}" escapeXml="false" />' + "&ida=" + $radioValue;
          var $delActUrl = '<c:out value="${delAct}${p.id}" escapeXml="false" />' + "&ida=" + $radioValue;
          $('#mod-act').attr('href', $modActUrl);
          $('#del-act').attr('href', $delActUrl);
        });
      });
    </script>