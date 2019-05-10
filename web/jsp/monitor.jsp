<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="m" value="${requestScope.monitor}" scope="page" />
    <c:set var="years" value="2018,2019,2020,2021,2022" scope="page" />
    <hr class="separatore" />
    <span class="float-right">
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
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
    <c:if test="${sessionScope.usr.pmoAteneo}">
    <c:choose>
      <c:when test="${not m.open}">
      <br /><div class="avvisiTot">Questo monitoraggio &egrave; chiuso</div>
      <form id="monitor_start" name="ctrl-mon-s" action="" method="post">
        <span>
          <button type="submit" class="btn btn-success" name="start" id="mon-start">
            <i class="fas fa-play"></i> &nbsp;ATTIVA MONITORAGGIO
          </button>
        </span>
      </form>
      </c:when>
      <c:otherwise>
      <br /><div class="avvisiTot">Questo monitoraggio &egrave; aperto</div>
      <form id="monitor_end" name="ctrl-mon-e" action="" method="post">
        <span>
          <button type="submit" class="btn btn-success" name="end" id="mon-end">
            <i class="fas fa-square"></i> &nbsp;CHIUDI MONITORAGGIO
          </button>
        </span>
      </form>
      </c:otherwise>
    </c:choose>
    </c:if>
    <hr class="separatore" />
    <form id="monitor_form" name="miur" action="#" method="post">
      <div class="tab-content responsive hidden-xs hidden-sm">
        <div role="tabpanel" class="tab-pane active" id="tab-pcvision">
          <div class="charNum"></div>
          <hr class="separatore" />
          <div class="row">
            <div class="col-9">
              <label for="mon-d4"><strong>QUADRO D.4 Reclutamento del personale</strong></label><br />
              <textarea id="mon-d4" name="mon-d4" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD4}</textarea>
            </div>
            <div class="col-3">
              <div class="row">
                <div class="col-1"></div>
                <hr class="separatore" />
                <div class="col-10 substatus">
                  <h6 class="monospace">&nbsp;Allegati quadro D.4</h6>
                  <hr class="separatore" />
              <c:if test="${not empty m.allegatiD4}">
                <c:catch var="exception">
                  <c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/monitoraggio_d4/${param['dip']}" scope="page" />
                  <ul class="list-unstyled">
                  <c:forEach var="all" items="${m.allegatiD4}" varStatus="loop">
                    <c:set var="ext" value="${fn:substring(all.estensione, 1, fn:length(all.estensione))}" scope="page" />
                    <li>
                      <span>
                        <img src="${initParam.urlDirectoryImmagini}ico_${ext}.png" border="0" alt="${all.estensione}">
                      </span>
                      <a href="${localDocumentRoot}/${all.file}${all.estensione}" class="transition monospace">
                        <c:out value="${all.titolo}" escapeXml="false" />
                      </a>
                      <span class="file-data monospace">
                        (<c:out value="${ext}" />,&nbsp;<fmt:formatNumber type="number" value="${all.dimensione/1024}" maxFractionDigits ="2" />&nbsp;KB,&nbsp;<fmt:formatDate value="${all.data}" pattern="dd/MM/yyyy" />)
                      </span>
                    </li>
                  </c:forEach>
                  </ul>
                </c:catch>
                <c:out value="${exception}" />
              </c:if>
                  <div class="centerlayout" onclick="callerId='d4';">
                    <a class="btn btn-disabled" id="d4-all"><i class="fas fa-plus"></i> Aggiungi</a>
                  </div>
                  <br />
                </div>
              </div>
            </div>
          </div>
          <hr class="separatore" />
          <div class="row">
            <div class="col-9">
              <label for="mon-d5"><strong>QUADRO D.5 Infrastrutture</strong></label><br />
              <textarea id="mon-d5" name="mon-d5" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD5}</textarea>
            </div>
            <div class="col-3">
              <div class="row">
                <div class="col-1"></div>
                <hr class="separatore" />
                <div class="col-10 substatus">
                  <h6 class="monospace">&nbsp;Allegati quadro D.5</h6>
                  <hr class="separatore" />
              <c:if test="${not empty m.allegatiD5}">
                <c:catch var="exception">
                  <c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/monitoraggio_d5/${param['dip']}" scope="page" />
                  <ul class="list-unstyled">
                  <c:forEach var="all" items="${m.allegatiD5}" varStatus="loop">
                    <c:set var="ext" value="${fn:substring(all.estensione, 1, fn:length(all.estensione))}" scope="page" />
                    <li>
                      <span>
                        <img src="${initParam.urlDirectoryImmagini}ico_${ext}.png" border="0" alt="${all.estensione}">
                      </span>
                      <a href="${localDocumentRoot}/${all.file}${all.estensione}" class="transition">
                        <c:out value="${all.titolo}" escapeXml="false" />
                      </a>
                      <span class="file-data monospace">
                        (<c:out value="${ext}" />,&nbsp;<fmt:formatNumber type="number" value="${all.dimensione/1024}" maxFractionDigits ="2" />&nbsp;KB,&nbsp;<fmt:formatDate value="${all.data}" pattern="dd/MM/yyyy" />)
                      </span>
                    </li>
                  </c:forEach>
                  </ul>
                </c:catch>
                <c:out value="${exception}" />
              </c:if>
                  <div class="centerlayout" onclick="callerId='d5';">
                    <a class="btn btn-disabled" id="d5-all"><i class="fas fa-plus"></i> Aggiungi</a>
                  </div>
                  <br />
                </div>
              </div>
            </div>
          </div>
          <hr class="separatore" />
          <div class="row">
            <div class="col-9">
              <label for="mon-d6"><strong>QUADRO D.6 Premialit&agrave;</strong></label><br />
              <textarea id="mon-d6" name="mon-d6" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD6}</textarea>
            </div>
            <div class="col-3">
              <div class="row">
                <div class="col-1"></div>
                <hr class="separatore" />
                <div class="col-10 substatus">
                  <h6 class="monospace">&nbsp;Allegati quadro D.6</h6>
                  <hr class="separatore" />
              <c:if test="${not empty m.allegatiD6}">
                <c:catch var="exception">
                  <c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/monitoraggio_d6/${param['dip']}" scope="page" />
                  <ul class="list-unstyled">
                  <c:forEach var="all" items="${m.allegatiD6}" varStatus="loop">
                    <c:set var="ext" value="${fn:substring(all.estensione, 1, fn:length(all.estensione))}" scope="page" />
                    <li>
                      <span>
                        <img src="${initParam.urlDirectoryImmagini}ico_${ext}.png" border="0" alt="${all.estensione}">
                      </span>
                      <a href="${localDocumentRoot}/${all.file}${all.estensione}" class="transition">
                        <c:out value="${all.titolo}" escapeXml="false" />
                      </a>
                      <span class="file-data monospace">
                        (<c:out value="${ext}" />,&nbsp;<fmt:formatNumber type="number" value="${all.dimensione/1024}" maxFractionDigits ="2" />&nbsp;KB,&nbsp;<fmt:formatDate value="${all.data}" pattern="dd/MM/yyyy" />)
                      </span>
                    </li>
                  </c:forEach>
                  </ul>
                </c:catch>
                <c:out value="${exception}" />
              </c:if>
                  <div class="centerlayout" onclick="callerId='d6';">
                    <a class="btn btn-disabled" id="d6-all"><i class="fas fa-plus"></i> Aggiungi</a>
                  </div>
                  <br />
                </div>
              </div>
            </div>
          </div>
          <hr class="separatore" />
          <div class="row">
            <div class="col-9">
              <label for="mon-d7"><strong>QUADRO D.7 Attivit&agrave; didattiche di eleveta qualificazione</strong></label><br />
              <textarea id="mon-d7" name="mon-d7" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD7}</textarea>
            </div>
          <div class="col-3">
              <div class="row">
                <div class="col-1"></div>
                <hr class="separatore" />
                <div class="col-10 substatus">
                  <h6 class="monospace">&nbsp;Allegati quadro D.7</h6>
                  <hr class="separatore" />
              <c:if test="${not empty m.allegatiD7}">
                <c:catch var="exception">
                  <c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/monitoraggio_d7/${param['dip']}" scope="page" />
                  <ul class="list-unstyled">
                  <c:forEach var="all" items="${m.allegatiD7}" varStatus="loop">
                    <c:set var="ext" value="${fn:substring(all.estensione, 1, fn:length(all.estensione))}" scope="page" />
                    <li>
                      <span>
                        <img src="${initParam.urlDirectoryImmagini}ico_${ext}.png" border="0" alt="${all.estensione}">
                      </span>
                      <a href="${localDocumentRoot}/${all.file}${all.estensione}" class="transition">
                        <c:out value="${all.titolo}" escapeXml="false" />
                      </a>
                      <span class="file-data monospace">
                        (<c:out value="${ext}" />,&nbsp;<fmt:formatNumber type="number" value="${all.dimensione/1024}" maxFractionDigits ="2" />&nbsp;KB,&nbsp;<fmt:formatDate value="${all.data}" pattern="dd/MM/yyyy" />)
                      </span>
                    </li>
                  </c:forEach>
                  </ul>
                </c:catch>
                <c:out value="${exception}" />
              </c:if>
                  <div class="centerlayout" onclick="callerId='d7';">
                    <a class="btn btn-disabled" id="d7-all"><i class="fas fa-plus"></i> Aggiungi</a>
                  </div>
                  <br />
                </div>
              </div>
            </div>
          </div>
          <hr class="separatore" />
          <div class="row">
            <div class="col-9">
              <label for="mon-d8"><strong>QUADRO D.8 Modalit&agrave; e fasi del monitoraggio</strong></label><br />
              <textarea id="mon-d8" name="mon-d8" class="form-control" aria-label="With textarea" maxlength="22000" readonly>${m.quadroD8}</textarea>
            </div>
            <div class="col-3">
              <div class="row">
                <div class="col-1"></div>
                <hr class="separatore" />
                <div class="col-10 substatus">
                  <h6 class="monospace">&nbsp;Allegati quadro D.8</h6>
                  <hr class="separatore" />
              <c:if test="${not empty m.allegatiD8}">
                <c:catch var="exception">
                  <c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/monitoraggio_d8/${param['dip']}" scope="page" />
                  <ul class="list-unstyled">
                  <c:forEach var="all" items="${m.allegatiD8}" varStatus="loop">
                    <c:set var="ext" value="${fn:substring(all.estensione, 1, fn:length(all.estensione))}" scope="page" />
                    <li>
                      <span>
                        <img src="${initParam.urlDirectoryImmagini}ico_${ext}.png" border="0" alt="${all.estensione}">
                      </span>
                      <a href="${localDocumentRoot}/${all.file}${all.estensione}" class="transition">
                        <c:out value="${all.titolo}" escapeXml="false" />
                      </a>
                      <span class="file-data monospace">
                        (<c:out value="${ext}" />,&nbsp;<fmt:formatNumber type="number" value="${all.dimensione/1024}" maxFractionDigits ="2" />&nbsp;KB,&nbsp;<fmt:formatDate value="${all.data}" pattern="dd/MM/yyyy" />)
                      </span>
                    </li>
                  </c:forEach>
                  </ul>
                </c:catch>
                <c:out value="${exception}" />
              </c:if>
                  <div class="centerlayout" onclick="callerId='d8';">
                    <a class="btn btn-disabled" id="d8-all"><i class="fas fa-plus"></i> Aggiungi</a>
                  </div>
                  <br />
                </div>
              </div>
            </div>
          </div>
          <hr class="separatore" />
          <div class="charNum"></div>
          <br /><br />
          <div id="container-fluid">
            <div class="row">
              <div class="col-2">
                <span class="float-left">
                  <a class="btn btnNav" href="${project}">
                    <i class="fas fa-home"></i>
                    Progetti
                  </a>
                </span>
              </div>
              <div class="col-8 text-center">
              <c:if test="${requestScope.privileges and m.open}">
                <button class="btn btn-success" id="btnMod" name="modifica" onclick="modifyPart()">
                  <i class="far fa-edit"></i>
                  Modifica
                </button>
                <button type="submit" class="btn btn-success" name="${distinguishingSubmitName}" id="${distinguishingSubmitId}" disabled>
                  <i class="far fa-save"></i>
                  Salva
                </button>
              </c:if>
              <c:if test="${not m.open}">
                <div class="alert alert-warning">
                  <span class="ui-icon ui-icon-alert"></span>
                  Non &egrave; possibile editare questo monitoraggio in quanto 
                <c:choose>
                <c:when test="${param['y'] lt requestScope.theCurrentYear}">
                  &egrave; stato chiuso 
                </c:when>
                <c:otherwise>
                  non &egrave; stato ancora aperto
                </c:otherwise>
                </c:choose>
                  dallo Steering Committee.<br />
                  Per problemi o necessit&agrave; di aggiornamento, rivolgersi al <a href="mailto:stefano.fedeli@univr.it">PMO di Ateneo</a>.
                </div>
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
    <form id="note-form" method="post" action="file" enctype="multipart/form-data" class="modal">
      <script>
      $('div.centerlayout').click(function(){
        if (callerId == 'd4') {
          $("#mon-frame option[value=4]").attr('selected', 'selected');
        } else if (callerId == 'd5') {
          $("#mon-frame option[value=5]").attr('selected', 'selected');
        } else if (callerId == 'd6') {
          $("#mon-frame option[value=6]").attr('selected', 'selected');
        } else if (callerId == 'd7') {
          $("#mon-frame option[value=7]").attr('selected', 'selected');
        } else if (callerId == 'd8') {
          $("#mon-frame option[value=8]").attr('selected', 'selected');
        }
        $("#qd-id").attr('value', callerId);
      })
      </script>
      <input type="hidden" id="dip-id" name="dip-id" value="${param['dip']}" />
      <input type="hidden" id="q" name="q" value="${param['q']}" />
      <input type="hidden" id="y" name="y" value="${param['y']}" />
      <input type="hidden" id="mon-id" name="mon-id" value="${requestScope.monitor.id}" />
      <input type="hidden" id="qd-id" name="qd-id" value="callerId" />
      <h3 class="heading">Aggiungi un allegato</h3>
      <br />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Quadro monitoraggio
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">
          <select class="form-control" id="mon-frame" name="mon-frame">
            <option value="4">D.4 Reclutamento</option>
            <option value="5">D.5 Infrastrutture</option>
            <option value="6">D.6 Premialit&agrave;</option>
            <option value="7">D.7 Attivit&agrave; Didattiche</option>
            <option value="8">D.8 Modalit&agrave; e Fasi del Monitoraggio</option>
          </select>
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Titolo Documento
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="text" class="form-control" id="doc-name" name="doc-name" value="" placeholder="Inserisci un titolo documento">
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-5">
          <strong>
            Seleziona un file da caricare
            <sup>&#10039;</sup>:
          </strong>
        </div>
        <div class="col-sm-5">  
          <input type="file" name="file" size="60" placeholder="Inserisci un file da caricare"><br /><br /> 
        </div>
      </div>
      <hr class="separatore" />
      <div class="row">
        <button type="submit" class="btn btn-warning"  value="Upload"><i class="fas fa-file-upload"></i> Upload</button>
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
        
        $('textarea').keyup(function () {
          var len = $('#mon-d4').val().length + $('#mon-d5').val().length + $('#mon-d6').val().length + $('#mon-d7').val().length + $('#mon-d8').val().length;
          var dblength = 20000;
          $('.charNum').addClass('errorPwd');
          if(len >= dblength) {
            this.value = this.value.substring(0, dblength);
            $('.charNum').text(' limite raggiunto');
          } else {
            var chars = dblength - len;
            $('.charNum').text(chars + ' caratteri rimanenti');
          }
        });
      });
    </script>