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


    @GetMapping
    @Operation(summary = "Список пользователей", description = "Получение списка пользователей с сортировкой и фильрацией", operationId = "listUser")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ListResponseDto<UserResponseDto>> listUser(@ModelAttribute @Nullable ListRequestDto<UserFilterDto> request) {
        ListResponseDto<UserResponseDto> list = userService.getUsersWithFilterAndSorting(request);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping()
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
