<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
    <form id="newAct_form" action="#" method="post">
      <h3>Inserisci una nuova attivit&agrave;:</h3>
      <br />
      <div class="row">
        <div class="col-sm-5">Identificativo della persona <sup>&star;</sup></div>
        <div class="col-sm-5">
          <select class="form-control" id="sRischi" name="sRischi">
          <c:forEach var="person" items="${requestScope.people}">
            <option value="${person.id}">${person.nome} ${person.cognome}</option>
          </c:forEach>
          </select>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Identificativo del ruolo ricoperto nell'attivit&agrave;</div>
        <div class="col-sm-5"><input type="text" class="form-control" id="actIdRuolo" name="actIdRuolo" value=""></div>
      </div>
      <div class="row">
        <div class="col-sm-5">&nbsp;</div>
        <div class="col-sm-5">
          <input type="button" id="show-extrainfo" class="btn extrainfo" value="Altre informazioni" onclick="modify()">
        </div>
      </div>
      <hr />
      <div class="additional-fields" style="display:none;">
        <div class="row">
          <div class="col-sm-5">Nome attivit&agrave;</div>
          <div class="col-sm-5"><input type="text" class="form-control" id="actNome" name="actNome" value="" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Descrizione dell'attivit&agrave;</div>
          <!-- <div class="col-sm-5"><input type="text" class="form-control" id="actDescrizione" name="actDescrizione" value=""></div> -->
          <div class="col-sm-5">
            <textarea class="form-control" name="sAvanzamento" readonly></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actDataInizio" name="actDataInizio" value="" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine dell'attivit&agrave;</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actDataFine" name="actDataFine" value="" readonly></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio attesa</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actDataInizioAttesa" name="actDataInizioAttesa" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine attesa</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actDataFineAttesa" name="actDataFineAttesa" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di inizio effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actDataInizioEffettiva" name="actDataInizioEffettiva" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Data di fine effettiva</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actDataFineEffettiva" name="actDataFineEffettiva" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo previsti</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actGuPrevisti" name="actGuPrevisti" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo effettivi</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actGuEffettivi" name="actGuEffettivi" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Giorni/Uomo rimanenti</div>
          <div class="col-sm-5">
            <input type="text" class="form-control" id="actGuRimanenti" name="actGuRimanenti" value="" readonly>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Note di avanzamento</div>
          <div class="col-sm-5">
            <!-- <input type="text" class="form-control" id="actNote" name="actNote" value=""> -->
            <textarea class="form-control" name="sAvanzamento" readonly></textarea>
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Milestone</div>
          <div class="col-sm-5">
            <input type="checkbox" class="form-check-input" id="actMilestone" name="actMilestone">
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Identificativo WBS</div>
          <div class="col-sm-5">
            <select class="form-control" id="sRischi" name="sRischi" disabled></select>
            <!-- <input type="text" class="form-control" id="actIdWbs" name="actIdWbs" value=""> -->
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Identificativo complessit&agrave; dell'attivit&agrave;</div>
          <div class="col-sm-5"><input type="text" class="form-control" id="actIdComplessita" name="actIdComplessita" value=""></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">Identificativo stato attivit&agrave;</div>
          <div class="col-sm-5"><input type="text" class="form-control" id="actIdStato" name="actIdStato" value=""></div>
        </div>
        <br />
        <input type='button' id='btn' value='Close' class="btn btn-primary" />
        <%@ include file="subPanel.jspf" %>
      </div>
    </form>
      <script type="text/javascript">
        $(document).ready(function () {
            $('#btn').click(function () {
                window.opener.location.reload(true);
                window.self.close();
            });
        });
    </script>
    <script>
    var offsetcharacter = 5;
    $(document).ready(function () {
      $('#newAct_form').validate ({
      rules: {
        'actNome': {
          required: true,
          minlength: offsetcharacter
        },
        'actDataInizio': {
          required: true
        },
        'actDataFine': {
          required: true
        },
        'actGuPrevisti': {
          numeric: true
        }
      }, 
      messages: {
        'actNome': "Please enter the description of project status.",
        'actDataInizio': "Please enter a valid data inizio.",
        'actDataFine': "Please enter a valid data fine."
      },
      submitHandler: function (form) {
        alert('ok');
        return true;
      }
      });
      $('#actDataInizio').datepicker();
      $('#actDataFine').datepicker();
      $('#actDataInizioAttesa').datepicker();
      $('#actDataFineAttesa').datepicker();
      $('#actDataInizioEffettiva').datepicker();
      $('#actDataFineEffettiva').datepicker();
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
    
    </script>