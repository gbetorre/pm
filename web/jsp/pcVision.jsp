<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="paramsAsTokens" value="${param}" scope="page" />
<c:set var="distinguishingSubmitName" value="" scope="page" />
<c:forTokens var="paramAsToken" items="${paramsAsTokens}" delims=",">
  <c:if test="${fn:startsWith(paramAsToken, ' p=')}">
    <c:set var="distinguishingSubmitName" value="${fn:substring(fn:substringAfter(paramAsToken, '='), 0, 3)}" scope="page" />
  </c:if>
</c:forTokens>
    <form name="pcv" action="#" method="post">
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
      		<textarea id="pcv-situazione" name="pcv-situazione" class="form-control" aria-label="With textarea" readonly>${p.situazioneAttuale}</textarea>
      		<br><br>
      		  Descrizione del Progetto
      		<br>
      		<textarea id="pcv-descrizione" name="pcv-descrizione" class="form-control" aria-label="With textarea" readonly>${p.descrizione}</textarea>
      		<br><br>
      		  Obiettivi misurabili di Progetto
      		<br>
      		<textarea id="pcv-obiettivi" name="pcv-obiettivi" class="form-control" aria-label="With textarea" readonly>${p.obiettiviMisurabili}</textarea>
      		<br><br>
      		  Minacce
      		<br>
      		<textarea id="pcv-minacce" name="pcv-minacce" class="form-control" aria-label="With textarea" readonly>${p.minacce}</textarea>
      		<br><br>
          <div id="container-fluid">
            <div class="row">
              <div class="col-2">  
                <span class="float-left">
                  <a class="btn btn-primary" href="${project}${p.id}">Chiudi</a>
                </span>
              </div>
              <div class="col-8 text-center">
                <%@ include file="subPanel.jspf" %>
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
