<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="#" method="post">
<div class="tab-container">
	<div role="tabpanel">
	<ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
 		<li role="Vision" class="active">
 		 	<a href="#" role="tab" data-toggle="tab" class="transition">Vision</a>
		</li>
 		<li><a href="#">Stakeholder</a></li>
 		<li><a href="#">Del</a></li>
 		<li><a href="#">Risorse</a></li>
 		<li><a href="#">Rischi</a></li>
 		<li><a href="#">Vincoli</a></li>
 		<li><a href="#">Milestone</a></li>
	</ul>
	
	<div class="tab-content responsive hidden-xs hidden-sm">
		<div role="tabpanel" class="tab-pane active" id="tab-pcvision">
		Situazione attuale
		<br>
		<input type="text" name="vision-situazione" value="" readonly>
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