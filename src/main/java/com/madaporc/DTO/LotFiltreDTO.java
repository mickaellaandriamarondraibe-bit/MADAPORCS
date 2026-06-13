package com.madaporc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotFiltreDTO {

    private String code;
    private Long raceId;
    private Long statutLotId;
}