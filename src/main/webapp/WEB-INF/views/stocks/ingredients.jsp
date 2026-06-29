<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Ingrédients" />
<c:set var="crumbs"    value="Stocks / <b>Ingrédients</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Ingrédients</h1><p>Aliments et matières premières en stock</p></div>
  <div class="flex gap-8">
    <a class="btn btn--ghost" href="${ctx}/stocks/mouvements"><i class="fa-solid fa-right-left"></i> Mouvements</a>
    <a class="btn btn--primary" href="${ctx}/ingredients/form"><i class="fa-solid fa-plus"></i> Nouvel ingrédient</a>
  </div>
</div>

<div class="toolbar"><div class="search"><input class="input" placeholder="Rechercher…" data-filter-input="#tbl-ing"></div></div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-ing">
      <thead><tr><th>Nom</th><th>Unité</th><th class="num">Stock actuel</th><th class="num">Seuil d'alerte</th><th>État</th><th class="actions"></th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty ingredients}">
            <c:forEach var="i" items="${ingredients}">
              <tr>
                <td><b>${i.nom}</b></td><td>${i.unite}</td>
                <td class="num">${i.stockActuel}</td><td class="num">${i.seuilAlerte}</td>
                <td>
                  <c:choose>
                    <c:when test="${i.stockActuel <= i.seuilAlerte}"><span class="badge badge--red"><span class="dot"></span>Stock bas</span></c:when>
                    <c:otherwise><span class="badge badge--green">OK</span></c:otherwise>
                  </c:choose>
                </td>
                <td class="actions"><a href="${ctx}/ingredients/form?id=${i.id}">Modifier</a></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="6"><div class="empty"><i class="ico fa-solid fa-wheat-awn"></i><p>Aucun ingrédient.</p><a class="btn btn--primary" href="${ctx}/ingredients/form">Ajouter</a></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
