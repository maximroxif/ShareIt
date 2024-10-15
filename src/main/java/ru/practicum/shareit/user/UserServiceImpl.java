package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::toDtoUser).toList();
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.toDtoUser(userStorage.getUser(id));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDtoUser(userStorage.createUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDtoUser(userStorage.updateUser(user, id));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
