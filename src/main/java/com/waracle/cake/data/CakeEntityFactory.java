package com.waracle.cake.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Component
public class CakeEntityFactory {

    private static final String CAKE_ENTITY_JSON_URL =
            "https://gist.githubusercontent.com/NormanLittle/db873d44bfdccb88f511276d6b9d1bfe/raw/25c1f8e58f2050b346eb8df039c722f27ae1b3df/cakes.json";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CakeEntityFactory() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = createObjectMapper();
    }

    private static ObjectMapper createObjectMapper() {
        /* Register customer deserializer to parse JSON response into
         * 'CakeEntity' instances.
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(CakeEntity.class, new CakeEntityDeserializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    public List<CakeEntity> createAll() throws JsonProcessingException {
        String response = restTemplate.getForObject(CAKE_ENTITY_JSON_URL, String.class);
        return objectMapper.readValue(response, new TypeReference<List<CakeEntity>>() {
        });
    }

    private static class CakeEntityDeserializer extends StdDeserializer<CakeEntity> {

        public CakeEntityDeserializer() {
            this(null);
        }

        public CakeEntityDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public CakeEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);

            CakeEntity cakeEntity = new CakeEntity();
            cakeEntity.setTitle(node.get("title").asText());
            cakeEntity.setDescription(node.get("desc").asText());
            cakeEntity.setImage(node.get("image").asText());

            return cakeEntity;
        }
    }
}
