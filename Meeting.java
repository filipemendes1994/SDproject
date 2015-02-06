import java.io.*;
import java.util.ArrayList;
 
 
public class Meeting implements Serializable {
    public int idMeeting =-1;
    public String title, local;
    public ArrayList <Invite> convites;
    public ArrayList <Chat> Chats;
    public DataHora horario;
    
    Meeting(String nome, String local, DataHora horario)
    {
        this.title = nome;
        this.local = local;
        this.horario = horario;

        this.Chats = new ArrayList<Chat>();
        this.convites = new ArrayList<Invite>(); 
    }

    public String toString(){
    	String participantes = "";
    	for(Invite i: this.convites)
    		if(i.reply == true)
    			participantes += i.userConvidado + ",";
    	return "Titulo: " + this.title + "\nParticipantes: " + participantes + "\n" + this.horario.toString() + "\nLocal: " + this.local;
    }
}