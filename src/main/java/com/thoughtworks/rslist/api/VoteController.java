package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

    @Autowired
    VoteRepository voteRepository;

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity<Object> voteByRsId(@PathVariable int rsEventId, @RequestBody VoteDto voteDto) {
        VoteEntity voteEntity = VoteEntity.builder()
                .voteTime(voteDto.getVoteTime())
                .voteNum(voteDto.getVoteNum())
                .userId(voteDto.getUserId())
                .build();
        voteRepository.save(voteEntity);
        return ResponseEntity.ok().build();
    }
}
