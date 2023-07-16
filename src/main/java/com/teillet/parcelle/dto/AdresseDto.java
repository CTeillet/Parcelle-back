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
	public String id;
	public String codeVoie;
	public String codeCommune;
	public String numeroComplet;
	public String numero;
	public String repetition;
	public boolean pseudoNumero;
	public String nomVoie;
	public String nomVoieOriginal;
	public String nomVoieType;
	public String nomVoieFantoir;
	public List<PositionDto> positions;
	public PositionDto meilleurePosition;
	public String destinationPrincipale;
	public boolean adresseUtile;
	public List<String> codesParcelles;
	public List<String> tags;
}
