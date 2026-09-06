import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import Components.TcpServer;


public class Main{
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TcpServer app = context.getBean(TcpServer.class);
        app.startServer();

        
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