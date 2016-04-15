/**
 * @author ${Hardik Vasa}
 *
 * ${Signature}
 */
package signature;

import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class ELGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
        //computing ((y^a mod p)(a^b mod p)) mod p
		BigInteger one = (y.modPow(a, p).multiply(a.modPow(b, p))).mod(p);
		//Converting string => BigInteger
		BigInteger two = new BigInteger(message.getBytes());
		//computing g^m mod p
		BigInteger result2 = g.modPow(two,p);
		return one.equals(result2);
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean status = verifySignature(y, g, p, a, b, message);

		//System.out.println(message);

		if (status == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}
