package anki;


import java.util.Scanner;

public class ServerLauncher {
    public static void main(String[]args) throws Exception{
        ServerController server = new ServerController();
        Scanner scan = new Scanner(System.in);
        server.server.start();
        System.out.println("Server started on: " + server.server.getAddress() + " " + server.server.getPort());
        while(true){
            if(scan.nextLine().equals("d")){
                server.server.stop();
                System.exit(0);
            }

        }
    }
}
