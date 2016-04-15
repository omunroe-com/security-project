package signature;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ELGamalAlice
{
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		BigInteger y = g.modPow(d, p);  // y = g^d mod p
		return y;
	}
	
	private static BigInteger computeK(BigInteger p)
	{
		SecureRandom rnd = new SecureRandom();
		int numBits = 1024;
		BigInteger k = new BigInteger(numBits, rnd); //This will generate a BigInteger with numBits = length, & rnd = source of random number
		BigInteger pMinusOne = p.subtract(BigInteger.ONE); //generate p-1
    
        //To check if BigInteger k is relatively prime to BigInteger (p-1)
		while(!k.gcd(pMinusOne).equals(BigInteger.ONE))
		{
			k = new BigInteger(numBits, rnd);
		}
		
		return k;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		BigInteger a = g.modPow(k, p); // a = g^k mod p
		
		return a;
	}

	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger m = new BigInteger(message.getBytes());
		BigInteger pOne = BigInteger.valueOf(1);
		BigInteger pMinusOne = p.subtract(pOne);

		return m.subtract(d.multiply(a)).multiply(k.modInverse(pMinusOne)).mod(pMinusOne);
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int numBits = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with numBits bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
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
