package co.edu.eafit.json;

import co.edu.eafit.model.common.Transformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TransformerAdapter implements Transformer {
    @Override
    public <T> String toJson(T value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            return e.toString();
        }
    }
}
