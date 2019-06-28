<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:url var="timelinesWp" context="${initParam.appName}" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="tml" />
  <c:param name="id" value="${p.id}" />
  <c:param name="idw" value="" />
</c:url>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {packages:["timeline"]});
      google.charts.setOnLoadCallback(drawChart);
      
      function drawChart() {
        var data = new google.visualization.DataTable();
        var chart = new google.visualization.Timeline(document.getElementById('timelines_div'));
        data.addColumn('string', 'nome');
        data.addColumn('date', 'inizio');
        data.addColumn('date', 'fine');
        data.addRows ([
          ['Attivita1', new Date(1789, 3, 30), new Date(1797, 2, 4)],
          ['Attivita2', new Date(1799, 2, 4),  new Date(1809, 2, 4)],
          ['Attivita3', new Date(1797, 2, 4),  new Date(1801, 2, 4)]
        ]);
        chart.draw(data);
      }
  	</script>
    <form  id="wpTimelines_form" action="#" method="post">
      <h4>
        Timelines del workpackage&nbsp;<strong>WP1</strong>
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
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Grafico</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" id="show_act">Attivit&agrave;</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
        <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="${timelines}${p.id}">Timelines workpackage</a></li>
      </ul>
      <hr class="separatore" />
      <div id="timelines_div"><!-- style="overflow-y: scroll;" --></div>
    </form>