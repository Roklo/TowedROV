package autoupdater;

import java.io.File;
import java.io.FileOutputStream;
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

        URL url = new URL(sUrl);
        ZipExtractor zipE = new ZipExtractor();

        //File where to be downloaded
        File file = new File(destinationZip);

        System.out.println("Starting to download file...");
        URLReader.copyURLToFile(url, file);
        System.out.println("Starting to unzip");
        zipE.unzip(destinationZip, destination);
        System.out.println("Unzip complete");
    }

}
