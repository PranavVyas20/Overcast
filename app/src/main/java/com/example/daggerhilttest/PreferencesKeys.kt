package com.example.daggerhilttest

import androidx.datastore.preferences.core.doublePreferencesKey

 object PreferencesKeys {
    val SAVED_LAT = doublePreferencesKey("saved_lat")
    val SAVED_LONG = doublePreferencesKey("saved_long")
}