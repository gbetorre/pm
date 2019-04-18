<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ include file="pcURL.jspf" %>
<c:set var="distinguishingSubmitId" value="pcAttivita" scope="page" />
<c:set var="actInstance" value="${requestScope.singolaAttivita}" scope="page" />
<jsp:useBean id="personInCharge" class="it.alma.bean.PersonBean" scope="page" />
<c:set target="${pageScope.personInCharge}" property="id" value="-3" />
<c:set target="${pageScope.personInCharge}" property="idQualificaPrincipaleDip" value="-3" />
<c:set var="personInCharge" value="${personInCharge}" scope="page" />
<c:catch var="exception">
  <c:forEach var="chosenOne" items="${actInstance.persone}" begin="0" end="0">
    <c:set var="personInCharge" value="${chosenOne}" scope="page" />
  </c:forEach>
  <c:set var="actName" value="" scope="page" />
  <c:set var="actDescr" value="" scope="page" />
  <c:set var="actEndDate" value="" scope="page" />
  <c:set var="actualStartDate" value="" scope="page" />
  <c:set var="actualEndDate" value="" scope="page" />
  <c:set var="actDays" value="" scope="page" />
  <c:set var="elapsedDays" value="" scope="page" />
  <c:set var="remainingDays" value="" scope="page" />
  <c:set var="actNotes" value="" scope="page" />
  <c:set var="actResult" value="" scope="page" />
  <c:set var="checked" value="" scope="page" />
  <c:set var="wbs" value="-3" scope="page" />
  <c:set var="complexity" value="-3" scope="page" />
  <c:set var="state" value="-3" scope="page" />
  <c:set var="tP" value="" scope="page" />
  <c:choose>
  <c:when test="${not empty actInstance}">
    <c:set var="actName" value="${actInstance.nome}" scope="page"  />
    <c:set var="actDescr" value="${actInstance.descrizione}" scope="page" />
    <c:set var="actStartDate" value="${actInstance.dataInizio}" scope="page" />
    <c:set var="actEndDate" value="${actInstance.dataFine}" scope="page" />
    <c:set var="actualStartDate" value="${actInstance.dataInizioEffettiva}" scope="page" />
    <c:set var="actualEndDate" value="${actInstance.dataFineEffettiva}" scope="page" />
    <c:set var="actDays" value="${actInstance.guPrevisti}" scope="page" />
    <c:set var="elapsedDays" value="${actInstance.guEffettivi}" scope="page" />
    <c:set var="remainingDays" value="${actInstance.guRimanenti}" scope="page" />
    <c:set var="actNotes" value="${actInstance.noteAvanzamento}" scope="page" />
    <c:set var="actResult" value="${actInstance.risultatiRaggiunti}" scope="page" />
    <c:if test="${actInstance.milestone}">
      <c:set var="checked" value="checked" scope="page" />
    </c:if>
    <c:set var="wbs" value="${actInstance.idWbs}" scope="page" />
    <c:set var="complexity" value="${actInstance.idComplessita}" scope="page" />
    <c:set var="state" value="${actInstance.idStato}" scope="page" />
    <c:set var="tP" value="${actName}:" scope="page" />
  </c:when>
  <c:otherwise>
    <c:set var="actStartDate" value="${requestScope.now}" scope="page" />
    <c:set var="tP" value="Inserisci una nuova attivit&agrave;:" scope="page" />
  </c:otherwise>
  </c:choose>
    <br />
    <form id="act_form" action="" method="post" class="panel">
      <input type="hidden" id="act-id" name="act-id" value="${actInstance.id}" />
      <c:set var="stato" value="" scope="page" />
      <c:forEach var="status" items="${requestScope.statiAttivita}" varStatus="loop">
        <c:if test="${status.id eq state}">
          <c:set var="stato" value="${status.nome}" scope="page" />
        </c:if> 
      </c:forEach>
      <div class="panel-heading bgAddAct bgAct${state}">
        <div class="noHeader"><em><c:out value="${pageScope.tP}" escapeXml="false" /></em></div>
        <div class="actstate">
        <c:choose>
        <c:when test="${not empty actInstance}">
        <c:out value="Stato: ${stato}" />
        </c:when>
        <c:otherwise>
        <c:out value="Stato: In apertura" />
        </c:otherwise>
        </c:choose>
        </div>
      </div>
      <hr class="separatore" />
      <div class="panel-body">
        <%@ include file="subButton.jspf" %>
        <div class="row">
          <div class="col-sm-5 mandatory">
            Identificativo della persona 
          </div>
          <div class="col-sm-5">
            <select class="form-control" id="act-people" name="act-people" disabled>
            <c:forEach var="person" items="${requestScope.people}" varStatus="status">
              <c:set var="selected" value="" scope="page" />
              <c:if test="${person.id eq personInCharge.id}">
                <c:set var="selected" value="selected" scope="page" />
              </c:if>
              <option value="${person.id}" ${selected}>
                <c:out value="${person.nome} ${person.cognome}" />
              </option>
              <c:choose>
                <c:when test="${empty actInstance and (status.index eq pageScope.zero)}">
                  <c:set var="skills" value="${person.competenze}" scope="page" />
                </c:when>
                <c:when test="${not empty actInstance}">
                  <c:set var="skills" value="${personInCharge.competenze}" scope="page" />
                </c:when>
              </c:choose>
            </c:forEach>
            </select>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">
            Identificativo del ruolo ricoperto nell'attivit&agrave;
            <c:if test="${pageScope.skills.size() gt zero}">
            <div id="act-role-label">
              (selezionarne uno)
            </div>
            </c:if>
          </div>
          <div class="col-sm-5">
            <select class="form-control" id="act-role" name="act-role" multiple disabled>
            <c:forEach var="skill" items="${pageScope.skills}">
              <c:set var="selected" value="" scope="page" />
              <c:if test="${skill.id eq personInCharge.idQualificaPrincipaleDip}">
                <c:set var="selected" value="selected" scope="page" />
              </c:if>
              <option value="${skill.id}" ${selected}>
                <c:out value="${skill.nome}" />
              </option>
            </c:forEach>
            </select>
          </div>
        </div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-5 mandatory">Nome attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-name" name="act-name" value="${actName}" placeholder="Inserisci il nome" readonly="readonly">
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Descrizione dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-descr" class="form-control" aria-label="With textarea" maxlength="8104" placeholder="Inserisci una descrizione" readonly><c:out value="${actDescr}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data prevista di inizio dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainizio" name="act-datainizio" value="<fmt:formatDate value='${actStartDate}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data in cui prevedi di iniziare" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Data prevista di fine dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafine" name="act-datafine" value="<fmt:formatDate value='${actEndDate}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data in cui prevedi di finire" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainiziovera" name="act-datainiziovera" value="<fmt:formatDate value='${actualStartDate}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data di inizio lavoro" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafinevera" name="act-datafinevera" value="<fmt:formatDate value='${actualEndDate}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data di fine lavoro" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Note di avanzamento</div>
          <div class="col-sm-5">
            <textarea class="form-control noActivateOnAdd" id="act-progress" name="act-progress" aria-label="With textarea" maxlength="8104" placeholder="Inserisci le note o clicca sul bottone +" readonly><c:out value="${actNotes}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
          <a class="ico" id="act-addNote">
            <img src="${initParam.urlDirectoryImmagini}/ico-add-inactive.png" class="btn-del addElement" alt="Link ad aggiunta nota" title="Aggiungi nota"/>
          </a>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Risultati raggiunti</div>
          <div class="col-sm-5">
            <textarea class="form-control noActivateOnAdd" id="act-result" name="act-result" aria-label="With textarea" maxlength="1024" placeholder="Inserisci i risultati o clicca sul bottone +" readonly><c:out value="${actResult}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
          <a class="ico" id="act-addResult">
            <img src="${initParam.urlDirectoryImmagini}/ico-add-inactive.png" class="btn-del addElement" alt="Link ad aggiunta risultati" title="Aggiungi risultati"/>
          </a>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <a href="javascript:popupWindow('Note','popup1',true,'Milestone indica che l\'attivit&agrave; &egrave; usata per segnalare un momento significativo del sottoprogetto, p.es. il termine di una fase preliminare. Le Milestone dovrebbero iniziare e terminare nello stesso giorno, cio&egrave; essere attivit&agrave; di durata uno.');" class="helpInfo" id="milestone">
              Milestone
            </a>
          </div>
          <div class="col-sm-5" style="margin-left:25px;">
            <input type="checkbox" class="form-check-input" id="act-milestone" name="act-milestone" <c:out value='${checked}' />>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">WBS</div>
          <div class="col-sm-5">
            <select class="form-control" id="act-wbs" name="act-wbs" disabled>
            <c:forEach var="wp" items="${requestScope.wbs}" varStatus="status">
              <c:set var="selected" value="" scope="page" />
              <c:if test="${wp.id eq wbs}">
                <c:set var="selected" value="selected" scope="page" />
              </c:if>          
              <option value="${wp.id}" ${selected}>${wp.nome}</option>
            </c:forEach>
            </select>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Complessit&agrave; dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <select class="form-control" id="act-compl" name="act-compl" disabled>
            <c:forEach var="status" items="${requestScope.complessita}" varStatus="loop">
              <c:set var="selected" value="" scope="page" />
              <c:if test="${status.id eq complexity}">
                <c:set var="selected" value="selected" scope="page" />
              </c:if>  
              <option value="${status.id}" ${selected}>${status.nome}</option>
            </c:forEach>
            </select>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
            <a href="<c:out value="${project}" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-home"></i> Progetti</a>
          </div>
          <div class="col-sm-5">
            <%@ include file="subPanel.jspf" %>
          </div>
        </div>
      </div>
    </form>
    <div id="note-div" class="modal">
      <h3 class="heading">Aggiungi una nota di avanzamento</h3>
      <br />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Data
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="text" class="form-control calendarData" id="act-datanota" name="act-datafinevera" pattern='dd/MM/yyyy' placeholder="Inserisci una data nota">
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Titolo Nota
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="text" class="form-control" id="act-titlenote" name="act-titlenote" value="" placeholder="Inserisci un titolo nota">
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Descrizione
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <textarea class="form-control" id="act-descnote" name="act-descnote" class="form-control" aria-label="With textarea" maxlength="8104" placeholder="Inserisci una descrizione nota"></textarea>
          <div class="charNum"></div>
        </div>
      </div>
      <hr class="separatore" />
      <a href="<c:out value="${redirect}" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="far fa-window-close"></i> Chiudi</a>
      <a href="#" class="btn btn-success" id="btn-add" onclick="addNote()" rel="modal:close"><i class="fas fa-plus"></i> Aggiungi</a>
    </div>
    <div id="result-div" class="modal">
      <h3 class="heading">Aggiungi un risultato raggiunto</h3>
      <br />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Data
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="text" class="form-control calendarData" id="act-dataresult" name="act-dataresult" pattern='dd/MM/yyyy' placeholder="Inserisci una data risultato">
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Risultato finale
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <textarea class="form-control" id="act-descresult" name="act-descresult" class="form-control" aria-label="With textarea" maxlength="8104" placeholder="Inserisci una descrizione del risultato"></textarea>
          <div class="charNum"></div>
        </div>
      </div>
      <hr class="separatore" />
      <a href="<c:out value="${redirect}" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="far fa-window-close"></i> Chiudi</a>
      <a href="#" class="btn btn-success" id="btn-add" onclick="addResult()" rel="modal:close"><i class="fas fa-plus"></i> Aggiungi</a>
    </div>
    <%@ include file="subPopup.jspf" %>
</c:catch>
<c:out value="${exception}" />
    <script type="text/javascript">
    var offsetcharacter = 2;
    // Adesso
    var rightNow = new Date();
    // Giorno odierno
    var dayOfMonth = rightNow.getUTCDate();
    // Millisecondi trascorsi da UNIX_EPOCH
    var elapsedTimeFromUnixEpochAsMillisec = Date.now();
    // Secondi trascorsi da UNIX_EPOCH
    var elapsedSeconds = elapsedTimeFromUnixEpochAsMillisec/1000;
    // Minuti trascorsi da UNIX_EPOCH
    var elapsedMinutes = elapsedSeconds/60;
    // Ore trascorse da UNIX_EPOCH
    var elapsedHours = elapsedMinutes/60;
    // Giorni trascorsi da UNIX_EPOCH
    var elapsedDays = elapsedHours/24;
    // Mesi trascorsi da UNIX_EPOCH
    var elapsedMonths = elapsedDays/30;
    // Rappresentazione a stringa della data di oggi, in formato italiano
    var todayAsString = rightNow.getDate() + "/" + (1 + rightNow.getMonth()) + "/" + rightNow.getFullYear();
    // Variabili per formattazione stringhe
    var limNoteTitleOpen = "\n - ";
    var limNoteTitleClose = " -\n";
    var newLine = "\n";
    var separatore = "\n================================\n";
    // Corpo del programma
    $(document).ready(function () {
      $('#btn-add, #btnBack').click(function (e){
        e.preventDefault;
      });
      
      $('#act_form').validate ({
      rules: {
        'act-role': {
          required: true
        },
        'act-name': {
          required: true,
          minlength: offsetcharacter
        },
        'act-datainizio': {
          dateITA: true
        },
        'act-datafine': {
          dateITA: true,
          required: true
        },
        'act-datainiziovera': {
          dateITA: true,
          lessThan: true
        },
        'act-datafinevera': {
          dateITA: true,
          lessThan: true
        }
      }, 
      messages: {
        'act-role': "Scegliere una competenza (ruolo) per la persona selezionata",
        'act-name': "Inserire il nome dell\'attivita\'",
        'act-datainizio': "Inserire una data di inizio valida",
        'act-datafine': "Inserire una data di fine valida",
        'act-datainiziovera': "La data inserita deve essere in formato italiano e non pu&ograve; essere maggiore della data odierna (" + todayAsString + ")",
        'act-datafinevera': "La data inserita deve essere in formato italiano e non pu&ograve; essere maggiore della data odierna (" + todayAsString + ")",
        'act-gueffettivi': "Inserire un valore numerico",
        'act-gurimanenti': "Inserire un valore numerico"
      },
      submitHandler: function (form) {
        return true;
      }
      });
      
      $('textarea[maxlength]:not(#act-descnote):not(#act-descresult)').keyup(function () {
        var len = $(this).val().length;
        var dblength = parseInt($(this).attr('maxlength'));
        if(len >= dblength) {
          this.value = this.value.substring(0, dblength);
          $(this).next('div').text(' hai raggiunto il limite massimo di caratteri inseribili');
        } else {
          var chars = dblength - len;
          $(this).next('div').text(chars + ' caratteri rimanenti');
        }
      });
      
      $('#act-descnote').keyup(function() {
        var len = $(this).val().length + $('#act-progress').val().length + $('#act-datanota').val().length + $('#act-titlenote').val().length + limNoteTitleOpen.length + limNoteTitleClose.length + separatore.length;
        var dblength = parseInt($(this).attr('maxlength'));
        if(len >= dblength) {
          this.value = this.value.substring(0, dblength);
          $(this).next('div').text(' hai raggiunto il limite massimo di caratteri inseribili');
        } else {
          var chars = dblength - len;
          $(this).next('div').text(chars + ' caratteri rimanenti');
        }
      });
      
      $('#act-descresult').keyup(function() {
        var len = $(this).val().length + $('#act-result').val().length + $('#act-dataresult').val().length + newLine.length + separatore.length;
        var dblength = parseInt($(this).attr('maxlength'));
        if(len >= dblength) {
          this.value = this.value.substring(0, dblength);
          $(this).next('div').text(' hai raggiunto il limite massimo di caratteri inseribili');
        } else {
          var chars = dblength - len;
          $(this).next('div').text(chars + ' caratteri rimanenti');
        }
      });
    });
    
    $("#act-people").change(function () {
      switch($(this).val()) {
      <c:forEach var="person" items="${requestScope.people}">
        case '${person.id}':
          $("#act-role").html("<c:forEach var="skill" items="${person.competenze}"><option value='${skill.id}'>${skill.nome}</option></c:forEach>");
        <c:choose>
        <c:when test="${person.competenze.size() gt 1}">
          $("#act-role-label").append("(selezionarne uno)");
        </c:when>
        <c:otherwise>
          $("#act-role-label").text("");
        </c:otherwise>
        </c:choose>
          break;
       </c:forEach>    
       }
    });
    
    function addNote() {
      var currentText = $('#act-progress').val();
      if ($('#act-datanota').val().length < 10 || $('#act-titlenote').val().length < 1 || $('#act-descnote').val() < 1) {
        alert("Tutti i campi sono obbligatori!");
        $('#act-progress').val(currentText);
      } else {
        var newText = $('#act-datanota').val() + limNoteTitleOpen + $('#act-titlenote').val() + limNoteTitleClose + $('#act-descnote').val() + separatore + currentText;
        $('#act-progress').val(newText);
        $('#act-datanota').val("");
        $('#act-titlenote').val("");
        $('#act-descnote').val("");
      }
    };
    
    function addResult() {
      var currentText = $('#act-result').val();
      if ($('#act-dataresult').val().length < 1 || $('#act-descresult').val().length < 1) {
        alert("Tutti i campi sono obbligatori!");
        $('#act-result').val(currentText);
      } else {
        var newText = $('#act-dataresult').val() + newLine + $('#act-descresult').val() + separatore + currentText;
        $('#act-result').val(newText);
        $('#act-dataresult').val("");
        $('#act-descresult').val("");
        
      }
    };
    </script>