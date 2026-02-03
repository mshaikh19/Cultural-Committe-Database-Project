import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.daiict.Handler.WebAppHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Main application class that initializes and starts the bare-bones HTTP server.
 * This acts as the entry point for the entire Java web application.
 */
public class App {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        
        // 1. Create the server instance, listening on the specified port.
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // 2. Set the context (route) for the root path ("/") 
        // and associate it with our main request handler (WebAppHandler).
        server.createContext("/", new WebAppHandler());

        // 3. Set an executor (thread pool) for handling requests.
        // This allows the server to handle multiple simultaneous client connections.
        server.setExecutor(Executors.newCachedThreadPool());

        // 4. Start the server.
        server.start();

        System.out.println("=================================================");
        System.out.println("🚀 DA-IICT Cultural Committee Web Server Started!");
        System.out.println("🔗 View application at: http://localhost:" + PORT);
        System.out.println("=================================================");
    }
}