package com.lescours.bobolognese.bean;

import com.lescours.bobolognese.model.UserRole;
import com.lescours.bobolognese.model.User;
import com.lescours.bobolognese.service.JwtService;
import com.lescours.bobolognese.service.UserService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Named("loginBean")
@SessionScoped
@Getter
@Setter
public class LoginBean implements Serializable {

    @Inject
    private UserService userService;

    @Inject
    private JwtService jwtService;

    private String username;
    private String password;
    private String confirmPassword;
    private User currentUser;

    public String login() {
        Optional<User> userOpt = userService.authenticate(username, password);

        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .put("user", currentUser);

            String token = jwtService.generateToken(currentUser);
            addJwtCookie(token);

            username = null;
            password = null;

            return "index?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Identifiants invalides",
                            "Le nom d'utilisateur ou le mot de passe est incorrect."));
            return null;
        }
    }

    public String register() {
        if (!password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erreur",
                            "Les mots de passe ne correspondent pas."));
            return null;
        }

        if (userService.existsByUsername(username)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Erreur",
                            "Ce nom d'utilisateur est déjà utilisé."));
            return null;
        }

        currentUser = userService.create(username, password);

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("user", currentUser);

        String token = jwtService.generateToken(currentUser);
        addJwtCookie(token);

        username = null;
        password = null;
        confirmPassword = null;

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Inscription réussie",
                        "Bienvenue sur BOBO-LOGNÈSE !"));

        return "index?faces-redirect=true";
    }

    public String logout() {
        removeJwtCookie();

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        currentUser = null;
        return "login?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user") != null;
    }

    public User getLoggedUser() {
        return (User) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");
    }

    public boolean isAdmin() {
        User user = getLoggedUser();
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    public boolean isViewer() {
        User user = getLoggedUser();
        return user != null && user.getRole() == UserRole.VIEWER;
    }

    private void addJwtCookie(String token) {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getResponse();

        Cookie cookie = new Cookie(JwtService.JWT_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);
    }

    private void removeJwtCookie() {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getResponse();

        Cookie cookie = new Cookie(JwtService.JWT_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
