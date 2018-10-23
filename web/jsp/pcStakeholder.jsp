<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="#" method="post">
<div class="tab-container">
	<div role="tabpanel">
	<ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
 		<li>
 		 	<a href="#">Vision</a>
		</li>
 		<li role="stakeholder" class="active">
 			<a href="#" role="tab" data-toggle="tab" class="transition">Stakeholder</a>
 		</li>
 		<li><a href="#">Del</a></li>
 		<li><a href="#">Risorse</a></li>
 		<li><a href="#">Rischi</a></li>
 		<li><a href="#">Vincoli</a></li>
 		<li><a href="#">Milestone</a></li>
	</ul>
	
	<div class="tab-content responsive hidden-xs hidden-sm">
		<div role="tabpanel" class="tab-pane active" id="tab-pcvision">
		Chiave
		<br>
		<textarea name="stakeholderarea" class="form-control" aria-label="With textarea" readonly></textarea>
		<br><br>
		Istituzionale
		<br>
		<textarea name="stakeholderarea" class="form-control" aria-label="With textarea" readonly></textarea>
		<br><br>
		Marginale
		<br>
		<textarea name="stakeholderarea" class="form-control" aria-label="With textarea" readonly></textarea>
		<br><br>
		Operativo
		<br>
		<textarea name="stakeholderarea" class="form-control" aria-label="With textarea" readonly></textarea>
		<br><br>
		
		<input type="button" name="modifica" value="modifica" onclick="modify(stakeholderarea)">
		<input type="submit" name="salva" value="salva">
		<input type="reset" name="annulla" value="annulla">
	</div>
</div>
</form>