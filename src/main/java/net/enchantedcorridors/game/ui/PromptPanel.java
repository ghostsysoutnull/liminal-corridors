package net.enchantedcorridors.game.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.enchantedcorridors.game.ui.themes.Theme;

public class PromptPanel extends JPanel implements HasTheme
{
    private static final long serialVersionUID = 1L;

    private LinkedList<String> commandHistory = new LinkedList<>();
    private int commandIndex;

    private JTextField commandTF = null;

    private Game game;

    public PromptPanel(Game game)
    {
        this.game = game;
        setLayout(new BorderLayout());

        this.commandTF = new JTextField("Help");
        add(commandTF, BorderLayout.CENTER);

        registerEvents(game, commandTF);
    }

    private void registerEvents(Game game, JTextField commandTF)
    {
        commandTF.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String command = commandTF.getText();
                game.getCommandProcessor().processCommand(command);
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
                    commandTF.setText("");
                } else if (e.isControlDown()) {
                    if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                        game.increaseFontSize();
                    } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                        game.increaseFontSize();
                    } else if (e.getKeyCode() == KeyEvent.VK_0) {
                        game.resetFontSize();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (commandIndex < commandHistory.size()) {
                        commandTF.setText(commandHistory.get(commandIndex));
                        commandIndex++;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_G && e.isControlDown()) {
                    game.openCommandSelectionDialog();
                }
            }
        });
    }

    @SuppressWarnings("serial")
    public void createKeyBindings()
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
                game.openThemeDialog();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullScreen");
        actionMap.put("toggleFullScreen", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.toggleFullScreen();
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
                game.toggleFullScreen();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, ctrl), "openCommandSelectionDialog");
        actionMap.put("openCommandSelectionDialog", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                game.openCommandSelectionDialog();
            }
        });
    }

    @Override
    public void applyThemeToComponent(Theme t)
    {
        commandTF.setBackground(t.getBackground());
        commandTF.setForeground(t.getForeground());
    }

    void requestFocusToPrompt()
    {
        commandTF.requestFocusInWindow();
    }

    @Override
    public void increaseFontSize()
    {
        commandTF.setFont(UIUtils.increaseFontSize(commandTF.getFont()));
        updateTFSize(commandTF);
    }

    @Override
    public void decreaseFontSize()
    {
        commandTF.setFont(UIUtils.decreaseFontSize(commandTF.getFont()));
        updateTFSize(commandTF);
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
}