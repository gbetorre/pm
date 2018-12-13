<%@ include file="pcURL.jspf" %>
    <form id="pcrisorse_form" action="#" method="post">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vision}${p.id}">Vision</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
        <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Risorse</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a></li>
      </ul>
      <hr class="separatore" />
      <div class="tab-content responsive hidden-xs hidden-sm">
        <div role="tabpanel" class="tab-pane active" id="tab-pcrisorse">
          <h4>Competenze del progetto</h4>
          <table class="table table-bordered table-hover">
            <thead class="thead-light">
            <tr>
              <th scope="col">Descrizione</th>
              <th scope="col">Informativa</th>
              <th scope="col"><div class="text-center">Presenza</div></th>
            </tr>
            </thead>
            <tbody>
              <c:set var="status" value="" scope="page" />
              <c:forEach var="skill" items="${requestScope.competenze}" varStatus="loop">
                <c:set var="status" value="${loop.index}" scope="page" />
                <input type="hidden" id="pcr-id${status}" name="pcr-id${status}" value="<c:out value="${skill.id}"/>">
                <tr>
                  <th scope="row"><input type="text" class="form-control" id="pcr-nome${status}" name="pcr-nome${status}" value="<c:out value="${skill.nome}" />" readonly></th>
                  <th scope="row"><input type="text" class="form-control" id="pcr-informativa${status}" name="pcr-informativa${status}" value="<c:out value="${skill.informativa}" />" readonly></th>
                  <th scope="row">
                    <c:choose>
                      <c:when test="${skill.presenza}">
                        <div class="form-check text-center">
                          <input type="checkbox" class="form-check-input" id="pcr-presenza${status}" name="pcr-presenza${status}" checked disabled>
                        </div>
                      </c:when>
                      <c:otherwise>
                        <div class="form-check text-center">
                          <input type="checkbox" class="form-check-input" id="pcr-presenza${status}" name="pcr-presenza${status}" disabled>
                        </div>
                      </c:otherwise>
                    </c:choose>
                  </th>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <input type="hidden" id="pcr-loop-status" name="pcr-loop-status" value="<c:out value="${status}"/>">
          <div class="text-center">
            <!-- <button class="btn btn-primary" id="addCompetenza" name="addCompetenza" onclick="window.open('http://localhost:8080/almalaurea/jsp/pcCompetenza.jsp', 'newCompetenza', true)" disabled>Aggiungi</button>  -->
            <a href="javascript:window.open('${initParam.appName}/jsp/pcCompetenza.jsp', 'newCompetenza', true)">Aggiungi</a>
          </div>
          <hr class="separatore" />
          <label for="pcr-chiaveesterni">Fornitori chiave esterni:</label>
          <br>
          <textarea name="pcr-chiaveesterni" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.fornitoriChiaveEsterni}</textarea>
          <div class="charNum"></div>
          <hr class="separatore" />
          <label for="pcr-chiaveinterni">Fornitori chiave interni:</label>
          <br>
          <textarea name="pcr-chiaveinterni" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.fornitoriChiaveInterni}</textarea>
          <div class="charNum"></div>
          <hr class="separatore" />
          <label for="pcr-serviziateneo">Servizi di ateneo:</label>
          <br>
          <textarea name="pcr-serviziateneo" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.serviziAteneo}</textarea>
          <div class="charNum"></div>
          <hr class="separatore" />
          <div id="container-fluid">
          <div class="row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btn-primary" href="${deliverable}${p.id}">&lt; Indietro</a>
              </span>
            </div>
            <div class="col-8 text-center">
              <%@ include file="subPanel.jspf" %>
            </div>
            <div class="col-2">
              <span class="float-right">
                <a class="btn btn-primary" href="${rischi}${p.id}">Avanti &gt;</a>
              </span>
            </div>
          </div>
          <hr class="separatore" /> 
          <div class="row">
            <div class="col-2">
              <span class="float-left">
                <a class="btn btn-primary" href="${project}${p.id}">Torna all'elenco progetti</a>
              </span>
            </div>
          </div>
        </div>
        </div>
      </div>
    </form>
    <script>
      var offsetcharacter = 5;
      $(document).ready(function () {
        $('#pcrisorse_form').validate ({
          rules: {
            'pcr-chiaveesterni': {
              minlength: offsetcharacter
            },
            'pcr-chiaveinterni': {
              minlength: offsetcharacter
            },
            'pcr-serviziateneo': {
              minlength: offsetcharacter
            }
          }, 
          messages: {
            'pcr-chiaveesterni': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcr-interni': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcr-serviziateneo': "Inserire almeno " + offsetcharacter + " caratteri."
          },
          submitHandler: function (form) {
            alert('valid form submitted');
            return true;
          }
        });
        
        $('textarea[maxlength]').keyup(function () {
          var len = $(this).val().length;
          var dblength = parseInt($(this).attr('maxlength'));
          if(len >= dblength) {
            this.value = this.value.substring(0, dblength);
            $(this).next('div').text(' you have reached the limit');
          } else {
            var chars = dblength - len;
            $(this).next('div').text(chars + ' characters left');
          }
        });
      });
    </script>
            