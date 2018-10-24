<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="#" method="post">
<div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li role="Vision" class="nav-item">
        <a href="#" data-toggle="tab" class="nav-link">Vision</a>
      </li>
      <li class="nav-item">
        <a class="nav-link active" role="tab" data-toggle="tab" href="#">Stakeholder</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Deliverable</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Risorse</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Rischi</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Vincoli</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Milestone</a>
      </li>
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