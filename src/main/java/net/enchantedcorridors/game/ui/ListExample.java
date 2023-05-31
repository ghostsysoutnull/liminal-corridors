package net.enchantedcorridors.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class ListExample extends JFrame
{
    private static final long serialVersionUID = 1L;
    
    private JList<String> list;
    private String[] items = { "Item 1", "Item 2", "Item 3", "Item 4" };

    public ListExample()
    {
        setTitle("List Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        list = new JList<>(items);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addKeyListener(new ListKeyListener());

        add(new JScrollPane(list), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private class ListKeyListener extends KeyAdapter
    {
        Integer keyPressedIndex = null;
        Integer keyReleasedIndex = null;

        @Override
        public void keyReleased(KeyEvent e)
        {
            keyReleasedIndex = list.getSelectedIndex();
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                System.err.println("keyReleased UP selectedIndex=" + list.getSelectedIndex());
                if (keyPressedIndex == keyReleasedIndex) {
                    list.setSelectedIndex(items.length - 1);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                System.err.println("keyReleased DOWN selectedIndex=" + list.getSelectedIndex());
                if (keyPressedIndex == keyReleasedIndex) {
                    list.setSelectedIndex(0);
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            keyPressedIndex = list.getSelectedIndex();
//            if (e.getKeyCode() == KeyEvent.VK_UP) {
//                System.err.println("keyPressed UP selectedIndex=" + list.getSelectedIndex());
//            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//                System.err.println("keyPressed DOWN selectedIndex=" + list.getSelectedIndex());
//            }
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            ListExample example = new ListExample();
            example.setVisible(true);
        });
    }
}