<%@ include file="pcURL.jspf" %>
<c:choose>
  <c:when test="${requestScope.checkThisOut}">
    <br />
    <h3>Elenco dei sottoprogetti di ${sessionScope.usr.nome} ${sessionScope.usr.cognome}</h3>
    <br />
    <c:catch var="exception">
    <c:forEach var="entry" items="${requestScope.progetti}">
      <c:if test="${not empty entry.value}">
      <div class="module">
        <c:set var="key" value="${entry.key}" scope="page" />
        <c:set var="d" value="${requestScope.dipart.get(key)}" />
        
<%--         <c:forEach var="a" items="${requestScope.dipart}"> --%>
<%--         ${a.value.id} --%>
<%--         </c:forEach> --%>
        <h4>Dipartimento di ${d.nome}</h4>
        <table class="table table-hover">
          <thead class="thead-light">
            <tr>
  <!--        <th scope="col">ID</th> -->
              <th scope="col">Titolo</th>
              <th scope="col">Stato</th>
              <th scope="col">Funzioni</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="prj" items="${entry.value}" varStatus="loop">
            <tr>
<%--          <td scope="row"><c:out value="${prj.id}" /></td> --%>
              <td scope="row"><a href="<c:out value="${progetto}${prj.id}" />"><c:out value="${prj.titolo}" /></a></td>
              <td scope="row"><c:out value="${prj.statoProgetto.nome}" escapeXml="false" /></td>
              <td width="40%" scope="row">
                <a href="<c:out value= "${vision}${prj.id}" />"  class="btn btn-primary">Project Charter</a>
                <a href="<c:out value= "${wbs}${prj.id}" />"  class="btn btn-primary">WBS</a>
                <a href="<c:out value= "${act}${prj.id}" />" class="btn btn-primary">Attivit&agrave;</a>
                <a href="<c:out value= "${lastStatus}${prj.id}" />"  class="btn btn-primary">Status</a>
              </td>
            </tr>
            </c:forEach>
            <c:if test="${not empty sessionScope.writableDeparments}">
            <tr>
              <td colspan="3" align="left"><a href="${mon}${d.id}"  class="btn btn-primary">Monitoraggio MIUR</a></td>
            </tr>
            </c:if>
          </tbody>
        </table>
      </div>
      </c:if>
    </c:forEach>
    </c:catch>
    <c:if test= "${not empty exception}">
      <div class= "alert alert-danger alert-dismissible" role= "alert">
        <button  type="button" class= "close fadeout" data-dismiss ="alert" aria-label="Close" >
          <span aria-hidden="true" >&times;</ span>
        </button>
        <strong> <fmt:message key="Attenzione" /></ strong><br />
        <em> <fmt:message key="ErroreDol" /></ em><hr />
        Dettagli: <br />
        <c:out value=" ${exception}" />
      </div >
    </c:if>
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