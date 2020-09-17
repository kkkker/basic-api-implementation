package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.rslist.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "rs_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "event_name")
    private String eventName;

    private String keyword;

    @Column(name = "user_id")
    private int userId;

    public RsEventEntity(String eventName, String keyword, int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RsEventEntity that = (RsEventEntity) o;
        return Objects.equals(eventName, that.eventName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName);
    }
}
