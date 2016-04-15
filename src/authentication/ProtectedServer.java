package authentication;

import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException
	{
		DataInputStream in = new DataInputStream(inStream);  //get data from the received buffer
		
		String user = in.readUTF(); //read all details sent by the client
		String password = lookupPassword(user); //gets the password stored in the system

		//reading the individual data values sent by the client
		long time_stamp_1 = in.readLong();  //Takes in the value of the
		double random_number_1 = in.readDouble();

		long time_stamp_2 = in.readLong();  //takes in (read) the
		double random_number_2 = in.readDouble();

		int length = in.readInt();  //reading the length of buffer array
		byte[] received_value = new byte [length];
		
        //reads bytes from an input stream and allocates those into the buffer array
        in.readFully(received_value);
		
        //create the hash of the stored password
		byte[] digest_value_1 = Protection.makeDigest(user, password, time_stamp_1, random_number_1);
		byte[] digest_value_2 = Protection.makeDigest(digest_value_1, time_stamp_2, random_number_2);

        //The same with client; generate at the server to make authentication
		boolean flag = true;
		flag = MessageDigest.isEqual(received_value, digest_value_2);

        //if the results in the client and server are the same
		return flag;
	}
	
	protected String lookupPassword(String user)
	{
		return "abc123";
	}
	
	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		
		ProtectedServer server = new ProtectedServer();
		
		if (server.authenticate(client.getInputStream()))
			System.out.println("Client logged in.");
		else
			System.out.println("Client failed to log in.");
		
		s.close();
	}
}
