
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.ApiServlet;
import servlet.MoneyTransactionServlet;
import servlet.RegistrationServlet;
import servlet.ResultServlet;

public class Main {
    public static void main(String[] args) throws Exception{
        ApiServlet apiServlet = new ApiServlet();
        ResultServlet resultServlet = new ResultServlet();
        RegistrationServlet registrationServlet = new RegistrationServlet();
        MoneyTransactionServlet moneyTransactionServlet = new MoneyTransactionServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(apiServlet), "/api/*");
        context.addServlet(new ServletHolder(resultServlet), "/result");
        context.addServlet(new ServletHolder(registrationServlet), "/registration");
        context.addServlet(new ServletHolder(moneyTransactionServlet), "/transaction ");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
