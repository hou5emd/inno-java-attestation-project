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
