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
        showInformationRequest(exchange);
        //urlS = reviewVirtualWeb(urlS);
        //urlS = "http://test-redes.125mb.com/";
        URL url = exchange.getRequestURI().toURL();
        System.out.println("URI: " + url.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
//        Map<String, String> parameters = new HashMap<>();
//        Headers heads  = exchange.getRequestHeaders();
//        for(Map.Entry<String, List<String>> s : heads.entrySet()){
//            parameters.put(s.getKey(), s.getValue().get(0));
//        }
        
        //
        //
        
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("param1", "val");
//
//        con.setDoOutput(true);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//        out.flush();
//        out.close();
//        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        System.out.println("STatus code: " + status);
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
    
    private static void postRequest(HttpExchange exchange){
        
    } // end postRequest
    
    private static void showInformationRequest(HttpExchange exchange) throws IOException{
        
        System.out.println("Encabezados:");
        
        
        System.out.println("Encabezados sjsjsjs:");
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
    } // end showInformationRequest
    
} // end class server
