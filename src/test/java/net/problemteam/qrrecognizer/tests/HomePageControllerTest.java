package net.problemteam.qrrecognizer.tests;

import java.io.File;

import net.problemteam.qrrecognizer.controllers.HomePageController;
import net.problemteam.qrrecognizer.services.GeneratorService;
import net.problemteam.qrrecognizer.services.RecognizerService;
import net.problemteam.qrrecognizer.util.UniqueFileFactory;

import org.easymock.EasyMock;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageControllerTest {
    private HomePageController homePageController;

    private RecognizerService recognizerServiceMock = null;

    private GeneratorService generatorServiceMock = null;

    private UniqueFileFactory uniqueFileFactoryMock = null;

    private String imagesDirectoryPath = "";

    private String generatedImagesPath = "";

    @BeforeMethod
    public void setup() throws Exception {
        this.homePageController = new HomePageController();

        this.recognizerServiceMock = EasyMock
                .createMock(RecognizerService.class);
        this.generatorServiceMock = EasyMock
                .createMock(GeneratorService.class);
        this.uniqueFileFactoryMock = EasyMock
                .createMock(UniqueFileFactory.class);

        homePageController.setRecognizerService(this.recognizerServiceMock);
        homePageController.setGeneratorService(generatorServiceMock);
        homePageController.setUniqueFileFactory(uniqueFileFactoryMock);
        homePageController.setImagesDirectoryPath(imagesDirectoryPath);
        homePageController.setGeneratedImagesPath(generatedImagesPath);
    }

    @Test
    public void testHomePage_normal() throws Exception {
        Assert.assertEquals(homePageController.homePage(), "index");
    }

    @Test
    public void testQrDecode_normal() throws Exception {
        File fileMock = EasyMock.createMock(File.class);
        MultipartFile multipartFileMock = EasyMock
                .createMock(MultipartFile.class);
        String qrCode = "awesome text";
        Model modelMock = EasyMock.createMock(Model.class);

        EasyMock.expect(
                uniqueFileFactoryMock.createUniqueFile(imagesDirectoryPath,
                        "/qrcode-", "")).andReturn(fileMock);
        EasyMock.expect(fileMock.createNewFile()).andReturn(true);
        EasyMock.expect(fileMock.exists()).andReturn(false);
        multipartFileMock.transferTo(fileMock);
        EasyMock.expectLastCall();
        EasyMock.expect(recognizerServiceMock.recognizeQrCode(fileMock))
        .andReturn(qrCode);
        EasyMock.expect(fileMock.delete()).andReturn(true);
        EasyMock.expect(modelMock.addAttribute("decodedText", qrCode))
        .andReturn(modelMock);

        EasyMock.replay(fileMock);
        EasyMock.replay(multipartFileMock);
        EasyMock.replay(modelMock);
        EasyMock.replay(this.recognizerServiceMock);
        EasyMock.replay(this.uniqueFileFactoryMock);

        String view = homePageController.qrDecode(multipartFileMock, modelMock);
        Assert.assertEquals(view, "decoded_text");
    }

    @Test
    public void testQrEncode_normal() throws Exception {
        String qrCode = "awesome text";
        File fileMock = EasyMock.createMock(File.class);
        Model modelMock = EasyMock.createMock(Model.class);
        String qrCodeFileName = "awesome name";

        EasyMock.expect(this.generatorServiceMock.generateQrCode(qrCode))
        .andReturn(fileMock);
        EasyMock.expect(fileMock.getName()).andReturn(qrCodeFileName);
        EasyMock.expect(
                modelMock.addAttribute("encodedImagePath", "/generated/"
                + qrCodeFileName)).andReturn(modelMock);

        EasyMock.replay(fileMock);
        EasyMock.replay(modelMock);
        EasyMock.replay(this.generatorServiceMock);
        EasyMock.replay(this.uniqueFileFactoryMock);

        String view = this.homePageController.qrEncode(qrCode, modelMock);
        Assert.assertEquals(view, "encoded_image");
    }
}
