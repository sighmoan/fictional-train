package eu.kendall.simon.timetables;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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
}
