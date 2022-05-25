package Drive;

import GUI.ViewUserInterface;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerDrive implements Runnable {

    private final ModelDrive[] modelDriveArray;
    private final long intervalTime;

    private static volatile boolean keepAlive;

    private final ExecutorService driveService;
    static CountDownLatch goLatch;
    static CountDownLatch resetLatch;
    static CountDownLatch modelLatch;

    public ControllerDrive(ViewUserInterface viewUserInterface, List<String> selectedDriveList, double intervalTime) {

        this.modelDriveArray = new ModelDrive[selectedDriveList.size()];

        for (int i = 0; i < modelDriveArray.length; i++)
            this.modelDriveArray[i] = new ModelDrive(viewUserInterface, selectedDriveList.get(i));

        this.intervalTime = (long) (intervalTime * 1000.0d);

        keepAlive = true;

        this.driveService = Executors.newFixedThreadPool(modelDriveArray.length);

        goLatch = new CountDownLatch(1);
        resetLatch = new CountDownLatch(1);
        modelLatch = new CountDownLatch(modelDriveArray.length);

    }

    public void setStopSignal() {
        keepAlive = false;
    }

    @Override
    public void run() {

        for (ModelDrive modelDrive : modelDriveArray) this.driveService.submit(modelDrive);

        while (keepAlive) {

            try {

                goLatch.countDown();
                modelLatch.await();

                goLatch = new CountDownLatch(1);
                resetLatch.countDown();

                modelLatch = new CountDownLatch(modelDriveArray.length);
                resetLatch = new CountDownLatch(1);

                Thread.sleep(intervalTime);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        this.driveService.shutdownNow();

    }

}
