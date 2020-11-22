package com.defence.administration.controller;

import com.defence.administration.dto.MemberDto;
import com.defence.administration.model.Member;
import com.defence.administration.repository.MemberRepository;
import com.defence.administration.service.MemberService;
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
public class MemberControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DtoUtils dtoUtils;

    @Autowired
    private EntityUtils entityUtils;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @After
    public void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        Member entity1 = entityUtils.createMember();
        Member entity2 = entityUtils.createMember();
        MvcResult result = mvc.perform(get("/members")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<MemberDto> members = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<MemberDto>>() {
        });

        assertThat(members.stream().anyMatch(m -> m.getId().equals(entity1.getId()))).isEqualTo(true);
        assertThat(members.stream().anyMatch(m -> m.getId().equals(entity2.getId()))).isEqualTo(true);
    }

    @Test
    public void testGetById() throws Exception {
        Member entity = entityUtils.createMember();
        entityUtils.createMember();
        MvcResult result = mvc.perform(get("/members/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MemberDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), MemberDto.class);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(memberRepository.findById(id)).isNotPresent();
        mvc.perform(get("/members/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testPost() throws Exception {
        MemberDto dto = dtoUtils.createMemberDto();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(dto);
        Long count = memberRepository.count();

        MvcResult result = mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated()).andReturn();

        assertThat(memberRepository.count()).isEqualTo(count + 1);
        Member entity = memberRepository.findById(Long.valueOf(result.getResponse().getHeader("Location"))).get();
        assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(entity.getMiddleName()).isEqualTo(dto.getMiddleName());
        assertThat(entity.getLastName()).isEqualTo(dto.getLastName());
        assertThat(entity.getPaidUntil()).isEqualTo(dto.getPaidUntil());
        assertThat(entity.getIBAN()).isEqualTo(dto.getIBAN());
        assertThat(entity.getStreetWithNr()).isEqualTo(dto.getStreetWithNr());
        assertThat(entity.getZipCode()).isEqualTo(dto.getZipCode());
        assertThat(entity.getResidence()).isEqualTo(dto.getResidence());
        assertThat(entity.getDateOfBirth()).isEqualTo(dto.getDateOfBirth());
        assertThat(entity.getPhoneHome()).isEqualTo(dto.getPhoneHome());
        assertThat(entity.getPhoneMobile()).isEqualTo(dto.getPhoneMobile());
        assertThat(entity.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    public void testPostWithoutFirstName() throws Exception {
        MemberDto dto = dtoUtils.createMemberDto();
        dto.setFirstName(null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(dto);
        Long count = memberRepository.count();

        mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(count);
    }
    
    @Test
    public void testPostWithId() throws Exception{
        MemberDto dto = dtoUtils.createMemberDto();
        dto.setId(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(dto);

        Long count = memberRepository.count();

        mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());

        assertThat(memberRepository.count()).isEqualTo(count);
        
    }

    @Test
    public void testPutMember() throws Exception {
        Member member = entityUtils.createMember();
        MemberDto memberDto = memberService.map(member);
        String firstName = memberDto.getFirstName() + 1;
        memberDto.setFirstName(firstName);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(memberDto);
        Long count = memberRepository.count();

        mvc.perform(put("/members/" + memberDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(memberRepository.findById(memberDto.getId())).isPresent();
        Member entity = memberRepository.findById(memberDto.getId()).get();
        assertThat(memberRepository.count()).isEqualTo(count);
        assertThat(entity.getFirstName()).isEqualTo(memberDto.getFirstName());
        assertThat(entity.getMiddleName()).isEqualTo(memberDto.getMiddleName());
        assertThat(entity.getLastName()).isEqualTo(memberDto.getLastName());
    }

    @Test
    public void testPutMemberWithNonExistingId() throws Exception {
        MemberDto memberDto = dtoUtils.createMemberDto();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(memberDto);
        Long nonExistingId = Long.MAX_VALUE;
        assertThat(memberService.findById(nonExistingId)).isNotPresent();
        Long count = memberRepository.count();

        mvc.perform(put("/members/" + nonExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isNotFound()).andReturn();

        assertThat(memberRepository.count()).isEqualTo(count);
    }

    @Test
    public void testDelete() throws Exception {
        Member member = entityUtils.createMember();
        Long count = memberRepository.count();
        
        mvc.perform(delete("/members/" + member.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(memberRepository.count()).isEqualTo(count - 1);
        assertThat(memberRepository.findById(member.getId())).isNotPresent();
    }

    @Test
    public void testDeleteWithNonExistingId() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(memberService.findById(id)).isNotPresent();
        Long count = memberRepository.count();

        mvc.perform(delete("/members/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        assertThat(memberRepository.count()).isEqualTo(count);
    }

}
