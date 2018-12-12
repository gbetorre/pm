<%@ include file="pcURL.jspf" %>
<h4>Attivit&agrave; del progetto <c:out value="${p.titolo}" /></h4>
<form action="#" method="post">
<c:choose>
  <c:when test="${not empty requestScope.attivita}">
  <table class="table table-bordered table-hover">
    <thead class="thead-light">
    <tr>
      <th scope="col">Nome</th>
      <th scope="col">Descrizione</th>
      <th scope="col"><div class="text-center">Milestone</div></th>
    </tr>
    </thead>
    <tbody>
    <c:set var="status" value="" scope="page" />
    <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
      <c:set var="status" value="${loop.index}" scope="page" />
      <input type="hidden" id="act-id${status}" name="act-id${status}" value="<c:out value="${act.id}"/>">
      <tr>
        <td scope="row">
          <input type="text" class="form-control" id="act-nome${status}" name="act-nome${status}" value="<c:out value="${act.nome}"/>" readonly>
        </td>
        <td scope="row">
          <input type="text" class="form-control" id="act-descrizione${status}" name="act-descrizione${status}" value="<c:out value="${act.descrizione}"/>" readonly>
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
  </c:when>
  <c:otherwise>
  <div class="alert alert-danger">
    <p>Non &egrave; stata trovata alcuna attivit&agrave; associata al progetto.</p>
  </div>
  </c:otherwise>
</c:choose>
  <div id="container-fluid">
    <div class="row">
      <div class="col-2">
        <span class="float-left">
          <a class="btn btn-primary" href="${project}${p.id}">Chiudi</a>
        </span>
      </div>
      <div class="col-8 text-center">
        <%@ include file="subPanel.jspf" %>
      </div>
    </div>
  </div>
</form>