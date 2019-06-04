<%@ include file="pcURL.jspf" %>
<c:set var="linkWbs" value="${modWbs}${p.id}&idw=" scope="page"/>
<c:set var="linkAct" value="${modAct}${p.id}&ida=" scope="page"/>
    <h4>Attivit&agrave; del sotto progetto <strong><c:out value="${p.titolo}" /></strong> (rappresentazione grafica)</h4>
    <span class="float-right">
      <a class="btn btnNav" href="${act}${p.id}">
        <i class="fas fa-undo"></i> Attivit&agrave;
      </a>
      <a class="btn btnNav" href="${project}">
        <i class="fas fa-home"></i>
        Progetti
      </a>
    </span>
    <ul class="nav nav-tabs responsive" role="tablist" id="tabs-0">
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${act}${p.id}">Attivit&agrave;</a></li>
      <li class="nav-item"><a class="nav-link active tabactive" data-toggle="tab" href="#">Grafico</a></li>
      <li class="nav-item"><a class="nav-link" data-toggle="tab" href="${binAct}${p.id}">Cestino</a></li>
    </ul>
    <hr class="separatore" />
    <link href="<c:out value="${initParam.urlDirectoryStili}" />orgchart/orgchart.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
      .jOrgChart .left {
          border-right: 1px solid black;
      }
      
      .wbs {
          background-color: #3399FF !important;
          color: #e6e6e6;
      }
      
      .jOrgChart .multi-tree span .content {
          background-color: #ffff00 !important;
      }
    </style>
    <script src="<c:out value="${initParam.urlDirectoryScript}" />orgchart/orgchart.js"></script>
    <div class="scrollX">
    <ul id="tree-data${p.id}" class="noDisplay">
      <li id="progetto${p.id}" class="wbs">
        ${p.titolo}
        <ul>
        <c:forEach var="wbsAvo" items="${requestScope.wbsHierarchy}">
          <li id="${wbsAvo.id}" class="wbs">
            <a href="${linkWbs}${wbsAvo.id}" class="linkWbsGrafico">${wbsAvo.nome}</a>
            <c:choose>
            <c:when test="${empty wbsAvo.wbsFiglie}">
            <ul type="vertical">
              <c:forEach var="actOfWbs" items="${wbsAvo.attivita}">
              <li id="${actOfWbs.id}">
                <a href="${linkAct}${actOfWbs.id}">${actOfWbs.nome}</a>
              </li>
              </c:forEach>
            </ul>
            </c:when>
            <c:otherwise>
            <ul>
              <c:forEach var="wbsFiglia" items="${wbsAvo.wbsFiglie}">
              <li id="${wbsFiglia.id}" class="wbs">
                <a href="${linkWbs}${wbsFiglia.id}" class="linkWbsGrafico">${wbsFiglia.nome}</a>
                <c:choose>
                <c:when test="${empty wbsFiglia.wbsFiglie}">
                <ul type="vertical">
                  <c:forEach var="actOfWbs" items="${wbsFiglia.attivita}">
                  <li id="${actOfWbs.id}">
                    <a href="${linkAct}${actOfWbs.id}">${actOfWbs.nome}</a>
                  </li>
                  </c:forEach>
                </ul>
                </c:when>
                <c:otherwise>
                <ul>
                  <c:forEach var="wbsNipote" items="${wbsFiglia.wbsFiglie}">
                  <li id="${wbsNipote.id}" class="wbs">
                    <a href="${linkWbs}${wbsNipote.id}" class="linkWbsGrafico">${wbsNipote.nome}</a>
                    <c:choose>
                    <c:when test="${empty wbsNipote.wbsFiglie}">
                    <ul type="vertical">
                      <c:forEach var="actOfWbs" items="${wbsNipote.attivita}">
                      <li id="${actOfWbs.id}">
                        <a href="${linkAct}${actOfWbs.id}">${actOfWbs.nome}</a>
                      </li>
                      </c:forEach>
                    </ul>
                    </c:when>
                    <c:otherwise>
                    <ul>
                      <c:forEach var="wbsProNipote" items="${wbsNipote.wbsFiglie}">
                      <li id="${wbsProNipote.id}" class="wbs">
                        <a href="${linkWbs}${wbsProNipote.id}" class="linkWbsGrafico">${wbsProNipote.nome}</a>
                        <c:choose>
                        <c:when test="${empty wbsProNipote.wbsFiglie}">
                        <ul type="vertical">
                          <c:forEach var="actOfWbs" items="${wbsProNipote.attivita}">
                          <li id="${actOfWbs.id}">
                            <a href="${linkAct}${actOfWbs.id}">${actOfWbs.nome}</a>
                          </li>
                          </c:forEach>
                        </ul>
                        </c:when>
                        <c:otherwise>
                        <ul>
                          <c:forEach var="wbsProProNipote" items="${wbsProNipote.wbsFiglie}">
                          <li id="${wbsProProNipote.id}" class="wbs">
                            <a href="${linkWbs}${wbsProProNipote.id}" class="linkWbsGrafico">${wbsProProNipote.nome}</a>
                          <c:if test="${empty wbsProProNipote.wbsFiglie}">
                            <ul type="vertical">
                              <c:forEach var="actOfWbs" items="${wbsProProNipote.attivita}">
                              <li id="${actOfWbs.id}">
                                <a href="${linkAct}${actOfWbs.id}">${actOfWbs.nome}</a>
                              </li>
                              </c:forEach>
                            </ul>
                          </c:if>
                          </li>
                          </c:forEach>
                        </ul>
                        </c:otherwise>
                        </c:choose>
                      </li>
                      </c:forEach>
                    </ul>
                    </c:otherwise>
                    </c:choose>
                  </li>
                  </c:forEach>
                </ul>
                </c:otherwise>
                </c:choose>
              </li>
              </c:forEach>
            </ul>
            </c:otherwise>
            </c:choose>
          </li>
        </c:forEach>
        </ul>
      </li>
    </ul>
    <div id="tree-view${p.id}"></div>
    </div>
    <hr class="separatore" />
    <script>
    $(document).ready(function () {
      // create a tree
      $('#tree-data'+${p.id}).jOrgChart({
        chartElement: $("#tree-view"+${p.id}), 
        nodeClicked: nodeClicked
      });
      
      // lighting a node in the selection
      function nodeClicked(node, type) {
        node = node || $(this);
        $('.jOrgChart .selected').removeClass('selected');
        node.addClass('selected');
      }
    });
    </script>