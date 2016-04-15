package authentication;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient {
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException
	{
        //put data into output buffer that is to be sent to the server
		DataOutputStream out = new DataOutputStream(outStream);  //create a new output buffer object

        Date date = new Date(); //Instantiate a Date object
		long time_stamp_1 = date.getTime(); //generates first time stamp from current value of time and date
        double random_number_1 = Math.random(); //generates random number
        //the makeDigest method creates message digest from its input data
        byte[] digest_value_1 = Protection.makeDigest(user, password, time_stamp_1, random_number_1);
        
		long time_stamp_2 = date.getTime(); //second time stamp
		double random_number_2 = Math.random();  //generating a random number
        //calls protection class to make hash value. Used second time.
		byte[] digest_value_2 = Protection.makeDigest(digest_value_1, time_stamp_2, random_number_2);

		//Writing all the required data and sending it to the ProtectedServer over the socket
		out.writeUTF(user); //Writing the user name
		out.writeLong(time_stamp_1); //writing the timestamp 1 value
		out.writeDouble(random_number_1);  //writing the random number 1 value

        out.writeLong(time_stamp_2); //writing the timestamp 2 value
		out.writeDouble(random_number_2);  //writing the random number 2 value
		out.writeInt(digest_value_1.length);  //writing the length of the 1st digest value
		out.write(digest_value_2);  //writing the value of the 2nd digest value

        out.flush();
	}
	
	public static void main(String[] args) throws Exception
	{
		String host = "localhost";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);
		
		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());
		
		s.close();
	}

}
