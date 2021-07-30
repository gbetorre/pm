<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
      <c:set var="wbsInstance" value="${requestScope.wbsInstance}" scope="page" />
      <c:catch var="exception">
      <c:set var="distinguishingSubmitId" value="${urlWbs}${p.id}" scope="page" />
      <c:set var="wbsPadre" value="${wbsInstance.wbsPadre}" scope="page" />
      <c:set var="wbsId" value="${wbsInstance.id}" scope="page" />
      <c:set var="wbsNome" value="${wbsInstance.nome}" scope="page" />
      <c:set var="wbsDescr" value="${wbsInstance.descrizione}" scope="page" />
      <c:set var="isWorkpackage" value="${wbsInstance.workPackage}" scope="page" />
      <c:set var="wbsNote" value="${wbsInstance.noteAvanzamento}" scope="page" />
      <c:set var="wbsRisultati" value="${wbsInstance.risultatiRaggiunti}" scope="page" />
      <form id="wbssus_form" action="" method="post" class="panel">
        <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsId}" />
        <div class="panel-heading bgWbs">
          <div class="noHeader">
            <em>Sospensione WBS: &nbsp;<strong><c:out value="${wbsInstance.nome}" escapeXml="false" /></strong></em>
          </div>
        </div>
        <hr class="separatore" />
        <div class="panel-body">
          <%@ include file="subButton.jspf" %>
          <div class="row">
            <div class="col-sm-5">
              <strong>WBS padre:</strong>
            </div>
            <div class="col-sm-5">
              <select class="form-control" id="wbs-idpadre" name="wbs-idpadre" disabled>
              <c:choose>
              <c:when test="${wbsPadre.id gt 0}">
                <option value="${wbsPadre.id}">${wbsPadre.nome}</option>
              </c:when>
              <c:otherwise>
                <option value="">Nessuna wbs padre</option>
              </c:otherwise>
              </c:choose>
              </select>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5"><strong>Nome WBS</strong></div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="wbs-name" name="wbs-name" value="${wbsNome}" placeholder="Inserisci nome WBS" readonly>
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5"><strong>Descrizione della WBS</strong></div>
            <div class="col-sm-5">
              <textarea class="form-control" name="wbs-descr" placeholder="Nessuna descrizione inserita" readonly>${wbsDescr}</textarea>
            </div>
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
          <hr class="riga" />
          <div class="row">
            <div class="col-sm-5 mandatory">
              <a href="javascript:popupWindow('Note','popup1',true,'Inserisci le motivazioni della sospensione (campo obbligatorio), quindi scorri in fondo alla pagina e clicca sul bottone \'Sospendi\'.');" class="helpInfo" id="sus-note">
                Motivazioni della sospensione
              </a> 
            </div>
            <div class="col-sm-5">
              <textarea class="form-control bg-warning" id="sus-descr" name="sus-descr" placeholder="Inserisci i motivi per cui hai deciso di sospendere il progetto"></textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">
              <a href="javascript:popupWindow('Note','popup1',true,'La data della operazione &egrave; la data in cui viene fatta la sospensione e non pu&ograve; essere modificata.');" class="helpInfo" id="data-note">
                Data Operazione
              </a>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-control calendarData" id="sus-data" name="sus-data" value="${requestScope.now}" readonly>
            </div>
          </div>
          <br />
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
                <a href="javascript:popupWindow('Note','popup1',true,'Su questo indicatore non sono state ancora inserite misurazioni. Il responsabile ha tempo fino al <em><fmt:formatDate value='${ind.dataTarget}' pattern='dd/MM/yyyy' /></em> ma, se decide di sospendere la WBS, non potr&agrave; pi&ugrave; inserire misurazioni fino alla riattivazione della stessa.');" class="helpInfo" id="helpAct1">
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
            </div>
            <hr class="riga" />
            </c:forEach>
          </div>
        </c:if>
      </c:if>
          <hr class="separatore" />
          <a href="${urlWbs}${requestScope.progetto.id}" id='btn-close' class="btn btnNav"><i class="far fa-window-close"></i> Chiudi</a>
          <%@ include file="subPanel.jspf" %>
        </div>
      </form>
      </c:catch>
      <c:out value="${exception}" />
      <%@ include file="subPopup.jspf" %>
      <script type="text/javascript">
        var offsetcharacter = 5;
        $(document).ready(function () {
          $('#wbssus_form').validate ({
            rules: {
              'sus-descr': {
                required: true,
                minlength: offsetcharacter
              },
              'sus-data': {
                required: true,
              },
            }, 
            messages: {
              'sus-descr': "Inserire le motivazioni della sospensione",
              'sus-data': "Data della sospensione obbligatoria"
            },
            submitHandler: function (form) {
              return true;
            }
          });
          
          $('#sus-descr').keyup(function (e) {
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
        });
      </script>