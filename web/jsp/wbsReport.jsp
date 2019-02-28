<%@ include file="pcURL.jspf" %>
    <c:set var="prj" value="${requestScope.progetto}" scope="page" />
    <h4>Report dei Work Packages del progetto: <cite><c:out value="${prj.titolo}" /></cite></h4>
    <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${wbs}${prj.id}">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${wbs}${prj.id}" id="show_act">Attività</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Report</a></li>
    </ul>
    <hr class="separatore" />
    <form id="editReport_form" action="#" method="post">
    <c:choose>
      <c:when test="${not empty requestScope.wps}">
      <div class="table-responsive">
        <table class="table table-striped overviewSummary">
          <thead class="thead-light">
          <tr>
            <th scope="col">Work Package</th>
            <th scope="col">Attivit&agrave;</th>
          </tr>
          </thead>
          <tbody>
          <c:set var="status" value="" scope="page" />
          <c:forEach var="wp" items="${requestScope.wps}" varStatus="loop">
            <c:set var="status" value="${loop.index}" scope="page" />
            <tr>
              <td scope="row" id="nameColumn">
                <a href="${modAct}${p.id}&ida=${wp.id}">
                  <c:out value="${wp.nome}"/>
                </a>
              </td>
              <td>
            <c:forEach var="act" items="${wp.attivita}" varStatus="loop">
              <dl class="dl-horizontal">
                <dt>
                  <a href="${modAct}${p.id}&ida=${act.id}">
                    <c:out value="${act.nome}" />
                  </a>
                </dt>
                <dd>${act.stato.informativa}</dd>
              </dl>
            </c:forEach>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      </c:when>
      <c:otherwise>
      <div class="alert alert-danger">
        <strong>Spiacente 
          <c:out value="${sessionScope.usr.nome}" />
          <c:out value="${sessionScope.usr.cognome}" />.<br />
        </strong>
        <p>Non &egrave; stata trovata alcuna WBS con attivit&agrave; per questo progetto.</p>
      </div>
      </c:otherwise>
    </c:choose>
      <div id="container-fluid">
        <div class="row">
          <div class="col-2">
            <span class="float-left">
              <a class="btn btn-primary" href="${wbs}${p.id}">Chiudi</a>
            </span>
          </div>
          <div class="col-8 text-center">
            <a href="${addAct}${p.id}" class="btn btn-primary" id="add-act">Esporta</a>
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
      });
    </script>
    <script>
      /*function checkUrl() {
        if ()
      }*/
    </script>