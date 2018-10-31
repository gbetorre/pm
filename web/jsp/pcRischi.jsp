<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url var="vincoli" context="/almalaurea" value="/" scope="page">
  <c:param name="q" value="pol" />
  <c:param name="p" value="pcc" />
  <c:param name="id" value="" />
</c:url>
<form action="#" method="post">
  <div class="container mt-3">
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li role="Vision" class="nav-item">
        <a href="#" data-toggle="tab" class="nav-link">Vision</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Stakeholder</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Deliverable</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Risorse</a>
      </li>
      <li class="nav-item">
        <a class="nav-link active" role="tab" data-toggle="tab" href="#">Rischi</a>
      </li>
      <li class="nav-item">
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#">Milestone</a>
      </li>
    </ul>
    
    <div class="tab-content">
      <div id="rischi" class="container tab-pane active">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>ID</th>
              <th>Descrizione</th>
              <th>Livello</th>
              <th>Stato</th>
            </tr>
          </thead>
          <tbody>
          <!-- TODO ciclo che mi genera una riga per ogni rischio del progetto -->
            <tr>
              <th></th>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  

</form>