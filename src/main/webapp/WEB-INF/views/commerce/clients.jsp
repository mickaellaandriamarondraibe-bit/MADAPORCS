<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Clients" />
<c:set var="crumbs"    value="Commerce / <b>Clients</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Clients</h1><p>Répertoire des acheteurs</p></div>
  <a class="btn btn--primary" href="${ctx}/clients/form"><i class="fa-solid fa-plus"></i> Nouveau client</a>
</div>

<div class="toolbar"><div class="search"><input class="input" placeholder="Rechercher un client…" data-filter-input="#tbl-clients"></div></div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-clients">
      <thead><tr><th>Nom</th><th>Téléphone</th><th>Adresse</th><th class="actions"></th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty clients}">
            <c:forEach var="cl" items="${clients}">
              <tr>
                <td><a href="${ctx}/clients/${cl.id}"><b>${cl.nom}</b></a></td>
                <td>${cl.telephone}</td><td class="muted">${cl.adresse}</td>
                <td class="actions"><a href="${ctx}/clients/${cl.id}">Détail</a><a href="${ctx}/clients/form?id=${cl.id}">Modifier</a></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="4"><div class="empty"><i class="ico fa-solid fa-user"></i><p>Aucun client.</p><a class="btn btn--primary" href="${ctx}/clients/form">Ajouter un client</a></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
