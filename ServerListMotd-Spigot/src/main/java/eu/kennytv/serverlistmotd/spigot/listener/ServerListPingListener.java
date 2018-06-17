package eu.kennytv.serverlistmotd.spigot.listener;

import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.io.File;

public final class ServerListPingListener implements Listener, IPingListener {
    private final Settings settings;
    private CachedServerIcon serverIcon;

    public ServerListPingListener(final Settings settings) {
        this.settings = settings;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void serverListPing(final ServerListPingEvent event) {
        event.setMotd(settings.getMotd().replace("%NEWLINE%", "\n"));

        if (serverIcon != null)
            event.setServerIcon(serverIcon);
    }

    @Override
    public boolean loadIcon() {
        try {
            serverIcon = Bukkit.loadServerIcon(ImageIO.read(new File("server-icon.png")));
        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
