package com.madaporc.DTO;

import lombok.Data;

@Data
public class RapportPresenceDTO {

    private long nombrePointages;
    private long presents;
    private long absents;
    private long retards;
}