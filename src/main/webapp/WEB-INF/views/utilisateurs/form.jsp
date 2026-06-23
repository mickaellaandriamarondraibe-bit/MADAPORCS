<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty utilisateur.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier un utilisateur' : 'Nouvel utilisateur'}" />
<c:set var="crumbs"    value="Administration / Utilisateurs / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1><p>Renseignez les informations du compte</p></div>
  <a class="btn btn--ghost" href="${ctx}/utilisateurs"><i class="fa-solid fa-arrow-left"></i> Retour à la liste</a>
</div>

<div class="card" style="max-width:760px">
  <div class="card__body">
    <%-- POST vers UtilisateurController @PostMapping("/utilisateurs/save"), bind UtilisateurDTO --%>
    <form method="post" action="${ctx}/utilisateurs/save">
      <input type="hidden" name="id" value="${utilisateur.id}">
      <div class="form-grid">
        <div class="field">
          <label for="nom">Nom complet <span class="req">*</span></label>
          <input class="input" id="nom" name="nom" value="${utilisateur.nom}" required>
        </div>
        <div class="field">
          <label for="email">E-mail <span class="req">*</span></label>
          <input class="input" type="email" id="email" name="email" value="${utilisateur.email}" required>
          <span class="hint">Doit être unique.</span>
        </div>
        <div class="field">
          <label for="motDePasse">Mot de passe ${edition ? '' : '*'}</label>
          <input class="input" type="password" id="motDePasse" name="motDePasse" ${edition ? '' : 'required'}>
          <span class="hint">${edition ? 'Laisser vide pour conserver le mot de passe actuel.' : 'Sera stocké de façon hashée.'}</span>
        </div>
        <div class="field">
          <label for="role">Rôle <span class="req">*</span></label>
          <select class="select" id="role" name="role" required>
            <option value="">— Choisir —</option>
            <option value="ADMIN" ${utilisateur.role == 'ADMIN' ? 'selected' : ''}>ADMIN</option>
            <option value="GESTIONNAIRE" ${utilisateur.role == 'GESTIONNAIRE' ? 'selected' : ''}>GESTIONNAIRE</option>
          </select>
        </div>
        <div class="field span-2">
          <label class="flex items-center gap-8" style="font-weight:600">
            <input type="checkbox" name="actif" value="true" ${empty utilisateur.id or utilisateur.actif ? 'checked' : ''}>
            Compte actif
          </label>
          <span class="hint">Un compte désactivé ne peut pas se connecter.</span>
        </div>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/utilisateurs">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer le compte'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
