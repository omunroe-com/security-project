package x509;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class X509server
{
	public static void main(String[] args) throws Exception 
	{   
		
		String aliasname="vasa";
        char[] password="standrose@123".toCharArray();
		
        int port = 7999;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());
		
        //Read the keystore and retrieve the server's private key
	    KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("vasa.jks"), password);
        PrivateKey dServer = (PrivateKey)ks.getKey(aliasname, password);
       
        //Decrypt: server's private key 
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] in = (byte[]) is.readObject();
		cipher.init(Cipher.DECRYPT_MODE, dServer);
		byte[] plaintText = cipher.doFinal(in);
		System.out.println("Enter any text: " + new String(plaintText));
		server.close();
	}

}
