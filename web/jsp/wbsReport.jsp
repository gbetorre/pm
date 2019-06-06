<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="pcURL.jspf" %>
    <c:set var="prj" value="${requestScope.progetto}" scope="page" />
    <h4>Report dei Work Packages del sotto progetto <strong><c:out value="${prj.titolo}" /></strong></h4>
    <span class="float-right">
      <a class="btn btnNav" href="${urlWbs}${p.id}">
        <i class="fas fa-undo"></i> WBS
      </a>
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${urlWbs}${prj.id}">WBS</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${grafico}${prj.id}">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" id="show_act">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Report</a></li>
    </ul>
    <hr class="separatore" />
    <div class="table-responsive">
      <table class="table table-striped thin">
        <thead>
          <tr class="thin reportHead">
            <th width="*" id="facility_header">Workpackage<br /><br /></th>
            <th width="50%" id="activity_header">Attivit&agrave;<br /><br /></th>
            <th width="5%" id="last_header">Durata prevista</th>
            <th width="5%" id="realLast_header">Durata effettiva</th>
            <th id="start_header">Data inizio</th>
            <th id="end_header">Data fine</th>
            <th id="realStart_header">Inizio effettivo</th>
            <th id="realEnd_header">Fine effettiva</th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="wp" items="${requestScope.wps}" varStatus="loop">
          <c:set var="status" value="${loop.index + 1}" scope="page" />
          <tr>
            <td colspan="8">
              <span class="monospace"><c:out value="${status}" /></span>&nbsp;
              <a href="${modWbs}${p.id}&idw=${wp.id}">
                <strong><em><c:out value="${wp.nome}"/></em></strong>
              </a>
            </td>
          </tr>
          <c:forEach var="act" items="${wp.attivita}" varStatus="innerLoop">
            <c:set var="innerStatus" value="${status}.${innerLoop.index + 1}" scope="page" />
             <tr>
              <td>&nbsp;</td>
               <td scope="row" id="nameColumn" class="success bgAct${act.stato.id} bgFade">
                <span class="monospace"><c:out value="${innerStatus}" /></span>&nbsp;
                <a href="${modAct}${p.id}&ida=${act.id}" title="${act.nome}">
                <c:choose>
                  <c:when test="${fn:length(act.nome) gt 35}">
                    <c:out value="${fn:substring(act.nome, 0, 35)} ..." />
                  </c:when>
                  <c:otherwise>
                    <c:out value="${act.nome}" />
                  </c:otherwise>
                </c:choose>
                </a>
              </td>
              <td><c:if test="${act.guPrevisti gt zero}">${act.guPrevisti}</c:if></td>
              <td><c:if test="${act.guEffettivi gt zero}">${act.guEffettivi}</c:if></td>
              <td><fmt:formatDate value="${act.dataInizio}" pattern='dd/MM/yyyy' /></td>
              <td><fmt:formatDate value="${act.dataFine}" pattern='dd/MM/yyyy' /></td>
              <td><fmt:formatDate value="${act.dataInizioEffettiva}" pattern='dd/MM/yyyy' /></td>
              <td><fmt:formatDate value="${act.dataFineEffettiva}" pattern='dd/MM/yyyy' /></td>
            </tr>
          </c:forEach>
        </c:forEach>
        </tbody>
      </table>
    </div>
    <hr class="separatore" />
      <div id="container-fluid">
        <div class="row">
          <div class="col-4">
            <span class="float-left">
              <a class="btn btnNav" href="${urlWbs}${p.id}">
                <i class="fas fa-undo"></i> WBS
              </a>
              <a class="btn btnNav" href="${project}">
                <i class="fas fa-home"></i>
                Progetti
              </a>
            </span>
          </div>
          <div class="col-4 text-center">
            <a href="${initParam.appName}/data?q=wbs&id=${param['id']}" class="btn btn-warning" id="expReport"><i class="fas fa-file-export"></i> Esporta CSV</a>
          </div>
        </div>
      </div>

    <%@ include file="subPopup.jspf" %>
    <script type="text/javascript">
    /* @license: http://jsfiddle.net/spetnik/gFzCk/1953/
      All code belongs to the poster and no license is enforced. 
      JSFiddle or its authors are not responsible or liable for 
      any loss or damage of any kind during the usage of provided code. */
    var table = $('table');
    $('#facility_header, #activity_header')
    .wrapInner('<span title="sort this column"/>')
    .each(function(){
        var th = $(this),
            thIndex = th.index(),
            inverse = false;
        th.click(function(){
            table.find('td').filter(function(){
                return $(this).index() === thIndex;
            }).sortElements(function(a, b){
                return $.text([a]) > $.text([b]) ?
                    inverse ? -1 : 1
                    : inverse ? 1 : -1;
            }, function(){
                // parentNode is the element we want to move
                return this.parentNode;  
            });
            inverse = !inverse;
        });  
    });
    
    /**
     * jQuery.fn.sortElements
     * --------------
     * @author James Padolsey (http://james.padolsey.com)
     * @version 0.11
     * @updated 18-MAR-2010
     * --------------
     * @param Function comparator:
     *   Exactly the same behaviour as [1,2,3].sort(comparator)
     *   
     * @param Function getSortable
     *   A function that should return the element that is
     *   to be sorted. The comparator will run on the
     *   current collection, but you may want the actual
     *   resulting sort to occur on a parent or another
     *   associated element.
     *   
     *   E.g. $('td').sortElements(comparator, function(){
     *      return this.parentNode; 
     *   })
     *   
     *   The <td>'s parent (<tr>) will be sorted instead
     *   of the <td> itself.
     */
    jQuery.fn.sortElements = (function(){
        
        var sort = [].sort;
        
        return function(comparator, getSortable) {
            
            getSortable = getSortable || function(){return this;};
            
            var placements = this.map(function(){
                
                var sortElement = getSortable.call(this),
                    parentNode = sortElement.parentNode,
                    
                    // Since the element itself will change position, we have
                    // to have some way of storing it's original position in
                    // the DOM. The easiest way is to have a 'flag' node:
                    nextSibling = parentNode.insertBefore(
                        document.createTextNode(''),
                        sortElement.nextSibling
                    );
                
                return function() {
                    
                    if (parentNode === this) {
                        throw new Error(
                            "You can't sort elements if any one is a descendant of another."
                        );
                    }
                    
                    // Insert before flag:
                    parentNode.insertBefore(this, nextSibling);
                    // Remove flag:
                    parentNode.removeChild(nextSibling);
                    
                };
                
            });
           
            return sort.call(this, comparator).each(function(i){
                placements[i].call(getSortable.call(this));
            });
            
        };
        
    })();
    </script>