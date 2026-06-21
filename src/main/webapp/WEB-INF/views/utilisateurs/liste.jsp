<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des utilisateurs - MADAPORC</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 1.5rem;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        .btn {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-size: 1rem;
            cursor: pointer;
            border: none;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .status-actif {
            color: #28a745;
            font-weight: bold;
        }
        .status-inactif {
            color: #dc3545;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Liste des utilisateurs</h1>
            <a href="${pageContext.request.contextPath}/utilisateurs/form" class="btn btn-primary">Ajouter un utilisateur</a>
        </div>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Rôle</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="utilisateur" items="${utilisateurs}">
                    <tr>
                        <td>${utilisateur.id}</td>
                        <td>${utilisateur.nom}</td>
                        <td>${utilisateur.prenom}</td>
                        <td>${utilisateur.email}</td>
                        <td>${utilisateur.role.nom}</td>
                        <td>
                            <span class="${utilisateur.actif ? 'status-actif' : 'status-inactif'}">
                                ${utilisateur.actif ? 'Actif' : 'Inactif'}
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/utilisateurs/form?id=${utilisateur.id}" class="btn btn-primary" style="margin-right: 0.5rem;">Modifier</a>
                            <c:if test="${utilisateur.actif}">
                                <form action="${pageContext.request.contextPath}/utilisateurs/desactiver/${utilisateur.id}" method="post" style="display: inline;">
                                    <button type="submit" class="btn btn-danger">Désactiver</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>