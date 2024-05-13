package eu.kendall.simon.timetables.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.kendall.simon.timetables.dtos.Departure;

public class DepartureSignModel {
    @JsonProperty("departures")
    Departure[] deps;

    public Departure[] getDeps() {
        return deps;
    }

    public Departure getDep(int index) {
        return deps[index];
    }
}
