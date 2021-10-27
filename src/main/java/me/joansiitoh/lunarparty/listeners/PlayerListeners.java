package me.joansiitoh.lunarparty.listeners;

import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (Party.getPlayerParty(e.getPlayer()) != null) {
            Party party = Party.getPlayerParty(e.getPlayer());
            party.refresh();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (Party.getPlayerParty(e.getPlayer()) != null) {
            Party party = Party.getPlayerParty(e.getPlayer());
            party.refresh();
        }
    }

}
