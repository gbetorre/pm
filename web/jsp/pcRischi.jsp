<%@ include file="pcURL.jspf" %>
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <form action="#" method="post">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
        <li role="Vision" class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${vision}${p.id}">Vision</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a>
        </li>
        <li class="nav-item">
          <a class="nav-link active tabactive" role="tab" data-toggle="tab" href="#">Rischi</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a>
        </li>
      </ul>
      <hr class="separatore" />
      <div class="tab-content">
        <div id="rischi">
          <table class="table table-bordered table-hover">
            <thead class="thead-light">
              <tr>
                <th>Descrizione</th>
                <th>Dimensione</th>
                <th>Tipologia</th>
                <th>Urgente</th>
              </tr>
            </thead>
            <tbody>
            <c:set var="status" value="" scope="page" />
            <c:forEach var="risk" items="${requestScope.rischi}" varStatus="loop">
            <c:set var="status" value="${loop.index}" scope="page" />
              <input type="hidden" id="pck-id${status}" name="pck-id${status}" value="<c:out value="${risk.id}"/>">
              <tr>
                <td scope="row"><c:out value="${risk.nome}" /></td>
                <td scope="row"><c:out value="${risk.impatto}" /></td>
                <td scope="row"><c:out value="${risk.informativa}" /></td>
            <c:choose>
              <c:when test="${risk.urgenza}">
                <td scope="row" class="bgcolorred">
                  <div class="form-check text-center">
                    <span>SI</span>
                  </div>
                </td>
              </c:when>
              <c:otherwise>
              <td scope="row" class="bgcolorgreen">
                <div class="form-check text-center">
                  <span>NO</span>
                </div>
              </td>
              </c:otherwise>
            </c:choose>          
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <input type="hidden" id="pck-loop-status" name="pck-loop-status" value="<c:out value="${status}"/>">
          <div class="text-center">
          </div>
          <hr class="separatore" />
          <div id="container-fluid">
            <div class="row">
              <div class="col-2">  
                <span class="float-left">
                  <a class="btn btnNav" href="${risorse}${p.id}">
                    <i class="fas fa-chevron-left"></i>
                    Indietro
                  </a>
                </span>
              </div>
              <div class="col-8 text-center">
                <%@ include file="subPanel.jspf" %>
              </div>
              <div class="col-2">
                <span class="float-right">
                  <a class="btn btnNav" href="${vincoli}${p.id}">
                    Avanti
                    <i class="fas fa-chevron-right"></i>
                  </a>
                </span>
              </div>
            </div>
            <hr class="separatore" />
          </div>
        </div>
      </div>
    </form>