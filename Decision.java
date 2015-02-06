import java.io.*;
import java.util.ArrayList;
 
public class Decision implements Serializable {
    public String text;
 
    Decision(String message)
    {
        this.text = message;
    }

    public String DecisionToString(){ //*
        return "Decidiu-se que: " + this.text;
    }
}