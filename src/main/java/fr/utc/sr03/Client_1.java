package fr.utc.sr03;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client_1 {

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