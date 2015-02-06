// TCPServer2.java: Multithreaded server
import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;

class Connection extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;
    int thread_number;
    User thisOne;
    RMI_I h;
    
    public Connection(Socket aClientSocket, int numero, RMI_I h) {
        thread_number = numero;
        clientSocket = aClientSocket;

        
        try{
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch(IOException e){System.out.println("Connection:" + e.getMessage());}

        this.h = h;
        this.start();
    }
    //=============================
    public void run(){
        String resposta;
        int signals;
        Object x;
        User thisUser;
        try{
            while(true)
            {
                while(true)
                {
                    x = in.readObject();

                    if(x instanceof Meeting)
                    {
                        out.writeObject(h.registerMeeting((Meeting)x));
                        out.flush();
                    } 
                    else if (x instanceof String)
                    {
                        if(((String)x).equals("listUsers"))
                        {
                            out.writeObject(h.listUsers());
                            out.flush();
                        }
                        else if(((String)x).equals("exit"))
                        {
                            User utilizador = (User) in.readObject();
                            h.logout(utilizador);
                            break;
                        }
                        else if(((String)x).equals("openChat"))
                        {
                            Chat thisChat = (Chat) in.readObject();
                            out.writeObject(h.openChat(thisChat));
                            out.flush();
                        }
                        else if(((String)x).equals("login"))
                        {
                            while(true)
                            {
                                thisUser = null;
                                User e = (User) in.readObject();
                                thisUser = h.loginUser(e);
                                out.writeObject(thisUser);
                                out.flush();
                                
                                x = in.readObject();
                                if(x instanceof User)
                                {
                                    out.writeObject(h.registerUser((User)x));      
                                    out.flush(); 
                                    thisOne = (User) x;         
                                    break;
                                }
                                else if(x instanceof Integer)
                                {
                                    if((Integer) x==0)
                                        break;
                                }
                            }
                        }
                        else if(((String)x).equals("saveChat"))
                        {
                            h.saveChat((Chat)in.readObject());
                        }
                        else if(((String)x).equals("actualizaUser"))
                        {
                            User e = (User)in.readObject();
                            e = h.actualizaUser(e);
                            out.writeObject(e);
                            out.flush();
                        }
                        else if(((String)x).equals("listUsersOn")) {
                            out.writeObject(h.listUsersOn());
                            out.flush();
                        }
                        else if(((String)x).equals("upcoming")){
                            String data = (String) in.readObject();
                            User u = (User) in.readObject();
                            out.writeObject(h.upcoming(data, u));
                            out.flush();
                        }
                    }
                    else if(x instanceof User)
                    {
                        System.out.println("entrou aqui");
                        out.writeObject(h.registerUser((User)x));
                        out.flush();
                    }
                }
            }
        }catch(EOFException e){
            System.out.println("EOF:" + e);
            try{
            if(thisOne!=null)
                h.logout(thisOne);}
            catch(RemoteException z){} //não consigo meter a funcionar, se poderes olha para isto
        }catch(IOException e){
            System.out.println("IO:" + e);
            //matar thread!!!
        }catch(ClassNotFoundException e){
            System.out.println("CNE:" + e);
        }
    }   
}


class UDP extends Thread{

    public UDP(){
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>ola");
        this.start();
    }

    public void run(){
        try{
            //System.out.println("ola");
            DatagramSocket udpSocket = new DatagramSocket(8000);  
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024]; 
            while(true){       
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);    
                udpSocket.receive(receivePacket);           
                String sentence = new String( receivePacket.getData());     
                //System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();  
                int port = receivePacket.getPort();     
                String capitalizedSentence = "ligado";     
                sendData = capitalizedSentence.getBytes();    
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);    
                udpSocket.send(sendPacket);   
            }    
        } catch (IOException e) {
            System.out.println("Erro na criação do coiso");
        }
    }
}


public class TCPServer {
    
 
    
    public static void main(String args[]) throws Exception {

        if (args.length < 2) {
            System.out.println("java TCPServer principalServerHostname portToListenIfPrincipal");
            System.exit(0);
        }
        int tentativas = 0;

        DatagramSocket udpSocket = new DatagramSocket();
        udpSocket.setSoTimeout(500);
        InetAddress IPAddress = InetAddress.getByName(args[0]);
        byte[] sendData = new byte[1024]; 
        byte[] receiveData = new byte[1024];
        String sentence = "ola"; 
        sendData = sentence.getBytes();

        while(true)
        {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8000); 
            udpSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try{
                udpSocket.receive(receivePacket);
                //System.out.println("Server ligado");
                if (tentativas>0){
                    System.out.println("Falha Temporária");
                    tentativas = 0;
                }
                tentativas = 0;
            } catch(SocketTimeoutException e){
                tentativas++;
                //System.out.println(tentativas);
                if(tentativas>3){
                    System.out.println("Falha Permanente");
                    udpSocket.close();
                    break;
                }
            }
        }


        new UDP();

        int numero=0; 

        String a;
        // usage: java HelloClient username
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
 
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        RMI_I h = null;
        try {
            //User user = new User();
            h = (RMI_I) LocateRegistry.getRegistry(7000).lookup("XPTO");
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
        }
        
        try{
            int serverPort = Integer.parseInt(args[1]);
            System.out.println("A Escuta no Porto " + args[1]);
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET="+listenSocket);
            while(true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
                numero ++;
                new Connection(clientSocket, numero, h);
            }
        }catch(IOException e){System.out.println("Listen:" + e.getMessage());}
    }
}
//= Thread para tratar de cada canal de comunicacao com um cliente
