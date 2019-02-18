<%@ include file="pcURL.jspf" %>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {packages:["orgchart"]});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Name');
        data.addColumn('string', 'Manager');
        data.addColumn('string', 'ToolTip');
        // For each orgchart box, provide the name, father, and tooltip to show.
        data.addRows([
        <c:forEach var="wbsAvo" items="${requestScope.wbsHierarchy}">
          [{v:'${wbsAvo.nome}', f:'${wbsAvo.nome}<div style="color:red; font-style:italic">WBS padre</div>'},
           '', 'WBS capostipite'],
          <c:forEach var="wbsFiglio" items="${wbsAvo.wbsFiglie}">
          [{v:'${wbsFiglio.nome}', f:'${wbsFiglio.nome}<div style="color:red; font-style:italic">WBS livello 1</div>'},
           '${wbsFiglio.wbsPadre.nome}', 'WBS figlia'],
            <c:forEach var="wbsNipote" items="${wbsFiglio.wbsFiglie}">
          ['${wbsNipote.nome}', '${wbsNipote.wbsPadre.nome}', 'Work package = ${wbsNipote.workPackage}'],
              <c:forEach var="wbsPronipote" items="${wbsNipote.wbsFiglie}">
          ['${wbsPronipote.nome}', '${wbsPronipote.wbsPadre.nome}', 'Work package = ${wbsPronipote.workPackage}'],
                <c:forEach var="wbsProPronipote" items="${wbsPronipote.wbsFiglie}">
          ['${wbsProPronipote.nome}', '${wbsProPronipote.wbsPadre.nome}', 'Work package = ${wbsProPronipote.workPackage}'],
                </c:forEach>
              </c:forEach>
            </c:forEach>
          </c:forEach>
        </c:forEach>
        ]);
        // Create the chart.
        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
        // Draw the chart, setting the allowHtml option to true for the tooltips.
        chart.draw(data, {allowHtml:true});
      }
    </script>
    <h2>WBS del progetto <strong><c:out value="${p.titolo}" /></strong></h2>
    <div id="chart_div"></div>
    <hr class="separatore" />
    <form action="" method="post" onsubmit="return confirmSubmit();">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
        <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">WBS</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Attività</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Report</a></li>
      </ul>
      <c:choose>
      <c:when test="${not empty requestScope.wbsHierarchy}">
      <div class="tab-content responsive hidden-xs hidden-sm">
        <div role="tabpanel" class="tab-pane active" id="tab-wbs">
        <br />
        <table class="table table-bordered table-hover">
          <thead class="thead-light">
          <tr>
            <th class="bg-primary" scope="col" width="5%"></th>
            <th class="bg-primary" scope="col" width="*">Nome WBS padre</th>
            <!-- <th scope="col" width="23%">Descrizione</th> -->
            <th class="bg-primary" scope="col" width="20%">Note di avanzamento</th>
            <th class="bg-primary" scope="col" width="20%">Risultati raggiunti</th>
            <th class="bg-primary" scope="col" width="5%"><div class="text-center">Workpackage</div></th>
          </tr>
          </thead>
          <tbody>
          <c:set var="duplicateHeaders" value="${false}" scope="page" />
          <c:set var="status" value="" scope="page" />
          <c:forEach var="wbs" items="${requestScope.wbsHierarchy}" varStatus="loop">
            <c:set var="status" value="${loop.index}" scope="page" />
            <c:if test="${duplicateHeaders}">
            <tr class="active">
            <td class="bg-primary" width="5%"></td>
            <td class="bg-primary" width="*"><strong>Nome WBS padre</strong></td>
            <td class="bg-primary" width="20%"><strong>Note di avanzamento</strong></td>
            <td class="bg-primary" width="20%"><strong>Risultati raggiunti</strong></td>
            <td class="bg-primary" width="5%"><div class="text-center"><strong>Workpackage</strong></div></td>
            </tr>
            </c:if>
            <tr>
              <td scope="col" id="radioColumn">
                <input type="radio" id="wbs-<c:out value="${wbs.id}"/>" name="wbs-select" value="<c:out value="${wbs.id}"/>"/>
                <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbs.id}"/>">
              </td>
              <td scope="col" id="nameColumn">
                <a href="${modWbs}${p.id}&idw=${wbs.id}">
                  <div><c:out value="${wbs.nome}" /></div>
                </a>
              </td>
              <%-- 
              <td scope="col">
                <c:out value="${wbs.descrizione}"/>
              </td>
              --%>
              <td scope="col">
                <c:out value="${wbs.noteAvanzamento}" />
              </td>
              <td scope="col">
                <c:out value="${wbs.risultatiRaggiunti}" />
              </td>
              <td scope="col">
                <c:choose>
                  <c:when test="${wbs.workPackage}">
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" checked disabled>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" disabled>
                    </div>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            <c:if test="${not empty wbs.wbsFiglie}">
            <c:set var="duplicateHeaders" value="${true}" scope="page" />
            <tr>
            <td colspan="5">
            <table class="table table-condensed col-md-10">
              <thead class="thead-light">
              <tr>
                <th scope="col" width="5%"></th>
                <th scope="col" width="*">Nome WBS figlia</th>
                <th scope="col" width="5%"><div class="text-center">Workpackage</div></th>
              </tr>
              </thead>
              <tbody> 
              <c:forEach var="wbsFiglio" items="${wbs.wbsFiglie}" varStatus="loop">
              <c:set var="status" value="${loop.index}" scope="page" />
              <tr>
              <td scope="col" id="radioColumn">
                <input type="radio" id="wbs-<c:out value="${wbsFiglio.id}"/>" name="wbs-select" value="<c:out value="${wbsFiglio.id}"/>"/>
                <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbsFiglio.id}"/>">
              </td>
              <td scope="col" id="nameColumn">
                <a href="${modWbs}${p.id}&idw=${wbsFiglio.id}">
                  <c:out value="${wbsFiglio.nome}" />
                </a>
                <c:if test="${not empty wbsFiglio.wbsFiglie}">
                  <div class="moduleThin">
                  <ol class="list-unstyled">
                  <c:forEach var="wbsNipote" items="${wbsFiglio.wbsFiglie}">
                  <li>
                    <input type="radio" id="wbs-<c:out value="${wbsNipote.id}"/>" name="wbs-select" value="<c:out value="${wbsNipote.id}"/>"/>
                    <a href="${modWbs}${p.id}&idw=${wbsNipote.id}">
                      <c:out value="${wbsNipote.nome}" />
                    </a>
                    <c:if test="${wbsNipote.workPackage}">
                    <cite>Workpackage</cite>
                    </c:if>
                    <c:if test="${not empty wbsNipote.wbsFiglie}">
                      <div class="moduleThin2">
                      <ul class="list-unstyled">
                      <c:forEach var="wbsPronipote" items="${wbsNipote.wbsFiglie}">
                        <li>
                        <input type="radio" id="wbs-<c:out value="${wbsPronipote.id}"/>" name="wbs-select" value="<c:out value="${wbsPronipote.id}"/>"/>
                        <a href="${modWbs}${p.id}&idw=${wbsPronipote.id}">
                          <c:out value="${wbsPronipote.nome}" />
                        </a>
                        <c:if test="${wbsPronipote.workPackage}">
                          <cite>Workpackage</cite>
                        </c:if>
                        <c:if test="${not empty wbsPronipote.wbsFiglie}">
                        <div class="moduleThin2">
                        <ul class="list-unstyled">
                        <c:forEach var="wbsProPronipote" items="${wbsPronipote.wbsFiglie}">
                          <li>
                            <input type="radio" id="wbs-<c:out value="${wbsProPronipote.id}"/>" name="wbs-select" value="<c:out value="${wbsProPronipote.id}"/>"/>
                            <a href="${modWbs}${p.id}&idw=${wbsProPronipote.id}">
                              <c:out value="${wbsProPronipote.nome}" />
                            </a>
                            <c:if test="${wbsProPronipote.workPackage}">
                              <cite>Workpackage</cite>
                            </c:if>
                          </li>
                        </c:forEach>
                        </ul>
                        </div>
                        </c:if>
                      </c:forEach>
                      </ul>
                      </div>
                    </c:if>
                  </li>
                  </c:forEach>
                  </ol>
                  </div>
                </c:if>
 
              </td>
              <td scope="col">
                <c:choose>
                  <c:when test="${wbsFiglio.workPackage}">
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" checked disabled>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" disabled>
                    </div>
                  </c:otherwise>
                </c:choose>
              </td>
              </c:forEach>
            </table>
            </td>
            </tr>
            </c:if>
          </c:forEach>
          </tbody>
        </table>
        <input type="hidden" id="wbs-loop-status" name="wbs-loop-status" value="<c:out value="${status}"/>">
        <div id="container-fluid">
          <hr class="separatore" />
          <div class="row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btn-primary" href="${project}">Chiudi</a>
              </span>
            </div>
            <div class="col-8 text-center">
              <a class="btn btn-primary" href="${addWbs}${p.id}" id="add-wbs">Aggiungi</a>
              <a class="btn btn-primary" href="" id="mod-wbs" onclick="selectionEdit('WBS')">Modifica</a>
              <input type="submit" class="btn btn-primary" id="del-wbs" name="" value="Elimina" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
<script type="text/javascript">
  $(document).ready(function() {
    $("#radioColumn, #nameColumn").click(function () {
      $('.selected').removeClass('selected');
      var trElement = $(this).parent();
      $(this).parent().addClass('selected');
      var tdElement = $("td", $(this).parent());
      $("input[name='wbs-select']", tdElement).attr("checked", true);
      var $radioValue = $("input[name='wbs-select']:checked").val();
      var $modWbsUrl = '<c:out value="${modWbs}${p.id}" escapeXml="false" />' + "&idw=" + $radioValue;
      var $delWbsName = "del-wbs" + $radioValue;
      $('#mod-wbs').attr('href', $modWbsUrl);
      $('#del-wbs').attr('name', $delWbsName);
    });
  });
</script>
      </c:when>
      <c:otherwise>
      <hr class="separatore" />
      <div class="alert alert-danger">
        <strong>Spiacente 
          <c:out value="${sessionScope.usr.nome}" />
          <c:out value="${sessionScope.usr.cognome}" />.<br />
        </strong>
        <p>Non sono state trovate WBS associate al progetto.</p>
      </div>
      </c:otherwise>
    </c:choose>