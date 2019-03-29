<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="paramsAsTokens" value="${param}" scope="page" />
<c:set var="distinguishingSubmitName" value="" scope="page" />
<c:forTokens var="paramAsToken" items="${paramsAsTokens}" delims=",">
  <c:if test="${fn:startsWith(paramAsToken, ' p=')}">
    <c:set var="distinguishingSubmitName" value="${fn:substring(fn:substringAfter(paramAsToken, '='), 0, 3)}" scope="page" />
  </c:if>
</c:forTokens>
    <span class="float-right">
      <a class="ico" href="${project}">
        <img src="${initParam.urlDirectoryImmagini}/ico-home.png" class="ico-home" alt="Torna a elenco progetti" title="Torna a elenco progetti" />
      </a>
    </span>
    <form id="pcvision_form" name="pcv" action="#" method="post">
  	<ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
   		<li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Vision</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${stakeholder}${p.id}">Stakeholder</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${deliverable}${p.id}">Deliverable</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${risorse}${p.id}">Risorse</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rischi}${p.id}">Rischi</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${vincoli}${p.id}">Vincoli</a></li>
   		<li class="nav-item"><a class="nav-link" data-toggle="tab" href="${milestone}${p.id}">Milestone</a></li>
  	</ul>
  	<div class="tab-content responsive hidden-xs hidden-sm">
  	  <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
  	    <label for="pcv-situazione">Situazione attuale</label>
  		<br>
  		<textarea id="pcv-situazione" name="pcv-situazione" class="form-control" aria-label="With textarea" maxlength="8104" readonly>${p.situazioneAttuale}</textarea>
          <div class="charNum"></div>
          <br><br>
  	    <label for="pcv-descrizione">Descrizione del Progetto</label>
  		<br>
  		<textarea id="pcv-descrizione" name="pcv-descrizione" class="form-control" aria-label="With textarea" maxlength="8104" readonly>${p.descrizione}</textarea>
  		<div class="charNum"></div>
          <br><br>
  	    <label for="pcv-obiettivi">Obiettivi misurabili di Progetto</label>
  		<br>
  		<textarea id="pcv-obiettivi" name="pcv-obiettivi" class="form-control" aria-label="With textarea" maxlength="8104" readonly>${p.obiettiviMisurabili}</textarea>
  		<div class="charNum"></div>
          <br><br>
  	    <label for="pcv-minacce">Minacce</label>
  		<br>
  		<textarea id="pcv-minacce" name="pcv-minacce" class="form-control" aria-label="With textarea" maxlength="8104" readonly>${p.minacce}</textarea>
  		<div class="charNum"></div>
          <br><br>
          <div id="container-fluid">
            <div class="row">
              <div class="col-2">
              </div>
              <div class="col-8 text-center">
                <%@ include file="subPanel.jspf" %>
              </div>
              <div class="col-2">
                <span class="float-right">
                  <a class="ico" href="${stakeholder}${p.id}">
                    <img src="${initParam.urlDirectoryImmagini}/ico-forward.png" class="ico-home" alt="Vai a pagina Stakeholder" title="Vai a pagina Stakeholder" />
                  </a>
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
        $('#pcvision_form').validate ({
          rules: {
            'pcv-situazione': {
              minlength: offsetcharacter
            },
            'pcv-descrizione': {
              minlength: offsetcharacter
            },
            'pcv-obiettivi': {
              minlength: offsetcharacter
            },
          	'pcv-minacce': {
              minlength: offsetcharacter
            },
          }, 
          messages: {
            'pcv-situazione': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcv-descrizione': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcv-obiettivi': "Inserire almeno " + offsetcharacter + " caratteri.",
            'pcv-minacce': "Inserire almeno " + offsetcharacter + " caratteri.",
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