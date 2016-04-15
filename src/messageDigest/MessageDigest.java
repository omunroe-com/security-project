package messageDigest;

import java.security.*;
import java.util.Scanner;

public class MessageDigest {

    public static void main(String[] args)throws Exception
    {
        System.out.println("Which kind of function do you want?");
        System.out.println("Enter 'md5' for MD5 function");
        System.out.println("Enter 'sha' for MD5 function");

        Scanner input = new Scanner(System.in);
        System.out.print("Your Option: ");
        String user_option = input.nextLine();
        //System.out.print(user_option);

        if (user_option.equals("md5")){
            //MD5

            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter any text: ");
            String user_option2 = input.nextLine();
            String md5="";
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(user_option2.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            md5 = sb.toString();
            System.out.println("MD5: " + md5);
        }

        else if (user_option.equals("sha")) {
            //SHA
            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter a Plaintext: ");
            String user_option2 = input.nextLine();
            String sha="";
            java.security.MessageDigest md2 = java.security.MessageDigest.getInstance("SHA");
            md2.update(user_option2.getBytes());
            byte[] b = md2.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            sha = buf.toString();
            System.out.println("SHA: " + sha.toString());
        }

        else {
            System.out.println("\nInvalid Input");
        }

    }
}
