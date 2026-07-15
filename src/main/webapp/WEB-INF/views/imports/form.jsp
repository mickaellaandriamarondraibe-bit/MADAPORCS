<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Import / Export" />
<c:set var="crumbs"    value="Données / <b>Import / Export</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Import / Export</h1><p>Importer des données depuis Excel, exporter en Excel ou PDF.</p></div>
  <a class="btn btn--ghost" href="${ctx}/imports-exports/historique">
    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
    Historique
  </a>
</div>

<div class="grid-2">

  <%-- Import Excel : POST /imports/excel (multipart), bind ImportExcelDTO --%>
  <div class="card">
    <div class="card__body">
      <div class="ie-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
        Importer depuis Excel
      </div>

      <form method="post" action="${ctx}/imports/excel" enctype="multipart/form-data" class="stack">
        <div class="field">
          <label>Module cible <span class="req">*</span></label>
          <select class="select" name="module" required>
            <option value="">— Choisir —</option>
            <option value="CLIENTS">Clients</option>
            <option value="INGREDIENTS">Ingrédients</option>
            <option value="LOTS">Lots de porcs</option>
            <option value="VACCINATIONS">Vaccinations</option>
          </select>
        </div>
        <div class="field">
          <label>Fichier CSV <span class="req">*</span></label>
          <input class="input" type="file" name="file" accept=".csv" required>
          <span class="hint">La première ligne doit contenir les en-têtes exacts (voir les modèles ci-dessous). Le CSV s'ouvre directement dans Excel.</span>
        </div>

        <div class="alert alert--info">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
          <span>Téléchargez d'abord le modèle du module, remplissez-le, puis importez-le :</span>
        </div>

        <div class="stack ie-models">
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=CLIENTS">Modèle Clients (nom, téléphone, adresse)</a>
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=INGREDIENTS">Modèle Ingrédients (nom, unité, stock, seuil)</a>
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=LOTS">Modèle Lots (sexe, objectif, origine, race, prix_achat, effectif)</a>
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=VACCINATIONS">Modèle Vaccinations (code_lot, vaccin, date, rappel, obs.)</a>
        </div>

        <button class="btn btn--primary w-full" style="justify-content:center" type="submit">
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
          Importer le fichier
        </button>
      </form>
    </div>
  </div>

  <%-- Exports --%>
  <div class="card">
    <div class="card__body">
      <div class="ie-title">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
        Exporter
      </div>
      <p class="muted">Téléchargez les données d'un module au format Excel ou PDF.</p>

      <c:set var="modules" value="LOTS,CLIENTS,VENTES,DEPENSES,INGREDIENTS,GROUPES,ANALYSE" />
      <table class="tbl ie-export">
        <thead><tr><th>Module</th><th class="right">Formats</th></tr></thead>
        <tbody>
          <c:forEach var="m" items="${fn:split(modules, ',')}">
            <tr>
              <td><b>${m}</b></td>
              <td class="right">
                <a class="btn btn--sm ie-btn ie-btn--excel" href="${ctx}/exports/excel?module=${m}">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2"/><line x1="3" y1="9" x2="21" y2="9"/><line x1="3" y1="15" x2="21" y2="15"/><line x1="9" y1="3" x2="9" y2="21"/></svg>
                  Excel
                </a>
                <a class="btn btn--sm ie-btn ie-btn--pdf" href="${ctx}/exports/pdf?module=${m}">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>
                  PDF
                </a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<style>
  .ie-title { position: relative; display: flex; align-items: center; gap: 8px; padding-left: 12px; font-weight: 700; font-size: 15px; color: var(--gris-900); margin-bottom: 16px; }
  .ie-title::before { content: ""; position: absolute; left: 0; top: 1px; bottom: 1px; width: 4px; border-radius: 2px; background: var(--marine-600); }
  .ie-title svg { color: var(--marine-600); }

  .ie-models .btn { justify-content: flex-start; text-align: left; }

  .ie-export th { text-transform: uppercase; font-size: 11px; letter-spacing: .4px; color: var(--gris-500); font-weight: 600; }
  .ie-export td { padding: 12px 8px; vertical-align: middle; }
  .ie-export td.right { white-space: nowrap; }

  .ie-btn { background: var(--blanc); }
  .ie-btn--excel { color: #1a7f4b; border-color: #b7e4c7; }
  .ie-btn--excel:hover { background: var(--vert-050); }
  .ie-btn--pdf { color: var(--rouge-600); border-color: #f3b9b9; margin-left: 8px; }
  .ie-btn--pdf:hover { background: var(--rouge-100); }
</style>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
