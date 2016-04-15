package publicKeySystem;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.util.Scanner;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;


public class PublicKeyRSAAlice {
    
	
	public static void main(String[] args) throws Exception {
        
		System.out.println("This is the RSA Public Key System");
		System.out.println("Which kind of service would you like ?");
		System.out.println("1. Confidentiality            : ");
	    System.out.println("2. Integrity & Authentication : ");
        System.out.println("3. Combination of both        : ");
	    
		Scanner input = new Scanner(System.in);
		
	    String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		
		ObjectOutputStream BPublicKey = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream APublicKey = new ObjectInputStream(s.getInputStream());
		
		/* This will generate Alice's keys (private key and public key)*/
		KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");
		genKeyPair.initialize(1024 ,new SecureRandom());
	    KeyPair keyPair = genKeyPair.genKeyPair();
	    RSAPublicKey eAlice = (RSAPublicKey) keyPair.getPublic();
	    RSAPrivateKey dAlice = (RSAPrivateKey) keyPair.getPrivate();
	    
	    /* This will send her public key to Bob */
	    BPublicKey.writeObject(eAlice);
	    
	    /* This will be used to get Bob's public key */
	    RSAPublicKey eBob = (RSAPublicKey) APublicKey.readObject();
	    Cipher cipher = Cipher.getInstance("RSA");
	    
	    int choice= input.nextInt();
        
        switch(choice)
        {
            case 1:
                //Confidentiality: Encipher by receiver's public key(Bob)
                
                System.out.println("Confidentiality: encipher by receiver's public key (Bob) ");
                Scanner input1 = new Scanner(System.in);
                System.out.println("Please enter the plaintext you want to encipner");
                String in1= input1.nextLine();
                cipher.init(Cipher.ENCRYPT_MODE, eBob);
                byte[] cipherText1 = cipher.doFinal(in1.getBytes());
                System.out.println("The ciphertext is: " + cipherText1);
                BPublicKey.writeInt(1);
                BPublicKey.writeObject(cipherText1);
                BPublicKey.flush();
                BPublicKey.close();
                break;
                
            case 2:
                //Integrity/Authentication: Encipher by sender's private key (Alice)
                
                System.out.println("Integrity/Authentication: encipher by sender's private key (Alice)");
                Scanner input2 = new Scanner(System.in);
                System.out.println("Please enter the plaintext you want to encipner");
                String in2= input2.nextLine();
                cipher.init(Cipher.ENCRYPT_MODE, dAlice);
                byte[] cipherText2 = cipher.doFinal(in2.getBytes());
                System.out.println("The ciphertext is: " + cipherText2);
                BPublicKey.writeInt(2);
                BPublicKey.writeObject(cipherText2);
                BPublicKey.flush();
                BPublicKey.close();
                break;
                
            case 3:
                //Combination of both Confidentiality and Intergrity/Authentication: Encipher by sender's private key first and then re-encipher by receiver's public key.
                System.out.println("Combination of both confidentiality and Integrity/Authentication: Encipher by sender's private key first (Alice) and then re-enciphered by receiver's public key (Bob)");
                Scanner input3 = new Scanner(System.in);
                System.out.println("Please enter the plaintext you want to encipner");
                String in3 = input3.nextLine();
               /* cipher.init(Cipher.ENCRYPT_MODE, dAlice);
                byte[] cipherText3 = cipher.doFinal(in3.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, eBob);
                String fnl = new String(cipherText3);
                byte[] cipherText4 = cipher.doFinal(fnl.getBytes());*/
                BigInteger BI = new BigInteger(in3.getBytes());
                BigInteger cipherText3 = (BI.modPow(dAlice.getPrivateExponent(), dAlice.getModulus()));
                BigInteger cipherText4 = (cipherText3.modPow(eBob.getPublicExponent(), eBob.getModulus()));
                System.out.println("The ciphertext is: ");
                System.out.println(new String(cipherText4.toByteArray()));
                BPublicKey.writeInt(3);
                BPublicKey.writeObject(cipherText4);
                BPublicKey.flush();
                BPublicKey.close();
                break;
                
        }
        s.close();
        input.close();
    }
    
    
    
}