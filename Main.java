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
            if(nextLine.contains("PING")){
                outputStream.write("+PONG\r\n".getBytes());
            }
        }
    }
}