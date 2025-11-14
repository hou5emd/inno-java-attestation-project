package ru.inno.attestation.attestation03.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.inno.attestation.attestation03.TestcontainersConfiguration;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.repositories.TrainingEventRepository;
import ru.inno.attestation.attestation03.repositories.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers
@Transactional
@DisplayName("TrainingEventService tests")
class TrainingEventServiceTest {

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

        owner = userService.create(new UserCreateRequestDto(
                "Иван",
                "Иванов",
                UserRole.TRAINER,
                "ivan_trainer_" + System.currentTimeMillis(),
                "password123"
        ));
        student = userService.create(new UserCreateRequestDto(
                "Петр",
                "Петров",
                UserRole.STUDENT,
                "petr_student_" + System.currentTimeMillis(),
                "password456"
        ));

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
    @DisplayName("Создание тренировки - успешно")
    void testCreateEvent_Success() {
        // Act
        TrainingEventResponseDto response = trainingEventService.create(validCreateRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(validCreateRequest.description(), response.getDescription());
        assertEquals(validCreateRequest.startDate(), response.getStartDate());
        assertEquals(validCreateRequest.endDate(), response.getEndDate());
        assertEquals(owner.id(), response.getOwner().id());
        assertEquals(1, response.getStudents().size());
    }

    @Test
    @DisplayName("Получение тренировки по ID - успешно")
    void testGetEventById_Success() {
        // Arrange
        TrainingEventResponseDto created = trainingEventService.create(validCreateRequest);

        // Act
        TrainingEventResponseDto response = trainingEventService.getById(created.getId());

        // Assert
        assertNotNull(response);
        assertEquals(created.getId(), response.getId());
        assertEquals(validCreateRequest.description(), response.getDescription());
    }

    @Test
    @DisplayName("Получение тренировки по ID - не найдена")
    void testGetEventById_NotFound() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trainingEventService.getById(9999L);
        });

        assertTrue(exception.getMessage().contains("не найдена"));
    }

    @Test
    @DisplayName("Обновление тренировки - успешно")
    void testUpdateEvent_Success() {
        // Arrange
        TrainingEventResponseDto created = trainingEventService.create(validCreateRequest);

        // Act
        TrainingEventResponseDto response = trainingEventService.update(created.getId(), updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(updateRequest.description(), response.getDescription());
        assertEquals(updateRequest.startDate(), response.getStartDate());
        assertEquals(updateRequest.endDate(), response.getEndDate());
        assertEquals(0, response.getStudents().size());
    }

    @Test
    @DisplayName("Удаление тренировки - успешно")
    void testDeleteEvent_Success() {
        // Arrange
        TrainingEventResponseDto created = trainingEventService.create(validCreateRequest);

        // Act
        trainingEventService.delete(created.getId());

        // Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trainingEventService.getById(created.getId());
        });

        assertTrue(exception.getMessage().contains("не найдена"));
    }

    @Test
    @DisplayName("Список тренировок - успешно")
    void testListEvents_Success() {
        // Arrange
        trainingEventService.create(validCreateRequest);

        TrainingEventListRequestDto request = new TrainingEventListRequestDto();
        request.setPage(0);
        request.setPageSize(10);

        // Act
        TrainingEventListResponseDto response = trainingEventService.list(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(1, response.getTotalCount());
    }

    @Test
    @DisplayName("Список тренировок - пустой")
    void testListEvents_Empty() {
        // Arrange
        TrainingEventListRequestDto request = new TrainingEventListRequestDto();
        request.setPage(0);
        request.setPageSize(10);

        // Act
        TrainingEventListResponseDto response = trainingEventService.list(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getItems().size());
        assertEquals(0, response.getTotalCount());
    }
}
