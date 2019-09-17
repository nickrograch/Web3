package dao;


import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException{
        List<BankClient> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clients");
        ResultSet resultSet = stmt.getResultSet();
        while (resultSet.next()){
            BankClient bankClient = new BankClient(resultSet.getLong(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getLong(4));
            list.add(bankClient);
        }
        return list;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        Statement stmt = connection.createStatement();
        boolean check = stmt.execute("select * from bank_clients where name = '" + name +
                "' and password = '" + password + "'");
        stmt.close();
        return check;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException{
        String update = "update bank_clients set money = ? where name = ? and password = ?";
        BankClient client = getClientByName(name);
        if (client.getPassword().equals(password)) {
            PreparedStatement stmt = connection.prepareStatement(update);
            stmt.setLong(1, client.getMoney() + transactValue);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.execute();
            stmt.close();
        }
        else{
            throw new SQLException();
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt  = connection.createStatement();
        stmt.execute("select  * from bank_clients where id ='" + id + "'");
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        BankClient bankClient = new BankClient(id, resultSet.getString(2), resultSet.getString(3),
                resultSet.getLong(4));
        resultSet.close();
        stmt.close();
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException{
        Statement stmt  = connection.createStatement();
        stmt.execute("select  * from bank_clients where name ='" + name + "'");
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        Long moneyAmount = resultSet.getLong(4);
        resultSet.close();
        stmt.close();
        return moneyAmount >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clients where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clients where name = '" + name + "'");
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        BankClient bankClient = new BankClient(resultSet.getLong(1), resultSet.getString(2),
                resultSet.getString(3), resultSet.getLong(4));
        resultSet.close();
        stmt.close();
        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException  {
        Statement stmt = connection.createStatement();
        BankClient check = null;
        try {
            check = getClientByName(client.getName());
        }
        catch (SQLException e){
            stmt.executeUpdate("INSERT INTO bank_clients (name, password, money) VALUES ('" + client.getName() +
                    "', '" + client.getPassword() + "', " + client.getMoney() + ")");
        }
        stmt.close();
        if (check != null){
            throw new SQLException();
        }
    }

    public void deleteClient(String name) throws SQLException{
        Statement stmt = connection.createStatement();
        Long id = getClientIdByName(name);
        stmt.executeUpdate("delete from bank_clients where id = " + id);
        stmt.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_clients (id bigint auto_increment, name varchar(256), " +
                "password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_clients");
        stmt.close();
    }
}
