package eu.kendall.simon.timetables.services;

import eu.kendall.simon.timetables.dtos.Departure;
import eu.kendall.simon.timetables.models.DepartureSignModel;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

public class DepartureSignService {
    static final String slussenStopId = "9192";

    private DepartureSignModel depSign;
    private static String apiURL = "https://transport.integration.sl.se/v1/sites/";

    public DepartureSignModel getDepartureSign() {
        return depSign;
    }

    public static DepartureSignService getDepartures(String id) {
        WebClient client = WebClient.create(apiURL);

        DepartureSignService serv = new DepartureSignService();

        serv.depSign = client.get()
                .uri(Integer.valueOf(id) + "/departures")
                .retrieve()
                .bodyToMono(DepartureSignModel.class)
                .block();

        return serv;
    }

    public String toHTML() {
        try {
            Path rowTemplatePath = Paths.get(getClass().getResource("/templates/templateRow.html").toURI());
            StringBuilder rows = new StringBuilder();
            if (Files.exists(rowTemplatePath))
                for (Departure dep : depSign.getDeps())
                    rows.append(Files.readString(rowTemplatePath)
                            .replace("{modality}", dep.lineData().modality())
                            .replace("{lineNumber}", Integer.toString(dep.lineData().lineNumber()))
                            .replace("{scheduled}", dep.scheduled().toLocalTime().toString())
                            .replace("{display}", dep.display()));

            Path templateFilePath = Paths.get(getClass().getResource("/templates/templatePage.html").toURI());
            if(Files.exists(templateFilePath)) {
                String templatePage = Files.readString(templateFilePath);
                String time = Integer.toString(LocalTime.now().getHour()) + ':' + Integer.toString(LocalTime.now().getMinute());
                return templatePage.replace("{time}", time).replace("{rows}", rows.toString());
            }
        } catch(URISyntaxException e) {
            System.out.println("URI Syntax exception raised when loading template.");
        } catch(IOException e) {
            System.out.println("Something went wrong loading the file.");
        }
        return "";
    }
}
