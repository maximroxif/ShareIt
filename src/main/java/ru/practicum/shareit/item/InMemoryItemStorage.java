package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items;


    @Override
    public Item getItem(Long id) {
        log.info("Getting item with id {}", id);
        return items.values().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public List<Item> getUserItem(Long id) {
        log.info("Getting user item with id {}", id);
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(ItemDto itemDto, User user) {
        log.info("Creating new item {}", itemDto);
        Item item = ItemMapper.fromDto(itemDto, getNextId(), user);
        items.put(item.getId(), item);
        return getItem(item.getId());
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long itemId) {
        log.info("Updating item with id {}", itemId);
       if (items.containsKey(itemId)) {
           Item oldItem = items.get(itemId);
           if (itemDto.getName() != null) {
               oldItem.setName(itemDto.getName());
               items.put(oldItem.getId(), oldItem);
           }
           if (itemDto.getDescription() != null) {
               oldItem.setDescription(itemDto.getDescription());
               items.put(oldItem.getId(), oldItem);
           }
           if (itemDto.getAvailable() != null) {
               oldItem.setAvailable(Boolean.parseBoolean(itemDto.getAvailable()));
               items.put(oldItem.getId(), oldItem);
           }
           return getItem(itemId);
       }
       throw new NotFoundException("Item not found");
    }

    @Override
    public void deleteItem(Long id) {
        log.info("Deleting item with id {}", id);
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item not found");
        }
        items.remove(id);
    }

    @Override
    public List<ItemDto> searchItem(String searchString) {
        log.info("Searching for items with {}", searchString);
        List<ItemDto> itemDtos = new ArrayList<>();
        List<ItemDto> itemName = items.values().stream()
                .filter(item -> item.getName().equalsIgnoreCase(searchString))
                .filter(Item::isAvailable)
                .map(ItemMapper::toDto)
                .toList();
        List<ItemDto> itemDescription = items.values().stream()
                .filter(item -> item.getDescription().equalsIgnoreCase(searchString))
                .filter(Item::isAvailable)
                .map(ItemMapper::toDto)
                .toList();
        if (!itemName.isEmpty()) {
            itemDtos.addAll(itemName);
        }
        if (!itemDescription.isEmpty()) {
            itemDtos.addAll(itemDescription);
        }

        return itemDtos;
    }

    @Override
    public Long getNextId() {
        long currentMaxId =  items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
