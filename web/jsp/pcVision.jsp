<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="p" value="${requestScope.progetto}" scope="page" />
      <form action="#" method="post">
        <div class="container mt-3">
        	<ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
         		<li role="Vision" class="nav-item">
         		 	<a href="#" role="tab" data-toggle="tab" class="nav-link active">Vision</a>
        		</li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Stakeholder</a></li>
         		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="#">Deliverable</a></li>
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
        		<input type="text" name="vision-situazione" value="${p.situazioneAttuale}" readonly>
        		<br><br>
        		Descrizione del Progetto
        		<br>
        		<input type="text" name="vision-descrizione" value="" readonly>
        		<br><br>
        		Obiettivi misurabili di Progetto
        		<br>
        		<input type="text" name="vision-obiettivi" value="" readonly>
        		<br><br>
        		Minacce
        		<br>
        		<input type="text" name="vision-minacce" value="" readonly>
        		<br><br>
        		
        		<input type="button" name="modifica" value="modifica">
        		<input type="submit" name="salva" value="salva">
        		<input type="reset" name="annulla" value="annulla">
        	</div>
        </div>
      </form>