<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
  <div class="container mt-3">
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
        <a class="nav-link active" role="tab" data-toggle="tab" href="#">Rischi</a>
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
      <div id="rischi" class="container tab-pane active">
        <table class="table table-bordered table-hover">
          <thead class="thead-light">
            <tr>
              <th>Descrizione</th>
              <th>Probabilit&agrave;</th>
              <th>Impatto</th>
              <th>Livello</th>
              <th>Stato</th>
            </tr>
          </thead>
          <tbody>
          <c:set var="status" value="" scope="page" />
          <c:forEach var="risk" items="${requestScope.rischi}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <input type="hidden" id="pck-id${status}" name="pck-id${status}" value="<c:out value="${risk.id}"/>">
              <tr>
                <th scope="row"><input type="text" class="form-control" id="pck-nome${status}" name="pck-nome${status}" value="<c:out value="${risk.nome}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-informativa${status}" name="pck-informativa${status}" value="<c:out value="${risk.informativa}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-impatto${status}" name="pck-impatto${status}" value="<c:out value="${risk.impatto}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-livello${status}" name="pck-livello${status}" value="<c:out value="${risk.livello}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-stato${status}" name="pck-stato${status}" value="<c:out value="${risk.stato}" />" readonly></th>             
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <input type="hidden" id="pck-loop-status" name="pck-loop-status" value="<c:out value="${status}"/>">
        <div class="text-center">
          <!-- <button class="btn btn-primary" id="addRischio" name="addRischio" onclick="window.open('http://localhost:8080/almalaurea/jsp/pcRischio.jsp', 'newRischio', true)" disabled>Aggiungi</button> -->
          <a href="javascript:window.open('${initParam.appName}/jsp/pcRischio.jsp', 'newRischio', true)">Aggiungi</a>
        </div>
        <hr class="separatore" />
        <div id="container-fluid">
          <div class="row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btn-primary" href="${risorse}${p.id}">&lt; Indietro</a>
              </span>
            </div>
            <div class="col-8 text-center">
              <%@ include file="subPanel.jspf" %>
            </div>
            <div class="col-2">
              <span class="float-right">
                <a class="btn btn-primary" href="${vincoli}${p.id}">Avanti &gt;</a>
              </span>
            </div>
          </div>
          <hr class="separatore" /> 
          <div class="row">
            <div class="col-2">
              <span class="float-left">
                <a class="btn btn-primary" href="${project}${p.id}">Torna all'elenco progetti</a>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>