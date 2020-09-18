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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setUp() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void should_register_user() throws Exception {
        User user = new User("小王", 19, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserEntity> userEntityList = userRepository.findAll();
        assertEquals(1, userEntityList.size());
        assertEquals("小王", userEntityList.get(0).getUserName());
        assertEquals(19, userEntityList.get(0).getAge());
        assertEquals("female", userEntityList.get(0).getGender());
        assertEquals("a@twu.com", userEntityList.get(0).getEmail());
        assertEquals("18888888888", userEntityList.get(0).getPhone());
        assertEquals(10, userEntityList.get(0).getVotes());
    }

    @Test
    void user_name_length_should_no_empty() throws Exception {
        User user = new User("", 19, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_name_should_no_more_than_eight() throws Exception {
        User user = new User("123456789", 19, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void gender_should_no_empty() throws Exception {
        User user = new User("小王", 19, "", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_no_empty() throws Exception {
        User user = new User("小王", null, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_no_more_than_one_hundred() throws Exception {
        User user = new User("小王", 101, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void age_should_no_less_than_eighteen() throws Exception {
        User user = new User("小王", 17, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void email_should_legal() throws Exception {
        User user = new User("小王", 19, "female", "@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_no_empty() throws Exception {
        User user = new User("小王", 19, "female", "a@twu.com", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_start_with_one_and_have_eleven_numbers() throws Exception {
        User user = new User("小王", 19, "female", "a@twu.com", "28888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_get_all_users() throws Exception {
        User user = new User("小王", 19, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        user = new User("小李", 20, "male", "b@twu.com", "18888888889");
        json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user_name", is("小王")))
                .andExpect(jsonPath("$[0].user_age", is(19)))
                .andExpect(jsonPath("$[0].user_gender", is("female")))
                .andExpect(jsonPath("$[0].user_email", is("a@twu.com")))
                .andExpect(jsonPath("$[0].user_phone", is("18888888888")))
                .andExpect(jsonPath("$[1].user_name", is("小李")))
                .andExpect(jsonPath("$[1].user_age", is(20)))
                .andExpect(jsonPath("$[1].user_gender", is("male")))
                .andExpect(jsonPath("$[1].user_email", is("b@twu.com")))
                .andExpect(jsonPath("$[1].user_phone", is("18888888889")));
    }

    @Test
    void should_get_user_by_id() throws Exception {
        User user = new User("小王", 19, "female", "a@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(get("/user/1").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name", is("小王")))
                .andExpect(jsonPath("$.user_age", is(19)))
                .andExpect(jsonPath("$.user_gender", is("female")))
                .andExpect(jsonPath("$.user_email", is("a@twu.com")))
                .andExpect(jsonPath("$.user_phone", is("18888888888")));
    }

    @Test
    void should_delete_user_and_events_of_user_when_delete_user_by_id() throws Exception {
        UserEntity userEntity = UserEntity.builder()
                .userName("小王")
                .age(23)
                .gender("male")
                .email("asda@tue.com")
                .phone("15245852396")
                .build();
        userRepository.save(userEntity);

        String json = "{\"eventName\":\"股市崩了\",\"keyword\":\"经济\",\"user_id\":\"" + userEntity.getId() + "\"}";
        mockMvc.perform(post("/rs/add/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(delete("/delete/user/" + userEntity.getId()))
                .andExpect(status().isOk());

        List<UserEntity> userEntities = userRepository.findAll();
        assertEquals(0, userEntities.size());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(0, rsEventEntities.size());
    }

    @Test
    void should_checkout_user_when_add() throws Exception {

        User user = new User("小王", 19, "female", "@twu.com", "18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/user/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }
}