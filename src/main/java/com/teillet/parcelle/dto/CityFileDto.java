package com.teillet.parcelle.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityFileDto {
    @JsonProperty("geo_point_2d")
    private CoordonneesGpsDto geoPoint2d;
    @JsonProperty("id_geofla")
    private String idGeofla;
    @JsonProperty("code_com")
    private String codeCom;
    @JsonProperty("insee_com")
    private String inseeCom;
    @JsonProperty("nom_com")
    private String nomCom;
    @JsonProperty("statut")
    private String statut;
    @JsonProperty("x_chf_lieu")
    private Integer xChfLieu;
    @JsonProperty("y_chf_lieu")
    private Integer yChfLieu;
    @JsonProperty("x_centroid")
    private Integer xCentroid;
    @JsonProperty("y_centroid")
    private Integer yCentroid;
    @JsonProperty("z_moyen")
    private Integer zMoyen;
    private Integer superficie;
    private Integer population;
    @JsonProperty("code_cant")
    private String codeCant;
    @JsonProperty("code_arr")
    private String codeArr;
    @JsonProperty("code_dept")
    private String codeDept;
    @JsonProperty("nom_dept")
    private String nomDept;
    @JsonProperty("code_reg")
    private String codeReg;
    @JsonProperty("nom_reg")
    private String nomReg;
    @JsonProperty("nom_de_la_commune")
    private String nomDeLaCommune;
    @JsonProperty("code_postal")
    private String codePostal;
    @JsonProperty("ligne_5")
    private String ligne5;
    @JsonProperty("libelle_d_acheminement")
    private String libelleDAcheminement;
    @JsonProperty("coordonnees_gps")
    private CoordonneesGpsDto coordonneesGps;

}
