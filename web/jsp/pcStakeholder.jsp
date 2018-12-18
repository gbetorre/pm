<%@ include file="pcURL.jspf" %>
    <form id="pcstakeholder_form" action="#" method="post">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
        <li class="nav-item">
          <a data-toggle="tab" class="nav-link" href="${vision}${p.id}">Vision</a>
        </li>
        <li class="nav-item">
          <a class="nav-link active tabactive" data-toggle="tab" href="#">Stakeholder</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a>
        </li>
    	</ul>
    	<hr class="separatore" />
    	<div class="tab-content responsive hidden-xs hidden-sm">
    		<div role="tabpanel" class="tab-pane active" id="tab-pcstakeholder">
    		<label for="pcs-chiave">Chiave</label>
    		<br>
    		<textarea name="pcs-chiave" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.stakeholderChiave}</textarea>
    		<div class="charNum"></div>
            <br><br>
    		<label for="pcs-istituzionale">Istituzionale</label>
    		<br>
    		<textarea name="pcs-istituzionale" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.stakeholderIstituzionali}</textarea>
    		<div class="charNum"></div>
            <br><br>
    		<label for="pcs-marginale">Marginale</label>
    		<br>
    		<textarea name="pcs-marginale" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.stakeholderMarginali}</textarea>
    		<div class="charNum"></div>
            <br><br>
    		<label for="pcs-operativo">Operativo</label>
    		<br>
    		<textarea name="pcs-operativo" class="form-control" aria-label="With textarea" maxlength="1024" readonly>${p.stakeholderOperativi}</textarea>
          	<div class="charNum"></div>	
            <br><br>
    		<div id="container-fluid">
              <div class="row">
                <div class="col-2">  
                  <span class="float-left">
                    <a class="btn btn-primary" href="${vision}${p.id}">&lt; Indietro</a>
                  </span>
                </div>
                <div class="col-8 text-center">
                  <%@ include file="subPanel.jspf" %>
                </div>
                <div class="col-2">
                  <span class="float-right">
              		  <a class="btn btn-primary" href="${deliverable}${p.id}">Avanti &gt;</a>
                  </span>
                </div>
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
    </form>
    <script>
      var offsetcharacter = 4;
      $(document).ready(function () {
        $('#pcstakeholder_form').validate ({
          rules: {
            'pcs-chiave': {
              minlength: offsetcharacter
            },
            'pcs-istituzionale': {
              minlength: offsetcharacter
            },
            'pcs-marginale': {
              minlength: offsetcharacter
            },
            'pcs-opeativo': {
              minlength: offsetcharacter
            }
          }, 
          messages: {
            'pcs-chiave': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcs-istituzionale': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcs-marginale': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcs-operativo': "Inserire almeno " + offsetcharacter + " caratteri."
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