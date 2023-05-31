package net.enchantedcorridors.game.ui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class CommandSelectionDialog
{
    public CommandSelectionDialog(JFrame f, Game game)
    {
        String[] commands = { "help", "inventory", "about", "quit" };
        JList<String> commandList = new JList<>(commands);
        commandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // commandList.setSelectedIndex(-1);
        commandList.setSelectedValue("inventory", true);

        JScrollPane commandScrollPane = new JScrollPane(commandList);

        JDialog commandDialog = new JDialog(f, "Command Selection", true);
        commandDialog.setLayout(new BorderLayout());
        commandDialog.add(commandScrollPane, BorderLayout.CENTER);
        commandDialog.setSize(200, 150);
        commandDialog.setLocationRelativeTo(f);
        commandDialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                commandDialog.dispose();
            }
        });

        commandList.addKeyListener(new KeyAdapter()
        {
            private Integer keyPressedIndex = null;
            private Integer keyReleasedIndex = null;

            @Override
            public void keyPressed(KeyEvent e)
            {
                keyPressedIndex = commandList.getSelectedIndex();

                JDialog openAboutDialog = null;
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedIndex = commandList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        String selectedCommand = commands[selectedIndex];
                        System.out.println("openCommandSelectionDialog [" + selectedCommand + "]");
                        if (selectedCommand.equalsIgnoreCase("about")) {
                            System.out.println("openCommandSelectionDialog 1");
                            openAboutDialog = game.openAboutDialog();
                        } else {
                            System.out.println("openCommandSelectionDialog 2");
                            game.processCommand(selectedCommand);
                        }
                        commandDialog.dispose();
                        if (openAboutDialog != null)
                            openAboutDialog.requestFocus();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    commandDialog.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                keyReleasedIndex = commandList.getSelectedIndex();
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    System.err.println("keyReleased UP selectedIndex=" + commandList.getSelectedIndex());
                    if (keyPressedIndex == keyReleasedIndex) {
                        commandList.setSelectedIndex(commandList.getModel().getSize() - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.err.println("keyReleased DOWN selectedIndex=" + commandList.getSelectedIndex());
                    if (keyPressedIndex == keyReleasedIndex) {
                        commandList.setSelectedIndex(0);
                    }
                }
            }
        });
        commandDialog.setVisible(true);
    }
}