<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Groupes de reproduction" />
<c:set var="crumbs"    value="Reproduction / <b>Groupes</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Groupes de reproduction</h1><p>Saillie partielle d'un lot femelle, date de mise bas calculée automatiquement</p></div>
  <a class="btn btn--primary" href="${ctx}/reproduction/groupes/form"> Nouveau groupe</a>
</div>

<div class="toolbar">
  <div class="search"><input class="input" placeholder="Rechercher un groupe…" data-filter-input="#tbl-gr"></div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-gr">
      <thead>
        <tr><th>Code</th><th>Lot femelle</th><th>Lot mâle</th><th class="num">Femelles</th><th>Saillie</th><th>Mise bas prévue</th><th>Statut</th><th class="actions"></th></tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty groupes}">
            <c:forEach var="g" items="${groupes}">
              <tr>
                <td><a href="${ctx}/reproduction/groupes/${g.id}"><b>${g.codeGroupe}</b></a></td>
                <td>${g.lotFemelle.codeLot}</td>
                <td>${g.lotMale.codeLot}</td>
                <td class="num">${g.nombreFemellesConcernees}</td>
                <td>${g.dateSaillie}</td>
                <td>${g.datePrevueMiseBas}</td>
                <td>
                  <c:choose>
                    <c:when test="${g.statut == 'EN_CYCLE'}"><span class="badge badge--amber"><span class="dot"></span>En cycle</span></c:when>
                    <c:when test="${g.statut == 'MISE_BAS'}"><span class="badge badge--green">Mise bas</span></c:when>
                    <c:when test="${g.statut == 'CLOTURE'}"><span class="badge badge--gray">Clôturé</span></c:when>
                    <c:otherwise><span class="badge badge--blue">${g.statut}</span></c:otherwise>
                  </c:choose>
                </td>
                <td class="actions"><a href="${ctx}/reproduction/groupes/${g.id}">Détail</a></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr><td colspan="8"><div class="empty"><p>Aucun groupe de reproduction.</p><a class="btn btn--primary" href="${ctx}/reproduction/groupes/form">Créer un groupe</a></div></td></tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
