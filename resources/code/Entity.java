
import java.util.List;
import java.util.Set;

public class Entity {
    private final String name;
    private final String description;
//    public Set<Location> locationSet;
//    public List<Location> paths;

    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
