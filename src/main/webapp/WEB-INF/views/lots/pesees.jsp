<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Pesées — ${lot.codeLot}" />
<c:set var="crumbs"    value="Cheptel / Lots / ${lot.codeLot} / <b>Pesées</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Suivi du poids</h1><p>Lot ${lot.codeLot}
    <c:if test="${not empty evolution}"> · évolution : <b>${evolution} kg</b></c:if></p></div>
  <a class="btn btn--ghost" href="${ctx}/lots/${lot.id}"><i class="fa-solid fa-arrow-left"></i> Retour au lot</a>
</div>

<div class="grid-2">
  <div class="card">
    <div class="card__head"><h2>Historique des pesées</h2></div>
    <div class="card__body" style="padding:0">
      <table class="tbl">
        <thead><tr><th>Date</th><th class="num">Poids moyen (kg)</th><th>Observation</th></tr></thead>
        <tbody>
          <c:choose>
            <c:when test="${not empty pesees}">
              <c:forEach var="p" items="${pesees}">
                <tr>
                  <td><fmt:formatDate value="${p.datePesee}" pattern="dd/MM/yyyy"/></td>
                  <td class="num"><b>${p.poidsMoyen}</b></td>
                  <td class="muted">${p.observation}</td>
                </tr>
              </c:forEach>
            </c:when>
            <c:otherwise><tr><td colspan="3"><div class="empty" style="padding:30px">Aucune pesée enregistrée.</div></td></tr></c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>

  <div class="card">
    <div class="card__head"><h2>Nouvelle pesée</h2></div>
    <div class="card__body">
      <%-- POST /lots/pesees/save, bind PeseeLotDTO --%>
      <form method="post" action="${ctx}/lots/pesees/save" class="stack">
        <input type="hidden" name="lotId" value="${lot.id}">
        <div class="field">
          <label>Poids moyen (kg) <span class="req">*</span></label>
          <input class="input" type="number" step="0.1" min="0.1" name="poidsMoyen" required>
          <span class="hint">Doit être supérieur à 0.</span>
        </div>
        <div class="field">
          <label>Date de pesée <span class="req">*</span></label>
          <input class="input" type="date" name="datePesee" required>
          <span class="hint">Ne peut pas être dans le futur.</span>
        </div>
        <div class="field">
          <label>Observation</label>
          <textarea class="textarea" name="observation"></textarea>
        </div>
        <button class="btn btn--primary w-full" style="justify-content:center" type="submit">Enregistrer la pesée</button>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
