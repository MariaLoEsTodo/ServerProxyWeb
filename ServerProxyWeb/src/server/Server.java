package server;

import com.sun.net.httpserver.Headers;
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
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

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
    
    private final int PORT = 16547;
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
        
        System.out.println(" -----Método Inicial: " + exchange.getRequestMethod());
        
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
//        showInformationRequest(exchange);
        //urlS = reviewVirtualWeb(urlS);
        //urlS = "http://test-redes.125mb.com/";
        URL url = exchange.getRequestURI().toURL();
        System.out.println("URI: " + url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        Headers heads  = exchange.getRequestHeaders();
        for(Map.Entry<String, List<String>> s : heads.entrySet()){
//            System.out.println("Aqui: " + s.getKey() + " Valor: " + s.getValue());
            con.setRequestProperty(s.getKey(), s.getValue().toString());
        }
        
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        System.out.println("Status code: " + status);
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
//        System.out.println("Se mando la respuesta: " + contentS);
    } // end getRequest
    
    private static void postRequest(HttpExchange exchange)throws ProtocolException, MalformedURLException, IOException{ 
        
        showInformationRequest(exchange);
        //urlS = reviewVirtualWeb(urlS);
        //urlS = "http://test-redes.125mb.com/";
        URL url = exchange.getRequestURI().toURL();
        System.out.println("URI: " + url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        Headers heads  = exchange.getRequestHeaders();
        for(Map.Entry<String, List<String>> s : heads.entrySet()){
            System.out.println("Aqui: " + s.getKey() + " Valor: " + s.getValue());
            con.setRequestProperty(s.getKey(), s.getValue().toString());
        }
        
        con.setRequestMethod("POST");
        int status = con.getResponseCode();
        System.out.println("Status code: " + status);
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
        
    } // end postRequest
    
    private static void showInformationRequest(HttpExchange exchange) throws IOException{
        
        System.out.println("Encabezados:");
        
        
        System.out.println("Encabezados:");
        System.out.println(exchange.getRequestHeaders().toString());
        
        System.out.println("URI");
        System.out.println(exchange.getRequestURI());
        System.out.println("Body");
        System.out.println(exchange.getRequestBody().toString());  
        
        System.out.println();
        System.out.println("Método: " + exchange.getRequestMethod());
        
        System.out.println();
        
        System.out.println("Query:");
        URI uri = exchange.getRequestURI();
        System.out.println(uri.getQuery());
        
        
        BufferedReader br = new BufferedReader(new InputStreamReader((exchange.getRequestBody())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
          sb.append(output);
        }
        System.out.println("Post Body");
        System.out.println(sb.toString());
        
 
        
    } // end showInformationRequest
    
} // end class server
