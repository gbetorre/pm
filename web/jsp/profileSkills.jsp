<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:url var="setSkill" context="${initParam.appName}" value="/" scope="page">
  <c:param name="q" value="home" />
  <c:param name="p" value="ski" />
  <c:param name="idp" value="" />
</c:url>
<c:catch var="exception">
<c:if test="${not empty sessionScope.usr}">
  <c:choose>
    <c:when test="${requestScope.resetPwd}">
    <hr class="separatore" />
    <span class="float-right">
      <a class="btn btnNav" href="${project}"><i class="fas fa-home"></i> Progetti</a>
    </span>
    <h3>Profilo di ${requestScope.guest.nome} ${requestScope.guest.cognome}</h3>
    <br />
    <div class="row">
      <div class="col-sm-9">
        <div class="row">
          <div class="col-sm-4"><label class="profileLabel">Cognome utente</label></div>
          <div class="col-sm-4"><p class="profileInfo"><c:out value="${requestScope.guest.cognome}" /></p></div>
        </div>
        <div class="row">
          <div class="col-sm-4"><label class="profileLabel">Nome utente</label></div>
          <div class="col-sm-4"><p class="profileInfo"><c:out value="${requestScope.guest.nome}" /></p></div>
        </div>
        <div class="row">
          <div class="col-sm-4"><label class="profileLabel">Data di nascita</label></div>
          <div class="col-sm-4"><p class="profileInfo"><fmt:formatDate value='${requestScope.guest.dataNascita}' pattern='dd/MM/yyyy'/></p></div>
        </div>
      </div>
      <br />
    </div>
  <c:if test="${not empty requestScope.projects}">
    <hr class="separatore" />
    <div class="lightTable">
      <div class="row">
        <div class="col"><strong>Competenze nei progetti di ${requestScope.guest.nome} ${requestScope.guest.cognome}</strong></div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-4 lightTableHead avvisiTot">Titolo progetto <span class="badge badge-primary">N. competenze</span></div>
        <div class="col-sm-4 lightTableHead avvisiTot">Competenze del progetto</div>
      </div>
      <hr class="separatore" />
      <c:forEach var="proj" items="${requestScope.projects}" varStatus="loop">
      <c:set var="totCompetenze" value="${proj.competenze.size()}" scope="page" />
      <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-4 rowBottomLine">
        <c:choose>
          <c:when test="${totCompetenze gt 0}">
          <a href="${risorse}${proj.id}"><c:out value="${proj.titolo}"/></a>
          <span class="badge badge-primary"><c:out value="${totCompetenze}" /></span>
          </c:when>
          <c:otherwise>
          <a href="javascript:popupWindow('Note','popup1',true,'Su questo progetto manca il link perch&eacute; non sono ancora state definite competenze di progetto (conseguentemente, all\'utente <em>\'${requestScope.guest.nome} ${requestScope.guest.cognome}\'</em> non possono essere state assegnate).');" class="helpInfo" id="helpAct${role.id}">
            <c:out value="${proj.titolo}"/>
            <span class="badge badge-dark"><c:out value="${totCompetenze}" /></span>
          </a>
          </c:otherwise>
        </c:choose>
        </div>
        <div class="col-sm-4 rowBottomLine">
          <ul class="list-group small">
          <c:forEach var="skill" items="${proj.competenze}" varStatus="loop">
            <li class="list-group-item">
              <c:out value="${skill.nome} (${skill.informativa})" />&nbsp;
              <c:choose>
              <c:when test="${not empty skill.persone}"> 
              <span class="badge badge-success float-right">assegnata</span>
              </c:when>
              <c:otherwise>
              <form id="skill-form${skill.id}" method="post" action="${setSkill}${requestScope.guest.id}" class="float-right">
                <input type="hidden" id="prj-id" name="prj-id" value="${proj.id}" />
                <input type="hidden" id="skl-id" name="skl-id" value="${skill.id}" />
                <button type="submit" id="btnSave" class="btn btn-info" value="Salva">Assegna</button>
              </form>
              </c:otherwise>
              </c:choose>
            </li>
          </c:forEach>
          </ul>
          </div>
      </div>
      <hr class="separatore" />
      </c:forEach>
    </div>
    <br />
  </c:if>
    </c:when>
    <c:otherwise>
      <div class="alert alert-danger">
        <strong>Spiacente 
          <c:out value="${sessionScope.usr.nome}" />
          <c:out value="${sessionScope.usr.cognome}" />!<br />
        </strong>
        <p>Il tuo profilo non permette la gestione dei ruoli nei progetti del dipartimento.</p>
      </div>
    </c:otherwise>
  </c:choose>
</c:if>
</c:catch>
<c:out value="${exception}" />
    <%@ include file="subPopup.jspf" %>