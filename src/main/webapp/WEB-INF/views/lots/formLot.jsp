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
  <a class="btn btn--ghost" href="${ctx}/lots">Retour</a>
</div>

<form method="post" action="${ctx}/lots/save" class="lot-form">

  <input type="hidden" name="id" value="${lot.id}" />
  <input type="hidden" name="groupeReproductionOrigineId" value="${lot.groupeReproductionOrigineId}" />

  <%-- ===== Informations générales ===== --%>
  <div class="card">
    <div class="card__body">
      <div class="lot-sec__title">Informations générales</div>
      <div class="form-grid">
        <c:if test="${edition}">
          <div class="field">
            <label for="codeLot">Code du lot <span class="req">*</span></label>
            <input class="input" id="codeLot" name="codeLot" value="${lot.codeLot}" readonly />
            <span class="hint">Généré automatiquement à partir du sexe et de l'id.</span>
          </div>
        </c:if>
        <c:if test="${not edition}">
          <div class="field">
            <label>Code du lot <span class="req">*</span></label>
            <input class="input" value="Généré automatiquement (LOT-M-xxx / LOT-F-xxx)" readonly />
            <span class="hint">Le code est créé selon le sexe et l'id du lot.</span>
          </div>
        </c:if>

        <div class="field">
          <label for="dateCreation">Date de création <span class="req">*</span></label>
          <input class="input" type="date" id="dateCreation" name="dateCreation" value="${lot.dateCreation}" />
        </div>
      </div>
    </div>
  </div>

  <%-- ===== Caractéristiques + Effectifs ===== --%>
  <div class="lot-row">
    <div class="card lot-sec--grow">
      <div class="card__body">
        <div class="lot-sec__title">Caractéristiques</div>
        <div class="form-grid">
          <div class="field">
            <label for="raceId">Race <span class="req">*</span></label>
            <select class="select" id="raceId" name="raceId" required>
              <option value="">— Choisir —</option>
              <c:forEach var="r" items="${races}">
                <option value="${r.id}" ${lot.raceId == r.id ? 'selected' : ''}>${r.nom}</option>
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
              <option value="REPRODUCTION" ${lot.objectif == 'REPRODUCTION' ? 'selected' : ''}>Reproduction</option>
              <option value="ENGRAISSEMENT" ${lot.objectif == 'ENGRAISSEMENT' ? 'selected' : ''}>Engraissement</option>
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

          <%-- Visibles seulement si l'origine est "Achat". --%>
          <div class="field" id="champAge" style="display:none">
            <label for="ageMois">Âge à l'achat (en mois) <span class="req">*</span></label>
            <input class="input" type="number" min="0" id="ageMois" name="ageMois" value="${lot.ageMois}" placeholder="Ex: 8" />
            <span class="hint">Âge moyen des animaux achetés, en mois.</span>
          </div>

          <div class="field" id="champPrixAchat" style="display:none">
            <label for="prixAchat">Prix d'achat / tête (Ar)</label>
            <input class="input" type="number" step="0.01" min="0" id="prixAchat" name="prixAchat" value="${lot.prixAchat}" placeholder="Ex: 500000" />
            <span class="hint">Enregistré comme dépense (prix × effectif initial).</span>
          </div>
        </div>
      </div>
    </div>

    <div class="card lot-sec--side">
      <div class="card__body">
        <div class="lot-sec__title">Effectifs</div>
        <div class="lot-stack">
          <div class="field">
            <label for="effectifInitial">Effectif initial <span class="req">*</span></label>
            <input class="input" type="number" min="1" id="effectifInitial" name="effectifInitial" value="${lot.effectifInitial}" required />
            <span class="hint">Doit être strictement supérieur à 0.</span>
          </div>

          <div class="field">
            <label for="effectifActuel">Effectif actuel</label>
            <input class="input" type="number" min="0" id="effectifActuel" name="effectifActuel" value="${lot.effectifActuel}" placeholder="Automatique si vide" />
            <span class="hint">Si vide à la création, il peut être égal à l'effectif initial.</span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <%-- ===== Informations complémentaires + Lot parent ===== --%>
  <div class="lot-row">
    <div class="card lot-sec--grow">
      <div class="card__body">
        <div class="lot-sec__title">Informations complémentaires</div>
        <div class="form-grid">
          <c:if test="${not edition}">
            <div class="field">
              <label for="poidsInitial">Poids de départ (kg)</label>
              <input class="input" type="number" step="0.01" min="0" id="poidsInitial" name="poidsInitial" value="${lot.poidsInitial}" placeholder="Ex: 12.5" />
              <span class="hint">Crée la première pesée du lot. Complétable ensuite.</span>
            </div>
          </c:if>

          <div class="field">
            <label for="statut">Statut</label>
            <select class="select" id="statut" name="statut">
              <option value="ACTIF" ${empty lot.statut || lot.statut == 'ACTIF' ? 'selected' : ''}>ACTIF</option>
              <option value="ARCHIVE" ${lot.statut == 'ARCHIVE' ? 'selected' : ''}>ARCHIVE</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <div class="card lot-sec--side">
      <div class="card__body">
        <div class="lot-sec__title">Lot parent</div>
        <div class="lot-stack">
          <div class="field">
            <label for="lotParentId">Lot parent</label>
            <select class="select" id="lotParentId" name="lotParentId">
              <option value="">— Aucun —</option>
              <c:forEach var="parent" items="${lotsParents}">
                <option value="${parent.id}" ${lot.lotParentId == parent.id ? 'selected' : ''}>${parent.codeLot}</option>
              </c:forEach>
            </select>
            <span class="hint">Utile si ce lot provient d'un autre lot.</span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <%-- ===== Description ===== --%>
  <div class="card">
    <div class="card__body">
      <div class="lot-sec__title">Description</div>
      <div class="field">
        <textarea class="textarea" id="description" name="description" placeholder="Remarque sur le lot...">${lot.description}</textarea>
      </div>
    </div>
  </div>

  <div class="form-actions">
    <a class="btn btn--ghost" href="${ctx}/lots">Annuler</a>
    <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer le lot'}</button>
  </div>
</form>

<style>
  .lot-form > .card { margin-bottom: 18px; }
  .lot-row { display: flex; gap: 18px; align-items: stretch; flex-wrap: wrap; margin-bottom: 18px; }
  .lot-row > .card { margin-bottom: 0; }
  .lot-sec--grow { flex: 1 1 480px; }
  .lot-sec--side { flex: 1 1 280px; }
  .lot-sec__title { position: relative; padding-left: 12px; font-weight: 700; font-size: 15px; color: var(--gris-900); margin-bottom: 16px; }
  .lot-sec__title::before { content: ""; position: absolute; left: 0; top: 1px; bottom: 1px; width: 4px; border-radius: 2px; background: var(--marine-600); }
  .lot-stack { display: flex; flex-direction: column; gap: 16px; }
</style>

<%-- Affiche les champs "âge" et "prix d'achat" seulement quand l'origine est "Achat". --%>
<script>
  const selectOrigine = document.getElementById("origine");
  const champAge = document.getElementById("champAge");
  const champPrixAchat = document.getElementById("champPrixAchat");

  function majAffichageAge() {
    const estAchat = selectOrigine.value === "ACHAT";
    champAge.style.display = estAchat ? "" : "none";
    if (champPrixAchat) {
      champPrixAchat.style.display = estAchat ? "" : "none";
    }
  }

  selectOrigine.addEventListener("change", majAffichageAge);
  majAffichageAge();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
