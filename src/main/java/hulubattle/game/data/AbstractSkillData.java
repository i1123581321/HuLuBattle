package hulubattle.game.data;

import java.util.Objects;

/**
 * 技能数据的抽象类，可以实现并重写 get 方法以定制化
 */
public abstract class AbstractSkillData implements Data {
    private int id = 0;
    private String name = "";

    private boolean harm = true;
    private int range = 0;
    private int atk = 0;
    private int atkNum = 0;

    @Override
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the harm
     */
    public boolean isHarm() {
        return harm;
    }

    /**
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * @return the atk
     */
    public int getAtk() {
        return atk;
    }

    /**
     * @return the atkNum
     */
    public int getAtkNum() {
        return atkNum;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(id, name, harm, range, atk, atkNum);
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
        if (!(obj instanceof AbstractSkillData)) {
            return false;
        }
        AbstractSkillData other = (AbstractSkillData) obj;
        return Objects.equals(other.id, this.id);
    }

    /**
     * @param id
     * @param name
     * @param harm
     * @param range
     * @param atk
     * @param atkNum
     */
    protected AbstractSkillData(int id, String name, boolean harm, int range, int atk, int atkNum) {
        this.id = id;
        this.name = name;
        this.harm = harm;
        this.range = range;
        this.atk = atk;
        this.atkNum = atkNum;
    }

    /**
     * 无参构造器
     */
    protected AbstractSkillData() {
    }
}
