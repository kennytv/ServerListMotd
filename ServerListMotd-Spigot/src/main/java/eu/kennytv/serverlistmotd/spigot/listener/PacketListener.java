package eu.kennytv.serverlistmotd.spigot.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.collect.Lists;
import eu.kennytv.serverlistmotd.core.Settings;
import eu.kennytv.serverlistmotd.core.listener.IPingListener;
import eu.kennytv.serverlistmotd.spigot.ServerListMotdSpigotBase;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;
import java.util.UUID;

public final class PacketListener implements IPingListener {
    private final String players = " §7%d§8/§7%d";
    private WrappedServerPing.CompressedImage image;

    public PacketListener(final ServerListMotdSpigotBase base, final Settings settings) {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(base, ListenerPriority.HIGH,
                PacketType.Status.Server.SERVER_INFO) {
            @Override
            public void onPacketSending(final PacketEvent event) {
                final WrappedServerPing ping = event.getPacket().getServerPings().read(0);
                if (settings.hasCustomPlayerCount()) {
                    ping.setVersionName(settings.showPlayerCount() ? settings.getPlayerCountMessage()
                            + String.format(players, ping.getPlayersOnline(), ping.getPlayersMaximum()) : settings.getPlayerCountMessage());
                    ping.setVersionProtocol(0);
                }

                ping.setMotD(settings.getMotd().replace("%NEWLINE%", "\n"));

                final List<WrappedGameProfile> players = Lists.newArrayList();
                for (final String string : settings.getPlayerCountHoverMessage().split("%NEWLINE%"))
                    players.add(new WrappedGameProfile(UUID.randomUUID(), string));
                ping.setPlayers(players);

                if (image != null)
                    ping.setFavicon(image);
            }
        });
    }

    @Override
    public boolean loadIcon() {
        try {
            image = WrappedServerPing.CompressedImage.fromPng(ImageIO.read(new File("server-icon.png")));
        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
