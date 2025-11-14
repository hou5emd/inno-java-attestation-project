package ru.inno.attestation.attestation03.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ru.inno.attestation.attestation03.TestcontainersConfiguration;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.repositories.TrainingEventRepository;
import ru.inno.attestation.attestation03.repositories.UserRepository;
import ru.inno.attestation.attestation03.services.TrainingEventService;
import ru.inno.attestation.attestation03.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("TrainingEventResource (Controller) tests")
class TrainingEventResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainingEventService trainingEventService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingEventRepository trainingEventRepository;

    @Autowired
    private UserRepository userRepository;

    private UserCreateResponseDto owner;
    private UserCreateResponseDto student;
    private TrainingEventCreateRequestDto validCreateRequest;
    private TrainingEventUpdateRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        trainingEventRepository.deleteAll();
        userRepository.deleteAll();

        UserCreateRequestDto ownerRequest = new UserCreateRequestDto(
                "Иван",
                "Иванов",
                UserRole.TRAINER,
                "ivan_trainer_" + System.currentTimeMillis(),
                "password123"
        );
        UserCreateRequestDto studentRequest = new UserCreateRequestDto(
                "Петр",
                "Петров",
                UserRole.STUDENT,
                "petr_student_" + System.currentTimeMillis(),
                "password456"
        );

        owner = userService.create(ownerRequest);
        student = userService.create(studentRequest);

        validCreateRequest = new TrainingEventCreateRequestDto(
                "Тренировка по Java",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                owner.id(),
                List.of(student.id())
        );

        updateRequest = new TrainingEventUpdateRequestDto(
                "Обновленная тренировка",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1),
                List.of()
        );
    }

    @Test
    @DisplayName("POST /api/v1/events/list - получение списка тренировок")
    void testListEvents_Success() throws Exception {
        // Arrange
        trainingEventService.create(validCreateRequest);

        TrainingEventListRequestDto request = new TrainingEventListRequestDto();
        request.setPage(0);
        request.setPageSize(10);

        // Act & Assert
        mockMvc.perform(post("/api/v1/events/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.totalCount", equalTo(1)));
    }

    @Test
    @DisplayName("POST /api/v1/events/list - пустой список тренировок")
    void testListEvents_Empty() throws Exception {
        TrainingEventListRequestDto request = new TrainingEventListRequestDto();
        request.setPage(0);
        request.setPageSize(10);

        // Act & Assert
        mockMvc.perform(post("/api/v1/events/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.totalCount", equalTo(0)));
    }

    @Test
    @DisplayName("POST /api/v1/events - создание тренировки")
    void testCreateEvent_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", equalTo(validCreateRequest.description())))
                .andExpect(jsonPath("$.owner.id", equalTo(validCreateRequest.ownerId().intValue())));
    }

    @Test
    @DisplayName("GET /api/v1/events/{id} - получение тренировки по ID")
    void testGetEventById_Success() throws Exception {
        // Arrange
        TrainingEventResponseDto created = trainingEventService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(get("/api/v1/events/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(created.getId().intValue())))
                .andExpect(jsonPath("$.description", equalTo(validCreateRequest.description())));
    }

    @Test
    @DisplayName("GET /api/v1/events/{id} - тренировка не найдена")
    void testGetEventById_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/events/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/v1/events/{id} - обновление тренировки")
    void testUpdateEvent_Success() throws Exception {
        // Arrange
        TrainingEventResponseDto created = trainingEventService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/events/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", equalTo(updateRequest.description())));
    }

    @Test
    @DisplayName("POST /api/v1/events/{id} - удаление тренировки")
    void testDeleteEvent_Success() throws Exception {
        // Arrange
        TrainingEventResponseDto created = trainingEventService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(post("/api/v1/events/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
