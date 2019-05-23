<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <c:url var="delWbs" context="${initParam.appName}" value="/" scope="page">
      <c:param name="q" value="wbs" />
      <c:param name="p" value="del" />
      <c:param name="id" value="${p.id}" />
      <c:param name="idw" value="" />
    </c:url>
    <h4>WBS del sotto progetto <strong><c:out value="${p.titolo}" /></strong></h4>
    <hr class="separatore" />
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafico}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="javascript:alert('Per visualizzare le attivit&agrave; di una WBS cliccare sull\'iconcina in corrispondenza della WBS')" id="show_act">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
    </ul>
    <c:choose>
      <c:when test="${not empty requestScope.wbsHierarchy}">
        <div class="tab-content responsive hidden-xs hidden-sm">
          <div role="tabpanel" class="tab-pane active" id="tab-wbs">
            <br />
            <table class="table table-bordered table-hover">
              <thead class="thead-light">
                <tr>
                  <th class="bg-primary" scope="col" width="*">Nome WBS padre</th>
                  <th class="bg-primary" scope="col" width="20%">Note di avanzamento</th>
                  <th class="bg-primary" scope="col" width="20%">Risultati raggiunti</th>
                  <th class="bg-primary" scope="col" width="5%"><div class="text-center">Workpackage</div></th>
                  <th class="bg-primary" width="5%"><div class="text-center">Funzioni</div></th>
                </tr>
              </thead>
              <tbody>
                <c:set var="duplicateHeaders" value="${false}" scope="page" />
                <c:set var="status" value="" scope="page" />
                <c:forEach var="wbsInstance" items="${requestScope.wbsHierarchy}" varStatus="loop">
                  <c:set var="status" value="${loop.index}" scope="page" />
                  <c:if test="${duplicateHeaders}">
                    <tr class="active">
                      <td class="bg-primary" width="*"><strong>Nome WBS padre</strong></td>
                      <td class="bg-primary" width="20%"><strong>Note di avanzamento</strong></td>
                      <td class="bg-primary" width="20%"><strong>Risultati raggiunti</strong></td>
                      <td class="bg-primary" width="5%"><div class="text-center"><strong>Workpackage</strong></div></td>
                      <td class="bg-primary" width="5%"><div class="text-center"><strong>Funzioni</strong></div></td>
                    </tr>
                  </c:if>
                  <tr>
                    <td scope="col" id="nameColumn">
                      <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbsInstance.id}"/>">
                      <a href="${modWbs}${p.id}&idw=${wbsInstance.id}">
                        <c:out value="${wbsInstance.nome}" />
                      </a>
                    </td>
                    <td scope="col">
                      <c:out value="${wbsInstance.noteAvanzamento}" />
                    </td>
                    <td scope="col">
                      <c:out value="${wbsInstance.risultatiRaggiunti}" />
                    </td>
                    <td scope="col">
                    <c:choose>
                      <c:when test="${wbsInstance.workPackage}">
                      <div class="form-check text-center">
                        <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" checked disabled>
                      </div>
                      </c:when>
                      <c:otherwise>
                      <div class="form-check text-center">
                        <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" disabled>
                      </div>
                      </c:otherwise>
                    </c:choose>
                    </td>
                    <td scope="col">
                    <c:choose>
                      <c:when test="${wbsInstance.attivita.capacity() gt zero}">
                        <c:set var="icoAct" value="ico-act-outline.png" scope="page" />
                        <c:set var="txtAct" value="Mostra Attivit&agrave;" scope="page" />
                      </c:when>
                      <c:otherwise>
                        <c:set var="icoAct" value="ico-act-strike.png" scope="page" />
                        <c:set var="txtAct" value="A questa WBS non sono state associate attivit&agrave;" scope="page" />
                      </c:otherwise>
                    </c:choose>
                      <a href="${act}${p.id}&idw=${wbsInstance.id}" class="ico" id="lightTable">
                        <img src="${initParam.urlDirectoryImmagini}/${icoAct}" class="btn-del" alt="Link a lista attivita" title="${txtAct}" />
                      </a>
                      <a href="#del-form${wbsInstance.id}" class="ico" id="del-wbs" rel="modal:open">
                        <img src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Elimina" title="Elimina" />
                      </a>
                      <form id="del-form${wbsInstance.id}" method="post" action="${delWbs}${wbsInstance.id}" class="modal">
                        <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsInstance.id}" />
                        <h3 class="heading">Riepilogo della WBS selezionata</h3>
                        <br />
                        <div class="row">
                          <div class="col-sm-5">
                            <strong>
                              WBS padre (se &eacute; presente) 
                              <sup>&#10039;</sup>:
                            </strong>
                          </div>
                          <div class="col-sm-5">
                            <c:choose>
                              <c:when test="${wbsInstance.wbsPadre.id ne -3}">
                                <c:out value="${wbsInstance.wbsPadre.nome}" />
                              </c:when>
                              <c:otherwise>
                                <c:out value="Nessuna wbs padre" />
                              </c:otherwise>
                            </c:choose>
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Nome WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbsInstance.nome}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Descrizione della WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbsInstance.descrizione}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Note di avanzamento della WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbsInstance.noteAvanzamento}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Risultati raggiunti della WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbsInstance.risultatiRaggiunti}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Workpackage:</strong></div>
                          <div class="col-sm-5">
                            <c:choose>
                              <c:when test="${wbsInstance.workPackage}">
                                <span class="ui-icon ui-icon-check"></span>
                              </c:when>
                              <c:otherwise>
                                No
                              </c:otherwise>
                            </c:choose>
                          </div>
                        </div>
                        <br />
                        <c:if test="${wbsInstance.attivita.capacity() gt zero}">
                          <div class="row">
                            <div class="col-sm-5"><strong>Attivit&agrave; collegate: ${wbsInstance.attivita.capacity()}</strong></div>
                            <div class="col-sm-5">
                              <a href="${act}${p.id}&idw=${wbsInstance.id}">Vedi Attivit&agrave;</a>
                            </div>
                          </div>
                        </c:if>
                        <br />
                        <c:if test="${fn:length(wbsInstance.wbsFiglie) gt zero}">
                          <div class="row">
                            <div class="col-sm-5"><strong>WBS direttamente collegate:</strong></div>
                            <div class="col-sm-5">
                              <select class="form-control" id="act-role" name="act-role" multiple>
                                <c:forEach var="wbsChild" items="${wbsInstance.wbsFiglie}" varStatus="loop">
                                  <option value="${wbsChild.id}">
                                    <c:out value="${wbsChild.nome}" />
                                  </option>
                                </c:forEach>
                              </select>
                            </div>
                          </div>
                        </c:if>
                        <br />
                        <c:choose>
                          <c:when test="${wbsInstance.attivita.capacity() eq zero and fn:length(wbsInstance.wbsFiglie) eq zero}">
                            <%@ include file="subPanel.jspf" %>
                          </c:when>
                          <c:otherwise>
                            <div class="alert alert-warning">
                              <p>
                                <span class="ui-icon ui-icon-alert"></span>
                                &nbsp;Non &egrave; possibile eliminare questa wbs, in quanto ha ancora attivit&agrave; o altre wbs collegate.
                              </p>
                            </div>
                          </c:otherwise>
                        </c:choose>
                      </form>
                    </td>
                  </tr>
                  <c:if test="${not empty wbsInstance.wbsFiglie}">
                    <c:set var="duplicateHeaders" value="${true}" scope="page" />
                    <tr>
                      <td colspan="5">
                        <table class="table table-condensed col-md-11" style="margin-left:10px;">
                          <thead class="thead-light">
                            <tr>
                              <th scope="col" width="60%">Nome WBS figlia</th>
                              <th width="15%">Note di avanzamento</th>
                              <th width="15%">Risultati raggiunti</th>
                              <th width="5%"><div class="text-center">Workpackage</div></th>
                              <th width="5%"><div class="text-center">Funzioni</div></th>
                            </tr>
                          </thead>
                     	    <tbody>
                            <c:forEach var="wbsFiglio" items="${wbsInstance.wbsFiglie}" varStatus="loop">
                            <c:set var="status" value="${loop.index}" scope="page" />
                            <tr>
                              <td scope="col" id="nameColumn">
                                <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbsFiglio.id}"/>">
                                <a href="${modWbs}${p.id}&idw=${wbsFiglio.id}">
                                  <c:out value="${wbsFiglio.nome}" />
                                </a>
                                <c:if test="${not empty wbsFiglio.wbsFiglie}">
                                  <div class="moduleThin">
                                    <p class="bg-warning"><strong>Sottoelenco (Livello 3)</strong></p>
                                    <ol class="list-unstyled">
                                      <c:forEach var="wbsNipote" items="${wbsFiglio.wbsFiglie}">
                                        <li>
                                          <a href="${modWbs}${p.id}&idw=${wbsNipote.id}">
                                            <c:out value="${wbsNipote.nome}" />
                                          </a>
                                          <c:if test="${wbsNipote.workPackage}">
                                            <cite>(Workpackage)</cite>
                                          </c:if>
                      	                  <div class="text-right">
                                          <c:choose>
                                            <c:when test="${wbsNipote.attivita.capacity() gt zero}">
                                              <c:set var="icoAct" value="ico-act-outline.png" scope="page" />
                                              <c:set var="txtAct" value="Mostra Attivit&agrave;" scope="page" />
                                            </c:when>
                                            <c:otherwise>
                                              <c:set var="icoAct" value="ico-act-strike.png" scope="page" />
                                              <c:set var="txtAct" value="A questa WBS non sono state associate attivit&agrave;" scope="page" />
                                            </c:otherwise>
                                          </c:choose>
                                            <a href="${act}${p.id}&idw=${wbsNipote.id}" class="ico" id="wbsAct">
                                              <img src="${initParam.urlDirectoryImmagini}/${icoAct}" class="btn-del" alt="Link a lista attivita" title="${txtAct}" />
                                            </a>
                                            <a href="#del-form${wbsNipote.id}" class="ico" id="del-wbs" rel="modal:open">
                                              <img src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Elimina" title="Elimina" />
                                            </a>
                                            <form id="del-form${wbsNipote.id}" method="post" action="${delWbs}${wbsNipote.id}" class="modal">
                                              <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsNipote.id}" />
                                              <h3 class="heading">Riepilogo della WBS selezionata</h3>
                                              <br />
                                              <div class="row">
                                                <div class="col-sm-5">
                                                  <strong>
                                                    WBS padre (se &eacute; presente) 
                                                    <sup>&#10039;</sup>:
                                                  </strong>
                                                </div>
                                                <div class="col-sm-5">
                                                  <c:choose>
                                                    <c:when test="${wbsNipote.wbsPadre.id ne -3}">
                                                      <c:out value="${wbsNipote.wbsPadre.nome}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                      <c:out value="Nessuna wbs padre" />
                                                    </c:otherwise>
                                                  </c:choose>
                                                </div>
                                              </div>
                                              <br />
                                              <div class="row">
                                                <div class="col-sm-5"><strong>Nome WBS:</strong></div>
                                                <div class="col-sm-5">
                                                  <c:out value="${wbsNipote.nome}" />
                                                </div>
                                              </div>
                                              <br />
                                              <div class="row">
                                                <div class="col-sm-5"><strong>Descrizione della WBS:</strong></div>
                                                <div class="col-sm-5">
                                                  <c:out value="${wbsNipote.descrizione}" />
                                                </div>
                                              </div>
                                              <br />
                                              <div class="row">
                                                <div class="col-sm-5"><strong>Note di avanzamento della WBS:</strong></div>
                                                <div class="col-sm-5">
                                                  <c:out value="${wbsNipote.noteAvanzamento}" />
                                                </div>
                                              </div>
                                              <br />
                                              <div class="row">
                                                <div class="col-sm-5"><strong>Risultati raggiunti della WBS:</strong></div>
                                                <div class="col-sm-5">
                                                  <c:out value="${wbsNipote.risultatiRaggiunti}" />
                                                </div>
                                              </div>
                                              <br />
                                              <div class="row">
                                                <div class="col-sm-5"><strong>Workpackage:</strong></div>
                                                <div class="col-sm-5">
                                                  <c:choose>
                                                    <c:when test="${wbsNipote.workPackage}">
                                                      <span class="ui-icon ui-icon-check"></span>
                                                    </c:when>
                                                    <c:otherwise>
                                                      No
                                                    </c:otherwise>
                                                  </c:choose>
                                                </div>
                                              </div>
                                              <br />
                                              <c:if test="${wbsNipote.attivita.capacity() gt zero}">
                                                <div class="row">
                                                  <div class="col-sm-5"><strong>Attivit&agrave; collegate: ${wbsNipote.attivita.capacity()}</strong></div>
                                                  <div class="col-sm-5">
                                                    <a href="${act}${p.id}&idw=${wbsNipote.id}">Vedi Attivit&agrave;</a>
                                                  </div>
                                                </div>
                                              </c:if>
                                              <br />
                                              <c:if test="${fn:length(wbsNipote.wbsFiglie) gt zero}">
                                                <div class="row">
                                                  <div class="col-sm-5"><strong>WBS direttamente collegate:</strong></div>
                                                  <div class="col-sm-5">
                                                    <select class="form-control" id="act-role" name="act-role" multiple>
                                                      <c:forEach var="wbsChild" items="${wbsNipote.wbsFiglie}" varStatus="loop">
                                                        <option value="${wbsChild.id}">
                                                          <c:out value="${wbsChild.nome}" />
                                                        </option>
                                                      </c:forEach>
                                                    </select>
                                                  </div>
                                                </div>
                                              </c:if>
                                              <br />
                                              <c:choose>
                                                <c:when test="${wbsNipote.attivita.capacity() eq zero and fn:length(wbsNipote.wbsFiglie) eq zero}">
                                                  <%@ include file="subPanel.jspf" %>
                                                </c:when>
                                                <c:otherwise>
                                                  <div class="alert alert-warning">
                                                    <p>
                                                      <span class="ui-icon ui-icon-alert"></span>
                                                      &nbsp;Non &egrave; possibile eliminare questa wbs, in quanto ha ancora attivit&agrave; o altre wbs collegate.
                                                    </p>
                                                  </div>
                                                </c:otherwise>
                                              </c:choose>
                                            </form>
                                          </div>
                                          <hr class="riga" />
                                          <c:if test="${not empty wbsNipote.wbsFiglie}">
                                            <div class="moduleThin2">
                                              <p><strong>Sottoelenco (Livello 4)</strong></p>
                                              <ul class="list-unstyled">
                                                <c:forEach var="wbsPronipote" items="${wbsNipote.wbsFiglie}">
                                                  <li>
                                                  <a href="${modWbs}${p.id}&idw=${wbsPronipote.id}">
                                                    <c:out value="${wbsPronipote.nome}" />
                                                  </a>
                                                  <c:if test="${wbsPronipote.workPackage}">
                                                    <cite>(Workpackage)</cite>
                                                  </c:if>
                                                  <div class="text-right">
                                                  <c:choose>
                                                    <c:when test="${wbsPronipote.attivita.capacity() gt zero}">
                                                      <c:set var="icoAct" value="ico-act-outline.png" scope="page" />
                                                      <c:set var="txtAct" value="Mostra Attivit&agrave;" scope="page" />
                                                    </c:when>
                                                    <c:otherwise>
                                                      <c:set var="icoAct" value="ico-act-strike.png" scope="page" />
                                                      <c:set var="txtAct" value="A questa WBS non sono state associate attivit&agrave;" scope="page" />
                                                    </c:otherwise>
                                                  </c:choose>
                                                    <a href="${act}${p.id}&idw=${wbsPronipote.id}" class="ico" id="wbsActLev4">
                                                      <img src="${initParam.urlDirectoryImmagini}/${icoAct}" class="btn-del" alt="Link a lista attivita" title="${txtAct}" />
                                                    </a>
                                                    <a href="#del-form${wbsPronipote.id}" class="ico" id="del-wbs" rel="modal:open">
                                                      <img src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Elimina" title="Elimina" />
                                                    </a>
                                                    <form id="del-form${wbsPronipote.id}" method="post" action="${delWbs}${wbsPronipote.id}" class="modal">
                                                      <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsPronipote.id}" />
                                                      <h3 class="heading">Riepilogo della WBS selezionata</h3>
                                                      <br />
                                                      <div class="row">
                                                        <div class="col-sm-5">
                                                          <strong>
                                                            WBS padre (se &eacute; presente) 
                                                            <sup>&#10039;</sup>:
                                                          </strong>
                                                        </div>
                                                        <div class="col-sm-5">
                                                          <c:choose>
                                                            <c:when test="${wbsPronipote.wbsPadre.id ne -3}">
                                                              <c:out value="${wbsPronipote.wbsPadre.nome}" />
                                                            </c:when>
                                                            <c:otherwise>
                                                              <c:out value="Nessuna wbs padre" />
                                                            </c:otherwise>
                                                          </c:choose>
                                                        </div>
                                                      </div>
                                                      <br />
                                                      <div class="row">
                                                        <div class="col-sm-5"><strong>Nome WBS:</strong></div>
                                                        <div class="col-sm-5">
                                                          <c:out value="${wbsPronipote.nome}" />
                                                        </div>
                                                      </div>
                                                      <br />
                                                      <div class="row">
                                                        <div class="col-sm-5"><strong>Descrizione della WBS:</strong></div>
                                                        <div class="col-sm-5">
                                                          <c:out value="${wbsPronipote.descrizione}" />
                                                        </div>
                                                      </div>
                                                      <br />
                                                      <div class="row">
                                                        <div class="col-sm-5"><strong>Note di avanzamento della WBS:</strong></div>
                                                        <div class="col-sm-5">
                                                          <c:out value="${wbsPronipote.noteAvanzamento}" />
                                                        </div>
                                                      </div>
                                                      <br />
                                                      <div class="row">
                                                        <div class="col-sm-5"><strong>Risultati raggiunti della WBS:</strong></div>
                                                        <div class="col-sm-5">
                                                          <c:out value="${wbsPronipote.risultatiRaggiunti}" />
                                                        </div>
                                                      </div>
                                                      <br />
                                                      <div class="row">
                                                        <div class="col-sm-5"><strong>Workpackage:</strong></div>
                                                        <div class="col-sm-5">
                                                          <c:choose>
                                                            <c:when test="${wbsPronipote.workPackage}">
                                                              <span class="ui-icon ui-icon-check"></span>
                                                            </c:when>
                                                            <c:otherwise>
                                                              No
                                                            </c:otherwise>
                                                          </c:choose>
                                                        </div>
                                                      </div>
                                                      <br />
                                                      <c:if test="${wbsPronipote.attivita.capacity() gt zero}">
                                                        <div class="row">
                                                          <div class="col-sm-5"><strong>Attivit&agrave; collegate: ${wbsPronipote.attivita.capacity()}</strong></div>
                                                          <div class="col-sm-5">
                                                            <a href="${act}${p.id}&idw=${wbsPronipote.id}">Vedi Attivit&agrave;</a>
                                                          </div>
                                                        </div>
                                                      </c:if>
                                                      <br />
                                                      <c:if test="${fn:length(wbsPronipote.wbsFiglie) gt zero}">
                                                        <div class="row">
                                                          <div class="col-sm-5"><strong>WBS direttamente collegate:</strong></div>
                                                          <div class="col-sm-5">
                                                            <select class="form-control" id="act-role" name="act-role" multiple>
                                                              <c:forEach var="wbsChild" items="${wbsPronipote.wbsFiglie}" varStatus="loop">
                                                                <option value="${wbsChild.id}">
                                                                  <c:out value="${wbsChild.nome}" />
                                                                </option>
                                                              </c:forEach>
                                                            </select>
                                                          </div>
                                                        </div>
                                                      </c:if>
                                                      <br />
                                                      <c:choose>
                                                        <c:when test="${wbsPronipote.attivita.capacity() eq zero and fn:length(wbsPronipote.wbsFiglie) eq zero}">
                                                          <%@ include file="subPanel.jspf" %>
                                                        </c:when>
                                                        <c:otherwise>
                                                          <div class="alert alert-warning">
                                                            <p>
                                                              <span class="ui-icon ui-icon-alert"></span>
                                                              &nbsp;Non &egrave; possibile eliminare questa wbs, in quanto ha ancora attivit&agrave; o altre wbs collegate.
                                                            </p>
                                                          </div>
                                                        </c:otherwise>
                                                      </c:choose>
                                                    </form>
                                                  </div>
                                                  <hr class="riga" />
                                                  <c:if test="${not empty wbsPronipote.wbsFiglie}">
                                                    <div class="moduleThin3">
                                                      <p><strong>Sottoelenco (Livello 5)</strong></p>
                                                      <ul class="list-unstyled">
                                                        <c:forEach var="wbsProPronipote" items="${wbsPronipote.wbsFiglie}">
                                                          <li>
                                                            <a href="${modWbs}${p.id}&idw=${wbsProPronipote.id}">
                                                              <c:out value="${wbsProPronipote.nome}" />
                                                            </a>
                                                            <c:if test="${wbsProPronipote.workPackage}">
                                                              <cite>(Workpackage)</cite>
                                                            </c:if>
                                                            <div class="text-right">
                                                            <c:choose>
                                                              <c:when test="${wbsProPronipote.attivita.capacity() gt zero}">
                                                                <c:set var="icoAct" value="ico-act-outline.png" scope="page" />
                                                                <c:set var="txtAct" value="Mostra Attivit&agrave;" scope="page" />
                                                              </c:when>
                                                              <c:otherwise>
                                                                <c:set var="icoAct" value="ico-act-strike.png" scope="page" />
                                                                <c:set var="txtAct" value="A questa WBS non sono state associate attivit&agrave;" scope="page" />
                                                              </c:otherwise>
                                                            </c:choose>
                                                              <a href="${act}${p.id}&idw=${wbsProPronipote.id}" class="ico" id="wbsActLev5">
                                                                <img src="${initParam.urlDirectoryImmagini}/${icoAct}" class="btn-del" alt="Link a lista attivita" title="${txtAct}" />
                                                              </a>
                                                              <a href="#del-form${wbsProPronipote.id}" class="ico" id="del-wbs" rel="modal:open">
                                                                <img src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Elimina" title="Elimina" />
                                                              </a>
                                                              <form id="del-form${wbsProPronipote.id}" method="post" action="${delWbs}${wbsProPronipote.id}" class="modal">
                                                                <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsProPronipote.id}" />
                                                                <h3 class="heading">Riepilogo della WBS selezionata</h3>
                                                                <br />
                                                                <div class="row">
                                                                  <div class="col-sm-5">
                                                                    <strong>
                                                                      WBS padre (se &eacute; presente) 
                                                                      <sup>&#10039;</sup>:
                                                                    </strong>
                                                                  </div>
                                                                  <div class="col-sm-5">
                                                                    <c:choose>
                                                                      <c:when test="${wbsProPronipote.wbsPadre.id ne -3}">
                                                                        <c:out value="${wbsProPronipote.wbsPadre.nome}" />
                                                                      </c:when>
                                                                      <c:otherwise>
                                                                        <c:out value="Nessuna wbs padre" />
                                                                      </c:otherwise>
                                                                    </c:choose>
                                                                  </div>
                                                                </div>
                                                                <br />
                                                                <div class="row">
                                                                  <div class="col-sm-5"><strong>Nome WBS:</strong></div>
                                                                  <div class="col-sm-5">
                                                                    <c:out value="${wbsProPronipote.nome}" />
                                                                  </div>
                                                                </div>
                                                                <br />
                                                                <div class="row">
                                                                  <div class="col-sm-5"><strong>Descrizione della WBS:</strong></div>
                                                                  <div class="col-sm-5">
                                                                    <c:out value="${wbsProPronipote.descrizione}" />
                                                                  </div>
                                                                </div>
                                                                <br />
                                                                <div class="row">
                                                                  <div class="col-sm-5"><strong>Note di avanzamento della WBS:</strong></div>
                                                                  <div class="col-sm-5">
                                                                    <c:out value="${wbsProPronipote.noteAvanzamento}" />
                                                                  </div>
                                                                </div>
                                                                <br />
                                                                <div class="row">
                                                                  <div class="col-sm-5"><strong>Risultati raggiunti della WBS:</strong></div>
                                                                  <div class="col-sm-5">
                                                                    <c:out value="${wbsProPronipote.risultatiRaggiunti}" />
                                                                  </div>
                                                                </div>
                                                                <br />
                                                                <div class="row">
                                                                  <div class="col-sm-5"><strong>Workpackage:</strong></div>
                                                                  <div class="col-sm-5">
                                                                    <c:choose>
                                                                      <c:when test="${wbsProPronipote.workPackage}">
                                                                        <span class="ui-icon ui-icon-check"></span>
                                                                      </c:when>
                                                                      <c:otherwise>
                                                                        No
                                                                      </c:otherwise>
                                                                    </c:choose>
                                                                  </div>
                                                                </div>
                                                                <br />
                                                                <c:if test="${wbsProPronipote.attivita.capacity() gt zero}">
                                                                  <div class="row">
                                                                    <div class="col-sm-5"><strong>Attivit&agrave; collegate:${wbsProPronipote.attivita.capacity()}</strong></div>
                                                                    <div class="col-sm-5">
                                                                      <a href="${act}${p.id}&idw=${wbsProPronipote.id}">Vedi Attivit&agrave;</a>
                                                                    </div>
                                                                  </div>
                                                                </c:if>
                                                                <br />
                                                                <c:if test="${fn:length(wbsProPronipote.wbsFiglie) gt zero}">
                                                                  <div class="row">
                                                                    <div class="col-sm-5"><strong>WBS direttamente collegate:</strong></div>
                                                                    <div class="col-sm-5">
                                                                      <select class="form-control" id="act-role" name="act-role" multiple>
                                                                        <c:forEach var="wbsChild" items="${wbsProPronipote.wbsFiglie}" varStatus="loop">
                                                                          <option value="${wbsChild.id}">
                                                                            <c:out value="${wbsChild.nome}" />
                                                                          </option>
                                                                        </c:forEach>
                                                                      </select>
                                                                    </div>
                                                                  </div>
                                                                </c:if>
                                                                <br />
                                                                <c:choose>
                                                                  <c:when test="${wbsProPronipote.attivita.capacity() eq zero and fn:length(wbsProPronipote.wbsFiglie) eq zero}">
                                                                    <%@ include file="subPanel.jspf" %>
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                    <div class="alert alert-warning">
                                                                      <p>
                                                                        <span class="ui-icon ui-icon-alert"></span>
                                                                        &nbsp;Non &egrave; possibile eliminare questa wbs, in quanto ha ancora attivit&agrave; o altre wbs collegate.
                                                                      </p>
                                                                    </div>
                                                                  </c:otherwise>
                                                                </c:choose>
                                                              </form>
                                                            </div>
                                                            <hr class="riga" />
                                                          </li>
                                                        </c:forEach>
                                                      </ul>
                                                    </div>
                                                  </c:if>
                                                </c:forEach>
                                              </ul>
                                            </div>
                                          </c:if>
                                        </li>
                                      </c:forEach>
                                    </ol>
                                  </div>
                                </c:if>
                              </td>
                              <td scope="col">
                                <c:out value="${wbsFiglio.noteAvanzamento}" />
                              </td>
                              <td scope="col">
                                <c:out value="${wbsFiglio.risultatiRaggiunti}" />
                              </td>
                              <td scope="col">
                                <c:choose>
                                  <c:when test="${wbsFiglio.workPackage}">
                                    <div class="form-check text-center">
                                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" checked disabled>
                                    </div>
                                  </c:when>
                                  <c:otherwise>
                                    <div class="form-check text-center">
                                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" disabled>
                                    </div>
                                  </c:otherwise>
                                </c:choose>
                              </td>
                              <td scope="col" align="center">
                              <c:choose>
                                <c:when test="${wbsFiglio.attivita.capacity() gt zero}">
                                  <c:set var="icoAct" value="ico-act-outline.png" scope="page" />
                                  <c:set var="txtAct" value="Mostra Attivit&agrave;" scope="page" />
                                </c:when>
                                <c:otherwise>
                                  <c:set var="icoAct" value="ico-act-strike.png" scope="page" />
                                  <c:set var="txtAct" value="A questa WBS non sono state associate attivit&agrave;" scope="page" />
                                </c:otherwise>
                              </c:choose>
                                <a href="${act}${p.id}&idw=${wbsFiglio.id}" class="ico" id="wbsActLev2">
                                  <img src="${initParam.urlDirectoryImmagini}/${icoAct}" class="btn-del" alt="Link a lista attivita" title="${txtAct}" />
                                </a>
                                <a href="#del-form${wbsFiglio.id}" class="ico" id="del-wbs" rel="modal:open">
                                  <img src="${initParam.urlDirectoryImmagini}/ico-del-outline.png" class="btn-del" alt="Elimina" title="Elimina" />
                                </a>
                                <form id="del-form${wbsFiglio.id}" method="post" action="${delWbs}${wbsFiglio.id}" class="modal">
                                  <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsFiglio.id}" />
                                  <h3 class="heading">Riepilogo della WBS selezionata</h3>
                                  <br />
                                  <div class="row">
                                    <div class="col-sm-5">
                                      <strong>
                                        WBS padre (se &eacute; presente) 
                                        <sup>&#10039;</sup>:
                                      </strong>
                                    </div>
                                    <div class="col-sm-5">
                                      <c:choose>
                                        <c:when test="${wbsFiglio.wbsPadre.id ne -3}">
                                          <c:out value="${wbsFiglio.wbsPadre.nome}" />
                                        </c:when>
                                        <c:otherwise>
                                          <c:out value="Nessuna wbs padre" />
                                        </c:otherwise>
                                      </c:choose>
                                    </div>
                                  </div>
                                  <br />
                                  <div class="row">
                                    <div class="col-sm-5"><strong>Nome WBS:</strong></div>
                                    <div class="col-sm-5">
                                      <c:out value="${wbsFiglio.nome}" />
                                    </div>
                                  </div>
                                  <br />
                                  <div class="row">
                                    <div class="col-sm-5"><strong>Descrizione della WBS:</strong></div>
                                    <div class="col-sm-5">
                                      <c:out value="${wbsFiglio.descrizione}" />
                                    </div>
                                  </div>
                                  <br />
                                  <div class="row">
                                    <div class="col-sm-5"><strong>Note di avanzamento della WBS:</strong></div>
                                    <div class="col-sm-5">
                                      <c:out value="${wbsFiglio.noteAvanzamento}" />
                                    </div>
                                  </div>
                                  <br />
                                  <div class="row">
                                    <div class="col-sm-5"><strong>Risultati raggiunti della WBS:</strong></div>
                                    <div class="col-sm-5">
                                      <c:out value="${wbsFiglio.risultatiRaggiunti}" />
                                    </div>
                                  </div>
                                  <br />
                                  <div class="row">
                                    <div class="col-sm-5"><strong>Workpackage:</strong></div>
                                    <div class="col-sm-5">
                                      <c:choose>
                                        <c:when test="${wbsFiglio.workPackage}">
                                          <span class="ui-icon ui-icon-check"></span>
                                        </c:when>
                                        <c:otherwise>
                                          No
                                        </c:otherwise>
                                      </c:choose>
                                    </div>
                                  </div>
                                  <br />
                                  <c:if test="${wbsFiglio.attivita.capacity() gt zero}">
                                    <div class="row">
                                      <div class="col-sm-5"><strong>Attivit&agrave; collegate: ${wbsFiglio.attivita.capacity()}</strong></div>
                                      <div class="col-sm-5">
                                        <a href="${act}${p.id}&idw=${wbsFiglio.id}">Vedi Attivit&agrave;</a>
                                      </div>
                                    </div>
                                  </c:if>
                                  <br />
                                  <c:if test="${fn:length(wbsFiglio.wbsFiglie) gt zero}">
                                    <div class="row">
                                      <div class="col-sm-5"><strong>WBS direttamente collegate:</strong></div>
                                      <div class="col-sm-5">
                                        <select class="form-control" id="act-role" name="act-role" multiple>
                                          <c:forEach var="wbsChild" items="${wbsFiglio.wbsFiglie}" varStatus="loop">
                                            <option value="${wbsChild.id}">
                                              <c:out value="${wbsChild.nome}" />
                                            </option>
                                          </c:forEach>
                                        </select>
                                      </div>
                                    </div>
                                  </c:if>
                                  <br />
                                  <c:choose>
                                    <c:when test="${wbsFiglio.attivita.capacity() eq zero and fn:length(wbsFiglio.wbsFiglie) eq zero}">
                                      <%@ include file="subPanel.jspf" %>
                                    </c:when>
                                    <c:otherwise>
                                      <div class="alert alert-warning">
                                        <p>
                                          <span class="ui-icon ui-icon-alert"></span>
                                          &nbsp;Non &egrave; possibile eliminare questa wbs, in quanto ha ancora attivit&agrave; o altre wbs collegate.
                                        </p>
                                      </div>
                                    </c:otherwise>
                                  </c:choose>
                                </form>
                              </td>
                            </tr>
                            </c:forEach>
                          </tbody>
                        </table>
                      </td>
                    </tr>
                  </c:if>
                </c:forEach>
              </tbody>
            </table>
            <input type="hidden" id="wbs-loop-status" name="wbs-loop-status" value="<c:out value="${status}"/>">
          </div>
        </div>
      </c:when>
      <c:otherwise>
        <hr class="separatore" />
        <div class="alert alert-danger">
          <strong>Spiacente 
            <c:out value="${sessionScope.usr.nome}" />
            <c:out value="${sessionScope.usr.cognome}" />.<br />
          </strong>
          <p>Non sono state trovate WBS associate al progetto.</p>
        </div>
      </c:otherwise>
    </c:choose>
    <div id="container-fluid">
      <hr class="separatore" />
      <div class="row">
        <div class="col-2">  
          <span class="float-left">
            <a class="btn btnNav" href="${project}">
              <i class="fas fa-home"></i>
              Progetti
            </a>
          </span>
        </div>
        <div class="col-8 text-center">
          <a href="${addWbs}${p.id}" class="btn btn-success" id="add-wbs"><i class="fas fa-plus"></i> Aggiungi</a>
        </div>
      </div>
    </div>