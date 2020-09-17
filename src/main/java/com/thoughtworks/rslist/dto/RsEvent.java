package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RsEvent {

    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyword;

    @NotNull
    @JsonProperty("user_id")
    private int userId;

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEvent(String eventName, String keyword, int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }
}
