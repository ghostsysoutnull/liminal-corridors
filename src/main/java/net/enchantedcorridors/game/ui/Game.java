package net.enchantedcorridors.game.ui;

import javax.swing.JDialog;

import net.enchantedcorridors.game.ui.themes.Theme;

public interface Game 
{
    JDialog openAboutDialog();
    void clearTextArea();
    void resetFontSize();
    void openCommandSelectionDialog();
    void appendToTextArea(String text);
    void applyTheme(Theme theme);
    void toggleFontMono();
    void openThemeDialog();
    CommandProcessor getCommandProcessor();
    void increaseFontSize();
    void toggleFullScreen();
}