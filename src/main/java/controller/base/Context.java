package controller.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows for information to be shared between screens by specifying the class of the stored object.
 * <p>
 * Created by Joseph Billingsley on 01/12/2015.
 */
public class Context {

    private final Map<String, Object> tagModelMap = new HashMap<>();

    /**
     * Adds an object to the context. If an object with the same tag already exists it will not be added.
     *
     * @param tag   The tag of the model to add that uniquely identifies it.
     * @param model The object to add.
     * @return True if the object was successfully added, false otherwise.
     */
    public boolean add(String tag, Object model) {

        if (tagModelMap.containsKey(tag))
            return false;

        tagModelMap.put(tag, model);

        return true;
    }

    /**
     * Adds an object to the context replacing any object that exists with the same tag.
     *
     * @param tag   The tag of the model to add that uniquely identifies it.
     * @param model The object to add.
     */
    public void forceAdd(String tag, Object model) {
        tagModelMap.put(tag, model);
    }

    /**
     * Returns the object corresponding to the provided tag.
     *
     * @param tag The tag of the object.
     * @return The object.
     */
    public Object get(String tag) {
        return tagModelMap.get(tag);
    }

    /**
     * Returns true if an object exists in the context with the provided tag.
     *
     * @param tag The tag to check.
     * @return True if an object exists matching the tag. False otherwise.
     */
    public boolean has(String tag) {
        return tagModelMap.containsKey(tag);
    }
}
