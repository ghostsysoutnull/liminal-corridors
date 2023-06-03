package net.enchantedcorridors.game.ui;

import net.enchantedcorridors.game.ui.themes.Theme;

public interface HasTheme
{
    void applyThemeToComponent(Theme t);

    void increaseFontSize();

    void decreaseFontSize();
}
