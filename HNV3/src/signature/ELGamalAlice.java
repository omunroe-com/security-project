/**
 * @author ${Hardik Vasa}
 *
 * ${Signature}
 */
package signature;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ELGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		//computey --> y = g^d mod p
		BigInteger bigy = g.modPow(d, p);
		return bigy;
	}
	
	private static BigInteger computeK(BigInteger p)
	{
        //computek
        //generating a cryptographiclly secure random number
		SecureRandom rnd = new SecureRandom();
		int numBits = 1024;
        //Generating a BigInteger with length 1024
		BigInteger bigk = new BigInteger(numBits, rnd);
        //generating p-1 computation
		BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        //Checking if BigInteger k is relatively prime to BigInteger (p-1) - step as per instruction document
		while(!bigk.gcd(pMinusOne).equals(BigInteger.ONE))	{ bigk = new BigInteger(numBits, rnd);	}
		return bigk;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
        //computeA --> a = g^k mod p
		BigInteger biga = g.modPow(k, p);
		return biga;
	}

	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		//computeB
        //assigning the value of message to the variable m
		BigInteger me = new BigInteger(message.getBytes());
        //computing pOne as '1'
		BigInteger pOne = BigInteger.valueOf(1);
        //Subtracting one from pOne
		BigInteger pMinusOne = p.subtract(pOne);
		BigInteger three = me.subtract(d.multiply(a)).multiply(k.modInverse(pMinusOne)).mod(pMinusOne);
		return three;
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// Securing the public and private key
		BigInteger y, g, p; // public key
		BigInteger d; // private key

        //defining the length of the key
		int numBits = 1024;
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with numBits bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime.
		p = new BigInteger(numBits, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length numBits-1
		g = new BigInteger(numBits-1, mSecureRandom);
		d = new BigInteger(numBits-1, mSecureRandom);
		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}
