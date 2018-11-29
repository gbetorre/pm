<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
  <h2>Inserisci una nuova competenza:</h2>
  <div class="row">
    <div class="col-sm-5">ID competenza</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="skillId" name="skillId" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Descrizione</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="skillDescrizione" name="skillDescrizione" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Informativa</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="skillInformativa" name="skillInformativa" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Presenza</div>
    <div class="col-sm-5"><input type="checkbox" class="form-check-input" id="skillPresenza" name="skillPresenza"></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Identificativo della Persona</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="skillIdPersona" name="skillIdPersona" value=""></div>
  </div>
  <%@ include file="subPanel.jspf" %>
</form> 