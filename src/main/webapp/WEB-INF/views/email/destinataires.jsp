<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Destinataires des alertes" />
<c:set var="crumbs"    value="Administration / <b>Destinataires des alertes</b>" />
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
  <div><h1>Destinataires des alertes</h1><p>Ces adresses reçoivent automatiquement les e-mails d'alerte de reproduction</p></div>
</div>

<div class="card" style="max-width:760px">
  <div class="card__body">
    <c:if test="${not configure}">
      <div class="alert alert--warn"><span>L'envoi d'e-mails n'est pas configuré (mot de passe SMTP manquant). Les alertes ne partiront pas tant que ce n'est pas renseigné.</span></div>
    </c:if>
    <form method="post" action="${ctx}/email/ajouter">
      <div class="form-grid">
        <div class="field span-2"><label>Nouvelle adresse <span class="req">*</span></label><input class="input" type="email" name="email" placeholder="exemple@mail.com" required></div>
      </div>
      <div class="form-actions">
        <button class="btn btn--primary" type="submit">Ajouter</button>
      </div>
    </form>
  </div>
</div>

<div class="card" style="max-width:760px">
  <div class="table-wrap">
    <table class="tbl">
      <thead><tr><th>Adresse e-mail</th><th class="actions">Actions</th></tr></thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty destinataires}">
            <c:forEach var="d" items="${destinataires}">
              <tr>
                <td>${d.email}</td>
                <td class="actions">
                  <form method="post" action="${ctx}/email/${d.id}/supprimer" style="display:inline" onsubmit="return confirm('Supprimer ce destinataire ?')">
                    <button type="submit" style="background:none;border:none;cursor:pointer;color:var(--vert-600);font-weight:600;font-family:inherit;font-size:13px">Supprimer</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr><td colspan="2"><div class="empty"><p>Aucun destinataire. Les alertes iront à l'adresse configurée par défaut.</p></div></td></tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>
