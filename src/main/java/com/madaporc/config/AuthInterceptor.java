package com.madaporc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Exige une session utilisateur (attribut "userId" posé au login).
 * Si l'utilisateur n'est pas connecté, on le renvoie vers la page de login ("/").
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        boolean connecte = session != null && session.getAttribute("userId") != null;

        if (!connecte) {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
        return true;
    }
}
