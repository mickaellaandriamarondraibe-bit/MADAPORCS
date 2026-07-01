<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Tableau de bord" />
<c:set var="activeNav" value="dashboard" />
<c:set var="crumbs" value="MADAPORC / <b>Tableau de bord</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<c:set var="d" value="${dashboard}" />

<div class="page-head">
  <div>
    <h1>Tableau de bord</h1>
    <p>Indicateurs clés de l'élevage au ${dateJour}</p>
  </div>

  <a class="btn btn--gold" href="${ctx}/rapports">
    <i class="fa-solid fa-file-lines"></i> Générer un rapport
  </a>
</div>

<div class="kpi-grid">

  <div class="kpi">
    <div class="kpi__label">Lots actifs</div>
    <div class="kpi__value">
      <c:out value="${d.lotsActifs}" default="0" />
    </div>
    <div class="kpi__sub">
      <c:out value="${d.totalPorcs}" default="0" /> porcs actifs
    </div>
  </div>

  <div class="kpi">
    <div class="kpi__label">Groupes actifs</div>
    <div class="kpi__value">
      <c:out value="${d.groupesActifs}" default="0" />
    </div>
    <div class="kpi__sub">
      <c:out value="${d.misesBasProches}" default="0" /> mise(s) bas proche(s)
    </div>
  </div>

  <div class="kpi kpi--gold">
    <div class="kpi__label">Aptitude globale</div>
    <div class="kpi__value">
      <c:out value="${d.tauxAptitudeGlobale}" default="0.00" />%
    </div>
    <div class="kpi__sub up">
      Fertilité observée : <c:out value="${d.tauxFertiliteObserve}" default="0.00" />%
    </div>
  </div>

  <div class="kpi kpi--blue">
    <div class="kpi__label">Ventes du mois</div>
    <div class="kpi__value">
      <fmt:formatNumber value="${empty d.ventesMois ? 0 : d.ventesMois}" type="number" maxFractionDigits="0" />
    </div>
    <div class="kpi__sub">Ar - chiffre d'affaires validé</div>
  </div>

  <div class="kpi kpi--danger">
    <div class="kpi__label">Dépenses du mois</div>
    <div class="kpi__value">
      <fmt:formatNumber value="${empty d.depensesMois ? 0 : d.depensesMois}" type="number" maxFractionDigits="0" />
    </div>
    <div class="kpi__sub">Ar - sorties</div>
  </div>

  <div class="kpi ${(empty d.beneficeNet ? 0 : d.beneficeNet) lt 0 ? 'kpi--danger' : ''}">
    <div class="kpi__label">Bénéfice net</div>
    <div class="kpi__value">
      <fmt:formatNumber value="${empty d.beneficeNet ? 0 : d.beneficeNet}" type="number" maxFractionDigits="0" />
    </div>
    <div class="kpi__sub ${(empty d.beneficeNet ? 0 : d.beneficeNet) lt 0 ? 'down' : 'up'}">
      Ar - mois courant
    </div>
  </div>

</div>

<div class="card mt-24">
  <div class="card__head">
    <h2>Statistiques</h2>

    <div class="flex gap-8">
      <button type="button" class="btn btn--primary btn--sm js-stat-tab" data-stat-type="finances">
        Finances
      </button>

      <button type="button" class="btn btn--ghost btn--sm js-stat-tab" data-stat-type="reproduction">
        Reproduction
      </button>

      <button type="button" class="btn btn--ghost btn--sm js-stat-tab" data-stat-type="cheptel">
        Cheptel
      </button>
    </div>
  </div>

  <div class="card__body">
    <div class="dashboard-chart"
         id="dashboardChart"
         data-ventes="${empty d.ventesMois ? 0 : d.ventesMois}"
         data-depenses="${empty d.depensesMois ? 0 : d.depensesMois}"
         data-benefice="${empty d.beneficeNet ? 0 : d.beneficeNet}"
         data-aptitude="${empty d.tauxAptitudeGlobale ? 0 : d.tauxAptitudeGlobale}"
         data-fertilite="${empty d.tauxFertiliteObserve ? 0 : d.tauxFertiliteObserve}"
         data-lots="${empty d.lotsActifs ? 0 : d.lotsActifs}"
         data-porcs="${empty d.totalPorcs ? 0 : d.totalPorcs}"
         data-groupes="${empty d.groupesActifs ? 0 : d.groupesActifs}">

      <div class="dashboard-chart__title" id="chartTitle">
        Statistiques financières
      </div>

      <div class="dashboard-chart__plot">
        <canvas id="dashLineChart"
                role="img"
                aria-label="Graphique en courbes des statistiques de l'élevage">
          Données statistiques mensuelles.
        </canvas>
      </div>
    </div>
  </div>
</div>

<div class="grid-2 mt-24">

  <div class="card">
    <div class="card__head">
      <h2>Mises bas proches</h2>
      <a class="btn btn--ghost btn--sm" href="${ctx}/reproduction/alertes">
        Voir les alertes
      </a>
    </div>

    <div class="card__body" style="padding:0">
      <div class="table-wrap">
        <table class="tbl">
          <thead>
            <tr>
              <th>Groupe</th>
              <th>Lot femelle</th>
              <th>Date prévue</th>
              <th>Statut</th>
            </tr>
          </thead>

          <tbody>
            <c:choose>
              <c:when test="${not empty misesBasProches}">
                <c:forEach var="groupe" items="${misesBasProches}">
                  <tr>
                    <td>
                      <b><c:out value="${groupe.codeGroupe}" /></b>
                    </td>

                    <td>
                      <c:out value="${groupe.lotFemelle.codeLot}" />
                    </td>

                    <td>
                      <c:out value="${groupe.datePrevueMiseBas}" />
                    </td>

                    <td>
                      <span class="badge badge--amber">
                        <span class="dot"></span>
                        <c:out value="${groupe.statut}" />
                      </span>
                    </td>
                  </tr>
                </c:forEach>
              </c:when>

              <c:otherwise>
                <tr>
                  <td colspan="4">
                    <div class="empty" style="padding:28px">
                      Aucune mise bas proche.
                    </div>
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <div class="stack">

    <div class="card">
      <div class="card__head">
        <h2>Stocks faibles</h2>
        <a class="btn btn--ghost btn--sm" href="${ctx}/ingredients">
          Gérer
        </a>
      </div>

      <div class="card__body" style="padding:0">
        <table class="tbl">
          <thead>
            <tr>
              <th>Ingrédient</th>
              <th class="num">Stock</th>
            </tr>
          </thead>

          <tbody>
            <c:choose>
              <c:when test="${not empty d.stocksFaibles}">
                <c:forEach var="ingredient" items="${d.stocksFaibles}">
                  <tr>
                    <td>
                      <c:out value="${ingredient.nom}" />
                      <span class="badge badge--red">bas</span>
                    </td>

                    <td class="num">
                      <c:out value="${ingredient.stockActuel}" />
                      <c:out value="${ingredient.unite}" />
                    </td>
                  </tr>
                </c:forEach>
              </c:when>

              <c:otherwise>
                <tr>
                  <td colspan="2">
                    <div class="empty" style="padding:22px">
                      Tous les stocks sont corrects.
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
        <h2>Vaccinations à venir</h2>
        <a class="btn btn--ghost btn--sm" href="${ctx}/vaccinations">
          Planning
        </a>
      </div>

      <div class="card__body" style="padding:0">
        <table class="tbl">
          <thead>
            <tr>
              <th>Lot</th>
              <th>Vaccin</th>
              <th>Date rappel</th>
            </tr>
          </thead>

          <tbody>
            <c:choose>
              <c:when test="${not empty d.vaccinationsAVenir}">
                <c:forEach var="vaccination" items="${d.vaccinationsAVenir}">
                  <tr>
                    <td>
                      <c:out value="${vaccination.lot.codeLot}" />
                    </td>

                    <td>
                      <c:out value="${vaccination.vaccin.nom}" />
                    </td>

                    <td>
                      <c:out value="${vaccination.dateRappel}" />
                    </td>
                  </tr>
                </c:forEach>
              </c:when>

              <c:otherwise>
                <tr>
                  <td colspan="3">
                    <div class="empty" style="padding:22px">
                      Aucune vaccination planifiée.
                    </div>
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</div>

<style>
  .dashboard-chart__title {
    color: var(--gris-700);
    font-size: 13px;
    font-weight: 700;
    margin-bottom: 8px;
  }

  .dashboard-chart__legend {
    display: flex;
    flex-wrap: wrap;
    gap: 14px;
    margin-bottom: 12px;
    font-size: 12px;
    color: var(--gris-500);
  }

  .dashboard-chart__legend-item {
    display: flex;
    align-items: center;
    gap: 5px;
  }

  .dashboard-chart__legend-line {
    width: 18px;
    height: 3px;
    border-radius: 2px;
    display: inline-block;
  }

  .dashboard-chart__legend-dash {
    width: 18px;
    height: 0;
    border-top: 2px dashed currentColor;
    display: inline-block;
  }

  .dashboard-chart__plot {
    position: relative;
    width: 100%;
    height: 300px;
  }

  .dashboard-chart__plot canvas {
    width: 100% !important;
    height: 100% !important;
  }
</style>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>

<script>
(function () {
  const chart = document.getElementById('dashboardChart');
  const buttons = document.querySelectorAll('.js-stat-tab');
  const titleEl = document.getElementById('chartTitle');
  const canvas = document.getElementById('dashLineChart');

  if (!chart || !canvas) {
    return;
  }

  // Lit une valeur unique stockée dans data-... (ex: data-ventes)
  function num(name) {
    return Number(String(chart.dataset[name] || 0).replace(',', '.')) || 0;
  }

  // Étiquettes des mois (12 mois réels venant du serveur)
  const mois = [
    <c:forEach var="m" items="${d.moisLabels}" varStatus="s">'${m}'<c:if test="${not s.last}">,</c:if></c:forEach>
  ];

  // Séries mensuelles réelles du cheptel
  const porcsMois = [
    <c:forEach var="v" items="${d.totalPorcsParMois}" varStatus="s">${v}<c:if test="${not s.last}">,</c:if></c:forEach>
  ];
  const lotsMois = [
    <c:forEach var="v" items="${d.lotsActifsParMois}" varStatus="s">${v}<c:if test="${not s.last}">,</c:if></c:forEach>
  ];
  const groupesMois = [
    <c:forEach var="v" items="${d.groupesActifsParMois}" varStatus="s">${v}<c:if test="${not s.last}">,</c:if></c:forEach>
  ];

  const labelsMois = mois.length > 0
    ? mois
    : ['Jan', 'Fev', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Aou', 'Sep', 'Oct', 'Nov', 'Dec'];

  // Couleurs
  const BLEU = '#2a78d6', ROUGE = '#e34948', VERT = '#1baf7a', OR = '#eda100';

  // Configuration de chaque onglet : le bon type de graphe par donnée
  const tabs = {
    // Comparaison de montants -> barres
    finances: {
      title: 'Finances du mois (Ar)',
      type: 'bar',
      unite: ' Ar',
      data: {
        labels: ['Ventes', 'Dépenses', 'Bénéfice net'],
        datasets: [{
          label: 'Montant',
          data: [num('ventes'), num('depenses'), num('benefice')],
          backgroundColor: [BLEU, ROUGE, VERT]
        }]
      }
    },

    // Pourcentages -> barres sur une échelle 0 à 100
    reproduction: {
      title: 'Performance reproductive (%)',
      type: 'bar',
      unite: ' %',
      max: 100,
      data: {
        labels: ['Aptitude globale', 'Fertilité observée'],
        datasets: [{
          label: 'Taux',
          data: [num('aptitude'), num('fertilite')],
          backgroundColor: [OR, BLEU]
        }]
      }
    },

    // Évolution dans le temps -> courbes (12 mois)
    cheptel: {
      title: 'Évolution du cheptel (12 mois)',
      type: 'line',
      unite: '',
      data: {
        labels: labelsMois,
        datasets: [
          { label: 'Total porcs', data: porcsMois, borderColor: BLEU, backgroundColor: BLEU + '22', tension: 0.35 },
          { label: 'Lots actifs', data: lotsMois, borderColor: VERT, backgroundColor: VERT + '22', tension: 0.35 },
          { label: 'Groupes actifs', data: groupesMois, borderColor: OR, backgroundColor: OR + '22', tension: 0.35 }
        ]
      }
    }
  };

  let graphe = null;

  function render(type) {
    const cfg = tabs[type] || tabs.finances;
    titleEl.textContent = cfg.title;

    // On détruit l'ancien graphe avant d'en créer un d'un autre type
    if (graphe) {
      graphe.destroy();
    }

    graphe = new Chart(canvas, {
      type: cfg.type,
      data: cfg.data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          // Légende affichée seulement pour les courbes (plusieurs séries)
          legend: { display: cfg.type === 'line' },
          tooltip: {
            callbacks: {
              label: function (ctx) {
                const v = new Intl.NumberFormat('fr-FR').format(ctx.parsed.y);
                return ' ' + v + cfg.unite;
              }
            }
          }
        },
        scales: {
          y: { beginAtZero: true, max: cfg.max }
        }
      }
    });

    // Met en avant l'onglet sélectionné
    buttons.forEach(function (btn) {
      const actif = btn.dataset.statType === type;
      btn.classList.toggle('btn--primary', actif);
      btn.classList.toggle('btn--ghost', !actif);
    });
  }

  buttons.forEach(function (btn) {
    btn.addEventListener('click', function () {
      render(btn.dataset.statType);
    });
  });

  render('finances');
})();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>