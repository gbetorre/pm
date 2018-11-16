<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
  <div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vision}${p.id}">Vision</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
      <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#">Vincoli</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a></li>
    </ul>
    <hr class="separatore" />
    <div class="tab-content responsive hidden-xs hidden-sm">
      <div role="tabpanel" class="tab-pane active" id="tab-pcvincoli">
      <textarea name="pcc-descrizione" class="form-control" aria-label="With textarea" readonly>${p.vincoli}</textarea>
      </div>
      <br><br>
      <div id="container-fluid">
        <div class="row">
          <div class="col-2">  
            <span class="float-left">
              <a class="btn btn-primary" href="${rischi}${p.id}">&lt; Indietro</a>
            </span>
          </div>
          <div class="col-8 text-center">
            <%@ include file="subPanel.jspf" %>
          </div>
          <div class="col-2">
            <span class="float-right">
              <a class="btn btn-primary" href="${milestone}${p.id}">Avanti &gt;</a>
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
</form>