package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotBlank
    String available;
    Long request;
}
