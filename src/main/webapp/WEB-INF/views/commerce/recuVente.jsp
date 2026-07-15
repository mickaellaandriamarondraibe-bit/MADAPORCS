<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="v" value="${vente}" />
<c:set var="pageTitle" value="Reçu ${v.reference}" />
<c:set var="crumbs"    value="Commerce / Ventes / <b>Reçu</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Reçu de vente</h1>
    <p>Aperçu du reçu, puis téléchargez-le en PDF</p>
  </div>
  <div class="flex gap-8">
    <a class="btn btn--ghost" href="${ctx}/ventes/${v.id}">Retour</a>
    <a class="btn btn--primary" href="${ctx}/ventes/recu/pdf/${v.id}" download>Télécharger (PDF)</a>
  </div>
</div>

<div class="card recu" style="max-width:640px">
  <div class="recu__body">

    <div class="recu__brand">
      <div class="recu__logo">MP</div>
      <div>
        <div class="recu__brand-name">MADAPORC</div>
        <div class="recu__brand-sub">Gestion d'élevage</div>
      </div>
    </div>

    <h2 class="recu__title">REÇU DE VENTE</h2>
    <p class="recu__lead">Ce reçu est délivré pour servir et valoir ce que de droit.</p>

    <div class="recu__cols">
      <div>
        <div class="recu__label">Vendeur</div>
        <div class="recu__strong">MADAPORC</div>
        <div>Gestion d'élevage porcin</div>
      </div>
      <div>
        <div class="recu__label">Client</div>
        <div class="recu__strong">${v.nomClient}</div>
        <c:if test="${not empty v.client.telephone}"><div>${v.client.telephone}</div></c:if>
        <c:if test="${not empty v.client.adresse}"><div>${v.client.adresse}</div></c:if>
      </div>
    </div>

    <div class="recu__meta">
      <div><span class="recu__label">Référence</span><b>${v.reference}</b></div>
      <div><span class="recu__label">Date de vente</span><b>${v.dateVente}</b></div>
      <div><span class="recu__label">Statut</span>
        <c:choose>
          <c:when test="${v.statut == 'VALIDEE'}"><b>Validée</b></c:when>
          <c:when test="${v.statut == 'ANNULEE'}"><b>Annulée</b></c:when>
          <c:otherwise><b>Brouillon</b></c:otherwise>
        </c:choose>
      </div>
    </div>

    <div class="recu__label recu__section">Détail des lignes</div>
    <table class="recu__table">
      <thead>
        <tr><th>Lot</th><th class="num">Qté</th><th class="num">Prix unitaire</th><th class="num">Montant</th></tr>
      </thead>
      <tbody>
        <c:forEach var="d" items="${v.lignes}">
          <tr>
            <td><b>${d.codeLot}</b></td>
            <td class="num">${d.quantite}</td>
            <td class="num"><fmt:formatNumber value="${d.prixUnitaire}" type="number" maxFractionDigits="0"/> Ar</td>
            <td class="num"><fmt:formatNumber value="${d.total}" type="number" maxFractionDigits="0"/> Ar</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <div class="recu__total">
      <span>TOTAL</span>
      <span><fmt:formatNumber value="${v.montantTotal}" type="number" maxFractionDigits="0"/> Ar</span>
    </div>

    <c:if test="${v.statut == 'VALIDEE'}">
      <div class="recu__paid">
        <span>Montant payé</span>
        <span><fmt:formatNumber value="${v.montantTotal}" type="number" maxFractionDigits="0"/> Ar</span>
      </div>
    </c:if>

    <div class="recu__foot">Merci de votre confiance.</div>
  </div>
</div>

<style>
  .recu__body { padding: 34px 38px; }
  .recu__brand { display: flex; align-items: center; gap: 10px; justify-content: center; }
  .recu__logo { width: 38px; height: 38px; border-radius: 9px; background: var(--marine-600); color: #fff; display: grid; place-items: center; font-weight: 800; }
  .recu__brand-name { font-weight: 800; color: var(--marine-900); letter-spacing: .5px; }
  .recu__brand-sub { font-size: 11px; color: var(--gris-500); }
  .recu__title { text-align: center; color: var(--marine-800); letter-spacing: 2px; margin: 18px 0 4px; font-size: 20px; }
  .recu__lead { text-align: center; color: var(--gris-500); font-size: 12.5px; margin-bottom: 22px; }
  .recu__cols { display: flex; gap: 24px; border-top: 1px solid var(--gris-200); padding-top: 16px; }
  .recu__cols > div { flex: 1; }
  .recu__label { text-transform: uppercase; font-size: 10.5px; letter-spacing: .6px; color: var(--gris-500); font-weight: 700; display: block; margin-bottom: 3px; }
  .recu__strong { font-weight: 700; color: var(--gris-900); }
  .recu__meta { display: flex; gap: 24px; flex-wrap: wrap; margin-top: 18px; padding-top: 16px; border-top: 1px solid var(--gris-200); }
  .recu__meta > div { display: flex; flex-direction: column; gap: 2px; }
  .recu__section { margin-top: 22px; }
  .recu__table { width: 100%; border-collapse: collapse; margin-top: 8px; font-size: 13px; }
  .recu__table th { text-align: left; font-size: 11px; text-transform: uppercase; letter-spacing: .4px; color: var(--gris-500); border-bottom: 2px solid var(--gris-200); padding: 8px 6px; }
  .recu__table td { padding: 9px 6px; border-bottom: 1px solid var(--gris-100); }
  .recu__table .num { text-align: right; }
  .recu__total { display: flex; justify-content: space-between; align-items: center; margin-top: 14px; padding: 10px 6px; font-weight: 800; color: var(--marine-800); font-size: 15px; border-top: 2px solid var(--gris-200); }
  .recu__paid { display: flex; justify-content: space-between; align-items: center; margin-top: 10px; padding: 11px 14px; border-radius: 9px; background: var(--vert-050); color: var(--vert-700); font-weight: 700; border: 1px solid var(--vert-100); }
  .recu__foot { text-align: center; color: var(--gris-400); font-size: 11.5px; margin-top: 24px; }
</style>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
