package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItem(Long id);

    List<Item> getUserItem(long id);

    Item createItem(ItemDto itemDto, Long id);

    Item updateItem(ItemDto itemDto, Long userId, Long itemId) throws NotOwnerException;

    void deleteItem(Long id);

    List<ItemDto> searchItem(String searchString);
}
