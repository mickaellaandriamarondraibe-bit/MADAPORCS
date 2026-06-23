<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty vaccin.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le vaccin' : 'Nouveau vaccin'}" />
<c:set var="crumbs"    value="Santé / Vaccins / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1></div>
  <a class="btn btn--ghost" href="${ctx}/vaccins"><i class="fa-solid fa-arrow-left"></i> Retour</a>
</div>

<div class="card" style="max-width:760px">
  <div class="card__body">
    <%-- POST /vaccins/save, bind VaccinDTO --%>
    <form method="post" action="${ctx}/vaccins/save">
      <input type="hidden" name="id" value="${vaccin.id}">
      <div class="form-grid">
        <div class="field"><label>Nom <span class="req">*</span></label><input class="input" name="nom" value="${vaccin.nom}" required></div>
        <div class="field"><label>Maladie ciblée</label><input class="input" name="maladieCiblee" value="${vaccin.maladieCiblee}"></div>
        <div class="field"><label>Voie d'administration</label>
          <select class="select" name="voie">
            <option value="INJECTION" ${vaccin.voie == 'INJECTION' ? 'selected' : ''}>Injection</option>
            <option value="ORALE" ${vaccin.voie == 'ORALE' ? 'selected' : ''}>Orale</option>
            <option value="NASALE" ${vaccin.voie == 'NASALE' ? 'selected' : ''}>Nasale</option>
          </select>
        </div>
        <div class="field"><label>Délai de rappel (jours)</label><input class="input" type="number" min="0" name="delaiRappel" value="${vaccin.delaiRappel}"></div>
        <div class="field span-2"><label>Description</label><textarea class="textarea" name="description">${vaccin.description}</textarea></div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/vaccins">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
