package Webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import static javax.imageio.ImageIO.*;

public class WebcamDisplay {

    public Webcam webcam;
    public WebcamPanel panel;
    public String imagePath = "capturedImage.jpg";



    public WebcamDisplay() {

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public BufferedImage getImage() throws IOException {
        WebcamUtils.capture(webcam, "capturedImage", ImageUtils.FORMAT_JPG);
        BufferedImage bufferedImage = ImageIO.read(new File("capturedImage.jpg"));
        return bufferedImage;
    }

}
