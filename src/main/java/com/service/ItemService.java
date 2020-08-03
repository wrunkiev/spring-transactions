package com.service;

import com.dao.ItemDAO;
import com.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ItemService {

    private ItemDAO itemDAO;

    @Autowired
    public ItemService(ItemDAO itemDAO){
        this.itemDAO = itemDAO;
    }

    public Item save(Item item)throws Exception{
        return itemDAO.save(item);
    }

    public Item findById(long id)throws NoSuchElementException {
        if(itemDAO.findById(id) == null){
            throw new NoSuchElementException();
        }
        return itemDAO.findById(id);
    }

    public Item update(Item item)throws Exception {
        return itemDAO.update(item);
    }

    public void delete(long id)throws Exception{
        itemDAO.delete(id);
    }

    public void deleteByName(String name)throws Exception{
        itemDAO.deleteByName(name);
    }
}
