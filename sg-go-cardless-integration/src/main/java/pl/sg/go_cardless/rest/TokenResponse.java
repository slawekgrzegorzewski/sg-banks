package pl.sg.go_cardless.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {

    private final String access;

    private final long accessExpires;

    private final String refresh;

    private final long refreshExpires;

    public TokenResponse(@JsonProperty("access") String access,
                         @JsonProperty("access_expires") long accessExpires,
                         @JsonProperty("refresh") String refresh,
                         @JsonProperty("refresh_expires") long refreshExpires) {
        this.access = access;
        this.accessExpires = accessExpires;
        this.refresh = refresh;
        this.refreshExpires = refreshExpires;
    }

    @JsonProperty("access")
    public String getAccess() {
        return access;
    }

    @JsonProperty("access_expires")
    public long getAccessExpires() {
        return accessExpires;
    }

    @JsonProperty("refresh")
    public String getRefresh() {
        return refresh;
    }

    @JsonProperty("refresh_expires")
    public long getRefreshExpires() {
        return refreshExpires;
    }
}
