<%@ include file="pcURL.jspf" %>
<form action="#" method="post">
  <h2>Inserisci una nuova attività:</h2>
  <div class="row">
    <div class="col-sm-5">ID attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actId" name="actId" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Nome attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actNome" name="actNome" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Descrizione dell'attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDescrizione" name="actDescrizione" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Data di inizio dell'attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDataInizio" name="actDataInizio" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Data di fine dell'attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDataFine" name="actDataFine" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Data di inizio attesa</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDataInizioAttesa" name="actDataInizioAttesa" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Data di fine attesa</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDataFineAttesa" name="actDataFineAttesa" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Data di inizio effettiva</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDataInizioEffettiva" name="actDataInizioEffettiva" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Data di fine effettiva</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actDataFineEffettiva" name="actDataFineEffettiva" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Giorni/Uomo previsti</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actGuPrevisti" name="actGuPrevisti" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Giorni/Uomo effettivi</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actGuEffettivi" name="actGuEffettivi" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Giorni/Uomo rimanenti</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actGuRimanenti" name="actGuRimanenti" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Note di avanzamento</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actNote" name="actNote" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Milestone</div>
    <div class="col-sm-5"><input type="checkbox" class="form-check-input" id="actMilestone" name="actMilestone"></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Identificativo WBS</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actIdWbs" name="actIdWbs" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Identificativo complessit&agrave; dell'attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actIdComplessita" name="actIdComplessita" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Identificativo stato attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actIdStato" name="actIdStato" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Identificativo del ruolo nell'attivit&agrave;</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actIdRuolo" name="actIdRuolo" value=""></div>
  </div>
  <div class="row">
    <div class="col-sm-5">Identificativo della persona</div>
    <div class="col-sm-5"><input type="text" class="form-control" id="actIdPersona" name="actIdPersona" value=""></div>
  </div>
  <%@ include file="subPanel.jspf" %>
</form> 