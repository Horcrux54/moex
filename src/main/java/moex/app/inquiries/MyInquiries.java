package moex.app.inquiries;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import moex.app.answers.HistoryAnswer;
import moex.app.answers.LastPriceAnswer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;

@Component
public class MyInquiries {
    private final RestTemplate restTemplate;

    public MyInquiries(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String lastPrice(String type, String ticker,int lastPriceIndex) throws IOException {
        String urlCheckPrice = "https://iss.moex.com/iss/engines/stock/markets/" + type + "/securities.json?securities=";

        String stringAnswer = restTemplate.getForEntity(urlCheckPrice + ticker, String.class).getBody();

        Iterator<JsonNode> jsonNodeIterator = new ObjectMapper().readTree(stringAnswer).findValue("marketdata").findValue("data").iterator();
        return LastPriceAnswer.createAnswer(ticker, jsonNodeIterator, lastPriceIndex);
    }

    @Cacheable("history")
    public String history(String type, String ticker, String from, String till) throws IOException {
        String urlCheckHistory = "https://iss.moex.com/iss/history/engines/stock/markets/" +
                type + "/securities/" + ticker + "/candleborders.json?from=" + from + "&till=" + till;
        String stringAnswer = restTemplate.getForEntity(urlCheckHistory, String.class).getBody();

        Iterator<JsonNode> jsonNodeIterator = new ObjectMapper().readTree(stringAnswer).findValue("history").findValue("data").iterator();
        return HistoryAnswer.createAnswer(ticker, jsonNodeIterator);
    }

}
