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

    private final String selectQuery =
            "SELECT Book.id id," +
                    "Book.name name" +
                    "Author.first_name author" +
                    "Genre.name genre" +
            "FROM Book" +
                    "JOIN author ON Book.author = Author.id" +
                    "JOIN genre ON Book.genre = Genre.id";

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.query(selectQuery + "WHERE id = :id",
                params, new BookExtractor());
    }

    @Override
    public void create(Book book) {
        List<Long> id = jdbc.query("SELECT max(id) FROM Book", (resultSet, i) -> resultSet.getLong("id"));
        Map<String, Object> params = new HashMap<>();
        params.put("id", id.get(0));
        params.put("name", book.getName());
        params.put("author", book.getAuthor());
        params.put("genre", book.getGenre());

        jdbc.update("INSERT INTO Book (" +
                " Id,\n" +
                " Name,\n" +
                " Author,\n" +
                " Genre\n" +
                ")" +
                "   VALUES ( " +
                " :id,\n" +
                " :name,\n" +
                " :author,\n" +
                " :genre\n" +
                ")", params);
    }

    @Override
    public void update(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("name", book.getName());
        params.put("author", book.getAuthor());
        params.put("genre", book.getGenre());

        jdbc.update("UPDATE Book SET" +
                        " name = :name,\n" +
                        " author = :author,\n" +
                        " genre = :genre\n" +
                        "WHERE id = :id",
                params);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        jdbc.update("DELETE FROM Book WHERE id = :id", params);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query(selectQuery, new BooksExtractor());
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
            Map<Long, Book> books = new HashMap<>();

            while (resultSet.next()) {
                Book book = books.get(resultSet.getLong("id"));
                if (book == null) {
                    book = new Book(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("author"),
                            resultSet.getInt("genre"));

                    books.put(resultSet.getLong("id"), book);
                }
            }

            return new ArrayList<>(books.values());
        }
    }
}
