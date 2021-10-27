package me.joansiitoh.lunarparty.commands.args;

import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InviteArgument extends PartyArgument {

    public InviteArgument() {
        super("invite", "lunarparty.invite", 2);
        setDescription("Invite player to your party.");
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
            player.sendMessage(Language.NOT_IN_PARTY.toString(true));
            return;
        }

        if (!party.getOwner().equals(player.getUniqueId())) {
            player.sendMessage(Language.ONLY_OWNER.toString(true));
            return;
        }

        INSTANCE.getPartyManager().invite(party, target);
    }

}
