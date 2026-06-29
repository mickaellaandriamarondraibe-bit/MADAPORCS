<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajout de mouvement</title>
</head>
<body>
    <h1>Ajouter un mouvement de stock</h1>

    <form action="<%= request.getContextPath() %>/stocks/mouvements/save" method="post">
        <label for="ingredient">Ingrédient :</label>

        <select id="ingredient" name="ingredientId">
            <c:forEach var="ingredient" items="${ingredients}">
                <option value="${ingredient.id}">
                    ${ingredient.nom}
                </option>
            </c:forEach>
        </select>

        <label for="typeMouvement">Type de mouvement :</label>
        <select id="typeMouvement" name="typeMouvement">
            <c:forEach var="type" items="${typeMouvements}">
                <option value="${type}">
                    ${type}
                </option>
            </c:forEach>
        </select>

        <label>Quantité :</label>
        <input type="number" step="0.01" name="quantite">

        <br><br>

        <label>Date :</label>
        <input type="date" name="dateMouvement">

        <br><br>

        <label>Observation :</label>
        <textarea name="observation"></textarea>

        <br><br>

        <button type="submit">Enregistrer</button>
    </form>
</body>
</html>