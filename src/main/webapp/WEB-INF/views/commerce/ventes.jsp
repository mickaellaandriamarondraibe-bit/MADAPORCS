<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Ventes" />
<c:set var="crumbs"    value="Commerce / <b>Ventes</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Ventes</h1><p>Une vente validée diminue l'effectif du lot</p></div>
  <a class="btn btn--primary" href="${ctx}/ventes/form"><i class="fa-solid fa-plus"></i> Nouvelle vente</a>
</div>

<div class="toolbar"><div class="search"><input class="input" placeholder="Rechercher…" data-filter-input="#tbl-ventes"></div></div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-ventes">
      <thead><tr><th>Référence</th><th>Client</th><th>Date</th><th class="num">Montant (Ar)</th><th>Statut</th><th class="actions">Actions</th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty ventes}">
            <c:forEach var="v" items="${ventes}">
              <tr>
                <td><a href="${ctx}/ventes/${v.id}"><b>${v.reference}</b></a></td>
                <td>${v.nomClient}</td>
                <td><fmt:formatDate value="${v.dateVente}" pattern="dd/MM/yyyy"/></td>
                <td class="num"><fmt:formatNumber value="${v.montantTotal}" type="number" maxFractionDigits="0"/></td>
                <td>
                  <c:choose>
                    <c:when test="${v.statut == 'VALIDEE'}"><span class="badge badge--green">Validée</span></c:when>
                    <c:when test="${v.statut == 'ANNULEE'}"><span class="badge badge--red">Annulée</span></c:when>
                    <c:otherwise><span class="badge badge--amber">Brouillon</span></c:otherwise>
                  </c:choose>
                </td>
                <td class="actions">
                  <a href="${ctx}/ventes/${v.id}">Détail</a>
                  <a href="${ctx}/ventes/recu/pdf/${v.id}">Reçu PDF</a>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="6"><div class="empty"><i class="ico fa-solid fa-receipt"></i><p>Aucune vente.</p><a class="btn btn--primary" href="${ctx}/ventes/form">Créer une vente</a></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
