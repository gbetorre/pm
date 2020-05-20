<%@ include file="pcURL.jspf" %>
<c:choose>
  <c:when test="${requestScope.checkThisOut}">
    <br />
    <h3>Elenco dei sottoprogetti di ${sessionScope.usr.nome} ${sessionScope.usr.cognome}</h3>
    <c:set var="deptsWithPrj" value="${0}" scope="page" />
    <c:forEach var="check" items="${requestScope.progetti.values()}">
      <c:if test="${not check.isEmpty()}">
        <c:set var="deptsWithPrj" value="${deptsWithPrj + 1}" scope="page" />
      </c:if>
    </c:forEach>
  <c:catch var="exception">
  <c:choose>
    <c:when test="${deptsWithPrj > 4}">
      <ul class="menu-nav">
      <c:forEach var="entry" items="${requestScope.progetti}">
        <c:if test="${not empty entry.value}">
          <c:set var="key" value="${entry.key}" scope="page" />
          <c:set var="d" value="${requestScope.dipart.get(key)}" />
          <li><a class="smooth" href="#${d.acronimo}"><c:out value="${d.acronimo}" /></a></li>
        </c:if>
      </c:forEach>
      </ul>
    </c:when>
    <c:otherwise>
      <br />
    </c:otherwise>
  </c:choose>
    <c:forEach var="entry" items="${requestScope.progetti}">
      <c:if test="${not empty entry.value}">
      <div class="module">
        <c:set var="key" value="${entry.key}" scope="page" />
        <c:set var="d" value="${requestScope.dipart.get(key)}" />
        <section id="${d.acronimo}">
          <h4>${d.prefisso} ${d.nome}</h4>
        </section>
        <table class="table table-hover">
          <thead class="thead-light">
            <tr>
              <th width="50%" scope="col">Titolo</th>
              <!-- <th width="5%" scope="col">Stato</th> -->
              <th width="45%" scope="col">Funzioni</th>
              <th width="5%"scope="col">&nbsp;</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="prj" items="${entry.value}" varStatus="loop">
            <tr>
              <td scope="row"><a href="<c:out value="${progetto}${prj.id}" />"><c:out value="${prj.titolo}" /></a></td>
              <!-- <td scope="row"><c:out value="${prj.statoProgetto.nome}" escapeXml="false" /></td> -->
              <td width="45%" scope="row">
                <a href="<c:out value= "${vision}${prj.id}" />" class="btn btn-success btn-spacer"><i class="fas fa-file-invoice"></i> Project Charter</a>
                <a href="<c:out value= "${lastStatus}${prj.id}" />" class="btn btn-success btn-spacer"><i class="far fa-clock"></i> Status</a>            
                <a href="<c:out value= "${urlWbs}${prj.id}" />" class="btn btn-success btn-spacer"><i class="fas fa-sitemap"></i> WBS</a>
                <a href="<c:out value= "${act}${prj.id}" />" class="btn btn-success btn-spacer"><i class="fas fa-bars"></i> Attivit&agrave;</a>
                <a href="<c:out value= "${report}${prj.id}" />" class="btn btn-success btn-spacer"><i class="fas fa-chart-line"></i> Report</a>
              </td>
              <td><cite><c:out value="${prj.tag}" /></cite></td>
            </tr>
            </c:forEach>
            <c:if test="${not empty sessionScope.writableDeparments}">
            <tr>
              <td colspan="3" align="left">
                <a href="${mon}${d.id}"  class="btn btn-success btn-spacer"><i class="fas fa-tv"></i> Monitoraggio MIUR</a>
                &nbsp;
                <a href="${monAte}" class="btn btn-success btn-spacer"><i class="fas fa-desktop"></i> Monitoraggio Ateneo</a>
              </td>
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
        <em> <fmt:message key="ErroreDol" /></em><hr />
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