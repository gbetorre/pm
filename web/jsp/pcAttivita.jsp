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
    <c:set var="tP" value="Modifica attivit&agrave; &quot;${actName}&quot;:" scope="page" />
  </c:when>
  <c:otherwise>
    <c:set var="actStartDate" value="${requestScope.now}" scope="page" />
    <c:set var="tP" value="Inserisci una nuova attivit&agrave;:" scope="page" />
  </c:otherwise>
  </c:choose>
    <h3><c:out value="${pageScope.tP}" escapeXml="false" /></h3>
    <br />
    <form id="act_form" action="" method="post" class="subfields">
      <input type="hidden" id="act-id" name="act-id" value="${actInstance.id}" />
      <c:set var="stato" value="" scope="page" />
      <c:forEach var="status" items="${requestScope.statiAttivita}" varStatus="loop">
        <c:if test="${status.id eq state}">
          <c:set var="stato" value="${status.nome}" scope="page" />
        </c:if> 
      </c:forEach>
      <div class="avvisiTot"><c:out value="Stato: ${stato}" /></div>
      <div class="row">
        <div class="col-sm-5 mandatory">
          Identificativo della persona 
          <sup>&#10039;</sup>
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
          <sup>&#10039;</sup>
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
          <div class="col-sm-5 mandatory">Nome attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-name" name="act-name" value="${actName}" readonly="readonly"> <%-- readonly="readonly" --%>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Descrizione dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-descr" class="form-control" aria-label="With textarea" maxlength="8104" readonly><c:out value="${actDescr}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data prevista di inizio dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainizio" name="act-datainizio" value="<fmt:formatDate value='${actStartDate}' pattern='dd/MM/yyyy' />" readonly> <%--readonly--%>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Data prevista di fine dell'attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafine" name="act-datafine" value="<fmt:formatDate value='${actEndDate}' pattern='dd/MM/yyyy' />" readonly> <%--readonly--%>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainiziovera" name="act-datainiziovera" value="<fmt:formatDate value='${actualStartDate}' pattern='dd/MM/yyyy' />" readonly> <%--readonly--%>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafinevera" name="act-datafinevera" value="<fmt:formatDate value='${actualEndDate}' pattern='dd/MM/yyyy' />" readonly> <%--readonly--%>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Note di avanzamento</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-progress" class="form-control" aria-label="With textarea" maxlength="8104" readonly><c:out value="${actNotes}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Risultati raggiunti</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-result" class="form-control" aria-label="With textarea" maxlength="1024" readonly><c:out value="${actResult}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Milestone</div>
          <div class="col-sm-5" style="margin-left:25px;">
            <input type="checkbox" class="form-check-input" id="act-milestone" name="act-milestone" <c:out value='${checked}' />>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">WBS <sup>&#10039;</sup></div>
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
          <div class="col-sm-5 mandatory">Complessit&agrave; dell'attivit&agrave; <sup>&#10039;</sup></div>
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
        <a href="<c:out value= "${act}${requestScope.progetto.id}" escapeXml="false" />" id='btn-close' class="btn btnNav">Chiudi</a>
        <input type="button" class="btn btn-primary" name="modifica" value="Modifica" onclick="modify();">
        <%@ include file="subPanel.jspf" %>
    </form>
</c:catch>
<c:out value="${exception}" />
    <script type="text/javascript">
    var offsetcharacter = 5;
    // Adesso
    var rightNow = new Date();
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
    // Data a partire dalla quale sono iniziati i progetti di eccellenza
    //var start = new Date('2018-01-01');
    // Millisecondi trascorsi da UNIX_EPOCH fino a start progetti
    //var startAsMillis = start.getTime();
    // Giorni trascorsi da UNIX_EPOCH fino a start
    //var elapsedDaysToStart = Math.round(startAsMillis/(1000*60*60*24));
    // Giorni trascorsi da start progetti fino ad oggi
    //var elapsedDaysFromStartToNow = elapsedDays - elapsedDaysToStart;
    // Rappresentazione a stringa della data di oggi, in formato italiano
    var todayAsString = rightNow.getDate() + "/" + (1 + rightNow.getMonth()) + "/" + rightNow.getFullYear();
    // Corpo del programma
    $(document).ready(function () {
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
        //'act-guprevisti': {
        //  number: true
        //},
        //'act-gueffettivi': {
        //  number: true
        //},
        //'act-gurimanenti': {
        //  number: true
        //}
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
        'act-name': "Inserire il nome dell\'attivita\'" + "--" + dayOfMonth + "--" + elapsedDays + $("#act-datainiziovera").val(),
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
      
      $('textarea[maxlength]').keyup(function () {
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
    </script>