package com.lescours.bobolognese.filter;

import com.lescours.bobolognese.model.UserRole;
import com.lescours.bobolognese.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/product-list.xhtml", "/product-form.xhtml"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginPage = httpRequest.getContextPath() + "/login.xhtml";
        String indexPage = httpRequest.getContextPath() + "/index.xhtml";

        User user = (session != null) ? (User) session.getAttribute("user") : null;
        boolean isLoggedIn = user != null;
        boolean isAdmin = user != null && user.getRole() == UserRole.ADMIN;

        if (!isLoggedIn) {
            httpResponse.sendRedirect(loginPage);
        } else if (!isAdmin) {
            httpResponse.sendRedirect(indexPage);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
