<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Historique import / export" />
<c:set var="crumbs"    value="Données / Import-Export / <b>Historique</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Historique des imports / exports</h1><p>Traçabilité des opérations de données</p></div>
  <a class="btn btn--ghost" href="${ctx}/imports"> Retour</a>
</div>

<c:if test="${not empty message}">
  <div class="alert alert--info"><span>${message}</span></div>
</c:if>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10">
      <thead><tr><th>Date</th><th>Module</th><th>Fichier</th><th>Format</th><th>Statut</th><th>Message</th><th>Utilisateur</th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty historique}">
            <c:forEach var="h" items="${historique}">
              <tr>
                <td>${h.dateFormatee}</td>
                <td><b>${h.module}</b></td>
                <td class="muted">${h.nomFichier}</td>
                <td><span class="badge badge--blue">${h.formatFichier}</span></td>
                <td>
                  <c:choose>
                    <c:when test="${h.statut == 'SUCCES'}"><span class="badge badge--green">Succès</span></c:when>
                    <c:when test="${h.statut == 'ECHEC'}"><span class="badge badge--red">Échec</span></c:when>
                    <c:otherwise><span class="badge badge--amber">Partiel</span></c:otherwise>
                  </c:choose>
                </td>
                <td class="muted">${h.message}</td>
                <td>${h.utilisateur}</td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="7"><div class="empty"><p>Aucune opération enregistrée.</p></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
