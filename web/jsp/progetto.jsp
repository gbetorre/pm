<%@ include file="pcURL.jspf" %>    <h1>      Benvenuto <c:out value="${sessionScope.usr.nome}" /> <c:out value="${sessionScope.usr.cognome}" />    </h1>    <c:set var="p" value="${requestScope.progetto}" scope="page" />  <c:choose>    <c:when test="${not empty p}">    <h2 align="center">      <c:out value="${p.titolo}" />    </h2>    <hr class="separatore" />    <form  action="#" method="post">      <div class="row">        <div class="col-sm-5">Mese corrente</div>        <div class="col-sm-5"><input type="text" class="form-control" name="sMese" value="${p.meseRiferimento}" readonly></div>      </div>      <hr class="separatore" />      <div class="row">        <div class="col-sm-5">Descrizione avanzamento</div>        <div class="col-sm-5"><textarea class="form-control" name="sAttuale" readonly>${p.descrizioneStatoCorrente}</textarea></div>      </div>      <hr class="separatore" />      <div class="row">        <div class="col">Stato costi</div>        <div class="col">Stato tempi</div>        <div class="col">Stato rischi</div>      </div>      <div class="row">        <div class="col">          <select class="form-control" id="sCosti" name="sCosti" disabled>            <option selected>${p.statoCosti}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>        <div class="col">          <select class="form-control" id="sTempi" name="sTempi" disabled>            <option selected>${p.statoTempi}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>        <div class="col">          <select class="form-control" id="sRischi" name="sRischi" disabled>            <option selected>${p.statoRischi}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>      </div>      <div class="row">        <div class="col">Stato risorse</div>        <div class="col">Stato scope</div>        <div class="col">Stato comunicazione</div>      </div>      <div class="row">        <div class="col">          <select class="form-control" id="sRisorse" name="sRisorse" disabled>            <option selected>${p.statoRisorse}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>        <div class="col">          <select class="form-control" id="sScope" name="sScope" disabled>            <option selected>${p.statoScope}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>        <div class="col">          <select class="form-control" id="sComunicazione" name="sComunicazione" disabled>            <option selected>${p.statoComunicazione}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>      </div>      <div class="row">        <div class="col">Stato qualit&agrave;</div>        <div class="col">Stato approvvigionamenti</div>        <div class="col">Stato stakeholder</div>      </div>      <div class="row">        <div class="col">          <select class="form-control" id="sQualita" name="sQualita" disabled>            <option selected>${p.statoQualita}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>        <div class="col">          <select class="form-control" id="sApprovvigionamenti" name="sApprovvigionamenti" disabled>            <option selected>${p.statoApprovvigionamenti}</option>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>        <div class="col">          <select class="form-control" id="sStakeholder" name="sStakeholder" disabled>            <option>OK</option>            <option>POSSIBILI PROBLEMI</option>            <option>CRITICO</option>          </select>        </div>      </div>      <hr class="separatore" />      <div class="row">        <div class="col-2">            <span class="float-left">            <a class="btn btn-primary" href="${project}${p.id}">Chiudi</a>          </span>        </div>        <div class="col-8 text-center">          <%@ include file="subPanel.jspf" %>        </div>      </div>    </form>    </c:when>    <c:otherwise>      <p style="color:red;">Spiacente: non &egrave; stato trovato un progetto a te associato con questo identificativo.</p>        </c:otherwise>  </c:choose>