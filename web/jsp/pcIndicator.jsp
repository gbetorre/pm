<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:set var="indicatore" value="${requestScope.indicatore}" scope="page" />
<c:set var="types" value="${requestScope.tipi}" scope="page" />
<c:catch var="exception">
<c:choose>
  <c:when test="${not empty indicatore}" >
    <c:set var="tP" value="${indicatore.nome}" scope="page" />
    <c:set var="isOpen" value="${false}" scope="page" />
    <c:if test="${indicatore.idStato eq 1}">
      <c:set var="isOpen" value="${true}" scope="page" />
    </c:if>
  </c:when>
  <c:otherwise>
    <c:set var="tP" value="Inserisci un nuovo indicatore:" scope="page" />
  </c:otherwise>
</c:choose>
    <div id="ind" class="panel">
      <div class="panel-heading bgInd">
        <div class="noHeader"><em><c:out value="${pageScope.tP}" escapeXml="false" /></em></div>
      </div>
      <div class="panel-body">
      <c:choose>
        <c:when test="${not empty indicatore and not isOpen}">
        <div class="avvisiTot">
          Questo indicatore &egrave; chiuso<br />
        </div>
        <div class="alert alert-warning">
          <span class="ui-icon ui-icon-alert"></span>
          Non &egrave; possibile aggiungere misurazioni a questo indicatore,
          n&eacute; modificarlo, in quanto si trova in stato 'chiuso'.<br />
        </div>
        <c:if test="${sessionScope.usr.pmoAteneo}">
        <form id="ind_start" name="ctrl-ind-s" action="" method="post">
          <input type="hidden" id="ind-id" name="ind-id" value="${indicatore.id}" />
          <span>
            <button type="submit" class="btn btn-success" name="start" id="mon-start">
              <i class="fas fa-play"></i> &nbsp;RIATTIVA INDICATORE
            </button>
          </span>
        </form>
        </c:if>
        <hr class="riga" />
        </c:when>
        <c:when test="${not empty indicatore and isOpen}">
        <div class="avvisiTot">Questo indicatore &egrave; aperto</div>
        <c:if test="${sessionScope.usr.pmoAteneo}">
        <form id="ind_end" name="ctrl-ind-e" action="" method="post">
          <input type="hidden" id="ind-id" name="ind-id" value="${indicatore.id}" />
          <span>
            <button type="submit" class="btn btn-success" name="end" id="mon-end">
              <i class="fas fa-square"></i> &nbsp;CHIUDI INDICATORE
            </button>
          </span>
        </form>
        </c:if>
        <hr class="riga" />
        <%@ include file="subButton.jspf" %>
        </c:when>
        <c:otherwise>
        <%@ include file="subButton.jspf" %>
        </c:otherwise>
      </c:choose>
      <form id="ind_form" action="" method="post">
        <input type="hidden" id="ind-id" name="ind-id" value="${indicatore.id}" />
          <div class="row">
            <div class="col-sm-5 mandatory">
              Tipo indicatore:
            </div>
            <div class="col-sm-5">
              <select class="form-control" id="ind-tipo" name="ind-tipo" disabled>
              <c:if test="${indicatore.tipo.id ne -3}">
                <option value="${indicatore.tipo.id}">${indicatore.tipo.nome}</option>
              </c:if>
              <c:forEach var="type" items="${requestScope.tipi}" varStatus="status">
                <c:set var="selected" value="" scope="page" />
                <c:if test="${type.id eq indicatore.idTipo}">
                  <c:set var="selected" value="selected" scope="page" />
                </c:if>
                <option value="${type.id}" ${selected}>${type.nome}</option>
              </c:forEach>
              </select>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Nome Indicatore</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="ind-nome" name="ind-nome" value="${indicatore.nome}" placeholder="Inserisci nome indicatore" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Descrizione</div>
            <div class="col-sm-5">
              <textarea class="form-control" name="ind-descr" placeholder="Inserisci una descrizione per l'indicatore" readonly>${indicatore.descrizione}</textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Baseline</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="ind-baseline" name="ind-baseline" value="${indicatore.baseline}" placeholder="Inserisci valore baseline" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Data Baseline</div>
            <div class="col-sm-5">
              <input type="text" class="form-control calendarData" id="ind-database" name="ind-database" value="<fmt:formatDate value='${indicatore.dataBaseline}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data valore baseline" readonly>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Note','popup1',true,'Anno, solare o accademico, a cui i valori baseline fanno riferimento.');" class="helpInfo" id="annobase">
                Anno Baseline
              </a>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="ind-annobase" name="ind-annobase" value="${indicatore.annoBaseline}" placeholder="Inserisci A.S. o A.A. di baseline" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Target</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="ind-target" name="ind-target" value="${indicatore.target}" placeholder="Inserisci valore target" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Data Target</div>
            <div class="col-sm-5">
              <input type="text" class="form-control calendarData" id="ind-datatarget" name="ind-datatarget" value="<fmt:formatDate value='${indicatore.dataTarget}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data valore target" readonly>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Note','popup1',true,'Anno, solare o accademico, a cui i valori target fanno riferimento.');" class="helpInfo" id="annotarget">
                Anno Target
              </a>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="ind-annotarget" name="ind-annotarget" value="${indicatore.annoTarget}" placeholder="Inserisci A.S. o A.A. di target" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">WBS:</div>
            <div class="col-sm-5">
              <select class="form-control" id="ind-wbs" name="ind-wbs" disabled>
              <option value="">Nessuna wbs</option>
              <c:forEach var="singleWbs" items="${requestScope.azioni}" varStatus="status">
                <c:set var="selected" value="" scope="page" />
                <c:if test="${indicatore.wbs.id eq singleWbs.id}">
                  <c:set var="selected" value="selected" scope="page" />
                </c:if>
                <option value="${singleWbs.id}" ${selected}>${singleWbs.nome}</option>
              </c:forEach>
              </select>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
              <a href="<c:out value="${ind}${requestScope.progetto.id}" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-ruler"></i> Indicatori</a>
            </div>
            <div class="col-sm-5">
            <c:choose>
              <c:when test="${not empty indicatore and not isOpen}">
              <div class="alert alert-warning">
                <span class="ui-icon ui-icon-alert"></span>
                Non &egrave; possibile aggiungere misurazioni a questo indicatore,
                n&eacute; modificarlo, in quanto si trova in stato 'chiuso'.<br />
              </div>
              </c:when>
              <c:otherwise>
                <%@ include file="subPanel.jspf" %>
              </c:otherwise>
            </c:choose>
            </div>
          </div>
          <hr class="separatore" />
      </form>
      </div>
    </div>
    <%@ include file="subPopup.jspf" %>
      </c:catch>
      <c:out value="${exception}" />

      <script type="text/javascript">
        var offsetcharacter = 5;
        // Variabili per formattazione stringhe
        var limNoteTitleOpen = "\n - ";
        var limNoteTitleClose = " -\n";
        var newLine = "\n";
        var separatore = "\n================================\n";
        $(document).ready(function () {
          $('#ind_form').validate ({
            rules: {
              'ind-tipo': {
                required: true
              },
              'ind-nome': {
                required: true,
                minlength: offsetcharacter
              },
              'ind-baseline': {
                required: true
              },
              'ind-target': {
                required: true
              },
            }, 
            messages: {
              'ind-tipo': "Inserire il tipo dell'indicatore",
              'ind-nome': "Inserire il nome dell'indicatore",
              'ind-baseline': "Inserire il valore baseline",
              'ind-target': "Inserire il valore target"
            },
            submitHandler: function (form) {
              return true;
            }
          });
        
          $('textarea:not(#wbs-descnote):not(#wbs-descresult)').keyup(function (e) {
            var len = $(this).val().length;
            var dblength = 8104;
            if(len >= dblength) {
              this.value = this.value.substring(0, dblength);
              $(this).next('div').text(' &egrave; stato raggiunto il limite di caratteri');
            } else {
              var chars = dblength - len;
              $(this).next('div').text(chars + ' caratteri rimanenti');
            }
          });
          
          $('#wbs-name').keyup(function (e) {
            var len = $(this).val().length;
            var dblength = 1024;
            if(len >= dblength) {
              this.value = this.value.substring(0, dblength);
              $(this).next('div').text(' &egrave; stato raggiunto il limite di caratteri');
            } else {
              var chars = dblength - len;
              $(this).next('div').text(chars + ' caratteri rimanenti');
            }
          });
        });
      </script> 