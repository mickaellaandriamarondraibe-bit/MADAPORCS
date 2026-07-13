<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Erreur" />
<c:set var="crumbs" value="MADAPORC / <b>Erreur</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Une erreur est survenue</h1>
    <p>L'opération n'a pas pu aboutir.</p>
  </div>
</div>

<div class="card" style="max-width:640px">
  <div class="card__body">
    <div class="alert alert--err">
      <span>${empty messageErreur ? "Une erreur inattendue est survenue." : messageErreur}</span>
    </div>
    <a class="btn btn--primary" href="${ctx}/dashboard">Retour au tableau de bord</a>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
