package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void should_get_one_rs_event() throws Exception {
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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(get("/rs/event/" + rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("股市崩了")))
                .andExpect(jsonPath("$.keyword", is("经济")));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);
        rsEventEntity = RsEventEntity.builder()
                .eventName("猪肉涨价了")
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        mockMvc.perform(get("/rs/event?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("股市崩了")))
                .andExpect(jsonPath("$[0].keyword", is("经济")))
                .andExpect(jsonPath("$[1].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[1].keyword", is("经济")));
    }

    @Test
    void should_get_one_rs_event_without_user() throws Exception {
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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);
        mockMvc.perform(get("/rs/event/" + rsEventEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(hasKey("user"))));
    }

    @Test
    void should_add_one_rs_event() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("小王")
                .age(23)
                .gender("male")
                .email("asda@tue.com")
                .phone("15245852396")
                .build();
        userRepository.save(userEntity);

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(0, rsEventEntities.size());

        String json = "{\"eventName\":\"股市崩了\",\"keyword\":\"经济\",\"user_id\":\"" + userEntity.getId() + "\"}";
        mockMvc.perform(post("/rs/add/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index",
                        is(String.valueOf(rsEventRepository.findAll().indexOf(new RsEventEntity("股市崩了",
                                "经济",
                                userEntity.getId())) + 1))));

        rsEventEntities = rsEventRepository.findAll();
        assertEquals(1, rsEventEntities.size());
        assertEquals("股市崩了", rsEventEntities.get(0).getEventName());
        assertEquals("经济", rsEventEntities.get(0).getKeyword());
        assertEquals(userEntity.getId(), rsEventEntities.get(0).getUserId());
    }

    @Test
    void should_not_add_rs_event_when_user_not_exist() throws Exception {
        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(0, rsEventEntities.size());

        String json = "{\"eventName\":\"股市崩了\",\"keyword\":\"经济\",\"user_id\":\"1\"}";
        mockMvc.perform(post("/rs/add/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        rsEventEntities = rsEventRepository.findAll();
        assertEquals(0, rsEventEntities.size());
    }

    @Test
    void should_update_rs_event_by_index() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"民生\",\"user_id\":\"" + userEntity.getId() + "\"}";
        mockMvc.perform(put("/rs/update/event/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(1, rsEventEntities.size());
        assertEquals("猪肉涨价了", rsEventEntities.get(0).getEventName());
        assertEquals("民生", rsEventEntities.get(0).getKeyword());
        assertEquals(userEntity.getId(), rsEventEntities.get(0).getUserId());
    }

    @Test
    void should_update_rs_event_by_index_with_event_name() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        String json = "{\"eventName\":\"股市涨了\",\"user_id\":\"" + userEntity.getId() + "\"}";
        mockMvc.perform(put("/rs/update/event/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(1, rsEventEntities.size());
        assertEquals("股市涨了", rsEventEntities.get(0).getEventName());
        assertEquals("经济", rsEventEntities.get(0).getKeyword());
        assertEquals(userEntity.getId(), rsEventEntities.get(0).getUserId());
    }

    @Test
    void should_update_rs_event_by_index_with_keyword() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        String json = "{\"keyword\":\"民生\",\"user_id\":\"" + userEntity.getId() + "\"}";
        mockMvc.perform(put("/rs/update/event/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(1, rsEventEntities.size());
        assertEquals("股市崩了", rsEventEntities.get(0).getEventName());
        assertEquals("民生", rsEventEntities.get(0).getKeyword());
        assertEquals(userEntity.getId(), rsEventEntities.get(0).getUserId());
    }

    @Test
    void should_not_update_rs_event_by_wrong_user_id() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"民生\",\"user_id\":\"" + 100 + "\"}";
        mockMvc.perform(put("/rs/update/event/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_update_rs_event_by_empty_user_id() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        String json = "{\"eventName\":\"猪肉涨价了\",\"keyword\":\"民生\"}";
        mockMvc.perform(put("/rs/update/event/" + rsEventEntity.getId())
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_delete_rs_event_by_index() throws Exception {

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
                .userId(userEntity.getId())
                .keyword("经济")
                .build();
        rsEventRepository.save(rsEventEntity);

        assertEquals(1, rsEventRepository.findAll().size());
        mockMvc.perform(delete("/rs/delete/event/" + rsEventEntity.getId()))
                .andExpect(status().isOk());

        assertEquals(0, rsEventRepository.findAll().size());
    }

    @Test
    void should_not_delete_rs_event_by_wrong_index() throws Exception {
        mockMvc.perform(delete("/rs/delete/event/4"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_checkout_start_and_end_when_get_rs_event_by_range() throws Exception {
        mockMvc.perform(get("/rs/event?start=1&end=20"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_checkout_index_when_get_rs_event_by_index() throws Exception {
        mockMvc.perform(get("/rs/event/4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

}
