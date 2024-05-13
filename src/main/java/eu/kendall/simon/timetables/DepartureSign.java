package eu.kendall.simon.timetables;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping("/stop")
public class DepartureSign {
    @JsonProperty("departures")
    Departure[] deps;

    private static String apiURL = "https://transport.integration.sl.se/v1/sites/";

    @GetMapping
    public @ResponseBody static DepartureSign getDepartures() {
        WebClient client = WebClient.create(apiURL);

        DepartureSign depSign = client.get()
                .uri("9192/departures")
                .retrieve()
                .bodyToMono(DepartureSign.class)
                .block();

        /*for(int i = 0; i < depSign.deps.length; i++) {
            System.out.printf("Line %d expected at %s", depSign.deps[i].lineNumber(), depSign.deps[i].expected());
        }*/

        return depSign;
    }

    public String toHTML() {
        return "";
    }
}
