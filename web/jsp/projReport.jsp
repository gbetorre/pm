<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
    <c:set var="divs" value="21,22,23,24,25,26" scope="page" />
    <c:set var="caption" scope="page">
      CHIUSA DA PI&Ugrave; TEMPO: attivit&agrave; chiusa da oltre 30 giorni rispetto la data in base alla quale il report fa riferimento.
      &lt;hr class=riga&gt;
      CHIUSA ENTRO IL MESE PRECEDENTE: attivit&agrave; chiusa entro i 30 giorni rispetto la data in base alla quale il report fa riferimento.
      &lt;hr class=riga&gt;
      IN RITARDO: attivit&agrave; in ritardo rispetto alle date previste.
      &lt;hr class=riga&gt;
      IN REALIZZAZIONE: attivit&agrave; con data di inizio effettiva non vuota e data di fine effettiva ancora da inserire.
      &lt;hr class=riga&gt;
      PROSSIMO MESE: attivit&agrave; che sono previste per una data successiva di massimo 30 giorni rispetto alla data in base alla quale il report fa riferimento.
      &lt;hr class=riga&gt;
      MESI SUCCESSIVI: attivit&agrave; che sono previste per una data successiva di oltre 30 giorni rispetto alla data in base alla quale il report fa riferimento.
    </c:set>
        <form id="report_form" action="#" method="post">
          <div id="prepareReport">
          <hr class="separatore" />
            <h2 align="center">
              Report del sotto progetto&nbsp;<strong><c:out value="${p.titolo}" /></strong>
              <span class="float-right">
                <a class="btn btnNav" href="${project}">
                  <i class="fas fa-home"></i>
                  Progetti
                </a>
              </span>
            </h2>
            <h4 align="center">
              <span class="avvisiTot">&nbsp; alla data <fmt:formatDate value="${selectionDate}" pattern="dd/MM/yyyy" /> &nbsp;</span>
            </h4>
            <hr class="riga" />
            <div class="row">
              <div class="col-4 avvisiTot"><label for="">Seleziona la data sulla quale calcolare il report:</label></div>
              <div class="col-3">
                <input type="text" class="form-control calendarData" id="rep-date" name="rep-date" value="<fmt:formatDate value="${selectionDate}" pattern="dd/MM/yyyy" />">
              </div>
              <div class="col-5">
                <a href="" class="btn btn-success" id="rep-view"><i class="fas fa-search"></i> Visualizza</a>
                <%--
                <button class="btn btn-warning" id="expReport" onclick="alert('Funzionalit&agrave; in corso di implementazione');"><i class="fas fa-file-export"></i> Esporta</button>
                --%>
              </div>
            </div>
            <hr class="separatore" />
          </div>
          <div class="row reportHead" id="headTable">
            <div class="col-sm-2">
              Workpackage
            </div>
            <div class="col-sm-4">
              Nome attivit&agrave;
            </div>
            <div class="col-sm-6 centerlayout">
              <a href="javascript:popupWindowReport('Note','popup1',true,'${caption}');" class="helpInfo" id="helpRep">Stato attivit&agrave;</a>
            </div>
          </div>
          <div class="row reportStateAct" id="headState">
            <div class="col-sm-6">
            </div>
            <div class="col-sm-1 bgSts21">
              Chiusa da pi&ugrave; tempo
            </div>
            <div class="col-sm-1 bgSts22">
              Chiusa entro il mese precedente
            </div>
            <div class="col-sm-1 bgSts23">
              In ritardo
            </div>
            <div class="col-sm-1 bgSts24">
             In realizzazione
            </div>
            <div class="col-sm-1 bgSts25">
              Prossimo mese
            </div>
            <div class="col-sm-1 bgSts26">
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
                  <div class="col-1 bgSts${divSts} ${bgAct}" title="Dal <fmt:formatDate value='${act.dataInizioAttesa}' pattern='dd/MM/yyyy' /> al <fmt:formatDate value='${act.dataFineAttesa}' pattern='dd/MM/yyyy' />"></div>
                </c:forTokens>
              </div>
            </c:forEach>
          </c:forEach>
          <hr class="separatore" />
          <div class="form-row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btnNav" href="${project}">
                  <i class="fas fa-home"></i>
                  Progetti
                </a>
              </span>
            </div>
          </div>
        </form>
    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript">
      $(document).ready(function() {
        $('input[type=\'text\'].calendarData').datepicker();
        $("#rep-view").click(function () {
          var $dateValue = $("input[name='rep-date']").val();
          var $repView = '<c:out value="${report}${p.id}" escapeXml="false" />' + "&d=" + $dateValue;
          $('#rep-view').attr('href', $repView);
    	  });
      });
      
      var headTableHeight = $("#idHeader").height() + $("#prepareReport").height();
      var headStateHeight = headTableHeight + $("#headTable").height();
      $(window).scroll(function(e) {
        var scroll = $(window).scrollTop();
        if (scroll >= headTableHeight) {
            $('#headTable').addClass("fixedTopTable");
        } else {
            $('#headTable').removeClass("fixedTopTable");
        }
        if (scroll >= headStateHeight) {
            $('#headState').addClass("fixedTopState");
        } else {
            $('#headState').removeClass("fixedTopState");
        }
      });
      
      function ShowContentReport(d, tipo) {
        if(d.length < 1) { return; }
        var dd = document.getElementById(d);
        AssignPosition(dd, tipo);
        dd.style.display = "block";
        dd.style.height = "700px";
      }
      
      function popupWindowReport(tit,o,d,t) {
        // o - Object to display.
        // d - Display, true =  display, false = hide
        // t - Text to display in the popup
        var obj = document.getElementById(o);
        var iltitolo = document.getElementById("titolopopup1");
        var contenuto = document.getElementById("popup1Text");
        
        if(d) {
            /*
            obj.style.display = 'block';
            obj.style.visibility = 'visible';
            iltitolo.innerHTML = tit;
            contenuto.innerHTML = t;
            obj.style.width= "350px";
            obj.style.height = "200px";
            obj.style.left = ((screen.availWidth - 700)/2);
            obj.style.top = ((screen.availHeight - 400)/2);
            */
            ShowContentReport(obj.id,tit);
            iltitolo.innerHTML = tit;
            contenuto.innerHTML = unescape(t);
        } else {
            /*
            contenuto.innerHTML = '';
            obj.style.display = 'none';
            obj.style.visibility = 'hidden';
            */
            contenuto.innerHTML = "";
            HideContent(obj.id);
          }
      }
    </script>