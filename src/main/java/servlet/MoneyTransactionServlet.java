package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.getWriter().println(new PageGenerator().getPage("moneyTransactionPage.html", pageVariables));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        String name = req.getParameter("senderName");
        String password = req.getParameter("senderPass");
        Long money = Long.valueOf(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        try {
            if (bankClientService.getClientByName(name).getPassword().equals(password)) {
                BankClient clientFrom = bankClientService.getClientByName(name);
                if (bankClientService.sendMoneyToClient(clientFrom, nameTo, money)) {
                    pageVariables.put("message", "The transaction was successful");
                } else {
                    pageVariables.put("message", "transaction rejected");
                }
            }
            else{
                throw new SQLException();
            }
        } catch (DBException | SQLException e) {
            pageVariables.put("message", "transaction rejected");
        }

        resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("senderName", "");
        pageVariables.put("senderPass", "");
        pageVariables.put("count", "");
        pageVariables.put("nameTo", "");
        pageVariables.put("message", "");
        return pageVariables;
    }
}
