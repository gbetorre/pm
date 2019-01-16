<%@ include file="pcURL.jspf" %>
<form action="${updWbs}" method="post">
  <h2>WBS del progetto <strong><c:out value="${p.titolo}" /></strong></h2>
  <ul class="nav nav-tabs responsive hidden-xs hidden-sm" role="tablist" id="tabs-0">
    <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">WBS</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Attività</a></li>
    <li class="nav-item"><a class="nav-link" data-toggle="tab" href="">Report</a></li>
  </ul>
  <div class="tab-content responsive hidden-xs hidden-sm">
    <div role="tabpanel" class="tab-pane active" id="tab-wbs">
      <br />
      <table class="table table-bordered table-hover">
        <thead class="thead-light">
        <tr>
          <th scope="col"></th>
          <th scope="col">Nome</th>
          <th scope="col">Descrizione</th>
          <th scope="col"><div class="text-center">Workpackage</div></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="status" value="" scope="page" />
        <c:forEach var="wbs" items="${requestScope.allWbs}" varStatus="loop">
          <c:set var="status" value="${loop.index}" scope="page" />
          <tr>
            <td scope="col">
              <input type="radio" id="wbs-<c:out value="${wbs.id}"/>" name="wbs-select" value="<c:out value="${wbs.id}"/>"/>
              <input type="hidden" id="wbs-id${status}" name="wbs-id${status}" value="<c:out value="${wbs.id}"/>">
            </td>
            <td scope="col">
              <c:out value="${wbs.nome}" />
            </td>
            <td scope="col">
              <c:out value="${wbs.descrizione}"/>
            </td>
            <td scope="col">
              <c:choose>
                <c:when test="${wbs.workPackage}">
                  <div class="form-check text-center">
                    <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" checked disabled>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="form-check text-center">
                    <input type="checkbox" class="form-check-input" id="wbs-workpackage${status}" name="wbs-workpackage${status}" disabled>
                  </div>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      <input type="hidden" id="wbs-loop-status" name="wbs-loop-status" value="<c:out value="${status}"/>">
      <div id="container-fluid">
        <hr class="separatore" />
        <div class="row">
          <div class="col-2">  
            <span class="float-left">
              <a class="btn btn-primary" href="${project}${p.id}">Chiudi</a>
            </span>
          </div>
          <div class="col-8 text-center">
            <a class="btn btn-primary" href="${addWbs}${p.id}" id="add-wbs">Aggiungi</a>
            <a class="btn btn-primary" href="" id="mod-wbs" onclick="selectionEdit('WBS')">Modifica</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
<script type="text/javascript">
  $(document).ready(function() {
    $("input[type='radio']").on('change', function() {
      var $radioValue = $("input[name='wbs-select']:checked").val();
      //alert($radioValue);
      //var $modActUrl = $('#mod-act').attr('href', 'pippo'); //"${modAct}${p.id}&idAct="$(this).val()
      var $modWbsUrl = '<c:out value="${modWbs}${p.id}" escapeXml="false" />' + "&idw=" + $(this).val();
      //$('#mod-act').attr('href', $(this).val());
      $('#mod-wbs').attr('href', $modWbsUrl);
    });
  });
</script>