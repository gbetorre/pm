<%@ include file="pcURL.jspf" %>
    <c:set var="wp" value="" scope="page" />
    <c:forEach var="workPackage" items="${requestScope.wbs}" begin="0" end="0">
      <c:set var="wp" value="${workPackage}" scope="page" />
    </c:forEach>
    <h4>Attivit&agrave; della WBS <c:out value="${wp.nome}" /></h4>
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${wbs}${p.id}">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafico}${p.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="javascript:alert('Occorre selezionare una WBS per indicarne le attività')" id="show_act">Attività</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${rep}${p.id}">Report</a></li>
    </ul>
    <hr class="separatore" />
    <form id="editWbsAct_form" action="#" method="post">
    <c:choose>
      <c:when test="${not empty requestScope.attivita}">
      <table class="table table-bordered table-hover" id="actOnWbs">
        <thead class="thead-light">
        <tr>
          <th scope="col"></th>
          <th scope="col">Nome</th>
          <th scope="col">Descrizione</th>
          <th scope="col"><div class="text-center">Milestone</div></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="status" value="" scope="page" />
        <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <input type="hidden" id="act-id${status}" name="act-id${status}" value="<c:out value="${act.id}"/>">
          <tr>
            <td scope="row" id="radioColumn">
              <input type="radio" id="act-${act.id}" name="act-select" value="${act.id}">
            </td>
            <td scope="row" id="nameColumn">
              <a href="${modAct}${p.id}&ida=${act.id}">
                <c:out value="${act.nome}"/>
              </a>
            </td>
            <td scope="row">
              <c:out value="${act.descrizione}"/>
            </td>
            <td scope="row">
              <input type="hidden" value="${act.milestone}" />
              <c:choose>
                <c:when test="${act.milestone}">
                  <div class="form-check text-center">
                    <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" checked disabled>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="form-check text-center">
                    <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" disabled>
                  </div>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      </c:when>
      <c:otherwise>
      <div class="alert alert-danger">
        <p>Non &egrave; stata trovata alcuna attivit&agrave; associata a questa WBS.</p>
      </div>
      </c:otherwise>
    </c:choose>
      <div id="container-fluid">
        <div class="row">
          <div class="col-2">
            <span class="float-left">
              <a class="btn btnNav" href="${project}">Chiudi</a>
            </span>
          </div>
          <div class="col-8 text-center">
            <a href="${addAct}${p.id}" class="btn btn-primary" id="add-act">Aggiungi</a>
            <a href="" class="btn btn-primary" id="mod-act" onclick="selectionEdit('Attivit&agrave;')">Modifica</a>
            <input type="submit" class="btn btn-primary" name="elimina" value="Elimina">
          </div>
        </div>
        <br />
        <div class="row">
          <div class="col-2">
            <span class="float-left">
              <a class="btn btnNav" href="${wbs}${p.id}">Torna a elenco WBS</a>
            </span>
          </div>
        </div>
      </div>
    </form>
    <script type="text/javascript">
      $(document).ready(function() {
        $("#radioColumn, #nameColumn").click(function () {
          $('.selected').removeClass('selected');
          var trElement = $(this).parent();
          $(this).parent().addClass('selected');
          var tdElement = $("td", $(this).parent());
          $("input[name='act-select']", tdElement).attr("checked", true);
          var $radioValue = $("input[name='act-select']:checked").val();
          var $modActUrl = '<c:out value="${modAct}${p.id}" escapeXml="false" />' + "&ida=" + $radioValue;
          $('#mod-act').attr('href', $modActUrl);
        });
        $("#actOnWbs").DataTable();
      });
    </script>
    <script>
      /*function checkUrl() {
        if ()
      }*/
    </script>