<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
      <c:set var="wbsInstance" value="${requestScope.wbsInstance}" scope="page" />
      <jsp:useBean id="wbsPadre" class="it.alma.bean.WbsBean" scope="page" />
      <c:set target="${pageScope.wbsPadre}" property="id" value="-3" />
      <c:set var="wbsPadre" value="${wbsPadre}" scope="page" />
      <c:catch var="exception">
      <c:set var="wbsNome" value="" scope="page" />
      <c:set var="wbsDescr" value="" scope="page" />
      <c:set var="isWorkpackage" value="" scope="page" />
      <c:set var="distinguishingSubmitId" value="${wbs}${p.id}" scope="page" />
      <form id="newWbs_form" action="#" method="post" class="module2">
        <hr class="separatore" />
        <c:choose>
          <c:when test="${not empty wbsInstance}" >
            <h3>Modifica la WBS ${wbsInstance.nome}:</h3>
            <c:set var="wbsPadre" value="${wbsInstance.wbsPadre}" scope="page" />
            <c:set var="wbsId" value="${wbsInstance.id}" scope="page" />
            <c:set var="wbsNome" value="${wbsInstance.nome}" scope="page" />
            <c:set var="wbsDescr" value="${wbsInstance.descrizione}" scope="page" />
            <c:set var="isWorkpackage" value="${wbsInstance.workPackage}" scope="page" />
            <c:set var="wbsNote" value="${wbsInstance.noteAvanzamento}" scope="page" />
            <c:set var="wbsRisultati" value="${wbsInstance.risultatiRaggiunti}" scope="page" />
          </c:when>
        <c:otherwise>
          <h3>Inserisci una nuova WBS:</h3>
        </c:otherwise>
      </c:choose>
      <br />
      <input type="hidden" id="wbs-id" name="wbs-id" value="${wbsId}" />
      <div class="row">
        <div class="col-sm-5">
          WBS padre (se &eacute; presente): 
          <sup>&#10039;</sup>
        </div>
        <div class="col-sm-5">
          <select class="form-control" id="wbs-idpadre" name="wbs-idpadre">
          <c:if test="${wbsPadre.id ne -3}">
            <option value="${wbsPadre.id}">${wbsPadre.nome}</option>
          </c:if>
          <option value="">Nessuna wbs padre</option>
          <c:forEach var="singleWbs" items="${requestScope.wbs}" varStatus="status">
            <option value="${singleWbs.id}">${singleWbs.nome}</option>
          </c:forEach>
          </select>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Nome WBS</div>
        <div class="col-sm-5">
          <input type="text" class="form-control" id="wbs-name" name="wbs-name" value="${wbsNome}">
          <div class="charNum"></div> 
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Descrizione della WBS</div>
        <div class="col-sm-5">
          <textarea class="form-control" name="wbs-descr">${wbsDescr}</textarea>
          <div class="charNum"></div>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Note di avanzamento della WBS</div>
        <div class="col-sm-5">
          <textarea class="form-control" name="wbs-note">${wbsNote}</textarea>
          <div class="charNum"></div>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Risultati raggiunti</div>
        <div class="col-sm-5">
          <textarea class="form-control" name="wbs-result">${wbsRisultati}</textarea>
          <div class="charNum"></div>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Workpackage</div>
        <div class="col-sm-5">
          <c:choose>
            <c:when test="${isWorkpackage}">
              <div class="form-check">
                <input type="checkbox" class="form-check-input" id="wbs-workpackage" name="wbs-workpackage" checked>
              </div>
            </c:when>
            <c:otherwise>
              <div class="form-check">
                <input type="checkbox" class="form-check-input" id="wbs-workpackage" name="wbs-workpackage">
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>
      <hr class="separatore" />
      <a href="${wbs}${p.id}" class="btn btn-primary">Chiudi</a>
      <%@ include file="subPanel.jspf" %>
      </form>
      </c:catch>
      <c:out value="${exception}" />
      <script type="text/javascript">
        var offsetcharacter = 5;
        $(document).ready(function () {
          $('#newWbs_form').validate ({
            rules: {
              'wbs-name': {
                required: true,
                minlength: offsetcharacter
              },
              'wbs-descr': {
                required: true,
                minlength: offsetcharacter
              },
            }, 
            messages: {
              'wbs-name': "Inserire il nome della WBS",
              'wbs-descr': "Inserire una descrizione per la WBS"
            },
            submitHandler: function (form) {
              return true;
            }
          });
        
          $('textarea').keyup(function (e) {
            var len = $(this).val().length;
            var dblength = 8104;
            if(len >= dblength) {
              this.value = this.value.substring(0, dblength);
              $(this).next('div').text(' è stato raggiunto il limite di caratteri');
            } else {
              var chars = dblength - len;
              $(this).next('div').text(chars + ' caratteri rimanenti');
            }
          });
          
          $('#wbs-name').keyup(function (e) {
            var len = $(this).val().length;
            var dblength = 1024;
            if(len >= dblength) {
              this.value = this.value.substring(0, dblength);
              $(this).next('div').text(' è stato raggiunto il limite di caratteri');
            } else {
              var chars = dblength - len;
              $(this).next('div').text(chars + ' caratteri rimanenti');
            }
          });
        });
        
        /*$("form").on("submit", function() {
          window.location = '${wbs}${p.id}';
        });*/
      </script>
    <c:choose>
      <c:when test="${not empty requestScope.attivitaWbs}">
      <div class="subfields">
      <h4>Attivit&agrave; di questa WBS</h4>
      <table class="table table-bordered table-hover ">
        <thead class="thead-light">
        <tr>
          <th scope="col">Nome</th>
          <th scope="col">Descrizione</th>
          <th scope="col"><div class="text-center">Milestone</div></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="status" value="" scope="page" />
        <c:forEach var="act" items="${requestScope.attivitaWbs}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <tr>
            <td scope="row" id="nameColumn">
              <a href="${modAct}${p.id}&ida=${act.id}">
                <c:out value="${act.nome}"/>
              </a>
            </td>
            <td scope="row">
              <c:out value="${act.descrizione}"/>
            </td>
            <td scope="row">
              <c:choose>
                <c:when test="${act.milestone}">
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
      </c:when>
      <c:otherwise>
      <div class="alert alert-danger">
        <p>Non &egrave; stata trovata alcuna attivit&agrave; associata alla WBS &quot;${wbsNome}&quot;.</p>
      </div>
      </c:otherwise>
    </c:choose>