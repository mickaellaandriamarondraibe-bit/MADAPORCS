<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Layout principal MADAPORC (réservé MIARO).
  Usage dans une vue :
    <c:set var="pageTitle" value="Tableau de bord" />
    <c:set var="activeNav"  value="dashboard" />
    <c:set var="crumbs"     value="Accueil / <b>Tableau de bord</b>" />
    <%@ include file="/WEB-INF/views/layout/header.jsp" %>
        ... contenu ...
    <%@ include file="/WEB-INF/views/layout/footer.jsp" %>
--%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>

  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${empty pageTitle ? 'MADAPORC' : pageTitle} · MADAPORC</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <%-- Police chargée de façon non bloquante : la page s'affiche tout de suite
       (police de repli), Nunito s'applique dès qu'elle est disponible. Évite
       la latence réseau à chaque changement de page. --%>
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;500;600;700;800;900&display=swap" media="print" onload="this.media='all'">
  <noscript><link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;500;600;700;800;900&display=swap"></noscript>
  <link rel="stylesheet" href="${ctx}/css/app.css">
</head>
<body>

<div id="toastContainer" style="position: fixed; top: 20px; right: 20px; z-index: 9999; display: flex; flex-direction: column; gap: 10px;"></div>

<div class="app-shell">

  <%-- Sidebar globale --%>
  <%@ include file="/WEB-INF/views/layout/sidebar.jsp" %>
  <div class="sidebar-backdrop"></div>

  <div class="main">

    <header class="topbar">
      <button class="menu-toggle" data-menu-toggle aria-label="Menu">☰</button>
      <button type="button" class="btn btn--ghost btn--sm" onclick="history.back()" title="Page précédente">&larr; Retour</button>
      <div>
        <c:if test="${not empty crumbs}">
          <div class="topbar__crumbs">${crumbs}</div>
        </c:if>
      </div>
      <div class="topbar__spacer"></div>
      <div class="topbar__user">
        <div class="avatar">
          <c:out value="${empty sessionScope.nom ? 'MA' : fn:toUpperCase(fn:substring(sessionScope.nom,0,2))}" default="MA"/>
        </div>
        <div>
          <span><c:out value="${empty sessionScope.nom ? 'Utilisateur' : sessionScope.nom}"/></span><br>
          <small><c:out value="${empty sessionScope.roleNom ? 'UTILISATEUR' : sessionScope.roleNom}"/></small>
        </div>
      </div>
    </header>
    <main class="content">
      <%-- Messages flash standardisés --%>
      <c:if test="${not empty success}"><div class="alert alert--ok"> <span>${success}</span></div></c:if>
      <c:if test="${not empty error}"><div class="alert alert--err"> <span>${error}</span></div></c:if>
      <c:if test="${not empty warning}"><div class="alert alert--warn"> <span>${warning}</span></div></c:if>
      <c:if test="${not empty info}"><div class="alert alert--info"> <span>${info}</span></div></c:if>
