package GUI;

import Drive.ControllerDrive;
import Search.ControllerSearch;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ViewUserInterface extends JFrame {

    private JPanel panelMain;
    private JLabel labelGitHubURL;
    private JLabel labelProgramStatus;
    private JLabel labelIntervalTime;
    private JTextField textFieldIntervalTime;
    private JButton buttonStartStop;
    private JList<String> listDriveSelection;
    private JPanel panelDriveActivity;

    private static final String[] validProgramStatusText = new String[] {"Running", "Loading", "Stopped"};
    private static final Color[] validColours = new Color[] {Color.GREEN, Color.ORANGE, Color.RED};
    private static final String[] validStartStopButtonText = new String[] {"Start", "Stop"};

    private final Map<String, JLabel> driveLabelMap = new HashMap<>();

    private boolean startStop = true; // True = Start, False = Stop

    private ControllerDrive controllerDrive = null;
    private ExecutorService controllerService = null;

    public ViewUserInterface() {

        SwingUtilities.invokeLater(this::initUserInterface);
        SwingUtilities.invokeLater(this::initNonUserInterface);

        buttonStartStop.addActionListener(e -> {

            if (startStop) {

                double intervalTime;
                List<String> selectedDriveList;

                try {

                    intervalTime = Double.parseDouble(textFieldIntervalTime.getText());
                    selectedDriveList = listDriveSelection.getSelectedValuesList();

                    if (intervalTime < 0.1d || intervalTime > 10.0d) {
                        showMessageBox("Warning -> Interval Time must be between 0.1s (100ms) and 10s!");
                        return;
                    }

                    if (selectedDriveList.size() == 0) {
                        showMessageBox("Warning! -> You need to select at least one drive!");
                        return;
                    }

                } catch (NumberFormatException ignored) {
                    showMessageBox("Warning -> Interval Time needs to be a number!");
                    return;
                }

                this.driveLabelMap.clear();
                this.panelDriveActivity.removeAll();

                for (String selectedDrive : selectedDriveList) {
                    this.driveLabelMap.put(selectedDrive, new JLabel(selectedDrive));
                    updateDriveActivityLabel(selectedDrive, 2);
                    this.panelDriveActivity.add(driveLabelMap.get(selectedDrive));
                }

                this.startStop = false;
                this.buttonStartStop.setText(validStartStopButtonText[1]);

                this.controllerService = Executors.newSingleThreadExecutor();

                this.controllerDrive = new ControllerDrive(this, selectedDriveList, intervalTime);

                this.controllerService.submit(controllerDrive);
                this.controllerService.shutdown();

                updateProgramStatusLabel(0, 0);

            } else {

                if (controllerDrive != null && controllerService != null) {

                    boolean controllerShutDown = false;

                    try {
                        this.controllerDrive.setStopSignal();
                        controllerShutDown = this.controllerService.awaitTermination(30L, TimeUnit.SECONDS);
                    } catch (InterruptedException ignored) {}

                    if (controllerShutDown) {

                        this.startStop = true;
                        this.buttonStartStop.setText(validStartStopButtonText[0]);

                        this.controllerDrive = null;
                        this.controllerService = null;

                        this.driveLabelMap.clear();
                        this.panelDriveActivity.removeAll();
                        this.panelDriveActivity.add(new JLabel("N/A"));

                        updateProgramStatusLabel(2, 2);

                    } else {

                        // TODO: 25/05/2022 Error Log
                        this.controllerService.shutdownNow();
                        System.exit(1);

                    }

                }

            }

        });

    }

    private void showMessageBox(String messageText) {

        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this,
                messageText,
                "Information",
                JOptionPane.INFORMATION_MESSAGE));

    }

    private void updateProgramStatusLabel(int programStatusID, int colourID) {

        SwingUtilities.invokeLater(() -> {

            this.labelProgramStatus.setText(validProgramStatusText[programStatusID]);
            this.labelProgramStatus.setForeground(validColours[colourID]);

        });

    }

    public void updateDriveActivityLabel(String driveLetter, int colourID) {

        SwingUtilities.invokeLater(() -> driveLabelMap.get(driveLetter).setForeground(validColours[colourID]));

    }

    private void initUserInterface() {

        updateProgramStatusLabel(1, 1);

        this.labelGitHubURL.setText("https://github.com/YorkshireDev/");

        this.labelIntervalTime.setText("Interval Time (Seconds)");
        this.textFieldIntervalTime.setText("2.5");

        this.buttonStartStop.setText(validStartStopButtonText[0]);

        this.panelDriveActivity.setLayout(new FlowLayout());
        this.panelDriveActivity.add(new JLabel("N/A"));

        this.setContentPane(panelMain);
        this.setTitle("Torpid");
        this.setPreferredSize(new Dimension(448, 224));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);

    }

    private void initNonUserInterface() {

        updateProgramStatusLabel(1, 1);

        DefaultListCellRenderer driveSelectionRenderer = (DefaultListCellRenderer) listDriveSelection.getCellRenderer();
        driveSelectionRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultListModel<String> driveList = new DefaultListModel<>();

        ControllerSearch controllerSearch = new ControllerSearch();
        controllerSearch.searchForDrives();
        driveList.addAll(controllerSearch.getDriveList());

        this.listDriveSelection.setModel(driveList);

        updateProgramStatusLabel(2, 2);

    }

}
