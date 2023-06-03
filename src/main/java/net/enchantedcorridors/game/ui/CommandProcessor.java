package net.enchantedcorridors.game.ui;

import net.enchantedcorridors.game.ui.themes.ThemeBuilder;

public class CommandProcessor
{
    private Game game;

    public CommandProcessor(Game game)
    {
        this.game = game;
    }

    public void processCommand(String command)
    {
        game.appendToTextArea("> " + command);

        if (command.equalsIgnoreCase("help")) {
            game.appendToTextArea("Available commands:\n- go <direction>\n- examine <object>\n- inventory\n- about\n- quit\n- theme\n- red\n- dark\n- solar\n- mono");
        } else if (command.equalsIgnoreCase("go north")) {
            game.appendToTextArea("You head north...");
        } else if (command.equalsIgnoreCase("examine door")) {
            game.appendToTextArea("You closely examine the door, noting its intricate carvings.");
        } /*
           * else if (command.equalsIgnoreCase("inventory")) {
           * game.appendToTextArea("You have the following items in your inventory:"); for
           * (int i = 0; i < inventoryTableModel.getRowCount(); i++) { String order =
           * inventoryTableModel.getValueAt(i, 0).toString(); String name =
           * inventoryTableModel.getValueAt(i, 1).toString(); String description =
           * inventoryTableModel.getValueAt(i, 2).toString(); game.appendToTextArea(order
           * + ". " + name + " - " + description); } } else if
           * (command.equalsIgnoreCase("pickup item")) { String itemName = "Item " +
           * itemIdx; String itemDescription = "Description of " + itemName;
           * appendToInventory(itemName, itemDescription); }
           */ else if (command.equalsIgnoreCase("quit")) {
            game.appendToTextArea("Goodbye!");
            System.exit(0);
        } else if (command.equalsIgnoreCase("dark")) {
            game.applyTheme(ThemeBuilder.it().from("dark"));
        } else if (command.equalsIgnoreCase("red")) {
            game.applyTheme(ThemeBuilder.it().from("red"));
        } else if (command.equalsIgnoreCase("solar")) {
            game.applyTheme(ThemeBuilder.it().from("solar"));
        } else if (command.equalsIgnoreCase("mono")) {
            game.toggleFontMono();
        } else if (command.equalsIgnoreCase("theme")) {
            game.openThemeDialog();
        } else if (command.equalsIgnoreCase("clear")) {
            game.clearTextArea();
        } else if (command.equalsIgnoreCase("about")) {
            game.openAboutDialog();
        } else {
            game.appendToTextArea("Invalid command. Type 'help' for a list of available commands.");
        }
    }
}
