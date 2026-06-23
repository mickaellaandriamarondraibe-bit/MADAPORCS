<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty client.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier le client' : 'Nouveau client'}" />
<c:set var="crumbs"    value="Commerce / Clients / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1></div>
  <a class="btn btn--ghost" href="${ctx}/clients"><i class="fa-solid fa-arrow-left"></i> Retour</a>
</div>

<div class="card" style="max-width:760px">
  <div class="card__body">
    <%-- POST /clients/save, bind ClientDTO --%>
    <form method="post" action="${ctx}/clients/save">
      <input type="hidden" name="id" value="${client.id}">
      <div class="form-grid">
        <div class="field"><label>Nom <span class="req">*</span></label><input class="input" name="nom" value="${client.nom}" required></div>
        <div class="field"><label>Téléphone</label><input class="input" name="telephone" value="${client.telephone}"></div>
        <div class="field"><label>E-mail</label><input class="input" type="email" name="email" value="${client.email}"></div>
        <div class="field"><label>Type</label>
          <select class="select" name="type">
            <option value="PARTICULIER" ${client.type == 'PARTICULIER' ? 'selected' : ''}>Particulier</option>
            <option value="GROSSISTE" ${client.type == 'GROSSISTE' ? 'selected' : ''}>Grossiste</option>
          </select>
        </div>
        <div class="field span-2"><label>Adresse</label><textarea class="textarea" name="adresse">${client.adresse}</textarea></div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/clients">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
