package eu.kendall.simon.timetables;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DepartureSignTests {
    static private DepartureSign depSign;

    @BeforeAll
    static void setup() {
        depSign = DepartureSign.getDepartures();
    }

    @Test
    void departureSignHasDepartures() {
        assertTrue(depSign.deps.length > 0);
    }

    @Test
    void departureSignDeparturesAreInTheFuture() {
        assertTrue(depSign.deps[10].expected().isAfter(LocalDateTime.now()));
    }

    @Test
    void departureSignDeparturesHaveLineData() {
        assertTrue(depSign.deps[10].lineData().lineNumber() != 0);
    }

    @Test
    void departureSignOutputsFormattedOutput() {
        assertTrue(depSign.toHTML().contains("<body>"));
    }

    @Test
    void departureSignWorksByStop() {
        WebClient client = WebClient.create("http://localhost:8080/stop");

        String response1 = client.get()
                .uri("?stop=8403")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String response2 = client.get()
                .uri("?stop=1234")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(response1 + response2);

        assertNotEquals(response1, response2);
    }
}
