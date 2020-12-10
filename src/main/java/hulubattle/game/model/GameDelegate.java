package hulubattle.game.model;

/**
 * 游戏事件发生时的委托对象
 */
public interface GameDelegate {
    public void sendLog(CombatLog log);

    public void sendLog(CombatLog msgA, CombatLog msgB);
}
