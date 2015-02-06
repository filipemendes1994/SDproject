import java.io.*;
 
public class DataHora implements Serializable{
    public  int dia, mes, ano, h, m;
    
    public DataHora(int dia, int mes, int ano, int h, int m)
    {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.h = h;
        this.m = m;
    }

    public String toString(){
    	return "Data: " + this.dia + "/" + this.mes + "/" + this.ano + "\nHoras: " + this.h + ":" + this.m;
    }
}