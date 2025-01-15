import javax.swing.*;
import java.awt.*;

public class FloatingEyes extends JFrame {
    private final int eyeRadius = 40;  // Radius of the eyes
    private final int pupilRadius = 10;  // Radius of the pupils
    private final int eyeSpacing = 60;  // Spacing between the eyes

    public FloatingEyes() {
        // Configure transparent, always-on-top window
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background
        setSize(200, 120);

        // Position the window above the taskbar at the bottom-right corner
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskbarHeight = screenInsets.bottom;
        setLocation(screenSize.width - getWidth(), screenSize.height - getHeight() - taskbarHeight);

        // Timer to repaint the window at regular intervals
        Timer timer = new Timer(10, e -> repaint());
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clear the window for transparency
        g2d.clearRect(0, 0, getWidth(), getHeight());

        // Eye center positions
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int leftEyeX = centerX - eyeSpacing;
        int rightEyeX = centerX + eyeSpacing;

        // Draw eyes
        g2d.setColor(Color.WHITE);
        g2d.fillOval(leftEyeX - eyeRadius, centerY - eyeRadius, eyeRadius * 2, eyeRadius * 2); // Left eye
        g2d.fillOval(rightEyeX - eyeRadius, centerY - eyeRadius, eyeRadius * 2, eyeRadius * 2); // Right eye

        // Get mouse position
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Map mouse position to pupil movement within eye boundaries
        int leftPupilX = mapToEye(leftEyeX, mousePosition.x, screenSize.width);
        int leftPupilY = mapToEye(centerY, mousePosition.y, screenSize.height);
        int rightPupilX = mapToEye(rightEyeX, mousePosition.x, screenSize.width);
        int rightPupilY = mapToEye(centerY, mousePosition.y, screenSize.height);

        // Draw pupils
        g2d.setColor(Color.BLACK);
        g2d.fillOval(leftPupilX - pupilRadius, leftPupilY - pupilRadius, pupilRadius * 2, pupilRadius * 2);
        g2d.fillOval(rightPupilX - pupilRadius, rightPupilY - pupilRadius, pupilRadius * 2, pupilRadius * 2);
    }

    private int mapToEye(int eyeCenter, int mouseCoord, int screenSize) {
        // Map mouse coordinate to pupil position relative to the eye center
        double normalized = (double) mouseCoord / screenSize; // Normalize mouse position (0 to 1)
        int maxOffset = eyeRadius - pupilRadius; // Max distance pupil can move
        return eyeCenter + (int) (maxOffset * (normalized - 0.5) * 2); // Scale and center
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FloatingEyes eyes = new FloatingEyes();
            eyes.setVisible(true);
        });
    }
}
