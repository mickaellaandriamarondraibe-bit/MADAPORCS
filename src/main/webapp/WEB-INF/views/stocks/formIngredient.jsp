<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
  <c:when test="${not empty ingredient.id}">
    <c:set var="edition"   value="true" />
    <c:set var="pageTitle" value="Modifier l'ingrédient" />
  </c:when>
  <c:otherwise>
    <c:set var="edition"   value="false" />
    <c:set var="pageTitle" value="Nouvel ingrédient" />
  </c:otherwise>
</c:choose>
<c:set var="crumbs" value="Stocks / Ingrédients / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1></div>
  <a class="btn btn--ghost" href="${ctx}/ingredients"><i class="fa-solid fa-arrow-left"></i> Retour</a>
</div>

<div class="card" style="max-width:760px">
  <div class="card__body">
    <h4 class="text-red-600"><c:if test="${not empty message}">${message}</c:if></h4>
    <form method="post" action="${ctx}/ingredients/save">
      <input type="hidden" name="id" value="${ingredient.id}">
      <div class="form-grid">
        <div class="field"><label>Nom <span class="req">*</span></label><input class="input" name="nom" value="${ingredient.nom}" required></div>
        <div class="field"><label>Unité <span class="req">*</span></label>
          <select class="select" name="unite" required>
            <option value="KG" ${ingredient.unite == 'KG' ? 'selected' : ''}>kg</option>
            <option value="L" ${ingredient.unite == 'L' ? 'selected' : ''}>litre</option>
            <option value="SAC" ${ingredient.unite == 'SAC' ? 'selected' : ''}>sac</option>
            <option value="UNITE" ${ingredient.unite == 'UNITE' ? 'selected' : ''}>unité</option>
          </select>
        </div>
        <div class="field"><label>Stock actuel</label><input class="input" type="number" step="0.01" min="0" name="stockActuel" value="${ingredient.stockActuel}"></div>
        <div class="field"><label>Seuil d'alerte</label><input class="input" type="number" step="0.01" min="0" name="seuilAlerte" value="${ingredient.seuilAlerte}"></div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/ingredients">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>