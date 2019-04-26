package autoupdater;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;

public class AutoUpdater
{

    protected static String sUrl = "https://github.com/Roklo/TowedROV/archive/master.zip";
    protected static String destinationZip = "C:\\Temp\\file.zip";
    protected static String destinationToDownloadedFolder = "C:\\Temp\\TowedROV-master";
    protected static String destination = "C:\\Temp";
    protected static String destinationToNewFiles = "C:\\temp\\TowedROV-master\\TowedROV_GUI\\PublishedVersion";
    protected static String destinationToSaveLocation = "C:\\TowedROV";
    protected static String newDestinationVersion = "C:\\temp\\TowedROV-master\\TowedROV_GUI\\PublishedVersion\\Version.txt";
    protected static String oldDestinationVersion = "C:\\TowedROV\\Version.txt";

    public static void main(String[] args) throws IOException
    {
        boolean connectedToInternet = false;
        //URL pointing to the file
        URL url = new URL(sUrl);
        ZipExtractor zipE = new ZipExtractor();

        //File where to be downloaded
        File filezip = new File(destinationZip);

        System.out.println("Starting to download file...");
        try
        {
            URLReader.copyURLToFile(url, filezip);
            connectedToInternet = true;
        } catch (Exception e)
        {
            connectedToInternet = false;
            System.out.println("Not connected to internet or GIT is not available");
        }
        if (connectedToInternet)
        {

            System.out.println("Starting to unzip...");
            zipE.unzip(destinationZip, destination);
            System.out.println("Unzip complete");
            System.out.println("Checking version...");

            //Version controll
            String newVersion = "0.0.0";
            String oldVersion = "0.0.0";
            boolean guiExist = false;
            try
            {
                File oldVersionFile = new File(oldDestinationVersion);
                oldVersion = FileUtils.readFileToString(oldVersionFile);
                guiExist = true;
            } catch (Exception e)
            {
                System.out.println("Can't find old file");
                guiExist = false;
            }
            if (guiExist)
            {
                //Read new version
                try
                {
                    File newVersionFile = new File(newDestinationVersion);
                    newVersion = FileUtils.readFileToString(newVersionFile);

                } catch (Exception e)
                {
                    System.out.println("Can't find new file");
                }

                System.out.println("New version is: " + newVersion);
                System.out.println("Old version is: " + oldVersion);

                //Translate version to int
                newVersion = newVersion.replace(".", "");
                oldVersion = oldVersion.replace(".", "");

                int intNewVersion = Integer.parseInt(newVersion);
                int intOldVersion = Integer.parseInt(oldVersion);

                File source = new File(destinationToNewFiles);
                File dest = new File(destinationToSaveLocation);
                if (intNewVersion > intOldVersion)
                {
                    //Do update
                    try
                    {
                        System.out.println("Deleting old files...");
                        FileUtils.deleteQuietly(dest);
                        System.out.println("Installing new files...");
                        FileUtils.copyDirectory(source, dest);
                        System.out.println("Cleaning up...");
                        cleaner();
                        System.out.println("Done");

                    } catch (Exception e)
                    {
                    }

                } else
                {
                    //Do not update
                    System.out.println("Newest version already installed!");
                }
            } else
            {
                File source = new File(destinationToNewFiles);
                File dest = new File(destinationToSaveLocation);

                try
                {
                    FileUtils.copyDirectory(source, dest);
                } catch (Exception e)
                {
                }
                System.out.println("New files installed");
            }

        }
    }

    private static void cleaner()
    {
        File source = new File(destinationToDownloadedFolder);
        File sourcezip = new File(destinationZip);
        FileUtils.deleteQuietly(source);
        FileUtils.deleteQuietly(sourcezip);
    }

    private static void startGUI()
    {

    }

}
