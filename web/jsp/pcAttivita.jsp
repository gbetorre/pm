<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ include file="pcURL.jspf" %>
<c:set var="distinguishingSubmitId" value="pcAttivita" scope="page" />
<c:set var="actToEdit" value="${requestScope.singolaAttivita}" scope="page" />
<jsp:useBean id="personInCharge" class="it.alma.bean.PersonBean" scope="page" />
<c:set target="${pageScope.personInCharge}" property="id" value="-3" />
<c:set target="${pageScope.personInCharge}" property="idQualificaPrincipaleDip" value="-3" />
<c:set var="personInCharge" value="${personInCharge}" scope="page" />
<c:catch var="exception">
  <c:forEach var="chosenOne" items="${actToEdit.persone}" begin="0" end="0">
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
  <c:set var="checked" value="" scope="page" />
  <c:set var="wbs" value="-3" scope="page" />
  <c:set var="complexity" value="-3" scope="page" />
  <c:set var="state" value="-3" scope="page" />
  <c:set var="tP" value="" scope="page" />
  <c:choose>
  <c:when test="${not empty actToEdit}">
    <c:set var="actName" value="${actToEdit.nome}" scope="page"  />
    <c:set var="actDescr" value="${actToEdit.descrizione}" scope="page" />
    <c:set var="actStartDate" value="${actToEdit.dataInizio}" scope="page" />
    <c:set var="actEndDate" value="${actToEdit.dataFine}" scope="page" />
    <c:set var="actualStartDate" value="${actToEdit.dataInizioEffettiva}" scope="page" />
    <c:set var="actualEndDate" value="${actToEdit.dataFineEffettiva}" scope="page" />
    <c:set var="actDays" value="${actToEdit.guPrevisti}" scope="page" />
    <c:set var="elapsedDays" value="${actToEdit.guEffettivi}" scope="page" />
    <c:set var="remainingDays" value="${actToEdit.guRimanenti}" scope="page" />
    <c:set var="actNotes" value="${actToEdit.noteAvanzamento}" scope="page" />
    <c:if test="${actToEdit.milestone}">
      <c:set var="checked" value="checked" scope="page" />
    </c:if>
    <c:set var="wbs" value="${actToEdit.idWbs}" scope="page" />
    <c:set var="complexity" value="${actToEdit.idComplessita}" scope="page" />
    <c:set var="state" value="${actToEdit.idStato}" scope="page" />
    <c:set var="tP" value="Modifica attivit&agrave; &quot;${actName}&quot;:" scope="page" />
  </c:when>
  <c:otherwise>
    <c:set var="actStartDate" value="${requestScope.now}" scope="page" />
    <c:set var="tP" value="Inserisci una nuova attivit&agrave;:" scope="page" />
  </c:otherwise>
  </c:choose>
    <h3><c:out value="${pageScope.tP}" escapeXml="false" /></h3>
    <br />
    <form id="act_form" action="#" method="post">
      <div class="row">
        <div class="col-sm-5 mandatory">
          Identificativo della persona 
          <sup>&#10039;</sup>
        </div>
        <div class="col-sm-5">
          <select class="form-control" id="act-people" name="act-people">
          <c:forEach var="person" items="${requestScope.people}" varStatus="status">
            <c:set var="selected" value="" scope="page" />
            <c:if test="${person.id eq personInCharge.id}">
              <c:set var="selected" value="selected" scope="page" />
            </c:if>
            <option value="${person.id}" ${selected}>
              <c:out value="${person.nome} ${person.cognome}" />
            </option>
            <c:choose>
              <c:when test="${empty actToEdit and (status.index eq pageScope.zero)}">
                <c:set var="skills" value="${person.competenze}" scope="page" />
              </c:when>
              <c:when test="${not empty actToEdit}">
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
          <select class="form-control" id="act-role" name="act-role" multiple>
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
      <div class="row">
        <div class="col-sm-5">&nbsp;</div>
        <div class="col-sm-5">
          <input type="button" id="show-extrainfo" class="btn extrainfo" value="Altre informazioni" onclick="modify()">
        </div>
      </div>
      <hr />
      <div class="additional-fields">
        <div class="row">
          <div class="col-sm-5 mandatory">Nome attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-name" name="act-name" value="${actName}" readonly="readonly">
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Descrizione dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-descr" readonly><c:out value="${actDescr}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainizio" name="act-datainizio" value="<fmt:formatDate value='${actStartDate}' pattern='dd/MM/yyyy' />" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Data di fine dell'attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafine" name="act-datafine" value="<fmt:formatDate value='${actEndDate}' pattern='dd/MM/yyyy' />" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainiziovera" name="act-datainiziovera" value="<fmt:formatDate value='${actualStartDate}' pattern='dd/MM/yyyy' />" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafinevera" name="act-datafinevera" value="<fmt:formatDate value='${actualEndDate}' pattern='dd/MM/yyyy' />" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo previsti</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-guprevisti" name="act-guprevisti" value="${actDays}" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo effettivi</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-gueffettivi" name="act-gueffettivi" value="${elapsedDays}" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo rimanenti</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-gurimanenti" name="act-gurimanenti" value="${remainingDays}" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Note di avanzamento</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-progress" readonly><c:out value="${actNotes}" escapeXml="false" /></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Milestone</div>
          <div class="col-sm-5">
            &nbsp;&nbsp;&nbsp;
            <input type="checkbox" class="form-check-input" id="act-milestone" name="act-milestone" <c:out value='${checked}' />>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Identificativo WBS <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <select class="form-control" id="act-wbs" name="act-wbs">
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
          <div class="col-sm-5 mandatory">Identificativo complessit&agrave; dell'attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <select class="form-control" id="act-compl" name="act-compl">
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
          <div class="col-sm-5 mandatory">Stato attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <select class="form-control" id="act-status" name="act-status">
            <c:forEach var="status" items="${requestScope.statiAttivita}" varStatus="loop">
              <c:set var="selected" value="" scope="page" />
              <c:if test="${status.id eq state}">
                <c:set var="selected" value="selected" scope="page" />
              </c:if> 
              <option value="${status.id}" ${selected}>${status.nome}</option>
            </c:forEach>
            </select>
          </div>
        </div>
        <br />
        <a href="<c:out value= "${act}${requestScope.progetto.id}" escapeXml="false" />" id='btn-close' class="btn btn-primary">Chiudi</a>
        <%@ include file="subPanel.jspf" %>
      </div>
    </form>
</c:catch>
<c:out value="${exception}" />
    <script type="text/javascript">
    var offsetcharacter = 5;
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
          required: true
        },
        'act-datafine': {
          required: true
        },
        'act-guprevisti': {
          number: true
        },
        'act-gueffettivi': {
          number: true
        },
        'act-gurimanenti': {
          number: true
        }
      }, 
      messages: {
        'act-role': "Scegliere una competenza (ruolo) per la persona selezionata",
        'act-name': "Inserire il nome dell\'attivita\'",
        'act-datainizio': "Inserire una data di inizio valida",
        'act-datafine': "Inserire una data di fine valida",
        'act-guprevisti': "Inserire un valore numerico",
        'act-gueffettivi': "Inserire un valore numerico",
        'act-gurimanenti': "Inserire un valore numerico"
      },
      submitHandler: function (form) {
        return true;
      }
      });
      
      $('textarea').keyup(function (e) {
          var len = $(this).val().length;
          var dblength = 16384;
          if(len >= dblength) {
            this.value = this.value.substring(0, dblength);
            $(this).next('div').text(' � stato raggiunto il limite di caratteri');
          } else {
            var chars = dblength - len;
            $(this).next('div').text(chars + ' caratteri rimanenti');
          }
      });
    });

    $('.extrainfo').click(function() {
      var $body = $('.additional-fields');
      if ($body.is(':hidden')) {
        $body.show();
      } else {
        //$body.show();
      }
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