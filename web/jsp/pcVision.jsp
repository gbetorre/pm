<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url var="stakeholder" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcs" />
  <c:param name="id" value="" />
</c:url>
<c:url var="deliverable" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcd" />
  <c:param name="id" value="" />
</c:url>
<c:url var="project" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="id" value="" />
</c:url>
<c:url var="risorse" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcr" />
  <c:param name="id" value="" />
</c:url>
<c:url var="rischi" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pck" />
  <c:param name="id" value="" />
</c:url>
<c:url var="vincoli" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcc" />
  <c:param name="id" value="" />
</c:url>
<c:url var="milestone" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcm" />
  <c:param name="id" value="" />
</c:url>
<c:set var="p" value="${requestScope.progetto}" scope="page" />
<form action="#" method="post">
  <div class="container mt-3">
  	<ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
   		<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#">Vision</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a></li>
  	</ul>
  	<hr class="separatore" />
  	<div class="tab-content responsive hidden-xs hidden-sm">
  		<div role="tabpanel" class="tab-pane active" id="tab-pcvision">
  		Situazione attuale
  		<br>
  		<textarea name="pcv-situazione" class="form-control" aria-label="With textarea">${p.situazioneAttuale}</textarea>
  		<br><br>
  		Descrizione del Progetto
  		<br>
  		<textarea name="pcv-descrizione" class="form-control" aria-label="With textarea">${p.descrizione}</textarea>
  		<br><br>
  		Obiettivi misurabili di Progetto
  		<br>
  		<textarea name="pcv-obiettivi" class="form-control" aria-label="With textarea">${p.obiettiviMisurabili}</textarea>
  		<br><br>
  		Minacce
  		<br>
  		<textarea name="pcv-minacce" class="form-control" aria-label="With textarea">${p.minacce}</textarea>
  		<br><br>
      <div id="container-fluid">
        <div class="row">
          <div class="col-2">  
            <span class="float-left">
              <a class="btn btn-primary" href="${project}${p.id}">Chiudi</a>
            </span>
          </div>
          <div class="col-8 text-center">
        		<input type="button" class="btn btn-primary" name="modifica" value="Modifica" >
            <c:set var="ruoli" value="${sessionScope.usr.ruoli}" scope="page" />
            <c:set var="ruolo" value="" scope="page" />
            <!-- <c:set var="found" value="${ruoli.size()}" scope="page" /> -->
            <!--  <c:set var="found" value="${fn:length(ruoli)}" scope="page" /> -->
            <fmt:parseNumber var="found" type="number" value="${fn:length(ruoli)}" />
            <c:forEach var="c" items="${ruoli}" varStatus="loop" begin="0" end="found">    <!-- testare -->
              <c:if test="${c.id eq p.id}">                                <!-- testare -->
                <c:set var="ruolo" value="${c.nome}" scope="page" />
                <c:set var="found" value="${loop.getIndex()}" scope="page" />    <!-- testare -->
              </c:if>
            </c:forEach>
            <c:if test="${ruolo eq 'PMOATE'}">
        		  <input type="submit" class="btn btn-primary" name="salva" value="Salva">
            </c:if>
          </div>
          <div class="col-2">
            <span class="float-right">
        		  <a class="btn btn-primary" href="${stakeholder}${p.id}">Avanti &gt;</a>
            </span>
          </div>
        </div>
	    </div>
      </div>
    </div>
  </div>
</form>