<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Dépenses" />
<c:set var="crumbs"    value="Finance / <b>Dépenses</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Dépenses</h1><p>Charges et sorties de trésorerie</p></div>
  <a class="btn btn--primary" href="${ctx}/depenses/form"> Nouvelle dépense</a>
</div>

<div class="listbar">
  <div class="search search--auto"><input class="input" placeholder="Rechercher une dépense…" data-filter-input="#tbl-dep"></div>
  <span class="spacer"></span>
  <button type="button" class="btn btn--ghost filters-toggle" data-filters-toggle="#f-dep" aria-expanded="false">
    Filtres <span class="badge-count" data-filters-count hidden>0</span> <span class="chevron">▾</span>
  </button>
</div>

<div class="filters-panel" id="f-dep" hidden>
  <form method="get" action="${ctx}/depenses" class="filters-row">
    <label class="fld">Du
      <input class="input" type="date" name="dateDebut" value="${filtre.dateDebut}">
    </label>
    <label class="fld">Au
      <input class="input" type="date" name="dateFin" value="${filtre.dateFin}">
    </label>
    <label class="fld">Catégorie
      <select class="select" name="categorieId">
        <option value="">Toutes</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}" ${filtre.categorieId == cat.id ? 'selected' : ''}>${cat.nom}</option>
        </c:forEach>
      </select>
    </label>
    <div class="fld-actions">
      <button class="btn btn--primary" type="submit">Filtrer</button>
      <a class="btn btn--ghost" href="${ctx}/depenses">Réinitialiser</a>
    </div>
  </form>

  <div class="filters-row">
    <label class="fld">Montant
      <span class="range-filter">
        <input class="input input--sm" type="number" min="0" placeholder="min" data-range="#tbl-dep" data-range-col="3" data-range-kind="min">
        <span class="range-sep">→</span>
        <input class="input input--sm" type="number" min="0" placeholder="max" data-range="#tbl-dep" data-range-col="3" data-range-kind="max">
      </span>
    </label>
    <div class="filters-sort">
      <span>Trier par</span>
      <select class="select" data-sort-select="#tbl-dep"></select>
      <button type="button" class="btn btn--ghost btn--sm sort-dir" data-sort-dir="asc" title="Croissant">&#8593;</button>
      <button type="button" class="btn btn--ghost btn--sm sort-dir" data-sort-dir="desc" title="Décroissant">&#8595;</button>
    </div>
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10" id="tbl-dep">
      <thead><tr><th>Date</th><th>Catégorie</th><th>Description</th><th class="num">Montant (Ar)</th><th class="actions"></th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty depenses}">
            <c:forEach var="d" items="${depenses}">
              <tr>
                <td>${d.dateDepense}</td>
                <td><span class="badge badge--gray">${d.categorie.nom}</span></td>
                <td class="muted">${d.description}</td>
                <td class="num"><b><fmt:formatNumber value="${d.montant}" type="number" maxFractionDigits="0"/></b></td>
                <td class="actions"><a href="${ctx}/depenses/form?id=${d.id}">Modifier</a></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="5"><div class="empty"><p>Aucune dépense enregistrée.</p><a class="btn btn--primary" href="${ctx}/depenses/form">Ajouter une dépense</a></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
      <c:if test="${not empty totalDepenses}">
        <tfoot><tr><td colspan="3" class="right"><b>Total</b></td><td class="num"><b><fmt:formatNumber value="${totalDepenses}" type="number" maxFractionDigits="0"/> Ar</b></td><td></td></tr></tfoot>
      </c:if>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
