package hulubattle.game.model;

import java.util.List;

public interface GameDelegate {
    public void gameDidStart(List<CombatLog> setupA, List<CombatLog> setupB);

    public void gameDidActSucceed(List<CombatLog> logs);

    public void gameDidActFail(CombatLog error);

    public void gameDidEnd(CombatLog msgA, CombatLog msgB);
}
