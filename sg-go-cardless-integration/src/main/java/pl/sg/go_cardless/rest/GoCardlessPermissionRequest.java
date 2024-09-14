package pl.sg.go_cardless.rest;

import java.net.URL;

public class GoCardlessPermissionRequest {
    private String institutionId;
    private int maxHistoricalDays;
    private URL redirect;
    private String userLanguage;

    public GoCardlessPermissionRequest() {
    }

    public GoCardlessPermissionRequest(String institutionId, int maxHistoricalDays, URL redirect, String userLanguage) {
        this.institutionId = institutionId;
        this.maxHistoricalDays = maxHistoricalDays;
        this.redirect = redirect;
        this.userLanguage = userLanguage;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public GoCardlessPermissionRequest setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public int getMaxHistoricalDays() {
        return maxHistoricalDays;
    }

    public GoCardlessPermissionRequest setMaxHistoricalDays(int maxHistoricalDays) {
        this.maxHistoricalDays = maxHistoricalDays;
        return this;
    }

    public URL getRedirect() {
        return redirect;
    }

    public GoCardlessPermissionRequest setRedirect(URL redirect) {
        this.redirect = redirect;
        return this;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public GoCardlessPermissionRequest setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
        return this;
    }
}
