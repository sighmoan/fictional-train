package eu.kendall.simon.timetables.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Line (
    @JsonProperty("id")
    int lineNumber,
    @JsonProperty("transport_mode")
    String modality,
    @JsonProperty("group_of_lines")
    String lineGroup
){}
