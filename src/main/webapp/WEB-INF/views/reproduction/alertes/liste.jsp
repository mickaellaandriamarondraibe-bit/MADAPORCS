<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Alertes de reproduction" />
<c:set var="crumbs"    value="Reproduction / <b>Alertes</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Alertes de reproduction</h1><p>Mises bas proches et alertes à traiter</p></div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10">
      <thead>
        <tr><th>Type</th><th>Groupe</th><th>Lot femelle</th><th>Date prévue</th><th>Statut</th><th class="actions">Actions</th></tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty alertes}">
            <c:forEach var="al" items="${alertes}">
              <tr>
                <td><span class="badge badge--amber">${al.typeAlerte}</span></td>
                <td><a href="${ctx}/reproduction/groupes/${al.groupeReproduction.id}"><b>${al.groupeReproduction.codeGroupe}</b></a></td>
                <td>${al.groupeReproduction.lotFemelle}</td>
                <td>${al.dateAlerteFormattee}</td>
                <td>
                  <c:choose>
                    <c:when test="${al.statut == 'NOUVELLE'}"><span class="badge badge--red"><span class="dot"></span>Nouvelle</span></c:when>
                    <c:when test="${al.statut == 'LUE'}"><span class="badge badge--blue">Lue</span></c:when>
                    <c:otherwise><span class="badge badge--gray">Traitée</span></c:otherwise>
                  </c:choose>
                </td>
                <td class="actions">
                  <c:if test="${al.statut == 'NOUVELLE'}">
                    <form method="post" action="${ctx}/reproduction/alertes/${al.id}/lire" style="display:inline">
                      <button type="submit" style="background:none;border:none;cursor:pointer;color:var(--vert-600);font-weight:600;font-family:inherit;font-size:13px">Marquer lue</button>
                    </form>
                  </c:if>
                  <c:if test="${al.statut != 'TRAITEE'}">
                    <form method="post" action="${ctx}/reproduction/alertes/${al.id}/traiter" style="display:inline">
                      <button type="submit" style="background:none;border:none;cursor:pointer;color:var(--vert-600);font-weight:600;font-family:inherit;font-size:13px;margin-left:12px">Traiter</button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr><td colspan="6"><div class="empty"><p>Aucune alerte active. Tout est sous contrôle.</p></div></td></tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
