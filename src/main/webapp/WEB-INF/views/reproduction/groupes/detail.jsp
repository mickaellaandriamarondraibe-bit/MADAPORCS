<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="g" value="${detail}" />

<c:set var="pageTitle" value="Groupe ${g.codeGroupe}" />
<c:set var="crumbs" value="Reproduction / Groupes / <b>${g.codeGroupe}</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
    <div>
        <h1>
            ${g.codeGroupe}

            <c:choose>
                <c:when test="${g.statut == 'SAILLIE'}">
                    <span class="badge badge--blue">
                        <span class="dot"></span>Saillie
                    </span>
                </c:when>

                <c:when test="${g.statut == 'EN_GESTATION'}">
                    <span class="badge badge--amber">
                        <span class="dot"></span>En gestation
                    </span>
                </c:when>

                <c:when test="${g.statut == 'MISE_BAS_PROCHE'}">
                    <span class="badge badge--amber">
                        Mise bas proche
                    </span>
                </c:when>

                <c:when test="${g.statut == 'MISE_BAS_CONFIRMEE'}">
                    <span class="badge badge--green">
                        Mise bas confirmée
                    </span>
                </c:when>

                <c:when test="${g.statut == 'ECHEC'}">
                    <span class="badge badge--red">
                        Échec
                    </span>
                </c:when>

                <c:when test="${g.statut == 'CLOTURE'}">
                    <span class="badge badge--gray">
                        Clôturé
                    </span>
                </c:when>

                <c:otherwise>
                    <span class="badge badge--blue">
                        ${g.statut}
                    </span>
                </c:otherwise>
            </c:choose>
        </h1>

        <p>
            Lot mère ${g.codeLotFemelle}
            ×
            <c:choose>
                <c:when test="${not empty g.codeLotMale}">
                ${g.codeLotMale}
                </c:when>
                <c:otherwise>
                    <span class="muted">Aucun lot mâle</span>
                </c:otherwise>
            </c:choose>
            · ${g.nombreFemellesConcernees} femelle(s)
        </p>
    </div>

    <div class="flex gap-8">
        <c:if test="${not empty g.lotFemelleId}">
            <a class="btn btn--ghost" href="${ctx}/reproduction/analyse/lots/${g.lotFemelleId}">
                Voir l'analyse
            </a>
        </c:if>

        <c:if test="${g.statut == 'SAILLIE' or g.statut == 'EN_GESTATION' or g.statut == 'MISE_BAS_PROCHE'}">
            <a class="btn btn--gold" href="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">
                Confirmer la mise bas
            </a>
        </c:if>

        <c:if test="${g.statut == 'MISE_BAS_CONFIRMEE' or g.statut == 'ECHEC'}">
            <form method="post"
                  action="${ctx}/reproduction/groupes/${g.id}/cloturer"
                  data-confirm="Clôturer ce groupe ?"
                  style="display:inline">
                <button class="btn btn--ghost" type="submit">
                    Clôturer le groupe
                </button>
            </form>
        </c:if>
    </div>
</div>

<div class="card mb-16">
    <div class="card__body">
        <div class="flex justify-between mb-16">
            <div>
                <b>Évolution du cycle</b><br>
                <span class="muted">
                    Jours restants avant mise bas :
                    <b>${joursRestants}</b>
                </span>
            </div>

            <div class="right">
                <span class="kpi__value" style="font-size:22px">
                    ${pourcentageEvolution}%
                </span>
            </div>
        </div>

        <%-- Couleur progressive selon les jours restants : vert (calme),
             orange (<= 15 j), rouge (<= 5 j, mise bas imminente). --%>
        <c:set var="progColor" value="progress--ok" />
        <c:if test="${not empty joursRestants and joursRestants le 15}"><c:set var="progColor" value="progress--warn" /></c:if>
        <c:if test="${not empty joursRestants and joursRestants le 5}"><c:set var="progColor" value="progress--danger" /></c:if>
        <div class="progress ${progColor}">
            <span style="width:${empty pourcentageEvolution ? 0 : pourcentageEvolution}%"></span>
        </div>

        <div class="flex justify-between mt-16 muted" style="font-size:12px">
            <span>Saillie : ${g.dateSaillie}</span>
            <span>Prévue : ${g.datePrevueMiseBas}</span>
        </div>
    </div>
</div>

<div class="grid-2">

    <div class="card">
        <div class="card__head">
            <h2>Détails du groupe</h2>
        </div>

        <div class="card__body">
            <dl class="dl">
                <dt>Code groupe</dt>
                <dd>${g.codeGroupe}</dd>


                <dt>Femelles concernées</dt>
                <dd>${g.nombreFemellesConcernees}</dd>

                <dt>Mâles utilisés</dt>
                <dd>${g.nombreMalesUtilises}</dd>

                <dt>Date de saillie</dt>
                <dd>${g.dateSaillie}</dd>

                <dt>Durée gestation</dt>
                <dd>${g.dureeGestationJours} jours</dd>

                <dt>Mise bas prévue</dt>
                <dd>${g.datePrevueMiseBas}</dd>

                <dt>Statut</dt>
                <dd>${g.statut}</dd>

                <dt>Observation</dt>
                <dd>
                    <c:choose>
                        <c:when test="${not empty g.observation}">
                            ${g.observation}
                        </c:when>
                        <c:otherwise>
                            <span class="muted">Aucune observation</span>
                        </c:otherwise>
                    </c:choose>
                </dd>
            </dl>
        </div>
    </div>

    <div class="card">
        <div class="card__head">
            <h2>Résultat de mise bas</h2>
        </div>

        <div class="card__body">
            <c:choose>
                <c:when test="${g.statut == 'MISE_BAS_CONFIRMEE' or g.statut == 'CLOTURE' or g.statut == 'ECHEC'}">
                    <dl class="dl">
                        <dt>Date réelle</dt>
                        <dd>${g.dateMiseBasReelle}</dd>

                        <dt>Femelles gestantes</dt>
                        <dd>${g.nbFemellesGestantes}</dd>

                        <dt>Femelles non gestantes</dt>
                        <dd>${g.nbFemellesNonGestantes}</dd>

                        <dt>Femelles ayant mis bas</dt>
                        <dd>${g.nbFemellesMiseBas}</dd>

                        <dt>Porcelets nés</dt>
                        <dd>${g.nbPorceletsNes}</dd>

                        <dt>Vivants</dt>
                        <dd>
                            <span class="badge badge--green">
                                ${g.nbPorceletsVivants}
                            </span>
                        </dd>

                        <dt>Morts</dt>
                        <dd>
                            <span class="badge badge--red">
                                ${g.nbPorceletsMorts}
                            </span>
                        </dd>

                        <dt>Lot(s) naissance</dt>
<dd>
    <c:choose>
        <c:when test="${not empty lotsNaissance}">
            <c:forEach var="ln" items="${lotsNaissance}">
                <a href="${ctx}/lots/${ln.id}">
                    <span class="badge badge--green">${ln.codeLot}</span>
                </a>
                <span class="muted">(${ln.sexe}, ${ln.effectifActuel} porcs)</span>
                <br>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <span class="muted">Non créé</span>
        </c:otherwise>
    </c:choose>
</dd>
                    </dl>
                </c:when>

                <c:otherwise>
                    <div class="empty" style="padding:24px">
                        <p>La mise bas n'est pas encore confirmée.</p>

                        <c:if test="${g.statut == 'SAILLIE' or g.statut == 'EN_GESTATION' or g.statut == 'MISE_BAS_PROCHE'}">
                            <a class="btn btn--gold" href="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">
                                Confirmer la mise bas
                            </a>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>