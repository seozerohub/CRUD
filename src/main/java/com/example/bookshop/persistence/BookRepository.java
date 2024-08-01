package com.example.bookshop.persistence;

import com.example.bookshop.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
    List<BookEntity> findByUserId(String userId);
    List<BookEntity> findByTitleAndUserId(String title, String userId);
}

