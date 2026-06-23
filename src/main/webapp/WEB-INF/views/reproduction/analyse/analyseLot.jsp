<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="a" value="${analyse}" />
<c:set var="pageTitle" value="Analyse reproductive" />
<c:set var="crumbs"    value="Reproduction / <b>Analyse — ${a.codeLot}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Analyse reproductive — ${a.codeLot}</h1><p>Calcul des taux et décision automatique pour le lot femelle</p></div>
  <%-- POST /reproduction/analyse/generer/{lotId} --%>
  <form method="post" action="${ctx}/reproduction/analyse/generer/${a.lotId}">
    <button class="btn btn--primary" type="submit"><i class="fa-solid fa-rotate"></i> Régénérer l'analyse</button>
  </form>
</div>

<div class="kpi-grid mb-16">
  <div class="kpi kpi--gold">
    <div class="kpi__label">Aptitude globale</div>
    <div class="kpi__value">${a.tauxAptitudeGlobal}%</div>
    <div class="kpi__sub">prêtes + aptes + à surveiller</div>
  </div>
  <div class="kpi">
    <div class="kpi__label">Taux recommandé</div>
    <div class="kpi__value">${a.tauxRecommande}%</div>
    <div class="kpi__sub">prêtes + déjà aptes</div>
  </div>
  <div class="kpi kpi--blue">
    <div class="kpi__label">Fertilité observée</div>
    <div class="kpi__value">
      <c:choose><c:when test="${empty a.tauxFertiliteObserve}">—</c:when><c:otherwise>${a.tauxFertiliteObserve}%</c:otherwise></c:choose>
    </div>
    <div class="kpi__sub">gestantes / saillies</div>
  </div>
  <div class="kpi">
    <div class="kpi__label">Total femelles</div>
    <div class="kpi__value">${a.totalFemelles}</div>
    <div class="kpi__sub">dans le lot</div>
  </div>
</div>

<div class="grid-2">
  <div class="card">
    <div class="card__head"><h2>Répartition reproductive</h2></div>
    <div class="card__body" style="padding:0">
      <table class="tbl">
        <thead><tr><th>Statut</th><th class="num">Effectif</th></tr></thead>
        <tbody>
          <tr><td><span class="badge badge--green">Prêtes (jamais saillies)</span></td><td class="num">${a.pretes}</td></tr>
          <tr><td><span class="badge badge--green">Déjà aptes</span></td><td class="num">${a.dejaAptes}</td></tr>
          <tr><td><span class="badge badge--amber">En cycle</span></td><td class="num">${a.enCycle}</td></tr>
          <tr><td><span class="badge badge--amber">À surveiller</span></td><td class="num">${a.aSurveiller}</td></tr>
          <tr><td><span class="badge badge--red">À retirer</span></td><td class="num">${a.aRetirer}</td></tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="card">
    <div class="card__head"><h2>Décision automatique</h2></div>
    <div class="card__body">
      <div class="alert ${a.decision == 'APTE' ? 'alert--ok' : (a.decision == 'A_RETIRER' ? 'alert--err' : 'alert--warn')}">
        <span><b>${a.decision}</b> — ${a.commentaireDecision}</span>
      </div>
      <dl class="dl mt-16">
        <dt>Femelles saillies</dt><dd>${a.femellesSaillies}</dd>
        <dt>Femelles gestantes</dt><dd>${a.femellesGestantes}</dd>
        <dt>Femelles disponibles</dt><dd>${a.femellesDisponibles}</dd>
      </dl>
      <p class="muted mt-16" style="font-size:12px">Les femelles jamais saillies ne sont pas considérées infertiles. La fertilité ne se calcule que sur les femelles déjà saillies.</p>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
