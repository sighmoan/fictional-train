package eu.kendall.simon.timetables;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/stop")
public class DepartureSign {
    @JsonProperty("departures")
    Departure[] deps;
    static final String slussenStopId = "9192";

    private static String apiURL = "https://transport.integration.sl.se/v1/sites/";

    public static DepartureSign getDepartures( String id) {
        WebClient client = WebClient.create(apiURL);

        DepartureSign depSign = client.get()
                .uri(Integer.valueOf(id) + "/departures")
                .retrieve()
                .bodyToMono(DepartureSign.class)
                .block();

        return depSign;
    }

    @GetMapping
    public @ResponseBody static String stopController(@RequestParam(defaultValue = slussenStopId) String stop) {
        return getDepartures(stop).toHTML();
    }

    public String toHTML() {
        try {
            Path templateFilePath = Paths.get(getClass().getResource("/templates/templatePage.html").toURI());
            System.out.println(getClass());

            if(Files.exists(templateFilePath)) {
                List<String> templatePage = Files.readAllLines(templateFilePath);
                StringBuilder rows = new StringBuilder();
                for(int i = 0; i < deps.length; i++) {
                    String row = String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                            deps[i].lineData().modality(),
                            deps[i].lineData().lineNumber(),
                            deps[i].scheduled().toLocalTime(),
                            deps[i].display());
                    rows.append(row);
                }
                String output = StringUtils.join(templatePage, ' ');
                String time = Integer.toString(LocalTime.now().getHour()) + ':' + Integer.toString(LocalTime.now().getMinute());
                return output.replace("{time}", time).replace("{rows}", rows.toString());
            }
        } catch(URISyntaxException e) {
            System.out.println("URI Syntax exception raised when loading template.");
        } catch(IOException e) {
            System.out.println("Something went wrong loading the file.");
        }
        return "";
    }
}
