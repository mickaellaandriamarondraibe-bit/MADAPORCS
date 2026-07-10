package com.madaporc.config;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Exige une session utilisateur (attribut "userId" posé au login).
 * Si l'utilisateur n'est pas connecté, on le renvoie vers la page de login ("/").
 * Certains chemins sont en plus réservés au rôle ADMIN (attribut "roleNom").
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    // Modules réservés à l'administrateur : gestion des utilisateurs, finances, import/export.
    private static final List<String> CHEMINS_ADMIN = List.of(
            "/utilisateurs", "/depenses", "/imports", "/imports-exports", "/exports");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }

        if (estCheminAdmin(request.getServletPath()) && !"ADMIN".equals(session.getAttribute("roleNom"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé à l'administrateur");
            return false;
        }
        return true;
    }

    private boolean estCheminAdmin(String path) {
        for (String prefixe : CHEMINS_ADMIN) {
            if (path.equals(prefixe) || path.startsWith(prefixe + "/")) {
                return true;
            }
        }
        return false;
    }
}
