<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <h2>WBS del sotto progetto <strong><c:out value="${p.titolo}" /></strong></h2>
    <hr class="separatore" />
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafico}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="javascript:alert('Occorre selezionare una WBS per visualizzarne le attività')" id="show_act">Attività</a></li>
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
                <c:forEach var="wbs" items="${requestScope.wbsHierarchy}" varStatus="loop">
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
                      <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbs.id}"/>">
                      <a href="${modWbs}${p.id}&idw=${wbs.id}">
                        <c:out value="${wbs.nome}" />
                      </a>
                    </td>
                    <td scope="col">
                      <c:out value="${wbs.noteAvanzamento}" />
                    </td>
                    <td scope="col">
                      <c:out value="${wbs.risultatiRaggiunti}" />
                    </td>
                    <td scope="col">
                      <c:choose>
                        <c:when test="${wbs.workPackage}">
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
                      <a href="#del-form${wbs.id}" class="btn btn-primary" id="del-wbs" rel="modal:open">Elimina</a>
                      <form id="del-form${wbs.id}" method="post" action="${wbs}${p.id}" class="modal">
                        <input type="hidden" id="wbs-id" name="wbs-id" value="${wbs.id}" />
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
                              <c:when test="${wbs.wbsPadre.id ne -3}">
                                <c:out value="${wbs.wbsPadre.nome}" />
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
                            <c:out value="${wbs.nome}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Descrizione della WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbs.descrizione}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Note di avanzamento della WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbs.noteAvanzamento}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Risultati raggiunti della WBS:</strong></div>
                          <div class="col-sm-5">
                            <c:out value="${wbs.risultatiRaggiunti}" />
                          </div>
                        </div>
                        <br />
                        <div class="row">
                          <div class="col-sm-5"><strong>Workpackage:</strong></div>
                          <div class="col-sm-5">
                            <c:choose>
                              <c:when test="${wbs.workPackage}">
                                <span class="ui-icon ui-icon-check"></span>
                              </c:when>
                              <c:otherwise>
                                No
                              </c:otherwise>
                            </c:choose>
                          </div>
                        </div>
                        <br />
                        <c:if test="${fn:length(wbs.attivita) gt 0}">
                          <div class="row">
                            <div class="col-sm-5"><strong>Attivit&agrave; collegate:</strong></div>
                            <div class="col-sm-5">
                              <a href="${act}${p.id}&idw=${wbs.id}">Vedi Attivit&agrave;</a>
                            </div>
                          </div>
                        </c:if>
                        <br />
                        <c:if test="${fn:length(wbs.wbsFiglie) gt 0}">
                          <div class="row">
                            <div class="col-sm-5"><strong>WBS direttamente collegate:</strong></div>
                            <div class="col-sm-5">
                              <select class="form-control" id="act-role" name="act-role" multiple>
                                <c:forEach var="wbsChild" items="${wbs.wbsFiglie}" varStatus="loop">
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
                          <c:when test="${fn:length(wbs.attivita) eq 0 && fn:length(wbs.wbsFiglie) eq 0}">
                            <span class="right"><input type="submit" class="btn btn-danger" id="del-wbs" value="Elimina" /></span>
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
                  <c:if test="${not empty wbs.wbsFiglie}">
                    <c:set var="duplicateHeaders" value="${true}" scope="page" />
                    <tr>
                      <td colspan="5" >
                        <table class="table table-condensed col-md-10">
                          <thead class="thead-light">
                            <tr>
                              <th scope="col" width="*">Nome WBS figlia</th>
                              <th scope="col" width="5%"><div class="text-center">Workpackage</div></th>
                              <th scope="col" width="5%"><div class="text-center">Funzioni</div></td>
                            </tr>
                          </thead>
                     	  <tbody> 
                            <c:forEach var="wbsFiglio" items="${wbs.wbsFiglie}" varStatus="loop">
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
                                            <a href="#del-form${wbsNipote.id}" class="btn btn-primary" id="del-wbs" rel="modal:open">Elimina</a>
                                            <form id="del-form${wbsNipote.id}" method="post" action="${wbs}${p.id}" class="modal">
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
                                              <c:if test="${fn:length(wbsNipote.attivita) gt 0}">
                                                <div class="row">
                                                  <div class="col-sm-5"><strong>Attivit&agrave; collegate:</strong></div>
                                                  <div class="col-sm-5">
                                                    <a href="${act}${p.id}&idw=${wbsNipote.id}">Vedi Attivit&agrave;</a>
                                                  </div>
                                                </div>
                                              </c:if>
                                              <br />
                                              <c:if test="${fn:length(wbsNipote.wbsFiglie) gt 0}">
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
                                                <c:when test="${fn:length(wbsNipote.attivita) eq 0 && fn:length(wbsNipote.wbsFiglie) eq 0}">
                                                  <span class="right"><input type="submit" class="btn btn-danger" id="del-wbs" value="Elimina" /></span>
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
                                                    <a href="#del-form${wbsPronipote.id}" class="btn btn-primary" id="del-wbs" rel="modal:open">Elimina</a>
                                                    <form id="del-form${wbsPronipote.id}" method="post" action="${wbs}${p.id}" class="modal">
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
                                                      <c:if test="${fn:length(wbsPronipote.attivita) gt 0}">
                                                        <div class="row">
                                                          <div class="col-sm-5"><strong>Attivit&agrave; collegate:</strong></div>
                                                          <div class="col-sm-5">
                                                            <a href="${act}${p.id}&idw=${wbsPronipote.id}">Vedi Attivit&agrave;</a>
                                                          </div>
                                                        </div>
                                                      </c:if>
                                                      <br />
                                                      <c:if test="${fn:length(wbsPronipote.wbsFiglie) gt 0}">
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
                                                        <c:when test="${fn:length(wbsPronipote.attivita) eq 0 && fn:length(wbsPronipote.wbsFiglie) eq 0}">
                                                          <span class="right"><input type="submit" class="btn btn-danger" id="del-wbs" value="Elimina" /></span>
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
                                                              <a href="#del-form${wbsProPronipote.id}" class="btn btn-primary" id="del-wbs" rel="modal:open">Elimina</a>
                                                              <form id="del-form${wbsProPronipote.id}" method="post" action="${wbs}${p.id}" class="modal">
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
                                                                <c:if test="${fn:length(wbsProPronipote.attivita) gt 0}">
                                                                  <div class="row">
                                                                    <div class="col-sm-5"><strong>Attivit&agrave; collegate:</strong></div>
                                                                    <div class="col-sm-5">
                                                                      <a href="${act}${p.id}&idw=${wbsProPronipote.id}">Vedi Attivit&agrave;</a>
                                                                    </div>
                                                                  </div>
                                                                </c:if>
                                                                <br />
                                                                <c:if test="${fn:length(wbsProPronipote.wbsFiglie) gt 0}">
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
                                                                  <c:when test="${fn:length(wbsProPronipote.attivita) eq 0 && fn:length(wbsProPronipote.wbsFiglie) eq 0}">
                                                                    <span class="right"><input type="submit" class="btn btn-danger" id="del-wbs" value="Elimina" /></span>
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
                              <td scope="col">
                                <a href="#del-form${wbsFiglio.id}" class="btn btn-primary" id="del-wbs" rel="modal:open">Elimina</a>
                                <form id="del-form${wbsFiglio.id}" method="post" action="${wbs}${p.id}" class="modal">
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
                                  <c:if test="${fn:length(wbsFiglio.attivita) gt 0}">
                                    <div class="row">
                                      <div class="col-sm-5"><strong>Attivit&agrave; collegate:</strong></div>
                                      <div class="col-sm-5">
                                        <a href="${act}${p.id}&idw=${wbsFiglio.id}">Vedi Attivit&agrave;</a>
                                      </div>
                                    </div>
                                  </c:if>
                                  <br />
                                  <c:if test="${fn:length(wbsFiglio.wbsFiglie) gt 0}">
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
                                    <c:when test="${fn:length(wbsFiglio.attivita) eq 0 && fn:length(wbsFiglio.wbsFiglie) eq 0}">
                                      <span class="right"><input type="submit" class="btn btn-danger" id="del-wbs" value="Elimina" /></span>
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
            <a class="btn btnNav" href="${project}">Chiudi</a>
          </span>
        </div>
        <div class="col-8 text-center">
          <%@ include file="subPanel.jspf" %>
        </div>
      </div>
    </div>
    <script type="text/javascript">
      $(document).ready(function() {
        $("#radioColumn, #nameColumn").click(function () {
          $('.selected').removeClass('selected');
          var trElement = $(this).parent();
          $(this).parent().addClass('selected');
          var tdElement = $("td", $(this).parent());
          $("input[name='wbs-select']", tdElement).attr("checked", true);
          var $radioValue = $("input[name='wbs-select']:checked").val();
          var $modWbsUrl = '<c:out value="${modWbs}${p.id}" escapeXml="false" />' + "&idw=" + $radioValue;
          var $delWbsName = "del-wbs" + $radioValue;
          $('#mod-wbs').attr('href', $modWbsUrl);
          $('#del-wbs').attr('name', $delWbsName);
          var $showActUrl = '<c:out value="${act}${p.id}" escapeXml="false" />' + "&idw=" + $radioValue;
          $('#show_act').attr('href', $showActUrl);
        });
      });
    </script>