package eu.fronts.unified.viz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Holds the properties of the vizualizer.
 */
public class VizProperties {

    /**
     * The key for the arduino count.
     */
    public static final String ARDUINO_COUNT = "arduino-count";

    /**
     * The key for the arduino size.
     */
    public static final String ARDUINO_SIZE = "arduino-size";

    /**
     * The key for the node position.
     */
    public static final String ARDUINO_POSITION = "arduino-position-";

    /**
     * The key for the node size.
     */
    public static final String NODE_SIZE = "node-size";

    /**
     * The key for the node position.
     */
    public static final String NODE_POSITION = "node-position-";

    /**
     * The key for the node key-label position.
     */
    public static final String NODE_KEYLABEL = "node-keylabel-";

    /**
     * The key for the node ignore flag.
     */
    public static final String NODE_IGNORE = "node-ignore-";

    /**
     * The key for the map image.
     */
    public static final String MAP_ENABLE = "map-enable";

    /**
     * The key for the map image.
     */
    public static final String MAP_FILE = "map-image";

    /**
     * The key for the testbed URN.
     */
    public static final String TESTBED_URN = "testbed-urn";

    /**
     * The key for the screen size.
     */
    public static final String SCREEN_SIZE = "screen-size";


    /**
     * Unique instance of this class.
     */
    private static VizProperties thisInstance;

    /**
     * The properties of the Vizualizer.
     */
    private final Properties properties;

    /**
     * The filename of the property file.
     */
    private String path;

    /**
     * Default constructor.
     */
    private VizProperties() {
        properties = new Properties();
    }


    /**
     * Get the unique instance of this class.
     *
     * @return the unique object of type VizProperties.
     */
    public static VizProperties getInstance() {
        synchronized (VizProperties.class) {
            if (thisInstance == null) {
                thisInstance = new VizProperties();
            }
        }

        return thisInstance;
    }

    /**
     * Set the path of the property file.
     *
     * @param filePath -- path of the file containing the properties.
     */
    public void setPath(final String filePath) {
        path = filePath;
    }

    /**
     * Load the properties from the file.
     *
     * @throws IOException in case an error occurred while reading the file.
     */
    public void load() throws IOException {
        properties.load(new FileInputStream(path));
    }

    /**
     * Write the properties to the file.
     *
     * @throws IOException in case an error occurred during the write to the file.
     */
    public void save() throws IOException {
        properties.store(new FileOutputStream(path), null);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * <code>null</code> if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     * @see java.util.Properties#setProperty
     * @see java.util.Properties#defaults
     */
    public String getProperty(final String key) {
        return properties.getProperty(key);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     * @see java.util.Properties#setProperty
     * @see java.util.Properties#defaults
     */
    public String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     * @see java.util.Properties#setProperty
     * @see java.util.Properties#defaults
     */
    public int getProperty(final String key, final int defaultValue) {
        return Integer.parseInt(getProperty(key, Integer.toString(defaultValue)));
    }

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     * @see java.util.Properties#setProperty
     * @see java.util.Properties#defaults
     */
    public boolean getProperty(final String key, final boolean defaultValue) {
        return Boolean.parseBoolean(getProperty(key, Boolean.toString(defaultValue)));
    }

    /**
     * Add a new property with the specified key and value.
     *
     * @param key   the hashtable key.
     * @param value the value.
     */
    public void setProperty(final String key, final String value) {
        properties.put(key, value);
    }

    /**
     * Add a new property with the specified key and value.
     *
     * @param key   the hashtable key.
     * @param value the value.
     */
    public void setProperty(final String key, final int value) {
        properties.put(key, Integer.toString(value));
    }

    /**
     * Add a new property with the specified key and value.
     *
     * @param key   the hashtable key.
     * @param value the value.
     */
    public void setProperty(final String key, final boolean value) {
        properties.put(key, Boolean.toString(value));
    }

}
