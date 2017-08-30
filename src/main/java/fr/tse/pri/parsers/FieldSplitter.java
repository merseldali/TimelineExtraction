package fr.tse.pri.parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Fieldsplitter with configurable fields
 */
public class FieldSplitter {

    private static Logger LOG = new Logger();

    /**
     * Test method
     * 
     * @param args
     *            not used
     */
    public static final void main(String[] args) {

        String[] fields = new String[] { "from:", "till:", "text:" };
        // regular test case
        printFieldValues(fields,
                "name ching chang field computer engineering grade 9.98");
        // missing field
        printFieldValues(fields,
                "ching chang field computer engineering grade 9.98");
        // duplicate field
        printFieldValues(fields,
                "name ching chang field computer engineering name johnny bravo grade 9.98");
    }

    /**
     * Gets the values of the fields from the input. If a field is not found the
     * value is null. If a field occurs multiple times the last value is used.
     * 
     * @param fields
     *            the known fields
     * @param input
     *            the input containing consecutive fields and their values
     *            separated by whitespace
     * @return a Map containing all known fields and their values
     */
    public static Map<String, String> getFieldValues(String[] fields,
            String input) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        for (String field : fields) {
            fieldValues.put(field, null);
        }
        String[] words = input.split(":");
        LOG.debug("words:" + Arrays.toString(words));
        String field = null;
        int start = 0;
        for (String word : words) {
            if (fieldValues.containsKey(word)) {
            	System.out.println("word found");
                int end = input.indexOf(word, start);
                if (end < 0) {
                    throw new RuntimeException(String.format(
                            "Error: '%s' not found after position %s", word,
                            end));
                }
                if (field != null) {
                    fieldValues.put(field, input.substring(start, end));
                }
                field = word;
                start = end + word.length();
            }
        }
        if (field != null) {
            fieldValues.put(field, input.substring(start, input.length()));
        }
        return fieldValues;
    }

    public static void printFieldValues(String[] fields, String input) {
        for (Entry<String, String> field : getFieldValues(fields, input)
                .entrySet()) {
            LOG.debug(String.format("%s=%s", field.getKey(), field.getValue()));
        }
    }

    /** A simple logger */
    private static class Logger {
        void debug(String message) {
            System.out.println(message);
        }
    }
}