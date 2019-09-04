<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:set var="allRoles" value="PMO,PM,TL,USER,Nessuno" scope="page" />
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
  <c:if test="${not empty requestScope.guest.ruoli}">
    <hr class="separatore" />
    <form id="grant_form" name="per" action="" method="post">
    <div class="lightTable">
      <div class="row">
        <div class="col"><strong>Progetti di ${requestScope.guest.nome} ${requestScope.guest.cognome}</strong></div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-4 lightTableHead avvisiTot">Titolo progetto</div>
        <div class="col-sm-3 lightTableHead avvisiTot">Dipartimento</div>
        <div class="col-sm-2 lightTableHead avvisiTot">Ruolo</div>
      </div>
      <hr class="separatore" />
      <c:forEach var="role" items="${requestScope.projectsByRole}" varStatus="loop">
      <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-4 rowBottomLine">
        <c:set var="makelink" value="${false}" scope="page" />
        <c:forEach var="r" items="${sessionScope.usr.ruoli}">
          <c:if test="${r.id eq role.id}">
            <c:set var="makelink" value="${true}" scope="page" />
          </c:if>
        </c:forEach>
        <c:choose>
          <c:when test="${makelink}">
          <a href="${progetto}${role.id}"><c:out value="${role.informativa}"/></a>
          </c:when>
          <c:otherwise>
          <a href="javascript:popupWindow('Note','popup1',true,'Su questo progetto manca il link perch&eacute; l\'utente <em>\'${sessionScope.usr.nome} ${sessionScope.usr.cognome}\'</em> non &egrave; ancora stato autorizzato a gestirlo.<br /> Eventualmente, richiedere l\'abilitazione al PMO di Ateneo.');" class="helpInfo" id="helpAct${role.id}">
            <c:out value="${role.informativa}"/>
          </a>
          </c:otherwise>
        </c:choose>
        </div>
        <div class="col-sm-3 rowBottomLine">
          <c:out value="${role.nome}"/>
        </div>
        <div class="col-sm-2 rowBottomLine">
          <select class="form-control" id="${role.id}-rol" name="${role.id}-rol">
          <c:forTokens var="roleEnum" items="${allRoles}" delims=",">
          <c:choose>
            <c:when test="${roleEnum eq role.extraInfo}">
              <c:set var="selected" value="selected" scope="page" />
            </c:when>
            <c:when test="${roleEnum eq 'Nessuno' and role.extraInfo eq 'NONE'}">
              <c:set var="selected" value="selected" scope="page" />
            </c:when>
            <c:otherwise>
              <c:set var="selected" value="" scope="page" />
            </c:otherwise>
          </c:choose>
            <option value="${role.id}-${roleEnum}" ${selected}><c:out value="${roleEnum}"/></option>
          </c:forTokens>
          </select>
        </div>
      </div>
      <hr class="separatore" />
      </c:forEach>
    </div>
    <br />
    <div class="row">
      <div class="col text-center">
        <button type="submit" class="btn btn-success" value="Salva">
          <i class="far fa-save"></i>
          Salva
        </button>
      </div>
    </div>
    </form>
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
    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript">
      $(document).ready(function(){
        $("select").change(function(){
            $opt = $(this).children("option:selected").text();
            if ($opt == "Nessuno") {
              alert("Attenzione, scegliendo il valore \'Nessuno\' intendi scollegare un utente da un progetto.\n" + 
                    "Questa operazione non può essere annullata.\n" + 
                    "Se in futuro desiderassi ripristinare l\'associazione tra l\'utente e il progetto, dovrai chiedere il ripristino al PMO di Ateneo.\n" +
                    "Tieni conto, inoltre, che puoi staccare un utente da non piu\' di due progetti.\n" +
                    "Le modifiche, comunque, non sono ancora state effettuate e verranno rese definitive dopo aver cliccato sul bottone \"SALVA\"");
            }
        });
      });
    </script>