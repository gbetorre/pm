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
          <c:forEach var="risk" items="${requestScope.rischi}" varStatus="loop">
              <tr>
                <th scope="row"><input type="text" class="form-control" id="pck-nome" name="pck-nome" value="<c:out value="${risk.nome}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-informativa" name="pck-informativa" value="<c:out value="${risk.informativa}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-impatto" name="pck-impatto" value="<c:out value="${risk.impatto}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-livello" name="pck-livello" value="<c:out value="${risk.livello}" />" readonly></th>
                <th scope="row"><input type="text" class="form-control" id="pck-stato" name="pck-stato" value="<c:out value="${risk.stato}" />" readonly></th>             
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <div class="text-center"><input type="button" class="btn btn-primary" name="addRischio" value="Aggiungi" ></div>
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
        </div>
      </div>
    </div>
  </div>
</form>