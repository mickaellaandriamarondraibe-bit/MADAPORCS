<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="g" value="${detail}" />

<c:set var="pageTitle" value="Confirmer la mise bas" />
<c:set var="crumbs" value="Reproduction / Groupes / ${g.codeGroupe} / Mise bas" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
    <div>
        <h1>Confirmer la mise bas — ${g.codeGroupe}</h1>
        <p>Saisie des résultats de mise bas.</p>
    </div>

    <a class="btn btn--ghost" href="${ctx}/reproduction/groupes/${g.id}">
        Retour au groupe
    </a>
</div>

<c:if test="${not empty erreur}">
    <div class="alert alert--danger">
        ${erreur}
    </div>
</c:if>

<div class="card" style="max-width:820px">
    <div class="card__body">

        <form:form method="post"
                   modelAttribute="dto"
                   action="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">

            <c:if test="${not empty alerteId}">
                <input type="hidden" name="alerteId" value="${alerteId}" />
            </c:if>

            <div class="form-grid">

                <div class="field span-2">
                    <label>Date réelle de mise bas <span class="req">*</span></label>
                    <form:input class="input" type="date" path="dateMiseBasReelle" />
                    <form:errors path="dateMiseBasReelle" cssClass="error" />
                    <span class="hint">Doit être ≥ date de saillie : ${g.dateSaillie}</span>
                </div>

                <div class="field">
                    <label>Femelles gestantes <span class="req">*</span></label>
                    <form:input class="input" type="number" min="0" path="nbFemellesGestantes" />
                    <form:errors path="nbFemellesGestantes" cssClass="error" />
                </div>

                <div class="field">
                    <label>Femelles non gestantes <span class="req">*</span></label>
                    <form:input class="input" type="number" min="0" path="nbFemellesNonGestantes" />
                    <form:errors path="nbFemellesNonGestantes" cssClass="error" />
                </div>

                <div class="field">
                    <label>Femelles ayant mis bas <span class="req">*</span></label>
                    <form:input class="input" type="number" min="0" path="nbFemellesMiseBas" />
                    <form:errors path="nbFemellesMiseBas" cssClass="error" />
                </div>

                <div class="field">
                    <label>Porcelets nés <span class="req">*</span></label>
                    <form:input class="input" type="number" min="0" path="nbPorceletsNes" />
                    <form:errors path="nbPorceletsNes" cssClass="error" />
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

                <div class="field span-2" style="border-top:1px solid #e1e0d9; padding-top:12px; margin-top:4px">
                    <strong>Création du / des lot(s) naissance</strong>
                    <span class="hint">Les porcelets vivants deviennent automatiquement un lot femelle et/ou un lot mâle.</span>
                </div>

                <div class="field">
                    <label>Porcelets femelles vivants</label>
                    <form:input class="input" type="number" min="0" path="nbFemelles" />
                    <form:errors path="nbFemelles" cssClass="error" />
                </div>

                <div class="field">
                    <label>Porcelets mâles vivants</label>
                    <form:input class="input" type="number" min="0" path="nbMales" />
                    <form:errors path="nbMales" cssClass="error" />
                </div>

                <div class="field span-2">
                    <label>Objectif du / des lot(s) naissance</label>
                    <form:select class="input" path="objectifLot">
                        <form:option value="ENGRAISSEMENT">Engraissement</form:option>
                        <form:option value="REPRODUCTION">Reproduction</form:option>
                    </form:select>
                </div>

                <div class="field span-2">
                    <label>Observation</label>
                    <form:textarea class="input" path="observation" rows="3" />
                </div>

            </div>

            <div class="form-actions">
                <a class="btn btn--ghost" href="${ctx}/reproduction/groupes/${g.id}">
                    Annuler
                </a>

                <button class="btn btn--primary" type="submit">
                    Confirmer et créer le(s) lot(s)
                </button>
            </div>

        </form:form>

    </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
