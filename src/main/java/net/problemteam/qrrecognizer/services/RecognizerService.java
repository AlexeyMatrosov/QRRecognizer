package net.problemteam.qrrecognizer.services;

import java.io.File;

public interface RecognizerService {

    String recognizeQrCode(File qrCodePath);

}