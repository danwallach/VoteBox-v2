package edu.rice.starvote.ballotbox.swingui;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class SwingDisplay extends JFrame {

    public static final String FORMATSTRING =
            "<html><div style=\"text-align: center; font-face: sans-serif; font-size: %s; color: %s;\">" +
                    "%s</div></html>";
    private JPanel contentPane;
    private final JLabel statusText = new JLabel(FORMATSTRING);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SwingDisplay frame = new SwingDisplay();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SwingDisplay() {
        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 619, 480);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        statusText.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(statusText, BorderLayout.CENTER);
    }

    public void start() {
        EventQueue.invokeLater(() -> {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            setVisible(true);
        });
    }

    public void setText(String text, String size, String textColor, String bgColor) {
        statusText.setText(String.format(FORMATSTRING, size, textColor, text));
        contentPane.setBackground(Color.decode(bgColor));
    }

}
