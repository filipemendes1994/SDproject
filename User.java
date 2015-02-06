import java.io.*;
import java.util.ArrayList;
 
public class User implements Serializable {
    public String name, pass;
    public int idUser = -1;
    public ArrayList <Invite> convites;
    public ArrayList <Meeting> meets;
    public ArrayList <Action> actions;
    public ArrayList <Decision> keyDecisions;
    public boolean on;
 
    User(String name, String pass){
        this.name = name;
        this.pass = pass;
        this.idUser = -1;
        this.convites = new ArrayList <Invite>();
        this.meets = new ArrayList <Meeting>();
		this.actions = new ArrayList <Action>();    
		this.on = true; 
    }

    User(){

    }

    public String toString(){
        return this.idUser + " - " + this.name;
    }

    public String listMeetings(){
        StringBuilder all = new StringBuilder("");
        for(Meeting e: this.meets)
            all.append(Integer.toString(e.idMeeting) + " - " + e.title + "\n");
        return all.toString();
    }
}