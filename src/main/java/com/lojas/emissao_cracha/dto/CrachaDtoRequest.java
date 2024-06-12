package com.lojas.emissao_cracha.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrachaDtoRequest {

    @NotNull(message = "Por favor escreva o nome que deseja ser chamado.")
    @NotBlank(message = "Por favor escreva o nome que deseja ser chamado.")
    private String nome;

    @NotNull(message = "Por favor insira o seu cargo.")
    @NotBlank(message =  "Por favor insira o seu cargo.")
    private String cargo;

    @NotNull(message = "Por favor insira a foto.")
    private MultipartFile foto;
}
