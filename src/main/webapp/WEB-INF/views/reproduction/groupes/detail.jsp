<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="g" value="${detail}" />
<c:set var="pageTitle" value="Groupe ${g.codeGroupe}" />
<c:set var="crumbs"    value="Reproduction / Groupes / <b>${g.codeGroupe}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>${g.codeGroupe}
      <c:choose>
        <c:when test="${g.statut == 'EN_CYCLE'}"><span class="badge badge--amber"><span class="dot"></span>En cycle</span></c:when>
        <c:when test="${g.statut == 'MISE_BAS'}"><span class="badge badge--green">Mise bas confirmée</span></c:when>
        <c:when test="${g.statut == 'CLOTURE'}"><span class="badge badge--gray">Clôturé</span></c:when>
        <c:otherwise><span class="badge badge--blue">${g.statut}</span></c:otherwise>
      </c:choose>
    </h1>
    <p>Lot mère ${g.codeLotFemelle} × ${g.codeLotMale} · ${g.nombreFemelles} femelle(s)</p>
  </div>
  <div class="flex gap-8">
    <c:if test="${g.statut == 'EN_CYCLE'}">
      <a class="btn btn--gold" href="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">Confirmer la mise bas</a>
    </c:if>
    <c:if test="${g.statut == 'MISE_BAS'}">
      <form method="post" action="${ctx}/reproduction/groupes/${g.id}/cloturer" data-confirm="Clôturer ce groupe ?" style="display:inline">
        <button class="btn btn--ghost" type="submit">Clôturer le groupe</button>
      </form>
    </c:if>
  </div>
</div>

<%-- Barre d'évolution du cycle --%>
<div class="card mb-16">
  <div class="card__body">
    <div class="flex justify-between mb-16">
      <div><b>Évolution du cycle</b><br><span class="muted">Jours restants avant mise bas : <b>${g.joursRestants}</b></span></div>
      <div class="right"><span class="kpi__value" style="font-size:22px">${g.pourcentageEvolution}%</span></div>
    </div>
    <div class="progress ${g.pourcentageEvolution >= 90 ? 'progress--gold' : ''}">
      <span style="width:${empty g.pourcentageEvolution ? 0 : g.pourcentageEvolution}%"></span>
    </div>
    <div class="flex justify-between mt-16 muted" style="font-size:12px">
      <span>Saillie <fmt:formatDate value="${g.dateSaillie}" pattern="dd/MM/yyyy"/></span>
      <span>Prévue <fmt:formatDate value="${g.datePrevueMiseBas}" pattern="dd/MM/yyyy"/></span>
    </div>
  </div>
</div>

<div class="grid-2">
  <div class="card">
    <div class="card__head"><h2>Détails du groupe</h2></div>
    <div class="card__body">
      <dl class="dl">
        <dt>Code groupe</dt><dd>${g.codeGroupe}</dd>
        <dt>Lot femelle</dt><dd>${g.codeLotFemelle}</dd>
        <dt>Lot mâle</dt><dd>${g.codeLotMale}</dd>
        <dt>Femelles concernées</dt><dd>${g.nombreFemelles}</dd>
        <dt>Mâles utilisés</dt><dd>${g.nombreMales}</dd>
        <dt>Date de saillie</dt><dd><fmt:formatDate value="${g.dateSaillie}" pattern="dd/MM/yyyy"/></dd>
        <dt>Durée gestation</dt><dd>${g.dureeGestation} jours</dd>
        <dt>Mise bas prévue</dt><dd><fmt:formatDate value="${g.datePrevueMiseBas}" pattern="dd/MM/yyyy"/></dd>
      </dl>
    </div>
  </div>

  <div class="card">
    <div class="card__head"><h2>Résultat de mise bas</h2></div>
    <div class="card__body">
      <c:choose>
        <c:when test="${g.statut == 'MISE_BAS' or g.statut == 'CLOTURE'}">
          <dl class="dl">
            <dt>Date réelle</dt><dd><fmt:formatDate value="${g.dateReelleMiseBas}" pattern="dd/MM/yyyy"/></dd>
            <dt>Porcelets nés</dt><dd>${g.porceletsNes}</dd>
            <dt>Vivants</dt><dd><span class="badge badge--green">${g.porceletsVivants}</span></dd>
            <dt>Morts</dt><dd><span class="badge badge--red">${g.porceletsMorts}</span></dd>
            <dt>Lot naissance</dt>
            <dd>
              <c:choose>
                <c:when test="${not empty g.codeLotNaissance}"><a href="${ctx}/lots/${g.lotNaissanceId}">${g.codeLotNaissance}</a></c:when>
                <c:otherwise><span class="muted">Non créé</span></c:otherwise>
              </c:choose>
            </dd>
          </dl>
        </c:when>
        <c:otherwise>
          <div class="empty" style="padding:24px">
            <p>La mise bas n'est pas encore confirmée.</p>
            <c:if test="${g.statut == 'EN_CYCLE'}">
              <a class="btn btn--gold" href="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">Confirmer la mise bas</a>
            </c:if>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
