<%@ page contentType="text/html;" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><body class="masthead">  <form action="${initParam.appName}/auth" method="post">    <br />    <div class="row justify-content-center">      <div class="col-3 text-center">        <img class="logo" src="${initParam.urlDirectoryImmagini}/logo1.png" />      </div>    </div>    <br />    <div class="row justify-content-center">      <div class="col text-center">        <h1>Progetti di eccellenza</h1>        <div class="container justify-content-center">          <br /><br />          <div class="row justify-content-center">            <div class="col-11">              <div class="input-group">                <input id="usr" type="text" class="form-control" name="usr" placeholder="Username">              </div>              <br />              <div class="input-group">                <input id="pwd" type="password" class="form-control" name="pwd" placeholder="Password">              </div>            </div>          </div>          <br />          <div class="row justify-content-center">            <div class="col text-center">              <input type="submit" class="btn btn-primary" value="LOGIN">            </div>          </div>        </div>      </div>    </div>  </form>  <p style="color:red;"><c:out value="${requestScope.msg}" /></p></body>