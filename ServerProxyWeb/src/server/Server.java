package server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import fileVirtual.FileVirtual;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    
    private final int PORT = 8080;
    private HttpServer httpd;  
    private HttpContext context;
    private final String dirArchi = "virtualA.txt";
    private static ArrayList<FileVirtual> listaFiles = new ArrayList<FileVirtual>();
   

    public Server() throws IOException {
        iniSitiosVirtuales();
        this.httpd = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        this.context =  httpd.createContext("/");
        context.setHandler(Server::manageRequest);
        httpd.start();
    } // end constructor Server
    private final void iniSitiosVirtuales() throws FileNotFoundException, IOException{
        File file = new File("/home/juansebastianbarretojimenez/Escritorio/Comunicaciones_y_Redes/Proyecto/ServerProxyWeb/ServerProxyWeb/src/server/virtualA.txt");
        FileReader fr= new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while( (line=br.readLine())!= null ){
            String[] parts = line.split(",");
            FileVirtual fv = new FileVirtual(parts[0], parts[1], parts[2]);
            this.listaFiles.add(fv);
        }
    }
            
    private static void manageRequest(HttpExchange exchange) throws IOException {
        final int CODIGO_RESPUESTA = 200;
        String contenido = "Respuesta desde el servidor HTTP Maria desde clase";
        
        //System.out.println(" -----Método Inicial: " + exchange.getRequestMethod());
        
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
        
        
        //urlS = reviewVirtualWeb(urlS);
        //urlS = "http://test-redes.125mb.com/";
        String mod="";
        String nHost = "";
        boolean cambio= false;

        String urlS = exchange.getRequestURI().toString();
        FileVirtual fAux = verificarSV( exchange.getRequestHeaders().get("Host").get(0)) ;
        if(fAux != null){
            cambio = true;
            mod = fAux.getHostReal() + "/" + fAux.getDirectorio();     
            nHost = fAux.getHostReal();
            urlS = urlS.replace(fAux.getNombreHostVirtual(),mod);
           
        }
        URL url;
        if(cambio == false){
            url  = exchange.getRequestURI().toURL();
        }else{
            System.err.println(" entre al coso URLLL_ "+ urlS);
            url =  new URL(urlS);
        }
        
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        Headers heads  = exchange.getRequestHeaders();
        for(Map.Entry<String, List<String>> s : heads.entrySet()){
            if(s.getKey().contains("Host") && cambio== true){
                con.setRequestProperty(s.getKey(), nHost);
            }else{
                con.setRequestProperty(s.getKey(), s.getValue().toString());
            }
            
        }        
        
        con.setRequestMethod("GET");
        
        System.out.println("AQUI VA LA RESPUESTA");
        int status = con.getResponseCode();
        System.out.println("Status code: " + status);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println();
        String contentS = content.toString();
        exchange.sendResponseHeaders(status,contentS.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(contentS.getBytes());
        os.close();
        
       
//        System.out.println("Se mando la respuesta: " + contentS);
    } // end getRequest
    
    private static void postRequest(HttpExchange exchange)throws ProtocolException, MalformedURLException, IOException{ 
        showInformationRequest(exchange);
        /// Make Requests
        URL url = exchange.getRequestURI().toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader((exchange.getRequestBody())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null)
          sb.append(output);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setInstanceFollowRedirects(false);
        Headers heads  = exchange.getRequestHeaders();
        for(Map.Entry<String, List<String>> s : heads.entrySet())
            con.setRequestProperty(s.getKey(), s.getValue().toString());
        byte[] postData = sb.toString().getBytes();
        int    postDataLength = postData.length;
        con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
        con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        con.setRequestProperty("charset", "utf-8");
        con.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
           wr.write( postData );
           wr.close();
        }
        /// Read Response
        int status = con.getResponseCode();
        System.out.println("Status code: " + status);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
            content.append(inputLine);
        in.close();
        String contentS = content.toString();
        exchange.sendResponseHeaders(status,contentS.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(contentS.getBytes());
        os.close();
        System.out.println("Se mando la respuesta: " + contentS);
    } // end postRequest
    
    private static FileVirtual verificarSV (String host){
        FileVirtual res = null;
        System.out.println(Server.listaFiles.size());
        for(FileVirtual f : Server.listaFiles){
      
            if( f.getNombreHostVirtual().equals(host) ){
                return f;
            }
        }
        return res;
    }
    
    private static void showInformationRequest(HttpExchange exchange) throws IOException{
        if(exchange.getRequestMethod().equals("POST")){
        System.out.println("Metodo: " + exchange.getRequestMethod());
        System.out.println();
        System.out.println("  Encabezados:");
        System.out.println();
        exchange.getRequestHeaders().entrySet().forEach(System.out::println);
        
        
        
//        System.out.println("Encabezados:");
//        System.out.println(exchange.getRequestHeaders().toString());
        /*
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
 
 
        */
        
//        if(exchange.getRequestMethod().equals("POST")){
//            BufferedReader br = new BufferedReader(new InputStreamReader((exchange.getRequestBody())));
//            StringBuilder sb = new StringBuilder();
//            String output;
//            while ((output = br.readLine()) != null) {
//              sb.append(output);
//            }
//            System.out.println("  Post Body:");
//            System.out.println(sb.toString());
//            System.out.println();
//        }
//        
    } // end showInformationRequest
    }
    
} // end class server
