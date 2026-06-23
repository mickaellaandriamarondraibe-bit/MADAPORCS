<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Vaccins" />
<c:set var="crumbs"    value="Santé / <b>Vaccins</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Vaccins</h1><p>Catalogue des vaccins disponibles</p></div>
  <a class="btn btn--primary" href="${ctx}/vaccins/form"><i class="fa-solid fa-plus"></i> Nouveau vaccin</a>
</div>

<div class="toolbar"><div class="search"><input class="input" placeholder="Rechercher…" data-filter-input="#tbl-vaccins"></div></div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-vaccins">
      <thead><tr><th>Nom</th><th>Maladie ciblée</th><th>Voie</th><th class="num">Rappel (jours)</th><th class="actions"></th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty vaccins}">
            <c:forEach var="v" items="${vaccins}">
              <tr><td><b>${v.nom}</b></td><td>${v.maladieCiblee}</td><td>${v.voie}</td><td class="num">${v.delaiRappel}</td>
                <td class="actions"><a href="${ctx}/vaccins/form?id=${v.id}">Modifier</a></td></tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="5"><div class="empty"><i class="ico fa-solid fa-syringe"></i><p>Aucun vaccin enregistré.</p><a class="btn btn--primary" href="${ctx}/vaccins/form">Ajouter un vaccin</a></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
