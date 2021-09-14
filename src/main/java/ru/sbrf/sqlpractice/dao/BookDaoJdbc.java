package ru.sbrf.sqlpractice.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.sbrf.sqlpractice.dto.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    }

    @Override
    public void deleteById(long book) {

    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    private class BookExtractor implements ResultSetExtractor<Book> {
        @Override
        public Book extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            resultSet.next();
            return new Book(resultSet.getInt())
        }
    }
}
