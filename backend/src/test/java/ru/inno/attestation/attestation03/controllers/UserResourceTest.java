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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.inno.attestation.attestation03.TestcontainersConfiguration;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.repositories.UserRepository;
import ru.inno.attestation.attestation03.services.UserService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("UserResource (Controller) tests")
class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserCreateRequestDto validCreateRequest;
    private UserUpdateRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        validCreateRequest = new UserCreateRequestDto(
                "Иван",
                "Иванов",
                UserRole.STUDENT,
                "ivan_ivanov_" + System.currentTimeMillis(),
                "password123"
        );
        updateRequest = new UserUpdateRequestDto(
                "Петр",
                "Петров",
                "petr_petrov",
                "newpassword123"
        );
    }

    @Test
    @DisplayName("GET /api/v1/users - получение списка пользователей")
    void testListUsers_Success() throws Exception {
        // Arrange
        userService.create(validCreateRequest);
        userService.create(new UserCreateRequestDto(
                "Петр",
                "Петров",
                UserRole.TRAINER,
                "petr_petrov_" + System.currentTimeMillis(),
                "password456"
        ));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.totalCount", equalTo(2)));
    }

    @Test
    @DisplayName("GET /api/v1/users - пустой список пользователей")
    void testListUsers_Empty() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.totalCount", equalTo(0)));
    }

    @Test
    @DisplayName("GET /api/v1/users - с сортировкой")
    void testListUsers_WithSorting() throws Exception {
        // Arrange
        userService.create(validCreateRequest);
        userService.create(new UserCreateRequestDto(
                "Александр",
                "Сидоров",
                UserRole.TRAINER,
                "alex_sidorov_" + System.currentTimeMillis(),
                "password789"
        ));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .param("sortField", "firstName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.totalCount", equalTo(2)));
    }

    @Test
    @DisplayName("POST /api/v1/users - создание пользователя")
    void testCreateUser_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", equalTo(validCreateRequest.firstName())))
                .andExpect(jsonPath("$.lastName", equalTo(validCreateRequest.lastName())))
                .andExpect(jsonPath("$.role", equalTo(validCreateRequest.role().toString())))
                .andExpect(jsonPath("$.userName", equalTo(validCreateRequest.userName())));
    }

    @Test
    @DisplayName("POST /api/v1/users - создание пользователя с дублирующимся userName")
    void testCreateUser_DuplicateUserName_Conflict() throws Exception {
        // Arrange
        userService.create(validCreateRequest);

        UserCreateRequestDto duplicateRequest = new UserCreateRequestDto(
                "Другое",
                "Имя",
                UserRole.TRAINER,
                validCreateRequest.userName(),
                "password999"
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - получение пользователя по ID")
    void testGetUserById_Success() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(created.id().intValue())))
                .andExpect(jsonPath("$.firstName", equalTo(validCreateRequest.firstName())))
                .andExpect(jsonPath("$.lastName", equalTo(validCreateRequest.lastName())))
                .andExpect(jsonPath("$.role", equalTo(validCreateRequest.role().toString())))
                .andExpect(jsonPath("$.userName", equalTo(validCreateRequest.userName())));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} - пользователь не найден")
    void testGetUserById_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} - обновление пользователя")
    void testUpdateUser_Success() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(created.id().intValue())))
                .andExpect(jsonPath("$.firstName", equalTo(updateRequest.firstName())))
                .andExpect(jsonPath("$.lastName", equalTo(updateRequest.lastName())))
                .andExpect(jsonPath("$.userName", equalTo(updateRequest.userName())));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} - обновление несуществующего пользователя")
    void testUpdateUser_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} - частичное обновление пользователя")
    void testUpdateUser_PartialUpdate() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);
        UserUpdateRequestDto partialUpdate = new UserUpdateRequestDto(
                "Новое Имя",
                null,
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Новое Имя")))
                .andExpect(jsonPath("$.id", equalTo(created.id().intValue())));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} - обновление только firstName")
    void testUpdateUser_OnlyFirstName() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);
        UserUpdateRequestDto updateOnlyFirstName = new UserUpdateRequestDto(
                "Владимир",
                null,
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOnlyFirstName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Владимир")));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} - обновление только lastName")
    void testUpdateUser_OnlyLastName() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);
        UserUpdateRequestDto updateOnlyLastName = new UserUpdateRequestDto(
                null,
                "Сергеев",
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOnlyLastName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", equalTo("Сергеев")));
    }

    @Test
    @DisplayName("PATCH /api/v1/users/{id} - обновление только userName")
    void testUpdateUser_OnlyUserName() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);
        UserUpdateRequestDto updateOnlyUserName = new UserUpdateRequestDto(
                null,
                null,
                "new_username_" + System.currentTimeMillis(),
                null
        );

        // Act & Assert
        mockMvc.perform(patch("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOnlyUserName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", equalTo(updateOnlyUserName.userName())));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - удаление пользователя")
    void testDeleteUser_Success() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(true)));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - удаление несуществующего пользователя")
    void testDeleteUser_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - проверка мягкого удаления")
    void testDeleteUser_SoftDelete() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act
        mockMvc.perform(delete("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert - пользователь должен быть помечен как удаленный
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.totalCount", equalTo(0)));
    }

    @Test
    @DisplayName("GET /api/v1/users - пагинация")
    void testListUsers_Pagination() throws Exception {
        // Arrange
        for (int i = 0; i < 15; i++) {
            userService.create(new UserCreateRequestDto(
                    "Пользователь",
                    String.valueOf(i),
                    UserRole.STUDENT,
                    "user_" + i + "_" + System.currentTimeMillis(),
                    "password" + i
            ));
        }

        // Act & Assert - первая страница
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(10)))
                .andExpect(jsonPath("$.totalCount", equalTo(15)));

        // Act & Assert - вторая страница
        mockMvc.perform(get("/api/v1/users")
                .param("page", "1")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(5)))
                .andExpect(jsonPath("$.totalCount", equalTo(15)));
    }

    @Test
    @DisplayName("GET /api/v1/users - без параметров пагинации (используются значения по умолчанию)")
    void testListUsers_WithoutPaginationParams() throws Exception {
        // Arrange
        userService.create(validCreateRequest);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/users - с DESC сортировкой")
    void testListUsers_WithDescSorting() throws Exception {
        // Arrange
        userService.create(validCreateRequest);
        userService.create(new UserCreateRequestDto(
                "Петр",
                "Петров",
                UserRole.TRAINER,
                "petr_petrov_" + System.currentTimeMillis(),
                "password456"
        ));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .param("sortField", "firstName")
                .param("sortType", "DESC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - проверка что пользователь не показывается в списке после удаления")
    void testDeleteUser_VerifyNotInList() throws Exception {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act - удаляем пользователя
        mockMvc.perform(delete("/api/v1/users/{id}", created.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert - проверяем, что в списке его нет
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("pageSize", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}

