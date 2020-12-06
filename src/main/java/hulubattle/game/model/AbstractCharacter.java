package hulubattle.game.model;

import java.util.Optional;

import hulubattle.game.data.AbstractCharacterData;

/**
 * 抽象的角色信息，用于游戏运行时（区分于静态的角色数据），可以重写方法以定制化
 */
public abstract class AbstractCharacter {
    /**
     * 函数接口，用于处理角色死亡时的操作
     */
    @FunctionalInterface
    public interface CharacterDeadHandler {
        /**
         * 角色死亡时（生命值降低到0）会调用该函数
         * @param id 角色的 id
         */
        public void handle(int id);
    }

    private final int id;
    private final int dataId;

    private int x = 0;
    private int y = 0;
    private int hp;
    private Camp camp;
    private Optional<CharacterDeadHandler> handler = Optional.empty();

    /**
     * 根据角色数据初始化
     * @param data 角色数据
     * @param id 角色的 id
     */
    protected AbstractCharacter(AbstractCharacterData data, int id, Camp camp) {
        this.id = id;
        this.dataId = data.getId();
        this.hp = data.getHp();
        this.camp = camp;
    }

    /**
     * @return the dataId
     */
    public int getDataId() {
        return dataId;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(CharacterDeadHandler handler) {
        this.handler = Optional.of(handler);
    }

    /**
     * 返回两个角色之间的距离
     * @param character 另一个角色
     * @return 曼哈顿距离
     */
    public int distance(AbstractCharacter character) {
        return Math.abs(character.x - x) + Math.abs(character.y - y);
    }

    /**
     * 将角色移动到指定位置
     * @param x 横坐标
     * @param y 纵坐标
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 角色受到伤害
     * @param damage 伤害值
     */
    public void hurt(int damage) {
        hp -= damage;
        if (hp <= 0) {
            handler.ifPresent(h -> h.handle(this.id));
        }
    }

    /**
     * 判断角色是否是敌方
     * @param character
     * @return
     */
    public boolean isHarm(AbstractCharacter character) {
        return character.camp != camp;
    }
}
