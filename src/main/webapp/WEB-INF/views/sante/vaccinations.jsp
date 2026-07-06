<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Vaccinations" />
<c:set var="crumbs" value="Santé / Vaccinations" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Vaccinations</h1>
    <p>Vaccinations réalisées et planifiées par lot</p>
  </div>

  <a class="btn btn--primary" href="${ctx}/vaccinations/form">
     Enregistrer une vaccination
  </a>
</div>

<div class="toolbar">
  <div class="search">
    <input class="input" placeholder="Rechercher…" data-filter-input="#tbl-vac" />
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10" id="tbl-vac">
      <thead>
        <tr>
          <th>Lot</th>
          <th>Vaccin</th>
          <th>Date vaccination</th>
          <th>Date rappel</th>
          <th>Statut</th>
          <th class="actions">Actions</th>
        </tr>
      </thead>

      <tbody>
        <c:choose>
          <c:when test="${not empty vaccinations}">
            <c:forEach var="v" items="${vaccinations}">
              <tr>
                <td>
                  <b>${v.lot.codeLot}</b>
                </td>

                <td>${v.vaccin.nom}</td>

                <td>${v.dateVaccination}</td>

                <td>
                  <c:choose>
                    <c:when test="${not empty v.dateRappel}">
                      ${v.dateRappel}
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <span class="badge ${empty v.dateRappel ? 'badge--green' : 'badge--amber'}">
                    ${empty v.dateRappel ? 'Réalisée' : 'Rappel prévu'}
                  </span>
                </td>

                <td class="actions">
                  <a href="${ctx}/vaccinations/form?id=${v.id}">Modifier</a>
                </td>
              </tr>
            </c:forEach>
          </c:when>

          <c:otherwise>
            <tr>
              <td colspan="6">
                <div class="empty">
                  
                  <p>Aucune vaccination enregistrée.</p>
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