package dao.mysql;

import dao.BaseDao;
import dao.TestDao;
import dao.exception.DaoException;
import entity.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12.07.2017.
 */
public class MySqlTestDao extends BaseDao<Test> implements TestDao {
    private static final String FIND_BY_ID = "SELECT * FROM tests WHERE test_id = ?";
    private static final String INSERT = "INSERT INTO tests VALUES(test_id,?)";
    private static final String UPDATE = "UPDATE tests SET name = ? WHERE test_id = ?";
    private static final String DELETE = "DELETE FROM tests WHERE test_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM tests";

    @Override
    public Test insert(Test item) throws DaoException {
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statementTest(statement, item);
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    item.setId(resultSet.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("can't insert " + item, e);
        }
        return item;
    }

    @Override
    public Test findById(int id) throws DaoException {
        Test test = null;
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_ID)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        test = itemTest(test, resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Can't find by id  ", e);
        }
        return test;
    }

    @Override
    public void update(Test item) throws DaoException {
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(UPDATE)) {
                statementTest(statement, item);
                statement.setInt(2, item.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("can't update " + item, e);
        }
    }

    @Override
    public void delete(Test item) throws DaoException {
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(DELETE)) {
                statement.setInt(1, item.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("can't delete test " + item, e);
        }
    }

    private Test itemTest(Test test, ResultSet resultSet) throws SQLException {
        test = new Test();
        test.setId(resultSet.getInt(1));
        test.setName(resultSet.getString(2));
        return test;
    }

    private PreparedStatement statementTest(PreparedStatement statement, Test item) throws SQLException {
        statement.setString(1, item.getName());
        return statement;
    }

    @Override
    public List<Test> getAllTests() throws DaoException {
        List<Test> list = new ArrayList<>();
        Test test = null;
        try {
            try (PreparedStatement statement = getConnection().prepareStatement(SELECT_ALL)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        test = itemTest(test, resultSet);
                        list.add(test);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("can't get all list ", e);
        }
        return list;
    }
}
