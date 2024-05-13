package eu.kendall.simon.timetables.controllers;

import eu.kendall.simon.timetables.services.DepartureSignService;
import eu.kendall.simon.timetables.dtos.Departure;
import eu.kendall.simon.timetables.services.DepartureSignService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/stop")
public class DepartureSignController {
    private final static String slussenStopId = "9192";
    private DepartureSignService serv;

    public DepartureSignController() {
        serv = DepartureSignService.getDepartures(slussenStopId);
    }

    @GetMapping
    public @ResponseBody
    String stopController(@RequestParam(defaultValue = slussenStopId) String stop) {
        serv = DepartureSignService.getDepartures(stop);
        return serv.toHTML();
    }


}
