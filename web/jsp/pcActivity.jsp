<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ include file="pcURL.jspf" %>
<c:set var="actInstance" value="${requestScope.singolaAttivita}" scope="page" />
<c:catch var="exception">
<c:set var="checked" value="" scope="page" />
<c:if test="${not empty actInstance}">
  <c:if test="${actInstance.milestone}">
    <c:set var="checked" value="checked" scope="page" />
  </c:if>
  <c:set var="wbs" value="${actInstance.idWbs}" scope="page" />
  <c:set var="complexity" value="${actInstance.idComplessita}" scope="page" />
  <c:set var="state" value="${actInstance.idStato}" scope="page" />    
</c:if>

    <h3>Riepilogo attivit&agrave; &quot;${actInstance.nome}&quot;:</h3>
    <br />
    <form id="delAct_form" action="" method="post" class="subfields">
      <input type="hidden" id="act-id" name="act-id" value="${actInstance.id}" />
      <div class="row">
        <div class="col-sm-5">
          <strong>Persona incaricata </strong>
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
          <strong>Identificativo del ruolo ricoperto nell'attivit&agrave;</strong>
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
        <div class="row">
          <div class="col-sm-5">
            <strong>Data di inizio effettiva</strong>
          </div>
          <div class="col-sm-5">
            <fmt:formatDate value='${actInstance.dataInizioEffettiva}' pattern='dd/MM/yyyy' />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Data di fine effettiva</strong>
          </div>
          <div class="col-sm-5">
            <fmt:formatDate value='${actInstance.dataFineEffettiva}' pattern='dd/MM/yyyy' />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5"><strong>Note di avanzamento</strong></div>
          <div class="col-sm-5">
            <c:out value="${actInstance.noteAvanzamento}" escapeXml="false" />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5"><strong>Risultati raggiunti</strong></div>
          <div class="col-sm-5">
            <c:out value="${actInstance.risultatiRaggiunti}" escapeXml="false" />
            <div class="charNum"></div>
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5"><strong>Milestone</strong></div>
          <div class="col-sm-5" style="margin-left:25px;">
            <input type="checkbox" class="form-check-input" id="act-milestone" name="act-milestone" <c:out value='${checked}' /> readonly disabled>
          </div>
        </div>
        <br />
        <%--
        <div class="row">
          <div class="col-sm-5">WBS</div>
          <div class="col-sm-5">

            <c:out value="${requestScope.w.nome}" />
            <c:if test="${not empty requestScope.w.wbsFiglie}">
              <c:forEach var="wbs1" items="${requestScope.w.wbsFiglie}" begin="0" end="0">
                <c:out value="${wbs1.nome}" />
                <c:if test="${not empty wbs1.wbsFiglie}">
                  <c:forEach var="wbs2" items="${wbs1.wbsFiglie}" begin="0" end="0">
                    <c:out value="${wbs2.nome}" />
                    <c:if test="${not empty wbs2.wbsFiglie}">
                      <c:forEach var="wbs3" items="${wbs2.wbsFiglie}" begin="0" end="0">
                        <c:out value="${wbs3.nome}" />
                      </c:forEach>
                    </c:if>
                  </c:forEach>
                </c:if>
              </c:forEach>
            </c:if>
          </div>
        </div>
        <br />
        --%>
        <div class="row">
          <div class="col-sm-5">
            <strong>Livello di complessit&agrave;</strong></div>
          <div class="col-sm-5">
            <c:out value="${actInstance.stato.informativa}" />
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-5">
            <strong>Stato attivit&agrave;</strong>
          </div>
          <div class="col-sm-5">
            <c:out value="${actInstance.stato.nome}" />
          </div>
        </div>
        <br />
        <a href="${act}${requestScope.progetto.id}" id='btn-close' class="btn btn-primary">Chiudi</a>
        <input type="button" class="btn btn-primary" name="elimina" value="Elimina" onclick="alert('funzionalit&agrave; in corso di sviluppo')"> <!-- onclick="selectionDelete()" -->
    </form>
</c:catch>
<c:out value="${exception}" />