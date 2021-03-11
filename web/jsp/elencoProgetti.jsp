<%@ include file="pcURL.jspf" %>
<c:choose>
  <c:when test="${requestScope.checkThisOut}">
    <c:set var="deptsWithPrj" value="${0}" scope="page" />
    <c:set var="totPrj" value="${0}" scope="page" />
    <c:set var="totPPPrj" value="${0}" scope="page" />
    <c:forEach var="check" items="${requestScope.progetti.values()}">
      <c:if test="${not check.isEmpty()}">
        <c:set var="deptsWithPrj" value="${deptsWithPrj + 1}" scope="page" />
      </c:if>
    </c:forEach>
    <c:catch var="exception">
    <c:choose>
      <c:when test="${deptsWithPrj > 4}">
        <ul class="nav nav-pills">
        <c:forEach var="entry" items="${requestScope.progetti}">
          <c:if test="${not empty entry.value}">
            <c:set var="key" value="${entry.key}" scope="page" />
            <c:set var="d" value="${requestScope.dipart.get(key)}" />
            <li class="nav-item small"><a class="smooth nav-link" href="#${d.acronimo}" title="${d.nome}"><c:out value="${d.acronimo}" /></a></li>
          </c:if>
        </c:forEach>
        </ul>
        <hr class="riga" />
      </c:when>
    </c:choose>
    <div class="row">
        <div class="col-10">
          <h3>Elenco degli obiettivi di ${sessionScope.usr.nome} ${sessionScope.usr.cognome}</h3>
        </div>
        <div class="col-2 form-row">
        <c:set var="active20" value="" scope="page" />
        <c:set var="active21" value="" scope="page" />
        <c:set var="active22" value="" scope="page" />
        <c:set var="active23" value="" scope="page" />
        <c:set var="active24" value="" scope="page" />
        <c:choose>
          <c:when test="${param['y'] eq 2020}">
            <c:set var="active20" value="selected" scope="page" />
          </c:when>
          <c:when test="${param['y'] eq 2021}">
            <c:set var="active21" value="selected" scope="page" />
          </c:when>
          <c:when test="${param['y'] eq 2022}">
            <c:set var="active22" value="selected" scope="page" />
          </c:when>
          <c:when test="${param['y'] eq 2023}">
            <c:set var="active23" value="selected" scope="page" />
          </c:when>
          <c:when test="${param['y'] eq 2024}">
            <c:set var="active24" value="selected" scope="page" />
          </c:when>
          <c:otherwise>
            <c:set var="active20" value="selected" scope="page" />
          </c:otherwise>
        </c:choose>
          <select id="myPlans" onchange="viewPlan()">
            <option value="2020" ${active20}>2020</option>
            <option value="2021" ${active21}>2021</option>
            <option value="2022" ${active22}>2022</option>
            <option value="2023" ${active23}>2023</option>
            <option value="2024" ${active24}>2024</option>
          </select>
        <%-- 
          <select id="myPlans" onchange="viewPlan()">
            <option value="2020">2020-2022</option>
            <option value="2021" ${active21}>2021-2023</option>
            <option value="2022" ${active22}>2022-2024</option>
            <option value="2023" ${active23}>2023-2025</option>
            <option value="2024" ${active24}>2024-2026</option>
          </select>
        --%>
      </div>
    </div>
    <c:forEach var="entry" items="${requestScope.progetti}">
      <c:if test="${not empty entry.value}">
      <div class="module">
        <c:set var="key" value="${entry.key}" scope="page" />
        <c:set var="d" value="${requestScope.dipart.get(key)}" />
        <section id="${d.acronimo}">
          <h4>${d.prefisso} ${d.nome}</h4>
          <div class="avvisiTot text-right">
          <c:set var="totObj" value="" scope="page" />
          <c:choose>
            <c:when test="${entry.value.size() eq 1}">
              <c:set var="totObj" value="1 obiettivo" scope="page" />
            </c:when>
            <c:when test="${entry.value.size() gt 1}">
              <c:set var="totObj" value="${entry.value.size()} obiettivi" scope="page" />
            </c:when>
          </c:choose>
            <c:out value="${totObj}" />
          </div>
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
            <c:set var="btnColor" value="btn-success" scope="page" />
            <c:if test="${prj.descrizioneStatoCorrente eq 5}">
              <c:set var="btnColor" value="btn-dark" scope="page" />
            </c:if>
            <tr>
              <td scope="row"><a href="<c:out value="${progetto}${prj.id}" />"><c:out value="${prj.titolo}" escapeXml="false" /></a></td>
              <!-- <td scope="row"><c:out value="${prj.statoProgetto.nome}" escapeXml="false" /></td> -->
              <td width="45%" scope="row">
                <a href="<c:out value= "${vision}${prj.id}" />" class="btn ${btnColor} btn-spacer"><i class="fas fa-file-invoice"></i> Project Charter</a>
                <a href="<c:out value= "${lastStatus}${prj.id}" />" class="btn ${btnColor} btn-spacer"><i class="far fa-clock"></i> Status</a>
                <c:set var="wbsURI" value="${urlWbs}${prj.id}" scope="page" />
                <c:if test="${(prj.tipo eq 'P') and (not empty param['y'])}">
                  <c:set var="wbsURI" value="${urlWbs}${prj.id}&y=${param['y']}" scope="page" />
                </c:if>
                <a href="<c:out value= "${wbsURI}" />" class="btn ${btnColor} btn-spacer"><i class="fas fa-sitemap"></i> WBS</a>
                <a href="<c:out value= "${act}${prj.id}" />" class="btn ${btnColor} btn-spacer"><i class="fas fa-bars"></i> Attivit&agrave;</a>              
                <a href="<c:out value= "${report}${prj.id}" />" class="btn ${btnColor} btn-spacer"><i class="fas fa-chart-line"></i> Report</a>
              <c:if test="${prj.tipo eq 'P'}">
                <a href="<c:out value= "${ind}${prj.id}" />" class="btn ${btnColor} btn-spacer"><i class="fas fa-ruler"></i> Indicatori</a>
                <c:set var="totPPPrj" value="${totPPPrj + 1}" scope="page" />
              </c:if>
              </td>
              <td><cite><c:out value="${prj.tag}" /></cite></td>
            </tr>
            <c:set var="totPrj" value="${totPrj + 1}" scope="page" />
            </c:forEach>
            <c:if test="${(not empty sessionScope.writableDeparments) and (totPrj gt totPPPrj)}">
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
      <c:set var="totPrj" value="${0}" scope="page" />
      <c:set var="totPPPrj" value="${0}" scope="page" />
    </c:forEach>
    <script>
    function viewPlan() {
      var y = document.getElementById("myPlans");
      //y.value = x.value.toUpperCase();
      window.self.location.href = '${projects}' + y.value;
    }
    </script>
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
        <p>
          Non sono stati trovati progetti a te associati nel triennio ${param['y']}&ndash;${param['y']+2}.<br />
          <a href="${project}"><i class="fas fa-home"></i> Torna alla home</a>
        </p>
      </div>
  </c:otherwise>
</c:choose>