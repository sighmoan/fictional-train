package eu.kendall.simon.timetables.services;

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
            Path templateFilePath = Paths.get(getClass().getResource("/templates/templatePage.html").toURI());
            System.out.println(getClass());

            if(Files.exists(templateFilePath)) {
                List<String> templatePage = Files.readAllLines(templateFilePath);
                StringBuilder rows = new StringBuilder();
                for(int i = 0; i < depSign.getDeps().length; i++) {
                    String row = String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                            depSign.getDep(i).lineData().modality(),
                            depSign.getDep(i).lineData().lineNumber(),
                            depSign.getDep(i).scheduled().toLocalTime(),
                            depSign.getDep(i).display());
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
