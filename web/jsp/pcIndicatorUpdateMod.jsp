<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="indicatore" value="${requestScope.indicatore}" scope="page" />
<c:set var="types" value="${requestScope.tipi}" scope="page" />
<c:catch var="exception">
<c:choose>
  <c:when test="${not empty indicatore}" >
    <c:set var="tP" value="Modifica l'aggiornamento per ${indicatore.nome}" scope="page" />
    <c:set var="open" value="${false}" scope="page" />
    <c:if test="${indicatore.idStato eq 1}">
      <c:set var="open" value="${true}" scope="page" />
    </c:if>
     <div id="ind" class="panel">
      <div class="panel-heading bgMis">
        <div class="noHeader text-white"><em><c:out value="${pageScope.tP}" escapeXml="false" /></em></div>
      </div>
      <div class="panel-body">
      <c:choose>
        <c:when test="${not open}">
        <div class="errorPwd">
          Questo indicatore &egrave; chiuso!<br />
        </div>
        <hr class="separatore" />
        <div class="alert alert-warning">
          <span class="ui-icon ui-icon-alert"></span>
          Non &egrave; possibile aggiornare valori per: <cite>&quot;${indicatore.nome}&quot;</cite>,
          n&eacute; modificarlo, in quanto si trova in stato 'chiuso'.<br />
          Se ritieni che la chiusura dell'indicatore sia avvenuta per errore o hai bisogno di altre informazioni, rivolgiti al <a href="mailto:reporting@ateneo.univr.it">PMO di Ateneo</a>.
        </div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-5">
            <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
          </div>
        </div>
        </c:when>
        <c:otherwise>
        <div class="successPwd">
          <a href="javascript:popupWindow('Note','popup1',true,'Indicatore in stato &quot;Aperto&quot; implica che possono essere aggiunte misurazioni.');" class="helpInfo" id="info" title="Che significa? Clicca per scoprirlo">
            Questo indicatore &egrave; aperto
          </a>
        </div>
        <hr class="riga" />
        <c:choose>
        <c:when test="${empty indicatore.wbs}">
        <div class="alert alert-warning">
          <span class="ui-icon ui-icon-alert"></span>
          Non &egrave; possibile aggiungere misurazioni a: <cite>&quot;${indicatore.nome}&quot;</cite>,
          perch&eacute; questo indicatore non &egrave; ancora stato associato ad alcuna WBS.<br />
          Se ritieni che la mancata associazione sia avvenuta per errore o hai bisogno di altre informazioni, rivolgiti al <a href="mailto:reporting@ateneo.univr.it">PMO di Ateneo</a>.
        </div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-5">
            <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
            <a href="<c:out value="${ind}${requestScope.progetto.id}&v=o" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-ruler"></i> Indicatori</a>
          </div>
        </div>
        </c:when>
        <c:otherwise>
      <form id="modext_form" action="" method="post">
        <input type="hidden" id="ind-id" name="ind-id" value="${indicatore.id}" />
        <input type="hidden" id="prj-id" name="prj-id" value="${p.id}" />
          <div class="bg-light text-dark">
              <div class="row">
                <div class="col-sm-5 initialism"><cite>Nome Azione</cite></div>
                <div class="col-sm-5">
                  <c:out value="${indicatore.wbs.nome}" />
                </div>
              </div>
              <hr class="riga" />
              <div class="row">
                <div class="col-sm-5 initialism"><cite>Nome Indicatore</cite></div>
                <div class="col-sm-5">
                  <c:out value="${indicatore.nome}" />
                </div>
              </div>
              <hr class="riga" />
              <div class="row">
                <div class="col-sm-5 initialism">
                  <cite>Tipo indicatore:</cite>
                </div>
                <div class="col-sm-5">
                  <c:out value="${indicatore.tipo.nome}" />
                </div>
              </div>
              <hr class="riga" />
              <div class="row">
                <div class="col-sm-5 initialism"><cite>Descrizione</cite></div>
                <div class="col-sm-5">
                  <c:out value="${fn:substring(indicatore.descrizione, 0, 20)}" />...
                  <div class="charNum"></div>
                </div>
              </div>
              <hr class="riga" />
              <div class="row">
                <div class="col-sm-5 initialism"><cite>Baseline</cite></div>
                <div class="col-sm-5">
                  <c:out value="${indicatore.baseline}" /> <em>(<fmt:formatDate value="${indicatore.dataBaseline}" pattern="dd/MM/yyyy" />)</em>
                </div>
              </div>
              <hr class="riga" />
              <div class="row">
                <div class="col-sm-5 initialism"><cite>Target Originale</cite></div>
                <div class="col-sm-5">
                  <c:out value="${indicatore.target}" /> <em>(<fmt:formatDate value="${indicatore.dataTarget}" pattern="dd/MM/yyyy" />)</em>
                </div>
              </div>
              <hr class="riga" />
              <div class="row">
                <div class="col-sm-5 initialism">
                  <cite>N. Misurazioni trovate:</cite>
                </div>
                <div class="col-sm-5">
                  <c:out value="${indicatore.totMisurazioni}" />
                </div>
              </div>
            </div>

          <hr class="riga" />
          <div class="row">
            <div class="col-sm-5 mandatory">Nuovo Target</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="modext-target" name="modext-target" value="${indicatore.targetRivisto}">
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Data Target</div>
            <div class="col-sm-5">
              <input type="text" class="form-control calendarData" id="ext-datatarget" name="ext-datatarget" value="<fmt:formatDate value='${indicatore.dataTarget}' pattern='dd/MM/yyyy' />" readonly>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Note','popup1',true,'Anno, solare o accademico, a cui i valori target fanno riferimento.<br />Dato facoltativo.');" class="helpInfo" id="annotarget">
                Anno Target
              </a>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="ext-annotarget" name="ext-annotarget" value="${indicatore.annoTarget}" readonly>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Motivo della variazione</div>
            <div class="col-sm-5">
              <textarea class="form-control" name="modext-note" id="modext-note">${indicatore.noteRevisione}</textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Autore e data SQL della variazione</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" name="ext-auth" id="ext-auth" value="${indicatore.autoreUltimaRevisione}, ${indicatore.dataRevisione}" readonly>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Note','popup1',true,'Se vuoi lasciare inalterata la traccia della modifica originale NON inserire il segno di spunta.');" class="helpInfo" id="data-note">
                Aggiorna anche i campi automatici
              </a>
            </div>
            <div class="col-sm-5 marginLeft">
              <input type="checkbox" class="form-check-input" id="modext-auto" name="modext-auto">
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
              <a href="<c:out value="${ind}${requestScope.progetto.id}&v=o" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-ruler"></i> Indicatori</a>
            </div>
            <div class="col-sm-5">
            <c:choose>
              <c:when test="${not open}">
              <div class="alert alert-warning">
                <span class="ui-icon ui-icon-alert"></span>
                Non &egrave; possibile aggiungere misurazioni a questo indicatore,
                n&eacute; modificarlo, in quanto si trova in stato 'chiuso'.<br />
              </div>
              </c:when>
              <c:otherwise>
                <button type="submit" id="btnSave" class="btn btn-success" value="Salva">
                  <i class="fas fa-retweet"></i>
                  Aggiorna
                </button>
              </c:otherwise>
            </c:choose>
            </div>
          </div>
          <hr class="separatore" />
      </form>
      </c:otherwise>
      </c:choose>
      </div>
    </div>
    <%@ include file="subPopup.jspf" %>
      <c:out value="${exception}" />

      <script type="text/javascript">
        var offsetcharacter = 5;
        $(document).ready(function () {
          $('#modext_form').validate ({
            rules: {
              'modext-target': {
                required: true
              },
              'modext-note': {
                required: true,
                minlength: offsetcharacter
              },
            }, 
            messages: {
              'modext-target': "Inserire il valore del nuovo target",
              'modext-note': "Inserire il motivo della variazione"
            },
            submitHandler: function (form) {
              return true;
            }
          });
        
          $('#modext-note').keyup(function (e) {
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
    
        </c:otherwise>
      </c:choose>
    
  </c:when>
  <c:otherwise>
    <div class="errorPwd">
      Attenzione: l'indicatore da aggiornare non &egrave; stato trovato.<br /> 
      Digitare nuovamente l'indirizzo o ripercorrere il percorso.<br />
      <c:out value="${exception}" />
    </div>
    <br />
    <div class="row">
      <div class="col-sm-5">
        <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
      </div>
    </div>
  </c:otherwise>
</c:choose>
</c:catch>