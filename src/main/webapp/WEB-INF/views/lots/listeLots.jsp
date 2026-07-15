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
</div>

<div class="card filterbox">
  <div class="filterbox__head">
    <div class="filterbox__title">Filtres de recherche</div>
    <button type="button" class="btn btn--ghost btn--sm filters-toggle" data-filters-toggle="#f-lots" aria-expanded="true">
      <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="4" y1="21" x2="4" y2="14"/><line x1="4" y1="10" x2="4" y2="3"/><line x1="12" y1="21" x2="12" y2="12"/><line x1="12" y1="8" x2="12" y2="3"/><line x1="20" y1="21" x2="20" y2="16"/><line x1="20" y1="12" x2="20" y2="3"/><line x1="1" y1="14" x2="7" y2="14"/><line x1="9" y1="8" x2="15" y2="8"/><line x1="17" y1="16" x2="23" y2="16"/></svg>
      Filtres <span class="badge-count" data-filters-count hidden>0</span> <span class="chevron">▾</span>
    </button>
  </div>

  <form method="get" action="${ctx}/lots" class="filterbox__panel" id="f-lots" data-open>
    <div class="filterbox__grid">
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
      <label class="fld">Effectif
        <span class="range-filter">
          <input class="input input--sm" type="number" min="0" placeholder="min" data-range="#tbl-lots" data-range-col="4" data-range-kind="min">
          <span class="range-sep">–</span>
          <input class="input input--sm" type="number" min="0" placeholder="max" data-range="#tbl-lots" data-range-col="4" data-range-kind="max">
        </span>
      </label>
    </div>

    <div class="filterbox__foot">
      <div class="filters-sort">
        <span>Trier par</span>
        <select class="select" data-sort-select="#tbl-lots"></select>
        <button type="button" class="btn btn--ghost btn--sm sort-dir" data-sort-dir="asc" title="Croissant">&#8593;</button>
        <button type="button" class="btn btn--ghost btn--sm sort-dir" data-sort-dir="desc" title="Décroissant">&#8595;</button>
      </div>
      <div class="fld-actions">
        <button class="btn btn--primary" type="submit">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></svg>
          Filtrer
        </button>
        <a class="btn btn--ghost" href="${ctx}/lots">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M23 4v6h-6"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg>
          Réinitialiser
        </a>
      </div>
    </div>
  </form>
</div>

<style>
  .filterbox { padding: 0; margin-bottom: 16px; }
  .filterbox__head { display: flex; align-items: center; justify-content: space-between; padding: 15px 20px; }
  .filterbox__title { font-weight: 700; color: var(--gris-900); }
  .filterbox__panel { padding: 0 20px 20px; }
  .filterbox__panel[hidden] { display: none; }
  /* 6 colonnes egales qui remplissent toute la largeur (comme la maquette) */
  .filterbox__grid { display: grid; grid-template-columns: repeat(6, minmax(0, 1fr)); gap: 14px 16px; align-items: end; padding-top: 16px; border-top: 1px solid var(--gris-200); }
  .filterbox__grid .fld { min-width: 0; text-transform: none; letter-spacing: 0; font-weight: 500; font-size: 12px; color: var(--gris-500); }
  .filterbox .select, .filterbox .input { border-radius: 9px; background: var(--gris-050); border-color: var(--gris-200); width: 100%; min-width: 0; }
  .filterbox .select:focus, .filterbox .input:focus { background: var(--blanc); }
  .filterbox input[type="date"].input { min-width: 0; }
  .filterbox .range-filter { display: flex; flex-wrap: nowrap; width: 100%; gap: 6px; }
  .filterbox .range-filter .input { flex: 1; max-width: none; padding-left: 8px; padding-right: 8px; }

  .filterbox__foot { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 14px; margin-top: 18px; }
  .filterbox__foot .fld-actions { align-items: center; }
  .filterbox__foot .filters-sort .select { min-width: 150px; }

  @media (max-width: 900px) { .filterbox__grid { grid-template-columns: repeat(3, minmax(0, 1fr)); } }
  @media (max-width: 560px) { .filterbox__grid { grid-template-columns: repeat(2, minmax(0, 1fr)); } }
</style>

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
                  <svg width="54" height="54" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" style="color:var(--gris-300);margin-bottom:6px"><path d="M22 12h-6l-2 3h-4l-2-3H2"/><path d="M5.45 5.11 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"/></svg>
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