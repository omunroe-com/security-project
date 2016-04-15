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
import java.util.Scanner;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class PublicKeyRSAAlice {

	public static void main(String[] args) throws Exception {
        
		System.out.println("This is the RSA Public Key System. Select an Option");

        //giving optios to the user
        System.out.println("1. Confidentiality: ");
	    System.out.println("2. Integrity & Authentication: ");
        System.out.println("3. Combination of both: ");
	    
		Scanner input = new Scanner(System.in);
		
	    String host = "localhost";
		int port = 7999;
		Socket socket = new Socket(host, port);

        //creating object instance for output stream over the socket
		ObjectOutputStream inPublicKey = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream outPublicKey = new ObjectInputStream(socket.getInputStream());
		
		//Generate Alice's key by applying the RSA transformation function
		KeyPairGenerator generateKeyPair = KeyPairGenerator.getInstance("RSA");
        //Generating cryptographically secure random number
        generateKeyPair.initialize(1024 ,new SecureRandom());
        //the below statement actually generates the keys
	    KeyPair keyPair = generateKeyPair.genKeyPair();
        //get the public key
	    RSAPublicKey publicAlice = (RSAPublicKey) keyPair.getPublic();
        //get the private key
	    RSAPrivateKey privateAlice = (RSAPrivateKey) keyPair.getPrivate();
	    // This will send her public key to Bob (server)
	    inPublicKey.writeObject(publicAlice);
	    
	    /* This will be used to get Bob's public key */
	    RSAPublicKey keyBob = (RSAPublicKey) outPublicKey.readObject();
	    Cipher cipher = Cipher.getInstance("RSA");

        //taking choice input from the user
	    int choice= input.nextInt();
        
        if (choice==1) {
            //Confidentiality: Encipher by receiver's public key(Bob)
            Scanner input1 = new Scanner(System.in);
            System.out.println("Please enter the plaintext");
            String inputText = input1.nextLine();
            //using the Encrypt mode for ciphering
            cipher.init(Cipher.ENCRYPT_MODE, keyBob);
            byte[] cipherTextchoice1 = cipher.doFinal(inputText.getBytes());
            //returns the cipher text
            System.out.println("The cipher text is: " + cipherTextchoice1);
            inPublicKey.writeInt(1);
            inPublicKey.writeObject(cipherTextchoice1);
        }
        else if (choice==2) {
            //Integrity/Authentication: Encipher by sender's private key (Alice)
            Scanner input1 = new Scanner(System.in);
            System.out.println("Please enter the plaintext");
            String inputText = input1.nextLine();
            cipher.init(Cipher.ENCRYPT_MODE, privateAlice);
            byte[] cipherTextchoice1 = cipher.doFinal(inputText.getBytes());
            System.out.println("The cipher text is: " + cipherTextchoice1);
            inPublicKey.writeInt(2);
            inPublicKey.writeObject(cipherTextchoice1);
        }
                
        else if (choice==3){
            //Encipher by sender's private key first and then re-encipher by receiver's public key.
            //taking input from the user
            Scanner input1 = new Scanner(System.in);
            System.out.println("Please enter the plaintext");
            String inputText = input1.nextLine();
            //assigning the value of input text to the type biginteger
            BigInteger binteger = new BigInteger(inputText.getBytes());
            //formula computation for mod function
            BigInteger ct = (binteger.modPow(privateAlice.getPrivateExponent(), privateAlice.getModulus()));
            BigInteger cipherTextchoice1 = (ct.modPow(keyBob.getPublicExponent(), keyBob.getModulus()));
            System.out.println("The cipher text is: ");
            //converting the big integer it a byte array after converting it into its 2s complement
            System.out.println(new String(cipherTextchoice1.toByteArray()));
            inPublicKey.writeInt(3);
            inPublicKey.writeObject(cipherTextchoice1);
        }

        else{
            System.out.println("Invalid Input. Please input either '1','2' or '3'");
        }

        inPublicKey.flush();
        inPublicKey.close();
        socket.close();
        input.close();

    }
}
