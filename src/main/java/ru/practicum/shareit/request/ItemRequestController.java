package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestService.findAll(requesterId);
    }

    @GetMapping
    List<ItemRequestDto> getRequesterList(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        return itemRequestService.findByRequester(requesterId);
    }

    @GetMapping("/{id}")
    ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable Long id) {
        return itemRequestService.finById(userId, id);
    }
}
