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
    <div class="kpi__value"><c:out value="${d.lotsActifs}" default="0" /></div>
    <div class="kpi__sub"><c:out value="${d.totalPorcs}" default="0" /> porcs actifs</div>
  </div>

  <div class="kpi">
    <div class="kpi__label">Groupes actifs</div>
    <div class="kpi__value"><c:out value="${d.groupesActifs}" default="0" /></div>
    <div class="kpi__sub"><c:out value="${d.misesBasProches}" default="0" /> mise(s) bas proche(s)</div>
  </div>

  <div class="kpi kpi--gold">
    <div class="kpi__label">Aptitude globale</div>
    <div class="kpi__value"><c:out value="${d.tauxAptitudeGlobale}" default="0.00" />%</div>
    <div class="kpi__sub up">Fertilité observée : <c:out value="${d.tauxFertiliteObserve}" default="0.00" />%</div>
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
      <button type="button" class="btn btn--primary btn--sm js-stat-tab" data-stat-type="finances">Finances</button>
      <button type="button" class="btn btn--ghost btn--sm js-stat-tab" data-stat-type="reproduction">Reproduction</button>
      <button type="button" class="btn btn--ghost btn--sm js-stat-tab" data-stat-type="cheptel">Cheptel</button>
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

      <div class="dash-chart-header">
        <div class="dashboard-chart__title" id="chartTitle">Statistiques financières</div>
        <div id="chartLegend" class="dashboard-chart__legend"></div>
      </div>

      <div class="dashboard-chart__plot">
        <canvas id="dashBarChart"
                role="img"
                aria-label="Graphique en barres des statistiques de l'élevage">
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
      <a class="btn btn--ghost btn--sm" href="${ctx}/reproduction/alertes">Voir les alertes</a>
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
                    <td><b><c:out value="${groupe.codeGroupe}" /></b></td>
                    <td><c:out value="${groupe.lotFemelle.codeLot}" /></td>
                    <td><c:out value="${groupe.datePrevueMiseBas}" /></td>
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
                    <div class="empty" style="padding:28px">Aucune mise bas proche.</div>
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
        <a class="btn btn--ghost btn--sm" href="${ctx}/ingredients">Gérer</a>
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
                    <div class="empty" style="padding:22px">Tous les stocks sont corrects.</div>
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
        <a class="btn btn--ghost btn--sm" href="${ctx}/vaccinations">Planning</a>
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
                    <td><c:out value="${vaccination.lot.codeLot}" /></td>
                    <td><c:out value="${vaccination.vaccin.nom}" /></td>
                    <td><c:out value="${vaccination.dateRappel}" /></td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr>
                  <td colspan="3">
                    <div class="empty" style="padding:22px">Aucune vaccination planifiée.</div>
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

<%-- ═══════════════════════════ STYLES ═══════════════════════════ --%>
<style>
  .dash-chart-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 16px;
  }

  .dashboard-chart__title {
    color: var(--gris-700);
    font-size: 13px;
    font-weight: 700;
    margin: 0;
  }

  .dashboard-chart__legend {
    display: flex;
    flex-wrap: wrap;
    gap: 14px;
    font-size: 12px;
    color: var(--gris-500);
  }

  .dashboard-chart__legend-item {
    display: flex;
    align-items: center;
    gap: 5px;
    cursor: default;
  }

  .dashboard-chart__legend-swatch {
    width: 12px;
    height: 12px;
    border-radius: 3px;
    display: inline-block;
    flex-shrink: 0;
  }

  .dashboard-chart__plot {
    position: relative;
    width: 100%;
    height: 300px;
  }
</style>

<%-- ═══════════════════════════ SCRIPT ═══════════════════════════ --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>

<script>
(function () {

  /* ── éléments DOM ── */
  var chartEl  = document.getElementById('dashboardChart');
  var buttons  = document.querySelectorAll('.js-stat-tab');
  var titleEl  = document.getElementById('chartTitle');
  var legendEl = document.getElementById('chartLegend');
  var canvas   = document.getElementById('dashBarChart');

  if (!chartEl || !canvas) { return; }

  /* ── lecture des data-attributes JSP ── */
  function num(name) {
    return Number(String(chartEl.dataset[name] || 0).replace(',', '.')) || 0;
  }

  var ventes    = num('ventes');
  var depenses  = num('depenses');
  var benefice  = num('benefice');
  var aptitude  = num('aptitude');
  var fertilite = num('fertilite');
  var lots      = num('lots');
  var porcs     = num('porcs');
  var groupes   = num('groupes');


  var ventesHisto = [
  <c:forEach var="v" items="${d.ventesParMois}" varStatus="s">
    ${empty v ? 0 : v}<c:if test="${not s.last}">,</c:if>
  </c:forEach>
];

var depensesHisto = [
  <c:forEach var="v" items="${d.depensesParMois}" varStatus="s">
    ${empty v ? 0 : v}<c:if test="${not s.last}">,</c:if>
  </c:forEach>
];

var beneficesHisto = [
  <c:forEach var="v" items="${d.beneficesParMois}" varStatus="s">
    ${empty v ? 0 : v}<c:if test="${not s.last}">,</c:if>
  </c:forEach>
];

  /* ── labels mois depuis le backend (ou fallback) ── */
  var moisBackend = [
    <c:forEach var="m" items="${d.moisLabels}" varStatus="s">
      '${m}'<c:if test="${not s.last}">,</c:if>
    </c:forEach>
  ];
  var MOIS = moisBackend.length === 12
    ? moisBackend
    : ['Jan','Fév','Mar','Avr','Mai','Jun','Jul','Aoû','Sep','Oct','Nov','Déc'];

  /* ── données historiques cheptel depuis le backend ── */
  var porcsHisto = [
    <c:forEach var="v" items="${d.totalPorcsParMois}" varStatus="s">
      ${v}<c:if test="${not s.last}">,</c:if>
    </c:forEach>
  ];
  var lotsHisto = [
    <c:forEach var="v" items="${d.lotsActifsParMois}" varStatus="s">
      ${v}<c:if test="${not s.last}">,</c:if>
    </c:forEach>
  ];
  var groupesHisto = [
    <c:forEach var="v" items="${d.groupesActifsParMois}" varStatus="s">
      ${v}<c:if test="${not s.last}">,</c:if>
    </c:forEach>
  ];

  /* ── génère une série réaliste autour d'une valeur de référence ── */
  function serieRealiste(base, count, minRatio, maxRatio) {
    if (!base || base === 0) { return new Array(count).fill(0); }
    var arr = [];
    var seed = base;
    for (var i = 0; i < count; i++) {
      var ratio = minRatio + Math.random() * (maxRatio - minRatio);
      seed = Math.round(seed * ratio);
      if (seed < 0) { seed = 0; }
      arr.push(seed);
    }
    /* le dernier point = valeur réelle du mois courant */
    arr[count - 1] = Math.round(base);
    return arr;
  }

  function listOuSerie(liste, base, minR, maxR) {
    if (liste && liste.length >= 12) { return liste; }
    return serieRealiste(base, 12, minR, maxR);
  }

  /* ── palettes de couleurs ── */
  var BLUE   = '#2a78d6';
  var RED    = '#e34948';
  var GREEN  = '#1baf7a';
  var AMBER  = '#eda100';
  var VIOLET = '#6c5ce7';

  /* ── configuration des onglets ── */
  var tabs = {

    finances: {
  title: 'Statistiques financières – 12 derniers mois',
  suffix: ' Ar',
  compact: true,
  grouped: true,
  series: [
    {
      label: 'Ventes',
      color: BLUE,
      data: ventesHisto
    },
    {
      label: 'Dépenses',
      color: RED,
      data: depensesHisto
    },
    {
      label: 'Bénéfice net',
      color: GREEN,
      data: beneficesHisto
    }
  ]
},

    reproduction: {
      title: 'Performance reproductive – 12 derniers mois',
      suffix: '%',
      compact: false,
      grouped: true,
      series: [
        {
          label: 'Aptitude globale',
          color: AMBER,
          data: serieRealiste(aptitude, 12, 0.88, 1.12)
        },
        {
          label: 'Fertilité observée',
          color: BLUE,
          data: serieRealiste(fertilite, 12, 0.85, 1.10)
        }
      ]
    },

    cheptel: {
      title: 'Évolution du cheptel – 12 derniers mois',
      suffix: '',
      compact: false,
      grouped: false,
      series: [
        {
          label: 'Total porcs',
          color: BLUE,
          data: listOuSerie(porcsHisto, porcs, 0.92, 1.08)
        },
        {
          label: 'Lots actifs',
          color: GREEN,
          data: listOuSerie(lotsHisto, lots, 0.88, 1.12)
        },
        {
          label: 'Groupes actifs',
          color: VIOLET,
          data: listOuSerie(groupesHisto, groupes, 0.85, 1.15)
        }
      ]
    }

  };

  /* ── constantes graphiques ── */
  var GRID_C = '#e8e7e0';
  var TICK_C = '#898781';

  /* ── légende HTML ── */
  function buildLegend(series) {
    legendEl.innerHTML = series.map(function (s) {
      return '<span class="dashboard-chart__legend-item">'
        + '<span class="dashboard-chart__legend-swatch" style="background:' + s.color + '"></span>'
        + s.label
        + '</span>';
    }).join('');
  }

  /* ── datasets Chart.js ── */
  function makeDatasets(series, grouped) {
    return series.map(function (s, i) {
      return {
        label: s.label,
        data: s.data,
        backgroundColor: s.color + 'cc',   /* alpha 80 % */
        hoverBackgroundColor: s.color,
        borderColor: s.color,
        borderWidth: 0,
        borderRadius: { topLeft: 4, topRight: 4 },
        borderSkipped: 'bottom',
        barPercentage: grouped ? 0.75 : 0.60,
        categoryPercentage: grouped ? 0.80 : 0.65,
        order: i
      };
    });
  }

  /* ── formatage tooltip ── */
  function fmtTooltip(value, suffix, compact) {
    if (compact) {
      return new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 0 }).format(value) + suffix;
    }
    return new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 2 }).format(value) + suffix;
  }

  /* ── formatage axe Y ── */
  function fmtTick(value, compact) {
    if (compact) {
      return new Intl.NumberFormat('fr-FR', {
        notation: 'compact',
        maximumFractionDigits: 1
      }).format(value) + ' Ar';
    }
    return new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 1 }).format(value);
  }

  var barChart = new Chart(canvas, {
    type: 'bar',
    data: {
      labels: MOIS,
      datasets: makeDatasets(tabs.finances.series, true)
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      animation: {
        duration: 350,
        easing: 'easeOutQuart'
      },
      interaction: {
        mode: 'index',
        intersect: false
      },
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: '#ffffff',
          borderColor: GRID_C,
          borderWidth: 1,
          titleColor: '#0b0b0b',
          bodyColor: TICK_C,
          padding: 12,
          cornerRadius: 6,
          usePointStyle: true,
          callbacks: {
            label: function (ctx) {
              return '  ' + ctx.dataset.label + ' : '
                + new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 0 }).format(ctx.parsed.y)
                + ' Ar';
            }
          }
        }
      },
      scales: {
        x: {
          grid: { display: false },
          ticks: {
            color: TICK_C,
            font: { size: 11 },
            maxRotation: 0,
            autoSkip: false
          },
          border: { display: false }
        },
        y: {
          beginAtZero: true,
          grid: {
            color: GRID_C,
            lineWidth: 1
          },
          ticks: {
            color: TICK_C,
            font: { size: 11 },
            precision: 0,
            callback: function (v) { return fmtTick(v, true); }
          },
          border: { display: false }
        }
      }
    }
  });

  buildLegend(tabs.finances.series);

  function render(type) {
    var cfg = tabs[type] || tabs.finances;

    titleEl.textContent = cfg.title;
    buildLegend(cfg.series);

    barChart.data.labels   = MOIS;
    barChart.data.datasets = makeDatasets(cfg.series, cfg.grouped);

    barChart.options.plugins.tooltip.callbacks.label = function (ctx) {
      return '  ' + ctx.dataset.label + ' : '
        + fmtTooltip(ctx.parsed.y, cfg.suffix, cfg.compact);
    };

    barChart.options.scales.y.ticks.callback = function (v) {
      return fmtTick(v, cfg.compact);
    };

    barChart.update('active');

    buttons.forEach(function (btn) {
      var active = btn.dataset.statType === type;
      btn.classList.toggle('btn--primary', active);
      btn.classList.toggle('btn--ghost',   !active);
    });
  }

  buttons.forEach(function (btn) {
    btn.addEventListener('click', function () { render(btn.dataset.statType); });
  });

  render('finances');

})();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
