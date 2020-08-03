package com.dao;

import com.model.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ItemDAO {
    private static final String NAME_LIKE = "SELECT * FROM ITEMS WHERE ITEM_NAME LIKE ?";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Item save(Item item) throws Exception{
        checkItem(item);
        entityManager.persist(item);
        return item;
    }

    @Transactional
    public Item findById(long id){
        return entityManager.find(Item.class, id);
    }

    @Transactional
    public Item update(Item item) throws Exception{
        checkItem(item);
        entityManager.merge(item);
        return item;
    }

    @Transactional
    public void delete(long id)throws Exception{
        Item item = entityManager.find(Item.class, id);
        checkItem(item);
        entityManager.remove(item);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public void deleteByName(String name)throws Exception{
        Query query = entityManager.createNativeQuery(NAME_LIKE, Item.class);
        query.setParameter(1, "%" + name + "%");
        List<Item> items = query.getResultList();
        for (Item i : items){
            delete(i.getId());
        }
    }

    private static void checkItem(Item item)throws Exception{
        if(item == null){
            throw new Exception("Exception in method ItemDAO.checkItem. Item can't be null.");
        }
    }
}
