package by.pvt.dao;


import by.pvt.entity.Word;

import java.sql.*;

public class WordDao {

    private static final String selectByEn = "SELECT * FROM `dictionary`.`translation` WHERE EN = ? LIMIT 1";
    private static final String insert = " insert into `dictionary`.`translation` (`EN`, `RUS`) values (?, ?)";
    private PreparedStatement prstSelect;
    private PreparedStatement prstInsert;


    private Word populatEntity(ResultSet result) throws SQLException {
        Word word = new Word();
        word.setId(result.getInt(1));
        word.setEn(result.getString(2));
        word.setRus(result.getString(3));
        return word;
    }

    private PreparedStatement preStatement(String sql) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/dictionary?useUnicode=true&characterEncoding=UTF-8&useSSL=false",
                "root", "Skatina964");
        return connection.prepareStatement(sql);
    }

    public Word selectWord(String en) throws SQLException, ClassNotFoundException {
        prstSelect = preStatement(selectByEn);
        prstSelect.setString(1, en);
        ResultSet result = prstSelect.executeQuery();
        if (result.next()) {
            return populatEntity(result);
        }
        result.close();
        return null;
    }

    public int insertWord(String en, String rus) throws SQLException, ClassNotFoundException {
        prstInsert = preStatement(insert);
        prstInsert.setString(1, en);
        prstInsert.setString(2, rus);
        return prstInsert.executeUpdate();
    }
}
