package pl.sg.go_cardless.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class EndUserAgreement {
    private final int id;
    private final LocalDateTime created;
    private final int maxHistoricalDays;
    private final int accessValidForDays;
    private final String[] accessScope;
    private final LocalDateTime accepted;
    private final String institutionId;

    public EndUserAgreement(
            @JsonProperty("id") int id,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("max_historical_days") int maxHistoricalDays,
            @JsonProperty("access_valid_for_days") int accessValidForDays,
            @JsonProperty("access_scope") String[] accessScope,
            @JsonProperty("accepted") LocalDateTime accepted,
            @JsonProperty("institution_id") String institutionId) {
        this.id = id;
        this.created = created;
        this.maxHistoricalDays = maxHistoricalDays;
        this.accessValidForDays = accessValidForDays;
        this.accessScope = accessScope;
        this.accepted = accepted;
        this.institutionId = institutionId;
    }
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("created")
    public LocalDateTime getCreated() {
        return created;
    }

    @JsonProperty("max_historical_days")
    public int getMaxHistoricalDays() {
        return maxHistoricalDays;
    }

    @JsonProperty("access_valid_for_days")
    public int getAccessValidForDays() {
        return accessValidForDays;
    }

    @JsonProperty("access_scope")
    public String[] getAccessScope() {
        return accessScope;
    }

    @JsonProperty("accepted")
    public LocalDateTime getAccepted() {
        return accepted;
    }

    @JsonProperty("institution_id")
    public String getInstitutionId() {
        return institutionId;
    }
}
