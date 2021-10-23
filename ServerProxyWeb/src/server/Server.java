package server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * PROXY WEB MARÍA
 * 
 * @Members:
 *  - Juan Sebastián Barreto Jimenéz
 *  - Janet Chen He
 *  - María José Niño Rodriguez
 *  - David Santiago Quintana Echavarria
 * 
 * @File: 
 *   Server.java
 */
public class Server {
    
    private final int PORT = 8080;
    private HttpServer httpd;  
    private HttpContext context;

    public Server() throws IOException {
        this.httpd = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.context =  httpd.createContext("/");
        context.setHandler(Server::manageRequest);
        httpd.start();
    } // end constructor Server
    
    private static void manageRequest(HttpExchange exchange) throws IOException {
        final int CODIGO_RESPUESTA = 200;
        String contenido = "Respuesta desde el servidor HTTP Maria desde clase";
        
        if(exchange.getRequestMethod().equals("GET")){
            getRequest(exchange);
        }else if(exchange.getRequestMethod().equals("POST")){
            postRequest(exchange);
        }
       
//        exchange.sendResponseHeaders(CODIGO_RESPUESTA, contenido.getBytes().length);
//        
//        OutputStream os = exchange.getResponseBody();
//        
//        os.write(contenido.getBytes());
//        os.close();
    } // end manageRequest
    
    private static void getRequest(HttpExchange exchange) throws ProtocolException, MalformedURLException, IOException{
        System.out.println("Entreeeeee");
        showInformationRequest(exchange);
        System.out.println("Entreeeeee Informa Imp");
        String urlS = "http://" + exchange.getRequestHeaders().get("Host").get(0);
        System.out.println("URL ===== " + urlS);
        //urlS = reviewVirtualWeb(urlS);
        URL url = new URL(urlS);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("param1", "val");
//
//        con.setDoOutput(true);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//        out.flush();
//        out.close();
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        
        String contentS = content.toString();
        exchange.sendResponseHeaders(status,contentS.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(contentS.getBytes());
        os.close();
        System.out.println("Se mando la respuesta: " + contentS);
    } // end getRequest
    
    private static void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }
    
    private static void postRequest(HttpExchange exchange){
        
    } // end postRequest
    
    private static void showInformationRequest(HttpExchange exchange){
        
        System.out.println("Encabezados:");
        exchange.getRequestHeaders().entrySet()
                .forEach(System.out::println);
      
        System.out.println();
        
        System.out.println("Método: " + exchange.getRequestMethod());
        
        System.out.println();
        
        System.out.println("Query:");
        URI uri = exchange.getRequestURI();
        System.out.println(uri.getQuery());
    } // end showInformationRequest
    
} // end class server
