<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="#" method="post">

	<h1>Elenco progetti</h1>
	<div class="row">
		<div class="col-sm-1">ID</div>
		<div class="col-sm-5">Nome</div>
		<div class="col-sm-2">
		    Funzione
		</div>
	</div>
		
	<div class="row">
  <c:forEach var="prj" items="${requestScope.progetti}" varStatus="loop">
    <div class="col-sm-1">id progetto <c:out value="${prj.id}" /></div>
    <div class="col-sm-5">NomeProgetto <c:out value="${prj.titolo}" /></div>
    <div class="col-sm-2">
        <select class="form-control" id="azione">
          <option>Project Charter</option>
          <option>Status</option>
          <option>WBS</option>
          <option>Attivit&agrave;</option>
          <option>Scheda</option>
        </select>
    </div>
  </c:forEach>		
	</div>
	
	<br><br>
	<input type="submit" name="visualizza" value="Visualizza">
	<input type="reset" name="annulla" value="Annulla">

</form>