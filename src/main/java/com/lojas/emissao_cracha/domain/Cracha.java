package com.lojas.emissao_cracha.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cracha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Por favor escreva o nome que deseja ser chamado.")
    private String Nome;

    @NotNull(message = "Por favor insira o seu cargo. ")
    private String cargo;

    @NotNull(message = "Por favor insira a foto.")
    private String foto;
}
