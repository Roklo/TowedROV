package autoupdater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AutoUpdater
{

    public static void main(String[] args) throws IOException
    {

        //URL pointing to the file
        String sUrl = "https://github.com/Roklo/TowedROV/archive/master.zip";
        String destinationZip = "C://Temp//file.zip";
        String destination = "C://Temp";
        String destinationVersion = "C:\\temp\\TowedROV-master\\TowedROV_GUI\\PublishedVersion\\Version.txt";

        
        URL url = new URL(sUrl);
        ZipExtractor zipE = new ZipExtractor();

        //File where to be downloaded
        File filezip = new File(destinationZip);

        System.out.println("Starting to download file...");
        URLReader.copyURLToFile(url, filezip);
        System.out.println("Starting to unzip...");
        zipE.unzip(destinationZip, destination);
        System.out.println("Unzip complete");
        System.out.println("Checking version...");

        File versionFile = new File(destinationVersion);
        String newVersion = "0.0.0";
        String oldVersion = "0.0.0";

        BufferedReader br = new BufferedReader(new FileReader(versionFile));

        String st;
        while ((st = br.readLine()) != null)
        {
            newVersion = st;
        }
        System.out.println("New version is: " + newVersion);
        System.out.println("Old version is: " + oldVersion);
    }

}
