import java.rmi.*;
import java.util.ArrayList;

public interface RMI_I extends Remote {

	public User loginUser(User e) throws java.rmi.RemoteException;
	public User registerUser(User e) throws java.rmi.RemoteException;
	public Meeting registerMeeting(Meeting m) throws java.rmi.RemoteException;
	public Chat openChat(Chat c) throws java.rmi.RemoteException;
	public User actualizaUser(User e) throws java.rmi.RemoteException;
	public ArrayList <User> listUsers() throws java.rmi.RemoteException;
	public void saveChat(Chat c) throws java.rmi.RemoteException;
	public void logout(User u) throws java.rmi.RemoteException;
	public ArrayList <User> listUsersOn() throws java.rmi.RemoteException;
	public ArrayList <Meeting> upcoming(String d, User u) throws java.rmi.RemoteException;
}