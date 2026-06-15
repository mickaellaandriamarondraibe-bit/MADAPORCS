package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDateTime;
    import lombok.Data;

    @Data
    public class MouvementStockDTO {
        private Long id;

    private Long ingredientId;

    private Long typeMouvementStockId;

    private BigDecimal quantiteKg;

    private BigDecimal prixTotal;

    private LocalDateTime dateMouvement;

    private String motif;
    }
