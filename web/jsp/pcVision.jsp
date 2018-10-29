<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<c:set var="p" value="${requestScope.progetto}" scope="page" />
      <form action="#" method="post">
        <div class="container mt-3">
        	<ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
         		<li class="nav-item"><a class="nav-link active" data-toggle="tab" href="">Vision</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Risorse</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Rischi</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Vincoli</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Milestone</a></li>
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
        		
        		<input type="button" class="btn btn-primary" name="modifica" value="modifica">
        		<input type="submit" class="btn btn-primary" name="salva" value="salva">
        		<input type="reset" class="btn btn-primary" name="annulla" value="annulla">
        	  </div>
          </div>
        </div>
      </form>