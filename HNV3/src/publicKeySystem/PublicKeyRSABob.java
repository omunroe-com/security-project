/**
 * @author ${Hardik Vasa}
 *
 * ${PublicKeyRSA}
 */
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

        //creating object instance for input stream over the socket
		ObjectInputStream AlicePublicKey = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream PublicKey = new ObjectOutputStream(client.getOutputStream());
		
		// This will generate Bob's keys (private key and public key)*/
		KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");
        //Generating cryptographically secure random number
	    genKeyPair.initialize(1024, new SecureRandom());
        //the below statement actually generates the keys
	    KeyPair keyPair = genKeyPair.genKeyPair();
	    RSAPublicKey publicBob = (RSAPublicKey) keyPair.getPublic();  //setting up the public key
	    RSAPrivateKey privateBob = (RSAPrivateKey) keyPair.getPrivate();  //setting up the private key
	    
	    // This will send over Bob's public key to Alice
	    PublicKey.writeObject(publicBob);
	    
	    // This statement will get the Alice's public key */
	    RSAPublicKey AlicePublic = (RSAPublicKey) AlicePublicKey.readObject();
        //Instantiating the cipher class with the RSA transformation function
	    Cipher cipher = Cipher.getInstance("RSA");
	    
	    int choice = AlicePublicKey.readInt();
	    
        if(choice==1) {
            //Achieve Confidentiality by using Bob's private key
            System.out.println("Confidentiality: encipher by receiver's public  key(Bob)");
            System.out.println("decipher by receiver's private key(Bob)");
            byte[] inputchoice1 = (byte[]) AlicePublicKey.readObject();
            //using the decrypt mode for de-ciphering Bob's key
            cipher.init(Cipher.DECRYPT_MODE, privateBob);
            byte[] plaintText = cipher.doFinal(inputchoice1);
            System.out.println("The plaintext is: " + new String(plaintText));
        }
        else if(choice==2) {
            //Achieve Integrity/Authentication by using Alice's public key to decipher */
            System.out.println("Integrity/Authentication: encipher by sender's private key (Alice)");
            System.out.println("decipher by sender's public key (Alice)");
            byte[] inputchoice2 = (byte[]) AlicePublicKey.readObject();
            //using the decrypt mode for de-ciphering
            cipher.init(Cipher.DECRYPT_MODE, AlicePublic);
            byte[] plaintText = cipher.doFinal(inputchoice2);
            System.out.println("The plaintext is: " + new String(plaintText));
        }
        else if (choice==3){
            //First decrypting using Bob's private key and then decrypting using Alice's public key.
            System.out.println(" Decipher first by receiver's private key (Bob) and then re-deciphering by sender's public key (Alice)");
            BigInteger inputchice3 = (BigInteger) AlicePublicKey.readObject();
            //formula computation for mod function
            BigInteger plaintText = (inputchice3.modPow(privateBob.getPrivateExponent(), privateBob.getModulus()));
            BigInteger plaintTextNew = (plaintText.modPow(AlicePublic.getPublicExponent(), AlicePublic.getModulus()));
            System.out.println("The plaintext is: ");
            //converting the big integer it a byte array after converting it into its 2s complement
            System.out.println(new String(plaintTextNew.toByteArray()));
        }
        else{
            System.out.println("Invalid Input"); //generating soft error for the user
        }

		 
    }
		
}
 


