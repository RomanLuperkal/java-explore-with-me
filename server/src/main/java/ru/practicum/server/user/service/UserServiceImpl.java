package ru.practicum.server.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.user.dto.ListNewUserRequestResp;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.NewUserRequestResponse;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService{
    private final UserRepository users;
    private final UserMapper mapper;

    @Override
    public NewUserRequestResponse createUser(NewUserRequest userRequest) {
        return mapper.mapToUserRequestResp(users.save(mapper.mapToUser(userRequest)));
    }

    @Override
    public ListNewUserRequestResp getUsers(List<Long> ids, Pageable pageable) {
        return ListNewUserRequestResp.builder()
                .users(users.findUsers(pageable, ids))
                .build();
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.existsById(userId)) {
            users.deleteById(userId);
        } else {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }
}
