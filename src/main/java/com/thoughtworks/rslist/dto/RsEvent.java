package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RsEvent {

    @NotEmpty
    private String eventName;

    @NotEmpty
    private String keyword;

    @NotNull
    @JsonProperty("user_id")
    private Integer userId;

    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int voteNum;

    @JsonProperty
    public int getVoteNum() {
        return voteNum;
    }

    @JsonIgnore
    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    private int id;

    @JsonProperty
    public int getId() {
        return id;
    }

    @JsonIgnore
    public void setId(int id) {
        this.id = id;
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
