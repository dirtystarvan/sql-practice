package ru.sbrf.sqlpractice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.sbrf.sqlpractice.dao.BookDaoJdbc;
import ru.sbrf.sqlpractice.dto.Book;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест методов BookDao")
@JdbcTest
@Import(BookDaoJdbc.class)
public class BookDaoJdbcTest {
    @Autowired
    private BookDaoJdbc bookJdbc;

    @Test
    void insertBookTest() {
        Book book = new Book(5, "SomeTestBook", "SomeTestAuthor", "SomeTestGenre");
        bookJdbc.create(book);

        Book result = bookJdbc.getById(5);
        assertThat(result).isEqualTo(book);
    }
}
