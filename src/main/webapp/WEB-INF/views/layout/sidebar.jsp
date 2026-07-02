<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<aside class="sidebar">
  <div class="sidebar__brand">
    <span class="logo"></span>
    <div>MADAPORC<small>Gestion d'élevage</small></div>
  </div>

  <nav class="nav">
    <a class="nav__link" data-match="/dashboard" href="${ctx}/dashboard">
       Tableau de bord
    </a>

    <div class="nav__section">Cheptel</div>
    <a class="nav__link" data-match="/lots" href="${ctx}/lots">
       Lots de porcs
    </a>


    <div class="nav__section">Reproduction</div>
    <a class="nav__link" data-match="/reproduction/groupes" href="${ctx}/reproduction/groupes">
       Groupes
    </a>
    <a class="nav__link" data-match="/reproduction/analyse" href="${ctx}/reproduction/analyse">
       Analyse reproductive
    </a>
    <a class="nav__link" data-match="/reproduction/alertes" href="${ctx}/reproduction/alertes">
       Alertes
      <c:if test="${not empty nbAlertes and nbAlertes > 0}"><span class="nav__badge">${nbAlertes}</span></c:if>
    </a>

    <div class="nav__section">Santé</div>
    <a class="nav__link" data-match="/vaccins" href="${ctx}/vaccins">
       Vaccins
    </a>
    <a class="nav__link" data-match="/vaccinations" href="${ctx}/vaccinations">
       Vaccinations
    </a>
    <a class="nav__link" data-match="/sante/suivis" href="${ctx}/sante/suivis">
       Suivis sanitaires
    </a>

    <div class="nav__section">Commerce</div>
    <a class="nav__link" data-match="/clients" href="${ctx}/clients">
       Clients
    </a>
    <a class="nav__link" data-match="/ventes" href="${ctx}/ventes">
       Ventes
    </a>

    <div class="nav__section">Stocks & Finance</div>
    <a class="nav__link" data-match="/ingredients" href="${ctx}/ingredients">
       Ingrédients
    </a>
    <a class="nav__link" data-match="/stocks/mouvements" href="${ctx}/stocks/mouvements">
       Mouvements stock
    </a>
    <a class="nav__link" data-match="/depenses" href="${ctx}/depenses">
       Dépenses
    </a>

    <div class="nav__section">Données</div>
    <a class="nav__link" data-match="/imports" href="${ctx}/imports">
       Import / Export
    </a>
    <a class="nav__link" data-match="/rapports" href="${ctx}/rapports">
       Rapports
    </a>

    <div class="nav__section">Administration</div>
    <a class="nav__link" data-match="/utilisateurs" href="${ctx}/utilisateurs">
       Utilisateurs
    </a>
    <a class="nav__link" href="${ctx}/logout">
       Déconnexion
    </a>
  </nav>
</aside>
