package publicKeySystem;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class PublicKeyRSABob
{
	public static void main(String[] args) throws Exception
    {
	    int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		

		ObjectInputStream APublicKey = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream PublicKey = new ObjectOutputStream(client.getOutputStream());
		
		/* This will generate Bob's keys (private key and public key)*/
		KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");
	    genKeyPair.initialize(1024, new SecureRandom()); 
	    KeyPair keyPair = genKeyPair.genKeyPair();
	    RSAPublicKey eBob = (RSAPublicKey) keyPair.getPublic();
	    RSAPrivateKey dBob = (RSAPrivateKey) keyPair.getPrivate();
	    
	    /* This will send Bob's public key to Alice */
	    PublicKey.writeObject(eBob);
	    
	    /* Get Alice's public key */
	    RSAPublicKey eAlice = (RSAPublicKey) APublicKey.readObject();
	    Cipher cipher = Cipher.getInstance("RSA");
	    
	    int choice = APublicKey.readInt();
	    
		    switch(choice)
			{
			  case 1:
				  /*Achieve Confidentiality by using Bob's private key to decipher */ 
				  System.out.println("Confidentiality: encipher by receiver's public  key(Bob)");
				  System.out.println("                 decipher by receiver's private key(Bob)");
				  byte[] in1 = (byte[]) APublicKey.readObject();
				  cipher.init(Cipher.DECRYPT_MODE, dBob);
   			      byte[] plaintText1 = cipher.doFinal(in1);
				  System.out.println("The plaintext is: " + new String(plaintText1));
				  break;
				    
				  
			  case 2:
				  /*Achieve Integrity/Authentication by using Alice's public key to decipher */ 
				  System.out.println("Integrity/Authentication: encipher by sender's private key (Alice)");
				  System.out.println("                          decipher by sender's public key (Alice)");
				  byte[] in2 = (byte[]) APublicKey.readObject();
				  cipher.init(Cipher.DECRYPT_MODE, eAlice);
   			      byte[] plaintText2 = cipher.doFinal(in2);
				  System.out.println("The plaintext is: " + new String(plaintText2));
				  break;
              case 3:
                  /*Achieve both confidentiality and Integrity/Authentication by first decrypting using Bob's private key and then decrypting using Alice's public key. */
                  System.out.println("Combination of both confidentiality and Integrity/Authentication: Enciphered by sender's public key (Alice) and then re-enciphered by receiver's public key (Bob)");
                  System.out.println(" Decipher first by receiver's private key (Bob) and then re-deciphering by sender's public key (Alice)");
                  BigInteger in3 = (BigInteger) APublicKey.readObject();
                  BigInteger plaintText3 = (in3.modPow(dBob.getPrivateExponent(), dBob.getModulus()));
                  BigInteger plaintText4 = (plaintText3.modPow(eAlice.getPublicExponent(), eAlice.getModulus()));
                  /*cipher.init(Cipher.DECRYPT_MODE, dBob);
                  byte[] plaintText3 = cipher.doFinal(in3);
                  cipher.init(Cipher.DECRYPT_MODE, eAlice);
                  byte[] plaintText4 = cipher.doFinal(plaintText3);*/
                  System.out.println("The plaintext is: ");
                  System.out.println(new String(plaintText4.toByteArray()));
                  break;

			
			}
		 
		}
		
	}
 


