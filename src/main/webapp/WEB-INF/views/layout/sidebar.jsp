<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<aside class="sidebar">
  <div class="sidebar__brand">
    <span class="logo"><i class="fa-solid fa-piggy-bank"></i></span>
    <div>MADAPORC<small>Gestion d'élevage</small></div>
  </div>

  <nav class="nav">
    <a class="nav__link" data-match="/dashboard" href="${ctx}/dashboard">
      <i class="ico fa-solid fa-table-cells-large"></i> Tableau de bord
    </a>

    <div class="nav__section">Cheptel</div>
    <a class="nav__link" data-match="/lots" href="${ctx}/lots">
      <i class="ico fa-solid fa-piggy-bank"></i> Lots de porcs
    </a>


    <div class="nav__section">Reproduction</div>
    <a class="nav__link" data-match="/reproduction/groupes" href="${ctx}/reproduction/groupes">
      <i class="ico fa-solid fa-venus-mars"></i> Groupes
    </a>
    <a class="nav__link" data-match="/reproduction/analyse" href="${ctx}/reproduction/analyse">
      <i class="ico fa-solid fa-chart-line"></i> Analyse reproductive
    </a>
    <a class="nav__link" data-match="/reproduction/alertes" href="${ctx}/reproduction/alertes">
      <i class="ico fa-solid fa-bell"></i> Alertes
      <c:if test="${not empty nbAlertes and nbAlertes > 0}"><span class="nav__badge">${nbAlertes}</span></c:if>
    </a>

    <div class="nav__section">Santé</div>
    <a class="nav__link" data-match="/vaccins" href="${ctx}/vaccins">
      <i class="ico fa-solid fa-syringe"></i> Vaccins
    </a>
    <a class="nav__link" data-match="/vaccinations" href="${ctx}/vaccinations">
      <i class="ico fa-solid fa-calendar-days"></i> Vaccinations
    </a>
    <a class="nav__link" data-match="/sante/suivis" href="${ctx}/sante/suivis">
      <i class="ico fa-solid fa-stethoscope"></i> Suivis sanitaires
    </a>

    <div class="nav__section">Commerce</div>
    <a class="nav__link" data-match="/clients" href="${ctx}/clients">
      <i class="ico fa-solid fa-user"></i> Clients
    </a>
    <a class="nav__link" data-match="/ventes" href="${ctx}/ventes">
      <i class="ico fa-solid fa-receipt"></i> Ventes
    </a>

    <div class="nav__section">Stocks & Finance</div>
    <a class="nav__link" data-match="/ingredients" href="${ctx}/ingredients">
      <i class="ico fa-solid fa-wheat-awn"></i> Ingrédients
    </a>
    <a class="nav__link" data-match="/stocks/mouvements" href="${ctx}/stocks/mouvements">
      <i class="ico fa-solid fa-right-left"></i> Mouvements stock
    </a>
    <a class="nav__link" data-match="/depenses" href="${ctx}/depenses">
      <i class="ico fa-solid fa-money-bill-wave"></i> Dépenses
    </a>

    <div class="nav__section">Données</div>
    <a class="nav__link" data-match="/imports" href="${ctx}/imports">
      <i class="ico fa-solid fa-file-import"></i> Import / Export
    </a>
    <a class="nav__link" data-match="/rapports" href="${ctx}/rapports">
      <i class="ico fa-solid fa-file-lines"></i> Rapports
    </a>

    <div class="nav__section">Administration</div>
    <a class="nav__link" data-match="/utilisateurs" href="${ctx}/utilisateurs">
      <i class="ico fa-solid fa-gear"></i> Utilisateurs
    </a>
    <a class="nav__link" href="${ctx}/logout">
      <i class="ico fa-solid fa-power-off"></i> Déconnexion
    </a>
  </nav>
</aside>
