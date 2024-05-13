package eu.kendall.simon.timetables;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record Departure (
    LocalDateTime expected,
    LocalDateTime scheduled,
    String display,
    @JsonProperty("line")
    Line lineData
){}
