package eu.kennytv.serverlistmotd.core.command;

import eu.kennytv.serverlistmotd.core.ServerListMotdPlugin;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.util.SenderInfo;

public abstract class ServerListMotdCommand {
    protected final ServerListMotdPlugin plugin;
    protected final Settings settings;
    private final String name;

    protected ServerListMotdCommand(final ServerListMotdPlugin plugin, final Settings settings, final String name) {
        this.plugin = plugin;
        this.settings = settings;
        this.name = name;
    }

    public void execute(final SenderInfo sender, final String[] args) {
        if (checkPermission(sender, "command")) return;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("remove")) {
                if (checkPermission(sender, "remove")) return;
                settings.setToConfig("motd", "");
                settings.saveConfig();
                settings.reloadConfig();
                sender.sendMessage(plugin.getPrefix() + "§aSet an emtpy motd.");
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (checkPermission(sender, "reload")) return;
                settings.reloadConfig();
                sender.sendMessage(plugin.getPrefix() + "§aReloaded config and the motd icon");
            } else if (args[0].equalsIgnoreCase("update")) {
                if (checkPermission(sender, "update")) return;
                checkForUpdate(sender);
            } else if (args[0].equals("forceupdate")) {
                if (checkPermission(sender, "update")) return;
                sender.sendMessage(plugin.getPrefix() + "§c§lDownloading update...");

                if (plugin.installUpdate())
                    sender.sendMessage(plugin.getPrefix() + "§a§lThe update was successful! To prevent issues with tasks and to complete the update, you have to restart the proxy!");
                else
                    sender.sendMessage(plugin.getPrefix() + "§4Failed!");
            } else
                sendUsage(sender);
        /*} else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setmotd")) {
                if (checkPermission(sender, "setmotd")) return;
                if (!isNumeric(args[1])) {
                    sender.sendMessage(plugin.getPrefix() + "§cThe first argument has to be numeric!");
                    return;
                }

                final int line = Integer.parseInt(args[1]);
                if (line != 1 && line != 2) {
                    sender.sendMessage(plugin.getPrefix() + "§cThe first argument has to be a 1 or a 2!");
                    return;
                }

                final String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                final String oldMessage = settings.getRawConfigString("motd");
                final String newMessage;
                if (line == 1)
                    newMessage = oldMessage.contains("%NEWLINE%") ?
                            message + "%NEWLINE%" + oldMessage.split("%NEWLINE%", 2)[1] : message;
                else
                    newMessage = oldMessage.contains("%NEWLINE%") ?
                            oldMessage.split("%NEWLINE%", 2)[0] + "%NEWLINE%" + message : oldMessage + "%NEWLINE%" + message;

                settings.setToConfig("motd", newMessage);
                settings.saveConfig();
                settings.reloadConfig();
                sender.sendMessage(plugin.getPrefix() + "§aSet line " + line + " of the motd motd to §f" + getColoredString(message));
            } else
                sendUsage(sender);*/
        } else
            sendUsage(sender);
    }

    private void sendUsage(final SenderInfo sender) {
        sender.sendMessage("");
        sender.sendMessage("§8===========[ §e" + name + " §8| §eVersion: §e" + plugin.getVersion() + " §8]===========");
        sender.sendMessage("§6/motd reload §7(Reloads the config file and the server-icon)");
        sender.sendMessage("§6/motd update §7(Remotely downloads the newest version of the plugin onto your server)");
        sender.sendMessage("§9Created by: KennyTV");
        sender.sendMessage("§8===========[ §e" + name + " §8| §eVersion: §e" + plugin.getVersion() + " §8]===========");
        sender.sendMessage("");
    }

    private boolean checkPermission(final SenderInfo sender, final String permission) {
        if (!sender.hasPermission("serverlistmotd." + permission)) {
            sender.sendMessage(settings.getNoPermMessage());
            return true;
        }
        return false;
    }

    private boolean isNumeric(final String string) {
        try {
            Integer.parseInt(string);
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    protected abstract void checkForUpdate(SenderInfo sender);

    protected abstract String getColoredString(String message);
}
