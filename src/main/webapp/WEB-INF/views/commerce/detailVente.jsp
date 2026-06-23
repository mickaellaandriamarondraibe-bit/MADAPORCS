<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="v" value="${vente}" />
<c:set var="pageTitle" value="Vente ${v.reference}" />
<c:set var="crumbs"    value="Commerce / Ventes / <b>${v.reference}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${v.reference}
      <c:choose>
        <c:when test="${v.statut == 'VALIDEE'}"><span class="badge badge--green">Validée</span></c:when>
        <c:when test="${v.statut == 'ANNULEE'}"><span class="badge badge--red">Annulée</span></c:when>
        <c:otherwise><span class="badge badge--amber">Brouillon</span></c:otherwise>
      </c:choose>
    </h1>
    <p>Client : ${v.nomClient} · <fmt:formatDate value="${v.dateVente}" pattern="dd/MM/yyyy"/></p>
  </div>
  <div class="flex gap-8">
    <a class="btn btn--ghost" href="${ctx}/ventes/recu/pdf/${v.id}"><i class="fa-solid fa-file-pdf"></i> Reçu PDF</a>
    <c:if test="${v.statut == 'BROUILLON'}">
      <form method="post" action="${ctx}/ventes/valider/${v.id}" data-confirm="Valider cette vente ? L'effectif des lots sera diminué." style="display:inline">
        <button class="btn btn--primary" type="submit">Valider</button>
      </form>
    </c:if>
    <c:if test="${v.statut != 'ANNULEE'}">
      <form method="post" action="${ctx}/ventes/annuler/${v.id}" data-confirm="Annuler cette vente ?" style="display:inline">
        <button class="btn btn--danger" type="submit">Annuler</button>
      </form>
    </c:if>
  </div>
</div>

<div class="card">
  <div class="card__head"><h2>Détail des lignes</h2></div>
  <div class="card__body" style="padding:0">
    <table class="tbl">
      <thead><tr><th>Lot</th><th class="num">Quantité</th><th class="num">Prix unitaire (Ar)</th><th class="num">Total (Ar)</th></tr></thead>
      <tbody>
        <c:forEach var="d" items="${v.lignes}">
          <tr>
            <td><b>${d.codeLot}</b></td>
            <td class="num">${d.quantite}</td>
            <td class="num"><fmt:formatNumber value="${d.prixUnitaire}" type="number" maxFractionDigits="0"/></td>
            <td class="num"><fmt:formatNumber value="${d.total}" type="number" maxFractionDigits="0"/></td>
          </tr>
        </c:forEach>
      </tbody>
      <tfoot>
        <tr><td colspan="3" class="right"><b>Montant total</b></td><td class="num"><b><fmt:formatNumber value="${v.montantTotal}" type="number" maxFractionDigits="0"/> Ar</b></td></tr>
      </tfoot>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
