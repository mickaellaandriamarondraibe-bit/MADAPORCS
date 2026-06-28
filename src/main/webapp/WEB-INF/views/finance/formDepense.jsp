<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${depenseDTO.id != null ? 'Modifier' : 'Ajouter'} une dépense - MADAPORC</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
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
        .form-group {
            margin-bottom: 1rem;
        }
        label {
            display: block;
            margin-bottom: 0.5rem;
            color: #555;
        }
        input, select, textarea {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 1rem;
            font-family: Arial, sans-serif;
        }
        textarea {
            resize: vertical;
            min-height: 80px;
        }
        .btn {
            padding: 0.75rem 1.5rem;
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
        .btn-secondary {
            background-color: #6c757d;
            color: white;
            margin-right: 0.5rem;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        .error {
            color: #dc3545;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>${depenseDTO.id != null ? 'Modifier' : 'Ajouter'} une dépense</h1>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/depenses/save" method="post">
            <c:if test="${depenseDTO.id != null}">
                <input type="hidden" name="id" value="${depenseDTO.id}">
            </c:if>

            <div class="form-group">
                <label for="dateDepense">Date *</label>
                <input type="date" id="dateDepense" name="dateDepense" value="${depenseDTO.dateDepense}" required>
            </div>

            <div class="form-group">
                <label for="montant">Montant (Ar) *</label>
                <input type="number" id="montant" name="montant" value="${depenseDTO.montant}" step="0.01" min="0.01" required>
            </div>

            <div class="form-group">
                <label for="categorieId">Catégorie</label>
                <select id="categorieId" name="categorieId">
                    <option value="">-- Aucune catégorie --</option>
                    <c:forEach var="categorie" items="${categories}">
                        <option value="${categorie.id}" ${depenseDTO.categorieId == categorie.id ? 'selected' : ''}>${categorie.nom}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description">${depenseDTO.description}</textarea>
            </div>

            <div class="form-group">
                <a href="${pageContext.request.contextPath}/depenses" class="btn btn-secondary">Annuler</a>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </form>
    </div>
</body>
</html>
