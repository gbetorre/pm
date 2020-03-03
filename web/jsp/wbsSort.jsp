<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <c:set var="ruoli" value="${sessionScope.usr.ruoli}" scope="page" />
    <c:set var="ruolo" value="" scope="page" />
    <c:forEach var="c" items="${ruoli}" varStatus="loop">
      <c:if test="${c.id eq p.id}">
        <c:set var="ruolo" value="${c.nome}" scope="page" />
      </c:if>
    </c:forEach>
    <c:set var="superuser" value="${false}" scope="page" />
    <c:if test="${(ruolo eq 'PMOATE') or (ruolo eq 'PMODIP') or (ruolo eq 'PM') or (ruolo eq 'TL')}">
      <c:set var="superuser" value="${true}" scope="page" />
    </c:if>
    <h4>WBS del sotto progetto <strong><c:out value="${p.titolo}" /></strong></h4>
    <hr class="separatore" />
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafico}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" id="show_act">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${timelines}${p.id}">Timelines workpackage</a></li>
    </ul>
    <c:choose>
      <c:when test="${not empty requestScope.wbsHierarchy}">
        <div class="tab-content responsive">
          <div role="tabpanel" class="tab-pane active" id="tab-wbs">
            <br />
            <table class="table table-bordered table-hover table-sm">
              <thead class="thead-light">
                <tr>
                  <th class="bg-warning" scope="col" width="5%">Ordinale</th>
                  <th class="bg-primary text-white" scope="col" width="*">Nome WBS padre</th>
                  <th class="bg-primary text-white" scope="col" width="5%"><div class="text-center">Workpackage</div></th>
                  <th class="bg-warning" scope="col" width="5%"><div class="text-center">Ordina</div></th>
                </tr>
              </thead>
              <tbody>
                <c:set var="duplicateHeaders" value="${false}" scope="page" />
                <c:set var="status" value="" scope="page" />
                <c:forEach var="wbsInstance" items="${requestScope.wbsHierarchy}" varStatus="loop">
                  <c:set var="status" value="${loop.index}" scope="page" />
                  <c:if test="${duplicateHeaders}">
                    <tr class="active">
                      <td class="bg-warning" width="5%"><strong>Ordinale</strong></td>
                      <td class="bg-primary text-white" width="*"><strong>Nome WBS padre</strong></td>
                      <td class="bg-primary text-white" width="5%"><div class="text-center"><strong>Workpackage</strong></div></td>
                      <td class="bg-warning" width="5%"><div class="text-center"><strong>Ordina</strong></div></td>
                    </tr>
                  </c:if>
                <form id="sort-form${wbsInstance.id}" method="post" action="${srtWbs}${p.id}&idw=${wbsInstance.id}">
                  <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsInstance.id}" />
                  <tr>
                    <td scope="col">
                      <input id="wbs-srt" type="text" class="form-control input-sm" size="2" name="wbs-srt" value="${wbsInstance.ordinale}">
                    </td>
                    <td scope="col" id="nameColumn">
                      <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbsInstance.id}"/>">
                      <a href="${modWbs}${p.id}&idw=${wbsInstance.id}">
                        <c:out value="${wbsInstance.nome}" />
                      </a>
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
                    <c:if test="${superuser}">
                     <div class="col-1">
                      <button type="submit" id="btnSave" class="btn btn-info" value="Salva">Imposta</button>
                     </div>
                    </c:if>
                    </td>
                  </tr>
                </form>
                  <c:if test="${not empty wbsInstance.wbsFiglie}"><!-- Wbs Figlie -->
                    <c:set var="duplicateHeaders" value="${true}" scope="page" />
                    <tr>
                      <td>&nbsp;</td>
                      <td colspan="3">
                        <table class="table table-condensed col-md-11 marginLeftSmall">
                          <thead class="thead-light">
                            <tr>
                              <th scope="col" width="60%">Nome WBS figlia</th>
                              <th width="5%"><div class="text-center">Workpackage</div></th>
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
                                  <div class="moduleThin m-3">
                                    <p class="bg-success badge-pill text-white"><strong>WBS figlie di ${wbsFiglio.nome}</strong></p>
                                    <ol class="list-unstyled m-2">
                                      <c:forEach var="wbsNipote" items="${wbsFiglio.wbsFiglie}">
                                        <li>
                                          <a href="${modWbs}${p.id}&idw=${wbsNipote.id}">
                                            <c:out value="${wbsNipote.nome}" />
                                          </a>
                                          <c:if test="${wbsNipote.workPackage}">
                                            <cite>(Workpackage)</cite>
                                          </c:if>
                                          <c:if test="${not empty wbsNipote.wbsFiglie}">
                                            <div class="moduleThin3 m-3">
                                              <p><strong>WBS figlie di ${wbsNipote.nome} (WBS di livello 4)</strong></p>
                                              <ol class="list-unstyled m-3">
                                                <c:forEach var="wbsPronipote" items="${wbsNipote.wbsFiglie}">
                                                  <li>
                                                  <a href="${modWbs}${p.id}&idw=${wbsPronipote.id}">
                                                    <c:out value="${wbsPronipote.nome}" />
                                                  </a>
                                                  <c:if test="${wbsPronipote.workPackage}">
                                                    <cite>(Workpackage)</cite>
                                                  </c:if>
                                                  <c:if test="${not empty wbsPronipote.wbsFiglie}">
                                                    <div class="moduleThin2 m-3">
                                                      <p class="bg-secondary badge-pill text-white"><strong>WBS figlie di ${wbsPronipote.nome} (WBS di livello 5)</strong></p>
                                                      <ul class="list-unstyled m-2">
                                                        <c:forEach var="wbsProPronipote" items="${wbsPronipote.wbsFiglie}">
                                                          <li>
                                                            <a href="${modWbs}${p.id}&idw=${wbsProPronipote.id}">
                                                              <c:out value="${wbsProPronipote.nome}" />
                                                            </a>
                                                            <c:if test="${wbsProPronipote.workPackage}">
                                                              <cite>(Workpackage)</cite>
                                                            </c:if>
                                                          </li>
                                                        </c:forEach>
                                                      </ul>
                                                    </div>
                                                  </c:if>
                                                </c:forEach>
                                              </ol>
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