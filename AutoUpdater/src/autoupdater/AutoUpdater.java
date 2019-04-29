package autoupdater;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import java.net.InetAddress;

public class AutoUpdater
{

    protected static String sUrl = "https://github.com/Roklo/TowedROV_GUI/archive/master.zip";
    protected static String destinationZip = "C:\\Temp\\file.zip";
    protected static String destinationToDownloadedFolder = "C:\\temp\\TowedROV_GUI-master";
    protected static String destination = "C:\\Temp";
    protected static String destinationToNewFiles = "C:\\temp\\TowedROV_GUI-master";
    protected static String destinationToSaveLocation = "C:\\TowedROV";
    protected static String newDestinationVersion = "C:\\temp\\TowedROV_GUI-master\\Version.txt";
    protected static String oldDestinationVersion = "C:\\TowedROV\\Version.txt";

    public static void main(String[] args) throws IOException
    {
        System.out.println("Checking internet connection...");
        boolean connectedToInternet = false;
//        Checking internet connection
        InetAddress address = InetAddress.getByName("8.8.8.8");
        String hostName = address.getHostName().toLowerCase();

        if (hostName.contains("google"))
        {
            connectedToInternet = true;
        } else
        {
            connectedToInternet = false;
        }

        if (connectedToInternet)
        {
            //URL pointing to the file
            URL url = new URL(sUrl);
            ZipExtractor zipE = new ZipExtractor();

            //File where to be downloaded
            File filezip = new File(destinationZip);

            System.out.println("Starting to download file...");
            try
            {
              //  URLReader.copyURLToFile(url, filezip);
                connectedToInternet = true;
            } catch (Exception e)
            {
                connectedToInternet = false;
                System.out.println("Not connected to internet or GIT is not available");
            }

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
                        startGUI();

                    } catch (Exception e)
                    {
                    }

                } else
                {
                    //Do not update
                    System.out.println("Newest version already installed!");
                    System.out.println("Cleaning up...");
                    cleaner();
                    System.out.println("Done");
                    startGUI();
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
                System.out.println("Cleaning up...");
                cleaner();
                System.out.println("Done");
                startGUI();

            }

        } else
        {
            System.out.println("------------------------------------------------------------------");
            System.out.println("Warning: Not connected to internet...");
            System.out.println("------------------------------------------------------------------");

            File oldVersionFile = new File(oldDestinationVersion);

            if (oldVersionFile.exists() && !oldVersionFile.isDirectory())
            {
                // do something
                System.out.println("Using local files");
                startGUI();
            } else
            {
                System.out.println("------------------------------------------------------------------");
                System.out.println("ERROR: TowedRov_GUI is not installed on this computer!!!");
                System.out.println("Connect to internet and run launcher again to fix the problem");
                System.out.println("------------------------------------------------------------------");
                System.out.println("Program exiting in ...");
                try
                {
                    for (int i = 10; i >= 0; i--)
                    {
                        System.out.println(i);
                        Thread.sleep(1000);

                    }

                } catch (Exception e)
                {
                }

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
        try
        {
            String exeLocation = destinationToSaveLocation + "\\TowedROV_GUI.exe";
            System.out.println("Starting GUI...");

            //  Runtime.getRuntime().exec(exeLocation, null, new File(destinationToSaveLocation));
            System.out.println("Done, GUI will start in: ");
            for (int i = 5; i >= 0; i--)
            {
                System.out.println(i);
                Thread.sleep(1000);

            }
            Runtime.getRuntime().exec("cmd /c start \"\" C:\\TowedROV\\TowedROV_GUI.exe");
            Thread.sleep(10);
        } catch (Exception e)
        {
            System.out.println("Error could not start TowedROV_GUI");
            System.out.println(e);
        }

    }

}
