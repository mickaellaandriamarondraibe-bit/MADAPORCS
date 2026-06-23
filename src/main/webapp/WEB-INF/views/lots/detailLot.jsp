<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="l" value="${detail}" />
<c:set var="pageTitle" value="Lot ${l.codeLot}" />
<c:set var="crumbs"    value="Cheptel / Lots / <b>${l.codeLot}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${l.codeLot}
      <c:choose>
        <c:when test="${l.sexe == 'FEMELLE'}"><span class="badge badge--gold"><i class="fa-solid fa-venus"></i> Femelle</span></c:when>
        <c:otherwise><span class="badge badge--blue"><i class="fa-solid fa-mars"></i> Mâle</span></c:otherwise>
      </c:choose>
      <c:if test="${l.statut == 'ARCHIVE'}"><span class="badge badge--gray">Archivé</span></c:if>
    </h1>
    <p>${l.race} · ${l.objectif}</p>
  </div>
  <div class="flex gap-8">
    <a class="btn btn--ghost" href="${ctx}/lots/${l.id}/pesees"><i class="fa-solid fa-scale-balanced"></i> Pesées</a>
    <a class="btn btn--ghost" href="${ctx}/lots/${l.id}/mouvements"><i class="fa-solid fa-right-left"></i> Mouvements</a>
    <a class="btn btn--primary" href="${ctx}/lots/form?id=${l.id}">Modifier</a>
    <c:if test="${l.statut == 'ACTIF'}">
      <form method="post" action="${ctx}/lots/archive/${l.id}" data-confirm="Archiver ce lot ?" style="display:inline">
        <button class="btn btn--danger" type="submit">Archiver</button>
      </form>
    </c:if>
  </div>
</div>

<div class="kpi-grid mb-16">
  <div class="kpi"><div class="kpi__label">Effectif actuel</div><div class="kpi__value">${l.effectifActuel}</div><div class="kpi__sub">initial : ${l.effectifInitial}</div></div>
  <div class="kpi kpi--gold"><div class="kpi__label">Poids moyen</div><div class="kpi__value">${empty l.poidsMoyen ? '—' : l.poidsMoyen}</div><div class="kpi__sub">kg / tête</div></div>
  <div class="kpi kpi--blue"><div class="kpi__label">Mouvements</div><div class="kpi__value">${empty l.nbMouvements ? 0 : l.nbMouvements}</div><div class="kpi__sub">enregistrés</div></div>
  <div class="kpi"><div class="kpi__label">Pesées</div><div class="kpi__value">${empty l.nbPesees ? 0 : l.nbPesees}</div><div class="kpi__sub">historisées</div></div>
</div>

<div class="grid-2">
  <div class="card">
    <div class="card__head"><h2>Informations</h2></div>
    <div class="card__body">
      <dl class="dl">
        <dt>Code</dt><dd>${l.codeLot}</dd>
        <dt>Sexe</dt><dd>${l.sexe}</dd>
        <dt>Race</dt><dd>${l.race}</dd>
        <dt>Objectif</dt><dd>${l.objectif}</dd>
        <dt>Date d'entrée</dt><dd><fmt:formatDate value="${l.dateEntree}" pattern="dd/MM/yyyy"/></dd>
        <dt>Statut</dt><dd>${l.statut}</dd>
        <dt>Observation</dt><dd>${empty l.observation ? '—' : l.observation}</dd>
      </dl>
    </div>
  </div>

  <div class="card">
    <div class="card__head"><h2>Derniers mouvements</h2><a class="btn btn--ghost btn--sm" href="${ctx}/lots/${l.id}/mouvements">Tout voir</a></div>
    <div class="card__body" style="padding:0">
      <table class="tbl">
        <thead><tr><th>Date</th><th>Type</th><th class="num">Qté</th></tr></thead>
        <tbody>
          <c:choose>
            <c:when test="${not empty l.derniersMouvements}">
              <c:forEach var="m" items="${l.derniersMouvements}">
                <tr><td><fmt:formatDate value="${m.dateMouvement}" pattern="dd/MM/yyyy"/></td><td><span class="badge badge--green">${m.typeMouvement}</span></td><td class="num">${m.quantite}</td></tr>
              </c:forEach>
            </c:when>
            <c:otherwise><tr><td colspan="3"><div class="empty" style="padding:24px">Aucun mouvement.</div></td></tr></c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
