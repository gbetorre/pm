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
      function createGraph(idWp, nome) {
        if (idWp == "void") {
          $('#timelines_div').hide();
          $('#voidWp').show();
          $("#title").text("Timelines");
        } else {
          google.charts.load('current', {packages:["timeline"], 'language': 'it'});
          google.charts.setOnLoadCallback(drawChart);
          $("#title").html("Timelines del workpackage <strong>" + nome + "</strong>");
          function drawChart() {
            var data = new google.visualization.DataTable();
            var chart = new google.visualization.Timeline(document.getElementById('timelines_div'));
            data.addColumn('string', 'nome');
            data.addColumn('date', 'inizio');
            data.addColumn('date', 'fine');
            <c:forEach var="wp" items="${requestScope.workPackagesOfProj}">
            if (${wp.id} == idWp) {
            <c:forEach var="act" items="${wp.attivita}">
              var startDate = "<fmt:formatDate value='${act.dataInizio}' pattern='dd-MM-yyyy'/>";
              var endDate = "<fmt:formatDate value='${act.dataFine}' pattern='dd-MM-yyyy'/>";
              var vStartDate = startDate.split("-");
              var vEndDate = endDate.split("-");
              var startYear = vStartDate[2];
              var startMonth = vStartDate[1];
              var startDay = vStartDate[0];    
              var endYear = vEndDate[2];
              var endMonth = vEndDate[1];
              var endDay = vEndDate[0];
              data.addRow([`${act.nome}`, new Date(startYear, startMonth - 1, startDay),  new Date(endYear, endMonth - 1, endDay)]);
            </c:forEach>
            }
            </c:forEach>
            var options = {
                height: 500,
            };
            $('#voidWp').hide();
            $('#timelines_div').show();
            chart.draw(data, options);
          }
        }
      };
  	</script>
    <form  id="wpTimelines_form" action="#" method="post">
      <h4 id="title">
        Timelines 
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
        <li class="nav-item"><a class="nav-link" data-toggle="tab" id="show_act">Attivit&agrave;</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
        <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="${timelines}${p.id}">Timelines workpackage</a></li>
      </ul>
      <hr class="separatore" />
      <div class="row">
        <div class="col-3 avvisiTot">
          <label for="">
            <a href="javascript:popupWindow('Note','popup1',true,'Vengono mostrate solo le WBS di tipo Workpackage <strong class=\'alert-danger\'>che hanno attivit&agrave; collegate</strong>, in quanto il report di timelines viene prodotto in base alle date delle attivit&agrave; stesse.');" class="helpInfo" id="helpAct1">
              Seleziona il workpackage da visualizzare:
            </a>
          </label>
        </div>
        <div class="col-3">
          <select class="form-control" id="wpId" onChange="createGraph(this.options[this.selectedIndex].value, this.options[this.selectedIndex].text);">
              <option value="void">Nessun workpackage selezionato</option>
            <c:forEach var="wp" items="${requestScope.workPackagesOfProj}" varStatus="loop">
              <option value="${wp.id}">${wp.nome}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <hr class="separatore" />
      <div id="timelines_div"></div>
      <div id="voidWp" class="alert alert-warning">
        <p>
          <span class="ui-icon ui-icon-alert"></span>
          &nbsp;Non &egrave; possibile visualizzare il grafico, in quanto non &egrave; stato selezionato alcun workpackage.
        </p>
      </div>
    </form>
    <%@ include file="subPopup.jspf" %>
