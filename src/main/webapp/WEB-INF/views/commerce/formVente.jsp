<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty vente.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier la vente' : 'Nouvelle vente'}" />
<c:set var="crumbs"    value="Commerce / Ventes / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1><p>Sélectionnez le client et les lignes de vente</p></div>
  <a class="btn btn--ghost" href="${ctx}/ventes"> Retour</a>
</div>

<div class="card" style="max-width:900px">
  <div class="card__body">
    <%-- POST /ventes/save, bind VenteDTO (avec lignes DetailVenteDTO) --%>
    <form method="post" action="${ctx}/ventes/save">
      <input type="hidden" name="id" value="${vente.id}">
      <div class="form-grid">
        <div class="field"><label>Client <span class="req">*</span></label>
          <select class="select" name="clientId" required>
            <option value="">— Choisir —</option>
            <c:forEach var="cl" items="${clients}"><option value="${cl.id}" ${vente.clientId == cl.id ? 'selected' : ''}>${cl.nom}</option></c:forEach>
          </select>
        </div>
        <div class="field"><label>Date de vente <span class="req">*</span></label><input class="input" type="date" name="dateVente" value="${vente.dateVente}" required></div>
      </div>

      <h2 style="font-size:15px;margin:22px 0 10px">Lignes de vente</h2>
      <div class="table-wrap">
        <table class="tbl">
          <thead><tr><th>Lot</th><th class="num">Quantité</th><th class="num">Prix unitaire (Ar)</th><th class="num">Total ligne</th></tr></thead>
          <tbody>
            <%-- 5 lignes par défaut ; le contrôleur peut en pré-remplir davantage --%>
            <c:forEach var="i" begin="0" end="4">
              <tr data-line>
                <td>
                  <select class="select" name="lignes[${i}].lotId">
                    <option value="">—</option>
                    <c:forEach var="l" items="${lots}"><option value="${l.id}">${l.codeLot} (${l.effectifActuel})</option></c:forEach>
                  </select>
                </td>
                <td class="num"><input class="input" type="number" min="0" name="lignes[${i}].quantite" data-qty style="text-align:right"></td>
                <td class="num"><input class="input" type="number" min="0" name="lignes[${i}].prixUnitaire" data-price style="text-align:right"></td>
                <td class="num"><b data-line-total>0</b></td>
              </tr>
            </c:forEach>
          </tbody>
          <tfoot>
            <tr><td colspan="3" class="right"><b>Montant total</b></td><td class="num"><b data-total-out>0</b> Ar</td></tr>
          </tfoot>
        </table>
      </div>
      <p class="hint">Une vente ne peut pas dépasser l'effectif actuel du lot. Le montant est recalculé automatiquement.</p>

      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/ventes">Annuler</a>
        <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer la vente'}</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
