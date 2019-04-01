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
          [{v:`${wbsAvo.nome}`, f:`${wbsAvo.nome}`},
           ``, `WBS capostipite`],
          <c:forEach var="wbsFiglio" items="${wbsAvo.wbsFiglie}">
          [{v:`${wbsFiglio.nome}`, f:`${wbsFiglio.nome}`},
           `${wbsFiglio.wbsPadre.nome}`, `WBS figlia`],
            <c:forEach var="wbsNipote" items="${wbsFiglio.wbsFiglie}">
          [`${wbsNipote.nome}`, `${wbsNipote.wbsPadre.nome}`, `Work package = ${wbsNipote.workPackage}`],
              <c:forEach var="wbsPronipote" items="${wbsNipote.wbsFiglie}">
          [`${wbsPronipote.nome}`, `${wbsPronipote.wbsPadre.nome}`, `Work package = ${wbsPronipote.workPackage}`],
                <c:forEach var="wbsProPronipote" items="${wbsPronipote.wbsFiglie}">
          [`${wbsProPronipote.nome}`, `${wbsProPronipote.wbsPadre.nome}`, `Work package = ${wbsProPronipote.workPackage}`],
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
    <span class="float-right">
      <a class="ico" href="${wbs}${p.id}">
        <img src="${initParam.urlDirectoryImmagini}/ico-return.png" class="ico-home" alt="Torna a elenco wbs" title="Torna a elenco wbs" />
      </a>
      <a class="ico" href="${project}">
        <img src="${initParam.urlDirectoryImmagini}/ico-home.png" class="ico-home" alt="Torna a elenco progetti" title="Torna a elenco progetti" />
      </a>
    </span>
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${wbs}${p.id}">WBS</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="${grafico}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="javascript:alert('Occorre selezionare una WBS per indicarne le attività')" id="show_act">Attività</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
    </ul>
    <hr class="separatore" />
    <h2>WBS del sotto progetto <strong><c:out value="${p.titolo}" /></strong></h2>
    <div id="chart_div" style="overflow-y: scroll;"></div>