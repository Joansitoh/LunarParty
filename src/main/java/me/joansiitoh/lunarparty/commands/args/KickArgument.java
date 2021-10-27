package me.joansiitoh.lunarparty.commands.args;

import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.events.PartyInviteEvent;
import me.joansiitoh.lunarparty.events.PartyKickEvent;
import me.joansiitoh.lunarparty.party.Party;
import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KickArgument extends PartyArgument {

    public KickArgument() {
        super("kick", "lunarparty.kick", 2);
        setDescription("Kick player from party.");
        setUsage("<player>");
    }

    @Override
    public void execute(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Language.PLAYER_NOT_FOUND.toString(true).replace("<party_target>", args[1]));
            return;
        }

        Party party = Party.getPlayerParty(player);
        if (party == null) {
            player.sendMessage(Language.NOT_IN_PARTY.toString(true).replace("<party_target>", target.getName()));
            return;
        }

        if (!party.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Language.ONLY_OWNER.toString(true));
            return;
        }


        Party targetParty = Party.getPlayerParty(target);
        if (targetParty == null || !targetParty.equals(party)) {
            player.sendMessage(Language.PLAYER_NOT_IN_YOUR_PARTY.toString(true).replace("<party_target>", target.getName()));
            return;
        }

        if (target.getUniqueId().equals(party.getOwner())) {
            return;
        }

        PartyKickEvent event = new PartyKickEvent(player, target, party);
        sLunar.INSTANCE.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        party.deleteMember(target.getUniqueId());

        target.sendMessage(Language.KICKED.toString(true));
        party.getPlayers().forEach(member -> member.sendMessage(Language.KICK.toString(true).replace("<party_target>", target.getName())));
        return;
    }

}
