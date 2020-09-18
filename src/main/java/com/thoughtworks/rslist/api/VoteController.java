package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class VoteController {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @GetMapping("/rs/votes")
    ResponseEntity<List<VoteDto>> getVotingRecord(@RequestParam Long start,
                                                  @RequestParam Long end) {
        List<VoteEntity> voteEntityList = voteRepository.findAllByVoteDateBetween(start, end);
        List<VoteDto> voteDtoList = voteEntityList.stream()
                .map(voteEntity -> VoteDto.builder()
                        .voteDate(voteEntity.getVoteDate())
                        .rsEventId(voteEntity.getRsEventEntity().getId())
                        .userId(voteEntity.getUserEntity().getId())
                        .voteNum(voteEntity.getVoteNum())
                        .build())
                .collect(Collectors.toList());
        return  ResponseEntity.ok().body(voteDtoList);
    }

    @GetMapping("/rs/vote/{rsEventId}")
    ResponseEntity<List<VoteDto>> getVotingRecord(@PathVariable int rsEventId) {
        List<VoteEntity> voteEntityList = voteRepository.findAll();
        List<VoteDto> voteDtoList = voteEntityList.stream()
                .filter(voteEntity -> voteEntity.getRsEventEntity().getId() == rsEventId)
                .map(voteEntity -> VoteDto.builder()
                        .voteDate(voteEntity.getVoteDate())
                        .rsEventId(voteEntity.getRsEventEntity().getId())
                        .userId(voteEntity.getUserEntity().getId())
                        .voteNum(voteEntity.getVoteNum())
                        .build())
                .collect(Collectors.toList());
        return  ResponseEntity.ok().body(voteDtoList);
    }

    @GetMapping("/user/vote/{userId}")
    ResponseEntity<List<VoteDto>> getVotingRecordByUserId(@PathVariable int userId) {
        List<VoteEntity> voteEntityList = voteRepository.findAll();
        List<VoteDto> voteDtoList = voteEntityList.stream()
                .filter(voteEntity -> voteEntity.getUserEntity().getId() == userId)
                .map(voteEntity -> VoteDto.builder()
                        .voteDate(voteEntity.getVoteDate())
                        .rsEventId(voteEntity.getRsEventEntity().getId())
                        .userId(voteEntity.getUserEntity().getId())
                        .voteNum(voteEntity.getVoteNum())
                        .build())
                .collect(Collectors.toList());
        return  ResponseEntity.ok().body(voteDtoList);
    }

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity<Object> voteByRsId(@PathVariable int rsEventId, @RequestBody VoteDto voteDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(voteDto.getUserId());
        Optional<RsEventEntity> optionalRsEventEntity = rsEventRepository.findById(rsEventId);
        if (!optionalUserEntity.isPresent() || !optionalRsEventEntity.isPresent()) {
            return ResponseEntity.status(400).build();
        }
        UserEntity userEntity = optionalUserEntity.get();
        if (userEntity.getVotes() < voteDto.getVoteNum()) {
            return ResponseEntity.status(400).build();
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
        return ResponseEntity.ok().build();
    }
}
