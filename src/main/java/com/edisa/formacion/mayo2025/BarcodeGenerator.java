package com.edisa.formacion.mayo2025;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BarcodeGenerator {

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                // Modo lectura de códigos desde imagen
                decodeFromImage(args[0]);
            } else if (args.length >= 3) {
                // Modo generación
                generateBarcode(args[0], args[1], args[2]);
            } else {
                throw new IllegalArgumentException("""
                        Uso:
                        - Para generar código: java BarcodeGenerator "Texto" "C:\\\\ruta\\\\codigo.jpg" "QR"
                        - Para leer código:    java BarcodeGenerator "C:\\\\ruta\\\\codigo.jpg"
                        """);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void generateBarcode(String text, String outputPath, String formatInput) throws WriterException, IOException {
        BarcodeFormat format;
        try {
            format = BarcodeFormat.valueOf(formatInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato inválido: " + formatInput + ". Usa QR, EAN_13, CODE_128, PDF_417, etc.");
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

        System.out.println("✅ Código generado correctamente en: " + outputPath);
    }

    public static void decodeFromImage(String imagePath) throws IOException, NotFoundException {
        File file = new File(imagePath);
        BufferedImage bufferedImage = ImageIO.read(file);

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.values()));

        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(bitmap, hints);

        System.out.println("✅ Código detectado:");
        System.out.println("Formato: " + result.getBarcodeFormat());
        System.out.println("Contenido: " + result.getText());
    }
}
