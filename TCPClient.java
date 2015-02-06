
import java.net.*;
import java.io.*;
import java.rmi.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.Console;
import java.lang.*;
import java.util.*;
import java.text.*;

public class TCPClient {

	public static Invite searchInvite(User e, int idMeeting)
	{
		for(Invite i: e.convites)
		{
			if(i.thisMeeting.idMeeting == idMeeting)
			{
				return i;
			}
		}
		return null;
	}

	public static Meeting searchMeeting(User e, int idMeeting)
	{
		for(Meeting m: e.meets)
		{
			if(m.idMeeting == idMeeting)
			{
				//System.out.println("RETORNANDO " + m.title + "ID." + m.idMeeting);
				return m;
			}
		}
		return null;
	}
	
	public static Chat searchChat(Meeting e, int idChat)
	{
		for(Chat m: e.Chats)
		{
			if(m.idChat == idChat)
			{
				return m;
			}
		}
		return null;
	}

	public static void limparEcra() throws IOException
	{
		for(int i = 0; i<100; i++)
			System.out.println("\n");
	}

	public static Socket changeServer(Socket s, int[] port, String[] ips, int serverUsed) throws Exception{ 
		
		
		if(serverUsed == 0){
			//s.close();
			s = new Socket(ips[1], port[1]);
			s.setSoTimeout(1000);
			
			return s;
		} else {
			//s.close();
			s = new Socket(ips[0], port[0]);
			s.setSoTimeout(1000);
			
			return s;
		}
	}


    public static void main(String args[]) {
	// args[0] <- hostname of destination
		if (args.length < 4) {
		    System.out.println("java TCPClient hostnameServer portServer hostnameServerBackup portServerBackup");
		    System.exit(0);
		}

		int serverUsed = 0;
		Socket s = null;

		String[] ips = new String[2];
		int[] ports = new int[2];
		ips[0] = args[0];
		ips[1] = args[2];
		ports[0] = Integer.parseInt(args[1]);
		ports[1] = Integer.parseInt(args[3]);

		
		int response, signals = 0, auxN = 0;
		User thisUser, auxUser;

		String received, aux;

		Scanner sc = new Scanner(System.in);
		try {
		    // 1o passo
		    s = new Socket(ips[0], ports[0]);
		    s.setSoTimeout(1000);

		    System.out.println("SOCKET=" + s);
		    // 2o passo
		    
		    ObjectInputStream input = new ObjectInputStream(s.getInputStream());
		    ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
		    

		    String user = "";
		    String pass = "";

		    // login
		    while(true)
		    {
			   	while(true)
			   	{
			   		//limparEcra();
			   		while(true){
			  			try{
					   		output.writeObject("login");
					   		output.flush();
				  			break;
			  			} catch(IOException e){
			  				s.close();
			  				try{s = changeServer(s, ports, ips, serverUsed);}
			  				catch(Exception z){}
			  				if(serverUsed==0)
			  					serverUsed=1;
			  				else
			  					serverUsed=0;
							input = new ObjectInputStream(s.getInputStream());
		    				output = new ObjectOutputStream(s.getOutputStream());
			  			}
			  		}

			   		thisUser=null;
			   		auxUser = null;
			   		user = "";
		    		pass = "";
				    System.out.println("Novo Registo(1) ou Login(2)");  
				  	aux= sc.nextLine();
				  	auxN= Integer.parseInt(aux);
				  	if(auxN == 1 || auxN == 2)
				  	{
				  		System.out.println("User: ");
					    user = sc.nextLine();
					   	System.out.println("Pass: ");
					   	pass = sc.nextLine();
					   	thisUser = new User(user,pass);  
				  		while(true)
				  		{
				  			try{
						  		//output.reset();
							    output.writeObject(thisUser);
							   	output.flush();
							   	auxUser = (User) input.readObject();
					  			break;
				  			} catch(IOException e){
				  				s.close();
				  				try{s = changeServer(s, ports, ips, serverUsed);}
				  				catch(Exception z){}
				  				if(serverUsed==0)
			  						serverUsed=1;
			  					else
			  						serverUsed=0;
								input = new ObjectInputStream(s.getInputStream());
			    				output = new ObjectOutputStream(s.getOutputStream());
				  			}
				  		}

					   	if(auxN == 1 && auxUser == null)
					   	{
					   		System.out.println("Registo Bem Sucedido");
					   		while(true){
								try{
							   		output.writeObject(thisUser);
							   		output.flush();	
									thisUser=(User) input.readObject();
									break;
								} catch(IOException e){
									s.close();
									try{s = changeServer(s, ports, ips, serverUsed);}
									catch(Exception z){}
									if(serverUsed==0)
			  							serverUsed=1;
			  						else
			  							serverUsed=0;
			  						input = new ObjectInputStream(s.getInputStream());
									output = new ObjectOutputStream(s.getOutputStream());
								}
							}
					   		break;
					   	}
					   	else if(auxN == 2 && auxUser!=null)
					   	{
					   		if(thisUser.pass.equals(auxUser.pass))
					   		{	
						   		System.out.println("Login Realizado com Sucesso");
						   		thisUser = auxUser;
						   		while(true)
						   		{
									try{
								   		output.writeObject(0);
								   		output.flush();
										break;
									} catch(IOException e){
										s.close();
										try{s = changeServer(s, ports, ips, serverUsed);}
										catch(Exception z){}
										if(serverUsed==0)
			  								serverUsed=1;
			  							else
			  								serverUsed=0;
										input = new ObjectInputStream(s.getInputStream());
										output = new ObjectOutputStream(s.getOutputStream());
									}
								}
						   		break;
						   	}
						   	else
						   	{
						   		System.out.println("Erro no Login");
						   	}
					   	}
					   	else if (auxN==1)
					   	{
					   		System.out.println("Usuário já em uso");
					   	}
					   	else
					   	{

					   		System.out.println("Erro no Login");
					   	} 
					   	while(true){
							try{
							   	output.writeObject(1);
								output.flush();
								break;
							} catch(IOException e){
								s.close();
								try{s = changeServer(s, ports, ips, serverUsed);}
								catch(Exception z){}
								if(serverUsed==0)
	  								serverUsed=1;
	  							else
	  								serverUsed=0;
								input = new ObjectInputStream(s.getInputStream());
								output = new ObjectOutputStream(s.getOutputStream());
							}
						}
					}
				}

				// auxMenu
				//limparEcra();
				while(true)
				{
					//limparEcra();
					
					while(true){
						try{
							output.writeObject("actualizaUser");
							output.flush();
							output.writeObject(thisUser);
							output.flush();
							thisUser = (User) input.readObject();

							output.reset();
							
							break;
						} catch(IOException e){
							s.close();
							try{s = changeServer(s, ports, ips, serverUsed);}
							catch(Exception z){}
							if(serverUsed==0)
  								serverUsed=1;
  							else
  								serverUsed=0;
							input = new ObjectInputStream(s.getInputStream());
							output = new ObjectOutputStream(s.getOutputStream());
						}
					}
					

					ArrayList <User> allUsersOn;
					while(true){
						try{
							
							//System.out.println("ID USER DEPOIS: " + thisUser.idUser);

							output.writeObject("listUsersOn");
							output.flush();

							allUsersOn = (ArrayList <User>) input.readObject();
							
							break;
						} catch(IOException e){
							s.close();
							try{s = changeServer(s, ports, ips, serverUsed);}
							catch(Exception z){}
							if(serverUsed==0)
  								serverUsed=1;
  							else
  								serverUsed=0;
							input = new ObjectInputStream(s.getInputStream());
							output = new ObjectOutputStream(s.getOutputStream());
						}
					}

					limparEcra();
					System.out.println("Utilizadores online");
					for(User u : allUsersOn) System.out.println(u);
						
					System.out.println("------------------- Menu ---------------------");
					System.out.println("1- Ver Convites\n2- ToDoList\n3- Criar Meeting\n4- Editar Meeting\n5- Participar em Meeting\n6- Ver Decisões de um Meeting\n7- Meetings no próximo mês\n8- Sair");
					System.out.println("Escolha: ");	
					String menu = sc.nextLine();
					int auxMenu = Integer.parseInt(menu);
					
					if(auxMenu == 1)
					{
						limparEcra();
						while(true)
						{
							System.out.println("CONVITES\n");
							if(!thisUser.convites.isEmpty())
							{
								for(Invite x: thisUser.convites)
									if(x.reply==false)
										System.out.println(x.toString()); 
								System.out.println("\n");
								System.out.println("Detalhes(ID do Convite), Sair(-1)\nEscolha:"); 

								received = sc.nextLine();
								auxN = Integer.parseInt(received);
								if(auxN!=-1)
								{
									Invite thisInvite = searchInvite(thisUser,Integer.parseInt(received));
									if(thisInvite!=null)
									{
										thisUser.convites.remove(thisInvite);
										System.out.println(thisInvite.toStringAll()); //implementar
										System.out.println("Aceitar(0), Rejeitar(1), Voltar Atrás(2)\nEscolha: ");

										signals = sc.nextInt();
										if(signals==0)
										{
											thisInvite.reply=true;
											thisInvite.accepted=true;

											while(true){
												try{
													output.writeObject(thisInvite.thisMeeting);
													output.flush();
													Meeting trush = (Meeting) input.readObject();

													thisUser.convites.add(thisInvite);
													thisUser.meets.add(thisInvite.thisMeeting);

													output.writeObject(thisUser);
													output.flush();
													thisUser = (User) input.readObject();
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}
											thisUser = (User) input.readObject();
											System.out.println("Convite Aceite!");
										}
										else if(signals==1)
										{
											thisInvite.reply =true;
											thisInvite.accepted=false;
											thisUser.convites.set(Integer.parseInt(received),thisInvite);
											while(true){
												try{
													output.writeObject(thisUser);
													output.flush();
													thisUser = (User) input.readObject();
													System.out.println("Convite Recusado!");
													
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}


										}
									}
									else
										System.out.println("Erro!");
								}
								if(auxN==-1)

									break;
							}

							else
							{
								System.out.println("NAO HA CONVITES\n");
								break;
							}
						}
					}
					else if(auxMenu == 2)
					{
						while(true)
						{
							limparEcra();
						
							for(Action a: thisUser.actions)
								System.out.println(a.toString());
							System.out.println("ESCOLHA(ID ACTION) PARA REALIZAR ou SAIR(-1)");
							aux = sc.nextLine();
							auxN = Integer.parseInt(aux);
							if(auxN<thisUser.actions.size() && auxN>-1)
							{
							 	Action thisAction = thisUser.actions.get(auxN);
							 	thisAction.done = true;
							 	thisUser.actions.set(auxN, thisAction);
							 	while(true){
									try{
										output.writeObject(thisUser);
										output.flush();
										break;
									} catch(IOException e){
										s.close();
										try{s = changeServer(s, ports, ips, serverUsed);}
										catch(Exception z){}
										if(serverUsed==0)
			  								serverUsed=1;
			  							else
			  								serverUsed=0;
										input = new ObjectInputStream(s.getInputStream());
										output = new ObjectOutputStream(s.getOutputStream());
									}
								}
								break;
							}
							else if(auxN==-1)
								break;
						}
					}
					else if (auxMenu == 3)
					{
						limparEcra();
						while(true)
						{
							System.out.println("NOVO MEETING");
							System.out.printf("Nome: ");
							String nome = sc.nextLine();

							System.out.printf("Dia: ");
							String dia = sc.nextLine();
							int diaT = Integer.parseInt(dia);

							System.out.printf("Mes: ");
							String mes = sc.nextLine();
							int mesT = Integer.parseInt(mes);
							
							System.out.printf("Ano: ");
							String ano = sc.nextLine();
							int anoT = Integer.parseInt(ano);
							
							System.out.printf("Horas: ");
							String horas = sc.nextLine();
							int horasT = Integer.parseInt(horas);
							
							System.out.printf("Minutos: ");
							String mins = sc.nextLine();
							int minsT = Integer.parseInt(mins);
							
							System.out.printf("Local: ");
							String local = sc.nextLine();

							if(!nome.isEmpty() && !local.isEmpty() && diaT>0 && diaT<32 && mesT>0 && mesT<13
								&& anoT>=2014 && minsT>-1 && minsT<60 && horasT>=0 && horasT<25)
							{
								Meeting thisMeeting = new Meeting(nome,local, new DataHora(diaT,mesT,anoT,horasT, minsT));
								while(true){
									try{
										
										output.writeObject(thisMeeting);
										output.flush();
										thisMeeting = (Meeting) input.readObject();
										break;
									} catch(IOException e){
										
										s.close();
										try{s = changeServer(s, ports, ips, serverUsed);}
										catch(Exception z){}
										if(serverUsed==0)
			  								serverUsed=1;
			  							else
			  								serverUsed=0;
										input = new ObjectInputStream(s.getInputStream());
										output = new ObjectOutputStream(s.getOutputStream());
									}
								}
								

								//System.out.println("ID MEETING CRIADO: " + thisMeeting.idMeeting);

								Invite myself = new Invite(thisUser.idUser, thisMeeting, thisUser.name);
								thisMeeting.convites.add(myself);
								thisUser.convites.add(myself);
								thisUser.meets.add(thisMeeting);

								//System.out.println("ID USER ANTES: " + thisUser.idUser);
								ArrayList <User> allUsers;
								while(true){
									try{
										output.writeObject(thisUser);
										output.flush();
										thisUser = (User) input.readObject();
										//System.out.println("ID USER DEPOIS: " + thisUser.idUser);

										output.writeObject("listUsers");
										output.flush();

										allUsers = (ArrayList <User>) input.readObject();
										
										break;
									} catch(IOException e){
										s.close();
										try{s = changeServer(s, ports, ips, serverUsed);}
										catch(Exception z){}
										if(serverUsed==0)
			  								serverUsed=1;
			  							else
			  								serverUsed=0;
										input = new ObjectInputStream(s.getInputStream());
										output = new ObjectOutputStream(s.getOutputStream());
									}
								}
								while(true)
								{
									System.out.println("UTILIZADORES A CONVIDAR: ");
									for(User d: allUsers)
										if(!d.name.equals(thisUser.name))
											System.out.println(d.idUser + " - " + d.name);

									System.out.println("Escolha(ID do User) ou Sair(-1): ");
									String idUser = sc.nextLine();
									auxN = Integer.parseInt(idUser);
									if(auxN==-1)
									{
										while(true){
											try{
												output.writeObject(thisMeeting);
												output.flush();
												thisMeeting = (Meeting) input.readObject();
												//System.out.println("ID do MEETING depois: "+ thisMeeting.idMeeting);
												
												//for(Meeting m: thisUser.meets)
												//	System.out.println("ID." + m.idMeeting + " Titulo."+m.title);
												output.writeObject(thisUser);
												output.flush();
												thisUser = (User) input.readObject();
												
												break;
											} catch(IOException e){
												s.close();
												try{s = changeServer(s, ports, ips, serverUsed);}
												catch(Exception z){}
												if(serverUsed==0)
					  								serverUsed=1;
					  							else
					  								serverUsed=0;
												input = new ObjectInputStream(s.getInputStream());
												output = new ObjectOutputStream(s.getOutputStream());
											}
										}
										
										//System.out.println("ID do MEETING antes: "+ thisMeeting.idMeeting);
										break;
									}
									else if(Integer.parseInt(idUser)<allUsers.size())
									{
										Invite auxI = searchInvite(allUsers.get(Integer.parseInt(idUser)), thisMeeting.idMeeting);
										if(auxI == null)
										{
											User trush;
											User invited = allUsers.get(Integer.parseInt(idUser));
											Invite thisInvite = new Invite(Integer.parseInt(idUser), thisUser.idUser, thisUser.name, invited.name, thisMeeting);
											invited.convites.add(thisInvite);
											thisMeeting.convites.add(thisInvite);
											while(true){
												try{
													output.writeObject(invited);
													output.flush();
													trush = (User) input.readObject();

													output.writeObject(thisMeeting);
													output.flush();
													thisMeeting = (Meeting) input.readObject();
													
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}



											System.out.println("Convite Enviado a " + allUsers.get(Integer.parseInt(idUser)).name);
										}
										else
											System.out.println("Já Convidado!");
									}
									else
										System.out.println("Não existe!");
								}

							}
							break;	
						}
					}	
					else if (auxMenu == 4) 
					{
						limparEcra();
					
						while(true)
						{
							for(Meeting m: thisUser.meets){
								System.out.println(m.idMeeting + " - " + m.title);
							}
							System.out.println("ESCOLHA O MEETING(ID) ou SAIR(-1)");
							aux = sc.nextLine();
							auxN = Integer.parseInt(aux);
							if(auxN!=-1)
							{
								int idm = Integer.parseInt(aux);
								Meeting thisMeeting = searchMeeting(thisUser, idm);
								if(thisMeeting!=null)
								{
									
									// LIMPAR JANELA
									System.out.println("---------- DETALHES -----------");
									System.out.println("Titulo - "+thisMeeting.title);
									System.out.println("ALTERAR(0), CONTINUAR(1)");
									auxN = sc.nextInt();
									sc.nextLine();
									if (auxN==0)
									{
										System.out.println("Insira: ");
										thisMeeting.title = sc.nextLine();
									}
									System.out.println("Data - "+thisMeeting.horario.toString());
									System.out.println("ALTERAR(0), CONTINUAR(1)");
									auxN = sc.nextInt();
									sc.nextLine();
									if (auxN==0)
									{
										while(true)
										{
											System.out.printf("Dia: ");
											String dia = sc.nextLine();
											int diaT = Integer.parseInt(dia);

											System.out.printf("Mes: ");
											String mes = sc.nextLine();
											int mesT = Integer.parseInt(mes);
											
											System.out.printf("Ano: ");
											String ano = sc.nextLine();
											int anoT = Integer.parseInt(ano);
											
											System.out.printf("Horas: ");
											String horas = sc.nextLine();
											int horasT = Integer.parseInt(horas);
											
											System.out.printf("Minutos: ");
											String mins = sc.nextLine();
											int minsT = Integer.parseInt(mins);
											if(diaT>0 && diaT<32 && mesT>0 && mesT<13 && anoT<2014 && minsT>-1 && minsT<60 && horasT>=0 && horasT<25)
											{
												thisMeeting.horario = new DataHora(diaT,mesT,anoT,horasT, minsT);
												break;
											}
											else
												System.out.println("Erro!");
										}
									}

									System.out.println("Local - "+thisMeeting.local);
									System.out.println("ALTERAR(0), CONTINUAR(1)");
									auxN = sc.nextInt();
									sc.nextLine();
									if (auxN==0)
									{
										System.out.println("Insira: ");
										thisMeeting.local = sc.nextLine();
									}

									// LIMPAR ECRA
									System.out.println("----- AGENDA ITEMS -----");
									while(true)
									{
										for (Chat f : thisMeeting.Chats){
											System.out.println(f.idChat + "-> " + f.text);
										}
										System.out.println("\n");
										System.out.println("ADICIONAR(0), MODIFICAR(1), ELIMINAR(2) PONTO DA ORDEM DE TRABALHOS ou CONTINUAR(3)");
										auxN = sc.nextInt();
										sc.nextLine();
										if(auxN==0)
										{
											System.out.println("INSIRA: ");
											aux=sc.nextLine();
											Chat dis = new Chat(aux, thisMeeting.idMeeting);
											dis.idChat = thisMeeting.Chats.size();
											while(true){
												try{
													output.writeObject("saveChat");
													output.flush();
													output.writeObject(dis);
													output.flush();
													
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}
											thisMeeting.Chats.add(dis);		
										}
										else if(auxN==1)
										{
											for (Chat f : thisMeeting.Chats)
												System.out.println(f.idChat + "-> " + f.text);
											System.out.println("ESCOLHA (ID DO PONTO DE ORDEM DE TRABALHOS): ");
											auxN = sc.nextInt();
											sc.nextLine();
											Chat thisChat = searchChat(thisMeeting, auxN);
											if(thisChat!=null)
											{
												System.out.println(thisChat.text + "\nALTERAR PARA: ");
												aux = sc.nextLine();
												thisChat.text = aux;

												while(true){
													try{
														output.writeObject("saveChat");
														output.flush();
														output.writeObject(thisChat);
														output.flush();
													
														
														break;
													} catch(IOException e){
														s.close();
														try{s = changeServer(s, ports, ips, serverUsed);}
														catch(Exception z){}
														if(serverUsed==0)
							  								serverUsed=1;
							  							else
							  								serverUsed=0;
														input = new ObjectInputStream(s.getInputStream());
														output = new ObjectOutputStream(s.getOutputStream());
													}
												}
												thisMeeting.Chats.set(thisChat.idChat, thisChat);
											}
											else
												System.out.println("Erro!");
										
										}
										else if(auxN==2)
										{
											for (Chat f : thisMeeting.Chats)
												System.out.println(f.idChat + "-> " + f.text);
											System.out.println("ESCOLHA (ID DO PONTO DE ORDEM DE TRABALHOS): ");
											auxN = sc.nextInt();
											Chat thisChat = searchChat(thisMeeting, auxN);
											if(thisChat!=null)
											{
												for(Chat f: thisMeeting.Chats)
												{
													if(f.idChat == thisChat.idChat && f.idMeeting==thisChat.idMeeting )
													{
														thisMeeting.Chats.remove(f);
														break;
													}
												}
												System.out.println("Item Apagado"); 

											}
											else
												System.out.println("Erro!");
										}
										else if (auxN==3)
										{
											while(true)
											{
												try
												{
													output.writeObject(thisMeeting);
													output.flush();	
													thisMeeting = (Meeting) input.readObject();								
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}

											break;
										}
									}

									ArrayList <User> allUsers;
									
									while(true){
										try{
											output.writeObject("listUsers");
											output.flush();
											allUsers = (ArrayList <User>) input.readObject();
											break;
										} catch(IOException e){
											s.close();
											try{s = changeServer(s, ports, ips, serverUsed);}
											catch(Exception z){}
											if(serverUsed==0)
				  								serverUsed=1;
				  							else
				  								serverUsed=0;
											input = new ObjectInputStream(s.getInputStream());
											output = new ObjectOutputStream(s.getOutputStream());
										}
									}

									while(true)
									{
										System.out.println("UTILIZADORES A CONVIDAR: ");
										for(User d: allUsers)
											if(!d.name.equals(thisUser.name))
												System.out.println(d.idUser + " - " + d.name);

										System.out.println("Escolha(ID do User) ou Sair(-1): ");
										String idUser = sc.nextLine();
										auxN = Integer.parseInt(idUser);
										if(auxN==-1)
											break;
										else if(Integer.parseInt(idUser)<allUsers.size())
										{
											Invite auxI = searchInvite(allUsers.get(Integer.parseInt(idUser)), thisMeeting.idMeeting);
											if(auxI == null)
											{
												User invited = allUsers.get(Integer.parseInt(idUser));
												Invite thisInvite = new Invite(Integer.parseInt(idUser), thisUser.idUser, thisUser.name, invited.name, thisMeeting);
												invited.convites.add(thisInvite);
												thisMeeting.convites.add(thisInvite);
												User trush;
												while(true){
													try{
														output.writeObject(invited);
														output.flush();
														trush = (User) input.readObject();

														output.writeObject(thisMeeting);
														output.flush();
														thisMeeting = (Meeting) input.readObject();
														
														break;
													} catch(IOException e){
														s.close();
														try{s = changeServer(s, ports, ips, serverUsed);}
														catch(Exception z){}
														if(serverUsed==0)
							  								serverUsed=1;
							  							else
							  								serverUsed=0;
														input = new ObjectInputStream(s.getInputStream());
														output = new ObjectOutputStream(s.getOutputStream());
													}
												}

												System.out.println("Convite Enviado a " + allUsers.get(Integer.parseInt(idUser)).name);
											}
											else
												System.out.println("Já Convidado!");
										}
										else
											System.out.println("Não existe!");
									}
									Meeting trush;
									while(true){
										try{
											output.writeObject(thisMeeting);
											output.flush();	
											trush = (Meeting) input.readObject();
											
											break;
										} catch(IOException e){
											s.close();
											try{s = changeServer(s, ports, ips, serverUsed);}
											catch(Exception z){}
											if(serverUsed==0)
				  								serverUsed=1;
				  							else
				  								serverUsed=0;
											input = new ObjectInputStream(s.getInputStream());
											output = new ObjectOutputStream(s.getOutputStream());
										}
									}
									int i;
									for(i=0; i<thisUser.meets.size();i++)
									{
										if(thisUser.meets.get(i).idMeeting == thisMeeting.idMeeting)
										{
											thisUser.meets.set(i,thisMeeting);
											break;
										}
									}
									while(true){
										try{
											output.writeObject(thisUser);
											output.flush();
											thisUser = (User) input.readObject();
											
											break;
										} catch(IOException e){
											s.close();
											try{s = changeServer(s, ports, ips, serverUsed);}
											catch(Exception z){}
											if(serverUsed==0)
				  								serverUsed=1;
				  							else
				  								serverUsed=0;
											input = new ObjectInputStream(s.getInputStream());
											output = new ObjectOutputStream(s.getOutputStream());
										}
									}
								}
							}
							else if(auxN==-1)
								break;
						}
				   	}
					else if (auxMenu == 5)
					{
						limparEcra();
						
						while(true)
						{
							System.out.println("------ MEETINGS A PARTICIPAR ------");
							System.out.println(thisUser.listMeetings());
							System.out.println("Escolha(ID do Meeting) ou Sair(-1)");

							received = sc.nextLine();
							auxN = Integer.parseInt(received);
							if(auxN!=-1)
							{
									
								Meeting thisMeeting = searchMeeting(thisUser,auxN);
								
								if(thisMeeting!=null)
								{
									System.out.println("MEETING " + thisMeeting.title);
									for(Chat f: thisMeeting.Chats)
										System.out.println(f.idChat + " - " + f.text);
									System.out.println("Escolha(ID do Chat), Adicionar Action(-1), Sair(-2)");
									received = sc.nextLine();
									auxN = Integer.parseInt(received);
									if(auxN!=-1 && auxN!=-2)
									{	
										Chat thisChat = searchChat(thisMeeting, Integer.parseInt(received));
										if(thisChat!=null)
										{
											aux ="";
											
											while(true)
											{
												while(true)
												{
													try{
														output.writeObject("openChat");
														output.flush();
														output.writeObject(thisChat);
														output.flush();
														thisChat = (Chat) input.readObject();
														
														break;
													} catch(IOException e){
														s.close();
														try{s = changeServer(s, ports, ips, serverUsed);}
														catch(Exception z){}
														if(serverUsed==0)
							  								serverUsed=1;
							  							else
							  								serverUsed=0;
														input = new ObjectInputStream(s.getInputStream());
														output = new ObjectOutputStream(s.getOutputStream());
													}
												}

												for(String t: thisChat.messages)
													System.out.println(t);
												System.out.println("Falar(0), Sair(1), Continuar(2), Adicionar KeyDecision(3)");
												auxN = sc.nextInt();
												sc.nextLine();
												if(auxN==0)
												{
													System.out.println("Digo: ");
													aux = sc.nextLine();
													aux = "["+thisUser.name+"] " + aux;
													thisChat.messages.add(aux);
													while(true){
														try{
															output.writeObject("saveChat");
															output.flush();
															output.writeObject(thisChat);
															output.flush();
															
															break;
														} catch(IOException e){
															s.close();
															try{s = changeServer(s, ports, ips, serverUsed);}
															catch(Exception z){}
															if(serverUsed==0)
								  								serverUsed=1;
								  							else
								  								serverUsed=0;
															input = new ObjectInputStream(s.getInputStream());
															output = new ObjectOutputStream(s.getOutputStream());
														}
													}
												}
												else if(auxN==1)
													break;
												else if(auxN==3)
												{
													System.out.println("Decidiu-se: ");
													aux = sc.nextLine();
													
													thisChat.keyDecision = new Decision(aux);
													
													output.writeObject("saveChat");
													output.flush();
													output.writeObject(thisChat);
													output.flush();
													break;
												}
											}
											thisMeeting.Chats.set(thisChat.idChat, thisChat);
											while(true){
												try{
													output.writeObject(thisMeeting);
													output.flush();
													thisMeeting = (Meeting) input.readObject();
													
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}

											int i;
											for(i=0;i<thisUser.meets.size();i++)
											{
												if(thisUser.meets.get(i).idMeeting == thisMeeting.idMeeting)
													thisUser.meets.set(i,thisMeeting);
											}
											while(true){
												try{
													output.writeObject(thisUser);
													output.flush();
													thisUser = (User) input.readObject();
													
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}
										}
										else
											System.out.println("Chat não existente");
									}
									else if(auxN==-1)
									{
										ArrayList <User> allUsers;
										System.out.println("Utilizadores: ");
										while(true){
											try{
												output.writeObject("listUsers");
												output.flush();
												allUsers = (ArrayList <User>) input.readObject();
												break;
											} catch(IOException e){
												s.close();
												try{s = changeServer(s, ports, ips, serverUsed);}
												catch(Exception z){}
												if(serverUsed==0)
					  								serverUsed=1;
					  							else
					  								serverUsed=0;
												input = new ObjectInputStream(s.getInputStream());
												output = new ObjectOutputStream(s.getOutputStream());
											}
										}
										for(User d: allUsers)
											System.out.println(d.idUser + " - " + d.name);
										System.out.println("Escolha(ID do User): ");
										auxN = sc.nextInt();
										sc.nextLine();
										if(auxN<allUsers.size())
										{
											User thisOne = allUsers.get(auxN);
											//System.out.println(thisOne.name);
											System.out.println("Action: ");
											aux = sc.nextLine();	
											Action a = 	new Action(aux, thisMeeting.title);
											a.idAction = thisOne.actions.size();									
											thisOne.actions.add(a);
											User trash;
											while(true){
												try{
													output.writeObject(thisOne);
													output.flush();
													trash = (User) input.readObject();
													break;
												} catch(IOException e){
													s.close();
													try{s = changeServer(s, ports, ips, serverUsed);}
													catch(Exception z){}
													if(serverUsed==0)
						  								serverUsed=1;
						  							else
						  								serverUsed=0;
													input = new ObjectInputStream(s.getInputStream());
													output = new ObjectOutputStream(s.getOutputStream());
												}
											}
											System.out.println("Acção Adicionada a " + thisOne.name);
										}	
									}
									else if(auxN==-2)
										break;
									else
										System.out.println("Erro!");
								}
								else
									System.out.println("Meeting não existente");
								
							}
							else if(auxN==-1)
								break;

						}
						
					}

					else if (auxMenu ==6) 
					{
						//LIMPAR ECRA
						limparEcra();
						
						for(Meeting m: thisUser.meets){
							System.out.println(m.idMeeting + " - " + m.title);
						}

						System.out.println("Escolha(ID do Meeting) ou Sair(-1)");
						aux = sc.nextLine();
						auxN = Integer.parseInt(aux);
						if(auxN!=-1)
						{
							
							Meeting thisMeeting = searchMeeting(thisUser, auxN);
							if(thisMeeting!=null)
							{
								for(Chat c: thisMeeting.Chats)
								{
									while(true){
										try{
											output.writeObject("openChat");
											output.flush();
											output.writeObject(c);
											output.flush();
											c = (Chat) input.readObject();
											break;
										} catch(IOException e){
											s.close();
											try{s = changeServer(s, ports, ips, serverUsed);}
											catch(Exception z){}
											if(serverUsed==0)
				  								serverUsed=1;
				  							else
				  								serverUsed=0;
											input = new ObjectInputStream(s.getInputStream());
											output = new ObjectOutputStream(s.getOutputStream());
										}
									}
									if(c.keyDecision == null)
										System.out.println(c.text + " - " + "Nada decidido");
									else
										System.out.println(c.text + " - " + c.keyDecision.text);
								}
								System.out.println("Prima qualquer tecla para continuar");
								aux = sc.nextLine();
							}
						}
						//LIMPAR ECRA
					}
					else if(auxMenu == 7)
					{
						Date now = new Date();
						String data = new SimpleDateFormat("yyyy-MM-dd").format(now);
						ArrayList <Meeting> upm = new ArrayList <Meeting>();

						while(true){
							try{
								output.writeObject("upcoming");
							   	output.flush();
								output.writeObject(data);
								output.flush();
								output.writeObject(thisUser);
								upm = (ArrayList <Meeting>) input.readObject();
								break;
							} catch(IOException e){
								s.close();
								try{s = changeServer(s, ports, ips, serverUsed);}
								catch(Exception z){}
								if(serverUsed==0)
	  								serverUsed=1;
	  							else
	  								serverUsed=0;
								input = new ObjectInputStream(s.getInputStream());
								output = new ObjectOutputStream(s.getOutputStream());
							}
						}

						if(upm.size()>0){
							System.out.println("Meetings no proximo mes");
							for (Meeting m : upm)
								System.out.println(m.idMeeting + " - " + m.title);
						} else {
							System.out.println("Não tem nenhum meeting no proximo mes");
						}
						System.out.println("Prima qualquer tecla para continuar");
						data = sc.nextLine();
					}
					else if(auxMenu==8)
					{
						limparEcra();
						while(true){
							try{
								output.writeObject("exit");
								output.flush();
								output.writeObject(thisUser);
								output.flush();
								break;
							} catch(IOException e){
								s.close();
								try{s = changeServer(s, ports, ips, serverUsed);}
								catch(Exception z){}
								if(serverUsed==0)
	  								serverUsed=1;
	  							else
	  								serverUsed=0;
								input = new ObjectInputStream(s.getInputStream());
								output = new ObjectOutputStream(s.getOutputStream());
							}
						}
						break;
					}
				}
			}
		}catch (EOFException e) {
		    System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
		    System.out.println("IO:" + e.getMessage());
		}catch(ClassNotFoundException e){
            System.out.println("CNE:" + e);
        } finally {
		    if (s != null)
				try {
				    s.close();
				} catch (IOException e) {
				    System.out.println("close:" + e.getMessage());
				}
		}
    }
}
