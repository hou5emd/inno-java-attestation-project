package ru.inno.attestation.attestation03.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.exceptions.UserAlreadyExistsException;
import ru.inno.attestation.attestation03.exceptions.UserNotFoundException;
import ru.inno.attestation.attestation03.mappers.UserMapper;
import ru.inno.attestation.attestation03.models.User;
import ru.inno.attestation.attestation03.repositories.UserRepository;
import ru.inno.attestation.attestation03.specifications.UserSpecifications;

import java.util.ArrayList;
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

    public ListResponseDto<UserResponseDto> getUsersWithFilterAndSorting(@Nullable ListRequestDto<UserFilterDto> request) {
        if (request == null) {
            request = new ListRequestDto<>();
        }
        Specification<User> specification = UserSpecifications.getSpecification(request.getFilter());
        List<Sort.Order> orders = new ArrayList<>();

        if(request.getSortField() != null) {
            orders.add(new Sort.Order(request.getSortType() != null ? request.getSortType() : Sort.Direction.ASC, request.getSortField()));
        }
        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getPageSize(),
                Sort.by(orders)
        );

        List<UserResponseDto> users = repository.findAll(specification, pageRequest).stream().map(userMapper::toGetResponseDto).toList();
        return  ListResponseDto.<UserResponseDto>builder()
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
