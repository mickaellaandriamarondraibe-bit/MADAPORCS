<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Vaccinations" />
<c:set var="crumbs"    value="Santé / <b>Vaccinations</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Vaccinations</h1><p>Vaccinations réalisées et planifiées par lot</p></div>
  <a class="btn btn--primary" href="${ctx}/vaccinations/form"><i class="fa-solid fa-plus"></i> Enregistrer une vaccination</a>
</div>

<div class="toolbar"><div class="search"><input class="input" placeholder="Rechercher…" data-filter-input="#tbl-vac"></div></div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-vac">
      <thead><tr><th>Lot</th><th>Vaccin</th><th>Date</th><th>Rappel</th><th>Statut</th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty vaccinations}">
            <c:forEach var="v" items="${vaccinations}">
              <tr>
                <td><b>${v.codeLot}</b></td><td>${v.nomVaccin}</td>
                <td><fmt:formatDate value="${v.dateVaccination}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${v.dateRappel}" pattern="dd/MM/yyyy"/></td>
                <td><span class="badge ${v.statut == 'A_VENIR' ? 'badge--amber' : 'badge--green'}">${v.statut}</span></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="5"><div class="empty"><i class="ico fa-solid fa-calendar-days"></i><p>Aucune vaccination enregistrée.</p></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
