<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Alertes de reproduction" />
<c:set var="crumbs" value="Reproduction / <b>Alertes</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Alertes de reproduction</h1>
    <p>Mises bas proches, alertes lues et actions a traiter</p>
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl">
      <thead>
        <tr>
          <th>Type</th>
          <th>Message</th>
          <th>Groupe</th>
          <th>Lot</th>
          <th>Date alerte</th>
          <th>Statut</th>
          <th class="actions">Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty alertes}">
            <c:forEach var="alerte" items="${alertes}">
              <tr>
                <td><span class="badge badge--amber"><c:out value="${alerte.typeAlerte}" /></span></td>
                <td><c:out value="${alerte.message}" /></td>
                <td>
                  <c:choose>
                    <c:when test="${not empty alerte.groupeReproduction}">
                      <a href="${ctx}/reproduction/groupes/${alerte.groupeReproduction.id}">
                        <b><c:out value="${alerte.groupeReproduction.codeGroupe}" /></b>
                      </a>
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${not empty alerte.lot}">
                      <c:out value="${alerte.lot.codeLot}" />
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
                <td><c:out value="${alerte.dateAlerte}" /></td>
                <td>
                  <c:choose>
                    <c:when test="${alerte.statut == 'NON_LUE'}">
                      <span class="badge badge--red"><span class="dot"></span>Non lue</span>
                    </c:when>
                    <c:when test="${alerte.statut == 'LUE'}">
                      <span class="badge badge--blue">Lue</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge badge--gray">Traitee</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td class="actions">
                  <c:if test="${alerte.statut == 'NON_LUE'}">
                    <form method="post" action="${ctx}/reproduction/alertes/${alerte.id}/lire" style="display:inline">
                      <button class="btn btn--ghost btn--sm" type="submit">Marquer lue</button>
                    </form>
                  </c:if>
                  <c:if test="${alerte.statut != 'TRAITEE'}">
                    <form method="post" action="${ctx}/reproduction/alertes/${alerte.id}/traiter" style="display:inline">
                      <button class="btn btn--primary btn--sm" type="submit">Traiter</button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="7">
                <div class="empty">
                  <i class="ico fa-solid fa-bell"></i>
                  <p>Aucune alerte active.</p>
                </div>
              </td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
