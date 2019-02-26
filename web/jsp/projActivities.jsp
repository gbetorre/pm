<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ include file="pcURL.jspf" %>
    <h4>Attivit&agrave; del progetto <c:out value="${p.titolo}" /></h4>
    <hr class="separatore" />
    <form id="editAct_form" action="#" method="post">
    <c:choose>
      <c:when test="${not empty requestScope.attivita}">
      <table class="table table-bordered table-hover">
        <thead class="thead-light">
        <tr>
          <th scope="col" width="2%"></th>
          <th scope="col" width="*">Nome</th>
          <th scope="col" width="10%">Data inizio</th>
          <th scope="col" width="10%">Data fine</th>
          <th scope="col" width="20%">Stato effettivo</th>
          <th scope="col" width="4%"><div class="text-center">Milestone</div></th>
        </tr>
        </thead>
        <tbody>
        <c:set var="status" value="" scope="page" />
        <c:forEach var="act" items="${requestScope.attivita}" varStatus="loop">
          <c:set var="stile" value="" scope="page" />
          <c:set var="testo" value="" scope="page" />
          <c:catch var="exception">
          <c:choose>
            <c:when test="${act.stato.id eq 1}">
              <c:set var="stile" value="table-light" scope="page" />
              <c:set var="testo" value="" scope="page" />
            </c:when>
            <c:when test="${act.stato.id eq 4}">
              <c:set var="stile" value="table-warning" scope="page" />
              <c:set var="testo" value="Le attivit&agrave; <cite>in realizzazione</cite> sono attivit&agrave;<br> in stato IN PROGRESS,<br> con la DATA FINE nel futuro<br> e la DATA FINE EFFETTIVA vuota." scope="page" />
            </c:when>
            <c:when test="${act.stato.id eq 5}">
              <c:set var="stile" value="bg-workingOn" scope="page" />
              <c:set var="testo" value="Le attivit&agrave; <cite>in ritardo</cite> sono attivit&agrave;<br> in stato APERTA o IN PROGRESS,<br> con la DATA FINE nel passato<br> e la DATA FINE EFFETTIVA vuota." scope="page" />
            </c:when>
            <c:when test="${act.stato.id eq 6}">
              <c:set var="stile" value="bg-success" scope="page" />
              <c:set var="testo" value="Le attivit&agrave; <cite>chiuse in anticipo</cite> sono attivit&agrave;<br> in stato CHIUSO,<br> con la DATA FINE EFFETTIVA che precede sia la DATA FINE PREVISTA sia la data odierna." scope="page" />
            </c:when>
            <c:when test="${act.stato.id eq 7}">
              <c:set var="stile" value="table-success" scope="page" />
              <c:set var="testo" value="Le attivit&agrave; <cite>chiuse in ritardo</cite> sono attivit&agrave;<br> in stato CHIUSO,<br> con la DATA FINE EFFETTIVA che precede la data odierna ma &egrave; successiva alla DATA FINE PREVISTA." scope="page" />
            </c:when>
            <c:when test="${act.stato.id eq 10}">
              <c:set var="stile" value="table-danger" scope="page" />
              <c:set var="testo" value="Le attivit&agrave; in stato inconsistente tipicamente sono attivit&agrave; in stato CHIUSO ma con la DATA FINE EFFETTIVA vuota." scope="page" />
            </c:when>
          </c:choose>
          </c:catch>
          <c:out value="${exception}" />
          <c:set var="status" value="${loop.index}" scope="page" />
          <input type="hidden" id="act-id${status}" name="act-id${status}" value="<c:out value="${act.id}"/>">
          <tr class="${pageScope.stile}">
            <td scope="row" id="radioColumn" class="success">
              <input type="radio" id="act-${act.id}" name="act-select" value="${act.id}">
            </td>
            <td scope="row" id="nameColumn" class="success">
              <a href="${modAct}${p.id}&ida=${act.id}">
                <c:out value="${act.nome}"/>
              </a>
            </td>
            <td scope="row">
              <fmt:formatDate value='${act.dataInizio}' pattern='dd/MM/yyyy' />
            </td>
            <td scope="row">
              <fmt:formatDate value='${act.dataFine}' pattern='dd/MM/yyyy' />
            </td>
            <td scope="row">
            <c:choose>
              <c:when test="${not empty pageScope.testo}">
              <a href="javascript:popupWindow('Note','popup1',true,'${testo}');" class="helpInfo">
                <c:out value="${act.stato.informativa}" escapeXml="false" />
              </a>
              </c:when>
              <c:otherwise>
                <c:out value="${act.stato.informativa}" escapeXml="false" />
              </c:otherwise>
            </c:choose>
            </td>
            <td scope="row">
              <c:choose>
                <c:when test="${act.milestone}">
                  <div class="form-check text-center">
                    <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" checked disabled>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="form-check text-center">
                    <input type="checkbox" class="form-check-input" id="act-milestone${status}" name="act-milestone${status}" disabled>
                  </div>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
      </c:when>
      <c:otherwise>
      <div class="alert alert-danger">
        <p>Non &egrave; stata trovata alcuna attivit&agrave; associata al progetto.</p>
      </div>
      </c:otherwise>
    </c:choose>
      <div id="container-fluid">
        <div class="row">
          <div class="col-2">
            <span class="float-left">
              <a class="btn btn-primary" href="${project}">Chiudi</a>
            </span>
          </div>
          <div class="col-8 text-center">
            <%@ include file="subPanel.jspf" %>
          </div>
        </div>
      </div>
    </form>
    <div id="popup1" class="popup">
      <div id="popup1Under" class="popupundertitle">
        <div id="titolopopup1" class="popuptitle" ></div>
        <div id="titolopopup1Under">
          <a href="Javascript:popupWindow('','popup1',false,'');"><img src="web/img/close-icon.gif" border="0" width="15" height="15" alt="Chiudi" title="Chiudi" /></a>
        </div>
      </div>
      <div class="popupbody" id="popup1Text" ></div>
    </div>
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
 // --------------------------  POP-UP ( incarichi)---------------
 // Trap Mouse Position 
 //Copyright 2006,2007 Bontrager Connection, LLC
   var cX = 0;var cY = 0;var rX = 0;var rY = 0;
   
   function UpdateCursorPosition(e){
     cX = e.pageX; cY = e.pageY;
   }
   
   function UpdateCursorPositionDocAll(e){
     cX = event.clientX; cY = event.clientY;
   }
   
   if(document.all) {
     document.onmousemove = UpdateCursorPositionDocAll;
   } else {
     document.onmousemove = UpdateCursorPosition;
   }
   
   function AssignPosition(d, tipo) {
     if(self.pageYOffset) {
       rX = self.pageXOffset; rY = self.pageYOffset;
       }
     else if(document.documentElement && document.documentElement.scrollTop) {
       rX = document.documentElement.scrollLeft; rY = document.documentElement.scrollTop;
       }
     else if(document.body) {
       rX = document.body.scrollLeft; rY = document.body.scrollTop;
       }
     if(document.all) {
       cX += rX; cY += rY;
       }
     if (tipo == "Note") {
       // Sposto + a sinistra, in quanto verrebbe in parte nascosto a destra
       d.style.left = (cX-100) + "px"; d.style.top = (cY+10) + "px";
     } else {
       d.style.left = (cX+10) + "px"; d.style.top = (cY+10) + "px";
     }
   }
   
   function HideContent(d) {
     if(d.length < 1) { return; }
     document.getElementById(d).style.display = "none";
   }
   
   function ShowContent(d, tipo) {
     if(d.length < 1) { return; }
     var dd = document.getElementById(d);
     AssignPosition(dd, tipo);
     dd.style.display = "block";
     dd.style.width= "230px";
     dd.style.height = "170px";
   }
   
   function ReverseContentDisplay(d) {
     if(d.length < 1) { return; }
     var dd = document.getElementById(d);
     AssignPosition(dd);
     if(dd.style.display == "none") { dd.style.display = "block"; }
     else { dd.style.display = "none"; }
   }
   //\Trap Mouse Position ------------------------------------------------
   
   function testClickPopup(nome) {
     // Chiusura
     var myname = nome.parentElement.name;
     if (!(myname == undefined)) {
       if (!(myname.indexOf('popup1')>=0)) {
         HideContent('popup1');
       }
     } else {
       var myname = nome.name;
       if (!(myname == undefined)) {
         if (!(myname.indexOf('popup1')>=0)) {
           HideContent('popup1');
         }
       } else {
         // Nasconde a prescindere
         HideContent('popup1');
       }
     }
   }

   function popupWindow(tit,o,d,t) {
       // o - Object to display.
       // d - Display, true =  display, false = hide
       // t - Text to display in the popup
       var obj = document.getElementById(o);
       var iltitolo = document.getElementById("titolopopup1");
       var contenuto = document.getElementById("popup1Text");
       
       if(d) {
           /*
           obj.style.display = 'block';
           obj.style.visibility = 'visible';
           iltitolo.innerHTML = tit;
           contenuto.innerHTML = t;
           obj.style.width= "350px";
           obj.style.height = "200px";
           obj.style.left = ((screen.availWidth - 700)/2);
           obj.style.top = ((screen.availHeight - 400)/2);
           */
           ShowContent(obj.id,tit);
           iltitolo.innerHTML = tit;
           contenuto.innerHTML = unescape(t);
       } else {
           /*
           contenuto.innerHTML = '';
           obj.style.display = 'none';
           obj.style.visibility = 'hidden';
           */
           contenuto.innerHTML = "";
           HideContent(obj.id);
         }
     }
    </script>