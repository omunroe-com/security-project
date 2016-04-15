package encryption;
/**
 * @author ${Hardik Vasa}
 *
 * ${encryption}
 */
import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherServer
{
        public static void main(String[] args) throws Exception 
        {
            //listening to the client on the same port (in this case, 7999)
            int port = 7999;
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();

            //reading the key from the file that was encrypted by the client
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("src/encryption/OutKeyDES.txt"));
            //storing the key in the key_chain variable
            Key key_chain = (Key)in.readObject();

            // This will decrypt the message above using the key and send it over socket s to the server.
            Cipher cipher = Cipher.getInstance("DES");
            //We use the decrypt mode
            cipher.init(Cipher.DECRYPT_MODE, key_chain);
            CipherInputStream cipher_input = new CipherInputStream(socket.getInputStream(), cipher);

            //Output.txt will be used to save the output string that we get after decrypting the message
            FileOutputStream input_1 = new FileOutputStream("src/encryption/OutputKey.txt");

            byte[] buffer = new byte[1024];

            int length;
            while( (length = cipher_input.read(buffer)) != -1 ) {
                // byte data is converted into the normal string
                input_1.write(buffer, 0, length);
            }

        }
}

