<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Mouvements de stock" />
<c:set var="crumbs"    value="Stocks / <b>Mouvements</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Mouvements de stock</h1><p>Entrées et sorties d'ingrédients</p></div>
  <a class="btn btn--primary" href="${ctx}/stocks/mouvements/form"><i class="fa-solid fa-plus"></i> Nouveau mouvement</a>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl">
      <thead><tr><th>Date</th><th>Ingrédient</th><th>Type</th><th class="num">Quantité</th><th class="num">Stock après</th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty mouvements}">
            <c:forEach var="m" items="${mouvements}">
              <tr>
                <td>${m.dateMouvement}</td>
                <td><b>${m.ingredient.nom}</b></td>
                <td><span class="badge ${m.typeMouvement == 'SORTIE' ? 'badge--red' : 'badge--green'}">${m.typeMouvement}</span></td>
                <td class="num">${m.quantite}</td>
                <td class="num">${m.stockApres}</td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="5"><div class="empty"><i class="ico fa-solid fa-right-left"></i><p>Aucun mouvement de stock.</p></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
