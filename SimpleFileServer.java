import java.io.*;
import java.net.*;

public class SimpleFileServer {

  public final static int SOCKET_PORT = 13267;  // you may change this
  public final static String
  FILE_TO_RECEIVED = "file-rec1.txt"; 

  public final static String FILE_TO_SEND = "file-rec1.txt";
  public final static int FILE_SIZE = 6022386;

  public static void main (String [] args ) throws IOException {
   	
	int bytesRead;
    	int current = 0;
    	FileOutputStream fos = null;
    	BufferedOutputStream bos = null;

 	FileInputStream fis = null;
    	BufferedInputStream bis = null;
    	OutputStream os = null;

    	ServerSocket servsock = null;
    	Socket sock = null;
      	servsock = new ServerSocket(SOCKET_PORT);
       	System.out.println("Waiting...");
        sock = servsock.accept();
        System.out.println("Accepted connection : " + sock);
	int ch;
        
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
	
	BufferedReader br1=null;
	
	System.out.println("1. Receiving Encrypted File \n2. Sending Decrypted File\n");
	System.out.println("Enter Your Choice");
	ch=Integer.parseInt(br.readLine());
	switch(ch)
	{

		case 1: 
			try {
			byte [] mybytearray  = new byte [FILE_SIZE];
      			InputStream is = sock.getInputStream();
      			fos = new FileOutputStream(FILE_TO_RECEIVED);
      			bos = new BufferedOutputStream(fos);
      			bytesRead = is.read(mybytearray,0,mybytearray.length);
      			current = bytesRead;

      			do {
         			bytesRead =
            			is.read(mybytearray, current, (mybytearray.length-current));
         			if(bytesRead >= 0) current += bytesRead;
      			} while(bytesRead > -1);

      			bos.write(mybytearray, 0 , current);
      			bos.flush();
      			System.out.println("File " + FILE_TO_RECEIVED
          		+ " downloaded (" + current + " bytes read)");
    			}

   			finally {
      				if (fos != null) fos.close();
      				if (bos != null) bos.close();
      				if (sock != null) sock.close();
      				if (servsock != null) servsock.close();
     				}
			System.out.println("\nContent of file-rec1.txt...");
			br1 = new BufferedReader(new FileReader("file-rec1.txt"));
    			for (String line; (line = br1.readLine()) != null;) {
        		System.out.print(line);
        		}
			System.out.println("\n");
			break;

		case 2: 
			
        		try {
			File myFile = new File (FILE_TO_SEND);
          		byte [] mybytearray  = new byte [(int)myFile.length()];
          		fis = new FileInputStream(myFile);
          		bis = new BufferedInputStream(fis);
          		bis.read(mybytearray,0,mybytearray.length);
          		os = sock.getOutputStream();
          		System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
          		os.write(mybytearray,0,mybytearray.length);
          		os.flush();
          		}
        		finally {
          		if (bis != null) bis.close();
          		if (os != null) os.close();
          		if (sock!=null) sock.close();
          		if (servsock != null) servsock.close();	 
        		}
			System.out.println("\nContent of file-rec1.txt...");
			br1 = new BufferedReader(new FileReader("file-rec1.txt"));
    			for (String line; (line = br1.readLine()) != null;) {
        		System.out.print(line);
        		}
			System.out.println("\n");
			break;


	}
        System.out.println("\nDone.");
     
  }
}

