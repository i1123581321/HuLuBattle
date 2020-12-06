package hulubattle.game.data;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 从 JSON 文件或字符串中读取对象数组，然后加载进内存
 */
public class JsonDataSupplier<T extends Data> implements DataSupplier<T> {
    private static final Gson gson = new Gson();
    private final Class<? extends T> typeClass;
    private Map<Integer, T> map = new HashMap<>();

    public JsonDataSupplier(Class<? extends T> typeClass, String string) {
        this.typeClass = typeClass;
        setDataSource(string);
    }

    public JsonDataSupplier(Class<? extends T> typeClass, Path path) throws IOException {
        this.typeClass = typeClass;
        setDataSource(path);
    }

    private void setDataSource(String string) {
        Type type = TypeToken.getParameterized(ArrayList.class, typeClass).getType();
        map = new HashMap<>();
        List<T> list = gson.fromJson(string, type);
        list.forEach(t -> map.put(t.getId(), t));
    }

    private void setDataSource(Path path) throws IOException {
        String string = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        setDataSource(string);
    }

    @Override
    public Optional<T> get(int id) {
        return Optional.ofNullable(map.get(id));
    }
}
