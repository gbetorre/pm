<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/avanzamento_all/${p.id}" scope="page" />
<c:choose>
  <c:when test="${p.id eq param['id']}">
    <div class="row">
      <div class="col-9">
        <form class="form-custom" id="status_form" action="#" method="post">
          <c:choose>
            <c:when test="${not empty ps}">
            <h2 align="center">
              Sotto progetto:&nbsp;<strong><c:out value="${p.titolo}" /></strong>
            </h2>
            <hr class="separatore" />
              <div class="form-row">
                <div class="col">
                  <input type="hidden" id="sts-id" name="sts-id" value="${ps.id}" />
                    <div class="form-row">
                      <div class="col-1 text-right form-control form-control-custom mandatory">Dal</div>
                      <div class="col-3">
                        <input type="text" class="form-control calendarData" id="sts-datainizio" name="sts-datainizio" value="<fmt:formatDate value="${ps.dataInizio}" pattern="dd/MM/yyyy" />" readonly>
                      </div>
                      <div class="col-1 text-right form-control form-control-custom mandatory">Al</div>
                      <div class="col-3">
                        <input type="text" class="form-control calendarData" id="sts-datafine" name="sts-datafine" value="<fmt:formatDate value="${ps.dataFine}" pattern="dd/MM/yyyy" />" readonly>
                      </div>
                      <div class="col-1"></div>
                      <div class="col-3 text-center form-control">
                        <a class="ico" id="add-all">
                          <img src="${initParam.urlDirectoryImmagini}/ico-add-inactive.png" class="btn-del addElement" alt="Link ad aggiunta documento allegato" title="Aggiungi un Allegato" />
                        </a>
                        <span id="add-label">Aggiungi Allegato</span>
                      </div>
                    </div>
                    <hr class="separatore" />
                    <div class="form-row">
                      <div class="col-3 text-right form-control form-control-custom mandatory">Descrizione avanzamento</div>
                      <div class="col">
                        <textarea class="form-control" id="sts-avanzamento" name="sts-avanzamento" maxlength="16384" readonly>${ps.descrizioneAvanzamento}</textarea>
                        <div class="charNum"></div>
                      </div>
                    </div>
                    <hr class="separatore" />
                    <div class="form-row">
                      <div class="col">Stato costi</div>
                      <div class="col">Stato tempi</div>
                      <div class="col">Stato rischi</div>
                    </div>
                    <div class="form-row">
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoCosti'].id}" id="sts-costi" name="sts-costi" disabled>
                          <option value="${ps.stati['StatoCosti'].id}" selected class="bgcolor${ps.stati['StatoCosti'].id}">${ps.stati['StatoCosti'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoTempi'].id}" id="sts-tempi" name="sts-tempi" disabled>
                          <option value="${ps.stati['StatoTempi'].id}" selected class="bgcolor${ps.stati['StatoTempi'].id}">${ps.stati['StatoTempi'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoRischi'].id}" id="sts-rischi" name="sts-rischi" disabled>
                          <option value="${ps.stati['StatoRischi'].id}" selected class="bgcolor${ps.stati['StatoRischi'].id}">${ps.stati['StatoRischi'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                    <div class="form-row">
                      <div class="col">Stato risorse</div>
                      <div class="col">Stato scope</div>
                      <div class="col">Stato comunicazione</div>
                    </div>
                    <div class="form-row">
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoRisorse'].id}" id="sts-risorse" name="sts-risorse" disabled >
                          <option value="${ps.stati['StatoRisorse'].id}" selected class="bgcolor${ps.stati['StatoRisorse'].id}">${ps.stati['StatoRisorse'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoScope'].id}" id="sts-scope" name="sts-scope" disabled>
                          <option value="${ps.stati['StatoScope'].id}" selected class="bgcolor${ps.stati['StatoScope'].id}">${ps.stati['StatoScope'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="$bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoComunicazione'].id}" id="sts-comunicazione" name="sts-comunicazione" disabled>
                          <option value="${ps.stati['StatoComunicazione'].id}" selected class="bgcolor${ps.stati['StatoComunicazione'].id}">${ps.stati['StatoComunicazione'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>
                    <div class="form-row">
                      <div class="col">Stato qualit&agrave;</div>
                      <div class="col">Stato approvvigionamenti</div>
                      <div class="col">Stato stakeholder</div>
                    </div>
                    <div class="form-row">
                      <div class="col">
                        <select class="form-control bgcolor${ps.stati['StatoQualita'].id}" id="sts-qualita" name="sts-qualita" disabled>
                          <option value="${ps.stati['StatoQualita'].id}" selected class="bgcolor${ps.stati['StatoQualita'].id}">${ps.stati['StatoQualita'].nome}</option>
                          <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                            <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                          </c:forEach>
                        </select>
                      </div>
                    <div class="col">
                      <select class="form-control bgcolor${ps.stati['StatoApprovvigionamenti'].id}" id="sts-approvvigionamenti" name="sts-approvvigionamenti" disabled>
                        <option value="${ps.stati['StatoApprovvigionamenti'].id}" selected class="bgcolor${ps.stati['StatoApprovvigionamenti'].id}">${ps.stati['StatoApprovvigionamenti'].nome}</option>
                        <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                          <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                        </c:forEach>
                      </select>
                    </div>
                    <div class="col">
                      <select class="form-control bgcolor${ps.stati['StatoStakeholder'].id}" id="sts-stakeholder" name="sts-stakeholder" disabled>
                        <option value="${ps.stati['StatoStakeholder'].id}" selected class="bgcolor${ps.stati['StatoStakeholder'].id}">${ps.stati['StatoStakeholder'].nome}</option>
                        <c:forEach var="status" items="${requestScope.statiValues}" varStatus="loop">
                          <option value="${status.id}" class="bgcolor${status.id}">${status.nome}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <hr class="separatore" />
            </c:when>
            <c:otherwise>
              <div class="alert alert-dark">
                <strong>Spiacente!</strong> 
                <p>
                  Non &egrave; stato trovato alcun status progetto associato al progetto <a title="${p.titolo}">corrente</a>. 
                  Avendone i diritti &egrave; per&ograve; possibile aggiungerne uno.
                </p>
              </div>   
            </c:otherwise>
          </c:choose>
          <hr class="separatore" />
          <div class="form-row">
            <div class="col text-center">
              <%@ include file="subPanel.jspf" %>
            </div>
          </div>
        <c:if test="${not empty ps.allegati}">
          <c:catch var="exception">
          <hr class="separatore" />
          <div class=" lightTable">
            <div class="row">
              <div class="col bordo"><strong>Allegati dell'Avanzamento</strong></div>
            </div>
            <hr class="separatore" />
            <ul class="list-unstyled">
            <c:forEach var="all" items="${ps.allegati}" varStatus="loop">
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
          </c:catch>
          <c:out value="${exception}" />
        </c:if>
        </form>
      </div>
        <form id="note-form" method="post" action="file" enctype="multipart/form-data" class="modal">
          <input type="hidden" id="prj-id" name="prj-id" value="${p.id}" />
          <input type="hidden" id="q" name="q" value="${param['q']}" />
          <input type="hidden" id="sts-id" name="sts-id" value="${ps.id}" />
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
              <input type="file" name="file" size="60" placeholder="Inserisci un file da caricare"><br /><br /> 
            </div>
          </div>
          <hr class="separatore" />
          <div class="row">
            <button type="submit" class="btn btn-warning"  value="Upload"><i class="fas fa-file-upload"></i> Upload</button>
          </div>
        </form>
      <div class="col-3">
        <div class="form-row">
          <div class="col"> 
            <span class="float-right">
              <a class="btn btnNav" href="${project}">
                <i class="fas fa-home"></i>
                Progetti
              </a>
            </span>
          </div>
        </div>
        <hr class="separatore" />
      <c:if test="${not empty ps}">
        <div class="form-row">
          <div class="col-2"></div>
          <div class="col avvisiTot heading">
            <ul class="list-unstyled">
              <li>
                <span>
                  <img src="${initParam.urlDirectoryImmagini}ico_doc.png" border="0" alt="documento formato rtf">
                </span>
                <a href="${initParam.appName}/data?q=pol&out=rtf&id=${p.id}" class="transition">
                  <c:out value="Scarica Relazione" escapeXml="false" />
                </a>
              </li>
            </ul>
          </div>
        </div>
      </c:if>
        <div class="form-row">
          <div class="col-2"></div>
          <div class="col">
            <%@ include file="subStatus.jspf" %>
          </div>
          <div class="col-1"></div>
        </div>
      </div>
    </div>
    <hr class="separatore" />
    <div class="form-row">
      <div class="col">
        <h4><label for="actByRange">Attivit&agrave; correnti:</label></h4>
      </div>
    </div>
    <div class="form-row">
      <div class="col">
        <table class="table table-bordered table-hover" id="actByRange">
          <thead class="thead-light">
          <tr>
            <th scope="col">Nome</th>
            <th scope="col">Descrizione</th>
            <th scope="col"><div class="text-center">Milestone</div></th>
          </tr>
          </thead>
          <tbody>
            <c:set var="status" value="" scope="page" />
            <c:forEach var="actRange" items="${requestScope.activitiesByRange}" varStatus="loop">
            <c:set var="status" value="${loop.index}" scope="page" />
            <tr>
              <td class="bgAct${actRange.stato.id} bgFade">
                <a href="${modAct}${p.id}&ida=${actRange.id}">
                  <c:out value="${actRange.nome}" />
                </a>
              </td>
              <td scope="row">
                <c:out value="${actRange.descrizione}"/>
              </td>
              <td scope="row">
                <input type="hidden" value="${actRange.milestone}" />
                <c:choose>
                  <c:when test="${actRange.milestone}">
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" checked disabled>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" disabled>
                    </div>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
    <div class="form-row">
      <div class="col">
        <h4><label for="actByDate">Attivit&agrave; presenti in futuro:</label></h4>
      </div>
    </div>
    <div class="form-row">
      <div class="col">
        <table class="table table-bordered table-hover" id="actByDate">
          <thead class="thead-light">
          <tr>
            <th scope="col">Nome</th>
            <th scope="col">Descrizione</th>
            <th scope="col"><div class="text-center">Milestone</div></th>
          </tr>
          </thead>
          <tbody>
            <c:forEach var="actDate" items="${requestScope.activitiesByDate}" varStatus="loop">
              <tr>
                <td scope="row" id="nameColumn"  class="bgAct${actDate.stato.id} bgFade">
                  <a href="${modAct}${p.id}&ida=${actDate.id}">
                    <c:out value="${actDate.nome}"/>
                  </a>
                </td>
                <td scope="row">
                  <c:out value="${actDate.descrizione}"/>
                </td>
                <td scope="row">
                  <input type="hidden" value="${actDate.milestone}" />
                  <c:choose>
                    <c:when test="${actDate.milestone}">
                      <div class="form-check text-center">
                        <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" checked disabled>
                      </div>
                    </c:when>
                    <c:otherwise>
                      <div class="form-check text-center">
                        <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" disabled>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <div class="form-row">
          <div class="col"> 
            <span class="float-left">
              <a class="btn btnNav" href="${project}">
                <i class="fas fa-home"></i>
                Progetti
              </a>
            </span>
          </div>
        </div>
      </div>
    </div>
    <script>
         var offsetcharacter = 5;
         $(document).ready(function () {
           $('#status_form').validate ({
             rules: {
                 'sts-avanzamento': {
                     required: true,
                     minlength: offsetcharacter
                 },
                 'sts-datainizio': {
                     required: true
                 },
                 'sts-datafine': {
                     required: true,
                     greaterThan: '#sts-datainizio'
                 }
             }, 
             messages: {
                 'sts-avanzamento': "Inserire una descrizione per lo status di progetto.",
                 'sts-datainizio': "Inserire una data di inizio valida.",
                 'sts-datafine': "Inserire una data di fine valida."
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
                 $(this).next('div').text(' you have reached the limit');
             } else {
                 var chars = dblength - len;
                 $(this).next('div').text(chars + ' characters left');
             }
           });
           $('#actByRange').DataTable({
             "columnDefs": [
               { "orderable": false, "targets": -1 }
             ],
             "lengthMenu": [5, 10, 25, 50, 100],
             "pageLength": 5
           });
           $('#actByDate').DataTable({
             "columnDefs": [
               { "orderable": false, "targets": -1 }
             ],
             "lengthMenu": [5, 10, 25, 50, 100],
             "pageLength": 5
           });
         });
         
         $("select").change(function () {
           $(this).removeClass("bgcolor1");
           $(this).removeClass("bgcolor2");
           $(this).removeClass("bgcolor3");
           if ($(this).val() == 1){
             $(this).addClass("bgcolor1");
           } else if ($(this).val() == 2) {
             $(this).addClass("bgcolor2");
           } else if ($(this).val() == 3) {
             $(this).addClass("bgcolor3");
           } else {}
         });
    </script>
  </c:when>
  <c:otherwise>
    <div class="alert alert-danger">
      <span class="ui-icon ui-icon-alert"></span>
      Non &egrave; possibile accedere a questa funzionalit&agrave;,
      in quanto l'indirizzo immesso risulta errato.<br />
    </div>
  </c:otherwise>
</c:choose>
