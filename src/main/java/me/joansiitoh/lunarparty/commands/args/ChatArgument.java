package me.joansiitoh.lunarparty.commands.args;

import club.skilldevs.utils.ChatUtils;
import me.joansiitoh.lunarparty.Language;
import me.joansiitoh.lunarparty.commands.PartyArgument;
import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.entity.Player;

public class ChatArgument extends PartyArgument {

    public ChatArgument() {
        super("chat", "lunarparty.chat", 1);
        setDescription("Toggle party chat.");
    }

    @Override
    public void execute(Player player, String[] args) {
        INSTANCE.getPartyManager().toggleChat(player);
    }

}
