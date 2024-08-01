package com.example.bookshop.service;

import com.example.bookshop.model.BookEntity;
import com.example.bookshop.persistence.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public String testService() {
        BookEntity entity = BookEntity.builder().title("My first Book").build();

        repository.save(entity);

        BookEntity savedEntity = repository.findById(entity.getId()).get();

        return savedEntity.getTitle();

    }

    public List<BookEntity> create(final BookEntity entity) {
        validate(entity);

        repository.save(entity);

        log.info("Entity id: {} is saved", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    public List<BookEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<BookEntity> update(final BookEntity entity) {
        validate(entity);

        final Optional<BookEntity> original = repository.findById(entity.getId());

        original.ifPresent( Book -> {
            Book.setTitle(entity.getTitle());

            repository.save(Book);
        });

        return retrieve(entity.getUserId());
    }

//    public List<BookEntity> delete(final BookEntity entity) {
//        validate(entity);
//        log.info("Deleting entity with id: {}", entity.getId());
//
//        try {
//            repository.delete(entity);
//        } catch (Exception e) {
//            log.error("Error deleting entity", entity.getId(), e);
//            throw new RuntimeException("Error deleting entity " + entity.getId());
//        }
//        return retrieve(entity.getUserId());
//    }
    public List<BookEntity> findByTitleAndUserId(String title, String userId) {
        return repository.findByTitleAndUserId(title, userId);
    }

    public List<BookEntity> deleteByTitleAndUserId(String title, String userId) {
        List<BookEntity> entitiesToDelete = repository.findByTitleAndUserId(title, userId);
        if (entitiesToDelete.isEmpty()) {
            throw new RuntimeException("No book with title " + title + " found for user " + userId);
        }

        for (BookEntity entity : entitiesToDelete) {
            validate(entity);
        }

        try {
            repository.deleteAll(entitiesToDelete);
        } catch (Exception e) {
            log.error("Error deleting entities with title: {}", title, e);
            throw new RuntimeException("Error deleting entities with title " + title);
        }

        return retrieve(userId);
    }

    public List<BookEntity> deleteByTitle(String title, String userId) {
        List<BookEntity> entitiesToDelete = repository.findByTitleAndUserId(title, userId);
        if (entitiesToDelete.isEmpty()) {
            throw new RuntimeException("No book with title " + title + " found for user " + userId);
        }

        for (BookEntity entity : entitiesToDelete) {
            validate(entity);
        }

        try {
            repository.deleteAll(entitiesToDelete);
        } catch (Exception e) {
            log.error("Error deleting entities with title: {}", title, e);
            throw new RuntimeException("Error deleting entities with title " + title);
        }

        return retrieve(userId);
    }

    public List<BookEntity> delete(BookEntity entity) {
        validate(entity);
        log.info("Deleting entity with id: {}", entity.getId());

        try {
            repository.delete(entity);
        } catch (Exception e) {
            log.error("Error deleting entity", entity.getId(), e);
            throw new RuntimeException("Error deleting entity " + entity.getId());
        }
        return retrieve(entity.getUserId());
    }

    private void validate(final BookEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if( entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
