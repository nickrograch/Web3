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
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            long money = resultSet.getLong("money");
            BankClient bankClient = new BankClient(id, name, password, money);
            list.add(bankClient);
        }
        return list;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        String query = "select * from bank_clients where name = ? and password = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, password);
        boolean check = stmt.execute();
        stmt.close();
        return check;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException{
        String update = "update bank_clients set money = ? where name = ? and password = ?";
        BankClient client = getClientByName(name);
        PreparedStatement stmt = connection.prepareStatement(update);
        stmt.setLong(1, client.getMoney() + transactValue);
        stmt.setString(2, name);
        stmt.setString(3, password);
        stmt.execute();
        stmt.close();

    }

    public BankClient getClientById(long id) throws SQLException {
        String query = "select  * from bank_clients where id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setLong(1, id);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        long money = resultSet.getLong("money");
        BankClient bankClient = new BankClient(id, name, password, money);
        resultSet.close();
        stmt.close();
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException{
        String query = "select  * from bank_clients where name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        Long moneyAmount = resultSet.getLong(4);
        resultSet.close();
        stmt.close();
        return moneyAmount >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        String query = "select  * from bank_clients where name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.execute();
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException{
        String query = "select  * from bank_clients where name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, name);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        long id = resultSet.getLong("id");
        String password = resultSet.getString("password");
        long money = resultSet.getLong("money");
        BankClient bankClient = new BankClient(id, name, password, money);
        resultSet.close();
        stmt.close();
        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException  {
        String query = "INSERT INTO bank_clients (name, password, money) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, client.getName());
        stmt.setString(2, client.getPassword());
        stmt.setLong(3, client.getMoney());
        BankClient check = null;
        try {
            check = getClientByName(client.getName());
        }
        catch (SQLException e){
            stmt.executeUpdate();
        }
        stmt.close();
        if (check != null){
            throw new SQLException();
        }
    }

    public void deleteClient(String name) throws SQLException{
        String query = "delete from bank_clients where id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        Long id = getClientIdByName(name);
        stmt.setLong(1, id);
        stmt.executeUpdate();
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
