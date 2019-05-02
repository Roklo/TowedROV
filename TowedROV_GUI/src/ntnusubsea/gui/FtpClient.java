/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 */
public class FtpClient implements Runnable
{

    private String server;
    private int port;
    private String user;
    private String password;
    private FTPClient ftp;

    public FtpClient(String IP)
    {
        this.server = IP; //The IP address for the camera RPi.
        this.port = 21; // The FTP port
        this.user = "pi";
        this.password = "";

    }
    
    @Override
    public void run()
    {
        //this.open();
        while(true)
        {
            // be alive
        }
    }



    public void open()
    {
        try
        {
            ftp = new FTPClient();

            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

            ftp.connect(server, port);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                throw new IOException("Exception in connecting to FTP Server");
            }

            ftp.login(user, password);
        } catch (IOException ex)
        {
            System.out.println("IOException in FtpClient.open(): " + ex.getMessage());
        }

    }

    public Collection<String> getFileList(String path)
    {
        Collection<String> fileList = null;
        try
        {
            FTPFile[] files = ftp.listFiles(path);
            fileList = Arrays.stream(files)
                    .map(FTPFile::getName)
                    .collect(Collectors.toList());

        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return fileList;
    }

    public void downloadFile(String source, String destination, String folderPath)
    {
        try
        {
            Path destinationPath = Paths.get(folderPath);
            if (Files.notExists(destinationPath))
            {
                boolean success = (new File(folderPath)).mkdirs();
                if (!success)
                {
                    System.out.println("Directory creation failed!");
                }
                else
                {
                    System.out.println("Directory created at " + destination);
                }
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            FileOutputStream out = new FileOutputStream(destination);
            ftp.retrieveFile(source, out);
        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public void disconnect()
    {
        if (ftp.isConnected())
        {
            try
            {
                ftp.disconnect();
            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void close() throws IOException
    {
        try
        {
            ftp.disconnect();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
