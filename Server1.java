import java.net.*;  
import java.io.*;  
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.lang.*;
import java.security.KeyFactory.*;

import javax.crypto.Cipher;
class Server1{  
public static void main(String args[])throws Exception{  
ServerSocket ss=new ServerSocket(3333);  
Socket s=ss.accept();  
  
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
 
ObjectInputStream obin = new ObjectInputStream(s.getInputStream());
ObjectOutputStream obout = new ObjectOutputStream(s.getOutputStream());
PrivateKey priv_s=null;
PublicKey pub_s=null;
PublicKey pub_c=null;  
KeyFactory kf=null;


try 
		{
      			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		      	keyGen.initialize(512);
	      		KeyPair pair = keyGen.generateKeyPair();
	      		priv_s= pair.getPrivate();
      			pub_s= pair.getPublic();
			pub_c=null;
			System.out.println("PRIVATE KEY");
			System.out.println(priv_s);
			System.out.println("\n\nPUBLIC KEY");
			System.out.println(pub_s);

    		} 
		catch (Exception e) 
		{
      		e.printStackTrace();
   		}

Frame frame_i=(Frame)obin.readObject();
byte[] pubKey = frame_i.data;                 
X509EncodedKeySpec ks = new X509EncodedKeySpec(pubKey);
kf = KeyFactory.getInstance("RSA");
pub_c = kf.generatePublic(ks);

System.out.println("\nCLIENT PUBLIC KEY");

System.out.println(pub_c);


Frame frame_o = new Frame();
frame_o.data = pub_s.getEncoded();
obout.writeObject(frame_o);

System.out.println("1. Encryption-Decryption\n2. Digital Signature");
System.out.println("\nEnter Your Choice");

int ch;
ch=Integer.parseInt(br.readLine());

String str="",str2=""; 
char ans; 
switch(ch)
{
	case 1:
		System.out.println("1. Encryption\n2. Decryption");
		System.out.println("\nEnter Your Choice");
		ch=Integer.parseInt(br.readLine());
		switch(ch)
		{	
		case 1:System.out.println("\n Performing Encryption...");
			try 
			{
			while(!str.equals("stop")){  
			str=br.readLine(); 
			byte[] cipherText = encrypt(str, pub_c); 
			System.out.println("Encrypted Text:"+cipherText+"\n");
			DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

			dOut.writeInt(cipherText.length); // write length of the message
			dOut.write(cipherText);
			} 
			}
			
        		finally 
			{
          		if (s!=null) s.close();
        		}
			break;

		case 2:System.out.println("\n Performing Decryption...");
			try {
				while(!str.equals("stop")){  
		
				DataInputStream dIn = new DataInputStream(s.getInputStream());
				
				int length = dIn.readInt();                    // read length of incoming message
				if(length>0) {
    				byte[] message = new byte[length];
    				dIn.readFully(message, 0, message.length); // read the message
				System.out.println("Encrypted Text:"+message);
				System.out.println("Do You Want to Decrypt the Message (y/n)");
				ans=(br.readLine()).charAt(0);
				if(ans=='y'||ans=='Y')
				{
				final String plainText = decrypt(message, priv_s);
				System.out.println("Decrypted Text:"+plainText+"\n");
				}
				}
				}
				}
			
   			finally {
      				if (s!= null) s.close();
      				if (ss != null) ss.close();
     				}
			break;
		}

		break;


	case 2:
		System.out.println("1. Sign\n2. Unsign");
		System.out.println("\nEnter Your Choice");
		ch=Integer.parseInt(br.readLine());
		switch(ch)
		{
		case 1:System.out.println("\n Performing Signing...");
			try 
			{
			while(!str.equals("stop")){  
			str=br.readLine(); 
			byte[] cipherText = sign(str, priv_s); 
			System.out.println("Encrypted Text:"+cipherText+"\n");
			DataOutputStream dOut = new DataOutputStream(s.getOutputStream());

			dOut.writeInt(cipherText.length); // write length of the message
			dOut.write(cipherText);
			} 
			}
			
        		finally 
			{
          		if (s!=null) s.close();
			if (ss != null) ss.close();
        		}
			break;

		case 2:System.out.println("\n Performing Unsigning...");
			try {
				while(!str.equals("stop")){  
		
				DataInputStream dIn = new DataInputStream(s.getInputStream());

				int length = dIn.readInt();                    // read length of incoming message
				if(length>0) {
    				byte[] message = new byte[length];
    				dIn.readFully(message, 0, message.length); // read the message
				System.out.println("Encrypted Text:"+message);
				System.out.println("Do You Want to Unsign the Message (y/n)");
				ans=(br.readLine()).charAt(0);
				if(ans=='y'||ans=='Y')
				{
				final String plainText = unsign(message, pub_c);
				System.out.println("Decrypted Text:"+plainText+"\n");
				}
				}
				}
				}
			
   			finally {
      				if (s!= null) s.close();
      				if (ss != null) ss.close();
     				}
			break;
		
		}
		break;
		
}

 
}
public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA");
      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }
 public static String decrypt(byte[] text, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA");

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new String(dectyptedText);
  }

public static byte[] sign(String text, PrivateKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA");
      // encrypt the plain text using the Private key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }
 public static String unsign(byte[] text, PublicKey key) {
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance("RSA");

      // decrypt the text using the Public key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new String(dectyptedText);
  }


}  