<%@ page contentType="text/html;" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html><html><head><title>Login</title></head><body><form action="/almalaurea/auth" method="post"><div class="text-center">  <h1>Login</h1>  <p>Username:<input type="text" name="usr"></p>    <p>Password:<input type="password" name="pwd"></p>    <input type="submit" class="btn btn-primary" value="Login">  <a class="btn btn-danger" href="/almalaurea/auth?q=logout">Esci</a></div></form><p style="color:red;"><c:out value="${requestScope.msg}" /></p></body></html>