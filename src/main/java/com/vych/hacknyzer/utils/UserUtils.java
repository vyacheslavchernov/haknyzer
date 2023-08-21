package com.vych.hacknyzer.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class UserUtils {

    public static UserDetails getUserDetails() {
        return (UserDetails) getAuth().getPrincipal();
    }

    public static boolean getAdminFlag() {
        return getAuth().getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    public static String getUsername() {
        return getAuth().getName();
    }

    private static Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
