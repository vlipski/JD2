package by.pvt.service;

import by.pvt.dao.WordDao;
import by.pvt.entity.Word;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WordService {

    private WordDao wordDao = new WordDao();
    private Connection connection;

    private void startTransaction() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/dictionary?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                "root", "Skatina964");
        connection.setAutoCommit(false);
    }

    private void commit() throws SQLException {
        connection.commit();
    }

    public void insert(String en, String rus) {
        try {
            startTransaction();
            wordDao.insertWord(en, rus);
            commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Word select(String en) {
        Word word;
        try {
            startTransaction();
            word = wordDao.selectWord(en);
            commit();
            return word;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
