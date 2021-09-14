package ru.sbrf.sqlpractice.dao;

import java.util.List;
import ru.sbrf.sqlpractice.dto.Book;

public interface BookDao {
    Book getById(long id);
    void create(Book book);
    void update(Book book);
    void deleteById(long book);
    List<Book> getAll();
}
