package com.madaporc.DTO;

    import java.time.LocalDateTime;
import java.util.List;
    import lombok.Data;

    @Data
    public class VenteDTO {
        private Long id;

    private Long clientId;

    private LocalDateTime dateVente;

    private String statutVente;

    private String observation;

    private List<DetailVenteDTO> details;
    }
