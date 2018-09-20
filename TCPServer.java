package a;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TCPServer {
	Set<InetAddress> firewall;	//makes a set called whitelist that stores ip addresses
	private PrintStream log;
	
	public TCPServer(int port, PrintStream log, Set<InetAddress>firewall, File running) throws Exception {
	
		this.log = log;
		this.firewall = firewall;
		ServerSocket server = new ServerSocket(port);
		System.out.println("Listening on: " + server.getLocalPort());
		firewall = new HashSet<InetAddress>();	//assigns whitelist as a set of type hashset
		firewall.add(server.getInetAddress());	//adds the IP address of the server itself to allow for local testing
		
		//this.insertLogEntry("Server Start", );
		//this.punch(server.getInetAddress());
		while(running.exists())	//runs only when the file running exists
		{
			Socket client = server.accept();
			log.println((new Date()).toString() + "|" + "Connection" + "|" + client.getInetAddress());
			log.println((new Date()).toString() + "|" + "Disconnected" + "|" + client.getInetAddress());
			//Worker worker = new Worker();
			//worker.handle(client);
		}
		//this.insertLogEntry("Server Shutdown", );
		server.close();
	}
	public String myTime()
	{
		ZonedDateTime nowTime = ZonedDateTime.now();
		DateTimeFormatter formattedTime = DateTimeFormatter.ofPattern("E d MMM yyyy HH:mm:ss z");
		String finalReturn = nowTime.format(formattedTime);
		return finalReturn;
	}
	public void firewallAdd( InetAddress inetAd)
	{
		firewall.add(inetAd);  // Adds the ip address to the firewall list
	}
	
	public void firewallRemove( InetAddress inetAd)
	{
		firewall.remove(inetAd);  //Removes the ip address from the firewall list
	}

	public void insertLog(String mainEntry, String subEntry)
	{
		log.println(mainEntry + " | " + subEntry + " | " + myTime() );  // inputs entry into the log
	}
	
	public boolean permittedIp(Socket client)
	{
		InetAddress myClientIp = client.getInetAddress();
		boolean permitted = firewall.contains(myClientIp);
		if(permitted)
		{
			return permitted;
		}
		else
		{
			return false;
		}
	}
	private void serverClose(ServerSocket server)
	{
		try {
			server.close();
			insertLog("Server shutdown", null);
		}
		catch(IOException e)
		{
			insertLog(e.getMessage(), e.getStackTrace().toString());
			System.out.println("Error" + e.getMessage());
			System.exit(1);
		}
	}
	
	private ServerSocket serverCreate(int port, ServerSocket server)
	{
		try {
			server = new ServerSocket(port);
		}
		catch(IOException e)
		{
			insertLog(e.getMessage(), e.getStackTrace().toString());
			System.out.println("Error" + e.getMessage());
			System.exit(1);
		}
		return server;
	}
	
	private Socket sampleClient(ServerSocket server)
	{
		Socket client = null;
		try {
			client = server.accept();
		}
		catch(IOException e)
		{
			insertLog(e.getMessage(), e.getStackTrace().toString());
		}
		
		return client;
	}
	
	
	private void firewallViolation(Socket client)
	{
		insertLog("Firewall Violation", client.getInetAddress().toString());
		try {
			PrintStream clientOutput = new PrintStream(client.getOutputStream());
			clientOutput.println("Authentication failed, exiting...");
			client.close();
		}
		catch(IOException e)
		{
			insertLog(e.getMessage(), e.getStackTrace().toString());
		}
	}
	
	private String ipPortMethod(InetAddress ip, int port)
	{
		String ipPort = ip.toString() +":"+ Integer.toString(port); 
		return ipPort;
	}
	public static void main(String[] args) {
		
	}
}
