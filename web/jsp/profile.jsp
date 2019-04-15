<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:if test="${not empty sessionScope.usr}">
    <hr class="separatore" />
    <span class="float-right">
      <a class="btn btnNav" href="${project}"><i class="fas fa-home"></i> Progetti</a>
    </span>
    <h3>Profilo di ${sessionScope.usr.nome} ${sessionScope.usr.cognome}</h3>
    <br />
    <div class="row">
      <div class="col-sm-7">
        <div class="row">
          <div class="col-sm-6"><label class="profileLabel">Cognome utente</label></div>
          <div class="col-sm-6"><p class="profileInfo"><c:out value="${sessionScope.usr.cognome}" /></p></div>
        </div>
        <div class="row">
          <div class="col-sm-6"><label class="profileLabel">Nome utente</label></div>
          <div class="col-sm-6"><p class="profileInfo"><c:out value="${sessionScope.usr.nome}" /></p></div>
        </div>
        <div class="row">
          <div class="col-sm-6"><label class="profileLabel">Data di nascita</label></div>
          <div class="col-sm-6"><p class="profileInfo"><fmt:formatDate value='${sessionScope.usr.dataNascita}' pattern='dd/MM/yyyy'/></p></div>
        </div>
        <br />
        <div class="row">
          <div class="col-sm-6 marginLeft">
            <a href="#form-changePwd" class="btn btn-success" id="btn-changePwd" name="btn-changePwd" rel="modal:open"><i class="fas fa-lock"></i> Cambia password</a>
          </div> 
        </div>
      </div>
    </div>
    <br />
  <c:if test="${not empty sessionScope.usr.ruoli}">
    <hr class="separatore" />
    <div class="actOfWbs">
      <div class="row">
        <div class="col"><strong>I tuoi progetti</strong></div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-4 actOfWbsHead">Titolo progetto</div>
        <div class="col-sm-3 actOfWbsHead">Dipartimento</div>
        <div class="col-sm-1 actOfWbsHead">Ruolo</div>
      </div>
      <hr class="separatore" />
      <c:forEach var="role" items="${projectsByRole}" varStatus="loop">
      <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-4 rowBottomLine">
          <c:out value="${role.informativa}"/>
        </div>
        <div class="col-sm-3 rowBottomLine">
          <c:out value="${role.nome}"/>
        </div>
        <div class="col-sm-1 rowBottomLine">
          <c:out value="${role.extraInfo}"/>
        </div>
      </div>
      <hr class="separatore" />
      </c:forEach>
    </div>
  </c:if>
</c:if>
    <div id="form-changePwd" class="modal">
      <form id="changePwd" action="#" method="post" onsubmit="return checkForm(this);">
        <div id="error" class="row"></div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-4"><label for="txt-pwd"><strong>Nuova password: </strong></label></div>
          <div class="col-sm-4"><input type="password" class="form-control" id="txtPwd" name="txtPwd" value="" placeholder="Inserisci la password" /></div>
        </div>
        <hr class="separatore" />
        <div class="row">
          <div class="col-sm-4"><label for="txt-confpwd"><strong>Conferma password: </strong></label></div>
          <div class="col-sm-4"><input type="password" class="form-control" id="txtConfPwd" name="txtConfPwd" value="" placeholder="Reinserisci la password" /></div>
        </div>
        <hr class="separatore" />
        <button type="submit" class="btn btn-success" id="btn-changePwd" name="btn-changePwd">
          <i class="far fa-save"></i>
          Salva
        </button>
      </form>
    </div>
    <script type="text/javascript">
      function checkForm(form) {
        if ($('#txtPwd').val().length < 1 || $('#txtConfPwd').val().length < 1 || $('#txtPwd').val() != $('#txtConfPwd').val()) {
          $('#errorMessage').remove();
          $('#error').addClass('errorPwd');
          $('#error').append('<div id="errorMessage"><i class="far fa-times-circle"></i> Le due password devono essere uguali e non devono essere vuote.</div>');
          return false;
        }
      }
    </script>
    