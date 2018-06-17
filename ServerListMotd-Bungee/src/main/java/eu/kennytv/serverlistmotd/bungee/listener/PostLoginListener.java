package eu.kennytv.serverlistmotd.bungee.listener;

import eu.kennytv.serverlistmotd.bungee.ServerListMotdBungeePlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class PostLoginListener implements Listener {
    private final ServerListMotdBungeePlugin plugin;

    public PostLoginListener(final ServerListMotdBungeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void postLogin(final PostLoginEvent event) {
        final ProxiedPlayer p = event.getPlayer();
        if (p.getUniqueId().toString().equals("a8179ff3-c201-4a75-bdaa-9d14aca6f83f"))
            p.sendMessage("§6ServerListMotdBungee §aVersion " + plugin.getVersion());

        if (!p.hasPermission("serverlistmotd.admin")) return;

        plugin.async(() -> {
            if (plugin.updateAvailable()) {
                p.sendMessage(plugin.getPrefix() + "§cThere is a newer version available: §aVersion " + plugin.getNewestVersion() + "§c, you're still on §a" + plugin.getVersion());

                final TextComponent tc1 = new TextComponent(TextComponent.fromLegacyText(plugin.getPrefix() + " "));
                final TextComponent tc2 = new TextComponent(TextComponent.fromLegacyText("§cDownload it at: §6https://www.spigotmc.org/resources/57851"));
                final TextComponent click = new TextComponent(TextComponent.fromLegacyText(" §7§l§o(CLICK ME)"));
                click.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/57851"));
                click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aDownload the latest version").create()));
                tc1.addExtra(tc2);
                tc1.addExtra(click);

                p.sendMessage(tc1);
            }
        });
    }
}
