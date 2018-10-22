<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>Project Charter</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.css">
</head>
<body>
<form action="/almalaurea/auth" method="post">
<!--<ul class="nav nav-tabs">
  <li class="active"><a href="#">Vision</a></li>
  <li><a href="#">Stakeholder</a></li>
  <li><a href="#">Del</a></li>
  <li><a href="#">Risorse</a></li>
  <li><a href="#">Rischi</a></li>
  <li><a href="#">Vincoli</a></li>
  <li><a href="#">Milestone</a></li>
</ul>-->

<nav>
  <div class="nav nav-tabs" id="nav-tab" role="tablist">
    <a class="nav-item nav-link active" id="nav-vision-tab" data-toggle="tab" href="#nav-vision" role="tab" aria-controls="nav-vision" aria-selected="true">Vision</a>
    <a class="nav-item nav-link" id="nav-stakeholder-tab" data-toggle="tab" href="#nav-stakeholder" role="tab" aria-controls="nav-stakeholder" aria-selected="false">Stakeholder</a>
    <a class="nav-item nav-link" id="nav-del-tab" data-toggle="tab" href="#nav-del" role="tab" aria-controls="nav-del" aria-selected="false">Del</a>
    <a class="nav-item nav-link" id="nav-risorse-tab" data-toggle="tab" href="#nav-risorse" role="tab" aria-controls="nav-risorse" aria-selected="false">Risorse</a>
    <a class="nav-item nav-link" id="nav-risorse-tab" data-toggle="tab" href="#nav-risorse" role="tab" aria-controls="nav-risorse" aria-selected="false">Risorse</a>
  </div>
</nav>
<input type="button" name="modifica" value="modifica">
<input type="submit" name="salva" value="salva">
<input type="reset" name="annulla" value="annulla>
</form>
</body>
</html>