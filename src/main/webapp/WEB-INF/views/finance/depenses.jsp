<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dépenses - MADAPORC</title>
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
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }
        h1 {
            color: #333;
            margin: 0;
        }
        .total-card {
            background-color: #f0f4ff;
            border: 1px solid #c7d2fe;
            border-radius: 6px;
            padding: 1rem 1.5rem;
            margin-bottom: 1.5rem;
            display: inline-block;
        }
        .total-card span {
            font-size: 1.1rem;
            color: #333;
        }
        .total-card strong {
            font-size: 1.3rem;
            color: #1d4ed8;
        }
        .btn {
            padding: 0.5rem 1rem;
            border-radius: 4px;
            text-decoration: none;
            font-size: 1rem;
            cursor: pointer;
            border: none;
            display: inline-block;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 0.9rem 1rem;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #555;
        }
        tr:hover {
            background-color: #fafafa;
        }
        .empty {
            text-align: center;
            color: #888;
            padding: 2rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Dépenses</h1>
            <a href="${pageContext.request.contextPath}/depenses/form" class="btn btn-primary">Ajouter une dépense</a>
        </div>

        <div class="total-card">
            <span>Total des dépenses : </span>
            <strong><fmt:formatNumber value="${total}" type="number" minFractionDigits="2" maxFractionDigits="2"/> Ar</strong>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Catégorie</th>
                    <th>Montant (Ar)</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty depenses}">
                        <tr>
                            <td colspan="5" class="empty">Aucune dépense enregistrée.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="depense" items="${depenses}">
                            <tr>
                                <td><fmt:formatDate value="${depense.dateDepense}" pattern="dd/MM/yyyy" type="date"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${depense.categorie != null}">${depense.categorie.nom}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                                <td><fmt:formatNumber value="${depense.montant}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty depense.description}">${depense.description}</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/depenses/form?id=${depense.id}" class="btn btn-primary">Modifier</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</body>
</html>
