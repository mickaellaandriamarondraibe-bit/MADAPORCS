<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Nouveau mouvement de stock" />
<c:set var="crumbs"    value="Stocks / Mouvements / <b>Création</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Nouveau mouvement de stock</h1><p>Une sortie ne peut pas dépasser le stock disponible</p></div>
  <a class="btn btn--ghost" href="${ctx}/stocks/mouvements"> Retour</a>
</div>

<div class="card" style="max-width:640px">
  <div class="card__body">
    <%-- POST /stocks/mouvements/save avec @RequestParam ingredientId, typeMouvement, quantite --%>
    <form method="post" action="${ctx}/stocks/mouvements/save" class="stack">
      <div class="field">
        <label>Ingrédient <span class="req">*</span></label>
        <select class="select" name="ingredientId" required>
          <option value="">— Choisir —</option>
          <c:forEach var="i" items="${ingredients}">
            <option value="${i.id}">${i.nom} (stock : ${i.stockActuel} ${i.unite})</option>
          </c:forEach>
        </select>
      </div>
      <div class="field">
        <label>Type de mouvement <span class="req">*</span></label>
        <select class="select" id="typeMouvement" name="typeMouvement" required>
          <option value="ENTREE">Entrée</option>
          <option value="SORTIE">Sortie</option>
        </select>
      </div>
      <div class="field">
        <label>Quantité <span class="req">*</span></label>
        <input class="input" type="number" step="0.01" min="0.01" name="quantite" required>
      </div>
      <div class="field" id="champMontant">
        <label>Montant dépensé (Kg)</label>
        <input class="input" type="number" step="0.01" min="0" name="montant" placeholder="Ex: 50000">
        <span class="hint">Pour une entrée (achat), enregistré comme dépense.</span>
      </div>
      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/stocks/mouvements">Annuler</a>
        <button class="btn btn--primary" type="submit">Enregistrer</button>
      </div>
    </form>
  </div>
</div>

<%-- Le montant ne concerne qu'une entrée (achat). --%>
<script>
  const selectType = document.getElementById("typeMouvement");
  const champMontant = document.getElementById("champMontant");

  function majAffichageMontant() {
    champMontant.style.display = selectType.value === "ENTREE" ? "" : "none";
  }

  selectType.addEventListener("change", majAffichageMontant);
  majAffichageMontant();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
