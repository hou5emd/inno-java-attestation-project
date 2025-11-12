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
import ru.inno.attestation.attestation03.models.User;
import ru.inno.attestation.attestation03.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers
@Transactional
@DisplayName("UserService tests")
class UserServiceTest {

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

    private ListRequestDto<UserFilterDto> createListRequest(Integer page, Integer pageSize, String sortField, org.springframework.data.domain.Sort.Direction sortType) {
        ListRequestDto<UserFilterDto> request = new ListRequestDto<>();
        request.setPage(page);
        request.setPageSize(pageSize);
        if (sortField != null) {
            request.setSortField(sortField);
            if (sortType != null) {
                request.setSortType(sortType);
            }
        }
        return request;
    }
    @DisplayName("Создание пользователя - успешно")
    void testCreateUser_Success() {
        // Act
        UserCreateResponseDto response = userService.create(validCreateRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(validCreateRequest.firstName(), response.firstName());
        assertEquals(validCreateRequest.lastName(), response.lastName());
        assertEquals(validCreateRequest.role(), response.role());
        assertEquals(validCreateRequest.userName(), response.userName());
    }

    @Test
    @DisplayName("Создание пользователя с дублирующимся userName - исключение")
    void testCreateUser_DuplicateUserName_ThrowsException() {
        // Arrange
        userService.create(validCreateRequest);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            UserCreateRequestDto duplicateRequest = new UserCreateRequestDto(
                    "Другое",
                    "Имя",
                    UserRole.TRAINER,
                    validCreateRequest.userName(),
                    "password456"
            );
            userService.create(duplicateRequest);
        });

        assertTrue(exception.getMessage().contains("уже существует"));
    }

    @Test
    @DisplayName("Получение пользователя по ID - успешно")
    void testGetUserById_Success() {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act
        UserResponseDto response = userService.getUserById(created.id());

        // Assert
        assertNotNull(response);
        assertEquals(created.id(), response.id());
        assertEquals(validCreateRequest.firstName(), response.firstName());
        assertEquals(validCreateRequest.lastName(), response.lastName());
        assertEquals(validCreateRequest.role(), response.role());
        assertEquals(validCreateRequest.userName(), response.userName());
    }

    @Test
    @DisplayName("Получение пользователя с несуществующим ID - исключение")
    void testGetUserById_NotFound_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> userService.getUserById(9999L));
    }

    @Test
    @DisplayName("Обновление пользователя - успешно")
    void testUpdateUser_Success() {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act
        UserResponseDto updated = userService.update(created.id(), updateRequest);

        // Assert
        assertNotNull(updated);
        assertEquals(created.id(), updated.id());
        assertEquals(updateRequest.firstName(), updated.firstName());
        assertEquals(updateRequest.lastName(), updated.lastName());
        assertEquals(updateRequest.userName(), updated.userName());
    }

    @Test
    @DisplayName("Обновление пользователя с несуществующим ID - исключение")
    void testUpdateUser_NotFound_ThrowsException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.update(9999L, updateRequest);
        });

        assertTrue(exception.getMessage().contains("не существует"));
    }

    @Test
    @DisplayName("Удаление пользователя - успешно (мягкое удаление)")
    void testDeleteUser_Success() {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);

        // Act
        userService.deleteUser(created.id());

        // Assert
        User deletedUser = userRepository.findById(created.id()).orElse(null);
        assertNotNull(deletedUser);
        assertTrue(deletedUser.getDeleted());
    }

    @Test
    @DisplayName("Удаление пользователя с несуществующим ID - исключение")
    void testDeleteUser_NotFound_ThrowsException() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(9999L);
        });

        assertTrue(exception.getMessage().contains("не существует"));
    }

    @Test
    @DisplayName("Получение списка пользователей без фильтра - успешно")
    void testGetUsersWithFilterAndSorting_WithoutFilter_Success() {
        // Arrange
        userService.create(validCreateRequest);
        userService.create(new UserCreateRequestDto(
                "Петр",
                "Петров",
                UserRole.TRAINER,
                "petr_petrov_" + System.currentTimeMillis(),
                "password456"
        ));

        ListRequestDto<UserFilterDto> request = createListRequest(0, 10, null, null);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getItems().size());
        assertEquals(2, response.getTotalCount());
    }

    @Test
    @DisplayName("Получение списка пользователей с сортировкой - успешно")
    void testGetUsersWithFilterAndSorting_WithSorting_Success() {
        // Arrange
        userService.create(validCreateRequest);
        userService.create(new UserCreateRequestDto(
                "Александр",
                "Сидоров",
                UserRole.TRAINER,
                "alex_sidorov_" + System.currentTimeMillis(),
                "password789"
        ));

        ListRequestDto<UserFilterDto> request = createListRequest(0, 10, "firstName", null);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getItems().size());
        assertNotNull(response.getTotalCount());
    }

    @Test
    @DisplayName("Получение пустого списка пользователей - успешно")
    void testGetUsersWithFilterAndSorting_Empty_Success() {
        // Arrange
        ListRequestDto<UserFilterDto> request = createListRequest(0, 10, null, null);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getItems().size());
        assertEquals(0, response.getTotalCount());
    }

    @Test
    @DisplayName("Получение списка пользователей с фильтрацией по firstName")
    void testGetUsersWithFilterAndSorting_WithFirstNameFilter_Success() {
        // Arrange
        userService.create(validCreateRequest);

        UserFilterDto filter = new UserFilterDto(null, "Иван", null, null, null);
        ListRequestDto<UserFilterDto> request = new ListRequestDto<>();
        request.setFilter(filter);
        request.setPage(0);
        request.setPageSize(10);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.getItems().isEmpty());
    }

    @Test
    @DisplayName("Получение списка пользователей с фильтрацией по lastName")
    void testGetUsersWithFilterAndSorting_WithLastNameFilter_Success() {
        // Arrange
        userService.create(validCreateRequest);

        UserFilterDto filter = new UserFilterDto(null, null, "Иванов", null, null);
        ListRequestDto<UserFilterDto> request = new ListRequestDto<>();
        request.setFilter(filter);
        request.setPage(0);
        request.setPageSize(10);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.getItems().isEmpty());
    }

    @Test
    @DisplayName("Получение списка пользователей с фильтрацией по userName")
    void testGetUsersWithFilterAndSorting_WithUserNameFilter_Success() {
        // Arrange
        userService.create(validCreateRequest);

        UserFilterDto filter = new UserFilterDto(null, null, null, null, validCreateRequest.userName());
        ListRequestDto<UserFilterDto> request = new ListRequestDto<>();
        request.setFilter(filter);
        request.setPage(0);
        request.setPageSize(10);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
    }

    @Test
    @DisplayName("Получение списка пользователей с фильтрацией по role")
    void testGetUsersWithFilterAndSorting_WithRoleFilter_Success() {
        // Arrange
        userService.create(validCreateRequest);

        UserFilterDto filter = new UserFilterDto(null, null, null, UserRole.STUDENT, null);
        ListRequestDto<UserFilterDto> request = new ListRequestDto<>();
        request.setFilter(filter);
        request.setPage(0);
        request.setPageSize(10);

        // Act
        ListResponseDto<UserResponseDto> response = userService.getUsersWithFilterAndSorting(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.getItems().isEmpty());
    }

    @Test
    @DisplayName("Обновление пользователя с частичными данными")
    void testUpdateUser_PartialUpdate_Success() {
        // Arrange
        UserCreateResponseDto created = userService.create(validCreateRequest);
        UserUpdateRequestDto partialUpdate = new UserUpdateRequestDto(
                "Новое Имя",
                null,
                null,
                null
        );

        // Act
        UserResponseDto updated = userService.update(created.id(), partialUpdate);

        // Assert
        assertNotNull(updated);
        assertEquals("Новое Имя", updated.firstName());
    }
}

