package moex.app.answers;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;

@Component
public class LastPriceAnswer {

    public static String createAnswer(String ticker, Iterator<JsonNode> jsonNodeIterator, int lastPriceIndex) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JsonFactory jfactory = new JsonFactory();
        JsonGenerator jGenerator = jfactory.createGenerator(stream, JsonEncoding.UTF8);

        jGenerator.writeStartObject();
        jGenerator.writeFieldName("resp");
        jGenerator.writeStartObject();
        jGenerator.writeFieldName("data");
        jGenerator.writeStartArray();
        while(jsonNodeIterator.hasNext()) {
            JsonNode node = jsonNodeIterator.next();
            jGenerator.writeStartObject();
            jGenerator.writeStringField("ticker", ticker);
            jGenerator.writeStringField("BOARD", node.path(1).asText());
            jGenerator.writeNumberField("lastPrice", node.path(lastPriceIndex).asDouble());
            jGenerator.writeEndObject();
        }
        jGenerator.writeEndArray();
        jGenerator.writeEndObject();
        jGenerator.writeNumberField("responseTime", new Date().getTime());
        jGenerator.writeEndObject();
        jGenerator.close();

        return stream.toString(StandardCharsets.UTF_8);
    }
}
