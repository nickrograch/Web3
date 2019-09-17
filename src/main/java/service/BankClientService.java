package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) throws DBException{
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() throws DBException{
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean deleteClient(String name) throws DBException{
        try {
            getBankClientDAO().deleteClient(name);
            return true;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean addClient(BankClient client) throws DBException {
        try {
            getBankClientDAO().addClient(client);
            return true;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws SQLException, DBException {
        BankClientDAO dao = getBankClientDAO();
        boolean check = false;
        if (dao.isClientHasSum(sender.getName(), value)){
            try {
                BankClient reciever = dao.getClientByName(name);
                dao.updateClientsMoney(reciever.getName(), reciever.getPassword(), value);
                value -= value*2;
                dao.updateClientsMoney(sender.getName(), sender.getPassword(), value);
                check = true;
            }
            catch (SQLException e){
                throw new DBException(e);
            }
        }
        return check;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("web3?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
