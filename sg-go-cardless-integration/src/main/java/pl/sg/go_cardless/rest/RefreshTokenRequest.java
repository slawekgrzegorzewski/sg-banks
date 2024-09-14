package pl.sg.go_cardless.rest;

public class RefreshTokenRequest {
    private final String refresh;

    public RefreshTokenRequest(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() {
        return refresh;
    }
}
