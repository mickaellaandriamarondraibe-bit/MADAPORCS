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
      <div class="dashboard-chart__plot" id="chartBars"></div>
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
    margin-bottom: 14px;
  }

  .dashboard-chart__plot {
    display: grid;
    grid-template-columns: repeat(3, minmax(90px, 1fr));
    gap: 18px;
    min-height: 260px;
    align-items: end;
    padding: 18px 10px 4px;
    border-left: 1px solid var(--gris-200);
    border-bottom: 1px solid var(--gris-200);
  }

  .chart-bar {
    display: grid;
    grid-template-rows: 24px 1fr auto;
    gap: 8px;
    height: 220px;
    min-width: 0;
    text-align: center;
  }

  .chart-bar__value {
    color: var(--gris-700);
    font-size: 12px;
    font-weight: 800;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .chart-bar__track {
    display: flex;
    align-items: end;
    justify-content: center;
    min-height: 150px;
  }

  .chart-bar__fill {
    width: min(72px, 70%);
    min-height: 8px;
    border-radius: 7px 7px 0 0;
    background: var(--vert-500);
    transition: height .25s ease;
  }

  .chart-bar__fill.is-blue { background: var(--bleu-600); }
  .chart-bar__fill.is-gold { background: var(--or-500); }
  .chart-bar__fill.is-red { background: var(--rouge-600); }

  .chart-bar__label {
    color: var(--gris-500);
    font-size: 12px;
    font-weight: 700;
    min-height: 32px;
  }

  @media (max-width: 700px) {
    .dashboard-chart__plot {
      gap: 10px;
      grid-template-columns: repeat(3, minmax(70px, 1fr));
    }

    .chart-bar__fill {
      width: min(48px, 80%);
    }
  }
</style>

<script>
  (function () {
    const chart = document.getElementById('dashboardChart');
    const bars = document.getElementById('chartBars');
    const title = document.getElementById('chartTitle');
    const buttons = document.querySelectorAll('.js-stat-tab');

    if (!chart || !bars || !title) {
      return;
    }

    const numberValue = (name) => Number(String(chart.dataset[name] || 0).replace(',', '.')) || 0;

    const datasets = {
      finances: {
        title: 'Statistiques financieres',
        suffix: ' Ar',
        values: [
          { label: 'Ventes du mois', value: numberValue('ventes'), color: 'is-blue' },
          { label: 'Depenses du mois', value: numberValue('depenses'), color: 'is-red' },
          { label: 'Benefice net', value: numberValue('benefice'), color: numberValue('benefice') < 0 ? 'is-red' : 'is-gold' }
        ]
      },
      reproduction: {
        title: 'Performance reproductive',
        suffix: '%',
        values: [
          { label: 'Aptitude globale', value: numberValue('aptitude'), color: 'is-gold' },
          { label: 'Fertilite observee', value: numberValue('fertilite'), color: 'is-blue' }
        ]
      },
      cheptel: {
        title: 'Effectif du cheptel',
        suffix: '',
        values: [
          { label: 'Lots actifs', value: numberValue('lots'), color: 'is-green' },
          { label: 'Total porcs', value: numberValue('porcs'), color: 'is-blue' },
          { label: 'Groupes actifs', value: numberValue('groupes'), color: 'is-gold' }
        ]
      }
    };

    function formatValue(value, suffix) {
      return new Intl.NumberFormat('fr-FR', { maximumFractionDigits: 2 }).format(value) + suffix;
    }

    function render(type) {
      const dataset = datasets[type] || datasets.finances;
      const max = Math.max(...dataset.values.map((item) => Math.abs(item.value)), 1);
      title.textContent = dataset.title;
      bars.innerHTML = dataset.values.map((item) => {
        const height = Math.max((Math.abs(item.value) / max) * 100, item.value === 0 ? 0 : 6);
        return ''
          + '<div class="chart-bar">'
          + '<div class="chart-bar__value">' + formatValue(item.value, dataset.suffix) + '</div>'
          + '<div class="chart-bar__track">'
          + '<div class="chart-bar__fill ' + item.color + '" style="height:' + height + '%"></div>'
          + '</div>'
          + '<div class="chart-bar__label">' + item.label + '</div>'
          + '</div>';
      }).join('');

      buttons.forEach((button) => {
        const active = button.dataset.statType === type;
        button.classList.toggle('btn--primary', active);
        button.classList.toggle('btn--ghost', !active);
      });
    }

    buttons.forEach((button) => {
      button.addEventListener('click', () => render(button.dataset.statType));
    });

    render('finances');
  })();
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
