<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Mouvements" />
<c:set var="crumbs" value="Traçabilité / <b>Mouvements</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Mouvements</h1>
    <p>Historique de traçabilité : tous les mouvements de lots de porcs et de stock d'aliment</p>
  </div>
</div>

<div class="tabs">
  <a href="${ctx}/mouvements?filtre=tout"      class="${filtreActif == 'tout' ? 'is-active' : ''}">Tout</a>
  <a href="${ctx}/mouvements?filtre=lot"       class="${filtreActif == 'lot' ? 'is-active' : ''}">Lots de porcs</a>
  <a href="${ctx}/mouvements?filtre=vente"     class="${filtreActif == 'vente' ? 'is-active' : ''}">Ventes</a>
  <a href="${ctx}/mouvements?filtre=deces"     class="${filtreActif == 'deces' ? 'is-active' : ''}">Décès</a>
  <a href="${ctx}/mouvements?filtre=naissance" class="${filtreActif == 'naissance' ? 'is-active' : ''}">Naissances</a>
  <a href="${ctx}/mouvements?filtre=stock"     class="${filtreActif == 'stock' ? 'is-active' : ''}">Stock aliment</a>
</div>

<div class="listbar">
  <div class="search search--auto">
    <input class="input" type="text" placeholder="Rechercher (code lot, ingrédient, type…)" data-filter-input="#tbl-mvt">
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="15" id="tbl-mvt">
      <thead>
        <tr>
          <th>Date</th>
          <th>Catégorie</th>
          <th>Type</th>
          <th>Sens</th>
          <th>Cible</th>
          <th class="num">Quantité</th>
          <th>Détail</th>
        </tr>
      </thead>

      <tbody>
        <c:choose>
          <c:when test="${not empty mouvements}">
            <c:forEach var="m" items="${mouvements}">
              <tr>
                <td>${m.date}</td>

                <td>
                  <c:choose>
                    <c:when test="${m.categorie == 'Lot de porcs'}">
                      <span class="badge badge--blue">Lot de porcs</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge badge--gold">Stock aliment</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>${m.type}</td>

                <td>
                  <c:choose>
                    <c:when test="${m.sens == 'ENTREE'}">
                      <span class="badge badge--green">Entrée</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge badge--red">Sortie</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td><b>${m.cible}</b></td>

                <td class="num">${m.quantite}</td>

                <td>
                  <c:choose>
                    <c:when test="${not empty m.detail}">${m.detail}</c:when>
                    <c:otherwise><span class="muted">—</span></c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
          </c:when>

          <c:otherwise>
            <tr>
              <td colspan="7">
                <div class="empty">
                  <p>Aucun mouvement enregistré.</p>
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
