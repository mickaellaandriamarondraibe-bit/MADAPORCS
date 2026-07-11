<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Rapports" />
<c:set var="crumbs"    value="Données / <b>Rapports</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Rapports</h1><p>Générez des rapports PDF par domaine, ou exportez les données brutes</p></div>
</div>

<%-- ===== Bilan financier (filtre par dates) ===== --%>
<div class="card" style="margin-bottom:18px">
  <div class="card__head"><h2> Bilan financier</h2></div>
  <div class="card__body">

    <c:if test="${not empty erreur}">
      <div class="alert alert--error" style="margin-bottom:12px">${erreur}</div>
    </c:if>

    <form method="get" action="${ctx}/rapports" class="filtre-form"
          style="display:flex;gap:12px;align-items:flex-end;flex-wrap:wrap;margin-bottom:16px">
      <div class="field">
        <label>Date début</label>
        <input type="date" name="dateDebut" value="${filtre.dateDebut}" class="input" />
      </div>
      <div class="field">
        <label>Date fin</label>
        <input type="date" name="dateFin" value="${filtre.dateFin}" class="input" />
      </div>
      <button class="btn btn--primary" type="submit">Calculer</button>
    </form>

    <div class="grid-3">
      <div class="card"><div class="card__body">
        <p class="muted" style="font-size:12.5px">Total ventes (validées)</p>
        <h2 style="color:#1b7f3b">${rapport.totalVentes} Ar</h2>
        <p class="muted" style="font-size:12px">${rapport.nombreVentes} vente(s)</p>
      </div></div>
      <div class="card"><div class="card__body">
        <p class="muted" style="font-size:12.5px">Total dépenses</p>
        <h2 style="color:#b3261e">${rapport.totalDepenses} Ar</h2>
        <p class="muted" style="font-size:12px">${rapport.nombreDepenses} dépense(s)</p>
      </div></div>
      <div class="card"><div class="card__body">
        <p class="muted" style="font-size:12.5px">Bénéfice net</p>
        <h2>${rapport.beneficeNet} Ar</h2>
        <p class="muted" style="font-size:12px">
          <c:choose>
            <c:when test="${empty filtre.dateDebut and empty filtre.dateFin}">Toute la période</c:when>
            <c:otherwise>${filtre.dateDebut} → ${filtre.dateFin}</c:otherwise>
          </c:choose>
        </p>
      </div></div>
    </div>

  </div>
</div>

<div class="grid-3">
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"> Sanitaire</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Vaccinations et suivis sur la période.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/sanitaire/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"> Commercial</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Ventes, clients et chiffre d'affaires.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/commercial/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"> Financier</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Recettes, dépenses et bénéfice net.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/financier/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"> Reproduction</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Groupes, mises bas et taux de fertilité.</p>
      <a class="btn btn--primary w-full" style="justify-content:center" href="${ctx}/rapports/reproduction/pdf">Générer le PDF</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"> Export global Excel</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Toutes les données dans un classeur.</p>
      <a class="btn btn--gold w-full" style="justify-content:center" href="${ctx}/rapports/export/excel">Télécharger Excel</a>
    </div>
  </div>
  <div class="card">
    <div class="card__body">
      <h2 style="font-size:15px;margin-bottom:4px"> Import / Export</h2>
      <p class="muted" style="font-size:12.5px;margin-bottom:14px">Outils détaillés par module.</p>
      <a class="btn btn--ghost w-full" style="justify-content:center" href="${ctx}/imports">Ouvrir les outils</a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
