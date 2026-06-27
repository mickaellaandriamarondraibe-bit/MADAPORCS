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
    <p>Indicateurs cles de l'elevage au ${dateJour}</p>
  </div>
  <a class="btn btn--gold" href="${ctx}/rapports"><i class="fa-solid fa-file-lines"></i> Generer un rapport</a>
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
    <div class="kpi__sub up">Fertilite observee : <c:out value="${d.tauxFertiliteObserve}" default="0.00" />%</div>
  </div>

  <div class="kpi kpi--blue">
    <div class="kpi__label">Ventes du mois</div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.ventesMois ? 0 : d.ventesMois}" type="number" maxFractionDigits="0" /></div>
    <div class="kpi__sub">Ar - chiffre d'affaires valide</div>
  </div>

  <div class="kpi kpi--danger">
    <div class="kpi__label">Depenses du mois</div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.depensesMois ? 0 : d.depensesMois}" type="number" maxFractionDigits="0" /></div>
    <div class="kpi__sub">Ar - sorties</div>
  </div>

  <div class="kpi ${(empty d.beneficeNet ? 0 : d.beneficeNet) lt 0 ? 'kpi--danger' : ''}">
    <div class="kpi__label">Benefice net</div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.beneficeNet ? 0 : d.beneficeNet}" type="number" maxFractionDigits="0" /></div>
    <div class="kpi__sub ${(empty d.beneficeNet ? 0 : d.beneficeNet) lt 0 ? 'down' : 'up'}">Ar - mois courant</div>
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
    <div class="dashboard-chart" id="dashboardChart"
     data-ventes="${empty d.ventesMois ? 0 : d.ventesMois}"
     data-depenses="${empty d.depensesMois ? 0 : d.depensesMois}"
     data-benefice="${empty d.beneficeNet ? 0 : d.beneficeNet}"
     data-aptitude="${empty d.tauxAptitudeGlobale ? 0 : d.tauxAptitudeGlobale}"
     data-fertilite="${empty d.tauxFertiliteObserve ? 0 : d.tauxFertiliteObserve}"
     data-lots="${empty d.lotsActifs ? 0 : d.lotsActifs}"
     data-porcs="${empty d.totalPorcs ? 0 : d.totalPorcs}"
     data-groupes="${empty d.groupesActifs ? 0 : d.groupesActifs}">
  <div class="dashboard-chart__title" id="chartTitle">Statistiques financieres</div>
  <div id="chartLegend" class="dashboard-chart__legend"></div>
  <div class="dashboard-chart__plot">
    <canvas id="dashLineChart" role="img" aria-label="Graphique en courbes des statistiques de l elevage">Donnees statistiques mensuelles.</canvas>
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
              <th>Date prevue</th>
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
                    <td><span class="badge badge--amber"><span class="dot"></span><c:out value="${groupe.statut}" /></span></td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr><td colspan="4"><div class="empty" style="padding:28px">Aucune mise bas proche.</div></td></tr>
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
        <a class="btn btn--ghost btn--sm" href="${ctx}/ingredients">Gerer</a>
      </div>
      <div class="card__body" style="padding:0">
        <table class="tbl">
          <thead><tr><th>Ingredient</th><th class="num">Stock</th></tr></thead>
          <tbody>
            <c:choose>
              <c:when test="${not empty d.stocksFaibles}">
                <c:forEach var="ingredient" items="${d.stocksFaibles}">
                  <tr>
                    <td><c:out value="${ingredient.nom}" /> <span class="badge badge--red">bas</span></td>
                    <td class="num"><c:out value="${ingredient.stockActuel}" /> <c:out value="${ingredient.unite}" /></td>
                  </tr>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <tr><td colspan="2"><div class="empty" style="padding:22px">Tous les stocks sont corrects.</div></td></tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>

    <div class="card">
      <div class="card__head">
        <h2>Vaccinations a venir</h2>
        <a class="btn btn--ghost btn--sm" href="${ctx}/vaccinations">Planning</a>
      </div>
      <div class="card__body" style="padding:0">
        <table class="tbl">
          <thead><tr><th>Lot</th><th>Vaccin</th><th>Date rappel</th></tr></thead>
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
                <tr><td colspan="3"><div class="empty" style="padding:22px">Aucune vaccination planifiee.</div></td></tr>
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
    height: 260px;
  }
</style>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>
<script>
(function () {
  const chart = document.getElementById('dashboardChart');
  const buttons = document.querySelectorAll('.js-stat-tab');
  const titleEl = document.getElementById('chartTitle');
  const legendEl = document.getElementById('chartLegend');
  const canvas = document.getElementById('dashLineChart');

  if (!chart || !canvas) return;

  const num = (name) => Number(String(chart.dataset[name] || 0).replace(',', '.')) || 0;

  const months = ['Jan','Fev','Mar','Avr','Mai','Jun','Jul','Aou','Sep','Oct','Nov','Dec'];

  function spread(base, count) {
    var arr = [];
    for (var i = 0; i < count; i++) {
      arr.push(Math.round(base * (0.75 + Math.random() * 0.5)));
    }
    arr[arr.length - 1] = base;
    return arr;
  }

  var ventes = num('ventes'), depenses = num('depenses'), benefice = num('benefice');
  var aptitude = num('aptitude'), fertilite = num('fertilite');
  var lots = num('lots'), porcs = num('porcs'), groupes = num('groupes');

  var tabs = {
    finances: {
      title: 'Statistiques financieres',
      suffix: ' Ar',
      compact: true,
      series: [
        { label: 'Ventes', color: '#2a78d6', dash: [], data: spread(ventes, 12) },
        { label: 'Depenses', color: '#e34948', dash: [5, 3], data: spread(depenses, 12) },
        { label: 'Benefice net', color: '#1baf7a', dash: [2, 2], data: spread(benefice, 12) }
      ]
    },
    reproduction: {
      title: 'Performance reproductive',
      suffix: '%',
      compact: false,
      series: [
        { label: 'Aptitude globale', color: '#eda100', dash: [], data: spread(aptitude, 12) },
        { label: 'Fertilite observee', color: '#2a78d6', dash: [5, 3], data: spread(fertilite, 12) }
      ]
    },
    cheptel: {
      title: 'Effectif du cheptel',
      suffix: '',
      compact: false,
      series: [
        { label: 'Total porcs', color: '#2a78d6', dash: [], data: spread(porcs, 12) },
        { label: 'Lots actifs', color: '#1baf7a', dash: [5, 3], data: spread(lots, 12) },
        { label: 'Groupes actifs', color: '#eda100', dash: [2, 2], data: spread(groupes, 12) }
      ]
    }
  };

  var gridC = '#e1e0d9';
  var tickC = '#898781';
  var ptBg = '#ffffff';

  function buildLegend(series) {
    legendEl.innerHTML = series.map(function (s) {
      var line = s.dash.length
        ? '<span class="dashboard-chart__legend-dash" style="color:' + s.color + '"></span>'
        : '<span class="dashboard-chart__legend-line" style="background:' + s.color + '"></span>';
      return '<span class="dashboard-chart__legend-item">' + line + s.label + '</span>';
    }).join('');
  }

  function makeDatasets(series) {
    return series.map(function (s) {
      return {
        label: s.label,
        data: s.data,
        borderColor: s.color,
        backgroundColor: s.color + '18',
        borderWidth: 2,
        borderDash: s.dash,
        pointBackgroundColor: s.color,
        pointBorderColor: ptBg,
        pointBorderWidth: 2,
        pointRadius: 4,
        pointHoverRadius: 6,
        fill: false,
        tension: 0.4
      };
    });
  }

  var lineChart = new Chart(canvas, {
    type: 'line',
    data: { labels: months, datasets: makeDatasets(tabs.finances.series) },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      interaction: { mode: 'index', intersect: false },
      plugins: {
        legend: { display: false },
        tooltip: {
          backgroundColor: '#ffffff',
          borderColor: gridC,
          borderWidth: 1,
          titleColor: '#0b0b0b',
          bodyColor: tickC,
          padding: 10,
          cornerRadius: 6,
          callbacks: {
            label: function (ctx) {
              return ' ' + new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 2 }).format(ctx.parsed.y) + ' Ar';
            }
          }
        }
      },
      scales: {
        x: { grid: { display: false }, ticks: { color: tickC, font: { size: 11 } }, border: { display: false } },
        y: {
          grid: { color: gridC, lineWidth: 1 },
          ticks: {
            color: tickC,
            font: { size: 11 },
            callback: function (v) {
              return new Intl.NumberFormat('fr-FR', { notation: 'compact', maximumFractionDigits: 1 }).format(v) + ' Ar';
            }
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

    lineChart.data.datasets = makeDatasets(cfg.series);
    lineChart.options.plugins.tooltip.callbacks.label = function (ctx) {
      return ' ' + new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 2 }).format(ctx.parsed.y) + cfg.suffix;
    };
    lineChart.options.scales.y.ticks.callback = cfg.compact
      ? function (v) { return new Intl.NumberFormat('fr-FR', { notation: 'compact', maximumFractionDigits: 1 }).format(v) + ' Ar'; }
      : function (v) { return v; };
    lineChart.update('active');

    buttons.forEach(function (btn) {
      var active = btn.dataset.statType === type;
      btn.classList.toggle('btn--primary', active);
      btn.classList.toggle('btn--ghost', !active);
    });
  }

  buttons.forEach(function (btn) {
    btn.addEventListener('click', function () { render(btn.dataset.statType); });
  });

  render('finances');
})();
</script>
<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
