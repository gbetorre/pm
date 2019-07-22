    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ include file="pcURL.jspf" %>
    <c:set var="linkWbs" value="${modWbs}${p.id}&idw=" scope="page"/>
    <c:set var="linkAct" value="${modAct}${p.id}&ida=" scope="page"/>
    <c:set var="ruoli" value="${sessionScope.usr.ruoli}" scope="page" />
    <c:set var="ruolo" value="" scope="page" />
    <c:forEach var="c" items="${ruoli}" varStatus="loop">
      <c:if test="${c.id eq p.id}">
        <c:set var="ruolo" value="${c.nome}" scope="page" />
      </c:if>
    </c:forEach>
    <c:set var="superuser" value="${false}" scope="page" />
    <c:if test="${(ruolo eq 'PMOATE') or (ruolo eq 'PMODIP') or (ruolo eq 'PM') or (ruolo eq 'TL')}">
      <c:set var="superuser" value="${true}" scope="page" />
    </c:if>
    <h4>WBS del sotto progetto <strong><c:out value="${p.titolo}" /></strong> (rappresentazione grafica)</h4>
    <hr class="separatore" />
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {packages:["orgchart"]});
      google.charts.setOnLoadCallback(drawChart);
	    var vWbsPadreOfWbs = new Array();
	  
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Name');
        data.addColumn('string', 'Manager');
        data.addColumn('string', 'ToolTip');
        // For each orgchart box, provide the name, father, and tooltip to show.
        data.addRows([
        <c:forEach var="wbsAvo" items="${requestScope.wbsHierarchy}">
          [{v:`${fn:trim(wbsAvo.nome)}_${wbsAvo.id}`, f:`${fn:trim(wbsAvo.nome)}`}, ``, `${wbsAvo.id}_0_${wbsAvo.workPackage}`],
          <c:forEach var="wbsFiglio" items="${wbsAvo.wbsFiglie}">
          [{v:`${fn:trim(wbsFiglio.nome)}_${wbsFiglio.id}`, f:`${fn:trim(wbsFiglio.nome)}`}, `${fn:trim(wbsFiglio.wbsPadre.nome)}_${wbsFiglio.wbsPadre.id}`, `${wbsFiglio.id}_${wbsFiglio.wbsPadre.id}_${wbsFiglio.workPackage}`],
            <c:forEach var="wbsNipote" items="${wbsFiglio.wbsFiglie}">
          [{v:`${fn:trim(wbsNipote.nome)}_${wbsNipote.id}`, f:`${fn:trim(wbsNipote.nome)}`}, `${fn:trim(wbsNipote.wbsPadre.nome)}_${wbsNipote.wbsPadre.id}`, `${wbsNipote.id}_${wbsNipote.wbsPadre.id}_${wbsNipote.workPackage}`],
              <c:forEach var="wbsPronipote" items="${wbsNipote.wbsFiglie}">
          [{v:`${fn:trim(wbsPronipote.nome)}_${wbsPronipote.id}`, f:`${fn:trim(wbsPronipote.nome)}`}, `${fn:trim(wbsPronipote.wbsPadre.nome)}_${wbsPronipote.wbsPadre.id}`, `${wbsPronipote.id}_${wbsPronipote.wbsPadre.id}_${wbsPronipote.workPackage}`],
                <c:forEach var="wbsProPronipote" items="${wbsPronipote.wbsFiglie}">
          [{v:`${fn:trim(wbsProPronipote.nome)}_${wbsProPronipote.id}`, f:`${fn:trim(wbsProPronipote.nome)}`}, `${fn:trim(wbsProPronipote.wbsPadre.nome)}_${wbsProPronipote.wbsPadre.id}`, `${wbsProPronipote.id}_${wbsProPronipote.wbsPadre.id}_${wbsProPronipote.workPackage}`],
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
        // Custom title of element
        $(".google-visualization-orgchart-node").prop("title", "Clicca per ulteriori funzionalita'");
        // Intercetto la selezione di un elemento del chart
        google.visualization.events.addListener(chart, 'select', selectHandler);
		    // Funzione collegata alla selezione dell'elemento
        function selectHandler(e) {
          var selectedItem = chart.getSelection()[0];
          if (selectedItem) {
            $("option").attr('selected', false);
            var valuesId = data.getValue(selectedItem.row, 2).split("_");
            var valueIdWbs = valuesId[0];
            var valueIdPadre = valuesId[1];
            var isWorkPackage = valuesId[2];
            var popupContent = null;
            var nameSelected = data.getValue(selectedItem.row, 0).split("_");
            $("#wbs-padre option[value=" + valueIdPadre + "]").attr('selected', 'selected');
            $("#wbs-id").attr('value', valueIdWbs);
        <c:choose>
          <c:when test="${superuser}">
         	document.getElementById("nameWbs").innerHTML = nameSelected[0];
          	popupContent = '<ul class="popupMenu">'+
		   				   '<li class="popupMenuContent"><a href="${modWbs}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza WBS</a></li>' +
        				   '<li class="popupMenuContent"><a href="#form-moveWbs" class="linkPopup" id="link-moveWbs" name="link-moveWbs" rel="modal:open" onclick>Sposta WBS</a></li>' +
        				   '<li class="popupMenuContent"><a href="${addWbs}${p.id}&idwp=' + valueIdWbs + '" class="linkPopup">Aggiungi WBS figlia</a></li>' +
        				   '<li class="lastMenuContent"><a href="${act}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza attivit&agrave; di WBS</a></li>' + <%-- (elenco) --%>
      					   '</ul>';
		   	if (isWorkPackage == "true") {
         		popupContent = '<ul class="popupMenu">'+
        				   	   '<li class="popupMenuContent"><a href="${modWbs}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza WBS</a></li>' +
        				   	   '<li class="popupMenuContent"><a href="#form-moveWbs" class="linkPopup" id="link-moveWbs" name="link-moveWbs" rel="modal:open">Sposta WBS</a></li>' +
        				       '<li class="popupMenuContent"><a href="#alertWorkPackage" class="linkPopup" id="link-alert" name="link-alert" rel="modal:open">Aggiungi WBS figlia</a></li>' +
        				   	   '<li class="lastMenuContent"><a href="${act}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza attivit&agrave; di WBS</a></li>' + <%-- (elenco) --%>
        				   	   <%-- '<li class="lastMenuContent"><a href="#form-grafico" class="linkPopup" id="link-form-grafico" name="link-form-grafico" rel="modal:open">Visualizza attivit&agrave; di WBS (grafico)</a></li>' + --%>
        				   	   '</ul>';
		   	   	$("a[href='#']").attr('href', "${modWbs}${p.id}&idw=" + valueIdWbs);
		   	}
          </c:when>
          <c:otherwise>
        	popupContent = '<ul class="popupMenu">'+
 				 		   '<li class="popupMenuContent"><a href="${modWbs}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza WBS</a></li>' +
 				  	 	   '<li class="lastMenuContent"><a href="${act}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza attivit&agrave; di WBS</a></li>' + <%-- (elenco) --%>
					  	   '</ul>';
          	if (isWorkPackage == "true") {
          	  popupContent = '<ul class="popupMenu">'+
        				   	 '<li class="popupMenuContent"><a href="${modWbs}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza WBS</a></li>' +
        				   	 '<li class="lastMenuContent"><a href="${act}${p.id}&idw=' + valueIdWbs + '" class="linkPopup">Visualizza attivit&agrave; di WBS (elenco)</a></li>' +
        			  	 	 <%-- '<li class="lastMenuContent"><a href="#form-grafico" class="linkPopup" id="link-form-grafico" name="link-form-grafico" rel="modal:open">Visualizza attivit&agrave; di WBS (grafico)</a></li>' + --%>
        				   	 '</ul>';
          	}
 	   	  </c:otherwise>
 	    </c:choose>
            popupWindow('Funzioni','popup2', true, popupContent);
            $('a').click( function(e) {
              popupWindow('','popup2', false, '');
            } );
          }
        }
      }
    </script>
    <link href="<c:out value="${initParam.urlDirectoryStili}" />orgchart/orgchart.css" rel="stylesheet" type="text/css"/>
      <style type="text/css">
        .jOrgChart .left {
            border-right: 1px solid black;
        }
        
        .wbs {
            background-color: #3399FF !important;
            color: #e6e6e6;
        }
        
        .jOrgChart .multi-tree span .content {
            background-color: #ffff00 !important;
        }
      </style>
      <script src="<c:out value="${initParam.urlDirectoryScript}" />orgchart/orgchart.js"></script>
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
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" id="show_act">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${timelines}${p.id}">Timelines workpackage</a></li>
    </ul>
    <div id="chart_div" style="overflow-y: scroll;"></div>
    <c:if test="${pageScope.superuser}">
    <div id="form-moveWbs" class="modal">
      <hr class="separatore" />
      <form id="moveWbs" action="" method="post">
        <div class="row">
          <div class="col bordo">Seleziona la WBS padre sulla quale spostare la WBS: <strong><span id="nameWbs"></span></strong></div>
        </div>
        <input type="hidden" id="wbs-id" name="wbs-id" value="" />
        <hr class="separatore" />
        <select class="form-control" id="wbs-padre" name="wbs-padre">
        <c:if test="${idPadre ne -3}">
          <option value="${wbsPadre.id}">${wbsPadre.nome}</option>
        </c:if>
          <option value="">Nessuna wbs padre</option>
        <c:forEach var="singleWbs" items="${requestScope.wbs}" varStatus="status">
          <option value="${singleWbs.id}">${singleWbs.nome}</option>
        </c:forEach>
        </select>
        <hr class="separatore" />
        <button type="submit" class="btn btn-success" id="btn-moveWbs" name="btn-moveWbs">
          <i class="far fa-save"></i>
          Salva
        </button>
      </form>
    </div>
    </c:if>
    <div id="alertWorkPackage" class="modal">
      <hr class="separatore" />
      <div class="alert alert-warning">
        <span class="ui-icon ui-icon-alert"></span>
        Non &egrave; possibile aggiungere una WBS figlia a questa WBS, in quanto &egrave; di tipo Workpackage.<br />
        Per eseguire l'operazione &egrave; necessario togliere il flag di Workpackage nella : <a href="#">pagina di modifica</a>.<br />
        In caso contrario, selezionare una WBS non di tipo Workpackage.
      </div>
    </div>
    <div id="form-grafico" class="modal">
      <hr class="separatore" />
      <div class="scrollX">
        <ul id="tree-data${p.id}" class="noDisplay">
          <li id="progetto${p.id}" class="wbs">
            ${p.titolo}
            <ul>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <div id="popup2" class="popup1">
      <div id="popup1Under" class="popupundertitle">
        <div id="titolopopup1" class="popuptitle" ></div>
        <div id="titolopopup1Under">
          <a href="Javascript:popupWindow('','popup2',false,'');"><img src="web/img/close-icon.gif" border="0" width="15" height="15" alt="Chiudi" title="Chiudi" /></a>
        </div>
      </div>
      <div class="popupbody" id="popup1Text" ></div>
    </div>