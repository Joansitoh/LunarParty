package me.joansiitoh.lunarparty.commands;

import me.joansiitoh.lunarparty.sLunar;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 29/09/2021 - 17:36.
 */
public class AddCMD {

    public void execute(Player p, Player target) {
        if (!sLunar.leaderHash.containsKey(p)) {
            p.sendMessage("no tienes party");
            return;
        }

        List<Player> members = sLunar.leaderHash.get(p);
        members.add(target);
        sLunar.memberHash.put(target, p);
        p.sendMessage("jugador añadido");

        sLunar.updatePlayer(p, target);
    }

}
