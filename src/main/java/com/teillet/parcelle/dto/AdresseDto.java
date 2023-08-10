package com.teillet.parcelle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AdresseDto {
    private String id;
    private String codeVoie;
    private String codeCommune;
    private String numeroComplet;
    private String numero;
    private String repetition;
    private boolean pseudoNumero;
    private String nomVoie;
    private String nomVoieOriginal;
    private String nomVoieType;
    private String nomVoieFantoir;
    private List<PositionDto> positions;
    private PositionDto meilleurePosition;
    private String destinationPrincipale;
    private boolean adresseUtile;
    private List<String> codesParcelles;
    private List<String> tags;
}
