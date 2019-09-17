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
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        resp.getWriter().println(new PageGenerator().getPage("registrationPage.html", pageVariables));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));
        BankClient client = new BankClient(name, password, money);
        try {
            new BankClientService().addClient(client);
            pageVariables.put("message", "Add client successful");
        } catch (DBException e) {
            pageVariables.put("message", "Client not add");
        }

        resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request){
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", "");
        pageVariables.put("password", "");
        pageVariables.put("money", 0);
        pageVariables.put("message", "");
        return pageVariables;
    }
}
