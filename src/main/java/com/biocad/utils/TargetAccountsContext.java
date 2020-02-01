package com.biocad.utils;

import com.biocad.models.User;

import java.util.HashMap;
import java.util.Map;

public class TargetAccountsContext {
    private static TargetAccountsContext instance;
    private Boolean isTargetUserLoggedIn; //TODO: This singleton should hold Map with multiple target users in future

    private TargetAccountsContext() {
        isTargetUserLoggedIn = false;
    }

    public static TargetAccountsContext getInstance() {
        if (instance == null) {
            instance = new TargetAccountsContext();
        }

        return instance;
    }

    public Boolean isTargetUserLoggedIn() {
        return isTargetUserLoggedIn;
    }

    public void isTargetUserLoggedIn(Boolean targetUserLoggedIn) {
        isTargetUserLoggedIn = targetUserLoggedIn;
    }
}
