<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Connexion · MADAPORC</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@400;500;600;700;800;900&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
  <link rel="stylesheet" href="${ctx}/css/app.css">
</head>
<body>
<div class="auth-wrap">
  <div class="auth-card">
    <div class="auth-card__head">
      <div class="logo"><i class="fa-solid fa-piggy-bank"></i></div>
      <h1>MADAPORC</h1>
      <p>Connexion à la gestion d'élevage</p>
    </div>
    <div class="auth-card__body">

      <c:if test="${not empty error}"><div class="alert alert--err"><i class="fa-solid fa-circle-xmark"></i> <span>${error}</span></div></c:if>
      <c:if test="${not empty info}"><div class="alert alert--info"><i class="fa-solid fa-circle-info"></i> <span>${info}</span></div></c:if>

      <%-- POST vers AuthController @PostMapping("/login"), bind LoginDTO --%>
      <form method="post" action="${ctx}/login">
        <div class="field">
          <label for="email">Adresse e-mail <span class="req">*</span></label>
          <input class="input" type="email" id="email" name="email"
                 value="${param.email}" placeholder="vous@madaporc.mg" required autofocus>
        </div>
        <div class="field">
          <label for="motDePasse">Mot de passe <span class="req">*</span></label>
          <input class="input" type="password" id="motDePasse" name="motDePasse"
                 placeholder="••••••••" required>
        </div>
        <button class="btn btn--primary w-full" type="submit" style="justify-content:center;margin-top:6px">
          Se connecter
        </button>
      </form>

      <p class="muted" style="text-align:center;font-size:12px;margin-top:18px">
        Accès réservé aux rôles ADMIN et GESTIONNAIRE.
      </p>
    </div>
  </div>
</div>
</body>
</html>
