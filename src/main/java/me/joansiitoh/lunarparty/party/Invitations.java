package me.joansiitoh.lunarparty.party;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joansiitoh (DragonsTeam && SkillTeam)
 * Date: 01/10/2021 - 0:41.
 */
@Getter
public class Invitations {

    private static final List<Invitations> invitations = new ArrayList<>();

    public static Invitations getInvitation(Player player, Player target) {
        for (Invitations invitation : invitations) {
            if (invitation.getReceiver().equals(player)) {
                if (invitation.getSender().equals(target)) return invitation;
            }
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////

    private final Player sender, receiver;
    private final long createdLong;

    public Invitations(Player sender, Player receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.createdLong = System.currentTimeMillis();

        invitations.add(this);
    }

    public void remove() {
        invitations.remove(this);
    }

    public boolean hasExpired() {
        return this.createdLong + 30000 < System.currentTimeMillis();
    }

}
