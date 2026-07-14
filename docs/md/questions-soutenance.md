# Questions / Reponses - Preparation soutenance

Reponses courtes a relire avant la soutenance.

---

## Est-il necessaire d'avoir une table analyse si les donnees (repartition) changent tout le temps ?

Oui. La repartition est ecrasee a chaque evenement, elle ne garde aucun historique.
L'analyse fige une photo datee (taux + decision) : c'est ce qui permet la tracabilite,
l'historique et la moyenne affichee sur le dashboard. Sans elle, on n'aurait que l'etat du moment.

Analogie : la repartition = le solde bancaire (change sans arret) ; l'analyse = un releve fige qu'on garde.

---

## La "prise de decision" du module, c'est une decision sur quoi ?

Sur l'avenir reproductif d'un lot de femelles : faut-il le garder pour la
reproduction ou le reformer ?

C'est une decision economique. Une femelle qu'on nourrit mais qui ne donne plus
de portees = perte seche (aliment, place, soins pour zero porcelet). Le module
dit a l'eleveur quels lots arreter, chiffres a l'appui.

Les sorties possibles (methode genererDecision) :

- APTE A LA REPRODUCTION : on garde le lot, on peut le remettre en saillie.
- A SURVEILLER : resultats moyens, on garde mais on observe le prochain cycle.
- Lot trop jeune : moins de 8 mois, pas evaluable, on attend.
- REFORME RECOMMANDEE : retirer de la reproduction et vendre / abattre.

Base sur 2 criteres :

- Taux recommande = part des femelles aptes / pretes dans le lot.
- Taux de fertilite observe = gestantes / saillies (est-ce que les saillies
  donnent vraiment des gestations).

Seuils : >= 80 / 80 -> apte ; >= 50 / 60 -> a surveiller ; sinon (et si le lot a
l'age d'etre juge) -> reforme.

Piege a connaitre : le taux de fertilite se calcule sur l'historique des groupes
du lot. Un lot neuf sans saillie a un taux de 0, d'ou le garde-fou "trop jeune"
qui evite une reforme injuste.

---

## Questions liees (au cas ou)

### Difference entre repartitions_reproductives_lots et analyses_reproduction_lots ?

Repartition = etat actuel du lot (combien de femelles par statut), mis a jour en continu.
Analyse = photo figee + calculs (taux, decision) a un instant donne, gardee comme historique.
L'analyse lit la repartition pour calculer ses chiffres.

### Quelle action remplit le dashboard Reproduction ?

Le bouton "Generer" sur la page Analyse reproductive : il cree une ligne d'analyse.
Le dashboard affiche la moyenne des analyses. Sans analyse generee, le dashboard reste vide.

### Que veut dire "reforme" (decision "REFORME RECOMMANDEE") ?

Reformer = retirer une femelle de la reproduction (trop vieille ou improductive).
C'est la decision quand les taux sont trop faibles.

### C'est quoi "femelles saillies" ?

Les femelles mises a la reproduction = celles en cycle + celles deja reproductrices aptes.
Les "pretes jamais saillies" ne comptent pas (elles n'ont jamais ete saillies).

### Difference entre "groupe de reproduction" et "analyse reproductive" ?

Groupe = suivi d'une saillie / mise bas (gestion d'un evenement).
Analyse = calcul de taux et decision sur un lot femelle (statistiques).
Le dashboard Reproduction lit l'analyse, pas le groupe.

---

# Module Calendrier

Questions possibles sur la page Calendrier de l'exploitation.

## A quoi sert le calendrier ?

A voir sur une seule vue (type Google Agenda) tous les evenements dates de
l'elevage : mises bas prevues, depenses, ventes et morts. L'eleveur repere d'un
coup d'oeil ce qui se passe dans le temps, sans ouvrir chaque module.

## Quels types d'evenements affiche-t-il ?

Quatre dans la version actuelle :
- Mises bas (or) : GroupeReproduction.datePrevueMiseBas
- Depenses (rouge) : Depense.dateDepense
- Ventes (vert) : Vente.dateVente
- Morts (gris) : MouvementLotPorc de type DECES, sur dateMouvement

## Comment reconnait-on un evenement "mort" ?

Un mouvement de lot dont le typeMouvement vaut "DECES". C'est la meme valeur que
celle utilisee ailleurs dans le projet (MouvementLotService), donc pas de nouvelle
convention inventee.

## As-tu cree une nouvelle table pour le calendrier ?

Non. Le calendrier ne stocke rien. Il lit les donnees deja en base (groupes,
depenses, ventes, mouvements) et les transforme a la volee en evenements. Aucune
migration, aucune table ajoutee.

## Comment les donnees arrivent au calendrier ?

Le CalendrierService rassemble les 4 sources dans une liste d'objets communs
(EvenementCalendrier : title, start, type, color). Le controller expose deux routes :
/calendrier (la page) et /calendrier/events (la liste en JSON). Le navigateur
recupere le JSON avec fetch et l'affiche.

## Pourquoi un DTO commun (EvenementCalendrier) ?

Parce que les 4 sources sont des entites differentes (Depense, Vente...). Le DTO
leur donne un format unique (titre, date, type, couleur) que la librairie
d'affichage comprend. Sans lui, il faudrait un traitement par type cote client.

## Quelle librairie pour l'affichage ? Pourquoi ?

FullCalendar, chargee par CDN. Elle donne le rendu Google Agenda (vues Mois /
Semaine / Liste, navigation, francais) avec tres peu de code. La reecrire a la
main aurait ete beaucoup plus long pour un resultat moins bon. Meme principe que
Chart.js deja utilise dans le projet.

## Comment marchent les boutons de categorie ?

Ce sont des toggles. Chaque bouton correspond a un type. Cliquer l'active ou le
desactive, et le calendrier reaffiche seulement les types actifs. Le filtrage se
fait cote navigateur (JavaScript), donc c'est instantane, sans recharger la page.

## Pourquoi filtrer cote navigateur et pas cote serveur ?

Le volume de donnees d'un elevage est petit : on peut tout envoyer une fois et
filtrer en local. C'est plus court a coder et plus rapide a l'usage (pas d'aller-
retour serveur a chaque clic). Filtrer cote serveur n'apporterait rien ici.

## Que renvoie exactement /calendrier/events ?

Une liste JSON. Chaque element : title (texte affiche), start (date au format
aaaa-mm-jj), type (misebas / depense / vente / mort) et color (la pastille).
FullCalendar lit title, start et color ; type sert au filtrage.

## Pourquoi la date est en texte (String) et pas en LocalDate ?

Pour envoyer directement le format attendu par FullCalendar ("2026-07-09") via
un simple toString() de la date. Cela evite tout souci de serialisation et reste
simple a lire cote client.

## Le calendrier modifie-t-il des donnees ?

Non. Il est en lecture seule : il affiche, il ne cree ni ne supprime rien. Aucun
risque pour les autres modules.

## Que se passe-t-il sans connexion internet ?

La page s'affiche mais le calendrier ne se rend pas, car FullCalendar vient d'un
CDN. C'est la meme limite que Chart.js. On pourrait heberger la librairie en local
pour y remedier.

## Comment ajouter un nouveau type (ex. vaccinations) ?

Trois petits ajouts : quelques lignes dans CalendrierService (lire la source et
creer les evenements), un bouton dans le JSP, une couleur dans le CSS. La
structure est prevue pour ca.

## Pourquoi seulement 4 types au depart ?

Ce sont les evenements les plus parlants (reproduction, argent, pertes = le coeur
de l'exploitation). On commence avec un module qui marche, puis on enrichit. Eviter
d'en faire trop d'un coup.

## Limites actuelles / ameliorations possibles ?

- Cliquer un evenement n'ouvre pas encore sa fiche (on pourrait ajouter un lien).
- Les montants ne sont pas formates avec separateur de milliers.
- Pas de creation d'evenement depuis le calendrier (lecture seule).
- Librairie en CDN (dependance internet).

---

## Comment gerez-vous la gestion financiere du projet ?

La gestion financiere repose sur deux flux : les recettes et les depenses, et le
benefice net qui en decoule.

Les recettes viennent des ventes. Une vente passe par deux etats : BROUILLON puis
VALIDEE. Le montant total est recalcule cote serveur a partir des lignes (prix
unitaire x quantite), jamais fait confiance au client. Et surtout, seules les
ventes validees comptent comme recette : tant qu'une vente est en brouillon ou
annulee, elle n'entre pas dans le chiffre d'affaires.

Les depenses sont centralisees dans une seule table. Il y a les depenses saisies
manuellement, mais aussi des depenses automatiques : quand on achete un lot, quand
on achete un ingredient de stock, ou quand on fait une vaccination, le systeme cree
automatiquement une depense via une methode reutilisable creerDepense. Comme ca,
tout ce qui coute de l'argent est trace au meme endroit, avec une categorie.

Le benefice net est simplement : somme des ventes validees - somme de toutes les
depenses, calcule sur une periode. Sur le tableau de bord, c'est le mois courant ;
dans le module Rapport, c'est la periode choisie. Si le benefice est negatif, il
s'affiche en rouge.

Techniquement, tous les montants sont en BigDecimal - jamais de double - pour
eviter les erreurs d'arrondi sur l'argent. Les totaux sont calcules directement en
base par des requetes SQL avec SUM et COALESCE(...,0), donc pas d'erreur meme
quand il n'y a aucune donnee.

Enfin, le module Rapport financier permet de generer, sur une periode donnee, un
recapitulatif : total des ventes, total des depenses, benefice net, nombre de
ventes et de depenses - exportable en PDF.

### Pourquoi seulement les ventes validees ?

Parce qu'un brouillon n'est pas une vente confirmee ; le compter fausserait le
chiffre d'affaires. La validation est aussi le moment ou on decremente l'effectif
du lot.

### Le benefice, c'est de la tresorerie ou du resultat comptable ?

C'est une approche tresorerie : on compte la depense au moment ou l'argent sort et
la recette au moment de la vente. Donc un lot achete un mois et vendu un autre mois
repartit la charge et le gain sur deux mois.
