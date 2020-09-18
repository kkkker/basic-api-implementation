package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

        public RsEventEntity(String eventName, String keyword, UserEntity userEntity) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userEntity = userEntity;
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
