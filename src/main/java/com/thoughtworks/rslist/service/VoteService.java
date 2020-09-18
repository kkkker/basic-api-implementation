package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    public List<VoteDto> getVotingRecordByDateRange(Long start, Long end) {
        List<VoteEntity> voteEntityList = voteRepository.findAllByVoteDateBetween(start, end);
        return voteEntityList.stream()
                .map(voteEntity -> VoteDto.builder()
                        .voteDate(voteEntity.getVoteDate())
                        .rsEventId(voteEntity.getRsEventEntity().getId())
                        .userId(voteEntity.getUserEntity().getId())
                        .voteNum(voteEntity.getVoteNum())
                        .build())
                .collect(Collectors.toList());
    }

    public List<VoteDto> getVotingRecordByRsEventId(int rsEventId) {
        List<VoteEntity> voteEntityList = voteRepository.findAll();
        return voteEntityList.stream()
                .filter(voteEntity -> voteEntity.getRsEventEntity().getId() == rsEventId)
                .map(voteEntity -> VoteDto.builder()
                        .voteDate(voteEntity.getVoteDate())
                        .rsEventId(voteEntity.getRsEventEntity().getId())
                        .userId(voteEntity.getUserEntity().getId())
                        .voteNum(voteEntity.getVoteNum())
                        .build())
                .collect(Collectors.toList());
    }

    public List<VoteDto> getVotingRecordByUserId(int userId) {
        List<VoteEntity> voteEntityList = voteRepository.findAll();
        return voteEntityList.stream()
                .filter(voteEntity -> voteEntity.getUserEntity().getId() == userId)
                .map(voteEntity -> VoteDto.builder()
                        .voteDate(voteEntity.getVoteDate())
                        .rsEventId(voteEntity.getRsEventEntity().getId())
                        .userId(voteEntity.getUserEntity().getId())
                        .voteNum(voteEntity.getVoteNum())
                        .build())
                .collect(Collectors.toList());
    }

    public boolean voteByRsEventId(int rsEventId, VoteDto voteDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(voteDto.getUserId());
        Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(rsEventId);
        if (!optionalUserEntity.isPresent() || !optionalRsEventEntity.isPresent()) {
            return false;
        }
        UserEntity userEntity = optionalUserEntity.get();
        if (userEntity.getVotes() < voteDto.getVoteNum()) {
            return false;
        }
        userEntity.setVotes(userEntity.getVotes() - voteDto.getVoteNum());
        userRepository.save(userEntity);
        RsEventEntity rsEventEntity = optionalRsEventEntity.get();
        rsEventEntity.setVoteNum(rsEventEntity.getVoteNum() + voteDto.getVoteNum());
        rsEventRepository.save(rsEventEntity);
        VoteEntity voteEntity = VoteEntity.builder()
                .rsEventEntity(optionalRsEventEntity.get())
                .voteDate(voteDto.getVoteDate())
                .voteNum(voteDto.getVoteNum())
                .userEntity(optionalUserEntity.get())
                .build();
        voteRepository.save(voteEntity);
        return true;
    }
}
