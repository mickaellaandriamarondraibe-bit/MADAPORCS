<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isAdmin" value="${sessionScope.roleNom == 'ADMIN'}" />
<aside class="sidebar">
  <div class="sidebar__brand">
    <span class="logo"><img src="${ctx}/css/oIBastcY.jpeg" class="logo"></span>
    <div>MADAPORC<small>Gestion d'élevage</small></div>
  </div>

  <nav class="nav">
    <a class="nav__link" data-match="/dashboard" href="${ctx}/dashboard"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/></svg></span>Tableau de bord</a>
    <a class="nav__link" data-match="/calendrier" href="${ctx}/calendrier"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg></span>Calendrier</a>
    <a class="nav__link" data-match="/mouvements" href="${ctx}/mouvements"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="16 3 21 3 21 8"/><line x1="4" y1="20" x2="21" y2="3"/><polyline points="21 16 21 21 16 21"/><line x1="15" y1="15" x2="21" y2="21"/><line x1="4" y1="4" x2="9" y2="9"/></svg></span>Mouvements</a>

    <div class="nav__group">
      <button type="button" class="nav__group-title" data-nav-group>Cheptel</button>
      <div class="nav__group-items">
        <a class="nav__link" data-match="/lots" href="${ctx}/lots"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="12 2 2 7 12 12 22 7 12 2"/><polyline points="2 17 12 22 22 17"/><polyline points="2 12 12 17 22 12"/></svg></span>Lots de porcs</a>
      </div>
    </div>

    <div class="nav__group">
      <button type="button" class="nav__group-title" data-nav-group>Reproduction</button>
      <div class="nav__group-items">
        <a class="nav__link" data-match="/reproduction/groupes" href="${ctx}/reproduction/groupes"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg></span>Groupes</a>
        <a class="nav__link" data-match="/reproduction/analyse" href="${ctx}/reproduction/analyse"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg></span>Analyse reproductive</a>
        <a class="nav__link" data-match="/reproduction/alertes" href="${ctx}/reproduction/alertes"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 0 1-3.46 0"/></svg></span>Alertes
          <c:if test="${not empty nbAlertes and nbAlertes > 0}"><span class="nav__badge">${nbAlertes}</span></c:if>
        </a>
      </div>
    </div>

    <div class="nav__group">
      <button type="button" class="nav__group-title" data-nav-group>Santé</button>
      <div class="nav__group-items">
        <a class="nav__link" data-match="/vaccins" href="${ctx}/vaccins"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg></span>Vaccins</a>
        <a class="nav__link" data-match="/vaccinations" href="${ctx}/vaccinations"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg></span>Vaccinations</a>
        <a class="nav__link" data-match="/sante/suivis" href="${ctx}/sante/suivis"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg></span>Suivis sanitaires</a>
      </div>
    </div>

    <div class="nav__group">
      <button type="button" class="nav__group-title" data-nav-group>Commerce</button>
      <div class="nav__group-items">
        <a class="nav__link" data-match="/clients" href="${ctx}/clients"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></span>Clients</a>
        <a class="nav__link" data-match="/ventes" href="${ctx}/ventes"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/></svg></span>Ventes</a>
      </div>
    </div>

    <%-- Dépenses réservées à l'ADMIN (AuthInterceptor bloque déjà /depenses côté serveur) --%>
    <c:if test="${isAdmin}">
      <div class="nav__group">
        <button type="button" class="nav__group-title" data-nav-group>Stocks & Finance</button>
        <div class="nav__group-items">
          <a class="nav__link" data-match="/ingredients" href="${ctx}/ingredients"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="21 8 21 21 3 21 3 8"/><rect x="1" y="3" width="22" height="5"/><line x1="10" y1="12" x2="14" y2="12"/></svg></span>Ingrédients</a>
          <a class="nav__link" data-match="/stocks/mouvements" href="${ctx}/stocks/mouvements"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="17 1 21 5 17 9"/><path d="M3 11V9a4 4 0 0 1 4-4h14"/><polyline points="7 23 3 19 7 15"/><path d="M21 13v2a4 4 0 0 1-4 4H3"/></svg></span>Mouvements stock</a>
          <a class="nav__link" data-match="/depenses" href="${ctx}/depenses"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg></span>Dépenses</a>
        </div>
      </div>
    </c:if>
    <c:if test="${!isAdmin}">
      <div class="nav__group">
        <button type="button" class="nav__group-title" data-nav-group>Stocks</button>
        <div class="nav__group-items">
          <a class="nav__link" data-match="/ingredients" href="${ctx}/ingredients"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="21 8 21 21 3 21 3 8"/><rect x="1" y="3" width="22" height="5"/><line x1="10" y1="12" x2="14" y2="12"/></svg></span>Ingrédients</a>
          <a class="nav__link" data-match="/stocks/mouvements" href="${ctx}/stocks/mouvements"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="17 1 21 5 17 9"/><path d="M3 11V9a4 4 0 0 1 4-4h14"/><polyline points="7 23 3 19 7 15"/><path d="M21 13v2a4 4 0 0 1-4 4H3"/></svg></span>Mouvements stock</a>
        </div>
      </div>
    </c:if>

    <%-- Import/Export réservé à l'ADMIN --%>
    <c:if test="${isAdmin}">
      <div class="nav__group">
        <button type="button" class="nav__group-title" data-nav-group>Données</button>
        <div class="nav__group-items">
          <a class="nav__link" data-match="/imports" href="${ctx}/imports"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg></span>Import / Export</a>
          <a class="nav__link" data-match="/rapports" href="${ctx}/rapports"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg></span>Rapports</a>
        </div>
      </div>
    </c:if>
    <c:if test="${!isAdmin}">
      <div class="nav__group">
        <button type="button" class="nav__group-title" data-nav-group>Données</button>
        <div class="nav__group-items">
          <a class="nav__link" data-match="/rapports" href="${ctx}/rapports"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg></span>Rapports</a>
        </div>
      </div>
    </c:if>

    <%-- Administration réservée à l'ADMIN --%>
    <c:if test="${isAdmin}">
      <div class="nav__group">
        <button type="button" class="nav__group-title" data-nav-group>Administration</button>
        <div class="nav__group-items">
          <a class="nav__link" data-match="/utilisateurs" href="${ctx}/utilisateurs"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg></span>Utilisateurs</a>
          <a class="nav__link" data-match="/email" href="${ctx}/email"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-10 5L2 7"/></svg></span>Destinataires alertes</a>
        </div>
      </div>
    </c:if>

    <a class="nav__link" href="${ctx}/logout"><span class="ico"><svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span>Déconnexion</a>
  </nav>
</aside>
