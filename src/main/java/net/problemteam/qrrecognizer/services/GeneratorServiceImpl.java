package net.problemteam.qrrecognizer.services;

import java.io.File;

import net.problemteam.qrrecognizer.util.UniqueFileFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class GeneratorServiceImpl implements GeneratorService {
    private static final Logger log = Logger.getLogger(GeneratorService.class);

    @Value("${qrrecognizer.generatedImagesPath}")
    private String generatedCodesPath;

    @Autowired
    private UniqueFileFactory uniqueFileFactory;

    public void setUniqueFileFactory(UniqueFileFactory uniqueFileFactory) {
        this.uniqueFileFactory = uniqueFileFactory;
    }

    public File generateQrCode(String qrCode) throws Exception {
        File outputFile = null;
        do {
            outputFile = uniqueFileFactory.createUniqueFile(generatedCodesPath,
                    "generated-", ".png");
        } while (outputFile.exists());

        try {
            BitMatrix qrCodeBitMatrix = new QRCodeWriter().encode(qrCode,
                    BarcodeFormat.QR_CODE, 256, 256);
            MatrixToImageWriter.writeToFile(qrCodeBitMatrix, "PNG", outputFile);
        } catch (WriterException e) {
            log.error("Could not write a qrcode to the file "
                    + outputFile.getAbsolutePath());
            if (!outputFile.delete()) {
                log.error("Generated file "
                        + outputFile.getAbsolutePath()
                        + " was not deleted after an exception has occured.");
            }
            throw new Exception(e);//Вызов исключения
        }

        return outputFile;
    }
}
