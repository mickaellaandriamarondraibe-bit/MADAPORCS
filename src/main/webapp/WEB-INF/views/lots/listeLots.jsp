<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Lots de porcs" />
<c:set var="crumbs" value="Cheptel / <b>Lots de porcs</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Lots de porcs</h1>
    <p>Création, suivi et archivage des lots</p>
  </div>

  <a class="btn btn--primary" href="${ctx}/lots/form">
     Nouveau lot
  </a>
</div>

<form method="get" action="${ctx}/lots" class="toolbar">
  <div class="search">
    <input
      class="input"
      type="text"
      name="codeLot"
      value="${filtre.codeLot}"
      placeholder="Code du lot…"
    />
  </div>

  <select class="select" name="sexe">
    <option value="">Sexe (tous)</option>
    <option value="MALE" ${filtre.sexe == 'MALE' ? 'selected' : ''}>Mâle</option>
    <option value="FEMELLE" ${filtre.sexe == 'FEMELLE' ? 'selected' : ''}>Femelle</option>
  </select>

  <select class="select" name="objectif">
    <option value="">Objectif (tous)</option>
    <option value="REPRODUCTION" ${filtre.objectif == 'REPRODUCTION' ? 'selected' : ''}>
      Reproduction
    </option>
    <option value="ENGRAISSEMENT" ${filtre.objectif == 'ENGRAISSEMENT' ? 'selected' : ''}>
      Engraissement
    </option>
  </select>

  <select class="select" name="statut">
    <option value="">Statut (tous)</option>
    <option value="ACTIF" ${filtre.statut == 'ACTIF' ? 'selected' : ''}>Actif</option>
    <option value="ARCHIVE" ${filtre.statut == 'ARCHIVE' ? 'selected' : ''}>Archivé</option>
  </select>

  <button class="btn btn--ghost" type="submit">
    Filtrer
  </button>

  <a class="btn btn--ghost" href="${ctx}/lots">
    Réinitialiser
  </a>
</form>

<div class="card">
  <div class="table-wrap">
    <table class="tbl">
      <thead>
        <tr>
          <th>Code</th>
          <th>Sexe</th>
          <th>Race</th>
          <th>Objectif</th>
          <th class="num">Effectif</th>
          <th>Statut</th>
          <th class="actions">Actions</th>
        </tr>
      </thead>

      <tbody>
        <c:choose>
          <c:when test="${not empty lots}">
            <c:forEach var="l" items="${lots}">
              <tr>
                <td>
                  <a href="${ctx}/lots/${l.id}">
                    <b>${l.codeLot}</b>
                  </a>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${l.sexe == 'FEMELLE'}">
                      <span class="badge badge--gold">
                         Femelle
                      </span>
                    </c:when>

                    <c:otherwise>
                      <span class="badge badge--blue">
                         Mâle
                      </span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${not empty l.race}">
                      ${l.race.nom}
                    </c:when>
                    <c:otherwise>
                      <span class="muted">Non définie</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>${l.objectif}</td>

                <td class="num">
                  <b>${l.effectifActuel}</b>
                  <span class="muted">/ ${l.effectifInitial}</span>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${l.statut == 'ACTIF'}">
                      <span class="badge badge--green">
                        <span class="dot"></span> Actif
                      </span>
                    </c:when>

                    <c:otherwise>
                      <span class="badge badge--gray">
                        Archivé
                      </span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td class="actions">
                  <a href="${ctx}/lots/${l.id}">Détail</a>
                  <a href="${ctx}/lots/form?id=${l.id}">Modifier</a>
                </td>
              </tr>
            </c:forEach>
          </c:when>

          <c:otherwise>
            <tr>
              <td colspan="7">
                <div class="empty">
                  
                  <p>Aucun lot ne correspond.</p>

                  <a class="btn btn--primary" href="${ctx}/lots/form">
                    Créer un lot
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