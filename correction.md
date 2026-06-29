Dashboard :
 -l'affichage du graffe ne montre pas vraiment les evolutions(monté , chute ) 
 - Le bouton generer rapport ne marche pas encore 

Mouvement : http://localhost:8082/lots/3/mouvements
- Le bouton Retour au lot ne marche plus 

Pesees : 
- apres creation de nouveau lot , on dois etre rediriger vers http://localhost:8082/lots/{id}/pesees pour completer le poid depart 

Lot : 
- Il est preferable de ne pas taper a la main le code comme LOT-F-001 dans http://localhost:8082/lots/form car si exemple on a plusieur lot , on sais plus quels sont les code deja existant , plutot exemple on met le choix sexe au debut et exemple on a pris Male , on aurra direct LOT-M et si on a choisi femelle alors LOT-F et la suite genre le chiffre on continu par l'id autoincriment de la base , exemple cest la 9 em lot alors LOT-F-009 ou LOT-M-009 

- apres avoir confirmer mis bas , le lot naissance doit etre creer automatiquement donc on appelle la fonction creerLotNaissanceApresMiseBas() dans confirmerMiseBas() et aussi lors de la confiramtion de mis bas on vas ajouter des input pas seulement dire que 10 porcellet nés mais on dit 5 femelle et 4 males nés donc cela va creer 2 nouveua lot , 1 lot mal et 1 lot femelle selon les contité entrer , comme cela on aura aussi directement les lot parents car lors de la creation du groupe on completeles information sur les lot origine et n peut les voir dans la table groue_reproduction dans la colonne lot_femelle_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE et lot_male_id BIGINT REFERENCES lots_porcs(id) ON DELETE SET NULL


