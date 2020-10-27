<%@ page contentType="text/html;" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:catch var="exception">
  <form action="${initParam.appName}/auth" method="post">
    <br />
    <div class="row justify-content-center">
      <div class="col-3 text-center">
        <img class="logo" src="${initParam.urlDirectoryImmagini}/logo1.png" />
      </div>
    </div>
    <br />
    <div class="row justify-content-center">
      <div class="col text-center">
        <h1>Progetti di eccellenza</h1>
        <hr class="separatore" />
        <h3>Progetti del piano delle performance</h3>
        <br />
        <div class="container justify-content-center">
          <br /><br /> 
          <div class="row justify-content-center">
            <div class="col-11">
              <div class="input-group">
                <input id="usr" type="text" class="form-control" name="usr" placeholder="Username">
              </div>
              <br />
              <div class="input-group">
                <input id="pwd" type="password" class="form-control" name="pwd" placeholder="Password">
              </div>
            </div>
          </div>
          <br />
          <div class="row justify-content-center">
            <div class="col text-center">
              <input type="submit" class="btn btn-primary" value="LOGIN">
            </div>
          </div>
        </div>
        <%--
        <hr class="riga" />
        <div class="row p-3 my-3 bg-info text-white">
          <h4>
            Al momento l'applicazione di gestione dei progetti &egrave; chiusa per 
            le normali elaborazioni legate al monitoraggio dei progetti del piano
            integrato delle performance 2020.
            <hr class="separatore" />
            Non appena risolta la fase di elaborazione, verr&agrave; riaperta
            alla consultazione e all'inserimento ove previsto.
          </h4>
        </div>
        <hr class="riga" />
        --%>
      </div>
    </div>
  </form>
</c:catch>
  <p style="color:red;"><c:out value="${exception}" /></p>
