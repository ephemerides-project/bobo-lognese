package com.lescours.bobolognese.filter;

import com.lescours.bobolognese.model.User;
import com.lescours.bobolognese.service.JwtService;
import com.lescours.bobolognese.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(filterName = "JwtAuthenticationFilter", urlPatterns = {"/*"})
public class JwtAuthenticationFilter implements Filter {

    @Inject
    private JwtService jwtService;

    @Inject
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            Optional<String> jwtToken = extractJwtFromCookies(httpRequest);

            if (jwtToken.isPresent()) {
                String token = jwtToken.get();

                if (!jwtService.isTokenExpired(token)) {
                    Optional<String> usernameOpt = jwtService.extractUsername(token);

                    if (usernameOpt.isPresent()) {
                        Optional<User> userOpt = userService.findByUsername(usernameOpt.get());

                        if (userOpt.isPresent()) {
                            session.setAttribute("user", userOpt.get());
                        } else {
                            removeJwtCookie(httpResponse);
                        }
                    }
                } else {
                    removeJwtCookie(httpResponse);
                }
            }
        }

        chain.doFilter(request, response);
    }

    private Optional<String> extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> JwtService.JWT_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void removeJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtService.JWT_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
