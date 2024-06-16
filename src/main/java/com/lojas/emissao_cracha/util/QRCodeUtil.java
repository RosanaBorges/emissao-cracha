package com.lojas.emissao_cracha.util;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
public class QRCodeUtil {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String geradorQRCode(String text, String fileName) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

        Path qrCodePath = Paths.get(uploadDir, fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", qrCodePath);

        return fileName;
    }
}