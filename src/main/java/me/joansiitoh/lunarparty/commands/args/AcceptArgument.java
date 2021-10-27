package me.joansiitoh.lunarparty.commands.args;

import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AcceptArgument extends PartyArgument {

    public AcceptArgument() {
        super("accept", "lunarparty.accept", 2);
        setDescription("Accept party invitation.");
        setUsage("<player>");
    }

    @Override
    public void execute(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Language.PLAYER_NOT_FOUND.toString(true).replace("<party_target>", args[1]));
            return;
        }

        if (Party.getPlayerParty(player) != null) {
            player.sendMessage(Language.ALREADY_IN_PARTY.toString(true));
            return;
        }

        INSTANCE.getPartyManager().accept(player, target);
    }

}
