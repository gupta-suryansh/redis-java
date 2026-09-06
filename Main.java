import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;


public class Main{
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try{
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);

            while(true){
                clientSocket = serverSocket.accept();
                Socket finalClienSocket = clientSocket;
                
                CompletableFuture.runAsync(()->{
                    try{
                        handleClient(finalClienSocket);
                    }catch(IOException e){
                        throw new RuntimeException(e);
                    }               
                 });
            }
        }catch(IOException e){
            System.out.println("IOException: "+e.getMessage());
        }finally{
            try{
                if(clientSocket!=null){
                    clientSocket.close();  //in case of erros or manually closing 
                }
            }catch(IOException e){
                System.out.println("IOException: "+e.getMessage());
            }
        }
    }

    public static void handleClient(Socket clientSocket) throws IOException{
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
            Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String nextLine = sc.nextLine();
            System.out.println(nextLine);
            if(nextLine.contains("PING")){
                outputStream.write("+PONG\r\n".getBytes());
            }
            if(nextLine.contains("ECHO")){
                String respHeader = sc.nextLine();
                String respBody = sc.nextLine();
                String response = respHeader+"\r\n"+respBody+"\r\n";
                outputStream.write(response.getBytes());
            }
        }
    }

    public static String encodingReString(String s){
        String resp = "$";
        resp+=s.length();
        resp+="\r\n";
        resp+=s;
        resp+="\r\n";
        return resp;
    }
}