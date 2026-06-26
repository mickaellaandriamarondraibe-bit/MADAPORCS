<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="edition" value="${not empty vaccin.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le vaccin' : 'Nouveau vaccin'}" />
<c:set var="crumbs" value="Santé / Vaccins / ${edition ? 'Édition' : 'Création'}" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${pageTitle}</h1>
  </div>

  <a class="btn btn--ghost" href="${ctx}/vaccins">
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

    <form method="post" action="${ctx}/vaccins/save">
      <input type="hidden" name="id" value="${vaccin.id}" />

      <div class="form-grid">

        <div class="field span-2">
          <label>Nom <span class="req">*</span></label>
          <input class="input"
                 name="nom"
                 value="${vaccin.nom}"
                 required />
        </div>

        <div class="field span-2">
          <label>Description</label>
          <textarea class="textarea" name="description">${vaccin.description}</textarea>
        </div>

      </div>

      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/vaccins">Annuler</a>
        <button class="btn btn--primary" type="submit">
          ${edition ? 'Enregistrer' : 'Créer'}
        </button>
      </div>
    </form>

  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>