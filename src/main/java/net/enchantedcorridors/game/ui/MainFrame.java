package net.enchantedcorridors.game.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.enchantedcorridors.game.ui.dialogs.AboutDialog;
import net.enchantedcorridors.game.ui.dialogs.CommandSelectionDialog;
import net.enchantedcorridors.game.ui.dialogs.ThemeDialog;
import net.enchantedcorridors.game.ui.themes.Theme;
import net.enchantedcorridors.game.ui.themes.ThemeBuilder;
import net.enchantedcorridors.game.ui.themes.ThemeChangeListener;

public class MainFrame extends JFrame implements MouseWheelListener, ThemeChangeListener, Game
{
    private static final long serialVersionUID = 1L;

    private JTextArea gameStateTA;
    private JTextField commandTF;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;

    private LinkedList<String> commandHistory;
    private int commandIndex;

    private int itemIdx;

    private Font defaultFont;
    private Font monoFont;
    private Boolean isUsingMonoFont = false;
    private int defaultFontSize;

    private boolean isFullScreen;

    public MainFrame()
    {
        setTitle("Mystery Mansion Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameStateTA = new JTextArea();
        gameStateTA.setEditable(false);
        gameStateTA.setLineWrap(true);
        gameStateTA.setWrapStyleWord(true);
        JScrollPane textScrollPane = new JScrollPane(gameStateTA);

        inventoryTableModel = new DefaultTableModel(new Object[] { "Order", "Name", "Description" }, 0);
        inventoryTable = new JTable(inventoryTableModel);
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, inventoryScrollPane);
        splitPane.setResizeWeight(0.8);
        splitPane.setDividerLocation(550);
        add(splitPane, BorderLayout.CENTER);

        this.commandTF = buildCommandTF();
        add(commandTF, BorderLayout.SOUTH);

        Border inventoryBorder = BorderFactory.createTitledBorder("Inventory");
        inventoryScrollPane.setBorder(inventoryBorder);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        commandHistory = new LinkedList<>();
        commandIndex = 0;
        itemIdx = 1;

        defaultFont = gameStateTA.getFont();
        monoFont = new Font(Font.MONOSPACED, Font.PLAIN, defaultFont.getSize());

        appendToTextArea("Welcome to the Mystery Mansion! Enter your commands below.");

        applyTheme(ThemeBuilder.it().from("solar"));
        commandTF.requestFocusInWindow();
        commandTF.setText("help");

        gameStateTA.addMouseWheelListener(this);
        createKeyBindings();

        isFullScreen = false;
    }

    public void processCommand(String command)
    {
        appendToTextArea("> " + command);

        if (command.equalsIgnoreCase("help")) {
            appendToTextArea("Available commands:\n- go <direction>\n- examine <object>\n- inventory\n- about\n- quit\n- theme\n- red\n- dark\n- solar\n- mono");
        } else if (command.equalsIgnoreCase("go north")) {
            appendToTextArea("You head north...");
        } else if (command.equalsIgnoreCase("examine door")) {
            appendToTextArea("You closely examine the door, noting its intricate carvings.");
        } else if (command.equalsIgnoreCase("inventory")) {
            appendToTextArea("You have the following items in your inventory:");
            for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
                String order = inventoryTableModel.getValueAt(i, 0).toString();
                String name = inventoryTableModel.getValueAt(i, 1).toString();
                String description = inventoryTableModel.getValueAt(i, 2).toString();
                appendToTextArea(order + ". " + name + " - " + description);
            }
        } else if (command.equalsIgnoreCase("pickup item")) {
            String itemName = "Item " + itemIdx;
            String itemDescription = "Description of " + itemName;
            appendToInventory(itemName, itemDescription);
        } else if (command.equalsIgnoreCase("quit")) {
            appendToTextArea("Goodbye!");
            System.exit(0);
        } else if (command.equalsIgnoreCase("dark")) {
            applyTheme(ThemeBuilder.it().from("dark"));
        } else if (command.equalsIgnoreCase("red")) {
            applyTheme(ThemeBuilder.it().from("red"));
        } else if (command.equalsIgnoreCase("solar")) {
            applyTheme(ThemeBuilder.it().from("solar"));
        } else if (command.equalsIgnoreCase("mono")) {
            toggleFontMono();
        } else if (command.equalsIgnoreCase("theme")) {
            openThemeDialog();
        } else if (command.equalsIgnoreCase("clear")) {
            clearTextArea();
        } else if (command.equalsIgnoreCase("about")) {
            openAboutDialog();
        } else {
            appendToTextArea("Invalid command. Type 'help' for a list of available commands.");
        }
    }

    private void clearTextField()
    {
        commandTF.setText("");
    }

    private void clearTextArea()
    {
        gameStateTA.setText("");
    }

    private void appendToTextArea(String text)
    {
        gameStateTA.append(text + "\n");
        gameStateTA.setCaretPosition(gameStateTA.getDocument().getLength());
    }

    private void appendToInventory(String name, String description)
    {
        inventoryTableModel.addRow(new Object[] { itemIdx++, name, description });
        inventoryTable.scrollRectToVisible(inventoryTable.getCellRect(inventoryTable.getRowCount() - 1, 0, true));
    }

    private void applyTheme(Theme t)
    {
        applyThemeToComponent(gameStateTA, t);
        applyThemeToComponent(inventoryTable, t);
        applyThemeToComponent(commandTF, t);
    }

    private static void applyThemeToComponent(Component c, Theme t)
    {
        c.setBackground(t.getBackground());
        c.setForeground(t.getForeground());
    }

    private void increaseFontSize()
    {
        Font currentFont = gameStateTA.getFont();
        int currentSize = currentFont.getSize();
        Font newFont = currentFont.deriveFont(currentSize + 1.0f);
        gameStateTA.setFont(newFont);
        commandTF.setFont(newFont);
        updateTFSize(commandTF);
    }

    private void decreaseFontSize()
    {
        Font currentFont = gameStateTA.getFont();
        int currentSize = currentFont.getSize();
        if (currentSize > 1) {
            Font newFont = currentFont.deriveFont(currentSize - 1.0f);
            gameStateTA.setFont(newFont);
            commandTF.setFont(newFont);
            updateTFSize(commandTF);
        }
    }

    void updateTFSize(JTextField textField)
    {
        FontMetrics fontMetrics = textField.getFontMetrics(textField.getFont());
        int textWidth = fontMetrics.stringWidth(textField.getText());
        int textHeight = fontMetrics.getHeight();

        Dimension preferredSize = textField.getPreferredSize();
        int maxWidth = 200;
        preferredSize.width = Math.min(textWidth + 10, maxWidth); // Add padding and limit to maximum width
        preferredSize.height = textHeight + 10; // Add padding

        textField.setPreferredSize(preferredSize);
        textField.setMaximumSize(preferredSize);
        SwingUtilities.getWindowAncestor(textField).pack();
    }

    @Override
    public void onThemeChange(Theme theme)
    {
        applyTheme(theme);
    }

    private void toggleFontMono()
    {
        this.isUsingMonoFont = !isUsingMonoFont;
        if (isUsingMonoFont) {
            Font newFont = monoFont.deriveFont(gameStateTA.getFont().getSize() * 1f);
            gameStateTA.setFont(newFont);
        } else {
            Font newFont = defaultFont.deriveFont(gameStateTA.getFont().getSize() * 1f);
            gameStateTA.setFont(newFont);
        }
    }

    private void resetFontSize()
    {
        System.out.println("resetFontSize defaultFontSize=" + defaultFontSize);
        Font newFont = gameStateTA.getFont().deriveFont(defaultFontSize * 1f);
        gameStateTA.setFont(newFont);
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (e.isControlDown()) {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                increaseFontSize();
            } else {
                decreaseFontSize();
            }
        }
    }

    public JDialog openAboutDialog()
    {
        return new AboutDialog(this.getWidth());
    }

    private void openThemeDialog()
    {
        new ThemeDialog(this, this);
    }

    private void openCommandSelectionDialog()
    {
        new CommandSelectionDialog(this, this);
    }

    @SuppressWarnings("serial")
    private void createKeyBindings()
    {
        int ctrl = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        InputMap inputMap = commandTF.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = commandTF.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, ctrl), "openThemeDialog");
        actionMap.put("openThemeDialog", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openThemeDialog();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullScreen");
        actionMap.put("toggleFullScreen", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                toggleFullScreen();
            }
        });

        inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullScreen");
        actionMap.put("toggleFullScreen", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                toggleFullScreen();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, ctrl), "openCommandSelectionDialog");
        actionMap.put("openCommandSelectionDialog", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openCommandSelectionDialog();
            }
        });
    }

    private void toggleFullScreen()
    {
        if (!isFullScreen) {
            SwingUtilities.invokeLater(() -> {
                dispose();
                setUndecorated(true);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setVisible(true);
                isFullScreen = true;
                commandTF.requestFocusInWindow(); // Set focus to command text field
            });
        } else {
            SwingUtilities.invokeLater(() -> {
                dispose();
                setUndecorated(false);
                setExtendedState(JFrame.NORMAL);
                setSize(800, 600);
                setLocationRelativeTo(null);
                setVisible(true);
                isFullScreen = false;
                commandTF.requestFocusInWindow(); // Set focus to command text field
            });
        }
    }
    
    private JTextField buildCommandTF()
    {
        JTextField commandTF = new JTextField();
        commandTF.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String command = commandTF.getText();
                processCommand(command);
                commandTF.setText("");

                commandHistory.addFirst(command);
                if (commandHistory.size() > 100) {
                    commandHistory.removeLast();
                }
                commandIndex = 0;
            }
        });

        commandTF.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    clearTextField();
                } else if (e.isControlDown()) {
                    if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                        increaseFontSize();
                    } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                        decreaseFontSize();
                    } else if (e.getKeyCode() == KeyEvent.VK_0) {
                        resetFontSize();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (commandIndex < commandHistory.size()) {
                        commandTF.setText(commandHistory.get(commandIndex));
                        commandIndex++;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_G && e.isControlDown()) {
                    openCommandSelectionDialog();
                }
            }
        });
        return commandTF;
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new MainFrame();
            }
        });
    }
}