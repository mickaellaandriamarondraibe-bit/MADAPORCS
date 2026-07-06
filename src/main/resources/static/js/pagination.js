// Pagination simple cote navigateur.
// Sur toute table <table data-paginate="10">, decoupe les lignes en pages
// et ajoute des boutons Precedent / Suivant. Aucun changement cote serveur.
(function () {

  function boutton(texte) {
    var b = document.createElement('button');
    b.type = 'button';
    b.className = 'btn btn--ghost btn--sm';
    b.textContent = texte;
    return b;
  }

  function paginer(table) {
    var taille = parseInt(table.getAttribute('data-paginate'), 10) || 10;
    var corps = table.tBodies[0];
    if (!corps) return;

    var lignes = Array.prototype.slice.call(corps.rows);
    if (lignes.length <= taille) return; // pas besoin de pagination

    var pages = Math.ceil(lignes.length / taille);
    var courante = 1;

    var nav = document.createElement('div');
    nav.className = 'pagination';
    var prec = boutton('‹ Precedent');
    var info = document.createElement('span');
    info.className = 'pagination__info';
    var suiv = boutton('Suivant ›');
    nav.appendChild(prec);
    nav.appendChild(info);
    nav.appendChild(suiv);
    table.parentNode.insertBefore(nav, table.nextSibling);

    function afficher(p) {
      courante = Math.min(Math.max(1, p), pages);
      var debut = (courante - 1) * taille;
      var fin = debut + taille;
      lignes.forEach(function (r, i) {
        r.style.display = (i >= debut && i < fin) ? '' : 'none';
      });
      info.textContent = 'Page ' + courante + ' / ' + pages;
      prec.disabled = (courante === 1);
      suiv.disabled = (courante === pages);
    }

    prec.addEventListener('click', function () { afficher(courante - 1); });
    suiv.addEventListener('click', function () { afficher(courante + 1); });
    afficher(1);
  }

  document.addEventListener('DOMContentLoaded', function () {
    var tables = document.querySelectorAll('table[data-paginate]');
    Array.prototype.forEach.call(tables, paginer);
  });

})();
