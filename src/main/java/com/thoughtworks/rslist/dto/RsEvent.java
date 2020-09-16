package com.thoughtworks.rslist.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RsEvent {

    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyword;
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
