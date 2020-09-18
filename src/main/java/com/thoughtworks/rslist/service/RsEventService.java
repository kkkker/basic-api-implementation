package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.EventIndexException;
import com.thoughtworks.rslist.exception.EventRangeException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Configuration
public class RsEventService {

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    public RsEvent getRsEventById(int id) throws EventIndexException {
        Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(id);
        if (!optionalRsEventEntity.isPresent()) {
            throw new EventIndexException();
        }
        RsEventEntity rsEventEntity = optionalRsEventEntity.get();
        return new RsEvent(rsEventEntity.getEventName(),
                rsEventEntity.getKeyword(),
                rsEventEntity.getUserEntity().getId(),
                rsEventEntity.getVoteNum(),
                rsEventEntity.getId());
    }

    public List<RsEvent> getRsEventByRange(Integer start, Integer end) throws EventRangeException {
        List<RsEvent> rsList = rsEventRepository.findAll().stream()
                .map(rsEventEntity -> new RsEvent(rsEventEntity.getEventName(),
                        rsEventEntity.getKeyword(),
                        rsEventEntity.getUserEntity().getId(),
                        rsEventEntity.getVoteNum(),
                        rsEventEntity.getId()))
                .collect(Collectors.toList());
        if (start == null || end == null) {
            return rsList;
        }
        if (start < 1 || start > rsList.size() || end < start || end > rsList.size()) {
            throw new EventRangeException();
        }
        return rsList.subList(start - 1, end);
    }

    public int addOneRsEvent(RsEvent rsEvent) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(rsEvent.getUserId());
        if (!optionalUserEntity.isPresent()) {
            return 0;
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .userEntity(optionalUserEntity.get())
                .keyword(rsEvent.getKeyword())
                .build();
        rsEventRepository.save(rsEventEntity);
        return rsEventRepository.findAll().indexOf(rsEventEntity) + 1;
    }

    public boolean updateRsEventByIndex(Integer id,RsEvent rsEvent) {
        Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(id);
        if (!optionalRsEventEntity.isPresent()) {
            return false;
        }
        RsEventEntity rsEventEntity = optionalRsEventEntity.get();
        if (!Objects.equals(rsEventEntity.getUserEntity().getId(), rsEvent.getUserId())) {
            return false;
        }
        updateRsEvent(rsEventEntity, rsEvent);
        rsEventRepository.save(rsEventEntity);
        return true;
    }

    void updateRsEvent(RsEventEntity rsEventEntity, RsEvent rsEvent) {
        if (rsEvent.getKeyword() != null) {
            rsEventEntity.setKeyword(rsEvent.getKeyword());
        }
        if (rsEvent.getEventName() != null) {
            rsEventEntity.setEventName(rsEvent.getEventName());
        }
    }

    public boolean deleteRsEventByIndex(int index) {
        Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(index);
        if (!optionalRsEventEntity.isPresent()) {
            return false;
        }
        rsEventRepository.deleteById(index);
        return true;
    }
}
