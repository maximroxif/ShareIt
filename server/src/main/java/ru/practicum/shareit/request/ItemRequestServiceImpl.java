package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        log.info("Create item request: {}", itemRequestDto);

        User requester = getUserById(userId);

        ItemRequest itemRequest = ItemRequest.builder()
                .requester(requester)
                .created(LocalDateTime.now())
                .description(itemRequestDto.getDescription())
                .build();

        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findAll(long requesterId) {
        log.info("Find all item requests: {}", requesterId);
        List<ItemRequest> requests = itemRequestRepository.findByOtherUsers(requesterId);
        return getRequestsWithItems(requests);
    }

    @Override
    public List<ItemRequestDto> findByRequester(long requesterId) {
        log.info("Find all item by requester: {}", requesterId);
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(requesterId);
        return getRequestsWithItems(requests);
    }

    @Override
    public ItemRequestDto finById(long userId, long requestId) {
        log.info("Find item request by id: {}", requestId);
        getUserById(userId);
        Optional<ItemRequest> itemRequestOptional = itemRequestRepository.findById(requestId);
        if (itemRequestOptional.isEmpty()) {
            throw new NotFoundException("Request with id " + requestId + " not found");
        }
        List<ItemDtoShort> item = itemRepository.findByRequest_Id(requestId).stream()
                .map(ItemMapper::toDtoShort)
                .toList();
        ItemRequestDto itemRequestDto = ItemRequestMapper.toDto(itemRequestOptional.get());
        itemRequestDto.setItems(item);
        return itemRequestDto;
    }

    private List<ItemRequestDto> getRequestsWithItems(List<ItemRequest> requests) {
        List<Long> requestIds = requests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRequestRepository.findByRequestIds(requestIds);
        Map<Long, List<Item>> map = items.stream().collect(Collectors.groupingBy(item -> item.getRequest().getId()));
        return requests.stream()
                .map(request -> ItemRequestMapper.toDto(request, map.getOrDefault(request.getId(), List.of()))).toList();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}
