package hulubattle.game.model;

import java.util.List;

/**
 * 游戏事件发生时的委托对象
 */
public interface GameDelegate {
    /**
     * 游戏开始时调用
     *
     * @param msgA 告知玩家 A 其阵营
     * @param msgB 告知玩家 B 其阵营
     */
    public void gameDidStart(CombatLog msgA, CombatLog msgB);

    /**
     * 游戏初始化后调用
     *
     * @param setup 一系列 SET 日志
     */
    public void gameDidSetUp(List<CombatLog> setup);

    /**
     * 游戏行动成功后调用
     *
     * @param log 成功运行的日志
     */
    public void gameDidActSucceed(CombatLog log);

    /**
     * 游戏行动失败后调用
     *
     * @param error 错误信息
     */
    public void gameDidActFail(CombatLog error);

    /**
     * 游戏结束时调用
     *
     * @param msgA 告知玩家 A 的信息
     * @param msgB 告知玩家 B 的信息
     */
    public void gameDidEnd(CombatLog msgA, CombatLog msgB);
}
