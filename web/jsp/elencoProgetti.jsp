<%@ include file="pcURL.jspf" %>
<c:choose>
  <c:when test="${requestScope.progetti.size() gt 0}">
    <form action="#" method="get">
      <h1>Elenco progetti</h1>
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
              <th scope="row"><a href="<c:out value="${status}${prj.id}" />"><c:out value="${prj.titolo}" /></a></th>
              <th scope="row"><c:out value="${prj.statoProgetto.nome}" /></th>
              <th scope="row">
                <div class="container">
                  <a href="<c:out value= "${vision}${prj.id}" />"  class="btn btn-primary">Project Charter</a>
                  <!-- <a href=""  class="btn btn-primary">Status</a> -->
                  <a href="#"  class="btn btn-primary">WBS</a>
                  <a href="#"  class="btn btn-primary">Attivit&agrave;</a>
                </div>
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
      <strong>
        Spiacente: 
        <c:out value="${sessionScope.usr.nome}" />
        <c:out value="${sessionScope.usr.cognome}" /><br />
        <span style="background:#ffc">Non sono stati trovati progetti a te associati.</span>
      </strong>
  </c:otherwise>
</c:choose>