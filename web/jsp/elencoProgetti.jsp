<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="#" method="post">

	<h1>Elenco progetti</h1>
	<table class="table table-hover">
		<thead class="thead-light">
      <tr>
        <th scope="col">ID</th>
        <th scope="col">Titolo</th>
        <th scope="col">Stato</th>
        <th scope="col">Funzioni</th>
      </tr>
    </thead>
		<tbody>
    	<c:forEach var="prj" items="${requestScope.progetti}" varStatus="loop">
        <tr>
          <th scope="row"><c:out value="${prj.id}" /></th>
          <th scope="row"><a href="#"><c:out value="${prj.titolo}" /></a></th>
          <th scope="row"><c:out value="${prj.statoProgetto.getNome()}" /></th>
          <th scope="row">
            <div class="container">
              <a href="#"  class="btn btn-primary">Project Charter</a>
              <!-- <a href=""  class="btn btn-primary">Status</a> -->
              <a href="#"  class="btn btn-primary">WBS</a>
              <a href="#"  class="btn btn-primary">Attivit&agrave;</a>
            </div>
          </th>
        </tr>
      </c:forEach>		
  	</tbody>
  </table>
	<br><br>
	<!-- <input type="submit" name="visualizza" value="Visualizza">
  <input type="reset" name="annulla" value="Annulla"> -->

</form>