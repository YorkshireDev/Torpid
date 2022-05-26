package Search;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class ModelSearch {

    // TODO: 25/05/2022 Linux Support

    private List<String> driveList;
    private final boolean isLinux;

    ModelSearch() {

        this.driveList = null;
        this.isLinux = ! System.getProperty("os.name").toUpperCase().startsWith("WINDOWS");

    }

    List<String> getDriveList() {
        return driveList;
    }

    void searchForDrives() {

        this.driveList = new ArrayList<>();

        if (isLinux) {

            try {

                ProcessBuilder dfProcessBuilder = new ProcessBuilder();
                dfProcessBuilder.command("df", "-h");

                Process dfProcess = dfProcessBuilder.start();

                BufferedReader dfReader = new BufferedReader(new InputStreamReader(dfProcess.getInputStream()));
                String currentLine;

                while ((currentLine = dfReader.readLine()) != null) {

                    String[] currentLineSplit = currentLine.split(" ");
                    currentLine = currentLineSplit[currentLineSplit.length - 1];

                    if (currentLine.startsWith("/")) this.driveList.add(currentLine);

                }

                dfProcess.waitFor();

            } catch (IOException | InterruptedException e) {
                return;
            }

        }

        else {

            String[] driveLetters = "QWERTYUIOPASDFGHJKLZXCVBNM".split("");

            for (String driveLetter : driveLetters) {

                String qualifiedDriveLetter = driveLetter + ":" + File.separator;
                if (Files.exists(Path.of(qualifiedDriveLetter))) this.driveList.add(qualifiedDriveLetter);

            }

        }

        if (driveList != null) Collections.sort(driveList);

    }

}
