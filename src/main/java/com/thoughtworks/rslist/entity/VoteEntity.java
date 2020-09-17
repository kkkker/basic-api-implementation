package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vote")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "vote_number")
    private int voteNum;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "vote_time")
    private String voteTime;

    @Column(name = "rs_event_id")
    private int rsEventId;
}
