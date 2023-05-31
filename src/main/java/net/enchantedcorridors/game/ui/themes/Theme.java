package net.enchantedcorridors.game.ui.themes;

import java.awt.Color;

public class Theme
{
    private String name;
    private Color background;
    private Color foreground;

    Theme(String name)
    {
        this.name = name;
    }

    public Color getBackground()
    {
        return background;
    }

    public void setBackground(Color background)
    {
        this.background = background;
    }

    public Color getForeground()
    {
        return foreground;
    }

    public void setForeground(Color foreground)
    {
        this.foreground = foreground;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}