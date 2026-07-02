<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="cl" value="${client}" />
<c:set var="pageTitle" value="Client ${cl.nom}" />
<c:set var="crumbs"    value="Commerce / Clients / <b>${cl.nom}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${cl.nom}</h1>
    <p>Fiche client</p>
  </div>
  <div class="flex gap-8">
    <a class="btn btn--ghost" href="${ctx}/clients"> Retour</a>
    <a class="btn btn--primary" href="${ctx}/clients/form?id=${cl.id}"> Modifier</a>
  </div>
</div>

<div class="card" style="max-width:760px">
  <div class="card__head"><h2>Informations</h2></div>
  <div class="card__body">
    <div class="form-grid">
      <div class="field"><label>Nom</label><div><b>${cl.nom}</b></div></div>
      <div class="field"><label>Téléphone</label><div>${not empty cl.telephone ? cl.telephone : '—'}</div></div>
      <div class="field span-2"><label>Adresse</label><div class="muted">${not empty cl.adresse ? cl.adresse : '—'}</div></div>
      <div class="field"><label>Créé le</label><div>${empty cl.createdAt ? '—' : cl.createdAt}</div></div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
