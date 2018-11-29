<%@ include file="pcURL.jspf" %>
<c:choose>
  <c:when test="${requestScope.progetti.size() gt 0}">
    <form action="#" method="get">
      <br />
      <h3>Elenco dei progetti di ${sessionScope.usr.nome} ${sessionScope.usr.cognome}</h3>
      <br />
      <table class="table table-hover">
        <thead class="thead-light">
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Titolo</th>
            <th scope="col">Stato</th>
            <th scope="col">Funzioni</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="prj" items="${requestScope.progetti}" varStatus="loop">
            <tr>
              <th scope="row"><c:out value="${prj.id}" /></th>
              <th scope="row"><a href="<c:out value="${progetto}${prj.id}" />"><c:out value="${prj.titolo}" /></a></th>
              <th scope="row"><c:out value="${prj.statoProgetto.nome}" /></th>
              <th scope="row">
                <a href="<c:out value= "${vision}${prj.id}" />"  class="btn btn-primary">Project Charter</a>
                <a href="<c:out value= "${lastStatus}${prj.id}" />"  class="btn btn-primary">Status</a>
                <a href="<c:out value= "${wbs}${prj.id}" />"  class="btn btn-primary">WBS</a>
                <a href="<c:out value= "${act}${prj.id}" />" class="btn btn-primary">Attivit&agrave;</a>
              </th>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      <!-- <input type="submit" name="visualizza" value="Visualizza">
      <input type="reset" name="annulla" value="Annulla"> -->
    </form>
  </c:when>
  <c:otherwise>
      <div class="alert alert-danger">
        <strong>Spiacente 
          <c:out value="${sessionScope.usr.nome}" />
          <c:out value="${sessionScope.usr.cognome}" />!<br />
        </strong>
        <p>Non sono stati trovati progetti a te associati.</p>
      </div>
  </c:otherwise>
</c:choose>