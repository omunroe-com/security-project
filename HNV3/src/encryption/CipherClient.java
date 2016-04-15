/**
 * @author ${Hardik Vasa}
 *
 * ${encryption}
 */
package encryption;

import sun.security.tools.keytool.Main;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
public class CipherClient
{
        public static void main(String[] args) throws Exception 
        {
            //block generating the key
            Key key_chain;

            //In order to create a Cipher object, the application calls the Cipher's getInstance method,
            // and passes the name of the requested transformation to it.
            KeyGenerator my_gen = KeyGenerator.getInstance("DES");

            //Generate a cryptographically strong random number
            my_gen.init(new SecureRandom());
            //below statement generates generation
            key_chain = my_gen.generateKey();

            // This will create a new ObjectOutputStream and specify the text file
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/encryption/OutKeyDES.txt"));
            //Now we specify the text file to write the key
            //writes the specified object to the ObjectOutputStream
            out.writeObject(key_chain);

            //message and its associated details are mentioned
            String message = "A Quick Brown Fox Jumps Over The Lazy Dog";
            String host = "localhost";
            int port = 7999;
            Socket socket = new Socket(host, port);

            // This will encrypt the message above using the key and send it over socket 'socket' to the server.
            Cipher cipher = Cipher.getInstance("DES");  //creating the instance of the DES
            cipher.init(Cipher.ENCRYPT_MODE, key_chain);      //defines the mode. This encrypts the data
            CipherOutputStream cipher_output = new CipherOutputStream(socket.getOutputStream(), cipher);
            cipher_output.write(message.getBytes()); //sending the ciphered text over the socket

            //close the connection
            cipher_output.close();
            out.close();

        }
}