<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="g" value="${detail}" />

<c:set var="pageTitle" value="Confirmer la mise bas" />
<c:set var="crumbs" value="Reproduction / Groupes / ${g.codeGroupe} / <b>Mise bas</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
    <div>
        <h1>Confirmer la mise bas — ${g.codeGroupe}</h1>
        <p>Saisie des résultats de mise bas.</p>
    </div>

    <a class="btn btn--ghost" href="${ctx}/reproduction/groupes/${g.id}">
        &larr; Retour au groupe
    </a>
</div>

<c:if test="${not empty erreur}">
    <div class="alert alert--err"><span>${erreur}</span></div>
</c:if>

<div class="mb-layout">

    <div class="mb-main">
        <form:form method="post"
                   modelAttribute="dto"
                   action="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">

            <c:if test="${not empty alerteId}">
                <input type="hidden" name="alerteId" value="${alerteId}" />
            </c:if>

            <%-- ===== 1. Informations de mise bas ===== --%>
            <div class="card">
                <div class="card__body">
                    <div class="mb-sec__title">1. Informations de mise bas</div>
                    <div class="form-grid">
                        <div class="field span-2">
                            <label>Date réelle de mise bas <span class="req">*</span></label>
                            <form:input class="input" type="date" path="dateMiseBasReelle" />
                            <form:errors path="dateMiseBasReelle" cssClass="error" />
                            <span class="hint">Doit être &ge; date de saillie : ${g.dateSaillie}</span>
                        </div>

                        <div class="field">
                            <label>Femelles gestantes <span class="req">*</span></label>
                            <form:input class="input" type="number" min="0" path="nbFemellesGestantes" />
                            <form:errors path="nbFemellesGestantes" cssClass="error" />
                        </div>

                        <div class="field">
                            <label>Femelles ayant mis bas <span class="req">*</span></label>
                            <form:input class="input" type="number" min="0" path="nbFemellesMiseBas" />
                            <form:errors path="nbFemellesMiseBas" cssClass="error" />
                        </div>

                        <div class="field">
                            <label>Porcelets vivants <span class="req">*</span></label>
                            <form:input class="input" type="number" min="0" path="nbPorceletsVivants" />
                            <form:errors path="nbPorceletsVivants" cssClass="error" />
                        </div>

                        <div class="field">
                            <label>Porcelets morts <span class="req">*</span></label>
                            <form:input class="input" type="number" min="0" path="nbPorceletsMorts" />
                            <form:errors path="nbPorceletsMorts" cssClass="error" />
                        </div>
                    </div>
                </div>
            </div>

            <%-- ===== 2. Création du / des lot(s) naissance ===== --%>
            <div class="card">
                <div class="card__body">
                    <div class="mb-sec__title">2. Création du / des lot(s) naissance</div>
                    <p class="mb-sec__desc">Indiquez combien de porcelets vivants sont des femelles. Le reste (vivants − femelles) devient automatiquement le lot mâle.</p>
                    <div class="form-grid">
                        <div class="field">
                            <label>Porcelets femelles vivants</label>
                            <form:input class="input" type="number" min="0" path="nbFemelles" />
                            <form:errors path="nbFemelles" cssClass="error" />
                            <span class="hint">Doit être &le; porcelets vivants.</span>
                        </div>

                        <div class="field span-2">
                            <label>Objectif du / des lot(s) naissance</label>
                            <form:select class="select" path="objectifLot">
                                <form:option value="ENGRAISSEMENT">Engraissement</form:option>
                                <form:option value="REPRODUCTION">Reproduction</form:option>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>

            <%-- ===== 3. Observation ===== --%>
            <div class="card">
                <div class="card__body">
                    <div class="mb-sec__title">3. Observation</div>
                    <div class="field">
                        <form:textarea class="textarea" path="observation" rows="3" placeholder="Remarque sur la mise bas (état, incidents, traitements, etc.)" />
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <a class="btn btn--ghost" href="${ctx}/reproduction/groupes/${g.id}">Annuler</a>
                <button class="btn btn--primary" type="submit">Confirmer et créer le(s) lot(s)</button>
            </div>

        </form:form>
    </div>

    <%-- ===== Colonne latérale : résumé + rappel ===== --%>
    <aside class="mb-side">
        <div class="card">
            <div class="card__body">
                <div class="mb-side__title">Résumé du groupe</div>
                <div class="mb-summary">
                    <div class="row"><span class="k">Groupe</span><span class="v">${g.codeGroupe}</span></div>
                    <div class="row"><span class="k">Lot femelle</span><span class="v">${g.codeLotFemelle}</span></div>
                    <div class="row"><span class="k">Lot mâle</span><span class="v">${empty g.codeLotMale ? '—' : g.codeLotMale}</span></div>
                    <div class="row"><span class="k">Date de saillie</span><span class="v">${g.dateSaillie}</span></div>
                    <div class="row"><span class="k">Durée de gestation (jours)</span><span class="v">${g.dureeGestationJours}</span></div>
                    <div class="row"><span class="k">Date prévue de mise bas</span><span class="v">${g.datePrevueMiseBas}</span></div>
                </div>
            </div>
        </div>

        <div class="alert alert--info mb-rappel">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>
            <div><b>Rappel</b><br>La mise bas confirme la fin de gestation. Les lots de naissance seront créés selon les informations saisies.</div>
        </div>
    </aside>

</div>

<style>
    .mb-layout { display: flex; gap: 20px; align-items: flex-start; flex-wrap: wrap; }
    .mb-main { flex: 1 1 560px; min-width: 0; }
    .mb-side { flex: 1 1 300px; }
    .mb-main .card, .mb-side .card { margin-bottom: 18px; }

    .mb-sec__title { position: relative; padding-left: 12px; font-weight: 700; font-size: 15px; color: var(--gris-900); margin-bottom: 16px; }
    .mb-sec__title::before { content: ""; position: absolute; left: 0; top: 1px; bottom: 1px; width: 4px; border-radius: 2px; background: var(--marine-600); }
    .mb-sec__desc { color: var(--gris-500); font-size: 12.5px; margin: -6px 0 14px; line-height: 1.5; }

    .mb-side__title { font-weight: 700; font-size: 15px; color: var(--gris-900); margin-bottom: 10px; }
    .mb-summary .row { display: flex; justify-content: space-between; gap: 12px; padding: 9px 0; border-bottom: 1px solid var(--gris-100); font-size: 13px; }
    .mb-summary .row:last-child { border-bottom: 0; }
    .mb-summary .k { color: var(--gris-500); }
    .mb-summary .v { font-weight: 600; color: var(--gris-900); text-align: right; }

    .mb-rappel { align-items: flex-start; gap: 10px; }
    .mb-rappel svg { flex: 0 0 auto; margin-top: 1px; }
</style>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
