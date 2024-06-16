package com.lojas.emissao_cracha.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrachaDtoResponse {

    private Long id;

    private String nome;

    private String cargo;

    private String foto;

    private String qrCode;
}
