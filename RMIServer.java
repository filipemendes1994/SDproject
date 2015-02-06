import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.lang.String;
import java.util.Scanner;


public class RMIServer extends UnicastRemoteObject implements RMI_I {

	public ArrayList <User> Utilizadores;
	public ArrayList <Meeting> Reunioes;
	//public ArrayList <Invite> Convites;
	public ArrayList <Chat> Chats;



	public RMIServer() throws RemoteException {
		super();
		this.Utilizadores = new ArrayList <User>();
		this.Reunioes = new ArrayList <Meeting>();
		//this.Convites = new ArrayList <Invite>();
		this.Chats = new ArrayList <Chat>();

		loadArrays();
	}


	public void logout(User u){
        u.on = false;
        this.Utilizadores.set(u.idUser, u);
        saveUser();
    }

	public ArrayList <Meeting> upcoming(String d, User u) throws RemoteException{
        ArrayList <Meeting> output = new ArrayList <Meeting>();
        int dia, mes, ano;
        String[] data = d.split("-");
        ano = Integer.parseInt(data[0]);
        mes = Integer.parseInt(data[1]);
        dia = Integer.parseInt(data[2]);

        for(Meeting m : this.Utilizadores.get(u.idUser).meets){
            if( (mes == m.horario.mes) && (dia<=m.horario.dia) && (ano==m.horario.ano)) output.add(m);
            if( (mes == m.horario.mes-1) && (dia>=m.horario.dia) && (ano==m.horario.ano)) output.add(m);
            if( (mes == 12) && (ano == m.horario.ano -1) && (m.horario.mes == 1) && (dia>=m.horario.dia)) output.add(m);
        }
        return output;
    }
	
	public ArrayList <User> listUsersOn() throws RemoteException {
        ArrayList <User> online = new ArrayList <User>();
        for(User e : this.Utilizadores){
            if (e.on==true) online.add(e);
        }

        return online;   
    }
    
	public User loginUser(User e) throws RemoteException {
		for(User d: this.Utilizadores)
		{
			if (d.name.equals(e.name))
				return d;
		}
		return null;
	}

	public User actualizaUser(User e) throws RemoteException {
		return this.Utilizadores.get(e.idUser);
	}

	public User registerUser(User e) throws RemoteException{
		if(e.idUser!=-1)
		{
			this.Utilizadores.set(e.idUser, e);
			System.out.println("actualizou " + e.name);
		}
		else
		{
			System.out.println(this.Utilizadores.size());
			System.out.println("---------------------");
			e.idUser = this.Utilizadores.size();
			this.Utilizadores.add(e);
			System.out.println("adicionou " + e.name);
		}

		saveUser();
		return e;
	}
 
    public Meeting registerMeeting(Meeting m) throws RemoteException{
    	System.out.println("->Meeting");
        if(m.idMeeting!=-1)
        { //alterar
            this.Reunioes.set(m.idMeeting, m);
        } 
        else 
        { //add
            m.idMeeting = this.Reunioes.size();
            Chat one = new Chat("Outros Assuntos", m.idMeeting);
            one.idChat = m.Chats.size();
            m.Chats.add(one);
            this.Chats.add(one);
            this.Reunioes.add(m);
        }
 
        saveMeet();
        return m;
    }

    public Chat openChat(Chat c) throws RemoteException{
    	System.out.println("-> Chat " + c.idChat);
    	System.out.println("-> Meeting " + c.idMeeting);
    	for(Chat x: this.Chats)
    	{
    		System.out.println("PROCURA");
    		System.out.println("-> Chat " + x.idChat);
    		System.out.println("-> Meeting " + x.idMeeting);
    		if(x.idChat == c.idChat && x.idMeeting == c.idMeeting)
    			return x;
    	}

    	return null;
    	
    }

    public void saveChat(Chat c) throws RemoteException{
    	int auxN=0;
    	for(Chat x: this.Chats)
    	{
    		if(x.idChat == c.idChat && x.idMeeting == c.idMeeting)
    		{
    			this.Chats.set(auxN,c);
    			break;
    		}
    		auxN++;
    	}
    	if(auxN==this.Chats.size())
    	{
    		this.Chats.add(c);
    	}

    	String aux = c.idMeeting + "-" + c.idChat + ".bin";
    	try{
            FileOutputStream chat = new FileOutputStream(aux);
            ObjectOutputStream chatOut = new ObjectOutputStream(chat);
            chatOut.writeObject(c.messages);
            chatOut.close();
        } catch(IOException e){
            System.out.println("IO:" + e);
        }
        saveChats();
        saveMeet();
    }

    public void saveUser(){
    	System.out.println("GUARDAR USERS");
        try{
            FileOutputStream users = new FileOutputStream("Users.bin");
            ObjectOutputStream usersOut = new ObjectOutputStream(users);
            usersOut.writeObject(this.Utilizadores);
            usersOut.close();
        } catch(IOException e){
            System.out.println("IO:" + e);
        }
    }
    public void saveMeet(){
    	System.out.println("GUARDAR MEETS");
        try{
            FileOutputStream meetings = new FileOutputStream("Meetings.bin");
            ObjectOutputStream meetingsOut = new ObjectOutputStream(meetings);
            meetingsOut.writeObject(this.Reunioes);
            meetingsOut.close();
        } catch(IOException e){
            System.out.println("IO:" + e);
        }
    }

    public void saveChats(){
    	System.out.println("GUARDAR CHATS");
        try{
            FileOutputStream chats = new FileOutputStream("Chats.bin");
            ObjectOutputStream chatsOut = new ObjectOutputStream(chats);
            chatsOut.writeObject(this.Chats);
            chatsOut.close();
        } catch(IOException e){
            System.out.println("IO:" + e);
        }
    }

	public ArrayList <User> listUsers() throws RemoteException {
		return this.Utilizadores;	
	}

	// =======================================================

    public void loadArrays()
    {
        try{
            FileInputStream users = new FileInputStream("Users.bin");
            ObjectInputStream usersIn = new ObjectInputStream(users);
            this.Utilizadores = (ArrayList<User>) usersIn.readObject();
            usersIn.close();
        } catch ( Exception ignored){}   
        
        try{
            FileInputStream meetings = new FileInputStream("Meetings.bin");
       		ObjectInputStream meetingsIn = new ObjectInputStream(meetings);
            this.Reunioes = (ArrayList<Meeting>) meetingsIn.readObject();
            meetingsIn.close();
        } catch ( Exception ignored){}

        try{
            FileInputStream chats = new FileInputStream("Chats.bin");
       		ObjectInputStream chatsIn = new ObjectInputStream(chats);
            this.Chats = (ArrayList<Chat>) chatsIn.readObject();
            chatsIn.close();
        } catch ( Exception ignored){}    

    }

    //========================================================

	public static void main(String args[]) {
		String a;

		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());

		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);


		try {
			RMIServer h = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("XPTO", h);
			System.out.println("RMIServer ready.");
		} catch (Exception re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		} 
	}
}
