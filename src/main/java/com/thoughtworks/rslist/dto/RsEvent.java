package com.thoughtworks.rslist.dto;

import lombok.Data;

@Data
public class RsEvent {

    private String eventName;
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
