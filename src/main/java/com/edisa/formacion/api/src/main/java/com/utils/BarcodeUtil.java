package com.edisa.formacion.api.src.main.java.com.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class BarcodeUtil {

    public static void generateBarcodeImage(String text, BarcodeFormat format, String outputPath) throws Exception {
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
    }

    public static List<String> readBarcodesFromImage(File imageFile) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        BinaryBitmap bitmap = new BinaryBitmap(
                new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage))
        );

        MultiFormatReader reader = new MultiFormatReader();
        List<String> results = new ArrayList<>();

        try {
            Result result = reader.decode(bitmap);
            results.add(result.getText());
        } catch (NotFoundException e) {
            results.add("No se detectaron códigos.");
        }

        return results;
    }
}
