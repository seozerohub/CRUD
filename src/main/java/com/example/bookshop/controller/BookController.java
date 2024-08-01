package com.example.bookshop.controller;
import com.example.bookshop.dto.ResponseDTO;
import com.example.bookshop.dto.BookDTO;
import com.example.bookshop.model.BookEntity;
import com.example.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping("/test")
    public ResponseEntity<?> testBook() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@AuthenticationPrincipal String userId, @RequestBody BookDTO dto) {
        try {
            //String temporaryUserId = "SeoYoungYoon";

            BookEntity entity = BookDTO.toEntity(dto);

            entity.setId(null);

            entity.setUserId(userId);

            List<BookEntity> entities = service.create(entity);

            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());

            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<?> retrieveBookList(@AuthenticationPrincipal String userId) {
        try {
            //String temporaryUserId = "SeoYoungYoon";
            List<BookEntity> entities = service.retrieve(userId);
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());

            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateBook(@AuthenticationPrincipal String userId, @RequestBody BookDTO dto) {
        //String temporaryUserId = "SeoYoungYoon";

        BookEntity entity = BookDTO.toEntity(dto);

        entity.setUserId(userId);

        List<BookEntity> entities = service.update(entity);

        List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());

        ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteBook(@AuthenticationPrincipal String userId, @RequestBody BookDTO dto) {
        try {
            //String temporaryUserId = "SeoYoungYoon";

            BookEntity entity = BookDTO.toEntity(dto);

            entity.setUserId(userId);

            List<BookEntity> entities = service.deleteByTitle(entity.getTitle(), entity.getUserId());

            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());

            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}


