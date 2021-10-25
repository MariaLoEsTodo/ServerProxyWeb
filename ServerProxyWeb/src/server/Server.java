/**************************************************************************
*   Comunicaciones y Redes Proyecto: Proxy Web María
*   Members:
*       - Juan Sebastián Barreto Jimenéz
*       - Janet Chen He
*       - María José Niño Rodriguez
*       - David Santiago Quintana Echavarria
*   File: 
*       Server.java
*   Purpose: 
*       Class for the web proxy that contains the necessary attributes
*       and methods to act as a server with clients and as a client 
*       with the internet
**************************************************************************/
/* Package */
package server;

/* Imports of libraries */
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
    
    private final int PORT = 8080;
    private HttpServer httpd;  
    private HttpContext context;
    private final String dirArchi = "/home/juansebastianbarretojimenez/Escritorio/Comunicaciones_y_Redes/Proyecto/ServerProxyWeb/ServerProxyWeb/src/server/virtualA.txt";
    private static ArrayList<FileVirtual> listaFiles = new ArrayList<FileVirtual>();
    
    public Server() throws IOException {
        iniSitiosVirtuales();
        this.httpd = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.context =  httpd.createContext("/");
        context.setHandler(Server::manageRequest);
        httpd.start();
    } // end builder Server
    
    /**************************************************************************
    *   Function:   manageRequest()
    *   Purpose:    Routine to select POST and GET requests
    *   Argumentos:
    *       exchange:    Object with the client's request.
    *   Retorno:
    *       void
    **************************************************************************/
    private static void manageRequest(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("GET")){
            getRequest(exchange);
        }else if(exchange.getRequestMethod().equals("POST")){
            postRequest(exchange);
        }
    } // end manageRequest
    
    /**************************************************************************
    *   Function:   iniSitiosVirtuales()
    *   Purpose:    Routine to read the virtual sites file
    *   Argumentos:
    *       void
    *   Retorno:
    *       void
    **************************************************************************/
    private final void iniSitiosVirtuales() throws FileNotFoundException, IOException{
        File file = new File(dirArchi);
        FileReader fr= new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while( (line=br.readLine())!= null ){
            String[] parts = line.split(",");
            FileVirtual fv = new FileVirtual(parts[0], parts[1], parts[2]);
            this.listaFiles.add(fv);
        }
    } // end iniSitiosVirtuales
    
    /**************************************************************************
    *   Function:   getRequest()
    *   Purpose:    Routine to handle GET requests
    *   Argumentos:
    *       exchange:    Object with the client's request.
    *   Retorno:
    *       void
    **************************************************************************/
    private static void getRequest(HttpExchange exchange) throws ProtocolException, MalformedURLException, IOException{
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("/home/juansebastianbarretojimenez/Escritorio/Comunicaciones_y_Redes/Proyecto/ServerProxyWeb/ServerProxyWeb/src/server/log.txt",true);
            pw = new PrintWriter(fichero);
            System.out.println();
            pw.println();
            pw.println();
            System.out.println("Solicitud Servidor Proxy Web:");
            pw.println("Solicitud Servidor Proxy Web:");
            System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol());
            pw.println(exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol());
            System.out.println("Headers:");
            pw.println("Headers:");
            exchange.getRequestHeaders().entrySet().forEach(System.out::println);
            exchange.getRequestHeaders().entrySet().forEach(pw::println);
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

            System.out.println();
            pw.println();
            System.out.println("Reenvio Solicitud Servidor Proxy Web:");
            pw.println("Reenvio Solicitud Servidor Proxy Web:");
            System.out.println(exchange.getRequestMethod() + " " + url.toString() + " " + exchange.getProtocol());
            pw.println(exchange.getRequestMethod() + " " + url.toString() + " " + exchange.getProtocol());
            System.out.println("Headers:");
            pw.println("Headers:");
            con.getRequestProperties().entrySet().forEach(System.out::println);
            con.getRequestProperties().entrySet().forEach(pw::println);

            con.setRequestMethod("GET");
            int status = con.getResponseCode();

            System.out.println();
            pw.println();
            System.out.println("Respuesta del servidor");
            pw.println("Respuesta del servidor");
            System.out.println( exchange.getProtocol()+" " + con.getResponseCode());
            pw.println( exchange.getProtocol()+" " + con.getResponseCode());
            System.out.println("Date: " + con.getDate());
            pw.println("Date: " + con.getDate());
            System.out.println("Last modification: " + con.getLastModified());
            pw.println("Last modification: " + con.getLastModified());
            System.out.println("Content-Length : " +con.getContentLength());
            pw.println("Content-Length : " +con.getContentLength());
            System.out.println("Content Type: " +con.getContentType());
            pw.println("Content Type: " +con.getContentType());

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    } // end getRequest
    
    /**************************************************************************
    *   Function:   postRequest()
    *   Purpose:    Routine to handle POST requests
    *   Argumentos:
    *       exchange:    Object with the client's request.
    *   Retorno:
    *       void
    **************************************************************************/
    private static void postRequest(HttpExchange exchange)throws ProtocolException, MalformedURLException, IOException{ 
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("/home/juansebastianbarretojimenez/Escritorio/Comunicaciones_y_Redes/Proyecto/ServerProxyWeb/ServerProxyWeb/src/server/log.txt",true);
            pw = new PrintWriter(fichero);
            System.out.println();
            pw.println();
            pw.println();
            System.out.println("Solicitud Servidor Proxy Web:");
            pw.println("Solicitud Servidor Proxy Web:");
            System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol());
            pw.println(exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol());
            System.out.println("Headers:");
            pw.println("Headers:");
            exchange.getRequestHeaders().entrySet().forEach(System.out::println);
            exchange.getRequestHeaders().entrySet().forEach(pw::println);
            BufferedReader br = new BufferedReader(new InputStreamReader((exchange.getRequestBody())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
              sb.append(output);
            }
            System.out.println("Body:");
            System.out.println(sb.toString());
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
                url =  new URL(urlS);
            }

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
            System.out.println();
            System.out.println("Reenvio Solicitud Servidor Proxy Web:");
            System.out.println(exchange.getRequestMethod() + " " + url.toString() + " " + exchange.getProtocol());
            System.out.println("Headers:");
            exchange.getRequestHeaders().entrySet().forEach(System.out::println);
            exchange.getRequestHeaders().entrySet().forEach(pw::println);
            System.out.println("Body:");
            pw.println("Body:");
            System.out.println(postData);
            pw.println(postData);
            /// Read Response
            int status = con.getResponseCode();
            System.out.println("Respuesta Servidor Proxy Web:");
            pw.println("Respuesta Servidor Proxy Web:");
            System.out.println( exchange.getProtocol() +" "+ con.getResponseCode());
            pw.println( exchange.getProtocol() +" "+ con.getResponseCode());
            System.out.println("Date: " + con.getDate());
            pw.println("Date: " + con.getDate());
            System.out.println("Last modification: " + con.getLastModified());
            pw.println("Last modification: " + con.getLastModified());
            System.out.println("Content-Length : " +con.getContentLength());
            pw.println("Content-Length : " +con.getContentLength());
            System.out.println("Content Type: " +con.getContentType());
            pw.println("Content Type: " +con.getContentType());

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fichero.close();
        }
    } // end postRequest
    
    /**************************************************************************
    *   Function:   verificarSV()
    *   Purpose:    Routine to check if a client's request is a virtual site
    *   Argumentos:
    *       host:  String with the name of the requested host.
    *   Retorno:
    *       FileVirtual: Object with the virtual site or null.
    **************************************************************************/
    private static FileVirtual verificarSV (String host){
        FileVirtual res = null;
        System.out.println(Server.listaFiles.size());
        for(FileVirtual f : Server.listaFiles){
      
            if( f.getNombreHostVirtual().equals(host) ){
                return f;
            }
        }
        return res;
    } // end verificarSV
    
} // end class Server