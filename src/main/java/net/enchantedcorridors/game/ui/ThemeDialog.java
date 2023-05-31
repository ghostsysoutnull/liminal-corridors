package net.enchantedcorridors.game.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ThemeDialog extends JDialog
{
    private static final long serialVersionUID = 1L;

    private JList<String> themeList;

    private static ThemeManager themeManager = new ThemeManager();

    public ThemeDialog(Frame owner, ThemeChangeListener changeListener)
    {
        super(owner, "Select Theme", true);
        themeManager.addOnChangeListener(changeListener);

        String[] themes = { "default", "dark", "solar", "red" };
        themeList = new JList<>(themes);
        themeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        themeList.setSelectedValue(themeManager.getCurrent().getName(), true);

        themeList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                themeManager.notifyChangeDontUpdateCurrent(new Theme(themeList.getSelectedValue()));
            }
        });

        themeList.addKeyListener(new KeyAdapter()
        {
            Integer keyPressedIndex = null;
            Integer keyReleasedIndex = null;

            @Override
            public void keyPressed(KeyEvent e)
            {
                keyPressedIndex = themeList.getSelectedIndex();

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    themeManager.notifyChange(new Theme(themeList.getSelectedValue()));
                    dispose();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    themeManager.notifyChange(themeManager.getCurrent());
                    dispose();
                }

            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                keyReleasedIndex = themeList.getSelectedIndex();
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    System.err.println("keyReleased UP selectedIndex=" + themeList.getSelectedIndex());
                    if (keyPressedIndex == keyReleasedIndex) {
                        themeList.setSelectedIndex(themeList.getModel().getSize() - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    System.err.println("keyReleased DOWN selectedIndex=" + themeList.getSelectedIndex());
                    if (keyPressedIndex == keyReleasedIndex) {
                        themeList.setSelectedIndex(0);
                    }
                }
            }
        });

        JScrollPane themeScrollPane = new JScrollPane(themeList);

        setLayout(new BorderLayout());
        add(themeScrollPane, BorderLayout.CENTER);
        setSize(200, 150);
        setLocationRelativeTo(this);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });
        setVisible(true);
    }
}