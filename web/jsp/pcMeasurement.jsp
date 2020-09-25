<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="indicatore" value="${requestScope.indicatore}" scope="page" />
<c:set var="types" value="${requestScope.tipi}" scope="page" />
<c:catch var="exception">
<c:choose>
  <c:when test="${not empty indicatore}" >
    <c:set var="tP" value="Effettua una nuova misurazione per ${indicatore.nome}" scope="page" />
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
          Non &egrave; possibile aggiungere misurazioni a: <cite>&quot;${indicatore.nome}&quot;</cite>,
          n&eacute; modificarlo, in quanto si trova in stato 'chiuso'.<br />
          Se ritieni che la chiusura dell'indicatore sia avvenuta per errore o hai bisogno di altre informazioni, rivolgiti al <a href="mailto:stefano.fedeli@univr.it">PMO di Ateneo</a>.
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
          Se ritieni che la mancata associazione sia avvenuta per errore o hai bisogno di altre informazioni, rivolgiti al <a href="mailto:stefano.fedeli@univr.it">PMO di Ateneo</a>.
        </div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-5">
            <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
            <a href="<c:out value="${ind}${requestScope.progetto.id}" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-ruler"></i> Indicatori</a>
          </div>
        </div>
        </c:when>
        <c:otherwise>
      <form id="mon_form" action="" method="post">
        <input type="hidden" id="ind-id" name="ind-id" value="${indicatore.id}" />
        <input type="hidden" id="prj-id" name="prj-id" value="${p.id}" />
        <div class="row bg-warning" style="padding-left:1%;padding-top:1%;padding-bottom:1%;margin:0.1%">
        <h4>Dati dell'indicatore misurato:</h4>
        <small>
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
            <div class="col-sm-5 initialism"><cite>Target</cite></div>
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
          </small>
          </div>
          <hr class="riga" />
          <div class="row">
            <div class="col-sm-5 mandatory">Valore Misurazione</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" id="mon-nome" name="mon-nome" value="" placeholder="Inserisci valore misurazione (obbligatorio)">
              <div class="charNum"></div> 
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">Risultati conseguiti</div>
            <div class="col-sm-5">
              <textarea class="form-control" name="mon-descr" id="mon-descr" placeholder="Inserisci risultati conseguiti (obbligatorio)"></textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5 mandatory">
              <a href="javascript:popupWindow('Note','popup1',true,'La data della misurazione &egrave; la data in cui viene fatta la misurazione e non pu&ograve; essere modificata.');" class="helpInfo" id="data-note">
                Data Misurazione
              </a>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-control calendarData" id="mon-data" name="mon-data" value="<fmt:formatDate value='${requestScope.now}' pattern='dd/MM/yyyy' />" placeholder="Inserisci data valore baseline" readonly>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Attenzione','popup1',true,'Il flag &quot;Ultima Misurazione&quot; indica che l\'indicatore misurato sar&agrave; chiuso e non sar&agrave; possibile effettuare ulteriori misurazioni su di esso.');" class="helpInfo" id="milestone">
                Ultima Misurazione
              </a>
            </div>
            <div class="col-sm-5 marginLeft">
              <input type="checkbox" class="form-check-input" id="mon-milestone" name="mon-milestone" <c:out value='${checked}' />>
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
              <c:when test="${not open}">
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
      </c:otherwise>
      </c:choose>
      </div>
    </div>
    <%@ include file="subPopup.jspf" %>
      <c:out value="${exception}" />

      <script type="text/javascript">
        var offsetcharacter = 5;
        $(document).ready(function () {
          $('#mon_form').validate ({
            rules: {
              'mon-nome': {
                required: true
              },
              'mon-descr': {
                required: true,
                minlength: offsetcharacter
              },
            }, 
            messages: {
              'mon-nome': "Inserire il valore della misurazione",
              'mon-descr': "Inserire i risultati conseguiti"
            },
            submitHandler: function (form) {
              return true;
            }
          });
        
          $('#mon-descr').keyup(function (e) {
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
      Attenzione: l'indicatore da misurare non &egrave; stato trovato.<br /> 
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