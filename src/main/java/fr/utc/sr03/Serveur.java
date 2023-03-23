package fr.utc.sr03;

import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.*;


public class Serveur {
    public static void main( String[] args ){
        Vector<Socket> sockets =new Vector<Socket>();
        Vector<String> socketsname=new Vector<String>();///合并成一个class

        try{
             ServerSocket serverSocket=new ServerSocket(10080);//set server and port
             System.out.println("Server is running");

             while(true) {

                 Socket socket = serverSocket.accept();///accept clients asks
                 sockets.add(socket);

                 MessageReceptorSender msgreceptor = new MessageReceptorSender(socket,sockets,socketsname);
                 msgreceptor.start();
             }
         }catch (IOException ex){
             Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
}
//////////////////////////////////////////////////////////
//储存链接的socket 和其聊天使用的名称

///////////////////////////////////////

///////////////////////////////////
// 消息接收和发送
 class MessageReceptorSender extends Thread{
    Socket client =null;
    Vector<Socket> clients;
    Vector<String> clientsname;
    MessageReceptorSender(Socket client,Vector <Socket>clients,Vector<String> clientsname){     //initial
        this.client=client;
        this.clients=clients;
        this.clientsname=clientsname;
    }
    @Override
    public void run(){

        try {
             DataInputStream in=new DataInputStream(client.getInputStream());
             String messagereceive=null;
             boolean name=false;
             while(!name){
                 name=true;
                 messagereceive=in.readUTF();

                 for (int i=0;i<clientsname.size();i++){
                     if(clientsname.get(i).equals(messagereceive)){
                         name=false;
                         DataOutputStream out1=new DataOutputStream(client.getOutputStream());
                         out1.writeUTF("rename");
                         break;
                     }
                 }

             }
            DataOutputStream out1=new DataOutputStream(client.getOutputStream());
            out1.writeUTF("name");
             clientsname.add(messagereceive);

             while(true){
                 if(!client.isClosed()){
                     messagereceive=in.readUTF();

                     if(!messagereceive.equals("exit")){
                         for(int i=0;i<clients.size();i++){
                             if(!clients.get(i).isClosed()){
                                 DataOutputStream out=new DataOutputStream(clients.get(i).getOutputStream());
                                 out.writeUTF(messagereceive);
                             }
                         }
                     }else{
                         int c=-1;
                         for(int i=0;i<clients.size();i++){
                             if (clients.get(i)==client){
                                  c =i;
                             }
                         }
                         for(int i=0;i<clients.size();i++) {
                             if (clients.get(i) != client && !clients.get(i).isClosed()) {

                                 DataOutputStream out = new DataOutputStream(clients.get(i).getOutputStream());
                                 out.writeUTF(clientsname.get(c) + " has exited the chat");
                             }
                         }

                         DataOutputStream out=new DataOutputStream(client.getOutputStream());
                         out.writeUTF("exit");
                         in.close();
                         out.close();
                         client.close();

                     }
                 }else {
                     break;
                 }
                 }
        }catch (IOException ex){
             Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/**  原本用作服务器发送消息 发送和接受合并在一起写 暂且注释保留
class MessageSender extends Thread{
    private Vector <Socket>client;
    MessageSender(Vector <Socket>client){
        this.client=client;
    }
    public void run(){
        try{
            Scanner scanner =new Scanner(System.in);//待修改

            while(true){
                String st=scanner.nextLine();
                for(int i=0;i<client.size();i++){
                    DataOutputStream ous=new DataOutputStream(client.get(i).getOutputStream());
                    ous.writeUTF(i+":"+st);// 待修改
                }

            }
        }catch (IOException ex){
            Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}*/