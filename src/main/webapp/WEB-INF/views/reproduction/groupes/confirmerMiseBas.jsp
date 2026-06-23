<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="g" value="${detail}" />
<c:set var="pageTitle" value="Confirmer la mise bas" />
<c:set var="crumbs"    value="Reproduction / Groupes / ${g.codeGroupe} / <b>Mise bas</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Confirmer la mise bas — ${g.codeGroupe}</h1><p>Saisie manuelle obligatoire. La création du lot naissance reste optionnelle.</p></div>
  <a class="btn btn--ghost" href="${ctx}/reproduction/groupes/${g.id}"><i class="fa-solid fa-arrow-left"></i> Retour au groupe</a>
</div>

<div class="card" style="max-width:820px">
  <div class="card__body">
    <%-- POST /reproduction/groupes/{id}/confirmer-mise-bas, bind ConfirmationMiseBasDTO --%>
    <form method="post" action="${ctx}/reproduction/groupes/${g.id}/confirmer-mise-bas">
      <div class="form-grid">
        <div class="field span-2">
          <label>Date réelle de mise bas <span class="req">*</span></label>
          <input class="input" type="date" name="dateReelleMiseBas" required>
          <span class="hint">Doit être ≥ date de saillie (<fmt:formatDate value="${g.dateSaillie}" pattern="dd/MM/yyyy"/>).</span>
        </div>
        <div class="field">
          <label>Porcelets nés <span class="req">*</span></label>
          <input class="input" type="number" min="0" name="porceletsNes" data-nes required>
        </div>
        <div class="field">
          <label>Porcelets vivants <span class="req">*</span></label>
          <input class="input" type="number" min="0" name="porceletsVivants" data-vivants required>
        </div>
        <div class="field">
          <label>Porcelets morts <span class="req">*</span></label>
          <input class="input" type="number" min="0" name="porceletsMorts" data-morts required>
        </div>
        <div class="field">
          <label>&nbsp;</label>
          <div class="alert alert--warn" data-mb-warn style="display:none;margin:0"><i class="fa-solid fa-triangle-exclamation"></i> <span>Vivants + morts ne peut pas dépasser le nombre de porcelets nés.</span></div>
        </div>

        <div class="field span-2">
          <label class="flex items-center gap-8" style="font-weight:600">
            <input type="checkbox" name="creerLotNaissance" value="true" checked>
            Créer un lot naissance après confirmation
          </label>
          <span class="hint">Le lot naissance sera lié au lot mère ${g.codeLotFemelle} et au groupe ${g.codeGroupe}.</span>
        </div>
        <div class="field span-2">
          <label>Code du lot naissance</label>
          <input class="input" name="codeLotNaissance" placeholder="LOT-N-001" value="${suggestionCodeLotNaissance}">
        </div>
      </div>

      <div class="form-actions">
        <a class="btn btn--ghost" href="${ctx}/reproduction/groupes/${g.id}">Annuler</a>
        <button class="btn btn--primary" type="submit" data-mb-submit>Confirmer la mise bas</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
