package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserStorage {
    List<UserDto> getAllUsers();

    User getUser(Long id);

    User createUser(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);

    Long getNextId();
}
