<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/indicatoregestione_all/${p.id}" scope="page" />
<c:set var="misurazione" value="${requestScope.misurazione}" scope="page" />
<c:set var="indicatore" value="${misurazione.indicatore}" scope="page" />
<c:if test="${misurazione.ultimo}">
  <c:set var="checked" value="checked" scope="page" />
</c:if>
<c:catch var="exception">
<c:choose>
  <c:when test="${not empty indicatore}" >
    <c:set var="tP" value="Visualizza una misurazione esistente per ${indicatore.nome}" scope="page" />
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
          <a href="javascript:popupWindow('Note','popup1',true,'Indicatore in stato &quot;Chiuso&quot; implica che non possono essere aggiunte ulteriori misurazioni.');" class="helpInfo" id="info" title="Che significa? Clicca per scoprirlo">
            Questo indicatore &egrave; chiuso!
          </a>
        </div>
        <hr class="separatore" />
        </c:when>
        <c:otherwise>
        <div class="successPwd">
          <a href="javascript:popupWindow('Note','popup1',true,'Indicatore in stato &quot;Aperto&quot; implica che possono essere aggiunte misurazioni.');" class="helpInfo" id="info" title="Che significa? Clicca per scoprirlo">
            Questo indicatore &egrave; aperto
          </a>
        </div>
        <hr class="separatore" />
        </c:otherwise>
      </c:choose>
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
            <a href="<c:out value="${ind}${requestScope.progetto.id}&v=o" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-ruler"></i> Indicatori</a>
          </div>
        </div>
        </c:when>
        <c:otherwise>
      <form id="mon_form" action="" method="post">
        <input type="hidden" id="ind-id" name="ind-id" value="${indicatore.id}" />
        <input type="hidden" id="prj-id" name="prj-id" value="${p.id}" />
        <h4 class="heading">Dati dell'indicatore a cui la misurazione &egrave; relativa:</h4>
        <div class="bgcolor1 text-dark" style="padding-left:1%;padding-top:1%;padding-bottom:1%;margin:0.1%">
            <hr class="separatore" />
            <div class="row">
              <div class="col-sm-5 initialism"><cite>Nome WBS</cite></div>
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
                <c:out value="${fn:substring(indicatore.descrizione, 0, 80)}" />...
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
                <a href="${monInd}${p.id}&v=a" class="btn btn-spacer text-secondary"><c:out value="${indicatore.totMisurazioni}" /></a>
              </div>
            </div>
          </div>
          <hr class="riga" />
          <h4 class="heading">Dati della misurazione inserita:</h4>
          <hr class="separatore" />
          <div class="row">
            <div class="col-sm-5">Valore Misurazione</div>
            <div class="col-sm-5">
              <input type="text" class="form-custom" id="mon-nome" name="mon-nome" value="${misurazione.descrizione}" readonly disabled>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Risultati conseguiti</div>
            <div class="col-sm-5">
              <textarea class="form-control" name="mon-descr" id="mon-descr">${misurazione.informativa}</textarea>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Note','popup1',true,'La data della misurazione &egrave; la data in cui &egrave; stata fatta la misurazione.');" class="helpInfo" id="data-note">
                Data Misurazione
              </a>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-custom calendarData" id="mon-data" name="mon-data" value="<fmt:formatDate value='${misurazione.dataMisurazione}' pattern='dd/MM/yyyy' />" readonly disabled>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">Autore della misurazione</div>
            <div class="col-sm-5">
              <input type="text" class="form-control" name="mon-auth" id="mon-auth" value="${misurazione.autoreUltimaModifica}" readonly>
              <div class="charNum"></div>
            </div>
          </div>
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a href="javascript:popupWindow('Attenzione','popup1',true,'Il flag &quot;Ultima Misurazione&quot; indica che la misurazione &egrave; stata contrassegnata come tale e non si aveva intenzione di effettuare ulteriori misurazioni sullo specifico indicatore.');" class="helpInfo" id="milestone">
                Ultima Misurazione
              </a>
            </div>
            <div class="col-sm-5 marginLeft">
              <input type="checkbox" class="form-check-input" id="mon-milestone" name="mon-milestone" <c:out value='${checked}' />>
            </div>
          </div>
          <hr class="riga" />
          <c:if test="${not empty misurazione.allegati}">
          <c:catch var="exception">
          <hr class="separatore" />
          <div class="row">
	          <div class="col-sm-5">&nbsp;</div>
	          <div class="col-sm-5">
	            <div class="lightTable">
	              <div class="row">
	                <div class="col bordo"><strong>Allegati della misurazione</strong></div>
	              </div>
	              <hr class="separatore" />
	              <ul class="list-unstyled">
	              <c:forEach var="all" items="${misurazione.allegati}" varStatus="loop">
	                <c:set var="ext" value="${fn:substring(all.estensione, 1, fn:length(all.estensione))}" scope="page" />
	                <li>
	                  <span>
	                    <img src="${initParam.urlDirectoryImmagini}ico_${ext}.png" border="0" alt="${all.estensione}">
	                  </span>
	                  <a href="${localDocumentRoot}/${all.file}${all.estensione}" class="transition">
	                    <c:out value="${all.titolo}" escapeXml="false" />
	                  </a>
	                  <span class="file-data">
	                    (<c:out value="${ext}" />,&nbsp;<fmt:formatNumber type="number" value="${all.dimensione/1024}" maxFractionDigits ="2" />&nbsp;KB,&nbsp;<fmt:formatDate value="${all.data}" pattern="dd/MM/yyyy" />)
	                  </span>
	                </li>
	              </c:forEach>
	              </ul>
	            </div>
	          </div>
          </div>
          </c:catch>
          <c:out value="${exception}" />
          </c:if>
          <br />
          <section id="Allegati">
          <div class="row">
            <div class="col-sm-5">
              &nbsp;
            </div>
            <div class="col-sm-5 ">
              <a href="#all-form" class="ico" id="add-all" rel="modal:open">
                <img src="${initParam.urlDirectoryImmagini}/ico-add.png" class="btn-del addElement" alt="Link ad aggiunta documento allegato" title="Aggiungi un Allegato" />
              </a>
              <span id="add-label">
                <a href="javascript:popupWindow('Note','popup1',true,'Gli allegati sono utili per caricare informazioni aggiuntive riguardo alla misurazione e/o all\'indicatore.<br>Clicca sul <strong>+</strong> per aggiungere un allegato.');" class="helpInfo" id="all-note">
                Aggiungi Allegato
                </a>
              </span>
            </div>
          </div> 
          </section>         
          <br />
          <div class="row">
            <div class="col-sm-5">
              <a id="btnBack" class="btn btnNav" onclick="goBack()"><i class="fas fa-chevron-left"></i> Indietro</a>
              <a href="<c:out value="${ind}${requestScope.progetto.id}&v=o" escapeXml="false" />" id='btn-close' class="btn btnNav"><i class="fas fa-ruler"></i> Indicatori</a>
            </div>
            <div class="col-sm-5">
              <%@ include file="subPanel.jspf" %>
            </div>
          </div>
          <hr class="separatore" />
      </form>
      </c:otherwise>
      </c:choose>
      </div>
    </div>
    <form id="all-form" method="post" action="file?q=ind&p=mon&id=${requestScope.progetto.id}&idi=${indicatore.id}" enctype="multipart/form-data" class="modal">
      <input type="hidden" id="prj-id" name="prj-id" value="${requestScope.progetto.id}" />
      <input type="hidden" id="mis-id" name="mis-id" value="${misurazione.id}" />
      <h3 class="heading">Aggiungi un allegato</h3>
      <br />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Titolo Documento
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="text" class="form-control" id="doc-name" name="doc-name" value="" placeholder="Inserisci un titolo documento">
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Seleziona un file da caricare
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="file" name="file" id="file" size="60" placeholder="Inserisci un file da caricare"><br /><br /> 
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <button type="submit" class="btn btn-warning" value="Upload"><i class="fas fa-file-upload"></i> Upload</button>
      </div>
    </form>
    <%@ include file="subPopup.jspf" %>
      <c:out value="${exception}" />

      <script type="text/javascript">
        var offsetcharacter = 300;
        $(document).ready(function () {
          $('#mon_form').validate ({
            rules: {
              'mon-value': {
                <c:if test="${indicatore.tipo.id eq 3}">
                number: true,
                range: [0, 100],
                required: true
                </c:if>
              },
              'mon-nome': {
                <c:if test="${indicatore.tipo.id eq 2}">
                number: true,
                </c:if>
                required: true
              },
              'mon-descr': {
                required: true,
                minlength: offsetcharacter
              },
            }, 
            messages: {
              'mon-nome': "Inserire un valore corretto per la misurazione (i valori decimali vanno separati con il punto. Esempio: 4.82)",
              'mon-descr': "Inserire almeno " + offsetcharacter + " caratteri."
            },
            submitHandler: function (form) {
              return true;
            }
          });
          
          $('#all-form').validate ({
            rules: {
              'doc-name': {
                required: true
              },
              'file': {
                  required: true
              },
            }, 
            messages: {
              'doc-name': "Inserire il titolo del documento",
              'file': "Scegliere un file da caricare!"
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