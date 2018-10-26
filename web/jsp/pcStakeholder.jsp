<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="vision" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcv" />
  <c:param name="id" value="" />
</c:url>
<c:set var="p" value="${requestScope.progetto}" scope="page" />
<form action="#" method="post">
<div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li role="Vision" class="nav-item">
        <a href="${vision}${prj.id}" data-toggle="tab" class="nav-link">Vision</a>
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
		<div role="tabpanel" class="tab-pane active" id="tab-pcstakeholder">
		Chiave
		<br>
		<textarea name="pcs-chiave" class="form-control" aria-label="With textarea">${p.stakeholderChiave}</textarea>
		<br><br>
		Istituzionale
		<br>
		<textarea name="pcs-istituzionale" class="form-control" aria-label="With textarea">${p.stakeholderIstituzionali}</textarea>
		<br><br>
		Marginale
		<br>
		<textarea name="pcs-marginale" class="form-control" aria-label="With textarea">${p.stakeholderMarginali}</textarea>
		<br><br>
		Operativo
		<br>
		<textarea name="pcs-operativo" class="form-control" aria-label="With textarea">${p.stakeholderOperativi}</textarea>
		<br><br>
		
		<input type="button" name="modifica" value="modifica" onclick="modify()">
		<input type="submit" name="salva" value="salva">
		<input type="reset" name="annulla" value="annulla">
	</div>
</div>
</form>
<script>
	function modify(){
		$('#textarea').editable({
            type:  'textarea',
            pk:    1,
            name:  'comments',
            url:   'post.php',  
            title: 'Enter comments'
         });
	}
</script>