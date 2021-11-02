package me.joansiitoh.lunarparty.listeners;

import club.skilldevs.utils.ChatUtils;
import club.skilldevs.utils.FileConfig;
import me.joansiitoh.lunarparty.party.Party;
import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (Party.getPlayerParty(e.getPlayer()) != null) {
            Party party = Party.getPlayerParty(e.getPlayer());
            party.refresh();

            if (party.getPlayers().isEmpty()) {
                if (sLunar.INSTANCE.getSettingsFile().getBoolean("DELETE-WHEN-ALL-OFFLINE")) party.disband();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Party.getPlayerParty(e.getPlayer()) != null) {
            Party party = Party.getPlayerParty(e.getPlayer());
            party.refresh();
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        FileConfig config = sLunar.INSTANCE.getSettingsFile();
        if (Party.getPlayerParty(e.getPlayer()) != null) {
            boolean symbol = e.getMessage().startsWith(config.getString("PARTY-CHAT-SYMBOL"));
            if (sLunar.INSTANCE.getPartyManager().isChatting(e.getPlayer()) || symbol) {
                e.setCancelled(true);
                if (symbol) e.setMessage(e.getMessage().substring(1));

                Party party = Party.getPlayerParty(e.getPlayer());
                String format = ChatUtils.translate(config.getString("PARTY-CHAT-FORMAT")
                        .replace("<party_player>", e.getPlayer().getName())
                ).replace("<party_message>", e.getMessage());

                party.getPlayers().forEach(player -> player.sendMessage(format));
            }
        }
    }

}
