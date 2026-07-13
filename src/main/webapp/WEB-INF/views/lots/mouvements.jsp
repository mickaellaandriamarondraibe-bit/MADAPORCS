<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Mouvements — ${lot.codeLot}" />
<c:set var="crumbs" value="Cheptel / Lots / ${lot.codeLot} / <b>Mouvements</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Mouvements d'effectif</h1>
    <p>
      Lot ${lot.codeLot} · effectif actuel :
      <b>${lot.effectifActuel}</b>
    </p>
  </div>

  <a class="btn btn--ghost" href="${ctx}/lots/${lot.id}">
     Retour au lot
  </a>
</div>

<c:if test="${not empty error}">
  <div class="alert alert--danger">
    ${error}
  </div>
</c:if>

<div class="grid-2">

  <div class="card">
    <div class="card__head">
      <h2>Historique</h2>
    </div>

    <div class="card__body" style="padding:0">
      <table class="tbl" data-paginate="10">
        <thead>
          <tr>
            <th>Date</th>
            <th>Type</th>
            <th class="num">Quantité</th>
            <th>Observation</th>
          </tr>
        </thead>

        <tbody>
          <c:choose>
            <c:when test="${not empty mouvements}">
              <c:forEach var="m" items="${mouvements}">
                <tr>
                  <td>${m.dateMouvement}</td>

                  <td>
                    <c:choose>
                      <c:when test="${m.typeMouvement == 'DECES'
                                      or m.typeMouvement == 'VENTE'
                                      or m.typeMouvement == 'TRANSFERT_SORTIE'}">
                        <span class="badge badge--red">
                          ${m.typeMouvement}
                        </span>
                      </c:when>

                      <c:otherwise>
                        <span class="badge badge--green">
                          ${m.typeMouvement}
                        </span>
                      </c:otherwise>
                    </c:choose>
                  </td>

                  <td class="num">
                    <b>${m.quantite}</b>
                  </td>

                  <td class="muted">
                    <c:choose>
                      <c:when test="${not empty m.observation}">
                        ${m.observation}
                      </c:when>
                      <c:otherwise>—</c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </c:forEach>
            </c:when>

            <c:otherwise>
              <tr>
                <td colspan="4">
                  <div class="empty" style="padding:30px">
                    Aucun mouvement enregistré.
                  </div>
                </td>
              </tr>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>

  <div class="card">
    <div class="card__head">
      <h2>Nouveau mouvement</h2>
    </div>

    <div class="card__body">
      <form method="post" action="${ctx}/lots/mouvements/save" class="stack">
        <input type="hidden" name="lotId" value="${lot.id}" />

        <div class="field">
          <label>Type de mouvement <span class="req">*</span></label>
          <select class="select" name="typeMouvement" required>
            <option value="">— Choisir —</option>
            <option value="ENTREE" ${mouvement.typeMouvement == 'ENTREE' ? 'selected' : ''}>
              Entrée
            </option>
            <option value="NAISSANCE" ${mouvement.typeMouvement == 'NAISSANCE' ? 'selected' : ''}>
              Naissance
            </option>
            <option value="DECES" ${mouvement.typeMouvement == 'DECES' ? 'selected' : ''}>
              Décès
            </option>
            <option value="TRANSFERT_ENTREE" ${mouvement.typeMouvement == 'TRANSFERT_ENTREE' ? 'selected' : ''}>
              Transfert entrée
            </option>
            <option value="TRANSFERT_SORTIE" ${mouvement.typeMouvement == 'TRANSFERT_SORTIE' ? 'selected' : ''}>
              Transfert sortie
            </option>
          </select>
        </div>

        <div class="field">
          <label>Quantité <span class="req">*</span></label>
          <input class="input"
                 type="number"
                 min="1"
                 name="quantite"
                 value="${mouvement.quantite}"
                 required />
          <span class="hint">
            Une sortie ne peut pas dépasser l'effectif actuel.
          </span>
        </div>

        <div class="field">
          <label>Date</label>
          <input class="input"
                 type="date"
                 name="dateMouvement"
                 value="${mouvement.dateMouvement}" />
        </div>

        <div class="field">
          <label>Observation</label>
          <textarea class="textarea" name="observation">${mouvement.observation}</textarea>
        </div>

        <button class="btn btn--primary w-full"
                style="justify-content:center"
                type="submit">
          Enregistrer le mouvement
        </button>
      </form>
    </div>
  </div>

</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>