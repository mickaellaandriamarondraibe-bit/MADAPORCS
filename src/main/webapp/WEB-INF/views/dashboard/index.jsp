<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Tableau de bord" />
<c:set var="crumbs"    value="MADAPORC / <b>Tableau de bord</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<%-- Le contrôleur fournit l'objet 'dashboard' (DashboardDTO). Valeurs par défaut si absent. --%>
<c:set var="d" value="${dashboard}" />

<div class="page-head">
  <div>
    <h1>Vue d'ensemble</h1>
    <p>Indicateurs clés de l'élevage — <fmt:formatDate value="${now}" pattern="dd/MM/yyyy" /></p>
  </div>
  <a class="btn btn--gold" href="${ctx}/rapports"><i class="fa-solid fa-file-lines"></i> Générer un rapport</a>
</div>

<%-- ===================== KPI ===================== --%>
<div class="kpi-grid">
  <div class="kpi">
    <div class="kpi__label">Lots actifs</div>
    <div class="kpi__value"><c:out value="${d.lotsActifs}" default="0"/></div>
    <div class="kpi__sub"><c:out value="${d.totalPorcs}" default="0"/> porcs au total</div>
  </div>
  <div class="kpi">
    <div class="kpi__label">Groupes reproduction</div>
    <div class="kpi__value"><c:out value="${d.groupesReproductionActifs}" default="0"/></div>
    <div class="kpi__sub"><c:out value="${d.misesBasProches}" default="0"/> mise(s) bas proche(s)</div>
  </div>
  <div class="kpi kpi--gold">
    <div class="kpi__label">Taux d'aptitude global</div>
    <div class="kpi__value"><c:out value="${d.tauxAptitudeGlobal}" default="0"/>%</div>
    <div class="kpi__sub up">Fertilité observée <c:out value="${d.tauxFertiliteGlobal}" default="0"/>%</div>
  </div>
  <div class="kpi kpi--blue">
    <div class="kpi__label">Ventes du mois</div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.ventesMois ? 0 : d.ventesMois}" type="number" maxFractionDigits="0"/></div>
    <div class="kpi__sub">Ar — chiffre d'affaires</div>
  </div>
  <div class="kpi kpi--danger">
    <div class="kpi__label">Dépenses du mois</div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.depensesMois ? 0 : d.depensesMois}" type="number" maxFractionDigits="0"/></div>
    <div class="kpi__sub">Ar — sorties</div>
  </div>
  <div class="kpi ${ (empty d.beneficeNet ? 0 : d.beneficeNet) >= 0 ? '' : 'kpi--danger'}">
    <div class="kpi__label">Bénéfice net</div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.beneficeNet ? 0 : d.beneficeNet}" type="number" maxFractionDigits="0"/></div>
    <div class="kpi__sub ${ (empty d.beneficeNet ? 0 : d.beneficeNet) >= 0 ? 'up' : 'down'}">Ar — sur le mois</div>
  </div>
</div>

<%-- ===================== Détails ===================== --%>
<div class="grid-2 mt-24">

  <%-- Alertes de reproduction --%>
  <div class="card">
    <div class="card__head">
      <h2>Alertes de mise bas proche</h2>
      <a class="btn btn--ghost btn--sm" href="${ctx}/reproduction/alertes">Voir tout</a>
    </div>
    <div class="card__body" style="padding:0">
      <div class="table-wrap">
        <table class="tbl">
          <thead>
            <tr><th>Groupe</th><th>Lot femelle</th><th>Date prévue</th><th>Statut</th></tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty d.alertes}">
                <c:forEach var="a" items="${d.alertes}">
                  <tr>
                    <td><b>${a.codeGroupe}</b></td>
                    <td>${a.codeLotFemelle}</td>
                    <td><fmt:formatDate value="${a.datePrevue}" pattern="dd/MM/yyyy"/></td>
                    <td><span class="badge badge--amber"><span class="dot"></span>${a.statut}</span></td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr><td colspan="4"><div class="empty" style="padding:28px">Aucune alerte active.</div></td></tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <%-- Colonne droite : stocks faibles + vaccinations --%>
  <div class="stack">
    <div class="card">
      <div class="card__head"><h2>Stocks faibles</h2><a class="btn btn--ghost btn--sm" href="${ctx}/ingredients">Gérer</a></div>
      <div class="card__body" style="padding:0">
        <table class="tbl">
          <thead><tr><th>Ingrédient</th><th class="num">Stock</th></tr></thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty d.stocksFaibles}">
                <c:forEach var="i" items="${d.stocksFaibles}">
                  <tr>
                    <td>${i.nom} <span class="badge badge--red">bas</span></td>
                    <td class="num">${i.quantiteStock} ${i.unite}</td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise><tr><td colspan="2"><div class="empty" style="padding:22px">Tous les stocks sont corrects.</div></td></tr></c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>

    <div class="card">
      <div class="card__head"><h2>Vaccinations à venir</h2><a class="btn btn--ghost btn--sm" href="${ctx}/vaccinations">Planning</a></div>
      <div class="card__body" style="padding:0">
        <table class="tbl">
          <thead><tr><th>Lot</th><th>Vaccin</th><th>Date</th></tr></thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty d.vaccinationsAVenir}">
                <c:forEach var="v" items="${d.vaccinationsAVenir}">
                  <tr><td>${v.codeLot}</td><td>${v.nomVaccin}</td><td><fmt:formatDate value="${v.datePrevue}" pattern="dd/MM"/></td></tr>
                </c:forEach>
              </c:when>
              <c:otherwise><tr><td colspan="3"><div class="empty" style="padding:22px">Aucune vaccination planifiée.</div></td></tr></c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
