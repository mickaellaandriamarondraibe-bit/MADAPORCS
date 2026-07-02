<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty depense.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier la dépense' : 'Nouvelle dépense'}" />
<c:set var="crumbs"    value="Finance / Dépenses / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1></div>
  <a class="btn btn--ghost" href="${ctx}/depenses"> Retour</a>
</div>

<div class="card" style="max-width:760px">
  <div class="card__body">
    <%-- POST /depenses/save, bind DepenseDTO --%>
    <form method="post" action="${ctx}/depenses/save">
      <input type="hidden" name="id" value="${depense.id}">
      <div class="form-grid">
        <div class="field"><label>Catégorie <span class="req">*</span></label>
          <select class="select" name="categorieId" required>
            <option value="">— Choisir —</option>
            <c:forEach var="cat" items="${categories}">
              <option value="${cat.id}" ${depense.categorieId == cat.id ? 'selected' : ''}>${cat.nom}</option>
            </c:forEach>
          </select>
          <span class="hint">Alimentation, Vétérinaire, Matériel, Personnel, Autre…</span>
        </div>
        <div class="field"><label>Montant (Ar) <span class="req">*</span></label>
          <input class="input" type="number" min="0" name="montant" value="${depense.montant}" required>
          <span class="hint">Doit être supérieur à 0.</span>
        </div>
        <div class="field"><label>Date <span class="req">*</span></label>
          <input class="input" type="date" name="dateDepense" value="${depense.dateDepense}" required>
        </div>
        <div class="field span-2"><label>Description</label><textarea class="textarea" name="description">${depense.description}</textarea></div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/depenses">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
