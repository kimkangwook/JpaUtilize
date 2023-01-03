package jpabook.jpashop.service;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
// 단순하게 repository에 위임하는 역할임. 이 부분은 Controller에서 바로 Repository에 접근하는 것을 고려해볼만함
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
//        findItem.change(price, name, stockQuantity); set으로 값을 수정하는 것보다 이런식으로 의미있는 메소드로 해야 다른사람들이 코드를 쉽게 역추적함
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        //스프링이 Transaction commit을 실행하면 JPA에서는 DB에 flush를 함(영속성 엔티티 중 변경된 사항에 대해 DB에 업데이트 쿼리를 날림)
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
