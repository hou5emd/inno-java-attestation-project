# Attestation03Application.java

```java
package ru.inno.attestation.attestation03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Attestation03Application {

	public static void main(String[] args) {
		SpringApplication.run(Attestation03Application.class, args);
	}

}
```

# ServletInitializer.java

```java
package ru.inno.attestation.attestation03;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Attestation03Application.class);
	}

}
```

# JacksonConfig.java

```java
package ru.inno.attestation.attestation03.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
```

# OpenApiConfig.java

```java
package ru.inno.attestation.attestation03.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Attestation03 API")
                        .version("1.0")
                        .description("Документация REST API проекта Attestation03"));
    }
}
```

# TrainingEventResource.java

```java
package ru.inno.attestation.attestation03.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.services.TrainingEventService;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class TrainingEventResource {

    private final TrainingEventService service;


    @PostMapping("/list")
    @Operation(summary = "Список тренировок", description = "Получение списка Тренировок с сортировкой и фильрацией", operationId = "listEvents")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventListResponseDto> listEvents(@RequestBody @Nullable TrainingEventListRequestDto request) {
        TrainingEventListResponseDto events = service.list(request);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    // TODO: для финальной аттестации добавить получение ownerId из токена, исправить dto TrainingEventCreateRequestDto
    @PostMapping
    @Operation(summary = "Создание тренировки", description = "Создание стренировки", operationId = "createEvent")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<TrainingEventResponseDto> createEvent(@RequestBody TrainingEventCreateRequestDto request) {
        TrainingEventResponseDto response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Удаление Тренировки", operationId = "deleteEvent")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение Тренировки по id", operationId = "getById")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventResponseDto> getById(@PathVariable Long id) {
        TrainingEventResponseDto response = service.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление тренировки по id", operationId = "update")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventResponseDto> update(@PathVariable Long id, @RequestBody TrainingEventUpdateRequestDto request) {
        TrainingEventResponseDto response = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
```

# UserResource.java

```java
package ru.inno.attestation.attestation03.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.services.UserService;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;


    @PostMapping("/list")
    @Operation(summary = "Список пользователей", description = "Получение списка пользователей с сортировкой и фильрацией", operationId = "listUser")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<UserListResponseDto> listUser(@RequestBody @Nullable UserListRequestDto request) {
        UserListResponseDto list = userService.getUsersWithFilterAndSorting(request);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создание нового пользователя в системе", operationId = "createUser")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан")
    public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) {
        UserCreateResponseDto response = userService.create(userCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по id", operationId = "getUserById")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно получен")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя по id", operationId = "deleteUserById")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно удалён")
    public ResponseEntity<Boolean> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя", operationId = "updateUser")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDto request) {
        UserResponseDto response = userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
```

# TrainingEvent.java

```java
package ru.inno.attestation.attestation03.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "training_event")
public class TrainingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "training_event_students",
            joinColumns = @JoinColumn(name = "training_event_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students = new ArrayList<>();

    @Column(nullable = false)
    private Boolean deleted = false;
}
```

# User.java

```java
package ru.inno.attestation.attestation03.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.inno.attestation.attestation03.enums.UserRole;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean deleted = false;
}
```

# TrainingEventService.java

```java
package ru.inno.attestation.attestation03.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.exceptions.TrainingEventNotFoundException;
import ru.inno.attestation.attestation03.mappers.ListRequestDtoMapper;
import ru.inno.attestation.attestation03.mappers.TrainingEventMapper;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.repositories.TrainingEventRepository;
import ru.inno.attestation.attestation03.repositories.UserRepository;
import ru.inno.attestation.attestation03.specifications.TrainingEventSpecification;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrainingEventService {

    private final TrainingEventRepository repository;
    private final TrainingEventMapper mapper;
    private final UserRepository userRepository;


    public TrainingEventListResponseDto list(@Nullable BaseListRequestDto<TrainingEventFilterDto> request) {

        Specification<TrainingEvent> specification = TrainingEventSpecification.getSpecification(request != null ? request.getFilter() : null);

        PageRequest pageRequest = ListRequestDtoMapper.toDefaultPageAndSize(request);

        List<TrainingEventResponseDto> list = repository.findAll(specification, pageRequest).stream().map(mapper::toResponse).toList();
        Long totalCount = repository.count(specification);

        return TrainingEventListResponseDto.builder()
                .items(list)
                .totalCount(totalCount)
                .build();
    }

    public TrainingEventResponseDto create(TrainingEventCreateRequestDto request) {
        TrainingEvent event = mapper.createRequestToEntity(request);
        if (request.studentIds() != null && !request.studentIds().isEmpty()) {
            event.setStudents(userRepository.findAllById(request.studentIds()));
        }
        return mapper.toResponse(repository.save(event));
    }

    public TrainingEventResponseDto getById(Long id) {
        TrainingEvent event = repository.findById(id).orElseThrow(() -> new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id)));
        if (event.getDeleted()) {
            throw new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id));
        }
        return mapper.toResponse(event);
    }

    @Transactional
    public void delete(Long id) {
        TrainingEvent event = repository.findById(id).orElseThrow(() -> new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id)));
        if (event.getDeleted()) {
            throw new TrainingEventNotFoundException(String.format("Тренировка с id = %d, уже удалена", id));
        }
        event.setDeleted(true);
    }

    public TrainingEventResponseDto update(Long id, TrainingEventUpdateRequestDto request) {
        TrainingEvent event = repository.findById(id).orElseThrow(() -> new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id)));
        if (event.getDeleted()) {
            throw new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id));
        }
        TrainingEvent updatedEvent = mapper.mergeWithUpdateRequestDto(event, request);
        if (request.studentIds() != null) {
            updatedEvent.setStudents(userRepository.findAllById(request.studentIds()));
        }
        return mapper.toResponse(repository.save(updatedEvent));
    }


}
```

# UserService.java

```java
package ru.inno.attestation.attestation03.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.exceptions.UserAlreadyExistsException;
import ru.inno.attestation.attestation03.exceptions.UserNotFoundException;
import ru.inno.attestation.attestation03.mappers.ListRequestDtoMapper;
import ru.inno.attestation.attestation03.mappers.UserMapper;
import ru.inno.attestation.attestation03.models.User;
import ru.inno.attestation.attestation03.repositories.UserRepository;
import ru.inno.attestation.attestation03.specifications.UserSpecifications;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserCreateResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        User userRequest = userMapper.userCreateRequestToUser(userCreateRequestDto);
        if (repository.existsByUserName(userRequest.getUserName())) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        return userMapper.toUserCreateResponseDto(repository.save(userRequest));
    }

    public UserResponseDto getUserById(Long id) {
        Optional<User> user = repository.findById(id);
        return userMapper.toGetResponseDto(user.orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
    }

    public UserListResponseDto getUsersWithFilterAndSorting(@Nullable UserListRequestDto request) {
        Specification<User> specification = UserSpecifications.getSpecification(request != null && request.getFilter() != null ? request.getFilter() : null);

        PageRequest pageRequest = ListRequestDtoMapper.toDefaultPageAndSize(request);

        List<UserResponseDto> users = repository.findAll(specification, pageRequest).stream().map(userMapper::toGetResponseDto).toList();
        return  UserListResponseDto.builder()
                .items(users)
                .totalCount(repository.count(specification))
                .build();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с id = %d, не существует", id)));
        user.setDeleted(true);
    }

    public UserResponseDto update(Long id, UserUpdateRequestDto request) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с id = %d, не существует", id)));
        User newUser = userMapper.mergeWithUpdateRequestDto(user, request);
        return userMapper.toGetResponseDto(repository.save(newUser));
    }
}
```

# BaseListRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class BaseListRequestDto<F> {
    private F filter;
    @Min(0)
    private Integer page;
    @Min(1)
    private Integer pageSize;
    private Sort.Direction sortType;
    // TODO: сделать генерацию через annotation-processor
    private String sortField;
}
```

# BaseListResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class BaseListResponseDto<E> {
    @NotNull
    private List<E> items;
    @NotNull
    private Long totalCount;
}
```

# ErrorResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
    private String code;
    private String message;
}
```

# TrainingEventCreateRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import jakarta.validation.constraints.NotNull;
import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
public record TrainingEventCreateRequestDto(@NotNull String description, @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime startDate,
                                            @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime endDate, Long ownerId, List<Long> studentIds) {
}
```

# TrainingEventFilterDto.java

```java
package ru.inno.attestation.attestation03.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
public record TrainingEventFilterDto(Long id, String description, @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime fromDate, @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime toDate,
                                     Long ownerId, List<Long> studentIds) {
}
```

# TrainingEventListRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

public class TrainingEventListRequestDto extends BaseListRequestDto<TrainingEventFilterDto> {
}
```

# TrainingEventListResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TrainingEventListResponseDto extends BaseListResponseDto<TrainingEventResponseDto> {
}
```

# TrainingEventResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link ru.inno.attestation.attestation03.models.TrainingEvent}
 */
@Value
public class TrainingEventResponseDto {
    @NotNull
    Long id;
    @NotNull
    String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime startDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime endDate;
    @NotNull
    UserResponseDto owner;
    List<UserResponseDto> students;
}
```

# TrainingEventUpdateRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
public record TrainingEventUpdateRequestDto(@NotNull String description, @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime startDate,
                                            @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime endDate, List<Long> studentIds) {
}
```

# UserCreateRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.inno.attestation.attestation03.enums.UserRole;

/**
 * DTO for {@link ru.inno.attestation.attestation03.models.User}
 */
public record UserCreateRequestDto(String firstName, String lastName, @NotNull UserRole role,
                                   @NotNull @Size String userName, @NotNull @Size String password) {
}
```

# UserCreateResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.models.User;

/**
 * DTO for {@link User}
 */
public record UserCreateResponseDto(@NotNull Long id, String firstName, String lastName, @NotNull UserRole role,
                                    @NotNull String userName) {
}
```

# UserFilterDto.java

```java
package ru.inno.attestation.attestation03.dto;

import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.models.User;


/**
 * DTO for {@link User}
 */
public record UserFilterDto(Long id, String firstName, String lastName, UserRole role, String userName) {
}
```

# UserListRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

public class UserListRequestDto extends BaseListRequestDto<UserFilterDto> {
}
```

# UserListResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserListResponseDto extends BaseListResponseDto<UserResponseDto> {
}
```

# UserResponseDto.java

```java
package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.inno.attestation.attestation03.enums.UserRole;

public record UserResponseDto(Long id, String firstName, String lastName, @NotNull UserRole role,
                              @NotNull @Size String userName) {

}
```

# UserUpdateRequestDto.java

```java
package ru.inno.attestation.attestation03.dto;

import ru.inno.attestation.attestation03.models.User;

/**
 * DTO for {@link User}
 */
public record UserUpdateRequestDto(String firstName, String lastName, String userName, String password) {
}
```

# UserRole.java

```java
package ru.inno.attestation.attestation03.enums;

public enum UserRole {
    TRAINER, STUDENT
}
```

# GlobalExceptionHandler.java

```java
package ru.inno.attestation.attestation03.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.inno.attestation.attestation03.dto.ErrorResponseDto;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponseDto error = new ErrorResponseDto("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TrainingEventNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleTrainingEventNotFound(TrainingEventNotFoundException ex) {
        ErrorResponseDto error = new ErrorResponseDto("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponseDto error = new ErrorResponseDto("CONFLICT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        ErrorResponseDto error = new ErrorResponseDto("INTERNAL_SERVER_ERROR", "Внутренняя ошибка сервера");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

# TrainingEventNotFoundException.java

```java
package ru.inno.attestation.attestation03.exceptions;

public class TrainingEventNotFoundException extends RuntimeException {
    public TrainingEventNotFoundException(String message) {
        super(message);
    }
}
```

# UserAlreadyExistsException.java

```java
package ru.inno.attestation.attestation03.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
```

# UserNotFoundException.java

```java
package ru.inno.attestation.attestation03.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

# ListRequestDtoMapper.java

```java
package ru.inno.attestation.attestation03.mappers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.inno.attestation.attestation03.dto.BaseListRequestDto;

import java.util.ArrayList;
import java.util.List;

public class ListRequestDtoMapper {
    public static PageRequest toDefaultPageAndSize(BaseListRequestDto<?> request) {
        List<Sort.Order> orders = new ArrayList<>();
        int page = request != null && request.getPage() != null ? request.getPage() : 0;
        int pageSize = request != null && request.getPageSize() != null ? request.getPageSize() : 25;

        if(request != null && request.getSortField() != null) {
            orders.add(new Sort.Order(request.getSortType() != null ? request.getSortType() : Sort.Direction.ASC, request.getSortField()));
        }

        return PageRequest.of(page, pageSize, Sort.by(orders));
    }
}
```

# TrainingEventMapper.java

```java
package ru.inno.attestation.attestation03.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.models.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TrainingEventMapper {
    TrainingEventResponseDto toResponse(TrainingEvent event);

    default List<Long> studentsToStudentIds(List<User> students) {
        return students.stream().map(User::getId).toList();
    }

    @Mapping(source = "ownerId", target = "owner.id")
    TrainingEvent createRequestToEntity(TrainingEventCreateRequestDto trainingEventCreateRequestDto);

    TrainingEvent mergeWithUpdateRequestDto(@MappingTarget TrainingEvent event, TrainingEventUpdateRequestDto newEvent);
}
```

# UserMapper.java

```java
package ru.inno.attestation.attestation03.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userCreateRequestToUser(UserCreateRequestDto userCreateRequestDto);

    UserResponseDto toGetResponseDto(User user);

    UserCreateResponseDto toUserCreateResponseDto(User user);

    User mergeWithUpdateRequestDto(@MappingTarget User user, UserUpdateRequestDto newUser);
}
```

# TrainingEventRepository.java

```java
package ru.inno.attestation.attestation03.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.inno.attestation.attestation03.models.TrainingEvent;

public interface TrainingEventRepository extends JpaRepository<TrainingEvent, Long>, JpaSpecificationExecutor<TrainingEvent> {

}
```

# UserRepository.java

```java
package ru.inno.attestation.attestation03.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.inno.attestation.attestation03.models.User;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUserName(String username);
}
```

# TrainingEventSpecification.java

```java
package ru.inno.attestation.attestation03.specifications;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.inno.attestation.attestation03.dto.TrainingEventFilterDto;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.models.User;

import java.util.ArrayList;
import java.util.List;

public class TrainingEventSpecification {
    public static Specification<TrainingEvent> getSpecification(@Nullable TrainingEventFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (filter == null) {
                return  cb.and(predicates.toArray(new Predicate[0]));
            }
            if(filter.id() != null) {
                predicates.add(cb.equal(root.get("id"), filter.id()));
            }
            if(filter.description() != null) {
                predicates.add(cb.equal(root.get("description"), filter.description()));
            }
            if(filter.ownerId() != null) {
                predicates.add(cb.equal(root.get("ownerId"), filter.ownerId()));
            }
            if(filter.fromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), filter.fromDate()));
            }
            if(filter.fromDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), filter.toDate()));
            }
            if (filter.studentIds() != null && !filter.studentIds().isEmpty()) {
                Join<TrainingEvent, User> studentsJoin = root.join("students");
                predicates.add(studentsJoin.get("id").in(filter.studentIds()));
            }


            return  cb.and(predicates.toArray(new Predicate[0]));

        };
    }
}
```

# UserSpecifications.java

```java
package ru.inno.attestation.attestation03.specifications;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.inno.attestation.attestation03.dto.UserFilterDto;
import ru.inno.attestation.attestation03.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> getSpecification(@Nullable UserFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isFalse(root.get("deleted")));

            if (filter == null) {
                return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
            if (filter.role() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), filter.role()));
            }
            if (filter.id() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), filter.id()));
            }
            if (filter.userName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userName"), filter.userName()));
            }

            if (filter.firstName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("firstName"), filter.firstName()));
            }

            if (filter.lastName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lastName"), filter.lastName()));
            }

            return  criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
```

# pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.5.7</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>ru.inno.attestation</groupId>
	<artifactId>attestation03</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>

	<name>attestation03</name>
	<description>Attestation project</description>
	<url/>
	<licenses>
		<license/>
	</licenses>
	<developers>
		<developer/>
	</developers>
	<scm>
		<connection/>
		<developerConnection/>
		<tag/>
		<url/>
	</scm>
	<properties>
		<java.version>21</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
<!--		<dependency>-->
<!--			<groupId>org.springframework.boot</groupId>-->
<!--			<artifactId>spring-boot-starter-security</artifactId>-->
<!--		</dependency>-->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.liquibase</groupId>
			<artifactId>liquibase-core</artifactId>
		</dependency>

        <!-- https://mvnrepository.com/artifact/org.mapstruct/mapstruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>1.6.3</version>
        </dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-docker-compose</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-configuration-processor</artifactId>
            <version>3.5.7</version>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
            <version>1.18.42</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>

		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
        <!-- https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.8.14</version>
        </dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-testcontainers</artifactId>
			<scope>test</scope>
		</dependency>
<!--		<dependency>-->
<!--			<groupId>org.springframework.security</groupId>-->
<!--			<artifactId>spring-security-test</artifactId>-->
<!--			<scope>test</scope>-->
<!--		</dependency>-->
		<dependency>
			<groupId>org.testcontainers</groupId>
			<artifactId>junit-jupiter</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.testcontainers</groupId>
			<artifactId>postgresql</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
                <version>3.14.1</version>

				<configuration>

                        <release>21</release>
                        <source>21</source>
                        <target>21</target>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
                            <version>1.18.42</version>
						</path>
						<path>
							<groupId>org.springframework.boot</groupId>
							<artifactId>spring-boot-configuration-processor</artifactId>
                            <version>3.5.7</version>
						</path>
                        <!-- https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor -->
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>1.6.3</version>
                        </path>

					</annotationProcessorPaths>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>
```