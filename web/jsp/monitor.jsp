<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="m" value="${requestScope.monitor}" scope="page" />
<c:set var="paramsAsTokens" value="${param}" scope="page" />
<c:set var="distinguishingSubmitName" value="" scope="page" />
<c:forTokens var="paramAsToken" items="${paramsAsTokens}" delims=",">
  <c:if test="${fn:startsWith(paramAsToken, ' p=')}">
    <c:set var="distinguishingSubmitName" value="${fn:substring(fn:substringAfter(paramAsToken, '='), 0, 3)}" scope="page" />
  </c:if>
</c:forTokens>
    <c:set var="years" value="2018,2019,2020,2021,2022" scope="page" />
    <form id="monitor_form" name="miur" action="#" method="post">
      <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <c:forTokens var="year" items="${years}" delims=",">
        <c:set var="active" value="" scope="page" />
        <c:if test="${year eq param['y']}">
          <c:set var="active" value="active tabactive" scope="page" />
        </c:if>
        <c:url var="monTab" context="${initParam.appName}" value="/" scope="page">
          <c:param name="q" value="mon" />
          <c:param name="y" value="${year}" />
          <c:param name="dip" value="${param['dip']}" />
        </c:url>
        <li class="nav-item"><a class="nav-link ${active}" data-toggle="tab" href="${monTab}">${year}</a></li>
      </c:forTokens>
      </ul>
      <hr class="separatore" />
      <div class="tab-content responsive hidden-xs hidden-sm">
        <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
          <label for="mon-d4">QUADRO D.4 Reclutamento del personale</label><br />
          <textarea id="mon-d4" name="mon-d4" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD4}</textarea>
          <div class="charNum"></div>
          <br /><br />
          <label for="mon-d5">QUADRO D.5 Infrastrutture</label><br />
          <textarea id="mon-d5" name="mon-d5" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD5}</textarea>
          <div class="charNum"></div>
          <br /><br />
          <label for="mon-d6">QUADRO D.6 Premialit&agrave;</label><br />
          <textarea id="mon-d6" name="mon-d6" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD6}</textarea>
          <div class="charNum"></div>
          <br /><br />
          <label for="mon-d7">QUADRO D.7 Attivit&agrave; didattiche di eleveta qualificazione</label><br />
          <textarea id="mon-d7" name="mon-d7" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD7}</textarea>
          <div class="charNum"></div>
          <br /><br />
          <label for="mon-d8">QUADRO D.8 Modalit&agrave; e fasi del monitoraggio</label><br />
          <textarea id="mon-d8" name="mon-d8" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD8}</textarea>
          <div class="charNum"></div>
          <br /><br />
          <div id="container-fluid">
            <div class="row">
              <div class="col-2">
                <span class="float-left">
                  <a class="btn btn-primary" href="${project}">Torna all'elenco progetti</a>
                </span>
              </div>
              <div class="col-8 text-center">
              <c:if test="${requestScope.privileges}">
                <input type="button" class="btn btn-primary" name="modifica" value="Modifica" onclick="modify()">
                <input type="submit" class="btn btn-primary" name="${distinguishingSubmitName}" id="${distinguishingSubmitId}" value="Salva">
              </c:if>
              </div>
              <div class="col-2">
                &nbsp;
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
    <script>
      var offsetcharacter = 5;
      $(document).ready(function () {
        $('#monitor_form').validate ({
          rules: {
            'mon-d4': {
              minlength: offsetcharacter
            },
            'mon-d5': {
              minlength: offsetcharacter
            },
            'mon-d6': {
              minlength: offsetcharacter
            },
          	'mon-d7': {
              minlength: offsetcharacter
            },
            'mon-d8': {
              minlength: offsetcharacter
            },
          }, 
          messages: {
            'mon-d4': "Inserire almeno " + offsetcharacter + " caratteri.",
            'mon-d5': "Inserire almeno " + offsetcharacter + " caratteri.",
            'mon-d6': "Inserire almeno " + offsetcharacter + " caratteri.",
            'mon-d7': "Inserire almeno " + offsetcharacter + " caratteri.",
            'mon-d8': "Inserire almeno " + offsetcharacter + " caratteri.",
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
            $(this).next('div').text(' limite raggiunto');
          } else {
            var chars = dblength - len;
            $(this).next('div').text(chars + ' caratteri rimanenti');
          }
        });
      });
    </script>