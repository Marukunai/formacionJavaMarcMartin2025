package com.edisa.formacion.mayo2025;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BarcodeGenerator {
    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                throw new IllegalArgumentException("Debes indicar: texto, ruta de imagen, y formato (QR, EAN_13, CODE_128, etc.)\n" +
                        "Ejemplo: java BarcodeGenerator \"Texto\" \"C:\\\\ruta\\\\codigo.jpg\" QR");
            }

            String text = args[0];
            String outputPath = args[1];
            String formatInput = args[2].toUpperCase();

            BarcodeFormat format;
            try {
                format = BarcodeFormat.valueOf(formatInput);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Formato inválido: " + formatInput + ". Usa QR, EAN_13, CODE_128, etc.");
            }

            int width = 300;
            int height = (format == BarcodeFormat.QR_CODE) ? 300 : 150;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            if (format == BarcodeFormat.QR_CODE) {
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            }

            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, format, width, height, hints);
            Path path = new File(outputPath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", path);

            System.out.println("Código generado correctamente en: " + outputPath);

        } catch (WriterException e) {
            System.err.println("Error generando el código: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error escribiendo el fichero: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
