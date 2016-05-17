import java.util.Map;

/**
 * Created by david on 17.05.2016.
 */
public interface MessageObserver {
    public abstract void updateMO(Map<String, Object> jsonObject);
}
