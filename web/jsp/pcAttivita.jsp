<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:set var="distinguishingSubmitId" value="pcAttivita" scope="page" />
    <form id="newAct_form" action="#" method="post">
      <h3>Inserisci una nuova attivit&agrave;:</h3>
      <br />
      <c:set var="skills" value="" scope="page" />
      <div class="row">
        <div class="col-sm-5 mandatory">
          Identificativo della persona 
          <sup>&#10039;</sup>
        </div>
        <div class="col-sm-5">
          <select class="form-control" id="act-people" name="act-people">
          <c:forEach var="person" items="${requestScope.people}" varStatus="status">
            <option value="${person.id}">${person.nome} ${person.cognome}</option>
            <c:if test="${status.index eq pageScope.zero}">
              <c:set var="skills" value="${person.competenze}" scope="page" />
            </c:if>
          </c:forEach>
          </select>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5 mandatory">
          Identificativo del ruolo ricoperto nell'attivit&agrave;
          <sup>&#10039;</sup>
          <div id="act-role-label">
          <c:if test="${pageScope.skills.size() gt zero}">
            (selezionarne uno)
          </c:if>
          </div>
        </div>
        <div class="col-sm-5">
          <select class="form-control" id="act-role" name="act-role" multiple>
          <c:forEach var="skill" items="${pageScope.skills}">
            <option value="${skill.id}"><c:out value="${skill.nome}" /></option>
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
          <div class="col-sm-5"><input type="text" class="form-control" id="act-name" name="act-name" value="" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Descrizione dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <textarea class="form-control" name="act-descr" readonly></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainizio" name="act-datainizio" value="${requestScope.now}" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Data di fine dell'attivit&agrave; <sup>&#10039;</sup></div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafine" name="act-datafine" value="" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datainiziovera" name="act-datainiziovera" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control calendarData" id="act-datafinevera" name="act-datafinevera" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo previsti</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-guprevisti" name="act-guprevisti" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo effettivi</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-gueffettivi" name="act-gueffettivi" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo rimanenti</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="act-gurimanenti" name="act-gurimanenti" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Note di avanzamento</div>
          <div class="col-sm-5">
            <!-- <input type="text" class="form-control" id="actNote" name="actNote" value=""> -->
            <textarea class="form-control" name="act-progress" readonly></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Milestone</div>
          <div class="col-sm-5">
            &nbsp;&nbsp;&nbsp;
            <input type="checkbox" class="form-check-input" id="act-milestone" name="act-milestone">
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5 mandatory">Identificativo WBS <sup>&#10039;</sup></div>
          <div class="col-sm-5">
          <select class="form-control" id="act-wbs" name="act-wbs">
          <c:forEach var="wp" items="${requestScope.wbs}" varStatus="status">
            <option value="${wp.id}">${wp.nome}</option>
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
              <option value="${status.id}">${status.nome}</option>
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
              <option value="${status.id}">${status.nome}</option>
            </c:forEach>
            </select>
          </div>
        </div>
        <br />
        <input type='button' id='btn' value='Chiudi' class="btn btn-primary" onclick="window.self.close();" />
        <%@ include file="subPanel.jspf" %>
      </div>
    </form>
    <script type="text/javascript">
      $(document).ready(function () {
          $('${pageScope.distinguishingSubmitId}').click(function () {
              window.opener.location.reload(true);
              window.self.close();
          });
      });
    </script>
    <script type="text/javascript">
    var offsetcharacter = 5;
    $(document).ready(function () {
      $('#newAct_form').validate ({
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
            $(this).next('div').text(' è stato raggiunto il limite di caratteri');
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