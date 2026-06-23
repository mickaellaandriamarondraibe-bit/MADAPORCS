<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Suivis sanitaires" />
<c:set var="crumbs"    value="Santé / <b>Suivis sanitaires</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Suivis sanitaires</h1><p>Maladies, traitements et guérisons par lot</p></div>
  <a class="btn btn--primary" href="${ctx}/sante/suivis/form"><i class="fa-solid fa-plus"></i> Nouveau suivi</a>
</div>

<div class="toolbar"><div class="search"><input class="input" placeholder="Rechercher…" data-filter-input="#tbl-suivis"></div></div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" id="tbl-suivis">
      <thead><tr><th>Lot</th><th>Maladie</th><th>Traitement</th><th>Diagnostic</th><th>Guérison</th><th>État</th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty suivis}">
            <c:forEach var="s" items="${suivis}">
              <tr>
                <td><b>${s.codeLot}</b></td><td>${s.maladie}</td><td>${s.traitement}</td>
                <td><fmt:formatDate value="${s.dateDiagnostic}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:formatDate value="${s.dateGuerison}" pattern="dd/MM/yyyy"/></td>
                <td><span class="badge ${empty s.dateGuerison ? 'badge--amber' : 'badge--green'}">${empty s.dateGuerison ? 'En cours' : 'Guéri'}</span></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise><tr><td colspan="6"><div class="empty"><i class="ico fa-solid fa-stethoscope"></i><p>Aucun suivi sanitaire.</p></div></td></tr></c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
