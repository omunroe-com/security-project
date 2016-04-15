package x509;

import java.io.*;
import java.net.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;
import javax.crypto.*;

public class X509client
{
	public static void main(String[] args) throws Exception 
	{
		String host = "localhost";
		int port = 7999;
		Socket socket = new Socket(host, port);

        //This will read the certificate file
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        InputStream inStream = new FileInputStream("vasa.cer");
        CertificateFactory certi = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)certi.generateCertificate(inStream);
        inStream.close();

        Date date = certificate.getNotAfter();
        if(date.after(new Date()))
        {
            //This will print the contents of the certificate
            System.out.println("The detail of the certificate file are: " + certificate.toString());
        	System.out.println("The certificate is recently created."); //Expiration date
            try {
                certificate.checkValidity(); //This will check server's signature
                System.out.println("The certificate is valid from "+ certificate.getNotBefore()+ " to "+certificate.getNotAfter()) ;}
            catch (Exception e){
                System.out.println(e);}
        }
        else {
        	System.out.println("The certificate is now expired.");
        }

        System.out.println("Please enter any text:");
        Scanner input = new Scanner(System.in);
        String message = input.nextLine();
        
        //Here, it will retrieve the public key from the certificate
        RSAPublicKey eServer = (RSAPublicKey) certificate.getPublicKey();
        
        //Encrypt server's public key
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, eServer);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        System.out.println("The ciphertext is: " + cipherText);
        output.writeObject(cipherText);
		output.flush();
		output.close();
	}

}