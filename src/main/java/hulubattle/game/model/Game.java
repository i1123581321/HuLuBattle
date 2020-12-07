package hulubattle.game.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import hulubattle.game.data.AbstractCharacterData;
import hulubattle.game.data.AbstractSkillData;
import hulubattle.game.data.DataSupplier;

public class Game {
    private Map<Integer, AbstractCharacter> characters = new HashMap<>();
    private DataSupplier<AbstractCharacterData> characterData;
    private DataSupplier<AbstractSkillData> skillData;
    private BiMap<Integer, Integer> map = HashBiMap.create();
    private GameState state = GameState.START;
    private Optional<GameDelegate> delegate = Optional.empty();
}
