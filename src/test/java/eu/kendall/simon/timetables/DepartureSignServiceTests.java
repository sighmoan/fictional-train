package eu.kendall.simon.timetables;

import eu.kendall.simon.timetables.services.DepartureSignService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DepartureSignTests {
    static private DepartureSignService serv;

    @BeforeAll
    static void setup() {
        serv = DepartureSignService.getDepartures("9192");
    }

    @Test
    void departureSignHasDepartures() {
        assertTrue(serv.getDepartureSign().getDeps().length > 0);
    }

    @Test
    void departureSignDeparturesAreInTheFuture() {
        assertTrue(serv.getDepartureSign().getDep(10).expected().isAfter(LocalDateTime.now()));
    }

    @Test
    void departureSignDeparturesHaveLineData() {
        assertTrue(serv.getDepartureSign().getDep(10).lineData().lineNumber() != 0);
    }

    @Test
    void departureSignOutputsFormattedOutput() {
        assertTrue(serv.toHTML().contains("<body>"));
    }
}
