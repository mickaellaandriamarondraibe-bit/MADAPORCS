<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty groupe.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le groupe' : 'Nouveau groupe de reproduction'}" />
<c:set var="crumbs"    value="Reproduction / Groupes / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1><p>Seule une partie du lot femelle entre en reproduction</p></div>
  <a class="btn btn--ghost" href="${ctx}/reproduction/groupes"><i class="fa-solid fa-arrow-left"></i> Retour</a>
</div>

<div class="card" style="max-width:820px">
  <div class="card__body">
    <%-- POST /reproduction/groupes/save, bind GroupeReproductionDTO --%>
    <form method="post" action="${ctx}/reproduction/groupes/save">
      <input type="hidden" name="id" value="${groupe.id}">
      <div class="form-grid">
        <div class="field">
          <label>Lot femelle <span class="req">*</span></label>
          <select class="select" name="lotFemelleId" required>
            <option value="">— Choisir un lot FEMELLE —</option>
            <c:forEach var="lf" items="${lotsFemelles}">
              <option value="${lf.id}" ${groupe.lotFemelleId == lf.id ? 'selected' : ''}>${lf.codeLot} (${lf.effectifActuel} dispo)</option>
            </c:forEach>
          </select>
          <span class="hint">Doit être de sexe FEMELLE.</span>
        </div>
        <div class="field">
          <label>Lot mâle <span class="req">*</span></label>
          <select class="select" name="lotMaleId" required>
            <option value="">— Choisir un lot MÂLE —</option>
            <c:forEach var="lm" items="${lotsMales}">
              <option value="${lm.id}" ${groupe.lotMaleId == lm.id ? 'selected' : ''}>${lm.codeLot}</option>
            </c:forEach>
          </select>
          <span class="hint">Doit être de sexe MÂLE.</span>
        </div>
        <div class="field">
          <label>Nombre de femelles concernées <span class="req">*</span></label>
          <input class="input" type="number" min="1" name="nombreFemelles" value="${groupe.nombreFemelles}" required>
          <span class="hint">Supérieur à 0 et ≤ femelles disponibles.</span>
        </div>
        <div class="field">
          <label>Nombre de mâles utilisés</label>
          <input class="input" type="number" min="1" name="nombreMales" value="${empty groupe.nombreMales ? 1 : groupe.nombreMales}">
        </div>
        <div class="field">
          <label>Date de saillie <span class="req">*</span></label>
          <input class="input" type="date" name="dateSaillie" value="${groupe.dateSaillie}" required>
        </div>
        <div class="field">
          <label>Durée de gestation (jours)</label>
          <input class="input" type="number" name="dureeGestation" value="${empty groupe.dureeGestation ? 114 : groupe.dureeGestation}">
          <span class="hint">La date prévue de mise bas est calculée automatiquement.</span>
        </div>
        <div class="field span-2">
          <label>Observation</label>
          <textarea class="textarea" name="observation">${groupe.observation}</textarea>
        </div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/reproduction/groupes">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer le groupe'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
