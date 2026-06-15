package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    public class PeseeLotDTO {
        private Long id;

    private Long lotPorcId;

    private BigDecimal poidsMoyenKg;

    private LocalDate datePesee;

    private String observation;
    }
