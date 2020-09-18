package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
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
    VoteService voteService;

    @GetMapping("/rs/votes")
    ResponseEntity<List<VoteDto>> getVotingRecord(@RequestParam Long start,
                                                  @RequestParam Long end) {
        List<VoteDto> voteDtoList = voteService.getVotingRecordByDateRange(start, end);
        return  ResponseEntity.ok().body(voteDtoList);
    }

    @GetMapping("/rs/vote/{rsEventId}")
    ResponseEntity<List<VoteDto>> getVotingRecord(@PathVariable int rsEventId) {
        List<VoteDto> voteDtoList = voteService.getVotingRecordByRsEventId(rsEventId);
        return  ResponseEntity.ok().body(voteDtoList);
    }

    @GetMapping("/user/vote/{userId}")
    ResponseEntity<List<VoteDto>> getVotingRecordByUserId(@PathVariable int userId) {
        List<VoteDto> voteDtoList = voteService.getVotingRecordByUserId(userId);
        return  ResponseEntity.ok().body(voteDtoList);
    }

    @PostMapping("/rs/vote/{rsEventId}")
    ResponseEntity<Object> voteByRsEventId(@PathVariable int rsEventId, @RequestBody VoteDto voteDto) {
        if (!voteService.voteByRsEventId(rsEventId, voteDto)) {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok().build();
    }
}
