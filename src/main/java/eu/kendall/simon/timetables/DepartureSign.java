package eu.kendall.simon.timetables;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.List;

@Controller
@RequestMapping("/stop")
public class DepartureSign {
    @JsonProperty("departures")
    Departure[] deps;

    private static String apiURL = "https://transport.integration.sl.se/v1/sites/";

    @GetMapping
    public @ResponseBody static String getDepartures() {
        WebClient client = WebClient.create(apiURL);

        DepartureSign depSign = client.get()
                .uri("9192/departures")
                .retrieve()
                .bodyToMono(DepartureSign.class)
                .block();

        /*for(int i = 0; i < depSign.deps.length; i++) {
            System.out.printf("Line %d expected at %s", depSign.deps[i].lineNumber(), depSign.deps[i].expected());
        }*/

        return depSign.toHTML();
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
                            deps[i].scheduled(),
                            deps[i].display());
                    rows.append(row);
                }
                String output = StringUtils.join(templatePage, ' ');
                return output.replace("{rows}", rows.toString());
            }
        } catch(URISyntaxException e) {
            System.out.println("URI Syntax exception raised when loading template.");
        } catch(IOException e) {
            System.out.println("Something went wrong loading the file.");
        }
        return "";
    }
}
