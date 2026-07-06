<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Suivis sanitaires" />
<c:set var="crumbs" value="Santé / Suivis sanitaires" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Suivis sanitaires</h1>
    <p>Maladies, traitements et guérisons par lot</p>
  </div>

  <a class="btn btn--primary" href="${ctx}/sante/suivis/form">
     Nouveau suivi
  </a>
</div>

<div class="toolbar">
  <div class="search">
    <input class="input" placeholder="Rechercher…" data-filter-input="#tbl-suivis" />
  </div>
</div>

<div class="card">
  <div class="table-wrap">
    <table class="tbl" data-paginate="10" id="tbl-suivis">
      <thead>
        <tr>
          <th>Lot</th>
          <th>Maladie</th>
          <th>Traitement</th>
          <th>Diagnostic</th>
          <th>Guérison</th>
          <th>État</th>
          <th class="actions">Actions</th>
        </tr>
      </thead>

      <tbody>
        <c:choose>
          <c:when test="${not empty suivis}">
            <c:forEach var="s" items="${suivis}">
              <tr>
                <td>
                  <b>${s.lot.codeLot}</b>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${not empty s.maladie}">
                      ${s.maladie.nom}
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${not empty s.traitement}">
                      ${s.traitement.nom}
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                  </c:choose>
                </td>

                <td>${s.dateDiagnostic}</td>

                <td>
                  <c:choose>
                    <c:when test="${not empty s.dateGuerison}">
                      ${s.dateGuerison}
                    </c:when>
                    <c:otherwise>—</c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <span class="badge ${empty s.dateGuerison ? 'badge--amber' : 'badge--green'}">
                    ${empty s.dateGuerison ? 'En cours' : 'Guéri'}
                  </span>
                </td>

                <td class="actions">
                  <a href="${ctx}/sante/suivis/form?id=${s.id}">Modifier</a>
                </td>
              </tr>
            </c:forEach>
          </c:when>

          <c:otherwise>
            <tr>
              <td colspan="7">
                <div class="empty">
                  
                  <p>Aucun suivi sanitaire.</p>
                </div>
              </td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>