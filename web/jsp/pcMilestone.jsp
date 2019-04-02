<%@ include file="pcURL.jspf" %>
    <span class="float-right">
      <a class="ico" href="${project}">
        <img src="${initParam.urlDirectoryImmagini}/ico-home.png" class="ico-home" alt="Torna a elenco progetti" title="Torna a elenco progetti" />
      </a>
    </span>
    <form action="#" method="post">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vision}${p.id}">Vision</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
        <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Milestone</a></li>
      </ul>
      <hr class="separatore" />
      <div class="tab-content responsive hidden-xs hidden-sm">
        <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
        <h4>Milestone del progetto</h4>
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
              <c:set var="status" value="${loop.index}" scope="page" />
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
                        <input type="checkbox" class="form-check-input" id="pcm-milestone${status}" name="pcm-milestone${status}" checked disabled>
                      </div>
                    </c:when>
                    <c:otherwise>
                      <div class="form-check text-center">
                        <input type="checkbox" class="form-check-input" id="pcm-milestone${status}" name="pcm-milestone${status}" disabled>
                      </div>
                    </c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <input type="hidden" id="pcm-loop-status" name="pcm-loop-status" value="<c:out value="${status}"/>">
          <hr class="separatore" />
          <div id="container-fluid">
            <div class="row">
              <div class="col-2">  
                <span class="float-left">
                  <a class="ico" href="${vincoli}${p.id}">
                    <img src="${initParam.urlDirectoryImmagini}/ico-back.png" class="ico-home" alt="Vai a pagina Stakeholder" title="Vai a pagina Stakeholder" />
                  </a>
                </span>
              </div>
              <div class="col-8 text-center">
                <%@ include file="subPanel.jspf" %>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>