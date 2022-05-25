package Search;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        if (isLinux) this.driveList = null; // TODO: 25/05/2022 Linux Support
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
