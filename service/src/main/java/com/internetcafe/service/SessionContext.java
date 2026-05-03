package com.internetcafe.service;

import com.internetcafe.domain.entities.User;
import com.internetcafe.domain.enums.UserRole;

public final class SessionContext {
    private static User currentUser;

    private SessionContext() {
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return isLoggedIn() && currentUser.getRole() == UserRole.ADMIN;
    }
}
