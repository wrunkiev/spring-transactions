package com.controller;

import com.dao.ItemDAO;
import com.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.Item;
import com.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@Controller
public class ItemController {
    private BufferedReader bufferedReader;
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/item/save", produces = "application/json")
    public ResponseEntity<String> save(HttpServletRequest req, HttpServletResponse resp){
        try{
            Item item = requestRead(req);
            itemService.save(item);
            return new ResponseEntity<>("Item was saved", HttpStatus.CREATED);
        }catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/item/find", produces = "text/plain")
    public ResponseEntity<String> findById(@RequestParam(value = "id") Long id) {
        try {
            itemService.findById(id);
            return new ResponseEntity<>(" Item was found ", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/item/update", produces = "application/json")
    public ResponseEntity<String> update(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Item item = requestRead(req);
            itemService.update(item);
            return new ResponseEntity<>(" Item was updated ", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/item/delete", produces = "text/plain")
    public ResponseEntity<String> delete(@RequestParam(value = "id") Long id) {
        try {
            itemService.delete(id);
            return new ResponseEntity<>(" Item was deleted ", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/item/deleteName", produces = "text/plain")
    public ResponseEntity<String> deleteByName(@RequestParam(value = "name") String name) {
        try {
            itemService.deleteByName(name);
            return new ResponseEntity<>(" Items with name = " + name + " was deleted ", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Item requestRead(HttpServletRequest req)throws IllegalArgumentException, IOException {
        bufferedReader = req.getReader();
        String str = bodyContent(bufferedReader);
        Item item = getItem(str);
        if(item == null){
            throw new IllegalArgumentException("Request is empty");
        }
        return item;
    }

    private Item getItem(String response) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Item item = mapper.readValue(response, Item.class);
            return item;
        }catch (Exception e){
            return null;
        }
    }

    private String bodyContent(BufferedReader reader) throws IOException {
        String input;
        StringBuilder requestBody = new StringBuilder();
        while((input = reader.readLine()) != null) {
            requestBody.append(input);
        }
        return requestBody.toString();
    }
}