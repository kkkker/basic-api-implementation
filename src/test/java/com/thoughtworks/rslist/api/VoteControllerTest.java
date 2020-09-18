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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        VoteDto voteDto = new VoteDto(5, rsEventEntity.getId(), userEntity.getId(), "current time");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/rs/vote/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<VoteEntity> voteEntityList = voteRepository.findAll();
        assertEquals(1, voteEntityList.size());
        assertEquals(voteDto.getUserId(), voteEntityList.get(0).getUserEntity().getId());
        assertEquals(voteDto.getVoteNum(), voteEntityList.get(0).getVoteNum());
        assertEquals(voteDto.getVoteTime(), voteEntityList.get(0).getVoteTime());
    }

    @Test
    void should_not_add_vote_when_voteNum_more_than_votes_of_user() throws Exception {

        UserEntity userEntity = UserEntity.builder()
                .userName("小王")
                .age(23)
                .gender("male")
                .email("asda@tue.com")
                .phone("15245852396")
                .build();
        userRepository.save(userEntity);

        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName("股市崩了")
                .userEntity(userEntity)
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteDto voteDto = new VoteDto(15, rsEventEntity.getId(), userEntity.getId(), "current time");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/rs/vote/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<VoteEntity> voteEntityList = voteRepository.findAll();
        assertEquals(0, voteEntityList.size());
    }
}