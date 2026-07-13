<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Import / Export" />
<c:set var="crumbs"    value="Données / <b>Import / Export</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Import / Export</h1><p>Importer des données depuis Excel, exporter en Excel ou PDF</p></div>
  <a class="btn btn--ghost" href="${ctx}/imports-exports/historique"> Historique</a>
</div>

<div class="grid-2">
  <%-- Import Excel : POST /imports/excel (multipart), bind ImportExcelDTO --%>
  <div class="card">
    <div class="card__head"><h2>Importer depuis Excel</h2></div>
    <div class="card__body">
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
        <div class="alert alert--info"><span>Téléchargez d'abord le modèle du module, remplissez-le, puis importez-le :</span></div>
        <div class="stack">
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=CLIENTS">Modèle Clients (nom, téléphone, adresse)</a>
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=INGREDIENTS">Modèle Ingrédients (nom, unité, stock, seuil)</a>
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=LOTS">Modèle Lots (sexe, objectif, origine, race, prix_achat, effectif)</a>
          <a class="btn btn--ghost btn--sm" href="${ctx}/imports/modele?module=VACCINATIONS">Modèle Vaccinations (code_lot, vaccin, date, rappel, obs.)</a>
        </div>
        <button class="btn btn--primary w-full" style="justify-content:center" type="submit">Importer le fichier</button>
      </form>
    </div>
  </div>

  <%-- Exports --%>
  <div class="card">
    <div class="card__head"><h2>Exporter</h2></div>
    <div class="card__body stack">
      <p class="muted">Téléchargez les données d'un module au format Excel ou PDF.</p>
      <c:set var="modules" value="LOTS,CLIENTS,VENTES,DEPENSES,INGREDIENTS,GROUPES,ANALYSE" />
      <table class="tbl">
        <thead><tr><th>Module</th><th class="right">Formats</th></tr></thead>
        <tbody>
          <c:forEach var="m" items="${fn:split(modules, ',')}">
            <tr>
              <td><b>${m}</b></td>
              <td class="right">
                <a class="btn btn--ghost btn--sm" href="${ctx}/exports/excel?module=${m}">Excel</a>
                <a class="btn btn--ghost btn--sm" href="${ctx}/exports/pdf?module=${m}">PDF</a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
