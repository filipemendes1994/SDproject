import java.io.*;
import java.util.ArrayList;
 
 
public class Invite implements Serializable {
    public String userConvida, userConvidado;
    public Meeting thisMeeting;
    public int idUserConvidado, idUserConvida;
    public boolean reply, accepted;

    Invite(int idUserConvidado, int idUserConvida, String nameConvida, String nameConvidado, Meeting este)
    {
        this.userConvida = nameConvida;
        this.userConvidado = nameConvidado;
        this.idUserConvidado = idUserConvidado;
        this.idUserConvida = idUserConvida;
        this.thisMeeting = este;
        this.reply = false;

    }

    Invite(int idUser, Meeting e, String userConvidado)
    {
        this.idUserConvidado = idUser;
        this.userConvidado = userConvidado;
        this.thisMeeting = e;
        this.reply = true;
    }

    public String toString(){
        return String.valueOf(this.thisMeeting.idMeeting) + " - " + this.thisMeeting.title + " convidado por " + this.userConvida;
    }

    public String toStringAll(){
        return this.toString() + "\n" + this.thisMeeting.toString();
    }
}




