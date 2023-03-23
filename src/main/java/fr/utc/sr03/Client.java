package fr.utc.sr03;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private Socket socket;

    public static void main(String[] args){
        try{
            System.out.println("pls enter your name");
            Scanner sc =new Scanner(System.in);
            String name =sc.nextLine();

            Socket client = new Socket ("localhost", 10080);
            DataOutputStream out=new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());

            out.writeUTF(name);
            while(in.readUTF().equals("rename")){
                System.out.println("pls enter your name again,there has one name");
                 name =sc.nextLine();
                out.writeUTF(name);
            }


            out.writeUTF(name+" has entred the chat");



            ClientReceptor receptor =new ClientReceptor(client);
            receptor.start();
            ClientSender   sender=new ClientSender(client,name);
            sender.start();
            System.out.println("-----------------------");
        }catch (IOException ex){
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class ClientReceptor extends Thread {
    private Socket client;

    public ClientReceptor(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(client.getInputStream());
            while (true) {
                String message=in.readUTF();
                if(message.equals("exit")){
                     System.out.println("-----exit------------");

                }else{
                    System.out.println(message);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);

        }


    }
}
class ClientSender extends Thread{
    private Socket socket;
    private String name;
    ClientSender(Socket socket,String name){
        this.socket=socket;
        this.name=name;
    }
    public void run(){
        try{
            DataOutputStream out=new DataOutputStream(socket.getOutputStream()); //待修改
            Scanner scanner =new Scanner(System.in);
            while(true){
                String st=scanner.nextLine();
                if (st.equals("exit")){
                    out.writeUTF("exit");
                }else {
                    out.writeUTF(name + " a dit:" + st);// 待修改
                }
            }


        }catch (IOException ex){
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
