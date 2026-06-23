<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Rapports" />
<c:set var="crumbs"    value="Données / <b>Rapports</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Rapports</h1><p>Générez des rapports PDF par domaine, ou exportez les données brutes</p></div>
</div>

<div class="grid-3">
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"><i class="fa-solid fa-stethoscope"></i> Sanitaire</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Vaccinations et suivis sur la période.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/sanitaire/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"><i class="fa-solid fa-receipt"></i> Commercial</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Ventes, clients et chiffre d'affaires.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/commercial/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"><i class="fa-solid fa-sack-dollar"></i> Financier</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Recettes, dépenses et bénéfice net.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/financier/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"><i class="fa-solid fa-venus-mars"></i> Reproduction</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Groupes, mises bas et taux de fertilité.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/reproduction/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"><i class="fa-solid fa-file-excel"></i> Export global Excel</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Toutes les données dans un classeur.</p>
      <a class="btn btn--gold w-full" style="justify-content:center" href="${ctx}/rapports/export/excel">Télécharger Excel</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"><i class="fa-solid fa-file-import"></i> Import / Export</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Outils détaillés par module.</p>
      <a class="btn btn--ghost w-full" style="justify-content:center" href="${ctx}/imports">Ouvrir les outils</a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
