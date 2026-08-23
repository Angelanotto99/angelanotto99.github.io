package com.arena.sentinel;

import android.content.Context;
import android.content.SharedPreferences;

class Prefs {
    private static final String NAME = "sentinel";
    private static final String K_KEY = "gemini_key";
    private static final String K_MODEL = "gemini_model";
    private static final String K_VERIFIED = "key_verified";

    // No hardcoded key — the user enters their own in Settings.
    static final String DEFAULT_KEY = "";
    static final String DEFAULT_MODEL = "models/gemini-2.5-flash";

    static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    static String getKey(Context c) {
        return sp(c).getString(K_KEY, "");
    }

    /** Changing the key always forces a re-verification on next use. */
    static void setKey(Context c, String key) {
        sp(c).edit()
                .putString(K_KEY, key == null ? "" : key.trim())
                .putBoolean(K_VERIFIED, false)
                .apply();
    }

    static boolean isKeyVerified(Context c) {
        return sp(c).getBoolean(K_VERIFIED, false);
    }

    static void setKeyVerified(Context c, boolean v) {
        sp(c).edit().putBoolean(K_VERIFIED, v).apply();
    }

    static String getModel(Context c) {
        return sp(c).getString(K_MODEL, DEFAULT_MODEL);
    }

    static void setModel(Context c, String model) {
        sp(c).edit().putString(K_MODEL, model).apply();
    }
}
