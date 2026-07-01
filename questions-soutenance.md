# Questions / Reponses - Preparation soutenance

Reponses courtes a relire avant la soutenance.

---

## Est-il necessaire d'avoir une table analyse si les donnees (repartition) changent tout le temps ?

Oui. La repartition est ecrasee a chaque evenement, elle ne garde aucun historique.
L'analyse fige une photo datee (taux + decision) : c'est ce qui permet la tracabilite,
l'historique et la moyenne affichee sur le dashboard. Sans elle, on n'aurait que l'etat du moment.

Analogie : la repartition = le solde bancaire (change sans arret) ; l'analyse = un releve fige qu'on garde.

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
