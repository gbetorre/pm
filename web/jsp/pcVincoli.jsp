<%@ include file="pcURL.jspf" %>
    <span class="float-right">
      <a class="btn btn-primary" href="${project}">Torna a elenco progetti</a>
    </span>
    <form id="pcvincoli_form" action="#" method="post">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vision}${p.id}">Vision</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
        <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Vincoli</a></li>
        <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a></li>
      </ul>
      <hr class="separatore" />
      <div class="tab-content responsive hidden-xs hidden-sm">
        <div role="tabpanel" class="tab-pane active" id="tab-pcvincoli">
        <textarea name="pcc-descrizione" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.vincoli}</textarea>
        <div class="charNum"></div>
        </div>
        <br><br>
        <div id="container-fluid">
          <div class="row">
            <div class="col-2">  
              <span class="float-left">
                <a class="btn btn-primary" href="${rischi}${p.id}">&lt; Indietro</a>
              </span>
            </div>
            <div class="col-8 text-center">
              <%@ include file="subPanel.jspf" %>
            </div>
            <div class="col-2">
              <span class="float-right">
                <a class="btn btn-primary" href="${milestone}${p.id}">Avanti &gt;</a>
              </span>
            </div>
          </div>
          <hr class="separatore" />
        </div>
      </div>
    </form>
    <script>
      var offsetcharacter = 5;
      $(document).ready(function () {
        $('#pcvincoli_form').validate ({
          rules: {
            'pcc-descrizione': {
              minlength: offsetcharacter
            }
          }, 
          messages: {
            'pcc-descrizione': "Inserire almeno " + offsetcharacter + " caratteri."
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