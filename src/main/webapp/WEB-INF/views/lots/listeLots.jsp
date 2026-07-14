<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Lots de porcs" />
<c:set var="crumbs" value="Cheptel / <b>Lots de porcs</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Lots de porcs</h1>
    <p>Création, suivi et archivage des lots</p>
  </div>

  <a class="btn btn--primary" href="${ctx}/lots/form">
     Nouveau lot
  </a>
</div>

<div class="listbar">
  <div class="search search--auto">
    <input class="input" type="text" placeholder="Rechercher un lot…" data-filter-input="#tbl-lots">
  </div>
  <span class="spacer"></span>
  <button type="button" class="btn btn--ghost filters-toggle" data-filters-toggle="#f-lots" aria-expanded="false">
    Filtres <span class="badge-count" data-filters-count hidden>0</span> <span class="chevron">▾</span>
  </button>
</div>

<div class="filters-panel" id="f-lots" hidden>
  <form method="get" action="${ctx}/lots" class="filters-row">
    <label class="fld">Sexe
      <select class="select" name="sexe">
        <option value="">Tous</option>
        <option value="MALE" ${filtre.sexe == 'MALE' ? 'selected' : ''}>Mâle</option>
        <option value="FEMELLE" ${filtre.sexe == 'FEMELLE' ? 'selected' : ''}>Femelle</option>
      </select>
    </label>
    <label class="fld">Objectif
      <select class="select" name="objectif">
        <option value="">Tous</option>
        <option value="REPRODUCTION" ${filtre.objectif == 'REPRODUCTION' ? 'selected' : ''}>Reproduction</option>
        <option value="ENGRAISSEMENT" ${filtre.objectif == 'ENGRAISSEMENT' ? 'selected' : ''}>Engraissement</option>
      </select>
    </label>
    <label class="fld">Statut
      <select class="select" name="statut">
        <option value="">Tous</option>
        <option value="ACTIF" ${filtre.statut == 'ACTIF' ? 'selected' : ''}>Actif</option>
        <option value="ARCHIVE" ${filtre.statut == 'ARCHIVE' ? 'selected' : ''}>Archivé</option>
      </select>
    </label>
    <label class="fld">Entrée depuis
      <input class="input" type="date" name="dateCreationDebut" value="${filtre.dateCreationDebut}">
    </label>
    <label class="fld">Entrée jusqu'au
      <input class="input" type="date" name="dateCreationFin" value="${filtre.dateCreationFin}">
    </label>
    <div class="fld-actions">
      <button class="btn btn--primary" type="submit">Filtrer</button>
      <a class="btn btn--ghost" href="${ctx}/lots">Réinitialiser</a>
    </div>
  </form>

  <div class="filters-row">
    <label class="fld">Effectif
      <span class="range-filter">
        <input class="input input--sm" type="number" min="0" placeholder="min" data-range="#tbl-lots" data-range-col="4" data-range-kind="min">
        <span class="range-sep">→</span>
        <input class="input input--sm" type="number" min="0" placeholder="max" data-range="#tbl-lots" data-range-col="4" data-range-kind="max">
      </span>
    </label>
    <div class="filters-sort">
      <span>Trier par</span>
      <select class="select" data-sort-select="#tbl-lots"></select>
      <button type="button" class="btn btn--ghost btn--sm sort-dir" data-sort-dir="asc" title="Croissant">&#8593;</button>
      <button type="button" class="btn btn--ghost btn--sm sort-dir" data-sort-dir="desc" title="Décroissant">&#8595;</button>
    </div>
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10" id="tbl-lots">
      <thead>
        <tr>
          <th>Code</th>
          <th>Sexe</th>
          <th>Race</th>
          <th>Objectif</th>
          <th class="num">Effectif</th>
          <th>Statut</th>
          <th class="actions">Actions</th>
        </tr>
      </thead>

      <tbody>
        <c:choose>
          <c:when test="${not empty lots}">
            <c:forEach var="l" items="${lots}">
              <tr>
                <td>
                  <a href="${ctx}/lots/${l.id}">
                    <b>${l.codeLot}</b>
                  </a>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${l.sexe == 'FEMELLE'}">
                      <span class="badge badge--gold">
                         Femelle
                      </span>
                    </c:when>

                    <c:otherwise>
                      <span class="badge badge--blue">
                         Mâle
                      </span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${not empty l.race}">
                      ${l.race.nom}
                    </c:when>
                    <c:otherwise>
                      <span class="muted">Non définie</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>${l.objectif}</td>

                <td class="num">
                  <b>${l.effectifActuel}</b>
                  <span class="muted">/ ${l.effectifInitial}</span>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${l.statut == 'ACTIF'}">
                      <span class="badge badge--green">
                        <span class="dot"></span> Actif
                      </span>
                    </c:when>

                    <c:otherwise>
                      <span class="badge badge--gray">
                        Archivé
                      </span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td class="actions">
                  <a href="${ctx}/lots/${l.id}">Détail</a>
                  <a href="${ctx}/lots/form?id=${l.id}">Modifier</a>
                </td>
              </tr>
            </c:forEach>
          </c:when>

          <c:otherwise>
            <tr>
              <td colspan="7">
                <div class="empty">
                  
                  <p>Aucun lot ne correspond.</p>

                  <a class="btn btn--primary" href="${ctx}/lots/form">
                    Créer un lot
                  </a>
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