<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Utilisateurs" />
<c:set var="crumbs" value="Administration / <b>Utilisateurs</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Utilisateurs</h1>
    <p>Comptes ADMIN et GESTIONNAIRE de la plateforme</p>
  </div>

  <a class="btn btn--primary" href="${ctx}/utilisateurs/form">
     Nouvel utilisateur
  </a>
</div>

<div class="toolbar">
  <div class="search">
    <input
      class="input"
      placeholder="Rechercher un utilisateur…"
      data-filter-input="#tbl-users"
    />
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10" id="tbl-users">
      <thead>
        <tr>
          <th>Nom</th>
          <th>Prénom</th>
          <th>E-mail</th>
          <th>Rôle</th>
          <th>Statut</th>
          <th class="actions">Actions</th>
        </tr>
      </thead>

      <tbody>
        <c:choose>
          <c:when test="${not empty utilisateurs}">
            <c:forEach var="u" items="${utilisateurs}">
              <tr>
                <td><b>${u.nom}</b></td>
                <td>${u.prenom}</td>
                <td>${u.email}</td>

                <td>
                  <c:choose>
                    <c:when test="${not empty u.role}">
                      <span class="badge ${u.role.nom == 'ADMIN' ? 'badge--gold' : 'badge--blue'}">
                        ${u.role.nom}
                      </span>
                    </c:when>

                    <c:otherwise>
                      <span class="badge badge--gray">Aucun rôle</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${u.actif}">
                      <span class="badge badge--green">
                        <span class="dot"></span> Actif
                      </span>
                    </c:when>

                    <c:otherwise>
                      <span class="badge badge--gray">Désactivé</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td class="actions">
                  <a href="${ctx}/utilisateurs/form?id=${u.id}">Modifier</a>

                  <c:if test="${u.actif}">
                    <form
                      method="post"
                      action="${ctx}/utilisateurs/desactiver/${u.id}"
                      style="display:inline"
                      data-confirm="Désactiver ${u.nom} ? Il ne pourra plus se connecter."
                    >
                      <button
                        type="submit"
                        style="background:none;border:none;cursor:pointer;color:var(--rouge-600);font-weight:600;font-family:inherit;font-size:13px;margin-left:12px"
                      >
                        Désactiver
                      </button>
                    </form>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </c:when>

          <c:otherwise>
            <tr>
              <td colspan="6">
                <div class="empty">
                  
                  <p>Aucun utilisateur enregistré.</p>

                  <a class="btn btn--primary" href="${ctx}/utilisateurs/form">
                    Créer le premier compte
                  </a>
                </div>
              </td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>