package hulubattle.game.data;

import java.util.Objects;

/**
 * 角色数据的抽象类，可以实现并重写 get 方法以定制化
 */
public abstract class AbstractCharacterData implements Data {
    private int id = 0;
    private String name = "";

    private int hp = 0;
    private int def = 0;
    private int mobility = 0;
    private int[] skillList;

    @Override
    public final int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the hp
     */
    public int getHp() {
        return hp;
    }

    /**
     * @return the mobility
     */
    public int getMobility() {
        return mobility;
    }

    /**
     * @return the def
     */
    public int getDef() {
        return def;
    }

    /**
     * @return the skillList
     */
    public int[] getSkillList() {
        return skillList;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hp, mobility);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractCharacterData)) {
            return false;
        }
        AbstractCharacterData other = (AbstractCharacterData) obj;
        return Objects.equals(other.id, this.id);
    }

    /**
     * 无参构造器
     */
    protected AbstractCharacterData() {

    }

    /**
     * @param id 角色数据 ID
     * @param name 角色姓名
     * @param hp 角色最大生命值
     * @param def 角色防御力
     * @param mobility 角色行动力
     * @param skillList 角色技能列表
     */
    protected AbstractCharacterData(int id, String name, int hp, int def, int mobility, int[] skillList) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.def = def;
        this.mobility = mobility;
        this.skillList = skillList;
    }
}
