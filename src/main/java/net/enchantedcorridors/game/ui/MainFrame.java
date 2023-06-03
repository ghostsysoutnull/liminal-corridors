package net.enchantedcorridors.game.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import net.enchantedcorridors.game.ui.dialogs.AboutDialog;
import net.enchantedcorridors.game.ui.dialogs.CommandSelectionDialog;
import net.enchantedcorridors.game.ui.dialogs.ThemeDialog;
import net.enchantedcorridors.game.ui.panels.PromptPanel;
import net.enchantedcorridors.game.ui.themes.HasTheme;
import net.enchantedcorridors.game.ui.themes.Theme;
import net.enchantedcorridors.game.ui.themes.ThemeBuilder;
import net.enchantedcorridors.game.ui.themes.ThemeChangeListener;

public class MainFrame extends JFrame implements MouseWheelListener, ThemeChangeListener, Game, HasTheme
{
    private static final long serialVersionUID = 1L;

    private JTextArea gameStateTA;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;

    private int itemIdx;

    private Font defaultFont;
    private Font monoFont;
    private Boolean isUsingMonoFont = false;
    private int defaultFontSize;

    private boolean isFullScreen;

    private CommandProcessor commandProcessor;

    private PromptPanel promptPanel;
    
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
        
        defaultFontSize = gameStateTA.getFont().getSize();

        inventoryTableModel = new DefaultTableModel(new Object[] { "Order", "Name", "Description" }, 0);
        inventoryTable = new JTable(inventoryTableModel);
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textScrollPane, inventoryScrollPane);
        splitPane.setResizeWeight(0.8);
        splitPane.setDividerLocation(550);
        add(splitPane, BorderLayout.CENTER);

        this.promptPanel = new PromptPanel(this);
        add(promptPanel, BorderLayout.SOUTH);

        Border inventoryBorder = BorderFactory.createTitledBorder("Inventory");
        inventoryScrollPane.setBorder(inventoryBorder);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        itemIdx = 1;

        defaultFont = gameStateTA.getFont();
        monoFont = new Font(Font.MONOSPACED, Font.PLAIN, defaultFont.getSize());

        appendToTextArea("Welcome to the Mystery Mansion! Enter your commands below.");

        applyTheme(ThemeBuilder.it().from("solar"));
        promptPanel.requestFocusToPrompt();
        // commandTF.requestFocusInWindow();

        gameStateTA.addMouseWheelListener(this);
        this.promptPanel.createKeyBindings();
//        createKeyBindings();

        isFullScreen = false;

        this.commandProcessor = new CommandProcessor(this);
    }

    @Override
    public CommandProcessor getCommandProcessor()
    {
        return commandProcessor;
    }

    public void clearTextArea()
    {
        gameStateTA.setText("");
    }

    public void appendToTextArea(String text)
    {
        gameStateTA.append(text + "\n");
        gameStateTA.setCaretPosition(gameStateTA.getDocument().getLength());
    }

    public void appendToInventory(String name, String description)
    {
        inventoryTableModel.addRow(new Object[] { itemIdx++, name, description });
        inventoryTable.scrollRectToVisible(inventoryTable.getCellRect(inventoryTable.getRowCount() - 1, 0, true));
    }

    public void applyTheme(Theme t)
    {
        applyThemeToComponent(gameStateTA, t);
        applyThemeToComponent(inventoryTable, t);
        promptPanel.applyThemeToComponent(t);
    }

    private static void applyThemeToComponent(Component c, Theme t)
    {
        c.setBackground(t.getBackground());
        c.setForeground(t.getForeground());
    }

    public void increaseFontSize()
    {
        Font currentFont = gameStateTA.getFont();
        int currentSize = currentFont.getSize();
        Font newFont = currentFont.deriveFont(currentSize + 1.0f);
        gameStateTA.setFont(newFont);

        promptPanel.increaseFontSize();
    }

    public void decreaseFontSize()
    {
        Font currentFont = gameStateTA.getFont();
        int currentSize = currentFont.getSize();
        if (currentSize > 1) {
            Font newFont = currentFont.deriveFont(currentSize - 1.0f);
            gameStateTA.setFont(newFont);
            promptPanel.decreaseFontSize();
        }
    }

    @Override
    public void onThemeChange(Theme theme)
    {
        applyTheme(theme);
    }

    public void toggleFontMono()
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

    public void resetFontSize()
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

    public void openThemeDialog()
    {
        new ThemeDialog(this, this);
    }

    public void openCommandSelectionDialog()
    {
        new CommandSelectionDialog(this, this);
    }

    public void toggleFullScreen()
    {
        if (!isFullScreen) {
            SwingUtilities.invokeLater(() -> {
                dispose();
                setUndecorated(true);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setVisible(true);
                isFullScreen = true;
                promptPanel.requestFocusToPrompt();
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
                promptPanel.requestFocusToPrompt();
            });
        }
    }

    @Override
    public void applyThemeToComponent(Theme t)
    {
        // TODO Auto-generated method stub
    }
}