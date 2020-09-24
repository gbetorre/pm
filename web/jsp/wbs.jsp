<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
      <c:set var="wbsInstance" value="${requestScope.wbsInstance}" scope="page" />
      <c:set var="idWbsP" value="${requestScope.idWbsP}" scope="page" />
      <jsp:useBean id="wbsPadre" class="it.alma.bean.WbsBean" scope="page" />
      <c:set target="${pageScope.wbsPadre}" property="id" value="-3" />
      <c:set var="wbsPadre" value="${wbsPadre}" scope="page" />
      <c:catch var="exception">
      <c:set var="wbsNome" value="" scope="page" />
      <c:set var="wbsDescr" value="" scope="page" />
      <c:set var="isWorkpackage" value="" scope="page" />
      <c:set var="distinguishingSubmitId" value="${urlWbs}${p.id}" scope="page" />
      <c:choose>
        <c:when test="${not empty wbsInstance}" >
          <c:set var="tP" value="${wbsInstance.nome}" scope="page" />
          <c:set var="wbsPadre" value="${wbsInstance.wbsPadre}" scope="page" />
          <c:set var="wbsId" value="${wbsInstance.id}" scope="page" />
          <c:set var="wbsNome" value="${wbsInstance.nome}" scope="page" />
          <c:set var="wbsDescr" value="${wbsInstance.descrizione}" scope="page" />
          <c:set var="isWorkpackage" value="${wbsInstance.workPackage}" scope="page" />
          <c:set var="wbsNote" value="${wbsInstance.noteAvanzamento}" scope="page" />
          <c:set var="wbsRisultati" value="${wbsInstance.risultatiRaggiunti}" scope="page" />
        </c:when>
        <c:otherwise>
          <c:set var="tP" value="Inserisci una nuova WBS:" scope="page" />
        </c:otherwise>
      </c:choose>
      <form id="wbs_form" action="" method="post" class="panel">
        <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsId}" />
        <div class="panel-heading bgWbs">
          <div class="noHeader"><em><c:out value="${pageScope.tP}" escapeXml="false" /></em></div>
        </div>
        <hr class="separatore" />
        <div class="panel-body">
          <%@ include file="subButton.jspf" %>
          <div class="row">
            <div class="col-sm-5 mandatory">
              WBS padre (se &eacute; presente):
            </div>
            <div class="col-sm-5">
              <select class="form-control" id="wbs-idpadre" name="wbs-idpadre" disabled>
              <c:if test="${wbsPadre.id ne -3}">
                <option value="${wbsPadre.id}">${wbsPadre.nome}</option>
              </c:if>
              <option value="">Nessuna wbs padre</option>
              <c:forEach var="singleWbs" items="${requestScope.wbs}" varStatus="status">
                <c:set var="selected" value="" scope="page" />
                <c:if test="${idWbsP eq singleWbs.id}">
                  <c:set var="selected" value="selected" scope="page" />
                </c:if>
                <option value="${singleWbs.id}" ${selected}>${singleWbs.nome}</option>
              </c:forEach>
              </select>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Nome WBS</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="wbs-name" name="wbs-name" value="${wbsNome}" placeholder="Inserisci nome WBS" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Descrizione della WBS</div>
            <div class="col-sm-5">
              <textarea class="form-control" name="wbs-descr" placeholder="Inserisci una descrizione per la WBS" readonly>${wbsDescr}</textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Note di avanzamento della WBS</div>
            <div class="col-sm-5">
              <textarea class="form-control noActivateOnAdd" id="wbs-note" name="wbs-note" placeholder="Inserisci note di avanzamento della WBS" readonly>${wbsNote}</textarea>
              <div class="charNum"></div>
            </div>
            <a class="ico" id="wbs-addNote">
              <img src="${initParam.urlDirectoryImmagini}/ico-add-inactive.png" class="btn-del addElement" alt="Link ad aggiunta note" title="Aggiungi note avanzamento"/>
            </a>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Risultati raggiunti da questa WBS</div>
            <div class="col-sm-5">
              <textarea class="form-control noActivateOnAdd" id="wbs-result" name="wbs-result"  placeholder="Inserisci risultati raggiunti dalla WBS" readonly>${wbsRisultati}</textarea>
              <div class="charNum"></div>
            </div>
            <a class="ico" id="wbs-addResult">
              <img src="${initParam.urlDirectoryImmagini}/ico-add-inactive.png" class="btn-del addElement" alt="Link ad aggiunta risultati" title="Aggiungi risultati"/>
            </a>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Workpackage</div>
            <div class="col-sm-5">
              <c:choose>
                <c:when test="${isWorkpackage}">
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="wbs-workpackage" name="wbs-workpackage" checked disabled>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="wbs-workpackage" name="wbs-workpackage" disabled>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
      <c:if test="${not empty wbsInstance}">
        <c:choose>
          <c:when test="${not empty wbsInstance.attivita}">
          <hr class="separatore" />
          <div class="lightTable">
            <div class="row">
              <div class="col"><strong>Attivit&agrave; della WBS</strong></div>
            </div>
            <hr class="separatore" />
            <div class="row">
              <div class="col-sm-1"></div>
              <div class="col-sm-3 lightTableHead">Nome attivit&agrave;</div>
              <div class="col-sm-2 lightTableHead">Data inizio prevista</div>
              <div class="col-sm-2 lightTableHead">Data fine prevista</div>
              <div class="col-sm-2 lightTableHead">Milestone</div>
            </div>
            <hr class="separatore" />
            <c:forEach var="act" items="${wbsInstance.attivita}" varStatus="loop">
            <div class="row">
              <div class="col-sm-1"></div>
              <div class="col-sm-3">
                <a href="${modAct}${p.id}&ida=${act.id}"><c:out value="${act.nome}"/></a>
            	</div>
              <div class="col-sm-2">
                <fmt:formatDate value='${act.dataInizio}' pattern='dd/MM/yyyy' />
              </div>
              <div class="col-sm-2">
                <fmt:formatDate value='${act.dataFine}' pattern='dd/MM/yyyy' />
              </div>
              <div class="col-sm-2">
              <c:choose>
              <c:when test="${act.milestone}">
                <div class="form-check text-center">
                  <input type="checkbox" class="form-check-input" checked disabled>
                </div>
              </c:when>
              <c:otherwise>
                <div class="form-check text-center">
                  <input type="checkbox" class="form-check-input" disabled>
                </div>
              </c:otherwise>
              </c:choose>
              </div>
            </div>
            </c:forEach>
          </div>
          </c:when>
          <c:otherwise>
          <hr class="separatore" />
          <div class="alert alert-warning">
            <p>Non &egrave; stata trovata alcuna attivit&agrave; associata <em>direttamente</em> alla WBS &quot;${wbsNome}&quot; 
            <c:if test="${not empty wbsPadre.id}">
              <br />
              (figlia di <a href="${modWbs}${p.id}&idw=${wbsPadre.id}">${wbsPadre.nome}</a>)
            </c:if>
            </p>
          </div>
          </c:otherwise>
        </c:choose>
        <c:if test="${not empty requestScope.indicatori}">
          <hr class="separatore" />
          <div class="lightTable">
            <div class="row">
              <div class="col"><strong>Indicatori della WBS</strong></div>
            </div>
            <hr class="separatore" />
            <div class="row">
              <div class="col-sm-1"></div>
              <div class="col-sm-3 lightTableHead">Nome indicatore</div>
              <div class="col-sm-1 lightTableHead">Baseline</div>
              <div class="col-sm-1 lightTableHead">Anno</div>
              <div class="col-sm-1 lightTableHead">Target</div>
              <div class="col-sm-1 lightTableHead">Anno</div>
              <div class="col-sm-2 lightTableHead">Misurazioni</div>
            </div>
            <hr class="separatore" />
            <c:forEach var="ind" items="${requestScope.indicatori}" varStatus="loop">            
            <div class="row">
              <div class="col-sm-1"></div>
              <div class="col-sm-3">
                <a href="${modInd}${p.id}&idi=${ind.id}"><c:out value="${ind.nome}"/></a>
              </div>
              <div class="col-sm-1">
                &nbsp;<c:out value="${ind.baseline}" />
              </div>
              <div class="col-sm-1">
                <fmt:formatDate value='${ind.dataBaseline}' pattern='yyyy' />
              </div>
              <div class="col-sm-1">
                &nbsp;<c:out value="${ind.target}" />
              </div>
              <div class="col-sm-1">
                <fmt:formatDate value='${ind.dataTarget}' pattern='yyyy' />
              </div>
              <div class="col-sm-1 text-center">
              <c:choose>
                <c:when test="${ind.totMisurazioni eq 0}">
                <a href="javascript:popupWindow('Note','popup1',true,'Su questo indicatore non sono state ancora inserite misurazioni. Il responsabile ha tempo fino al <em><fmt:formatDate value='${ind.dataTarget}' pattern='dd/MM/yyyy' /></em> per inserirle!');" class="helpInfo" id="helpAct1">
                  <span class="badge badge-danger">
                    <c:out value="${ind.totMisurazioni}" />
                  </span>
                </a>
                </c:when>
                <c:otherwise>
                <c:set var="misurazioni" value="" scope="page" />
                <c:forEach var="mis" items="${ind.misurazioni}" varStatus="loop">
                  <c:set var="misurazioni" value="${misurazioni} ${loop.index+1}- ${mis.descrizione}<br>" scope="page" />
                </c:forEach>
                <a href="javascript:popupWindow('Misurazioni','popup1',true,'${misurazioni}');" class="helpInfo" id="helpAct1">
                  <span class="badge badge-success">
                    <c:out value="${ind.totMisurazioni}" />
                  </span>
                </a>
                </c:otherwise>
              </c:choose>
              </div>
              <div class="col-sm-1 text-center">
                <a href="<c:out value="${monInd}${p.id}&idi=${ind.id}" escapeXml="false" />" id='btn-mon'>
                  <button type="button" class="btn btn-sm btn-outline-primary">Misura</button>
                </a>
              </div>
            </div>
            <hr class="riga" />
            </c:forEach>
          </div>
          </c:if>
      </c:if>
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
              <input type="text" class="form-control calendarData" id="wbs-datanota" name="wbs-datafinevera" pattern='dd/MM/yyyy' placeholder="Inserisci una data nota">
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
              <input type="text" class="form-control" id="wbs-titlenote" name="wbs-titlenote" value="" placeholder="Inserisci un titolo nota">
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
              <textarea class="form-control" id="wbs-descnote" name="wbs-descnote" class="form-control" aria-label="With textarea" maxlength="8104" placeholder="Inserisci una descrizione nota"></textarea>
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
              <input type="text" class="form-control calendarData" id="wbs-dataresult" name="wbs-dataresult" pattern='dd/MM/yyyy' placeholder="Inserisci una data risultato">
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
              <textarea class="form-control" id="wbs-descresult" name="wbs-descresult" class="form-control" aria-label="With textarea" maxlength="8104" placeholder="Inserisci una descrizione del risultato"></textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <hr class="separatore" />
          <a href="<c:out value="${redirect}" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="far fa-window-close"></i> Chiudi</a>
          <a href="#" class="btn btn-success" id="btn-add" onclick="addResult()" rel="modal:close"><i class="fas fa-plus"></i> Aggiungi</a>
        </div>
      </form>
      </c:catch>
      <c:out value="${exception}" />
      <%@ include file="subPopup.jspf" %>
      <script type="text/javascript">
        var offsetcharacter = 5;
        // Variabili per formattazione stringhe
        var limNoteTitleOpen = "\n - ";
        var limNoteTitleClose = " -\n";
        var newLine = "\n";
        var separatore = "\n================================\n";
        $(document).ready(function () {
          $('#newWbs_form').validate ({
            rules: {
              'wbs-name': {
                required: true,
                minlength: offsetcharacter
              },
              'wbs-descr': {
                required: true,
                minlength: offsetcharacter
              },
            }, 
            messages: {
              'wbs-name': "Inserire il nome della WBS",
              'wbs-descr': "Inserire una descrizione per la WBS"
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
          
          $('#wbs-descnote').keyup(function() {
            var len = $(this).val().length + $('#wbs-note').val().length + $('#wbs-datanota').val().length + $('#wbs-titlenote').val().length + limNoteTitleOpen.length + limNoteTitleClose.length + separatore.length;
            var dblength = parseInt($(this).attr('maxlength'));
            if(len >= dblength) {
              this.value = this.value.substring(0, dblength);
              $(this).next('div').text(' hai raggiunto il limite massimo di caratteri inseribili');
            } else {
              var chars = dblength - len;
              $(this).next('div').text(chars + ' caratteri rimanenti');
            }
          });
          
          $('#wbs-descresult').keyup(function() {
            var len = $(this).val().length + $('#wbs-result').val().length + $('#wbs-dataresult').val().length + newLine.length + separatore.length;
            var dblength = parseInt($(this).attr('maxlength'));
            if(len >= dblength) {
              this.value = this.value.substring(0, dblength);
              $(this).next('div').text(' hai raggiunto il limite massimo di caratteri inseribili');
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
        
        function addNote() {
          var currentText = $('#wbs-note').val();
          if ($('#wbs-datanota').val().length < 10 || $('#wbs-titlenote').val().length < 1 || $('#wbs-descnote').val() < 1) {
            alert("Tutti i campi sono obbligatori!");
            $('#wbs-progress').val(currentText);
          } else {
            var newText = $('#wbs-datanota').val() + limNoteTitleOpen + $('#wbs-titlenote').val() + limNoteTitleClose + $('#wbs-descnote').val() + separatore + currentText;
            $('#wbs-note').val(newText);
            $('#wbs-datanota').val("");
            $('#wbs-titlenote').val("");
            $('#wbs-descnote').val("");
          }
        };
        
        function addResult() {
          var currentText = $('#wbs-result').val();
          if ($('#wbs-dataresult').val().length < 1 || $('#wbs-descresult').val().length < 1) {
            alert("Tutti i campi sono obbligatori!");
            $('#wbs-result').val(currentText);
          } else {
            var newText = $('#wbs-dataresult').val() + newLine + $('#wbs-descresult').val() + separatore + currentText;
            $('#wbs-result').val(newText);
            $('#wbs-dataresult').val("");
            $('#wbs-descresult').val("");
            
          }
        };
      </script>