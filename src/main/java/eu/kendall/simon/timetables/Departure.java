package eu.kendall.simon.timetables;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record Departure (
    LocalDateTime expected,
    LocalDateTime scheduled,
    String display,
    @JsonProperty("line.id")
    int lineNumber,
    @JsonProperty("line.transport_mode")
    String modality,
    @JsonProperty("line.group_of_lines")
    String lineGroup
){}
