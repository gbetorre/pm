<%@ include file="pcURL.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
    <form id="newWbs_form" action="#" method="post">
        <hr class="separatore" />
      <h3>Inserisci una nuova WBS:</h3>
      <br />
      <c:set var="skills" value="" scope="page" />
      <div class="row">
        <div class="col-sm-5">
          WBS padre (se &eacute; presente): 
          <sup>&#10039;</sup>
        </div>
        <div class="col-sm-5">
          <select class="form-control" id="wbs-idpadre" name="wbs-idpadre">
          <option value="">Nessuna wbs padre</option>
          <c:forEach var="singleWbs" items="${requestScope.wbs}" varStatus="status">
            <option value="${singleWbs.id}">${singleWbs.nome}</option>
          </c:forEach>
          </select>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Nome WBS</div>
        <div class="col-sm-5">
          <input type="text" class="form-control" id="wbs-name" name="wbs-name" value="">
          <div class="charNum"></div> 
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Descrizione della WBS</div>
        <div class="col-sm-5">
          <textarea class="form-control" name="wbs-descr"></textarea>
          <div class="charNum"></div>
        </div>
      </div>
      <br />
      <div class="row">
        <div class="col-sm-5">Workpackage</div>
        <div class="col-sm-5">
          &nbsp;&nbsp;&nbsp;
          <input type="checkbox" class="form-check-input" id="wbs-workpackage" name="wbs-workpackage">
        </div>
      </div>
      <hr class="separatore" />
      <a href="${wbs}${p.id}" class="btn btn-primary">Chiudi</a>
      <%@ include file="subPanel.jspf" %>
    </form>
    <script type="text/javascript">
      $(document).ready(function () {
          $('#btn').click(function () {
              window.opener.location.reload(true);
              window.self.close();
          });
      });
    </script>
    <script type="text/javascript">
    var offsetcharacter = 5;
    $(document).ready(function () {
      $('#newWbs_form').validate ({
        rules: {
          'wbs-name': {
            required: true,
            minlength: offsetcharacter
          },
          'wbs-descr': {
            required: true,
            minlength: offsetcharacter
          },
        }, 
        messages: {
          'wbs-name': "Inserire il nome della WBS",
          'wbs-descr': "Inserire una descrizione per la WBS"
        },
        submitHandler: function (form) {
          return true;
        }
      });

      $('textarea').keyup(function (e) {
        var len = $(this).val().length;
        var dblength = 8104;
        if(len >= dblength) {
          this.value = this.value.substring(0, dblength);
          $(this).next('div').text(' è stato raggiunto il limite di caratteri');
        } else {
          var chars = dblength - len;
          $(this).next('div').text(chars + ' caratteri rimanenti');
        }
      });
      
      $('#wbs-name').keyup(function (e) {
        var len = $(this).val().length;
        var dblength = 1024;
        if(len >= dblength) {
          this.value = this.value.substring(0, dblength);
          $(this).next('div').text(' è stato raggiunto il limite di caratteri');
        } else {
          var chars = dblength - len;
          $(this).next('div').text(chars + ' caratteri rimanenti');
        }
      });
    });
    </script>