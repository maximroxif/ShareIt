package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long requesterId);

    @Query("SELECT i FROM Item i WHERE i.request.id IN :requestIds")
    List<Item> findByRequestIds(@Param("requestIds") List<Long> requestIds);

    @Query("SELECT ir FROM ItemRequest ir WHERE ir.requester.id <> :requesterId order by ir.created desc ")
    List<ItemRequest> findByOtherUsers(@Param("requesterId") long requesterId);
}
