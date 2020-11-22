package com.defence.administration.controller;

import com.defence.administration.dto.UserDto;
import com.defence.administration.model.User;
import com.defence.administration.repository.UserRepository;
import com.defence.administration.service.UserService;
import com.defence.administration.utils.DtoUtils;
import com.defence.administration.utils.EntityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private DtoUtils dtoUtils;

    @Autowired
    private EntityUtils entityUtils;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testIsUser() throws Exception {
        User entity = new User();
        entity.setUsername("Bramm");
        entity.setPassword(bCryptPasswordEncoder.encode("Timmm"));
        userRepository.save(entity);

        // matches identically
        UserDto dto = new UserDto();
        dto.setUsername("Bramm");
        dto.setPassword("Timmm");
        Boolean isUser = userService.isUser(dto);
        assertThat(isUser).isEqualTo(true);

        // machtes without case
        UserDto dto2 = new UserDto();
        dto2.setUsername("bramm");
        dto2.setPassword("Timmm");
        Boolean isUser2 = userService.isUser(dto2);
        assertThat(isUser2).isEqualTo(true);

        // wrong password
        UserDto dto3 = new UserDto();
        dto3.setUsername("Bramm");
        dto3.setPassword("pwpwpw");
        Boolean isUser3 = userService.isUser(dto3);
        assertThat(isUser3).isEqualTo(false);

        // non existing user
        UserDto dto4 = new UserDto();
        dto4.setUsername("Hanss");
        dto4.setPassword("pwpwpw");
        Boolean isUser4 = userService.isUser(dto4);
        assertThat(isUser4).isEqualTo(false);

    }

    @Test
    public void testGetAll() throws Exception {
        User entity1 = entityUtils.createUser();
        User entity2 = entityUtils.createUser();

        MvcResult result = mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<UserDto> users = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<UserDto>>() {
        });

        assertThat(users.stream().anyMatch(u -> u.getId().equals(entity1.getId()))).isEqualTo(true);
        assertThat(users.stream().anyMatch(u -> u.getId().equals(entity2.getId()))).isEqualTo(true);
    }

    @Test
    public void testGetById() throws Exception {
        User entity = entityUtils.createUser();
        entityUtils.createUser();
        MvcResult result = mvc.perform(get("/users/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        UserDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(userRepository.findById(id)).isNotPresent();
        mvc.perform(get("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
    
    @Test
    public void testLogin() throws Exception {
        UserDto dto = dtoUtils.createUserDto();
        userService.create(userService.map(dto));
        
        String request = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isOk()).andReturn();
    }
    
    @Test
    public void testLoginInvalidUser() throws Exception {
        UserDto dto = dtoUtils.createUserDto();
        assertThat(userService.isUser(dto)).isEqualTo(false);
        String request = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest()).andReturn();
    }
    
    
    @Test
    public void testPost() throws Exception {
        UserDto dto = dtoUtils.createUserDto();

        String request = objectMapper.writeValueAsString(dto);
        Long count = userRepository.count();

        MvcResult result = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated()).andReturn();

        assertThat(userRepository.count()).isEqualTo(count + 1);
        assertThat(userService.findUserByUsername(dto.getUsername()));
    }
    
    @Test
    public void testPostDuplicateUsername() throws Exception {
        User user = entityUtils.createUser();
        UserDto dto = dtoUtils.createUserDto();
        dto.setUsername(user.getUsername());
        String request = objectMapper.writeValueAsString(dto);
        Long count = userRepository.count();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());

        assertThat(userRepository.count()).isEqualTo(count);
    }
    
    @Test
    public void testPostWithoutUsername() throws Exception {
        UserDto dto = dtoUtils.createUserDto();
        dto.setUsername(null);
        String request = objectMapper.writeValueAsString(dto);
        Long count = userRepository.count();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());

        assertThat(userRepository.count()).isEqualTo(count);
    }

    @Test
    public void testPostWithId() throws Exception {
        UserDto dto = dtoUtils.createUserDto();
        dto.setId(1L);
        String request = objectMapper.writeValueAsString(dto);
        Long count = userRepository.count();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());

        assertThat(userRepository.count()).isEqualTo(count);

    }

    @Test
    public void testPutUser() throws Exception {
        User user = entityUtils.createUser();
        UserDto userDto = userService.map(user);
        String userName = userDto.getUsername()+ 1;
        userDto.setUsername(userName);
        String userPassword = userDto.getPassword()+ 1;
        userDto.setPassword(userPassword);
        String request = objectMapper.writeValueAsString(userDto);
        Long count = userRepository.count();

        mvc.perform(put("/users/" + userDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(userRepository.findById(userDto.getId())).isPresent();
        User entity = userRepository.findById(userDto.getId()).get();
        assertThat(userRepository.count()).isEqualTo(count);
        assertThat(entity.getUsername()).isEqualTo(userDto.getUsername().toLowerCase());
        assertThat(bCryptPasswordEncoder.matches(userDto.getPassword(), entity.getPassword())).isEqualTo(true);
        
    }

    @Test
    public void testPutUserWithNonExistingId() throws Exception {
        UserDto userDto = dtoUtils.createUserDto();
        String request = objectMapper.writeValueAsString(userDto);
        Long nonExistingId = Long.MAX_VALUE;
        assertThat(userService.findById(nonExistingId)).isNotPresent();
        Long count = userRepository.count();

        mvc.perform(put("/users/" + nonExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isNotFound()).andReturn();

        assertThat(userRepository.count()).isEqualTo(count);
    }
    
    @Test
    public void testPutUserWithInvalidUsername() throws Exception {
        UserDto userDto = dtoUtils.createUserDto();
        userDto.setUsername("bram");
        String request = objectMapper.writeValueAsString(userDto);
        Long nonExistingId = Long.MAX_VALUE;
        assertThat(userService.findById(nonExistingId)).isNotPresent();
        Long count = userRepository.count();

        mvc.perform(put("/users/" + nonExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest()).andReturn();

        assertThat(userRepository.count()).isEqualTo(count);
    }

    @Test
    public void testDelete() throws Exception {
        User user = entityUtils.createUser();
        Long count = userRepository.count();

        mvc.perform(delete("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(userRepository.count()).isEqualTo(count - 1);
        assertThat(userRepository.findById(user.getId())).isNotPresent();
    }

    @Test
    public void testDeleteWithNonExistingId() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(userService.findById(id)).isNotPresent();
        Long count = userRepository.count();

        mvc.perform(delete("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        assertThat(userRepository.count()).isEqualTo(count);
    }
}
