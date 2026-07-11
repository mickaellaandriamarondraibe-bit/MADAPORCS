<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="a" value="${analyse}" />

<c:set var="pageTitle" value="Analyse reproductive" />
<c:set var="crumbs" value="Reproduction / <b>Analyse — ${a.lotPorc}</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Analyse reproductive — ${a.lotPorc}</h1>
    <p>Calcul des taux et décision automatique pour le lot femelle</p>
  </div>

  <form method="post" action="${ctx}/reproduction/analyse/generer/${a.lotId}">
    <button class="btn btn--primary" type="submit">
       Régénérer l'analyse
    </button>
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
      <c:choose>
        <c:when test="${empty a.tauxFertiliteObserve}">
          —
        </c:when>
        <c:otherwise>
          ${a.tauxFertiliteObserve}%
        </c:otherwise>
      </c:choose>
    </div>
    <div class="kpi__sub">gestantes / saillies</div>
  </div>

  <div class="kpi">
    <div class="kpi__label">Total femelles</div>
    <div class="kpi__value">${a.nbFemellesTotal}</div>
    <div class="kpi__sub">dans le lot</div>
  </div>

</div>

<div class="grid-2">

  <div class="card">
    <div class="card__head">
      <h2>Répartition reproductive</h2>
    </div>

    <div class="card__body" style="padding:0">
      <table class="tbl">
        <thead>
          <tr>
            <th>Statut</th>
            <th class="num">Effectif</th>
          </tr>
        </thead>

        <tbody>
          <tr>
            <td>
              <span class="badge badge--green">Prêtes jamais saillies</span>
            </td>
            <td class="num">${a.nbPretesJamaisSaillies}</td>
          </tr>

          <tr>
            <td>
              <span class="badge badge--green">Déjà reproductrices aptes</span>
            </td>
            <td class="num">${a.nbDejaReproductricesAptes}</td>
          </tr>

          <tr>
            <td>
              <span class="badge badge--amber">En cycle</span>
            </td>
            <td class="num">${a.nbEnCycle}</td>
          </tr>

          <tr>
            <td>
              <span class="badge badge--amber">À surveiller</span>
            </td>
            <td class="num">${a.nbASurveiller}</td>
          </tr>

          <tr>
            <td>
              <span class="badge badge--red">À retirer reproduction</span>
            </td>
            <td class="num">${a.nbARetirerReproduction}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <div class="card">
    <div class="card__head">
      <h2>Décision automatique</h2>
    </div>

    <div class="card__body">

      <c:choose>
        <c:when test="${a.decision == 'APTE A LA REPRODUCTION'}">
          <div class="alert alert--ok">
            <span><b>${a.decision}</b></span>
          </div>
        </c:when>

        <c:when test="${a.decision == 'REFORME RECOMMANDEE'}">
          <div class="alert alert--err">
            <span><b>${a.decision}</b></span>
          </div>
        </c:when>

        <c:otherwise>
          <div class="alert alert--warn">
            <span><b>${a.decision}</b></span>
          </div>
        </c:otherwise>
      </c:choose>

      <dl class="dl mt-16">
        <dt>Femelles saillies</dt>
        <dd>${a.nbFemellesSailliesTotal}</dd>

        <dt>Femelles gestantes</dt>
        <dd>${a.nbFemellesGestantesTotal}</dd>

        <dt>Total femelles</dt>
        <dd>${a.nbFemellesTotal}</dd>
      </dl>

      <p class="muted mt-16" style="font-size:12px">
        Les femelles jamais saillies ne sont pas considérées infertiles.
        La fertilité ne se calcule que sur les femelles déjà saillies.
      </p>
    </div>
  </div>

</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>