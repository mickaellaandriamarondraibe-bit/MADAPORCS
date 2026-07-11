<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${empty pageTitle ? 'Module' : pageTitle}" />
<c:set var="crumbs" value="MADAPORC / <b>${pageTitle}</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="card">
  <div class="card__body">
    <div class="empty">
      
      <h2 style="margin-bottom:6px">${empty titre ? 'Module en préparation' : titre}</h2>
      <p>${empty message ? 'Cette page sera disponible prochainement. Le module est en cours de développement par l équipe MADAPORC.' : message}</p>
      <a class="btn btn--ghost" href="${ctx}/dashboard"> Retour au tableau de bord</a>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
