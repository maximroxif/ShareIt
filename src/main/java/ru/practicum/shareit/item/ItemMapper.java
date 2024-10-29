package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(String.valueOf(item.isAvailable()))
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public static Item fromDto(ItemDto dto, User user) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(Boolean.parseBoolean(dto.getAvailable()))
                .owner(user)
                .build();
    }
}
