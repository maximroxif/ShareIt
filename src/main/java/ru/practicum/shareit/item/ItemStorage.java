package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemStorage {
    Item getItem(Long id);

    List<Item> getUserItem(Long id);

    Item createItem(ItemDto itemDto, User user);

    Item updateItem(ItemDto itemDto, Long itemId);

    void deleteItem(Long id);

    List<ItemDto> searchItem(String searchString);

    Long getNextId();
}
