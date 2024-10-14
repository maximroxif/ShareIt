package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item getItem(Long id) {
        return itemStorage.getItem(id);
    }

    @Override
    public List<Item> getUserItem(long id) {
        if (userService.getUser(id) == null) {
            throw new NotFoundException("User not found");
        }
        return itemStorage.getUserItem(id);
    }

    @Override
    public Item createItem(ItemDto itemDto, Long id) {
        User user = userService.getUser(id);
        return itemStorage.createItem(itemDto, user);
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long userId, Long itemId) throws NotOwnerException {
        if (itemStorage.getItem(itemId).getOwner().getId() != userId) {
            throw new NotOwnerException("Not owner of this item");
        }
        return itemStorage.updateItem(itemDto, itemId);
    }

    @Override
    public void deleteItem(Long id) {
        itemStorage.deleteItem(id);
    }

    @Override
    public List<ItemDto> searchItem(String searchString) {
        return itemStorage.searchItem(searchString);
    }
}
