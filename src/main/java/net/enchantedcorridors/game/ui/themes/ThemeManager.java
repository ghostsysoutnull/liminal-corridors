package net.enchantedcorridors.game.ui.themes;

import java.util.LinkedList;
import java.util.List;

public class ThemeManager
{
    private static final Theme defaultTheme = new Theme("default");

    private List<ThemeChangeListener> listerners = new LinkedList<>();

    private Theme current = defaultTheme;

    public static Theme getDefault()
    {
        return defaultTheme;
    }

    public void addOnChangeListener(ThemeChangeListener changeListener)
    {
        listerners.add(changeListener);
    }

    public void notifyChange(Theme theme)
    {
        this.current = theme;
        notifyChangeDontUpdateCurrent(theme);
    }

    public Theme getCurrent()
    {
        return current;
    }

    public void notifyChangeDontUpdateCurrent(Theme theme)
    {
        for (ThemeChangeListener tcl : listerners) {
            tcl.onThemeChange(theme);
        }
    }
}