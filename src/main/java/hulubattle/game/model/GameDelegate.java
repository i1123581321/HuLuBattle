package hulubattle.game.model;

import java.util.List;

public interface GameDelegate {
    public void gameDidStart(CombatLog msgA, CombatLog msgB);

    public void gameDidSetUp(List<CombatLog> setup);

    public void gameDidActSucceed(CombatLog log);

    public void gameDidActFail(CombatLog error);

    public void gameDidEnd(CombatLog msgA, CombatLog msgB);
}
