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
        <c:param name="p" value="ate" />
        <c:param name="y" value="${year}" />
      </c:url>
      <li class="nav-item"><a class="nav-link ${active}" data-toggle="tab" href="${monTab}">${year}</a></li>
    </c:forTokens>
    </ul>
    <hr class="separatore" />
    <div class="tab-content responsive">
      <div role="tabpanel" class="tab-pane active" id="tab-monate">
        <div class="row">
          <div class="col-12">
            <strong><c:out value="${m.descrizione}" /></strong><br />
            <c:out value="${m.informativa}" />
          </div>
        </div>
        <hr class="separatore" />
        <h4 class="avvisiTot">&nbsp;Allegati</h4>
        <div class="row">
          <div class="col-12 substatus avvisiTot marginBoth">
          <c:choose>
            <c:when test="${not empty m.allegati}">
              <br />
              <c:catch var="exception">
                <c:set var="localDocumentRoot" value="${initParam.urlDirectoryDocumenti}/upload/monitoraggio_all/" scope="page" />
                <ul class="list-unstyled marginLeft">
                <c:forEach var="all" items="${m.allegati}" varStatus="loop">
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
              <c:if test="${not empty exception}">
                <div class="alert alert-danger">
                  <c:out value="${exception}" />
                </div>
              </c:if>
            </c:when>
            <c:otherwise>
              <div class="alert alert-warning">
                Non sono stati ancora caricati documenti.
              </div>
            </c:otherwise>
          </c:choose>
            <br />
          </div>
        </div>
      </div>
    </div>
    <hr class="separatore" />
    <c:if test="${sessionScope.usr.pmoAteneo}">
      <div class="centerlayout">
        <a href="#all-form" class="btn btn-success" id="add-all" rel="modal:open">
          <i class="fas fa-plus"></i> Aggiungi
        </a>
      </div>
    </c:if>
    <form id="all-form" method="post" action="file" enctype="multipart/form-data" class="modal">
      <input type="hidden" id="q" name="q" value="${param['q']}" />
      <input type="hidden" id="p" name="p" value="${param['p']}" />
      <input type="hidden" id="y" name="y" value="${param['y']}" />
      <input type="hidden" id="mon-id" name="mon-id" value="${m.id}" />
      <h3 class="heading">Aggiungi un allegato</h3>
      <br />
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