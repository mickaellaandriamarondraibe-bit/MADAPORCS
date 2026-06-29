<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mouvements de stock</title>
</head>
<body>

    <h1>Mouvements de stock</h1>
    
    <table>
        <thead>
            <tr>
                <th>Code</th>
                <th>Ingrédient</th>
                <th>Type de mouvement</th>
                <th>Quantité</th>
                <th>Date du mouvement</th>
                <th>Observation</th>
            </tr>
        </thead>
        <tbody>
            <% for (MouvementStockAliment mouvement : mouvements) { %>
                <tr>
                    <td><%= mouvement.getId() %></td>
                    <td><%= mouvement.getIngredient().getNom() %></td>
                    <td><%= mouvement.getTypeMouvement().name() %></td>
                    <td><%= mouvement.getQuantite() %></td>
                    <td><%= mouvement.getDateMouvement() %></td>
                    <td><%= mouvement.getObservation() %></td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <a href="<%= request.getContextPath() %>/mouvements/form">Ajouter un mouvement</a>
</body>
</html>