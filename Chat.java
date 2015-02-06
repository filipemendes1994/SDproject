import java.io.*;
import java.util.ArrayList;
 

 
public class Chat implements Serializable {
    public int idChat = -1, idMeeting;
    public String text;
    public ArrayList <String> messages;
    public Decision keyDecision;
    public ArrayList <User> participantes;

    Chat(String text, int idMeeting)
    {
        this.text = text;
        this.idMeeting = idMeeting;
        messages = new ArrayList<String>();
        participantes = new ArrayList<User>();
    }
}