package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId()==null) {
            em.persist(item); // id값이 없으면 jpa에 등록할 때 generatevalue를 이용해 넣어줌
        } else {
            em.merge(item); // 이미 db에 저장된 item을 수정하는 느낌,
                            // 사실 ItemService.updateItem(Long itemId, Book param)의
                            // 코드와 같음 즉 JPA가 DB에 엔티티를 가져와 수정하여 DB에 업데이트함
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
