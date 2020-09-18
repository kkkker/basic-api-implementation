package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class VoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    VoteRepository voteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setUp() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_get_voting_record_by_start_time_and_end_time() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("小王")
                .age(23)
                .gender("male")
                .email("asda@tue.com")
                .phone("15245852396")
                .votes(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股市崩了")
                .userEntity(userEntity)
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteEntity voteEntity = VoteEntity.builder()
                .voteDate(10000000L)
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .voteNum(4)
                .build();

        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteDate(System.currentTimeMillis())
                .userEntity(userEntity)
                .rsEventEntity(rsEventEntity)
                .voteNum(4)
                .build();

        voteRepository.save(voteEntity);
        mockMvc.perform(get("/rs/vote")
                .param("start", "10000000000")
                .param("end", String.valueOf(System.currentTimeMillis())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].voteNum", is(voteEntity.getVoteNum())))
                .andExpect(jsonPath("$[0].voteDate", is(voteEntity.getVoteDate())))
                .andExpect(jsonPath("$[0].userId", is(voteEntity.getUserEntity().getId())));
    }

    @Test
    void should_add_vote() throws Exception {

        UserEntity userEntity = UserEntity.builder()
                .userName("小王")
                .age(23)
                .gender("male")
                .email("asda@tue.com")
                .phone("15245852396")
                .votes(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股市崩了")
                .userEntity(userEntity)
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteDto voteDto = new VoteDto(4, rsEventEntity.getId(), userEntity.getId(), System.currentTimeMillis());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/rs/vote/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<VoteEntity> voteEntityList = voteRepository.findAll();
        assertEquals(1, voteEntityList.size());
        assertEquals(voteDto.getUserId(), voteEntityList.get(0).getUserEntity().getId());
        assertEquals(voteDto.getVoteNum(), voteEntityList.get(0).getVoteNum());
        assertEquals(voteDto.getVoteDate(), voteEntityList.get(0).getVoteDate());

        userEntity = userRepository.findById(voteDto.getUserId()).orElse(null);
        assert userEntity != null;
        assertEquals(6, userEntity.getVotes());
        rsEventEntity = rsEventRepository.findById(voteDto.getRsEventId()).orElse(null);
        assert rsEventEntity != null;
        assertEquals(4, rsEventEntity.getVoteNum());
    }

    @Test
    void should_not_add_vote_when_voteNum_more_than_votes_of_user() throws Exception {

        UserEntity userEntity = UserEntity.builder()
                .userName("小王")
                .age(23)
                .gender("male")
                .email("asda@tue.com")
                .phone("15245852396")
                .votes(10)
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股市崩了")
                .userEntity(userEntity)
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteDto voteDto = new VoteDto(15, rsEventEntity.getId(), userEntity.getId(), System.currentTimeMillis());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/rs/vote/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<VoteEntity> voteEntityList = voteRepository.findAll();
        assertEquals(0, voteEntityList.size());
    }
}