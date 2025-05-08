package com.edisa.formacion.mayo2025;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                throw new IllegalArgumentException("Debes proporcionar el texto y la ruta de salida. Ejemplo:\njava QRCodeGenerator \"Hola mundo\" \"C:\\\\Users\\\\aaa\\\\Documents\\\\codigoqr.jpg\"");
            }

            String text = args[0];
            String outputPath = args[1];

            int width = 300;
            int height = 300;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            Path path = new File(outputPath).toPath();

            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", path);
            System.out.println("QR generado en: " + outputPath);

        } catch (WriterException e) {
            System.err.println("Error generando el código QR: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error escribiendo el fichero: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
