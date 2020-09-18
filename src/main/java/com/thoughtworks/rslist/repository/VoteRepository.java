package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends CrudRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();
    List<VoteEntity> findAllByVoteDateBetween(Long startTime, Long endTime);
}
