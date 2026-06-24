package com.madaporc.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PeseeLotDTO {

    private Long id;

    private Long lotId;

    private LocalDate datePesee;

    private BigDecimal poidsMoyen;

    private String observation;
}

