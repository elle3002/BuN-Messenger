package yourApp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class FileManager {

    /*
    public static byte[] readImage(String imagePath) throws IOException {
        File file = new File(imagePath);
        if (!file.exists() || !file.getName().endsWith(".png")) {
            throw new IllegalArgumentException("Only PNG files are supported.");
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] imageData = new byte[(int) file.length()];
        fis.read(imageData);
        fis.close();
        return imageData;
    }

    // Hilfsmethode zum Speichern einer PNG-Datei
    public static String saveImage(String IP, byte[] imageData) throws IOException {
        String directoryPath = "ressources/" + IP;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Speichern des Bildes im Verzeichnis
        String filePath = directoryPath + "/bild.png";
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(imageData);
        fos.close();

        return filePath;
    }
     */

    public static byte[] readFile(String path) throws IOException {
        String kindOfFile = extractFileEndung(path);
        
        switch (kindOfFile) {
            case ".pdf":
                return readPDFFile(path);
            case ".png":
                return readImage(path);
            case ".jpg":
                return readImage(path);
            case ".jpeg":
                return readImage(path);
            default:
                throw new IllegalArgumentException("Dieses Format wird nicht unterstützt");
        }
    }

    private static byte[] readImage(String path) throws IOException {
        // Lies das Bild von der Datei ein
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        if (bufferedImage == null) {
            throw new IOException("Das Bild konnte nicht gelesen werden, möglicherweise ist das Bilddatenformat ungültig.");
        }

        // Konvertiere das BufferedImage in ein Byte-Array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        baos.flush();
        byte[] imageData = baos.toByteArray();
        baos.close();

        return imageData;
    }

    private static byte[] readPDFFile(String path) throws IOException {
        Path filepath = Paths.get(path);
        return Files.readAllBytes(filepath);
    }



    public static String saveFile(String IP, byte[] data, String fileEndung) throws IOException {

        try {
            String name;
            Map<String, String> IPData = NameIPLogic.getNameAndIPFromFile();

            if (IPData.containsKey(IP)) {
                name = IPData.get(IP);
            } else{
                name = IP;
            }

            String directoryPath = "ressources/" + name ;


            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Speichern des Bildes im Verzeichnis mit aktueller Uhrzeit als Dateiname
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");

            String fileName = currentTime.format(formatter) + fileEndung;
            String filePath = directoryPath + "/" + fileName;
            File file = new File(filePath);

            switch (fileEndung) {
                case ".pdf":
                    savePDF(data, file);
                    break;
                case ".png":
                    saveImage(data, file);
                    break;
                case ".jpg":
                    saveImage(data, file);
                    break;
                case ".jpeg":
                    saveImage(data, file);
                    break;
            }
            return filePath;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveImage(byte[] imageData, File file) throws IOException {
        // Konvertiere das Byte-Array in ein BufferedImage
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(bais);
        if (bufferedImage == null) {
            throw new IOException("Das Bild konnte nicht gelesen werden, möglicherweise ist das Bilddatenformat ungültig.");
        }
        // Schreibe das BufferedImage als PNG-Datei
        ImageIO.write(bufferedImage, "png", file);
    }

    private static void savePDF(byte[] pdfData, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(pdfData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractFileEndung(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            throw new IllegalArgumentException("Kein File gefunden");
        }
        return "." + fileName.substring(lastIndex + 1);
    }
}
