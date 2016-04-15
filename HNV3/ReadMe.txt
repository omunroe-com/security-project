/**
 * @author ${Hardik Vasa}
 *
 * ${ISP Project}
 * ${Email: hnv3@pitt.edu}
 */

This project was developed using the IntelliJ IDEA 15.0.1 software (IDE). I have commented most of the lines to explain my code and the flow of the code.

Authentication:
Summary - It authenticates the client into the server using the credentials of the client.
out.write() write a string in the statement . This method cannot be inherited from the Writer class because it must suppress I/O exceptions
First, the ProtectedServer.java file is to be run. Then the ProtectedClient.java is to be run. Then in the console of ProtectedServer, the logging information of the user appears.

Encryption:
Summary - The input string is encrypted using a key and then decrypted to get the original message back using the same key sent over to the server using socket port
First, the CipherServer.java file is to be run. Then the CipherClient.java is to be run. Then the 2 text files are generated in the package. OutKeyDES.txt contains the cipher text and OutputKey.txt contains the original text received after de-ciphering.

MessageDigest:
Summary - Creates MD5 and SHA keys for the plaintext entered by the user
Run the MessageDigest.java file. It will ask you for the option whether you like to use the 'md5' or the 'sha' functionality. After typing the option, it will then ask you to enter the plain text. Once the plain text is entered, it will then display the ciphered text.

PublicKeySystem:
Summary - Using the public key and Private key for ciphering and deciphering of messages
Run the PublicKeyRSABob.java file and then run the PublicKeyRSAAlice.java file. You will get an option to select between Confidentiality, Integrity or both. After selecting an option, you will then be asked to enter the plain text. The result shown on the console will be the ciphered text.

Signature:
Summary - Signature from client (here, Alice) is sent to the server (here, Bob)
Run the ELGamalBob.java file. The run the ELGamalAlice.java file. Then in the console of ElGamalBob, you can find if the Signature is verified or not.

x509:
Summary - Validation and Authorization via the certificate
First run the X509server.java. Then run the x509client.java. The validation of the certificate and its associated details can then be viewed on the console. Self-signed certificates are used for validation.

Thank you!