/* =============================================================
   MADAPORC — JS partagé (app.js)
   Chargé par layout/footer.jsp
   ============================================================= */
(function () {
  "use strict";

  /* ---- Menu mobile : ouverture/fermeture sidebar ---- */
  function setupMenu() {
    var toggle = document.querySelector("[data-menu-toggle]");
    var sidebar = document.querySelector(".sidebar");
    var backdrop = document.querySelector(".sidebar-backdrop");
    if (!toggle || !sidebar) return;

    function open() { sidebar.classList.add("is-open"); if (backdrop) backdrop.classList.add("is-open"); }
    function close() { sidebar.classList.remove("is-open"); if (backdrop) backdrop.classList.remove("is-open"); }

    toggle.addEventListener("click", function () {
      sidebar.classList.contains("is-open") ? close() : open();
    });
    if (backdrop) backdrop.addEventListener("click", close);
    document.addEventListener("keydown", function (e) { if (e.key === "Escape") close(); });
  }

  /* ---- Marque le lien de navigation actif selon l'URL ---- */
  function setupActiveNav() {
    var path = window.location.pathname.replace(/\/$/, "") || "/";
    var links = document.querySelectorAll(".nav__link[data-match]");
    var best = null, bestLen = -1;
    links.forEach(function (a) {
      var m = a.getAttribute("data-match");
      if (m && (path === m || path.indexOf(m + "/") === 0) && m.length > bestLen) {
        best = a; bestLen = m.length;
      }
    });
    if (best) {
      links.forEach(function (a) { a.classList.remove("is-active"); });
      best.classList.add("is-active");
    }
  }

  /* ---- Menu lateral repliable (accordeon) ---- */
  function setupNavGroups() {
    var groups = document.querySelectorAll(".nav__group");
    groups.forEach(function (group) {
      var title = group.querySelector("[data-nav-group]");
      if (!title) return;
      title.addEventListener("click", function () {
        group.classList.toggle("is-open");
      });
      // Ouvre automatiquement le groupe contenant la page courante.
      if (group.querySelector(".nav__link.is-active")) {
        group.classList.add("is-open");
      }
    });
  }

  /* ---- Confirmation avant action destructrice (data-confirm) ---- */
  function setupConfirms() {
    document.addEventListener("submit", function (e) {
      var form = e.target;
      var msg = form.getAttribute("data-confirm");
      if (msg && !window.confirm(msg)) e.preventDefault();
    });
    document.querySelectorAll("a[data-confirm]").forEach(function (a) {
      a.addEventListener("click", function (e) {
        if (!window.confirm(a.getAttribute("data-confirm"))) e.preventDefault();
      });
    });
  }

  /* ---- Filtre de tableau côté client (data-filter-input -> data-filter-table) ---- */
  function setupTableFilter() {
    document.querySelectorAll("[data-filter-input]").forEach(function (input) {
      var sel = input.getAttribute("data-filter-input");
      var table = document.querySelector(sel);
      if (!table) return;
      function apply() {
        var q = input.value.toLowerCase().trim();
        table.querySelectorAll("tbody tr").forEach(function (tr) {
          tr.style.display = tr.textContent.toLowerCase().indexOf(q) > -1 ? "" : "none";
        });
      }
      input.addEventListener("input", apply);
      // Bouton "Rechercher" optionnel, dans la même barre d'outils.
      var container = input.closest(".toolbar");
      var btn = container ? container.querySelector("[data-filter-btn]") : null;
      if (btn) {
        btn.addEventListener("click", function (e) { e.preventDefault(); apply(); });
      }
    });
  }

  /* ---- Calcul automatique de total (ventes/détails) ----
     Inputs avec [data-qty]/[data-price] et sortie [data-total] */
  function setupAutoTotal() {
    var out = document.querySelector("[data-total-out]");
    if (!out) return;
    function recompute() {
      var total = 0;
      document.querySelectorAll("[data-line]").forEach(function (line) {
        var q = parseFloat(line.querySelector("[data-qty]")?.value || "0");
        var p = parseFloat(line.querySelector("[data-price]")?.value || "0");
        var lt = (isFinite(q) ? q : 0) * (isFinite(p) ? p : 0);
        var cell = line.querySelector("[data-line-total]");
        if (cell) cell.textContent = lt.toLocaleString("fr-FR");
        total += lt;
      });
      out.textContent = total.toLocaleString("fr-FR");
    }
    document.addEventListener("input", function (e) {
      if (e.target.matches("[data-qty],[data-price]")) recompute();
    });
    recompute();
  }

  /* ---- Validation mise bas : vivants + morts <= nés ---- */
  function setupMiseBasCheck() {
    var nes = document.querySelector("[data-nes]");
    var viv = document.querySelector("[data-vivants]");
    var mor = document.querySelector("[data-morts]");
    var warn = document.querySelector("[data-mb-warn]");
    if (!nes || !viv || !mor) return;
    function check() {
      var n = +nes.value || 0, v = +viv.value || 0, m = +mor.value || 0;
      var bad = (v + m) > n;
      if (warn) warn.style.display = bad ? "flex" : "none";
      var submit = document.querySelector("[data-mb-submit]");
      if (submit) submit.disabled = bad;
    }
    [nes, viv, mor].forEach(function (el) { el.addEventListener("input", check); });
    check();
  }

  /* ---- Notifications temps réel par SSE sur toutes les pages ---- */
  function setupRealtimeNotifications() {
    if (!window.EventSource) return;

    var base = window.MADAPORC_CTX || "";
    var streamUrl = base + "/notifications/stream";
    var container = document.querySelector("[data-notification-stack]");

    if (!container) {
      container = document.createElement("div");
      container.setAttribute("data-notification-stack", "true");
      container.className = "notification-stack";
      document.body.appendChild(container);
    }

    function showToast(message) {
      var toast = document.createElement("div");
      toast.className = "notification-toast";
      toast.innerHTML = '<i class="fa-solid fa-bell"></i><span>' + message + '</span>';
      container.appendChild(toast);

      window.setTimeout(function () {
        toast.classList.add("is-visible");
      }, 20);

      window.setTimeout(function () {
        toast.classList.remove("is-visible");
        window.setTimeout(function () {
          if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
          }
        }, 250);
      }, 5000);
    }

    try {
      var source = new EventSource(streamUrl);
      source.addEventListener("notification", function (event) {
        if (event && event.data) {
          showToast(event.data);
        }
      });
    } catch (error) {
      // Pas de fallback silencieux.
    }
  }

  document.addEventListener("DOMContentLoaded", function () {
    setupMenu();
    setupActiveNav();
    setupNavGroups();
    setupConfirms();
    setupTableFilter();
    setupAutoTotal();
    setupMiseBasCheck();
    setupRealtimeNotifications();
  });
})();
