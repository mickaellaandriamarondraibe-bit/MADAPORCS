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

  /* ---- Menu lateral repliable ----
     L'etat ouvert/ferme de chaque groupe est memorise et conserve entre les pages :
     un sous-menu ne se ferme (ou ne se rouvre) qu'au re-clic sur son titre. */
  function setupNavGroups() {
    var CLE = "madaporc.nav";
    var prefs = {};
    try { prefs = JSON.parse(localStorage.getItem(CLE) || "{}"); } catch (e) { prefs = {}; }
    function sauver() { try { localStorage.setItem(CLE, JSON.stringify(prefs)); } catch (e) {} }

    document.querySelectorAll(".nav__group").forEach(function (group) {
      var title = group.querySelector("[data-nav-group]");
      if (!title) return;
      var id = (title.getAttribute("data-nav-group") || title.textContent || "").trim();

      // Etat initial : preference memorisee si elle existe, sinon ouvert si la page courante est dans ce groupe.
      var actif = !!group.querySelector(".nav__link.is-active");
      var ouvert = prefs.hasOwnProperty(id) ? !!prefs[id] : actif;
      group.classList.toggle("is-open", ouvert);

      title.addEventListener("click", function () {
        var v = !group.classList.contains("is-open");
        group.classList.toggle("is-open", v);
        prefs[id] = v;
        sauver();
      });
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

  /* ---- Outils de liste : filtre texte + filtre par intervalle + tri + pagination ----
     Un seul moteur pour toute <table class="tbl"> (tri) et/ou [data-paginate] (pagination),
     de sorte que le tri (qui réordonne le DOM) reste cohérent avec la pagination.
     - Filtre texte    : <input data-filter-input="#idTable">  (+ bouton [data-filter-btn])
     - Filtre intervalle: <input data-range="#idTable" data-range-col="N" data-range-kind="min|max">
     - Tri              : clic sur un <th> (hors .actions). Colonnes .num en numérique, dates ISO en date. */
  function setupTables() {
    var tables = [];
    document.querySelectorAll("table.tbl, table[data-paginate]").forEach(function (t) {
      if (tables.indexOf(t) < 0) tables.push(t);
    });
    tables.forEach(initTable);

    function btnNav(txt) {
      var b = document.createElement("button");
      b.type = "button";
      b.className = "btn btn--ghost btn--sm";
      b.textContent = txt;
      return b;
    }

    // Convertit un texte affiché en nombre, en gérant les séparateurs de milliers
    // (espace, "." ou ",") et la virgule/point décimal (fr comme en). Ex : "1 250 000",
    // "1,250,000", "50000.00", "1.250,50" -> nombres corrects.
    function versNombre(txt) {
      // Sur une valeur "courant / total" (ex. effectif "5 / 5"), on ne garde que
      // le premier nombre ; sinon "5 / 5" serait lu comme 55.
      var brut = (txt || "").split("/")[0];
      var s = brut.replace(/\s/g, "").replace(/[^\d.,\-]/g, "");
      if (s === "") return NaN;
      var vir = s.lastIndexOf(","), pt = s.lastIndexOf(".");
      if (vir > -1 && pt > -1) {
        var decChar = vir > pt ? "," : ".";
        var milleChar = decChar === "," ? "." : ",";
        s = s.split(milleChar).join("").replace(decChar, ".");
      } else if (vir > -1) {
        s = (s.split(",").length === 2 && s.length - vir - 1 <= 2) ? s.replace(",", ".") : s.split(",").join("");
      } else if (pt > -1) {
        if (!(s.split(".").length === 2 && s.length - pt - 1 <= 2)) s = s.split(".").join("");
      }
      return parseFloat(s);
    }

    function initTable(table) {
      var corps = table.tBodies[0];
      if (!corps) return;
      var toutes = Array.prototype.slice.call(corps.rows);
      if (!toutes.length) return;

      var triable = table.classList.contains("tbl");
      var taillePage = parseInt(table.getAttribute("data-paginate"), 10) || 0;
      var etat = { q: "", triCol: -1, asc: true, page: 1, ranges: [] };

      // Type de chaque colonne (num / date / texte) pour trier et filtrer correctement.
      var ths = (table.tHead && table.tHead.rows[0]) ? table.tHead.rows[0].cells : [];
      var types = [];
      Array.prototype.forEach.call(ths, function (th, i) {
        if (th.classList.contains("num")) { types[i] = "num"; return; }
        var ex = toutes[0].cells[i] ? toutes[0].cells[i].textContent.trim() : "";
        types[i] = /^\d{4}-\d{2}-\d{2}/.test(ex) ? "date" : "texte";
      });

      function valeur(tr, col) {
        var c = tr.cells[col];
        var t = c ? c.textContent.trim() : "";
        if (types[col] === "num") {
          var n = versNombre(t);
          return isNaN(n) ? -Infinity : n;
        }
        if (types[col] === "date") return t; // ISO -> comparaison lexicale correcte
        return t.toLowerCase();
      }

      // Tri : en-tête cliquable ET menu de tri déporté (dans le panneau de filtres).
      function refletMenuTri(col, asc) {
        var sSel = null;
        document.querySelectorAll("[data-sort-select]").forEach(function (s) {
          if (document.querySelector(s.getAttribute("data-sort-select")) === table) sSel = s;
        });
        if (!sSel) return;
        sSel.value = col < 0 ? "" : String(col);
        var host = sSel.closest(".filters-sort");
        if (host) {
          host.setAttribute("data-dir", asc ? "asc" : "desc");
          host.querySelectorAll("[data-sort-dir]").forEach(function (b) {
            b.classList.toggle("is-active", b.getAttribute("data-sort-dir") === (asc ? "asc" : "desc"));
          });
        }
      }
      function trier(col, asc) {
        etat.triCol = col; etat.asc = asc; etat.page = 1;
        Array.prototype.forEach.call(ths, function (o) { o.removeAttribute("data-sort"); });
        if (col >= 0 && ths[col]) ths[col].setAttribute("data-sort", asc ? "asc" : "desc");
        refletMenuTri(col, asc);
        rendre();
      }
      if (triable) {
        Array.prototype.forEach.call(ths, function (th, i) {
          if (th.classList.contains("actions") || th.textContent.trim() === "") return;
          th.classList.add("th-sort");
          th.addEventListener("click", function () {
            trier(i, etat.triCol === i ? !etat.asc : true);
          });
        });
      }
      // Menu de tri : conteneur .filters-sort { <select data-sort-select="#t">, boutons [data-sort-dir] }.
      document.querySelectorAll(".filters-sort").forEach(function (host) {
        var sel = host.querySelector("[data-sort-select]");
        if (!sel || document.querySelector(sel.getAttribute("data-sort-select")) !== table) return;
        if (!sel.options.length) {
          var vide = document.createElement("option"); vide.value = ""; vide.textContent = "—"; sel.appendChild(vide);
          Array.prototype.forEach.call(ths, function (th, i) {
            if (th.classList.contains("actions") || th.textContent.trim() === "") return;
            var o = document.createElement("option"); o.value = String(i); o.textContent = th.textContent.trim(); sel.appendChild(o);
          });
        }
        function appliquer() {
          if (sel.value === "") { trier(-1, true); return; }
          trier(parseInt(sel.value, 10), host.getAttribute("data-dir") !== "desc");
        }
        sel.addEventListener("change", appliquer);
        host.querySelectorAll("[data-sort-dir]").forEach(function (b) {
          b.addEventListener("click", function () { host.setAttribute("data-dir", b.getAttribute("data-sort-dir")); appliquer(); });
        });
      });
      // Bouton "Effacer" : réinitialise les filtres client (intervalles + recherche) et le tri.
      document.querySelectorAll("[data-filters-reset]").forEach(function (btn) {
        if (document.querySelector(btn.getAttribute("data-filters-reset")) !== table) return;
        btn.addEventListener("click", function () {
          etat.ranges.forEach(function (rg) { if (rg.min) rg.min.value = ""; if (rg.max) rg.max.value = ""; });
          document.querySelectorAll("[data-filter-input]").forEach(function (inp) {
            if (document.querySelector(inp.getAttribute("data-filter-input")) === table) inp.value = "";
          });
          etat.q = "";
          trier(-1, true);
          var panel = btn.closest(".filters-panel");
          if (panel) panel.dispatchEvent(new Event("input", { bubbles: true }));
        });
      });

      // Filtre texte.
      document.querySelectorAll("[data-filter-input]").forEach(function (inp) {
        if (document.querySelector(inp.getAttribute("data-filter-input")) !== table) return;
        function go() { etat.q = inp.value.toLowerCase().trim(); etat.page = 1; rendre(); }
        inp.addEventListener("input", go);
        var barre = inp.closest(".toolbar");
        var btn = barre ? barre.querySelector("[data-filter-btn]") : null;
        if (btn) btn.addEventListener("click", function (e) { e.preventDefault(); go(); });
      });

      // Filtres par intervalle (min/max sur une colonne).
      var parCol = {};
      document.querySelectorAll("[data-range]").forEach(function (inp) {
        if (document.querySelector(inp.getAttribute("data-range")) !== table) return;
        var col = parseInt(inp.getAttribute("data-range-col"), 10);
        if (isNaN(col)) return;
        if (!parCol[col]) parCol[col] = { col: col, min: null, max: null };
        if (inp.getAttribute("data-range-kind") === "max") parCol[col].max = inp;
        else parCol[col].min = inp;
        inp.addEventListener("input", function () { etat.page = 1; rendre(); });
      });
      etat.ranges = Object.keys(parCol).map(function (k) { return parCol[k]; });

      function borne(inp, col) {
        var v = inp ? inp.value.trim() : "";
        if (v === "") return null;
        if (types[col] !== "num") return v;
        var n = versNombre(v);
        return isNaN(n) ? null : n;
      }

      // Barre de pagination.
      var nav = null, info = null, prec = null, suiv = null;
      if (taillePage > 0) {
        nav = document.createElement("div"); nav.className = "pagination";
        prec = btnNav("‹ Precedent");
        info = document.createElement("span"); info.className = "pagination__info";
        suiv = btnNav("Suivant ›");
        nav.appendChild(prec); nav.appendChild(info); nav.appendChild(suiv);
        table.parentNode.insertBefore(nav, table.nextSibling);
        prec.addEventListener("click", function () { etat.page--; rendre(); });
        suiv.addEventListener("click", function () { etat.page++; rendre(); });
      }

      function rendre() {
        // 1) filtrer (texte + intervalles)
        var lignes = toutes.filter(function (tr) {
          if (etat.q && tr.textContent.toLowerCase().indexOf(etat.q) < 0) return false;
          for (var r = 0; r < etat.ranges.length; r++) {
            var rg = etat.ranges[r];
            var lo = borne(rg.min, rg.col), hi = borne(rg.max, rg.col);
            if (lo === null && hi === null) continue;
            var v = valeur(tr, rg.col);
            if (lo !== null && v < lo) return false;
            if (hi !== null && v > hi) return false;
          }
          return true;
        });
        // 2) trier
        if (etat.triCol >= 0) {
          lignes.sort(function (a, b) {
            var x = valeur(a, etat.triCol), y = valeur(b, etat.triCol);
            if (x < y) return etat.asc ? -1 : 1;
            if (x > y) return etat.asc ? 1 : -1;
            return 0;
          });
        }
        // 3) réordonner le DOM, masquer les lignes exclues
        toutes.forEach(function (tr) { tr.style.display = "none"; });
        lignes.forEach(function (tr) { corps.appendChild(tr); });
        // 4) paginer
        if (taillePage > 0 && lignes.length > taillePage) {
          var pages = Math.ceil(lignes.length / taillePage);
          etat.page = Math.min(Math.max(1, etat.page), pages);
          var debut = (etat.page - 1) * taillePage;
          lignes.forEach(function (tr, i) {
            tr.style.display = (i >= debut && i < debut + taillePage) ? "" : "none";
          });
          if (nav) {
            nav.style.display = "";
            info.textContent = "Page " + etat.page + " / " + pages;
            prec.disabled = etat.page === 1;
            suiv.disabled = etat.page === pages;
          }
        } else {
          lignes.forEach(function (tr) { tr.style.display = ""; });
          if (nav) nav.style.display = "none";
        }
      }

      rendre();
    }
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

  /* ---- Panneau de filtres repliable (bouton "Filtres" -> ouvre/masque le panneau) ---- */
  function setupFilterPanels() {
    document.querySelectorAll("[data-filters-toggle]").forEach(function (btn) {
      var panel = document.querySelector(btn.getAttribute("data-filters-toggle"));
      if (!panel) return;
      var badge = btn.querySelector("[data-filters-count]");

      function ouvrir(v) {
        panel.hidden = !v;
        btn.classList.toggle("is-open", v);
        btn.setAttribute("aria-expanded", v ? "true" : "false");
      }
      // Nombre de filtres actifs (hors tri) : selects renseignés + intervalles renseignés.
      function compter() {
        var n = 0, vus = {};
        panel.querySelectorAll("select").forEach(function (el) {
          if (el.hasAttribute("data-sort-select")) return;
          if ((el.value || "").trim() !== "") n++;
        });
        panel.querySelectorAll("input").forEach(function (el) {
          if (el.type === "button" || el.type === "submit" || (el.value || "").trim() === "") return;
          if (el.hasAttribute("data-range")) {
            var k = el.getAttribute("data-range") + ":" + el.getAttribute("data-range-col");
            if (vus[k]) return; vus[k] = true;
          }
          n++;
        });
        return n;
      }
      function rafraichir() {
        if (!badge) return;
        var n = compter();
        badge.textContent = String(n);
        badge.hidden = n === 0;
      }

      btn.addEventListener("click", function () { ouvrir(panel.hidden); });
      panel.addEventListener("input", rafraichir);
      panel.addEventListener("change", rafraichir);
      rafraichir();
      ouvrir(panel.hasAttribute("data-open") || compter() > 0); // ouvert si data-open ou filtres actifs
    });
  }

  document.addEventListener("DOMContentLoaded", function () {
    setupMenu();
    setupActiveNav();
    setupNavGroups();
    setupConfirms();
    setupTables();
    setupFilterPanels();
    setupAutoTotal();
    setupMiseBasCheck();
    setupRealtimeNotifications();
  });
})();
