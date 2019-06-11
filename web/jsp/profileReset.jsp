    <%@ include file="pcURL.jspf" %>
    <div id="showPwd">
      <div id="successMessage" class="successPwd">
        <i class="far fa-check-circle"></i> La password &egrave; stata modificata con successo.<br />
        &Egrave; necessario comunicare all'utente la password modificata.
      </div>
      <hr class="separatore" />
      <h4>Password modificata: </h4>
      <hr class="separatore" />
      <div class="row">
        <div class="col-sm-2">
          <input type="text" class="form-control" value="${requestScope.password}" id="pwdMod" > 
        </div>
        <div class="col-sm-2"> 
          <button class="btn btn-success" onclick="copyText()">Copia password</button> 
        </div>
      </div>
    </div>
    <script type="text/javascript">
      function copyText() {
        /* Get the text field */
        var copyText = document.getElementById("pwdMod");
  
        /* Select the text field */
        copyText.select();
  
        /* Copy the text inside the text field */
        document.execCommand("copy");
      }
    </script>