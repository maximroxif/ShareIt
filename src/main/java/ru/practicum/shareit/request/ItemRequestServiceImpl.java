package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> findAll(long idRequester) {
        List<ItemRequest> list = itemRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
        return list.stream()
                .filter(ItemRequest -> ItemRequest.getRequester().getId() != idRequester)
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> findByRequester(long idRequester) {
        userRepository.findById(idRequester)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(idRequester);

        List<Long> requestIds = requests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<Item> items = itemRequestRepository.findByRequestIds(requestIds);

        Map<Long, List<Item>> itemsMap = items.stream().collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return requests.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto finById(long userId, long id) {
        Optional<ItemRequest> itemRequestOptional = itemRequestRepository.findById(id);
        if (itemRequestOptional.isEmpty()) {
            throw new NotFoundException("Request not found");
        }
        List<ItemDtoShort> item = itemRepository.findByRequest_Id(id).stream()
                .map(ItemMapper::toDtoShort)
                .toList();
        ItemRequestDto itemRequestDto = ItemRequestMapper.toDto(itemRequestOptional.get());
        itemRequestDto.setItems(item);
        return itemRequestDto;
    }
}
