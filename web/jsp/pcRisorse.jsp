<%@ include file="pcURL.jspf" %>
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
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
          <label for="pcr-chiaveesterni">Fornitori chiave esterni:</label>
          <br>
          <textarea name="pcr-chiaveesterni" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.fornitoriChiaveEsterni}</textarea>
          <div class="charNum"></div>
          <hr class="separatore" />
          <label for="pcr-serviziateneo">Servizi di ateneo:</label>
          <br>
          <textarea name="pcr-serviziateneo" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.serviziAteneo}</textarea>
          <div class="charNum"></div>
          <hr class="separatore" />
          <div id="container-fluid">
            <div class="row">
              <div class="col-2"></div>
              <div class="col-8 text-center">
                <%@ include file="subPanel.jspf" %>
              </div>
              <div class="col-2"></div>
            </div>
            <hr class="separatore" />
          </div>
        </div>
      </div>
    </form>
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
        <c:set var="status" value="0" scope="page" />
        <c:forEach var="skill" items="${requestScope.competenze}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <input type="hidden" id="pcr-id${status}" name="pcr-id${status}" value="<c:out value="${skill.id}"/>">
          <tr>
            <td scope="row"><c:out value="${skill.nome}" /></td>
            <td scope="row"><c:out value="${skill.informativa}" /></td>
            <c:choose>
              <c:when test="${skill.presenza}">
                <td scope="row" class="bgcolorgreen">
                  <div class="form-check text-center">
                    <span>SI</span>
                  </div>
                </td>
              </c:when>
              <c:otherwise>
                <td scope="row" class="bgcolorred">
                  <div class="form-check text-center">
                    <span>NO</span>
                  </div>
                </td>
              </c:otherwise>
            </c:choose>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <input type="hidden" id="pcr-loop-status" name="pcr-loop-status" value="<c:out value="${status}"/>">
    <hr class="separatore" />
    <div id="container-fluid">
      <div class="row">
        <div class="col-2">  
          <span class="float-left">
            <a class="btn btnNav" href="${deliverable}${p.id}">
              <i class="fas fa-chevron-left"></i>
              Indietro
            </a>
          </span>
        </div>
        <div class="col-8"></div>
        <div class="col-2">
          <span class="float-right">
            <a class="btn btnNav" href="${rischi}${p.id}">
              Avanti
              <i class="fas fa-chevron-right"></i>
            </a>
          </span>
        </div>
      </div>
      <hr class="separatore" />
      </div>
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