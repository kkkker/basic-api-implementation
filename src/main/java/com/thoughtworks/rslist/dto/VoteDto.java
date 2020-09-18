package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteDto {

    private int voteNum;

    @JsonIgnore
    private int rsEventId;

    private int userId;

    private Long voteDate;
}
