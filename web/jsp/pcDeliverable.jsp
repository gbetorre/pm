<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="vision" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcv" />
  <c:param name="id" value="" />
</c:url>
<c:url var="stakeholder" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcs" />
  <c:param name="id" value="" />
</c:url>
<c:set var="p" value="${requestScope.progetto}" scope="page" />
<form action="#" method="post">
  <div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li role="Vision" class="nav-item">
        <a data-toggle="tab" class="nav-link" href="${vision}${p.id}">Vision</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" role="tab" data-toggle="tab"  href="${stakeholder}${p.id}">Stakeholder</a>
      </li>
      <li class="nav-item">
        <a class="nav-link active" data-toggle="tab">Deliverable</a>
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
    <hr class="separatore" /> 
    <div class="tab-content responsive hidden-xs hidden-sm">
      <div role="tabpanel" class="tab-pane active" id="tab-pcdeliverable">
        <textarea name="pcd-descrizione" class="form-control" aria-label="With textarea">${p.deliverable}</textarea>
      </div>
    </div>
    <br><br>
    <input type="button" class="btn btn-primary" name="modifica" value="modifica" onclick="modify()">
    <input type="submit" class="btn btn-primary" name="salva" value="salva">
    <input type="reset" class="btn btn-primary" name="annulla" value="annulla">
  </div>
</form>