<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${utilisateurDTO.id != null ? 'Modifier' : 'Ajouter'} un utilisateur - MADAPORC</title>
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
        input, select {
            width: 100%;
            padding: 0.75rem;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 1rem;
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
        <h1>${utilisateurDTO.id != null ? 'Modifier' : 'Ajouter'} un utilisateur</h1>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/utilisateurs/save" method="post">
            <c:if test="${utilisateurDTO.id != null}">
                <input type="hidden" name="id" value="${utilisateurDTO.id}">
            </c:if>
            
            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" value="${utilisateurDTO.nom}" required>
            </div>
            
            <div class="form-group">
                <label for="prenom">Prénom</label>
                <input type="text" id="prenom" name="prenom" value="${utilisateurDTO.prenom}">
            </div>
            
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" value="${utilisateurDTO.email}" required>
            </div>
            
            <div class="form-group">
                <label for="motDePasse">Mot de passe ${utilisateurDTO.id == null ? '*' : '(laisser vide pour conserver)'}</label>
                <input type="password" id="motDePasse" name="motDePasse" ${utilisateurDTO.id == null ? 'required' : ''}>
            </div>
            
            <div class="form-group">
                <label for="roleId">Rôle *</label>
                <select id="roleId" name="roleId" required>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.id}" ${utilisateurDTO.roleId == role.id ? 'selected' : ''}>${role.nom}</option>
                    </c:forEach>
                </select>
            </div>
            
            <c:if test="${utilisateurDTO.id != null}">
                <div class="form-group">
                    <label for="actif">Actif</label>
                    <select id="actif" name="actif">
                        <option value="true" ${utilisateurDTO.actif ? 'selected' : ''}>Oui</option>
                        <option value="false" ${!utilisateurDTO.actif ? 'selected' : ''}>Non</option>
                    </select>
                </div>
            </c:if>
            
            <div class="form-group">
                <a href="${pageContext.request.contextPath}/utilisateurs" class="btn btn-secondary">Annuler</a>
                <button type="submit" class="btn btn-primary">Enregistrer</button>
            </div>
        </form>
    </div>
</body>
</html>
