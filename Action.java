import java.io.*;

 
public class Action implements Serializable {
    public Boolean done=false;
    public String text, textMeeting;
    public int idAction;

    Action(String text, String textMeeting)
    {
        this.text = text; 
        this.textMeeting = textMeeting;
    }

    public String toString()
    {
        if(this.done==false)
            return "Action ID:" + this.idAction + "\nTarefa: " + this.text + "\nMeeting: " + this.textMeeting + "(Por Fazer)";
        return "Action ID:" + this.idAction + "Tarefa: " + this.text + "\nMeeting: " + this.textMeeting + " (Feito)";
    }
}