package hulubattle.game.model;

/**
 * 战斗日志
 */
public final class CombatLog {
    public final String type;
    public final int src;
    public final int dest;
    public final int x;
    public final int y;
    public final int skill;
    public final String msg;

    /**
     * 获取 SET 类型的日志，将 src 角色设置在 x y 处，生命值设置为最大生命值
     *
     * @param src 角色 ID
     * @param x   横坐标
     * @param y   纵坐标
     * @return 日志对象
     */
    public static CombatLog set(int src, int x, int y) {
        return new CombatLog(LogType.SET.name(), src, -1, x, y, -1, "");
    }

    /**
     * 获取 MOVE 类型的日志，将 src 角色移动到 x y 处
     *
     * @param src 角色 ID
     * @param x   横坐标
     * @param y   纵坐标
     * @return 日志对象
     */
    public static CombatLog move(int src, int x, int y) {
        return new CombatLog(LogType.MOVE.name(), src, -1, x, y, -1, "");
    }

    /**
     * 获取 CAST 类型的日志，src 角色对 dest 角色释放 skill 技能
     *
     * @param src   源角色 ID
     * @param dest  目标角色 ID
     * @param skill 技能 ID
     * @return 日志对象
     */
    public static CombatLog cast(int src, int dest, int skill) {
        return new CombatLog(LogType.CAST.name(), src, dest, -1, -1, skill, "");
    }

    /**
     * 获取 DESTROY 类型的日志，将 src 角色移除战场
     *
     * @param src 角色 ID
     * @return 日志对象
     */
    public static CombatLog destroy(int src) {
        return new CombatLog(LogType.DESTROY.name(), src, -1, -1, -1, -1, "");
    }

    /**
     * 获取 ERROR 类型的日志
     *
     * @param msg 错误信息
     * @return 日志对象
     */
    public static CombatLog error(String msg) {
        return new CombatLog(LogType.ERROR.name(), -1, -1, -1, -1, -1, msg);
    }

    private CombatLog(String type, int src, int dest, int x, int y, int skill, String msg) {
        this.type = type;
        this.src = src;
        this.dest = dest;
        this.x = x;
        this.y = y;
        this.skill = skill;
        this.msg = msg;
    }
}
