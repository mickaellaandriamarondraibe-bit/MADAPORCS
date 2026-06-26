<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="l" value="${detail}" />

<c:set var="pageTitle" value="Lot ${l.codeLot}" />
<c:set var="crumbs" value="Cheptel / Lots / ${l.codeLot}" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="detail-page">

    <!-- HERO -->
    <section class="detail-hero">
        <div class="detail-hero__main">
            <div class="eyebrow">Fiche lot porcin</div>

            <div class="detail-title-row">
                <h1>${l.codeLot}</h1>

                <c:choose>
                    <c:when test="${l.sexe == 'FEMELLE'}">
                        <span class="badge badge--gold">
                            <i class="fa-solid fa-venus"></i> Femelle
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge--blue">
                            <i class="fa-solid fa-mars"></i> Mâle
                        </span>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${l.statut == 'ACTIF'}">
                        <span class="badge badge--green">Actif</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge--gray">${l.statut}</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <p class="detail-subtitle">
                ${empty l.raceNom ? 'Race non renseignée' : l.raceNom}
                · ${l.objectif}
                · Origine : ${l.origine}
            </p>
        </div>

        <div class="detail-hero__actions">
            <a class="btn btn--ghost" href="${ctx}/lots/${l.id}/pesees">
                <i class="fa-solid fa-scale-balanced"></i> Pesées
            </a>

            <a class="btn btn--ghost" href="${ctx}/lots/${l.id}/mouvements">
                <i class="fa-solid fa-right-left"></i> Mouvements
            </a>

            <a class="btn btn--primary" href="${ctx}/lots/form?id=${l.id}">
                Modifier
            </a>

            <c:if test="${l.statut == 'ACTIF'}">
                <form method="post"
                      action="${ctx}/lots/archive/${l.id}"
                      data-confirm="Archiver ce lot ?">
                    <button class="btn btn--danger" type="submit">
                        Archiver
                    </button>
                </form>
            </c:if>
        </div>
    </section>

    <!-- KPI -->
    <section class="pro-kpi-grid">
        <div class="pro-kpi">
            <span class="pro-kpi__label">Effectif actuel</span>
            <strong>${l.effectifActuel}</strong>
            <small>Initial : ${l.effectifInitial}</small>
        </div>

        <div class="pro-kpi">
            <span class="pro-kpi__label">Sexe du lot</span>
            <strong>${l.sexe}</strong>
            <small>Lot non mixte</small>
        </div>

        <div class="pro-kpi">
            <span class="pro-kpi__label">Race</span>
            <strong>${empty l.raceNom ? '—' : l.raceNom}</strong>
            <small>Classification génétique</small>
        </div>

        <div class="pro-kpi">
            <span class="pro-kpi__label">Statut</span>
            <strong>${l.statut}</strong>
            <small>État opérationnel</small>
        </div>
    </section>

    <!-- CONTENT -->
    <section class="detail-layout">

        <!-- MAIN CARD -->
        <div class="card pro-card">
            <div class="card__head pro-card__head">
                <div>
                    <h2>Informations du lot</h2>
                    <p>Résumé technique et zootechnique du lot.</p>
                </div>
            </div>

            <div class="card__body">
                <div class="info-grid">

                    <div class="info-item">
                        <span>Code lot</span>
                        <strong>${l.codeLot}</strong>
                    </div>

                    <div class="info-item">
                        <span>Date de création</span>
                        <strong>${l.dateCreation}</strong>
                    </div>

                    <div class="info-item">
                        <span>Sexe</span>
                        <strong>${l.sexe}</strong>
                    </div>

                    <div class="info-item">
                        <span>Race</span>
                        <strong>${empty l.raceNom ? 'Non renseignée' : l.raceNom}</strong>
                    </div>

                    <div class="info-item">
                        <span>Objectif</span>
                        <strong>${l.objectif}</strong>
                    </div>

                    <div class="info-item">
                        <span>Origine</span>
                        <strong>${l.origine}</strong>
                    </div>

                    <div class="info-item">
                        <span>Effectif initial</span>
                        <strong>${l.effectifInitial}</strong>
                    </div>

                    <div class="info-item">
                        <span>Effectif actuel</span>
                        <strong>${l.effectifActuel}</strong>
                    </div>

                    <div class="info-item">
                        <span>Lot parent</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty l.codeLotParent}">
                                    ${l.codeLotParent}
                                </c:when>
                                <c:otherwise>—</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div class="info-item">
                        <span>Groupe reproduction origine</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty l.groupeReproductionOrigineId}">
                                    Groupe #${l.groupeReproductionOrigineId}
                                </c:when>
                                <c:otherwise>—</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                </div>

                <div class="description-box">
                    <span>Description</span>
                    <p>
                        <c:choose>
                            <c:when test="${not empty l.description}">
                                ${l.description}
                            </c:when>
                            <c:otherwise>
                                Aucune description renseignée pour ce lot.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>
        </div>

        <!-- SIDE PANEL -->
        <aside class="side-panel">

            <div class="card pro-card">
                <div class="card__head pro-card__head">
                    <div>
                        <h2>Actions rapides</h2>
                        <p>Gestion opérationnelle du lot.</p>
                    </div>
                </div>

                <div class="card__body action-list">
                    <a href="${ctx}/lots/${l.id}/pesees" class="action-item">
                        <i class="fa-solid fa-scale-balanced"></i>
                        <div>
                            <strong>Suivre les pesées</strong>
                            <span>Historique du poids moyen.</span>
                        </div>
                    </a>

                    <a href="${ctx}/lots/${l.id}/mouvements" class="action-item">
                        <i class="fa-solid fa-right-left"></i>
                        <div>
                            <strong>Voir les mouvements</strong>
                            <span>Entrées, sorties et ajustements.</span>
                        </div>
                    </a>

                    <a href="${ctx}/lots/form?id=${l.id}" class="action-item">
                        <i class="fa-solid fa-pen-to-square"></i>
                        <div>
                            <strong>Modifier le lot</strong>
                            <span>Mettre à jour les informations.</span>
                        </div>
                    </a>
                </div>
            </div>

            <div class="card pro-card">
                <div class="card__head pro-card__head">
                    <div>
                        <h2>Traçabilité</h2>
                        <p>Informations système.</p>
                    </div>
                </div>

                <div class="card__body trace-list">
                    <div>
                        <span>Créé le</span>
                        <strong>${empty l.createdAt ? '—' : l.createdAt}</strong>
                    </div>

                    <div>
                        <span>Dernière modification</span>
                        <strong>${empty l.updatedAt ? '—' : l.updatedAt}</strong>
                    </div>
                </div>
            </div>

        </aside>

    </section>

</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>