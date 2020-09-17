package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoteController {

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity<Object> voteByRsId(@PathVariable int rsEventId, @RequestBody VoteDto voteDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(voteDto.getUserId());
        if (!optionalUserEntity.isPresent()) {
            return ResponseEntity.status(400).build();
        }
        UserEntity userEntity = optionalUserEntity.get();
        if (userEntity.getVotes() < voteDto.getVoteNum()) {
            return ResponseEntity.status(400).build();
        }
        userEntity.setVotes(userEntity.getVotes() - voteDto.getVoteNum());
        userRepository.save(userEntity);
        VoteEntity voteEntity = VoteEntity.builder()
                .rsEventId(rsEventId)
                .voteTime(voteDto.getVoteTime())
                .voteNum(voteDto.getVoteNum())
                .userId(voteDto.getUserId())
                .build();
        voteRepository.save(voteEntity);
        return ResponseEntity.ok().build();
    }
}
