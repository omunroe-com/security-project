/**
 * @author ${Hardik Vasa}
 *
 * ${x509}
 */
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
        //Defining port informtion
		String host = "localhost";
		int port = 7999;
		Socket socket = new Socket(host, port);

        //This will read the certificate file
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        //Passing the certificate to a local variable
        InputStream inputStream = new FileInputStream("vasa.cer");
        //Instantiating the object by using the 'X.509' transformation function
        CertificateFactory certi = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)certi.generateCertificate(inputStream);
        inputStream.close();

        Date date = certificate.getNotAfter();
        if(date.after(new Date()))
        {
            //This will print the contents of the certificate
            System.out.println("The detail of the certificate file are: " + certificate.toString());
            try {
                //This will check server's validity signature
                certificate.checkValidity();
                System.out.println("The certificate is valid from "+ certificate.getNotBefore()+ " to "+certificate.getNotAfter()) ;}
            catch (Exception e){
                System.out.println(e);}
        }
        else {
        	System.out.println("The certificate is now expired.");
        }

        System.out.println("Please enter any text:");
        Scanner inputNew = new Scanner(System.in);
        String message = inputNew.nextLine();
        
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