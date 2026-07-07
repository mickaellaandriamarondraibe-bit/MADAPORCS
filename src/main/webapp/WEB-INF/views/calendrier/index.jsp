<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Calendrier" />
<c:set var="activeNav" value="calendrier" />
<c:set var="crumbs" value="MADAPORC / <b>Calendrier</b>" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div>
    <h1>Calendrier de l'exploitation</h1>
    <p>Mises bas, dépenses, ventes et pertes sur un seul calendrier</p>
  </div>
</div>

<%-- Boutons de catégorie : afficher / masquer chaque calendrier --%>
<div class="cal-filters">
  <button type="button" class="cal-btn is-active" data-type="misebas">Mises bas</button>
  <button type="button" class="cal-btn is-active" data-type="depense">Dépenses</button>
  <button type="button" class="cal-btn is-active" data-type="vente">Ventes</button>
  <button type="button" class="cal-btn is-active" data-type="mort">Morts</button>
</div>

<div class="card">
  <div class="card__body">
    <div id="calendar"></div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/locales-all.global.min.js"></script>

<script>
document.addEventListener('DOMContentLoaded', function () {
  const el = document.getElementById('calendar');
  const active = new Set(['misebas', 'depense', 'vente', 'mort']);
  let tous = [];

  const calendar = new FullCalendar.Calendar(el, {
    initialView: 'dayGridMonth',
    locale: 'fr',
    height: 'auto',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth,timeGridWeek,listWeek'
    }
  });
  calendar.render();

  function refresh() {
    calendar.removeAllEvents();
    calendar.addEventSource(tous.filter(function (e) { return active.has(e.type); }));
  }

  fetch('${ctx}/calendrier/events')
    .then(function (r) { return r.json(); })
    .then(function (data) { tous = data; refresh(); });

  document.querySelectorAll('.cal-btn').forEach(function (btn) {
    btn.addEventListener('click', function () {
      const t = btn.dataset.type;
      if (active.has(t)) { active.delete(t); btn.classList.remove('is-active'); }
      else { active.add(t); btn.classList.add('is-active'); }
      refresh();
    });
  });
});
</script>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
