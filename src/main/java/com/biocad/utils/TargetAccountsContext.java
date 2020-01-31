package com.biocad.utils;

import com.biocad.models.User;

import java.util.HashMap;
import java.util.Map;

public class TargetAccountsContext {
    private static TargetAccountsContext instance;
    private Map<User, Boolean> userCache;

    private TargetAccountsContext() {
        userCache = new HashMap<>();
    }

    public static TargetAccountsContext getInstance() {
        if (instance == null) {
            instance = new TargetAccountsContext();
        }

        return instance;
    }

    public Map<User, Boolean> getUserCache() {
        return userCache;
    }
}
