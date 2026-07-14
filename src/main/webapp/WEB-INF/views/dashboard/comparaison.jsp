<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Comparaison" />
<c:set var="crumbs" value="MADAPORC / Tableau de bord / <b>Comparaison</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Comparaison mensuelle</h1>
    <p>Synthèse comparative entre deux mois</p>
  </div>
  <a class="btn btn--ghost" href="${ctx}/dashboard">Retour</a>
</div>

<form method="get" action="${ctx}/dashboard/comparaison" class="toolbar">
  <input class="input" type="month" name="mois1" value="${mois1}" style="width:auto">
  <span class="muted">vs</span>
  <input class="input" type="month" name="mois2" value="${mois2}" style="width:auto">
  <button class="btn btn--primary" type="submit">Comparer</button>
</form>

<div class="card">
  <div class="table-wrap">
    <table class="tbl">
      <thead>
        <tr><th>Indicateur</th><th class="num">${mois1}</th><th class="num">${mois2}</th><th class="num">Écart</th></tr>
      </thead>
      <tbody>
        <tr><td>Lots actifs</td><td class="num">${d1.lotsActifs}</td><td class="num">${d2.lotsActifs}</td><td class="num">${d2.lotsActifs - d1.lotsActifs}</td></tr>
        <tr><td>Porcs actifs</td><td class="num">${d1.totalPorcs}</td><td class="num">${d2.totalPorcs}</td><td class="num">${d2.totalPorcs - d1.totalPorcs}</td></tr>
        <tr><td>Groupes actifs</td><td class="num">${d1.groupesActifs}</td><td class="num">${d2.groupesActifs}</td><td class="num">${d2.groupesActifs - d1.groupesActifs}</td></tr>
        <tr><td>Mises bas proches</td><td class="num">${d1.misesBasProches}</td><td class="num">${d2.misesBasProches}</td><td class="num">${d2.misesBasProches - d1.misesBasProches}</td></tr>
        <tr><td>Ventes (Ar)</td>
          <td class="num"><fmt:formatNumber value="${d1.ventesMois}" type="number" maxFractionDigits="0"/></td>
          <td class="num"><fmt:formatNumber value="${d2.ventesMois}" type="number" maxFractionDigits="0"/></td>
          <td class="num"><fmt:formatNumber value="${d2.ventesMois - d1.ventesMois}" type="number" maxFractionDigits="0"/></td></tr>
        <tr><td>Dépenses (Ar)</td>
          <td class="num"><fmt:formatNumber value="${d1.depensesMois}" type="number" maxFractionDigits="0"/></td>
          <td class="num"><fmt:formatNumber value="${d2.depensesMois}" type="number" maxFractionDigits="0"/></td>
          <td class="num"><fmt:formatNumber value="${d2.depensesMois - d1.depensesMois}" type="number" maxFractionDigits="0"/></td></tr>
        <tr><td>Bénéfice net (Ar)</td>
          <td class="num"><fmt:formatNumber value="${d1.beneficeNet}" type="number" maxFractionDigits="0"/></td>
          <td class="num"><fmt:formatNumber value="${d2.beneficeNet}" type="number" maxFractionDigits="0"/></td>
          <td class="num"><fmt:formatNumber value="${d2.beneficeNet - d1.beneficeNet}" type="number" maxFractionDigits="0"/></td></tr>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
