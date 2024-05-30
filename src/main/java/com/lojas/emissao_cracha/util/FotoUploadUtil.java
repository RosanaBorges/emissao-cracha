package com.lojas.emissao_cracha.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FotoUploadUtil {

    @Value("${file.upload-dir}")
    private String fotosUpload;

    public String salvarFoto(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(fotosUpload + File.separator + fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }

}
