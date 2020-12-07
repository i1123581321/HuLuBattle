package hulubattle.game.model;

import hulubattle.game.data.AbstractCharacterData;

/**
 * 无定制的角色信息
 */
public class SimpleCharacter extends AbstractCharacter {

    /**
     * @param data 角色数据
     * @param id   角色 ID
     * @param camp 角色阵营
     */
    public SimpleCharacter(AbstractCharacterData data, int id, Camp camp) {
        super(data, id, camp);
    }
}
