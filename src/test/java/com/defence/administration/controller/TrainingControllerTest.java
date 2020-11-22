package com.defence.administration.controller;

import com.defence.administration.dto.MemberDto;
import com.defence.administration.dto.TrainingDto;
import com.defence.administration.enums.TrainingType;
import com.defence.administration.model.Member;
import com.defence.administration.model.Training;
import com.defence.administration.repository.TrainingRepository;
import com.defence.administration.service.MemberService;
import com.defence.administration.service.TrainingService;
import com.defence.administration.utils.DtoUtils;
import com.defence.administration.utils.EntityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.EnumSet;
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
public class TrainingControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private DtoUtils dtoUtils;

    @Autowired
    private EntityUtils entityUtils;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TrainingService trainingService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @After
    public void tearDown() {
        trainingRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        Training training1 = entityUtils.createTraining();
        entityUtils.createMember(training1.getId());
        entityUtils.createMember(training1.getId());
        Training training2 = entityUtils.createTraining();
        entityUtils.createMember(training2.getId());
        entityUtils.createMember(training2.getId());

        MvcResult result = mvc.perform(get("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<TrainingDto> trainings = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<TrainingDto>>() {
        });
        assertThat(trainings.stream().anyMatch(t -> t.getId().equals(training1.getId()))).isEqualTo(true);
        assertThat(trainings.stream().anyMatch(t -> t.getId().equals(training2.getId()))).isEqualTo(true);
    }

    @Test
    public void testGetById() throws Exception {
        Training training1 = entityUtils.createTraining();
        entityUtils.createMember(training1.getId());
        entityUtils.createMember(training1.getId());
        Training training2 = entityUtils.createTraining();
        entityUtils.createMember(training2.getId());
        entityUtils.createMember(training2.getId());

        MvcResult result = mvc.perform(get("/trainings/" + training1.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        TrainingDto dto = objectMapper.readValue(result.getResponse().getContentAsString(), TrainingDto.class);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(training1.getId());
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(trainingRepository.findById(id)).isNotPresent();
        mvc.perform(get("/trainings/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void testPost() throws Exception {
        TrainingDto dto = dtoUtils.createTrainingDto();
        dto.getMembers().add(memberService.map(entityUtils.createMember()));
        dto.getMembers().add(memberService.map(entityUtils.createMember()));
        String request = objectMapper.writeValueAsString(dto);
        Long count = trainingRepository.count();

        MvcResult result = mvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated()).andReturn();

        assertThat(trainingRepository.count()).isEqualTo(count + 1);
        Training entity = trainingRepository.findById(Long.valueOf(result.getResponse().getHeader("Location"))).get();
        assertThat(entity.getTrainerName()).isEqualTo(dto.getTrainerName());
        assertThat(entity.getSenderName()).isEqualTo(dto.getSenderName());
        assertThat(entity.getType()).isEqualTo(dto.getType());
    }

    @Test
    public void testPostWithNonExistingMember() throws Exception {
        TrainingDto dto = dtoUtils.createTrainingDto();
        dto.getMembers().add(memberService.map(entityUtils.createMember()));
        MemberDto memberDto = dtoUtils.createMemberDto();
        memberDto.setId(Long.MAX_VALUE);
        dto.getMembers().add(memberDto);
        String request = objectMapper.writeValueAsString(dto);

        Long count = trainingRepository.count();
        int nrMembers = memberService.findAll().size();

        mvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());

        assertThat(trainingRepository.count()).isEqualTo(count + 1);
        assertThat(memberService.findAll().size()).isEqualTo(nrMembers);
    }

    @Test
    public void testPostWithId() throws Exception {
        TrainingDto dto = dtoUtils.createTrainingDto();
        dto.setId(1L);
        String request = objectMapper.writeValueAsString(dto);
        Long count = trainingRepository.count();

        mvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());

        assertThat(trainingRepository.count()).isEqualTo(count);
    }

    @Test
    public void testGetTrainingsOfMember() throws Exception {
        Member member = entityUtils.createMember();
        Training training1 = entityUtils.createTraining();
        training1.getMembers().add(member);
        training1.getMembers().add(entityUtils.createMember());
        training1.getMembers().add(entityUtils.createMember());
        Training training2 = entityUtils.createTraining();
        training2.getMembers().add(member);

        MvcResult result = mvc.perform(get("/member/" + member.getId() + "/trainings")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        List<TrainingDto> trainingDtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<TrainingDto>>() {
        });
        assertThat(trainingDtos.stream().allMatch(t -> t.getMembers().stream().anyMatch(m -> m.getId().equals(member.getId())))).isEqualTo(true);
    }

    @Test
    public void testGetTrainingsOfMemberNotFound() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(trainingService.findMemberById(id)).isNotPresent();
        mvc.perform(get("/member/" + id + "/trainings")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testPutTraining() throws Exception {
        Training training = entityUtils.createTraining();
        TrainingDto trainingDto = trainingService.map(training);
        MemberDto memberDto = dtoUtils.createMemberDto();
        memberDto.setId(Long.MAX_VALUE);
        assertThat(trainingRepository.findById(memberDto.getId()).isPresent()).isEqualTo(false);
        trainingDto.getMembers().add(memberDto);
        trainingDto.setTrainerName(trainingDto.getTrainerName() + 1);
        String request = objectMapper.writeValueAsString(trainingDto);
        Long count = trainingRepository.count();

        mvc.perform(put("/trainings/" + trainingDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(trainingRepository.findById(trainingDto.getId())).isPresent();
        Training entity = trainingRepository.findById(trainingDto.getId()).get();
        assertThat(trainingRepository.count()).isEqualTo(count);
        assertThat(entity.getTrainerName()).isEqualTo(trainingDto.getTrainerName());
        assertThat(entity.getSenderName()).isEqualTo(trainingDto.getSenderName());
        assertThat(entity.getType()).isEqualTo(trainingDto.getType());
    }

    @Test
    public void testPutTrainingWithNonExistingId() throws Exception {
        TrainingDto trainingDto = dtoUtils.createTrainingDto();
        String request = objectMapper.writeValueAsString(trainingDto);
        Long nonExistingId = Long.MAX_VALUE;
        assertThat(trainingService.findById(nonExistingId)).isNotPresent();
        Long count = trainingRepository.count();

        mvc.perform(put("/trainings/" + nonExistingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isNotFound()).andReturn();

        assertThat(trainingRepository.count()).isEqualTo(count);
        assertThat(trainingService.findById(nonExistingId)).isNotPresent();
    }

    @Test
    public void testDeleteTraining() throws Exception {
        Training training = entityUtils.createTraining();
        Long count = trainingRepository.count();
        mvc.perform(delete("/trainings/" + training.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent()).andReturn();

        assertThat(trainingRepository.count()).isEqualTo(count - 1);
        assertThat(trainingRepository.findById(training.getId())).isNotPresent();
    }

    @Test
    public void testDeleteTrainingWithNonExistingId() throws Exception {
        Long id = Long.MAX_VALUE;
        assertThat(trainingService.findById(id)).isNotPresent();
        Long count = trainingRepository.count();

        mvc.perform(delete("/trainings/" + id)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        assertThat(trainingRepository.count()).isEqualTo(count);
        assertThat(trainingRepository.findById(id).isPresent()).isEqualTo(false);
    }

    @Test
    public void testGetTrainingTypes() throws Exception {
        MvcResult result = mvc.perform(get("/trainings/types")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        
        List<TrainingType> types = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<TrainingType>>() {
        });
        assertThat(types).isEqualTo(new ArrayList<>(EnumSet.allOf(TrainingType.class)));
    }

}
