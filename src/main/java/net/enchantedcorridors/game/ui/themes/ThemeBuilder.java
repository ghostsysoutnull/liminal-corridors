package net.enchantedcorridors.game.ui.themes;

import java.awt.Color;

public class ThemeBuilder
{
    private static final ThemeBuilder instance = new ThemeBuilder();

    private ThemeBuilder()
    {
    }

    public static ThemeBuilder it()
    {
        return instance;
    }

    public Theme from(String name)
    {
        Color fg, bg;
        switch (name) {
        case "dark":
            bg = Color.DARK_GRAY;
            fg = Color.WHITE;
            break;
        case "red":
            bg = new Color(57, 0, 0);
            fg = new Color(162, 63, 63);
            break;
        case "solar":
            bg = new Color(0, 43, 54);
            fg = new Color(131, 148, 150);
            break;
        default:
            bg = Color.WHITE;
            fg = Color.BLACK;
            break;
        }
        Theme t = new Theme(name);
        t.setBackground(bg);
        t.setForeground(fg);
        return t;
    }
}
