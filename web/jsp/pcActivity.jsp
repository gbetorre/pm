<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ include file="pcURL.jspf" %>
<c:set var="actInstance" value="${requestScope.singolaAttivita}" scope="page" />
<c:catch var="exception">
<c:set var="checked" value="" scope="page" />
<c:set var="state" value="${actInstance.idStato}" scope="page" />
<c:if test="${not empty actInstance}">
  <c:if test="${actInstance.milestone}">
    <c:set var="checked" value="checked" scope="page" />
  </c:if>
  <c:set var="wbs" value="${actInstance.idWbs}" scope="page" />
  <c:set var="complexityIdAsIndex" value="${actInstance.idComplessita - 1}" scope="page" />
  <c:set var="complexityValues" value="ALTA,MEDIA,BASSA" scope="page" /> 
</c:if>
    <form id="delAct_form" action="" method="post" class="panel">
      <input type="hidden" id="act-id" name="act-id" value="${actInstance.id}" />
      <c:set var="stato" value="" scope="page" />
      <c:forEach var="status" items="${requestScope.statiAttivita}" varStatus="loop">
        <c:if test="${status.id eq state}">
          <c:set var="stato" value="${status.nome}" scope="page" />
        </c:if> 
      </c:forEach>
      <div class="panel-heading bgAddAct bgAct${state}">
        <div class="noHeader"><em>Riepilogo attivit&agrave; <strong>${actInstance.nome}</strong></em></div>
        <div class="actstate">
        <c:out value="Stato: ${stato}" />
        </div>
      </div> 
      <hr class="separatore" />
      <div class="panel-body">
        <div class="row">
          <div class="col-sm-5">
            <strong>Persona responsabile </strong>
          </div>
          <div class="col-sm-5">
          <c:set var="skills" value="" scope="page" />
          <c:forEach var="person" items="${actInstance.persone}" begin="0" end="0">
            <c:out value="${person.nome} ${person.cognome}" />
            <c:set var="skills" value="${person.competenze}" scope="page" />
          </c:forEach>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Identificativo della competenza</strong>
          </div>
          <div class="col-sm-5">
            <c:forEach var="skill" items="${skills}" begin="0" end="0">
              <c:out value="${skill.nome}" />
            </c:forEach>
          </div>
        </div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-5">
            <strong>Nome attivit&agrave;</strong>
          </div>
          <div class="col-sm-5">
            <c:out value="${actInstance.nome}" />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Descrizione dell'attivit&agrave;</strong>
          </div>
          <div class="col-sm-5">
            <c:out value="${actInstance.descrizione}" escapeXml="false" />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Data di inizio dell'attivit&agrave;</strong>
          </div>
          <div class="col-sm-5">
            <fmt:formatDate value="${actInstance.dataInizio}" pattern="dd/MM/yyyy" />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Data di fine dell'attivit&agrave;</strong>
          </div>
          <div class="col-sm-5">
            <fmt:formatDate value="${actInstance.dataFine}" pattern="dd/MM/yyyy" />
          </div>
        </div>
        <br />
      <c:if test="${not empty actInstance.dataInizioEffettiva}">
        <div class="row">
          <div class="col-sm-5">
            <strong>Data di inizio effettiva</strong>
          </div>
          <div class="col-sm-5">
            <fmt:formatDate value='${actInstance.dataInizioEffettiva}' pattern='dd/MM/yyyy' />
          </div>
        </div>
        <br />
      </c:if>
      <c:if test="${not empty actInstance.dataFineEffettiva}">
        <div class="row">
          <div class="col-sm-5">
            <strong>Data di fine effettiva</strong>
          </div>
          <div class="col-sm-5">
            <fmt:formatDate value='${actInstance.dataFineEffettiva}' pattern='dd/MM/yyyy' />
          </div>
        </div>
        <br />
      </c:if>
      <c:if test="${not empty actInstance.noteAvanzamento}">
        <div class="row">
          <div class="col-sm-5"><strong>Note di avanzamento</strong></div>
          <div class="col-sm-5">
            <c:out value="${actInstance.noteAvanzamento}" escapeXml="false" />
          </div>
        </div>
        <br />
      </c:if>
      <c:if test="${not empty actInstance.risultatiRaggiunti}">
        <div class="row">
          <div class="col-sm-5"><strong>Risultati raggiunti</strong></div>
          <div class="col-sm-5">
            <c:out value="${actInstance.risultatiRaggiunti}" escapeXml="false" />
            <div class="charNum"></div>
          </div>
        </div>
        <br />
      </c:if>
        <div class="row">
          <div class="col-sm-5"><strong>Milestone</strong></div>
          <div class="col-sm-5" style="margin-left:25px;">
            <input type="checkbox" class="form-check-input" id="act-milestone" name="act-milestone" <c:out value='${checked}' /> readonly disabled>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5"><strong>WBS</strong></div>
          <div class="col-sm-5">
          <ol>
            <li>
              <a href="${modWbs}${p.id}&idw=${requestScope.w.id}">
                <c:out value="${requestScope.w.nome}" />
              </a>
            </li>
          <c:if test="${not empty requestScope.w.wbsFiglie}">
            <ol start="2">
            <c:forEach var="wbs1" items="${requestScope.w.wbsFiglie}" begin="0" end="0">
              <li>
                <a href="${modWbs}${p.id}&idw=${wbs1.id}">
                  <c:out value="${wbs1.nome}" />
                </a>
              </li>
              <c:if test="${not empty wbs1.wbsFiglie}">
                <ol start="3">
                  <c:forEach var="wbs2" items="${wbs1.wbsFiglie}" begin="0" end="0">
                  <li>
                    <c:out value="${wbs2.nome}" />
                  </li>
                    <c:if test="${not empty wbs2.wbsFiglie}">
                    <ol start="4">
                      <c:forEach var="wbs3" items="${wbs2.wbsFiglie}" begin="0" end="0">
                      <li>
                        <c:out value="${wbs3.nome}" />
                      </li>
                      <c:if test="${not empty wbs3.wbsFiglie}">
                      <ol start="5">
                        <c:forEach var="wbs4" items="${wbs3.wbsFiglie}" begin="0" end="0">
                        <li>
                          <c:out value="${wbs4.nome}" />
                        </li>
                        </c:forEach>
                      </ol>
                      </c:if>
                      </c:forEach>
                    </ol>
                    </c:if>
                  </c:forEach>
                </ol>
              </c:if>
            </c:forEach>
            </ol>
          </c:if>
          </ol>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Livello di complessit&agrave;</strong>
          </div>
          <div class="col-sm-5">
          <c:forTokens var="complessita" items="${complexityValues}" delims="," begin="${complexityIdAsIndex}" end="${complexityIdAsIndex}">
            <c:out value="${complessita}" />
          </c:forTokens>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Stato attivit&agrave;</strong>
          </div>
          <div class="col-sm-5">
            <c:out value="${actInstance.stato.nome}" escapeXml="false" />
          </div>
        </div>
        <hr class="separatore" />
        <a href="${act}${requestScope.progetto.id}" id='btn-close' class="btn btnNav"><i class="far fa-window-close"></i> Chiudi</a>
        <%@ include file="subPanel.jspf" %>
      </div>
    </form>
</c:catch>
<c:out value="${exception}" />