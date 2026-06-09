# MADAPORC - Squelette strict PDF, placeholders uniquement

Cette version respecte les noms du PDF :

- `AuthController` et non `LoginController`
- `SalaireController` et non `SalaireEmployeController`
- routes PDF comme `/lots/form`, `/lots/{id}`, `/cycles`, `/reproduction/evenements`, etc.
- vues PDF comme `lots/listeLots`, `lots/formLot`, `commerce/ventes`, `settings/utilisateurs`, etc.

## Important

Ce projet contient des placeholders uniquement.
Il ne contient pas encore de vraie logique métier.

Les seuls fichiers ajoutés hors PDF sont les fichiers techniques obligatoires d'un projet Spring Boot :

- `pom.xml`
- `MadaporcApplication.java`
- `application.properties`
- `.gitignore`
- `README.md`
- `static/css/app.css`
- `templates/placeholder.html`
- `templates/fragments/sidebar.html`
- `docs/CONFORMITE_PDF.md`

## Lancer

```bash
mvn spring-boot:run
```

Puis ouvrir directement une route du PDF, par exemple :

```text
http://localhost:8080/login
http://localhost:8080/dashboard
http://localhost:8080/lots
```

## Vérification

La liste des routes strictes est dans :

```text
docs/CONFORMITE_PDF.md
```
