/**
 * @author ${Hardik Vasa}
 *
 * ${MessageDigest, MD5, SHA}
 */

package messageDigest;

import java.security.*;
import java.util.Scanner;

public class MessageDigest {

    public static void main(String[] args)throws Exception
    {
        System.out.println("Which kind of operation do you want to perform?");
        System.out.println("Enter 'md5' for MD5 function");
        System.out.println("Enter 'sha' for MD5 function");

        //takes input from the user regarding the option to be selected
        Scanner input = new Scanner(System.in);
        System.out.print("Your Option: ");
        String user_option = input.nextLine();
        //System.out.print(user_option); //used for troubleshooting

        //conditional to compare if the input is 'md5'
        if (user_option.equals("md5")){

            //taking plain text as input which will be ciphered
            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter any text: ");
            String user_option2 = input.nextLine();
            String md5="";

            //making use of the MessageDigest function of the java security library
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(user_option2.getBytes());
            byte byteData[] = md.digest();

            //convert the byte to hex format using the for loop
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            md5 = sb.toString(); //convert it into string for representation
            System.out.println("MD5: " + md5);  //showing it to the user
        }

        //conditional to compare if the input is 'sha'
        else if (user_option.equals("sha")) {

            //taking plain text as input which will be ciphered
            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter a Plaintext: ");
            String user_option2 = input.nextLine();
            String sha="";

            //making use of the MessageDigest function of the java security library
            java.security.MessageDigest md2 = java.security.MessageDigest.getInstance("SHA");
            md2.update(user_option2.getBytes());
            byte[] b = md2.digest();
            int i;
            StringBuffer buf = new StringBuffer("");

            //convert the byte to hex format using the for loop
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));  //converting the value to string
            }
            sha = buf.toString();
            System.out.println("SHA: " + sha.toString()); //showing it to the user
        }

        //if the input is not 'md5' or 'sha' then it gives an error to user
        else {
            System.out.println("\nInvalid Input");
        }

    }
}
