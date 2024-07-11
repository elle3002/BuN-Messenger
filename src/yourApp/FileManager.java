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

/**
 * Diese Klasse ist für das Lesen, Erstellen und Speichern von Dateien zuständig
 */
public class FileManager {

    /**
     * liest Dateidaten in ein byte-Array ein und gibt es zurück
     * @param path Dateipfad der Datei, die ausgelesen werden soll
     * @return byte[] mit den Dateidaten
     * @throws IOException
     */
    public static byte[] readFile(String path) throws IOException {

        //liest das File je nach Fileendung aus
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

    /**
     * Liest die Daten hinter einem Bild aus
     * @param path Dateipfad der auszulesenden Datei
     * @return byte [] mit den Bilddaten
     * @throws IOException
     */
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

    /**
     * Liest die Daten hinter einer PDF aus
     * @param path Dateipfad der auszulesenden Datei
     * @return byte[] mit den PDF-Daten
     * @throws IOException
     */
    private static byte[] readPDFFile(String path) throws IOException {
        //erstellen eines Path-Objektes, welches im folgenden Schritt nötig ist
        Path filepath = Paths.get(path);
        //ermitteln des byte[] durch FIles
        return Files.readAllBytes(filepath);
    }


    /**
     * Speichert ein File
     * @param IP von der die Datei kommt
     * @param data die in ein File umgewandelt werden müssen
     * @param fileEndung der Datei
     * @return Filepath der neuen Datei
     * @throws IOException
     */
    public static String saveFile(String IP, byte[] data, String fileEndung) throws IOException {

        //Überprüfen, ob die IP bereits bekannt ist. Wenn ja wird der Ordner nach dem gespeicherten Namen der IP gespeichert, andernfalls nach der IP des Senders
        String name;
        Map<String, String> IPData = NameIPLogic.getNameAndIPFromFile();

        if (IPData.containsKey(IP)) {
            name = IPData.get(IP);
        } else{
            name = IP;
        }
        String directoryPath = "ressources/" + name ;

        //erstellen des Ordners, wenn noch keiner vorhanden ist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //erstellen eines Files in dem Verzeichnis mit aktueller Uhrzeit als Dateiname
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");

        String fileName = currentTime.format(formatter) + fileEndung;
        String filePath = directoryPath + "/" + fileName;
        File file = new File(filePath);

        //Je nach Fileendung, werden die Dateien abgespeichert
        switch (fileEndung) {
            case ".pdf":
                savePDF(data, file);
                break;
            case ".png":
                saveImage(data, file, fileEndung);
                break;
            case ".jpg":
                saveImage(data, file, fileEndung);
                break;
            case ".jpeg":
                saveImage(data, file, fileEndung);
                break;
        }
        return filePath;
    }

    /**
     * speichert ein Image ab
     * @param imageData Daten, aus denen das Bild erstellt wird
     * @param file File in dem gespeichert werden soll
     * @param fileEndung fileEndung der Datei
     * @throws IOException
     */
    private static void saveImage(byte[] imageData, File file, String fileEndung) throws IOException {
        // Konvertiere das Byte-Array in ein BufferedImage
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(bais);
        if (bufferedImage == null) {
            throw new IOException("Das Bild konnte nicht gelesen werden, möglicherweise ist das Bilddatenformat ungültig.");
        }
        // Entferne den führenden Punkt von der Dateiendung
        String formatName = fileEndung.startsWith(".") ? fileEndung.substring(1) : fileEndung;
        // Schreibe das BufferedImage als Bild-Datei
        ImageIO.write(bufferedImage, formatName, file);
    }


    /**
     * speichert ein PDF ab
     * @param pdfData Daten, aus denen das PDF erstallt werden soll
     * @param file File in dem gespeichert werden soll
     */
    private static void savePDF(byte[] pdfData, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(pdfData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * extrahiert die Fileendung einer Datei
     * @param filePath in dem das File liegt
     * @return Fileendung mit punkt vorher (z.B .png)
     */
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
