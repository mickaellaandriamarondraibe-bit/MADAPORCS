<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="edition" value="${not empty lot.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le lot' : 'Nouveau lot'}" />
<c:set var="crumbs" value="Cheptel / Lots / <b>${edition ? 'Édition' : 'Création'}</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${pageTitle}</h1>
    <p>Un lot est soit MÂLE soit FEMELLE — jamais mixte</p>
  </div>

  <a class="btn btn--ghost" href="${ctx}/lots">
    <i class="fa-solid fa-arrow-left"></i> Retour
  </a>
</div>

<div class="card" style="max-width:820px">
  <div class="card__body">

    <form method="post" action="${ctx}/lots/save">

      <input type="hidden" name="id" value="${lot.id}" />

      <%-- Utilisé seulement si le lot vient d’un groupe de reproduction --%>
      <input type="hidden" name="groupeReproductionOrigineId" value="${lot.groupeReproductionOrigineId}" />

      <div class="form-grid">

        <div class="field">
          <label for="codeLot">Code du lot <span class="req">*</span></label>
          <input
            class="input"
            id="codeLot"
            name="codeLot"
            value="${lot.codeLot}"
            placeholder="LOT-F-001"
            required
          />
          <span class="hint">Unique. Ex. LOT-F-001, LOT-M-001.</span>
        </div>

        <div class="field">
          <label for="dateCreation">Date de création</label>
          <input
            class="input"
            type="date"
            id="dateCreation"
            name="dateCreation"
            value="${lot.dateCreation}"
          />
        </div>

        <div class="field">
          <label for="raceId">Race <span class="req">*</span></label>
          <select class="select" id="raceId" name="raceId" required>
            <option value="">— Choisir —</option>

            <c:forEach var="r" items="${races}">
              <option value="${r.id}" ${lot.raceId == r.id ? 'selected' : ''}>
                ${r.nom}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label for="sexe">Sexe <span class="req">*</span></label>
          <select class="select" id="sexe" name="sexe" required>
            <option value="">— Choisir —</option>
            <option value="MALE" ${lot.sexe == 'MALE' ? 'selected' : ''}>Mâle</option>
            <option value="FEMELLE" ${lot.sexe == 'FEMELLE' ? 'selected' : ''}>Femelle</option>
          </select>
          <span class="hint">Un lot ne doit jamais contenir les deux sexes.</span>
        </div>

        <div class="field">
          <label for="objectif">Objectif <span class="req">*</span></label>
          <select class="select" id="objectif" name="objectif" required>
            <option value="">— Choisir —</option>
            <option value="REPRODUCTION" ${lot.objectif == 'REPRODUCTION' ? 'selected' : ''}>
              Reproduction
            </option>
            <option value="ENGRAISSEMENT" ${lot.objectif == 'ENGRAISSEMENT' ? 'selected' : ''}>
              Engraissement
            </option>
          </select>
        </div>

        <div class="field">
          <label for="origine">Origine</label>
          <select class="select" id="origine" name="origine">
            <option value="">— Choisir —</option>
            <option value="ACHAT" ${lot.origine == 'ACHAT' ? 'selected' : ''}>Achat</option>
            <option value="NAISSANCE" ${lot.origine == 'NAISSANCE' ? 'selected' : ''}>Naissance</option>
            <option value="TRANSFERT" ${lot.origine == 'TRANSFERT' ? 'selected' : ''}>Transfert</option>
          </select>
        </div>

        <div class="field">
          <label for="effectifInitial">Effectif initial <span class="req">*</span></label>
          <input
            class="input"
            type="number"
            min="1"
            id="effectifInitial"
            name="effectifInitial"
            value="${lot.effectifInitial}"
            required
          />
          <span class="hint">Doit être strictement supérieur à 0.</span>
        </div>

        <div class="field">
          <label for="effectifActuel">Effectif actuel</label>
          <input
            class="input"
            type="number"
            min="0"
            id="effectifActuel"
            name="effectifActuel"
            value="${lot.effectifActuel}"
            placeholder="Automatique si vide"
          />
          <span class="hint">Si vide à la création, il peut être égal à l’effectif initial.</span>
        </div>

        <div class="field">
          <label for="statut">Statut</label>
          <select class="select" id="statut" name="statut">
            <option value="ACTIF" ${empty lot.statut || lot.statut == 'ACTIF' ? 'selected' : ''}>ACTIF</option>
            <option value="ARCHIVE" ${lot.statut == 'ARCHIVE' ? 'selected' : ''}>ARCHIVE</option>
          </select>
        </div>

        <div class="field">
          <label for="lotParentId">Lot parent</label>
          <select class="select" id="lotParentId" name="lotParentId">
            <option value="">— Aucun —</option>

            <c:forEach var="parent" items="${lotsParents}">
              <option value="${parent.id}" ${lot.lotParentId == parent.id ? 'selected' : ''}>
                ${parent.codeLot}
              </option>
            </c:forEach>
          </select>
          <span class="hint">Utile si ce lot provient d’un autre lot.</span>
        </div>

        <div class="field span-2">
          <label for="description">Description</label>
          <textarea
            class="textarea"
            id="description"
            name="description"
            placeholder="Remarque sur le lot..."
          >${lot.description}</textarea>
        </div>

      </div>

      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/lots">Annuler</a>

        <button class="btn btn--primary" type="submit">
          ${edition ? 'Enregistrer' : 'Créer le lot'}
        </button>
      </div>

    </form>

  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>