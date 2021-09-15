package ru.sbrf.sqlpractice.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.sbrf.sqlpractice.dto.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.query("select * from books where id = :id",
                params, new BookExtractor());
    }

    @Override
    public void create(Book book) {

    }

    @Override
    public void update(Book book) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", book.getId());
        params.put("author", book.getAuthor());
        params.put("genre", book.getGenre());
        params.put("name", book.getName());

        jdbc.update("update books set" +
                        " name = :name,\n" +
                        " author = :author,\n" +
                        " genre = :genre\n" +
                        "where id = :id",
                params);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        jdbc.update("DELETE FROM books WHERE id = :id", params);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from books", new BooksExtractor());
    }

    private class BookExtractor implements ResultSetExtractor<Book> {
        @Override
        public Book extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<Book> books = new BooksExtractor().extractData(resultSet);
            return books.isEmpty() ? null : books.get(0);
        }
    }

    private class BooksExtractor implements ResultSetExtractor<List<Book>> {
        @Override
        public List<Book> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Integer, Book> books = new HashMap<>();

            while (resultSet.next()) {
                Book book = books.get(resultSet.getInt("id"));
                if (book == null) {
                    book = new Book(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("author"),
                            resultSet.getString("genre"));

                    books.put(resultSet.getInt("id"), book);
                }
            }
            return new ArrayList<>(books.values());
        }
    }
}
