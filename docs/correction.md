
Mouvement : http://localhost:8082/lots/3/mouvements
- Le bouton Retour au lot ne marche plus 

Pesees : 
- apres creation de nouveau lot , on dois etre rediriger vers http://localhost:8082/lots/{id}/pesees pour completer le poid depart 

Lot : 
- Il est preferable de ne pas taper a la main le code comme LOT-F-001 dans http://localhost:8082/lots/form car si exemple on a plusieur lot , on sais plus quels sont les code deja existant , plutot exemple on met le choix sexe au debut et exemple on a pris Male , on aurra direct LOT-M et si on a choisi femelle alors LOT-F et la suite genre le chiffre on continu par l'id autoincriment de la base , exemple cest la 9 em lot alors LOT-F-009 ou LOT-M-009 
- Pour la creation du lot il faut aussi mettre un champ de poid pour pouvoir avoir un debut 

- apres avoir confirmer mis bas , le lot naissance doit etre creer automatiquement donc on appelle la fonction creerLotNaissanceApresMiseBas() dans confirmerMiseBas() et aussi lors de la confiramtion de mis bas on vas ajouter des input pas seulement dire que 10 porcellet nés mais on dit 5 femelle et 4 males nés donc cela va creer 2 nouveua lot , 1 lot mal et 1 lot femelle selon les contité entrer , comme cela on aura aussi directement les lot parents car lors de la creation du groupe on completeles information sur les lot origine et n peut les voir dans la table groue_reproduction dans la colonne lot_femelle_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE et lot_male_id BIGINT REFERENCES lots_porcs(id) ON DELETE SET NULL


depense : 
vaccin c'est depense , entrer (ingredient , lot , vaccin  .... ) c'est depense donc pour chaque formulaire qui depense de l'argent on doit utiliser la fonction creerDepense() pour enregistrer la depense dans la table depenses et aussi on doit faire le lien entre la depense et le vaccin ou l'ingredient ou le lot ou autre chose , donc on doit ajouter une colonne montant dans les tables qui depense de l'argent , exemple dans la table vaccins on ajoute une colonne montant BIGINT REFERENCES depenses(id) ON DELETE CASCADE et lors de l'enregistrement du vaccin on vas appeler la fonction creerDepense() pour enregistrer la depense et recuperer l'id de la depense et l'enregistrer dans la colonne montant de la table vaccins

donc on gros dans nouveau depense cest juste transport et tout , genre ceux qui n'est pas une fonctionnnaliter dans le projet 

Benefice :
dedant le calcule c'est juste total des vente , mais normalement cest genre prix de vente -prix d'achat car le prix d'achat de chaque lot peuve etre different


Groupe reproduction
- pour le creation de groupe de reproducion il faut verifier l'etat des lots(M et F) si atteind de maladit ou je sais pas 







Partie 2 :
2.1-quand j'ai essaer de modifier un lot , dans http://localhost:8082/lots/form?id=1 et 'ai changer actif en archiver cela n'a pas etet modifier pourtant quand je modifie genre le descrit ca marche 
2.2-quand je declar des mort dans un lot ca n'affiche pas dans calendrier 
le calendrier ne marche plus , que ce soit lors de creation vente ou creation mouvement dece et tout 
3.il devrai y avoir un condition que un groupe ne peu se resaillire que apres la difference entre la nouvelle date  saillit dans le formulaire et le date de mise bas nest pas superieru a 60 jours 



quand je fait confirmation de mis bae j'ai fait genre 5 viviant dont 3 femelle et jai enregistrer et ca a reussi mais seul le lot femelle a ete creer donc ou sont passer les 2 reste 