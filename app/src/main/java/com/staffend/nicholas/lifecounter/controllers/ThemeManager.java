package com.staffend.nicholas.lifecounter.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.staffend.nicholas.lifecounter.R;

/**
 * Created by Nicholas on 3/17/2016.
 */
public class ThemeManager {
    private int drawable_background;
    private int lightColor;
    private int theme;


    /**
     * Loads the theme from shared preferences for use by the app.
     * @param c
     */
    public ThemeManager(Context c) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        String guild = sp.getString("prefColorScheme", "Default");
        if (guild.equalsIgnoreCase(c.getResources().getString(R.string.selesnya))) {
            this.drawable_background = R.drawable.std_life_bkg_seles;
            this.lightColor = R.color.primary_light_seles;
            this.theme = R.style.AppThemeSeles;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.azorious))) {
            this.drawable_background = R.drawable.std_life_bkg_azorious;
            this.lightColor = R.color.primary_light_azorious;
            this.theme = R.style.AppThemeAzorious;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.dimir))) {
            this.drawable_background = R.drawable.std_life_bkg_dimir;
            this.lightColor = R.color.primary_light_dimir;
            this.theme = R.style.AppThemeDimir;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.rakdos))) {
            this.drawable_background = R.drawable.std_life_bkg_rakdos;
            this.lightColor = R.color.primary_light_rakdos;
            this.theme = R.style.AppThemeRakdos;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.gruul))) {
            this.drawable_background = R.drawable.std_life_bkg_gruul;
            this.lightColor = R.color.primary_light_gruul;
            this.theme = R.style.AppThemeGruul;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.orzhov))) {
            this.drawable_background = R.drawable.std_life_bkg_orzhov;
            this.lightColor = R.color.primary_light_orzhov;
            this.theme = R.style.AppThemeOrzhov;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.izzet))) {
            this.drawable_background = R.drawable.std_life_bkg_izzet;
            this.lightColor = R.color.primary_light_izzet;
            this.theme = R.style.AppThemeIzzet;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.golgari))) {
            this.drawable_background = R.drawable.std_life_bkg_golgari;
            this.lightColor = R.color.primary_light_golgari;
            this.theme = R.style.AppThemeGolgari;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.boros))) {
            this.drawable_background = R.drawable.std_life_bkg_boros;
            this.lightColor = R.color.primary_light_boros;
            this.theme = R.style.AppThemeBoros;
        } else if (guild.equalsIgnoreCase(c.getResources().getString(R.string.simic))) {
            this.drawable_background = R.drawable.std_life_bkg_simic;
            this.lightColor = R.color.primary_light_simic;
            this.theme = R.style.AppThemeSimic;
        } else {
            this.drawable_background = R.drawable.std_life_bkg;
            this.lightColor = R.color.primary_light_seles;
            this.theme = R.style.AppTheme;
        }
    }

    /**
     * Gets the currently set theme
     * @return
     */
    public int getTheme() {
        return this.theme;
    }

    /**
     * Get the current background drawable resource for app
     * @return
     */
    public int getDrawableBackground() {
        return this.drawable_background;
    }

    /**
     * Get the primary light color for the app
     * @return
     */
    public int getPrimaryLight(){
        return this.lightColor;
    }


}
