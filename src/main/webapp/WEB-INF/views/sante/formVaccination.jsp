<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="edition" value="${not empty vaccination.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier la vaccination' : 'Nouvelle vaccination'}" />
<c:set var="crumbs" value="Santé / Vaccinations / <b>${edition ? 'Édition' : 'Création'}</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${pageTitle}</h1>
  </div>

  <a class="btn btn--ghost" href="${ctx}/vaccinations">
     Retour
  </a>
</div>

<c:if test="${not empty error}">
  <div class="alert alert--danger">
    ${error}
  </div>
</c:if>

<div class="card" style="max-width:760px">
  <div class="card__body">

    <form method="post" action="${ctx}/vaccinations/save">
      <input type="hidden" name="id" value="${vaccination.id}" />

      <div class="form-grid">

        <div class="field">
          <label>Lot concerné <span class="req">*</span></label>
          <select class="select" name="lotId" required>
            <option value="">— Choisir —</option>

            <c:forEach var="l" items="${lots}">
              <option value="${l.id}" ${vaccination.lotId == l.id ? 'selected' : ''}>
                ${l.codeLot}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label>Vaccin <span class="req">*</span></label>
          <select class="select" name="vaccinId" required>
            <option value="">— Choisir —</option>

            <c:forEach var="v" items="${vaccins}">
              <option value="${v.id}" ${vaccination.vaccinId == v.id ? 'selected' : ''}>
                ${v.nom}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label>Date de vaccination <span class="req">*</span></label>
          <input class="input"
                 type="date"
                 name="dateVaccination"
                 value="${vaccination.dateVaccination}"
                 required />
        </div>

        <div class="field">
          <label>Date de rappel</label>
          <input class="input"
                 type="date"
                 name="dateRappel"
                 value="${vaccination.dateRappel}" />
          <span class="hint">Doit être supérieure ou égale à la date de vaccination.</span>
        </div>

          <div class="field">
            <label>Coût (Ar)</label>
            <input class="input"
                   type="number"
                   step="0.01"
                   min="0"
                   name="cout"
                   value="${vaccination.cout}"
                   placeholder="Ex: 20000" />
            <span class="hint">Enregistré comme dépense (santé). Laissez vide pour aucune dépense.</span>
          </div>

        <div class="field span-2">
          <label>Observation</label>
          <textarea class="textarea" name="observation">${vaccination.observation}</textarea>
        </div>

      </div>

      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/vaccinations">Annuler</a>
        <button class="btn btn--primary" type="submit">
          ${edition ? 'Enregistrer' : 'Créer'}
        </button>
      </div>
    </form>

  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>