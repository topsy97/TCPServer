package a;

import java.io.File;
import projA.util;
import projA.util
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Worker {

	private Socket myclient;
	private TCPServer myserver;
	private String ip;
	private Map<String, String> authorization;
	
	public Worker(TCPServer myserver, Socket myclient ) {
		this.myserver= myserver;
		this.myclient= myclient;
		authorization = new HashMap<>();
		
		authorization.put("init", "init");
		authorization.put("Tee", "Teee");
		
		
	
	}

	public String getTime()
	{
		return myserver.myTime();
	}
	
	private void bye() throws IOException 
	{
		
			myclient.close();  //closes socket
			
			myserver.insertLog("Disconnected", ip); // shows on log that there has been a disconnection
	}
	public void punch(InetAddress inetAd)
	{
		myserver.firewallAdd(inetAd);
	}
	
	
	
	public void plug(InetAddress inetAd)
	{
		myserver.firewallRemove(inetAd);
	}
	
	public long prime(int digitCount)
	{
		final float DEC_BIN_RATIO = 3.5f;
		int totalDigitCount = (int) (DEC_BIN_RATIO * digitCount);
		BigInteger randomPrime = BigInteger.probablePrime(totalDigitCount, new Random());
		long methodReturnedPrime = randomPrime.longValue();
		
		return methodReturnedPrime;
	}
	
	public void handle() throws IOException
	{
		ip = myclient.getInetAddress().toString(); // store the socket ip as a string
		myserver.insertLog("Connected to Client", ip);
		System.out.println("Connected to client");
		clientExcept();
		System.out.println("Disconnected from Client");
	}
	
	private void clientExcept () throws IOException
	{
		PrintStream clientOutput = new PrintStream(myclient.getOutputStream());
		Scanner clientScreenInput = new Scanner(myclient.getInputStream());
		
		clientOutput.println("Enter your command. Type 'bye' to exit");
		String clientInput = clientScreenInput.nextLine();
		
		while(parser(clientInput, clientOutput))
		{
			clientOutput.println("Enter another Command");
			clientInput = clientScreenInput.nextLine();
		}
	}
	
	private boolean parser(String clientInput, PrintStream clientOutput)
	{
		boolean plausibleClient = true;
		
		Pattern byePattern = Pattern.compile("(\\s*)(bye)(\\s*)", Pattern.CASE_INSENSITIVE);
		Matcher byeMatcher = byePattern.matcher(clientInput);
		
		Pattern timePattern = Pattern.compile("(\\s*)(get)(\\s*)(time)(\\s*)", Pattern.CASE_INSENSITIVE);
		Matcher timeMatcher = timePattern.matcher(clientInput);
		
		Pattern authPattern = Pattern.compile("(\\s*)(auth)(\\s*)(\\S+)(\\s*)(\\S+)(\\s)", Pattern.CASE_INSENSITIVE);
		Matcher authMatcher = authPattern.matcher(clientInput);
		
		Pattern primePattern = Pattern.compile("(\\s*)(prime)(\\s*)(\\d+)(\\s*)", Pattern.CASE_INSENSITIVE);
		Matcher primeMatcher = primePattern.matcher(clientInput);
		
		Pattern rosterPattern = Pattern.compile("(\\s*)(roster)(\\s*)(\\S+)(\\s*)", Pattern.CASE_INSENSITIVE);
		Matcher rosterMatcher = rosterPattern.matcher(clientInput);
		
		
		
		
		
		if(byeMatcher.matches())
		{
			plausibleClient = false;
		}
		
		else if(timeMatcher.matches())
		{
			String returnTime = myserver.myTime();
			System.out.println(returnTime + "\n");			
		}
		
		else if(authMatcher.matches())
		{
			String userNameInput = authMatcher.group(4);
			String passwordInput = authMatcher.group(6);
			
			clientOutput.println(authMethod(userNameInput, passwordInput));
		}
		
		else if(primeMatcher.matches())
		{
			int clientDigits = Integer.parseInt(primeMatcher.group(4));
			long returnedPrime = prime(clientDigits);
		}
		
		return plausibleClient;
		
	}
	
	private String authMethod(String username, String password)
	{
		if(authorization.containsKey(username))
		{
			String correctPassword = authorization.get(username);
			if (correctPassword.equals(password))
			{
				return "Welcome!";
			}
			
		}
		return "Incorrect Username/Password";
			
	}
	
	private String roster() 
	{
		Course course = Util.get
	}
	
	
	public void run()
	{
		try
		{
				handle();
		}
		catch(IOException e)
		{
			myserver.insertLog(e.getMessage(), e.getStackTrace().toString());
			System.out.println(e.getMessage() + "exiting");
			System.exit(1);
		}
	}
}
