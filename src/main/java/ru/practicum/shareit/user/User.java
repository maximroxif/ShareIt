package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    Long id;
    String name;
    String email;
}
