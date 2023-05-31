package net.enchantedcorridors.game.ui;

import javax.swing.JDialog;

public interface Game
{
    JDialog openAboutDialog();

    void processCommand(String command);
}