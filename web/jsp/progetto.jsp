<%@ include file="pcURL.jspf" %><c:choose>  <c:when test="${not empty lps}">    <h2 align="center">      <c:out value="${p.titolo}" />    </h2>    <hr class="separatore" />    <div class="form-row">      <div class="col col-centered">      <%@ include file="subStatus.jspf" %>      </div>    </div>  </c:when>  <c:otherwise>    <div class="alert alert-danger">      <strong>Spiacente!</strong>       <p>        Non &egrave; stato trovato alcun status progetto a te associato con questo identificativo.        Avendone i diritti &egrave; possibile <a href="${lastStatus}${prj.id}">aggiungerne uno</a>.      </p>    </div>     </c:otherwise></c:choose><a class="btn btn-primary" href="${project}">Chiudi</a>