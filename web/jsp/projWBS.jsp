<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
  <h2>WBS del progetto <c:out value="${p.titolo}" /></h2>
  <div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Attività</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Report</a></li>
    </ul>
    <hr class="separatore" />
    <div class="tab-content responsive hidden-xs hidden-sm">
      <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
      <h4>WBS del progetto</h4>
        <table class="table table-bordered table-hover">
          <thead class="thead-light">
          <tr>
            <th scope="col">Nome</th>
            <th scope="col">Descrizione</th>
            <th scope="col"><div class="text-center">Workpackage</div></th>
          </tr>
          </thead>
          <tbody>
          <c:set var="status" value="" scope="page" />
          <c:forEach var="wbs" items="${requestScope.wbs}" varStatus="loop">
            <c:set var="status" value="${loop.index}" scope="page" />
            <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbs.id}"/>">
            <tr>
              <td scope="row">
                <input type="text" class="form-control" id="wbs-nome${status}" name="wbs-nome${status}" value="<c:out value="${wbs.nome}"/>" readonly>
              </td>
              <td scope="row">
                <input type="text" class="form-control" id="wbs-descrizione${status}" name="wbs-descrizione${status}" value="<c:out value="${wbs.descrizione}"/>" readonly>
              </td>
              <td scope="row">
                <c:choose>
                  <c:when test="${wbs.workPackage}">
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" checked disabled>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" disabled>
                    </div>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
        <div id="container-fluid">
        <div class="row">
          <div class="col-1 text-center"></div>
          <div class="col-1 text-center">
            <button class="btn btn-primary" disabled>&and;</button>
          </div>
          <div class="col-1 text-center"></div>
        </div>
        <div class="row">
          <div class="col-1 text-center">
            <button class="btn btn-primary" disabled>&lt;</button>
          </div>
          <div class="col-1 text-center"></div>
          <div class="col-1 text-center">
            <button class="btn btn-primary" disabled>&gt;</button>
          </div>
        </div>
        <div class="row">
          <div class="col-1 text-center"></div>
          <div class="col-1 text-center">
            <button class="btn btn-primary" disabled>&or;</button>
          </div>
          <div class="col-1 text-center"></div>
        </div>
          <hr class="separatore" />
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
      </div>
    </div>
  </div>
</form>