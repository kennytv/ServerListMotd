package eu.kennytv.serverlistmotd.bungee.listener;

import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public final class ProxyPingListener implements Listener, IPingListener {
    private final Settings settings;
    private Favicon favicon;

    public ProxyPingListener(final Settings settings) {
        this.settings = settings;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void proxyPing(final ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();
        if (settings.hasCustomPlayerCount())
            ping.setVersion(new ServerPing.Protocol(settings.getPlayerCountMessage(), 1));
        ping.setDescription(settings.getMotd().replace("%NEWLINE%", "\n"));
        ping.setPlayers(new ServerPing.Players(0, 0, new ServerPing.PlayerInfo[]{
                new ServerPing.PlayerInfo(settings.getPlayerCountHoverMessage().replace("%NEWLINE%", "\n"), "")
        }));

        if (favicon != null)
            ping.setFavicon(favicon);
    }

    @Override
    public boolean loadIcon() {
        try {
            favicon = Favicon.create(ImageIO.read(new File("server-icon.png")));
        } catch (final IOException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
