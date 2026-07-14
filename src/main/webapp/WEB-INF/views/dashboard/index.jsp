<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="pageTitle" value="Tableau de bord" />
<c:set var="activeNav" value="dashboard" />
<c:set var="crumbs" value="MADAPORC / <b>Tableau de bord</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

<c:set var="d" value="${dashboard}" />
<c:set var="beneficeVal" value="${empty d.beneficeNet ? 0 : d.beneficeNet}" />
<c:set var="nbStocks" value="${fn:length(d.stocksFaibles)}" />
<c:set var="nbVaccins" value="${fn:length(d.vaccinationsAVenir)}" />

<div class="page-head">
  <div>
    <h1>Tableau de bord exploitation</h1>
    <p>Vue globale de l'élevage au ${dateJour}</p>
  </div>

  <form method="get" action="${ctx}/dashboard" class="flex gap-8" style="align-items:center">
    <label class="muted" style="font-size:13px">Mois</label>
    <input type="month" name="mois" value="${moisSelectionne}" class="input" style="width:auto"
           onchange="this.form.submit()">
    <button class="btn btn--ghost" type="submit">Voir</button>
    <a class="btn btn--ghost" href="${ctx}/dashboard/comparaison?mois1=${moisSelectionne}">Comparer</a>
    <a class="btn btn--primary" href="${ctx}/rapports">Générer un rapport</a>
  </form>
</div>

<%-- ================= KPI ================= --%>
<div class="kpi-grid">

  <div class="kpi kpi--green">
    <div class="kpi__top">
      <div class="kpi-icon"><i class="bi bi-box-seam"></i></div>
      <div class="kpi__label">Lots actifs</div>
    </div>
    <div class="kpi__value"><c:out value="${d.lotsActifs}" default="0" /></div>
    <div class="kpi__sub"><c:out value="${d.totalPorcs}" default="0" /> porcs actifs</div>
    <div class="kpi__foot"><span class="kpi__trend flat" id="trendLots">&rarr; stable</span></div>
  </div>

  <div class="kpi kpi--teal">
    <div class="kpi__top">
      <div class="kpi-icon"><i class="bi bi-diagram-3"></i></div>
      <div class="kpi__label">Groupes actifs</div>
    </div>
    <div class="kpi__value"><c:out value="${d.groupesActifs}" default="0" /></div>
    <div class="kpi__sub"><c:out value="${d.misesBasProches}" default="0" /> mise(s) bas proche(s)</div>
    <div class="kpi__foot"><span class="kpi__trend flat" id="trendGroupes">&rarr; stable</span></div>
  </div>

  <div class="kpi kpi--gold">
    <div class="kpi__top">
      <div class="kpi-icon"><i class="bi bi-heart-pulse"></i></div>
      <div class="kpi__label">Aptitude globale</div>
    </div>
    <div class="kpi__value"><c:out value="${d.tauxAptitudeGlobale}" default="0.00" />%</div>
    <div class="kpi__sub">Fertilité observée : <c:out value="${d.tauxFertiliteObserve}" default="0.00" />%</div>
    <div class="kpi__foot"><span class="kpi__trend warn" id="trendAptitude">À surveiller</span></div>
  </div>

  <div class="kpi kpi--blue">
    <div class="kpi__top">
      <div class="kpi-icon"><i class="bi bi-cash-coin"></i></div>
      <div class="kpi__label">Ventes du mois</div>
    </div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.ventesMois ? 0 : d.ventesMois}" type="number" maxFractionDigits="0" /> Ar</div>
    <div class="kpi__sub">Chiffre d'affaires validé</div>
    <div class="kpi__foot"><span class="kpi__trend info" id="trendVentes">0%</span></div>
  </div>

  <div class="kpi kpi--danger">
    <div class="kpi__top">
      <div class="kpi-icon"><i class="bi bi-wallet2"></i></div>
      <div class="kpi__label">Dépenses du mois</div>
    </div>
    <div class="kpi__value"><fmt:formatNumber value="${empty d.depensesMois ? 0 : d.depensesMois}" type="number" maxFractionDigits="0" /> Ar</div>
    <div class="kpi__sub">Sorties</div>
    <div class="kpi__foot"><span class="kpi__trend down" id="trendDepenses">&uarr; 0%</span></div>
  </div>

  <div class="kpi ${beneficeVal lt 0 ? 'kpi--danger' : 'kpi--green'}">
    <div class="kpi__top">
      <div class="kpi-icon"><i class="bi bi-graph-up-arrow"></i></div>
      <div class="kpi__label">Bénéfice net</div>
    </div>
    <div class="kpi__value ${beneficeVal lt 0 ? 'is-neg' : ''}">
      <fmt:formatNumber value="${beneficeVal}" type="number" maxFractionDigits="0" /> Ar
    </div>
    <div class="kpi__sub">Mois courant</div>
    <div class="kpi__foot">
      <span class="kpi__trend ${beneficeVal lt 0 ? 'down' : 'up'}">
        ${beneficeVal lt 0 ? 'Négatif' : 'Positif'}
      </span>
    </div>
  </div>

</div>

<%-- ================= Alertes prioritaires ================= --%>
<div class="section-title mt-24">
  <h2>Alertes prioritaires</h2>
  <a class="section-title__link" href="${ctx}/reproduction/alertes">
    Voir toutes les alertes <i class="bi bi-arrow-right"></i>
  </a>
</div>

<div class="alert-strip">

  <div class="alert-strip__item">
    <div class="alert-strip__icon ${d.misesBasProches > 0 ? 'is-warn' : 'is-ok'}"><i class="bi bi-calendar-heart"></i></div>
    <div class="alert-strip__body">
      <div class="alert-strip__title">Mises bas proches</div>
      <div class="alert-strip__value"><c:out value="${d.misesBasProches}" default="0" /></div>
      <div class="alert-strip__sub">Dans les 5 prochains jours</div>
    </div>
    <span class="badge-soft ${d.misesBasProches > 0 ? 'badge-soft-warning' : 'badge-soft-success'}">
      ${d.misesBasProches > 0 ? 'Proche' : 'RAS'}
    </span>
  </div>

  <div class="alert-strip__item">
    <div class="alert-strip__icon ${nbStocks > 0 ? 'is-danger' : 'is-ok'}"><i class="bi bi-box2"></i></div>
    <div class="alert-strip__body">
      <div class="alert-strip__title">Stocks faibles</div>
      <div class="alert-strip__value">${nbStocks}</div>
      <div class="alert-strip__sub">Ingrédients en faible stock</div>
    </div>
    <span class="badge-soft ${nbStocks > 0 ? 'badge-soft-danger' : 'badge-soft-success'}">
      ${nbStocks > 0 ? 'À traiter' : 'OK'}
    </span>
  </div>

  <div class="alert-strip__item">
    <div class="alert-strip__icon is-warn"><i class="bi bi-diagram-3"></i></div>
    <div class="alert-strip__body">
      <div class="alert-strip__title">Groupes à surveiller</div>
      <div class="alert-strip__value"><c:out value="${d.groupesActifs}" default="0" /></div>
      <div class="alert-strip__sub">Performance à contrôler</div>
    </div>
    <span class="badge-soft badge-soft-warning">À surveiller</span>
  </div>

  <div class="alert-strip__item">
    <div class="alert-strip__icon ${nbVaccins > 0 ? 'is-warn' : 'is-ok'}"><i class="bi bi-shield-plus"></i></div>
    <div class="alert-strip__body">
      <div class="alert-strip__title">Alertes sanitaires</div>
      <div class="alert-strip__value">${nbVaccins}</div>
      <div class="alert-strip__sub">${nbVaccins > 0 ? 'Rappels à planifier' : 'Aucune alerte active'}</div>
    </div>
    <span class="badge-soft ${nbVaccins > 0 ? 'badge-soft-warning' : 'badge-soft-success'}">
      ${nbVaccins > 0 ? 'Rappels' : 'Bas'}
    </span>
  </div>

</div>

<%-- ================= Indicateurs + Résumé financier ================= --%>
<div class="grid-2 mt-24">

  <div class="card chart-card">
    <div class="section-title section-title--inline">
      <h2>Indicateurs opérationnels</h2>
    </div>

    <div class="chart-tabs">
      <button type="button" class="chart-tab is-active js-stat-tab" data-stat-type="finances">Finances</button>
      <button type="button" class="chart-tab js-stat-tab" data-stat-type="reproduction">Reproduction</button>
      <button type="button" class="chart-tab js-stat-tab" data-stat-type="cheptel">Cheptel</button>
    </div>

    <div class="card__body">
      <div class="dashboard-chart"
           id="dashboardChart"
           data-ventes="${empty d.ventesMois ? 0 : d.ventesMois}"
           data-depenses="${empty d.depensesMois ? 0 : d.depensesMois}"
           data-benefice="${beneficeVal}"
           data-aptitude="${empty d.tauxAptitudeGlobale ? 0 : d.tauxAptitudeGlobale}"
           data-fertilite="${empty d.tauxFertiliteObserve ? 0 : d.tauxFertiliteObserve}"
           data-lots="${empty d.lotsActifs ? 0 : d.lotsActifs}"
           data-porcs="${empty d.totalPorcs ? 0 : d.totalPorcs}"
           data-groupes="${empty d.groupesActifs ? 0 : d.groupesActifs}">

        <div class="dashboard-chart__title" id="chartTitle">Pilotage financier mensuel (Ar)</div>

        <div class="dashboard-chart__plot">
          <canvas id="dashLineChart"
                  role="img"
                  aria-label="Graphique des indicateurs opérationnels de l'élevage">
            Indicateurs opérationnels mensuels.
          </canvas>
        </div>

        <div class="chart-legend" id="chartLegend">
          <span><i style="background:#1baf7a"></i>Ventes</span>
          <span><i style="background:#e34948"></i>Dépenses</span>
          <span><i style="background:#f3a3a3"></i>Bénéfice net</span>
        </div>

        <p class="dashboard-chart__note" id="chartNote" hidden>
          L'aptitude indique le potentiel reproductif global, tandis que la fertilité observée
          mesure les résultats réellement confirmés.
        </p>
      </div>
    </div>
  </div>

  <div class="card fin-card">
    <div class="card__head">
      <h2>Résumé financier</h2>
    </div>
    <div class="card__body">
      <div class="fin-row">
        <span class="fin-label">Ventes du mois</span>
        <span class="fin-value fin-value--blue">
          <fmt:formatNumber value="${empty d.ventesMois ? 0 : d.ventesMois}" type="number" maxFractionDigits="0" /> Ar
        </span>
      </div>
      <div class="fin-row">
        <span class="fin-label">Dépenses du mois</span>
        <span class="fin-value fin-value--red">
          <fmt:formatNumber value="${empty d.depensesMois ? 0 : d.depensesMois}" type="number" maxFractionDigits="0" /> Ar
        </span>
      </div>
      <div class="fin-row">
        <span class="fin-label">Bénéfice net</span>
        <span class="fin-value ${beneficeVal lt 0 ? 'fin-value--red' : 'fin-value--green'}">
          <fmt:formatNumber value="${beneficeVal}" type="number" maxFractionDigits="0" /> Ar
        </span>
      </div>
      <div class="fin-row fin-row--highlight">
        <span class="fin-label">Tendance</span>
        <span class="badge-soft ${beneficeVal lt 0 ? 'badge-soft-danger' : 'badge-soft-success'}">
          ${beneficeVal lt 0 ? 'Négative' : 'Positive'}
          <i class="bi ${beneficeVal lt 0 ? 'bi-graph-down-arrow' : 'bi-graph-up-arrow'}"></i>
        </span>
      </div>
    </div>
  </div>

</div>

<%-- ================= Tableaux du bas ================= --%>
<div class="grid-2 grid-2--even mt-24">

  <div class="card">
    <div class="card__head">
      <h2>Suivi des mises bas à venir</h2>
      <a class="btn btn--ghost btn--sm" href="${ctx}/reproduction/alertes">
        <i class="bi bi-bell"></i> Voir les alertes
      </a>
    </div>

    <div class="card__body" style="padding:0">
      <div class="table-wrap">
        <table class="tbl table-modern">
          <thead>
            <tr>
              <th>Groupe</th>
              <th>Lot femelle</th>
              <th>Date prévue</th>
              <th>Statut</th>
              <th class="num">Jours restants</th>
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
                      <span class="badge-soft badge-soft-warning">
                        <span class="dot"></span><c:out value="${groupe.statut}" />
                      </span>
                    </td>
                    <td class="num">
                      <span class="jours-restants" data-date="${groupe.datePrevueMiseBas}">&mdash;</span>
                    </td>
                  </tr>
                </c:forEach>
              </c:when>

              <c:otherwise>
                <tr>
                  <td colspan="5">
                    <div class="empty empty--sm">
                      <i class="bi bi-calendar-check ico"></i>
                      <p>Aucune mise bas proche.</p>
                    </div>
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
    <c:if test="${not empty misesBasProches}">
      <div class="tbl-foot">${fn:length(misesBasProches)} résultat(s)</div>
    </c:if>
  </div>

  <div class="card">
    <div class="card__head">
      <h2>Alertes stock alimentaire</h2>
      <a class="btn btn--ghost btn--sm" href="${ctx}/ingredients">
        <i class="bi bi-sliders"></i> Gérer
      </a>
    </div>

    <div class="card__body" style="padding:0">
      <div class="table-wrap">
        <table class="tbl table-modern">
          <thead>
            <tr>
              <th>Ingrédient</th>
              <th class="num">Stock actuel</th>
              <th class="num">Seuil min</th>
              <th>Statut</th>
            </tr>
          </thead>

          <tbody>
            <c:choose>
              <c:when test="${not empty d.stocksFaibles}">
                <c:forEach var="ingredient" items="${d.stocksFaibles}">
                  <tr>
                    <td><b><c:out value="${ingredient.nom}" /></b></td>
                    <td class="num">
                      <fmt:formatNumber value="${ingredient.stockActuel}" type="number" maxFractionDigits="2" />
                      <c:out value="${ingredient.unite}" />
                    </td>
                    <td class="num">
                      <fmt:formatNumber value="${ingredient.seuilAlerte}" type="number" maxFractionDigits="2" />
                      <c:out value="${ingredient.unite}" />
                    </td>
                    <td><span class="badge-soft badge-soft-danger">Bas</span></td>
                  </tr>
                </c:forEach>
              </c:when>

              <c:otherwise>
                <tr>
                  <td colspan="4">
                    <div class="empty empty--sm">
                      <i class="bi bi-check2-circle ico"></i>
                      <p>Tous les stocks sont corrects.</p>
                    </div>
                  </td>
                </tr>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
    <c:if test="${not empty d.stocksFaibles}">
      <div class="tbl-foot">${nbStocks} résultat(s)</div>
    </c:if>
  </div>

</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>

<script>
(function () {
  const chart = document.getElementById('dashboardChart');
  const buttons = document.querySelectorAll('.js-stat-tab');
  const titleEl = document.getElementById('chartTitle');
  const noteEl = document.getElementById('chartNote');
  const legendEl = document.getElementById('chartLegend');
  const canvas = document.getElementById('dashLineChart');

  if (!chart || !canvas) {
    return;
  }

  function num(name) {
    return Number(String(chart.dataset[name] || 0).replace(',', '.')) || 0;
  }

  // Étiquettes des mois (12 mois réels venant du serveur)
  const mois = [
    <c:forEach var="m" items="${d.moisLabels}" varStatus="s">'${m}'<c:if test="${not s.last}">,</c:if></c:forEach>
  ];
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
  const BLEU = '#2a78d6', ROUGE = '#e34948', VERT = '#1baf7a', OR = '#eda100', ROUGE_CLAIR = '#f3a3a3';

  const ventes = num('ventes');
  const depenses = num('depenses');
  const benefice = num('benefice');
  const couleurBenefice = benefice < 0 ? ROUGE : VERT;

  const tabs = {
    // Comparaison financière : chaque montant affiché tel quel. Les dépenses
    // sont un montant depensé (positif), pas une valeur négative.
    finances: {
      title: 'Pilotage financier mensuel (Ar)',
      type: 'bar',
      unite: ' Ar',
      showLegend: true,
      data: {
        labels: ['Ventes', 'Dépenses', 'Bénéfice net'],
        datasets: [{
          label: 'Montant',
          data: [ventes, depenses, benefice],
          backgroundColor: [VERT, ROUGE, benefice < 0 ? ROUGE_CLAIR : VERT]
        }]
      }
    },

    reproduction: {
      title: 'Performance reproductive (%)',
      type: 'bar',
      unite: ' %',
      max: 100,
      showNote: true,
      data: {
        labels: ['Aptitude globale', 'Fertilité observée'],
        datasets: [{
          label: 'Taux',
          data: [num('aptitude'), num('fertilite')],
          backgroundColor: [OR, BLEU]
        }]
      }
    },

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

    if (noteEl) { noteEl.hidden = !cfg.showNote; }
    if (legendEl) { legendEl.style.display = cfg.showLegend ? 'flex' : 'none'; }

    if (graphe) { graphe.destroy(); }

    graphe = new Chart(canvas, {
      type: cfg.type,
      data: cfg.data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: cfg.type === 'line' },
          tooltip: {
            callbacks: {
              label: function (ctx) {
                const v = new Intl.NumberFormat('fr-FR').format(Math.abs(ctx.parsed.y));
                return ' ' + v + cfg.unite;
              }
            }
          }
        },
        scales: { y: { beginAtZero: true, max: cfg.max } }
      }
    });

    buttons.forEach(function (btn) {
      btn.classList.toggle('is-active', btn.dataset.statType === type);
    });
  }

  buttons.forEach(function (btn) {
    btn.addEventListener('click', function () { render(btn.dataset.statType); });
  });

  render('finances');

  // ---- Pieds de KPI : tendances calculées sur les données réelles ----
  function delta(arr) {
    if (!arr || arr.length < 2) { return null; }
    return Number(arr[arr.length - 1]) - Number(arr[arr.length - 2]);
  }
  function setTrend(id, value, unit) {
    const el = document.getElementById(id);
    if (!el || value === null) { return; }
    el.classList.remove('up', 'down', 'flat');
    if (value > 0) { el.classList.add('up'); el.textContent = '↑ +' + value + unit; }
    else if (value < 0) { el.classList.add('down'); el.textContent = '↓ ' + value + unit; }
    else { el.classList.add('flat'); el.textContent = '→ stable'; }
  }
  setTrend('trendLots', delta(lotsMois), ' ce mois');
  setTrend('trendGroupes', delta(groupesMois), ' ce mois');

  // Part de trésorerie ventes / dépenses
  const totalFlux = ventes + depenses;
  const partVentes = totalFlux > 0 ? Math.round(ventes / totalFlux * 100) : 0;
  const partDepenses = totalFlux > 0 ? Math.round(depenses / totalFlux * 100) : 0;
  const tv = document.getElementById('trendVentes');
  if (tv) { tv.textContent = partVentes + '% des flux'; }
  const td = document.getElementById('trendDepenses');
  if (td) { td.textContent = '↑ ' + partDepenses + '% des flux'; }

  // Aptitude : seuil sur la fertilité observée
  const fertilite = num('fertilite');
  const ta = document.getElementById('trendAptitude');
  if (ta) {
    if (fertilite >= 60) { ta.classList.remove('warn'); ta.classList.add('up'); ta.textContent = 'Bon'; }
    else { ta.textContent = 'À surveiller'; }
  }

  // ---- Jours restants avant mise bas ----
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  document.querySelectorAll('.jours-restants').forEach(function (el) {
    const raw = el.dataset.date;
    if (!raw) { return; }
    const d = new Date(raw + 'T00:00:00');
    if (isNaN(d.getTime())) { return; }
    const diff = Math.round((d - today) / 86400000);
    if (diff < 0) { el.textContent = 'En retard'; el.classList.add('is-late'); }
    else if (diff === 0) { el.textContent = "Aujourd'hui"; el.classList.add('is-soon'); }
    else { el.textContent = diff + (diff > 1 ? ' jours' : ' jour'); if (diff <= 7) { el.classList.add('is-soon'); } }
  });
})();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
