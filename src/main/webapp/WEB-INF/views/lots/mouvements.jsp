<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Mouvements — ${lot.codeLot}" />
<c:set var="crumbs"    value="Cheptel / Lots / ${lot.codeLot} / <b>Mouvements</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Mouvements d'effectif</h1><p>Lot ${lot.codeLot} · effectif actuel : <b>${lot.effectifActuel}</b></p></div>
  <a class="btn btn--ghost" href="${ctx}/lots/${lot.id}"><i class="fa-solid fa-arrow-left"></i> Retour au lot</a>
</div>

<div class="grid-2">
  <div class="card">
    <div class="card__head"><h2>Historique</h2></div>
    <div class="card__body" style="padding:0">
      <table class="tbl">
        <thead><tr><th>Date</th><th>Type</th><th class="num">Quantité</th><th>Motif</th></tr></thead>
        <tbody>
          <c:choose>
            <c:when test="${not empty mouvements}">
              <c:forEach var="m" items="${mouvements}">
                <tr>
                  <td><fmt:formatDate value="${m.dateMouvement}" pattern="dd/MM/yyyy"/></td>
                  <td><span class="badge ${m.typeMouvement == 'DECES' or m.typeMouvement == 'VENTE' ? 'badge--red' : 'badge--green'}">${m.typeMouvement}</span></td>
                  <td class="num">${m.quantite}</td>
                  <td class="muted">${m.motif}</td>
                </tr>
              </c:forEach>
            </c:when>
            <c:otherwise><tr><td colspan="4"><div class="empty" style="padding:30px">Aucun mouvement enregistré.</div></td></tr></c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>

  <div class="card">
    <div class="card__head"><h2>Nouveau mouvement</h2></div>
    <div class="card__body">
      <%-- POST /lots/mouvements/save, bind MouvementLotDTO --%>
      <form method="post" action="${ctx}/lots/mouvements/save" class="stack">
        <input type="hidden" name="lotId" value="${lot.id}">
        <div class="field">
          <label>Type de mouvement <span class="req">*</span></label>
          <select class="select" name="typeMouvement" required>
            <option value="ENTREE">Entrée</option>
            <option value="NAISSANCE">Naissance</option>
            <option value="DECES">Décès</option>
            <option value="VENTE">Vente</option>
            <option value="TRANSFERT">Transfert</option>
            <option value="AJUSTEMENT">Ajustement</option>
          </select>
        </div>
        <div class="field">
          <label>Quantité <span class="req">*</span></label>
          <input class="input" type="number" min="1" name="quantite" required>
          <span class="hint">Une sortie ne peut pas dépasser l'effectif actuel.</span>
        </div>
        <div class="field">
          <label>Date</label>
          <input class="input" type="date" name="dateMouvement">
        </div>
        <div class="field">
          <label>Motif</label>
          <textarea class="textarea" name="motif"></textarea>
        </div>
        <button class="btn btn--primary w-full" style="justify-content:center" type="submit">Enregistrer le mouvement</button>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
