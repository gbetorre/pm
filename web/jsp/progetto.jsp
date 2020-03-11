<%@ include file="pcURL.jspf" %>    <h2 align="center">      <c:out value="${p.titolo}" />    </h2>    <hr class="separatore" />    <c:out value="${p.descrizione}" escapeXml="false" />    <hr class="separatore" />    <div class="form-row">    <c:if test="${not empty requestScope.staff}">      <div class="col-4">        <h3 class="bordo">Partecipanti al Progetto</h3>        <div class="container-fluid">          <h4 class="heading">Project Manager</h4>          <c:forEach var="pm" items="${requestScope.staff}">            <c:if test="${pm.nomeClasse eq 'PMCommand'}">              <c:out value="${pm.nome} ${pm.nomeReale}" /><br />            </c:if>          </c:forEach>          <hr class="separatore" />          <h4 class="heading">Team Leader</h4>          <c:forEach var="tl" items="${requestScope.staff}">            <c:if test="${tl.nomeClasse eq 'TLCommand'}">              <c:out value="${tl.nome} ${tl.nomeReale}" /><br />            </c:if>          </c:forEach>          <hr class="separatore" />          <h4 class="heading">Collaboratori</h4>          <c:forEach var="user" items="${requestScope.staff}">            <c:if test="${user.nomeClasse eq 'USERCommand'}">              <c:out value="${user.nome} ${user.nomeReale}" /><br />            </c:if>          </c:forEach>        </div>      </div>    </c:if>    <c:choose>      <c:when test="${not empty lps}">      <div class="col-5">&nbsp;</div>        <div class="col-3 col-centered">          <%@ include file="subStatus.jspf" %>          <hr class="separatore" />          <p class="text-center">            <a href="${initParam.appName}/data?q=pol&out=rtf&id=${p.id}" class="btn btn-warning" id="expReport"><i class="fas fa-file-export"></i> Stampa</a>          </p>        </div>      </div>      </c:when>      <c:otherwise>        <div class="col-8 alert alert-danger">          <strong>Attenzione!</strong>           <p>            Non &egrave; stato trovato alcuno status progetto associato al progetto.            Avendone i diritti &egrave; possibile <a href="${lastStatus}${p.id}">aggiungerne uno</a>.          </p>        </div>         </c:otherwise>    </c:choose>    <hr class="separatore" />    <div class="col-12 text-center">      <a class="btn btn-primary" href="${project}"><i class="far fa-times-circle"></i> Chiudi </a>    </div>  <%--    <p>      <strong>Data inizio</strong> <c:out value="${p.dataInizio}" /><br />      <strong>Data fine</strong> <c:out value="${p.dataFine}" />    </p>      <h4>Situazione Attuale</h4>      <c:out value="${p.situazioneAttuale}" />    <h4>Situazione Finale</h4>      <c:out value="${p.situazioneFinale}" />    <h4>Obiettivi Misurabili</h4>      <c:out value="${p.obiettiviMisurabili}" />    <h4>Minacce</h4>      <c:out value="${p.minacce}" />    <h4>Stakeholder Marginali</h4>      <c:out value="${p.stakeholderMarginali}" />    <h4>Stakeholder Operativi</h4>      <c:out value="${p.stakeholderOperativi}" />    <h4>Stakeholder Istituzionali</h4>      <c:out value="${p.stakeholderIstituzionali}" />    <h4>Stakeholder Chiave</h4>      <c:out value="${p.stakeholderChiave}" />    <h4>Deliverable</h4>      <c:out value="${p.deliverable}" />    <h4>Fornitori chiave interni</h4>      <c:out value="${p.fornitoriChiaveInterni}" />    <h4>Fornitori chiave esterni</h4>      <c:out value="${p.fornitoriChiaveEsterni}" />    <h4>Servizi di ateneo</h4>      <c:out value="${p.serviziAteneo}" />  --%>