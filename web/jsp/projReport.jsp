<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:set var="divs" value="21,22,23,24,25,26" scope="page" />
<div class="row">
  <div class="col-12">
    <form id="status_form" action="#" method="post">
      <hr class="separatore" />
        <h2 align="center">
          Report del sotto progetto:&nbsp;<strong><c:out value="${p.titolo}" /></strong>
          <span class="float-right">
            <a href="#" class="btn btn-primary" id="exp-report">Esporta</a>
          </span>
        </h2>
        <hr class="riga" />
        <div class="row">
          <div class="col-3"><label for="">Seleziona la data sulla quale calcolare il report:</label></div>
          <div class="col-3">
            <input type="text" class="form-control calendarData" id="rep-date" name="rep-date" value="<fmt:formatDate value="${selectionDate}" pattern="dd/MM/yyyy" />">
          </div>
          <div class="col-1">
            <a href="" class="btn btn-primary" id="rep-view">Visualizza</a>
          </div>
        </div>
        <div class="row reportHead">
          <div class="col-2">
            Workpackage
          </div>
          <div class="col-4">
            Nome attività
          </div>
          <div class="col-6" style="text-align: center;">
            Stato attività
          </div>
        </div>
        <div class="row reportStateAct">
          <div class="col-6">
          </div>
          <div class="col-1 bgSts21">
            Chiusa da pi&ugrave; tempo
          </div>
          <div class="col-1 bgSts22">
            Chiusa il mese scorso
          </div>
          <div class="col-1 bgSts23">
            In ritardo
          </div>
          <div class="col-1 bgSts24">
            In realizzazione
          </div>
          <div class="col-1 bgSts25">
            Prossimo mese
          </div>
          <div class="col-1 bgSts26">
            Mesi successivi
          </div>
        </div>
        <c:forEach var="wp" items="${requestScope.workPackagesOfProj}" varStatus="loop">
          <div class="row reportWpRow">
            <div class="col-6 reportWpHead">
              <a href="${modWbs}${p.id}&idw=${wp.id}">
                <strong>${wp.nome}</strong>
              </a>
            </div>
            <div class="col-2 bgSts21"></div>
            <div class="col-2 bgSts23"></div>
            <div class="col-2 bgSts25"></div>
          </div>
          <c:forEach var="act" items="${wp.attivita}" varStatus="loop">
            <div class="row">
              <div class="col-1 reportWpRow"></div>
              <div class="col-5 reportAct">
                <a href="${modAct}${p.id}&ida=${act.id}">
                  <c:out value="${act.nome}" />
                </a>
              </div>
              <c:forTokens var="divSts" items="${divs}" delims=",">
                <c:set var="bgAct" value="" scope="page" />
                <c:if test="${act.stato.ordinale eq divSts}">
                  <c:set var="bgAct" value="bgAct${act.stato.ordinale}" scope="page" />
                </c:if>
                <div class="col-1 bgSts${divSts} ${bgAct}"></div>
              </c:forTokens>
            </div>
          </c:forEach>
        </c:forEach>
      <hr class="separatore" />
      <div class="form-row">
        <div class="col-2">  
          <span class="float-left">
            <a class="btn btnNav" href="${project}">Chiudi</a>
          </span>
        </div>
      </div>
    </form>
  </div>
</div>
<script type="text/javascript">
  $(document).ready(function() {
    $('input[type=\'text\'].calendarData').datepicker();
    $("#rep-view").click(function () {
      var $dateValue = $("input[name='rep-date']").val();
      var $repView = '<c:out value="${report}${p.id}" escapeXml="false" />' + "&d=" + $dateValue;
      $('#rep-view').attr('href', $repView);
	});
  });
</script>