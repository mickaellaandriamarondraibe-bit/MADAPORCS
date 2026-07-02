<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Dépenses" />
<c:set var="crumbs"    value="Finance / <b>Dépenses</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Dépenses</h1><p>Charges et sorties de trésorerie</p></div>
  <a class="btn btn--primary" href="${ctx}/depenses/form"> Nouvelle dépense</a>
</div>

<%-- Filtre par période / catégorie -> GET /depenses --%>
<form method="get" action="${ctx}/depenses" class="toolbar">
  <input class="input" type="date" name="dateDebut" value="${filtre.dateDebut}">
  <span class="muted"></span>
  <input class="input" type="date" name="dateFin" value="${filtre.dateFin}">
  <select class="select" name="categorieId">
    <option value="">Catégorie (toutes)</option>
    <c:forEach var="cat" items="${categories}">
      <option value="${cat.id}" ${filtre.categorieId == cat.id ? 'selected' : ''}>${cat.nom}</option>
    </c:forEach>
  </select>
  <button class="btn btn--ghost" type="submit">Filtrer</button>
  <a class="btn btn--ghost" href="${ctx}/depenses">Réinitialiser</a>
</form>

<div class="card">
  <div class="table-wrap">
    <table class="tbl">
      <thead><tr><th>Date</th><th>Catégorie</th><th>Description</th><th class="num">Montant (Ar)</th><th class="actions"></th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty depenses}">
            <c:forEach var="d" items="${depenses}">
              <tr>
                <td>${d.dateDepense}</td>
                <td><span class="badge badge--gray">${d.categorie.nom}</span></td>
                <td class="muted">${d.description}</td>
                <td class="num"><b><fmt:formatNumber value="${d.montant}" type="number" maxFractionDigits="0"/></b></td>
                <td class="actions"><a href="${ctx}/depenses/form?id=${d.id}">Modifier</a></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="5"><div class="empty"><p>Aucune dépense enregistrée.</p><a class="btn btn--primary" href="${ctx}/depenses/form">Ajouter une dépense</a></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
      <c:if test="${not empty totalDepenses}">
        <tfoot><tr><td colspan="3" class="right"><b>Total</b></td><td class="num"><b><fmt:formatNumber value="${totalDepenses}" type="number" maxFractionDigits="0"/> Ar</b></td><td></td></tr></tfoot>
      </c:if>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
