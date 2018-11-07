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
<c:url var="deliverable" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcd" />
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
<c:url var="project" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="id" value="" />
</c:url>
<c:set var="p" value="${requestScope.progetto}" scope="page" />
<form action="#" method="post">
  <div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vision}${p.id}">Vision</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
      <li class="nav-item"><a class="nav-link active" data-toggle="tab" href="#">Milestone</a></li>
    </ul>
    <hr class="separatore" />
    <div class="tab-content responsive hidden-xs hidden-sm">
      <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
      <h4>Attività del progetto</h4>
        <table class="table table-bordered table-hover">
          <thead class="thead-light">
          <tr>
            <th scope="col">Nome</th>
            <th scope="col">Descrizione</th>
            <th scope="col"><div class="text-center">Milestone</div></th>
          </tr>
          </thead>
          <tbody>
          <c:set var="status" value="" scope="page" />
          <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
            <c:set var="status" value="${loop.index}" scope="page" />+
            <input type="hidden" id="pcm-loop-status" name="pcm-loop-status" value="${status}">
            <input type="hidden" id="pcm-id${status}" name="pcm-id${status}" value="<c:out value="${act.id}"/>">
            <tr>
              <td scope="row">
                <input type="text" class="form-control" id="pcm-nome${status}" name="pcm-nome${status}" value="<c:out value="${act.nome}"/>" readonly>
              </td>
              <td scope="row">
                <input type="text" class="form-control" id="pcm-descrizione${status}" name="pcm-descrizione${status}" value="<c:out value="${act.descrizione}"/>" readonly>
              </td>
              <td scope="row">
                <c:choose>
                  <c:when test="${act.milestone}">
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="pcm-milestone${status}" name="pcm-milestone${status}" value="" checked disabled>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="form-check text-center">
                      <input type="checkbox" class="form-check-input" id="pcm-milestone${status}" name="pcm-milestone${status}" value="" disabled>
                    </div>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
        <div class="text-center"><input type="button" class="btn btn-primary" name="addAttivita" value="Aggiungi"></div>
        <hr class="separatore" />
        <div id="container-fluid">
          <div class="row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btn-primary" href="${vincoli}${p.id}">&lt; Indietro</a>
              </span>
            </div>
            <div class="col-8 text-center">
              <%@ include file="panel.jspf" %>
            </div>
            <div class="col-2">
              <span class="float-right">
                <a class="btn btn-primary" href="${project}${p.id}">Chiudi</a>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
