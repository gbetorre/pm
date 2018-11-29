<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
  <h2>Inserisci un nuovo rischio:</h2>
  <div class="row">
    <div class="col-sm-5">ID rischio</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="riskId" name="riskId" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Descrizione</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="riskDescrizione" name="riskDescrizione" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Probabilit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="riskProbabilita" name="riskProbabilita" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Impatto</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="riskImpatto" name="riskImpatto" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Livello</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="riskLivello" name="riskLivello" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Stato</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="riskStato" name="riskStato" value=""></div>
  </div>
  <%@ include file="subPanel.jspf" %>
</form> 