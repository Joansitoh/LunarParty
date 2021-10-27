package me.joansiitoh.lunarparty.commands.args;

import club.skilldevs.utils.ChatUtils;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PromoteArgument extends PartyArgument {

    public PromoteArgument() {
        super("promote", "lunarparty.promote", 2);
        setDescription("Promote player to owner. (Soon...)");
        setUsage("<player>");
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(Language.PREFIX.toString() + ChatUtils.translate("&cComing soon..."));
    }

}
