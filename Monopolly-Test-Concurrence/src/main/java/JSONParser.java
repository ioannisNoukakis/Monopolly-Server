import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by durza9390 on 07.12.2016.
 */
public class JSONParser {
    public static Object parse(String json, Class c) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        //JSON from file to Object
        return mapper.readValue(json, c);
    }

    public static String toJson(Object o)
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
