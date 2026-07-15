<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="edition" value="${not empty vente.id}" />
<c:set var="pageTitle" value="${edition ? 'Modifier la vente' : 'Nouvelle vente'}" />
<c:set var="crumbs"    value="Commerce / Ventes / <b>${edition ? 'Édition' : 'Création'}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>${pageTitle}</h1><p>Sélectionnez le client et les lignes de vente</p></div>
  <a class="btn btn--ghost" href="${ctx}/ventes">&larr; Retour</a>
</div>

<form method="post" action="${ctx}/ventes/save" class="vente-form">
  <input type="hidden" name="id" value="${vente.id}">

  <div class="card">
    <div class="card__body">
      <div class="form-grid">
        <div class="field"><label>Client <span class="req">*</span></label>
          <select class="select" name="clientId" required>
            <option value="">— Choisir —</option>
            <c:forEach var="cl" items="${clients}"><option value="${cl.id}" ${vente.clientId == cl.id ? 'selected' : ''}>${cl.nom}</option></c:forEach>
          </select>
        </div>
        <div class="field"><label>Date de vente <span class="req">*</span></label><input class="input" type="date" name="dateVente" value="${vente.dateVente}" required></div>
      </div>
    </div>
  </div>

  <div class="card">
    <div class="card__head"><h2>Lignes de vente</h2></div>
    <div class="card__body">
      <div class="table-wrap">
        <table class="tbl vente-lines">
          <thead>
            <tr>
              <th>Lot</th>
              <th>Quantité</th>
              <th>Prix unitaire (Ar)</th>
              <th class="num">Total ligne</th>
              <th class="actions">Action</th>
            </tr>
          </thead>
          <tbody data-lines-body>
            <c:choose>
              <c:when test="${edition and not empty vente.lignes}">
                <c:forEach var="ligne" items="${vente.lignes}" varStatus="st">
                  <tr data-line>
                    <td>
                      <select class="select" name="lignes[${st.index}].lotId">
                        <option value="">— Choisir —</option>
                        <c:forEach var="l" items="${lots}"><option value="${l.id}" ${ligne.lotId == l.id ? 'selected' : ''}>${l.codeLot} (${l.effectifActuel})</option></c:forEach>
                      </select>
                    </td>
                    <td><input class="input" type="number" min="0" name="lignes[${st.index}].quantite" data-qty value="${ligne.quantite}" placeholder="0"></td>
                    <td><input class="input" type="number" min="0" name="lignes[${st.index}].prixUnitaire" data-price value="${ligne.prixUnitaire}" placeholder="0"></td>
                    <td class="num"><b data-line-total>0</b> Ar</td>
                    <td class="actions"><button type="button" class="icon-btn" data-del-line title="Supprimer la ligne"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg></button></td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr data-line>
                  <td>
                    <select class="select" name="lignes[0].lotId">
                      <option value="">— Choisir —</option>
                      <c:forEach var="l" items="${lots}"><option value="${l.id}">${l.codeLot} (${l.effectifActuel})</option></c:forEach>
                    </select>
                  </td>
                  <td><input class="input" type="number" min="0" name="lignes[0].quantite" data-qty placeholder="0"></td>
                  <td><input class="input" type="number" min="0" name="lignes[0].prixUnitaire" data-price placeholder="0"></td>
                  <td class="num"><b data-line-total>0</b> Ar</td>
                  <td class="actions"><button type="button" class="icon-btn" data-del-line title="Supprimer la ligne"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg></button></td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>

      <button type="button" class="btn btn--ghost btn--add-line" data-add-line>
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
        Ajouter une ligne
      </button>

      <div class="vente-total">
        <span>Montant total</span>
        <span class="amount"><b data-total-out>0</b> Ar</span>
      </div>
    </div>
  </div>

  <div class="alert alert--info vente-note">
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
    <span>Une vente ne peut pas dépasser l'effectif actuel du lot.<br>Le montant est recalculé automatiquement.</span>
  </div>

  <div class="form-actions">
    <a class="btn btn--ghost" href="${ctx}/ventes">Annuler</a>
    <button class="btn btn--primary" type="submit">${edition ? 'Enregistrer' : 'Créer la vente'}</button>
  </div>
</form>

<style>
  .vente-form .card { margin-bottom: 18px; }
  .vente-lines th { font-size: 11px; text-transform: uppercase; letter-spacing: .4px; color: var(--gris-500); font-weight: 600; }
  .vente-lines td { vertical-align: middle; }
  .vente-lines .select, .vente-lines .input { min-width: 0; width: 100%; }
  .vente-lines td.num, .vente-lines td.actions { white-space: nowrap; }
  .btn--add-line { border-style: dashed; color: var(--marine-600); margin-top: 12px; }
  .btn--add-line:hover { background: var(--bleu-100); border-color: var(--marine-400); }
  .vente-total { display: flex; justify-content: flex-end; align-items: center; gap: 26px; margin-top: 18px; padding-top: 16px; border-top: 1px solid var(--gris-200); font-weight: 800; }
  .vente-total .amount { color: var(--marine-700); font-size: 16px; }
  .icon-btn { background: none; border: 1px solid var(--gris-200); border-radius: 8px; width: 34px; height: 34px; display: inline-grid; place-items: center; cursor: pointer; color: var(--gris-500); transition: color .15s, background .15s, border-color .15s; }
  .icon-btn:hover { color: var(--rouge-600); border-color: var(--rouge-100); background: var(--rouge-100); }
  .vente-note { display: flex; align-items: flex-start; gap: 10px; margin-bottom: 18px; }
  .vente-note svg { flex: 0 0 auto; margin-top: 1px; }
</style>

<script>
  (function () {
    var tbody = document.querySelector("[data-lines-body]");
    var addBtn = document.querySelector("[data-add-line]");
    if (!tbody || !addBtn) return;

    // Renumérote les noms lignes[i]... pour rester contigu (binding Spring).
    function reindex() {
      tbody.querySelectorAll("[data-line]").forEach(function (row, i) {
        row.querySelectorAll("[name]").forEach(function (el) {
          el.name = el.name.replace(/lignes\[\d+\]/, "lignes[" + i + "]");
        });
      });
    }

    // Force le recalcul du total en réutilisant le JS existant (setupAutoTotal).
    function recompute() {
      var q = tbody.querySelector("[data-qty]");
      if (q) q.dispatchEvent(new Event("input", { bubbles: true }));
    }

    addBtn.addEventListener("click", function () {
      var rows = tbody.querySelectorAll("[data-line]");
      var clone = rows[rows.length - 1].cloneNode(true);
      clone.querySelectorAll("input").forEach(function (inp) { inp.value = ""; });
      clone.querySelectorAll("select").forEach(function (sel) { sel.selectedIndex = 0; });
      var lt = clone.querySelector("[data-line-total]");
      if (lt) lt.textContent = "0";
      tbody.appendChild(clone);
      reindex();
    });

    tbody.addEventListener("click", function (e) {
      var btn = e.target.closest("[data-del-line]");
      if (!btn) return;
      var row = btn.closest("[data-line]");
      if (tbody.querySelectorAll("[data-line]").length <= 1) {
        // Dernière ligne : on la vide au lieu de la supprimer.
        row.querySelectorAll("input").forEach(function (inp) { inp.value = ""; });
        row.querySelectorAll("select").forEach(function (sel) { sel.selectedIndex = 0; });
        var lt = row.querySelector("[data-line-total]");
        if (lt) lt.textContent = "0";
      } else {
        row.remove();
      }
      reindex();
      recompute();
    });
  })();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
