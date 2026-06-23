<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty lot.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le lot' : 'Nouveau lot'}" />
<c:set var="crumbs"    value="Cheptel / Lots / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1><p>Un lot est soit MÂLE soit FEMELLE — jamais mixte</p></div>
  <a class="btn btn--ghost" href="${ctx}/lots"><i class="fa-solid fa-arrow-left"></i> Retour</a>
</div>

<div class="card" style="max-width:820px">
  <div class="card__body">
    <%-- POST /lots/save, bind LotPorcDTO --%>
    <form method="post" action="${ctx}/lots/save">
      <input type="hidden" name="id" value="${lot.id}">
      <div class="form-grid">
        <div class="field">
          <label for="codeLot">Code du lot <span class="req">*</span></label>
          <input class="input" id="codeLot" name="codeLot" value="${lot.codeLot}" placeholder="LOT-F-001" required>
          <span class="hint">Unique. Ex. LOT-F-001, LOT-M-001.</span>
        </div>
        <div class="field">
          <label for="race">Race <span class="req">*</span></label>
          <select class="select" id="race" name="raceId" required>
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
            <option value="MALE"    ${lot.sexe == 'MALE' ? 'selected' : ''}>Mâle</option>
            <option value="FEMELLE" ${lot.sexe == 'FEMELLE' ? 'selected' : ''}>Femelle</option>
          </select>
        </div>
        <div class="field">
          <label for="objectif">Objectif <span class="req">*</span></label>
          <select class="select" id="objectif" name="objectif" required>
            <option value="REPRODUCTION"  ${lot.objectif == 'REPRODUCTION' ? 'selected' : ''}>Reproduction</option>
            <option value="ENGRAISSEMENT" ${lot.objectif == 'ENGRAISSEMENT' ? 'selected' : ''}>Engraissement</option>
          </select>
        </div>
        <div class="field">
          <label for="effectifInitial">Effectif initial <span class="req">*</span></label>
          <input class="input" type="number" min="1" id="effectifInitial" name="effectifInitial" value="${lot.effectifInitial}" required>
          <span class="hint">Doit être strictement supérieur à 0.</span>
        </div>
        <div class="field">
          <label for="dateEntree">Date d'entrée</label>
          <input class="input" type="date" id="dateEntree" name="dateEntree" value="${lot.dateEntree}">
        </div>
        <div class="field span-2">
          <label for="observation">Observation</label>
          <textarea class="textarea" id="observation" name="observation">${lot.observation}</textarea>
        </div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/lots">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer le lot'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
