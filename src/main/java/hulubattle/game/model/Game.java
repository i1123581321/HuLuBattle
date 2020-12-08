package hulubattle.game.model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import hulubattle.game.data.AbstractCharacterData;
import hulubattle.game.data.AbstractSkillData;
import hulubattle.game.data.DataSupplier;
import hulubattle.game.data.JsonDataSupplier;
import hulubattle.game.data.SimpleCharacterData;
import hulubattle.game.data.SimpleSkillData;
import hulubattle.game.model.AbstractCharacter.CharacterHurtHandler;
import hulubattle.game.model.AbstractCharacter.CharacterMoveHandler;

/**
 * 代表游戏逻辑的类
 */
public class Game {
    public static final int MAP_SIZE = 10;
    public static final int SKILL_NUM = 4;
    public static final int CHARACTER_NUM = 4;
    public static final int MAX_CHARACTER = 10;

    private static Gson gson = new Gson();
    private Map<Integer, AbstractCharacter> characters = new HashMap<>();
    private DataSupplier<AbstractCharacterData> characterData;
    private DataSupplier<AbstractSkillData> skillData;
    private BiMap<Integer, Integer> map = HashBiMap.create();
    private GameState state = GameState.MOVE;
    private Optional<GameDelegate> delegate = Optional.empty();
    private Random random = new Random();
    private CharacterMoveHandler moveHandler = (src, x, y) -> {
        map.forcePut(src, x * MAP_SIZE + y);
        delegate.ifPresent(d -> d.gameDidActSucceed(CombatLog.move(src, x, y)));
    };
    private CharacterHurtHandler hurtHandler = (src, hp) -> delegate
            .ifPresent(d -> d.gameDidActSucceed(CombatLog.hurt(src, hp)));

    /**
     * 计算指定技能作用在指定目标上得到的伤害
     *
     * @param skill  技能数据
     * @param target 角色数据
     * @return 伤害值（正值为治疗负值为伤害）
     */
    public static int calcDamage(AbstractSkillData skill, AbstractCharacterData target) {
        if (skill.isHarm()) {
            return (skill.getAtk() - target.getDef()) * skill.getAtkNum() * -1;
        } else {
            return skill.getAtk() * skill.getAtkNum();
        }
    }

    /**
     * 无参构造器，直接读取配置好的 JSON 文件，如果文件不存在或读取错误则抛出异常
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    public Game() throws URISyntaxException, IOException {
        Path characterPath = Paths.get(getClass().getClassLoader().getResource("config/characters.json").toURI());
        Path skillPath = Paths.get(getClass().getClassLoader().getResource("config/skills.json").toURI());
        characterData = new JsonDataSupplier<>(SimpleCharacterData.class, characterPath);
        skillData = new JsonDataSupplier<>(SimpleSkillData.class, skillPath);
    }

    /**
     * 设置委托对象
     *
     * @param delegate 委托对象
     */
    public void setDelegate(GameDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate);
    }

    /**
     * 随机布置战场
     */
    public void setUpRandomly() {
        List<CombatLog> logList = new ArrayList<>();
        IntStream.range(0, MAX_CHARACTER).forEach(i -> {

            // 随机生成不重复的位置
            int position = random.nextInt(MAP_SIZE * MAP_SIZE);
            while (map.containsValue(position)) {
                position = random.nextInt(MAP_SIZE * MAP_SIZE);
            }
            int x = position / MAP_SIZE;
            int y = position % MAP_SIZE;

            // 确定阵营
            int camp = i / (MAX_CHARACTER / 2);

            // 随机获取数据
            AbstractCharacterData data = characterData.get(random.nextInt(CHARACTER_NUM)).get();

            // 初始化角色
            AbstractCharacter character = AbstractCharacter.getDefault(i, data, x, y, camp);
            character.setMoveHandler(moveHandler);
            character.setHurtHandler(hurtHandler);

            map.put(i, position);
            characters.put(i, character);

            logList.add(CombatLog.set(i, data.getId(), x, y, camp));
        });
        delegate.ifPresent(d -> d.gameDidSetUp(logList));
    }

    /**
     * 根据 JSON 配置文件设置起始战场，如果读取错误则随机设置
     */
    public void setUp() {

        Optional<String> str = Optional.empty();
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("config/init.json").toURI());
            str = Optional.ofNullable(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        } catch (URISyntaxException | IOException e) {
            setUpRandomly();
        }

        str.ifPresent(s -> {
            List<CombatLog> logList = new ArrayList<>();

            Type type = new TypeToken<List<Map<String, Integer>>>() {
            }.getType();
            List<Map<String, Integer>> init = gson.fromJson(s, type);

            IntStream.range(0, init.size()).forEach(i -> {
                Map<String, Integer> config = init.get(i);

                // 获取数据
                AbstractCharacterData data = characterData.get(config.get("id")).get();
                int x = config.get("x");
                int y = config.get("y");
                int camp = config.get("camp");

                // 初始化角色
                AbstractCharacter character = AbstractCharacter.getDefault(i, data, x, y, camp);
                character.setMoveHandler(moveHandler);
                character.setHurtHandler(hurtHandler);

                map.put(i, x * MAP_SIZE + y);
                characters.put(i, character);

                logList.add(CombatLog.set(i, data.getId(), x, y, camp));
            });
            delegate.ifPresent(d -> d.gameDidSetUp(logList));
        });
    }
}
