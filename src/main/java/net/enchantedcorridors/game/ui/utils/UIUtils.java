package net.enchantedcorridors.game.ui.utils;

import java.awt.Font;

public class UIUtils
{
    public static Font changeFontSize(Font f, float size)
    {
        return f.deriveFont(f.getSize() + size);
    }

    public static Font increaseFontSize(Font f)
    {
        return changeFontSize(f, 1F);
    }

    public static Font decreaseFontSize(Font f)
    {
        return changeFontSize(f, -1F);
    }
}
