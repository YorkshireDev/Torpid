package Drive;

import GUI.ViewUserInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ModelDrive implements Runnable {

    private final ViewUserInterface viewUserInterface;
    private final String myDrive;

    ModelDrive(ViewUserInterface viewUserInterface, String myDrive) {

        this.viewUserInterface = viewUserInterface;
        this.myDrive = myDrive;

    }

    @Override
    public void run() {

        boolean interrupted = false;
        File txtFile = new File(Path.of(myDrive, "Torpid_Keep_Alive_File.TXT").toFile().getAbsolutePath());

        while (! interrupted) {

            try {

                ControllerDrive.goLatch.await();

                this.viewUserInterface.updateDriveActivityLabel(myDrive, 1);

                try {

                    BufferedWriter txtFileWriter = new BufferedWriter(new FileWriter(txtFile));
                    txtFileWriter.write("");
                    txtFileWriter.flush();
                    txtFileWriter.close();

                    Files.deleteIfExists(txtFile.toPath());

                    this.viewUserInterface.updateDriveActivityLabel(myDrive, 0);

                } catch (IOException ignored) {
                    // This normally means admin privileges needed
                    this.viewUserInterface.updateDriveActivityLabel(myDrive, 2);
                }

                ControllerDrive.modelLatch.countDown();
                ControllerDrive.resetLatch.await();

            } catch (InterruptedException ignored) {
                interrupted = true;
            }

        }

        try {
            Files.deleteIfExists(txtFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
