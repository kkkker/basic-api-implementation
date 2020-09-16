package com.thoughtworks.rslist.dto;

import lombok.Data;
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
    @Valid
    private User user;

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }

    public RsEvent(String eventName, String keyword, User user) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.user = user;
    }
}
