<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Analyse reproductive" />
<c:set var="crumbs" value="Reproduction / Analyse reproductive" />

<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="page-head">
    <div>
        <h1>Analyse reproductive</h1>
        <p>Choisissez un lot femelle de reproduction à analyser.</p>
    </div>
</div>

<div class="card">
    <div class="card__head">
        <h2>Lots disponibles pour analyse</h2>
        <p>Seuls les lots FEMELLE, REPRODUCTION et ACTIF sont affichés.</p>
    </div>

    <div class="card__body" style="padding:0">
        <table class="tbl">
            <thead>
                <tr>
                    <th>Code lot</th>
                    <th>Sexe</th>
                    <th>Objectif</th>
                    <th class="num">Effectif actuel</th>
                    <th>Statut</th>
                    <th class="actions-col">Actions</th>
                </tr>
            </thead>

            <tbody>
                <c:choose>
                    <c:when test="${not empty lots}">
                        <c:forEach var="l" items="${lots}">
                            <tr>
                                <td>
                                    <strong>${l.codeLot}</strong>
                                </td>

                                <td>
                                    <span class="badge badge--gold">
                                        ${l.sexe}
                                    </span>
                                </td>

                                <td>${l.objectif}</td>

                                <td class="num">
                                    <b>${l.effectifActuel}</b>
                                </td>

                                <td>
                                    <span class="badge badge--green">
                                        ${l.statut}
                                    </span>
                                </td>

                                <td>
                                    <div class="actions">
                                        <a class="btn btn--ghost"
                                           href="${ctx}/reproduction/analyse/lots/${l.id}">
                                            Voir analyse
                                        </a>

                                        <form method="post"
                                              action="${ctx}/reproduction/analyse/generer/${l.id}"
                                              style="display:inline">
                                            <button class="btn btn--primary" type="submit">
                                                Générer
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="6">
                                <div class="empty" style="padding:30px">
                                    Aucun lot femelle de reproduction actif trouvé.
                                </div>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>  