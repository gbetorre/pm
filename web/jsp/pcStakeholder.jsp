<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
<div class="container mt-3">
  <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
    <li class="nav-item">
      <a data-toggle="tab" class="nav-link" href="${vision}${p.id}">Vision</a>
    </li>
    <li class="nav-item">
      <a data-toggle="tab" class="nav-link active" href="#">Stakeholder</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a>
    </li>
    <li class="nav-item">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a>
    </li>
	</ul>
	<hr class="separatore" />
	<div class="tab-content responsive hidden-xs hidden-sm">
		<div role="tabpanel" class="tab-pane active" id="tab-pcstakeholder">
		Chiave
		<br>
		<textarea name="pcs-chiave" class="form-control" aria-label="With textarea" readonly>${p.stakeholderChiave}</textarea>
		<br><br>
		Istituzionale
		<br>
		<textarea name="pcs-istituzionale" class="form-control" aria-label="With textarea" readonly>${p.stakeholderIstituzionali}</textarea>
		<br><br>
		Marginale
		<br>
		<textarea name="pcs-marginale" class="form-control" aria-label="With textarea" readonly>${p.stakeholderMarginali}</textarea>
		<br><br>
		Operativo
		<br>
		<textarea name="pcs-operativo" class="form-control" aria-label="With textarea" readonly>${p.stakeholderOperativi}</textarea>
		<br><br>
		
		<div id="container-fluid">
          <div class="row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btn-primary" href="${vision}${p.id}">&lt; Indietro</a>
              </span>
            </div>
            <div class="col-8 text-center">
              <%@ include file="subPanel.jspf" %>
            </div>
            <div class="col-2">
              <span class="float-right">
          		  <a class="btn btn-primary" href="${deliverable}${p.id}">Avanti &gt;</a>
              </span>
            </div>
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