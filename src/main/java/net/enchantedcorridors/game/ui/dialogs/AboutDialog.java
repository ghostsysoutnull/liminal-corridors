package net.enchantedcorridors.game.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class AboutDialog extends JDialog
{
    private static final long serialVersionUID = 1L;
    

    public AboutDialog(int parentWidth )
    {
        JTabbedPane aboutDialogTabbedPane = new JTabbedPane();

        JPanel environmentPanel = createEnvironmentPanel();
        aboutDialogTabbedPane.addTab("Environment Variables", environmentPanel);

        JPanel systemPropertiesPanel = createSystemPropertiesPanel();
        aboutDialogTabbedPane.addTab("System Properties", systemPropertiesPanel);

        JPanel gameInfoPanel = createGameInfoPanel();
        aboutDialogTabbedPane.addTab("Game Information", gameInfoPanel);

        JDialog aboutFrame = new JDialog(this, "About");

        aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aboutFrame.setLayout(new BorderLayout());
        aboutFrame.add(aboutDialogTabbedPane, BorderLayout.CENTER);
        aboutFrame.setSize((int) (parentWidth * 0.7), 400);
        aboutFrame.setLocationRelativeTo(this);
        aboutFrame.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                aboutFrame.dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        aboutFrame.setVisible(true);
        aboutFrame.setAlwaysOnTop(true);
        // aboutFrame.requestFocus();
        aboutFrame.toFront();

        aboutDialogTabbedPane.requestFocus();
    }

    private JPanel createEnvironmentPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(getEnvironmentData(), new Object[] { "Variable", "Value" });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private Object[][] getEnvironmentData()
    {
        Map<String, String> environment = System.getenv();
        Object[][] data = new Object[environment.size()][2];
        int index = 0;
        for (Map.Entry<String, String> entry : environment.entrySet()) {
            data[index][0] = entry.getKey();
            data[index][1] = entry.getValue();
            index++;
        }
        return data;
    }

    private JPanel createSystemPropertiesPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(getSystemPropertiesData(), new Object[] { "Property", "Value" });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private Object[][] getSystemPropertiesData()
    {
        Properties properties = System.getProperties();
        Object[][] data = new Object[properties.size()][2];
        int index = 0;
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            data[index][0] = entry.getKey();
            data[index][1] = entry.getValue();
            index++;
        }
        return data;
    }

    private JPanel createGameInfoPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea gameInfoTextArea = new JTextArea();
        gameInfoTextArea.setEditable(false);
        gameInfoTextArea.setLineWrap(true);
        gameInfoTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(gameInfoTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        try (InputStream inputStream = getClass().getResourceAsStream("/net/enchantedcorridors/game/ui/game_info.txt"); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                gameInfoTextArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return panel;
    }
}
