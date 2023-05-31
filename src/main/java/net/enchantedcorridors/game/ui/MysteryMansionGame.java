package net.enchantedcorridors.game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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

public class MysteryMansionGame extends JFrame implements ActionListener, KeyListener, MouseWheelListener, ThemeChangeListener, Game
{
    private static final long serialVersionUID = 1L;

    private JTextArea gameStateTA;
    private JTextField commandTF;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;

    private LinkedList<String> commandHistory;
    private int commandIndex;

    private int orderCounter;
    private Font defaultFont;
    private Font monoFont;
    private Boolean isUsingMonoFont = false;
    private int defaultFontSize;

    private boolean isFullScreen;

    public MysteryMansionGame()
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

        commandTF = new JTextField();
        commandTF.addActionListener(this);
        commandTF.addKeyListener(this);
        add(commandTF, BorderLayout.SOUTH);

        Border inventoryBorder = BorderFactory.createTitledBorder("Inventory");
        inventoryScrollPane.setBorder(inventoryBorder);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        commandHistory = new LinkedList<>();
        commandIndex = 0;
        orderCounter = 1;

        defaultFont = gameStateTA.getFont();
        defaultFontSize = defaultFont.getSize();
        monoFont = new Font(Font.MONOSPACED, Font.PLAIN, defaultFont.getSize());

        appendToTextArea("Welcome to the Mystery Mansion! Enter your commands below.");

        applyTheme(new Theme("solar"));
        commandTF.requestFocusInWindow();
        commandTF.setText("help");

        gameStateTA.addMouseWheelListener(this);
        createKeyBindings();

        isFullScreen = false;
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
        inventoryTableModel.addRow(new Object[] { orderCounter++, name, description });
        inventoryTable.scrollRectToVisible(inventoryTable.getCellRect(inventoryTable.getRowCount() - 1, 0, true));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = commandTF.getText();
        processCommand(command);
        commandTF.setText("");

        commandHistory.addFirst(command);
        if (commandHistory.size() > 10) {
            commandHistory.removeLast();
        }
        commandIndex = 0;
    }

    public void processCommand(String command)
    {
        appendToTextArea("> " + command);

        // Implement game logic based on the command entered
        if (command.equalsIgnoreCase("help")) {
            appendToTextArea("Available commands:\n- go <direction>\n- examine <object>\n- inventory\n- about\n- quit\n- theme\n- red\n- dark\n- solar\n- mono");
        } else if (command.equalsIgnoreCase("go north")) {
            appendToTextArea("You head north...");
        } else if (command.equalsIgnoreCase("examine door")) {
            appendToTextArea("You closely examine the door, noting its intricate carvings.");
        } else if (command.equalsIgnoreCase("inventory")) {
            appendToTextArea("You have the following items in your inventory:");
            // Print the inventory table
            for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
                String order = inventoryTableModel.getValueAt(i, 0).toString();
                String name = inventoryTableModel.getValueAt(i, 1).toString();
                String description = inventoryTableModel.getValueAt(i, 2).toString();
                appendToTextArea(order + ". " + name + " - " + description);
            }
        } else if (command.equalsIgnoreCase("pickup item")) {
            String itemName = "Item " + orderCounter;
            String itemDescription = "Description of " + itemName;
            appendToInventory(itemName, itemDescription);
        } else if (command.equalsIgnoreCase("quit")) {
            appendToTextArea("Goodbye!");
            System.exit(0);
        } else if (command.equalsIgnoreCase("dark")) {
            applyTheme(new Theme("dark"));
        } else if (command.equalsIgnoreCase("red")) {
            applyTheme(new Theme("red"));
        } else if (command.equalsIgnoreCase("solar")) {
            applyTheme(new Theme("solar"));
        } else if (command.equalsIgnoreCase("mono")) {
            applyTheme(new Theme("mono"));
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

    @Override
    public void onThemeChange(Theme theme)
    {
        applyTheme(theme);
    }

    private void applyTheme(Theme theme)
    {
        String mode = theme.getName();
        if (mode.equalsIgnoreCase("dark")) {
            gameStateTA.setBackground(Color.DARK_GRAY);
            gameStateTA.setForeground(Color.WHITE);
            inventoryTable.setBackground(Color.DARK_GRAY);
            inventoryTable.setForeground(Color.WHITE);
        } else if (mode.equalsIgnoreCase("red")) {
            Color bg = new Color(57, 0, 0);
            Color fg = new Color(162, 63, 63);
            gameStateTA.setBackground(bg);
            gameStateTA.setForeground(fg);
            inventoryTable.setBackground(bg);
            inventoryTable.setForeground(fg);
        } else if (mode.equalsIgnoreCase("solar")) {
            gameStateTA.setBackground(new Color(0, 43, 54));
            gameStateTA.setForeground(new Color(131, 148, 150));
            inventoryTable.setBackground(new Color(0, 43, 54));
            inventoryTable.setForeground(new Color(131, 148, 150));
        } else if (mode.equalsIgnoreCase("mono")) {
            this.isUsingMonoFont = !isUsingMonoFont;
            if (isUsingMonoFont) {
                Font newFont = monoFont.deriveFont(gameStateTA.getFont().getSize() * 1f);
                gameStateTA.setFont(newFont);
            } else
                gameStateTA.setFont(defaultFont);
        } else {
            gameStateTA.setBackground(Color.WHITE);
            gameStateTA.setForeground(Color.BLACK);
            inventoryTable.setBackground(Color.WHITE);
            inventoryTable.setForeground(Color.BLACK);
            gameStateTA.setFont(defaultFont);
        }
    }

    private void increaseFontSize()
    {
        Font currentFont = gameStateTA.getFont();
        int currentSize = currentFont.getSize();
        Font newFont = currentFont.deriveFont(currentSize + 1.0f);
        gameStateTA.setFont(newFont);
    }

    private void decreaseFontSize()
    {
        Font currentFont = gameStateTA.getFont();
        int currentSize = currentFont.getSize();
        if (currentSize > 1) {
            Font newFont = currentFont.deriveFont(currentSize - 1.0f);
            gameStateTA.setFont(newFont);
        }
    }

    private void resetFontSize()
    {
        System.out.println("resetFontSize defaultFontSize=" + defaultFontSize);
        Font newFont = gameStateTA.getFont().deriveFont(defaultFontSize * 1f);
        gameStateTA.setFont(newFont);
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

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new MysteryMansionGame();
            }
        });
    }
}