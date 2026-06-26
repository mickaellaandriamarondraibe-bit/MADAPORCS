<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="edition" value="${not empty suivi.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le suivi' : 'Nouveau suivi sanitaire'}" />
<c:set var="crumbs" value="Santé / Suivis / ${edition ? 'Édition' : 'Création'}" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${pageTitle}</h1>
  </div>

  <a class="btn btn--ghost" href="${ctx}/sante/suivis">
    <i class="fa-solid fa-arrow-left"></i> Retour
  </a>
</div>

<c:if test="${not empty error}">
  <div class="alert alert--danger">
    ${error}
  </div>
</c:if>

<div class="card" style="max-width:760px">
  <div class="card__body">

    <form method="post" action="${ctx}/sante/suivis/save">
      <input type="hidden" name="id" value="${suivi.id}" />

      <div class="form-grid">

        <div class="field">
          <label>Lot <span class="req">*</span></label>
          <select class="select" name="lotId" required>
            <option value="">— Choisir —</option>

            <c:forEach var="l" items="${lots}">
              <option value="${l.id}" ${suivi.lotId == l.id ? 'selected' : ''}>
                ${l.codeLot}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label>Maladie</label>
          <select class="select" name="maladieId">
            <option value="">— Aucune —</option>

            <c:forEach var="m" items="${maladies}">
              <option value="${m.id}" ${suivi.maladieId == m.id ? 'selected' : ''}>
                ${m.nom}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label>Traitement</label>
          <select class="select" name="traitementId">
            <option value="">— Aucun —</option>

            <c:forEach var="t" items="${traitements}">
              <option value="${t.id}" ${suivi.traitementId == t.id ? 'selected' : ''}>
                ${t.nom}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label>Date de diagnostic <span class="req">*</span></label>
          <input class="input"
                 type="date"
                 name="dateDiagnostic"
                 value="${suivi.dateDiagnostic}"
                 required />
        </div>

        <div class="field">
          <label>Date de traitement</label>
          <input class="input"
                 type="date"
                 name="dateTraitement"
                 value="${suivi.dateTraitement}" />
          <span class="hint">Doit être supérieure ou égale à la date de diagnostic.</span>
        </div>

        <div class="field">
          <label>Date de guérison</label>
          <input class="input"
                 type="date"
                 name="dateGuerison"
                 value="${suivi.dateGuerison}" />
          <span class="hint">Doit être supérieure ou égale à la date de diagnostic.</span>
        </div>

        <div class="field span-2">
          <label>Observation</label>
          <textarea class="textarea" name="observation">${suivi.observation}</textarea>
        </div>

      </div>

      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/sante/suivis">Annuler</a>
        <button class="btn btn--primary" type="submit">
          ${edition ? 'Enregistrer' : 'Créer'}
        </button>
      </div>
    </form>

  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>