package com.company;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws IOException {
	   // String server = "40.87.136.135";
       // int port = 21;
       // String user = "temenos";
       // String pass = "Temenos123$$";
      //  String filename = "test.txt";

        String server = args[0];
        int port = 21;
        String user = args[1];
        String pass = args[2];
        String filename = args[3];
        String filename2 = args[4];
        boolean error = false;
        FTPSClient ftp = null;
        FileInputStream fis = null;

        try
        {
            ftp = new FTPSClient("SSL");
            ftp.setAuthValue("SSL");
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

            int reply;

            ftp.connect(server, port);
            System.out.println("working dir: "+System.getProperty("user.dir"));
            System.out.println("Connected to ftps server");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            ftp.login(user, pass);
            ftp.execPBSZ(0);
            ftp.execPROT("P");
            /// ... // transfer files
            //ftp.setBufferSize(1000);
            ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("GB2312");
            ftp.changeWorkingDirectory("/");
            //ftp.changeWorkingDirectory("/ae"); //path where my files are
            ftp.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            System.out.println("Remote system is " + ftp.getSystemName());


            fis = new FileInputStream(filename);

            ftp.storeFile(filename2, fis);
            fis.close();

            //String[] tmp = ftp.listNames();  //returns null
           // System.out.println(tmp.length);
        }
        catch (IOException ex)
        {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        }
        finally
        {
            // logs out and disconnects from server
            try
            {
                if (ftp.isConnected())
                {
                    ftp.logout();
                    ftp.disconnect();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
