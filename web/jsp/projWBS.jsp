<%@ include file="pcURL.jspf" %>
<form action="${updWbs}" method="post">
  <c:url var="updWbs" context="${initParam.appName}" value="/" scope="page">
    <c:param name="q" value="wbs" />
    <c:param name="p" value="upd" />
    <c:param name="id" value="${p.id}" />
    <c:param name="idw" value="" />
  </c:url>
  <h2>WBS del progetto <strong><c:out value="${p.titolo}" /></strong></h2>
  <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
    <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="">WBS</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Attività</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Report</a></li>
  </ul>
  <hr class="separatore" />
  <div class="tab-content responsive hidden-xs hidden-sm">
    <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
      <table class="table table-bordered table-hover">
        <thead class="thead-light">
        <tr>
          <th scope="col"></th>
          <th scope="col">Nome</th>
          <th scope="col">Descrizione</th>
          <th scope="col"><div class="text-center">Workpackage</div></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="status" value="" scope="page" />
        <c:forEach var="wbs" items="${requestScope.wbs}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <tr>
            <td scope="col">
              <input type="radio" id="wbs-<c:out value="${wbs.id}"/>" name="wbs-select" value="<c:out value="${wbs.id}"/>"/>
              <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbs.id}"/>">
            </td>
            <td scope="col">
              <input type="text" class="form-control" id="wbs-nome${status}" name="wbs-nome${status}" value="<c:out value="${wbs.nome}"/>" readonly>
            </td>
            <td scope="col">
              <input type="text" class="form-control" id="wbs-descrizione${status}" name="wbs-descrizione${status}" value="<c:out value="${wbs.descrizione}"/>" readonly>
            </td>
            <td scope="col">
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
      <input type="hidden" id="wbs-loop-status" name="wbs-loop-status" value="<c:out value="${status}"/>">
      <input type="submit" id="sumbit" name="submit" value="submit">
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
</form>