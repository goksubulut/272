package kt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Dosyanın tam yolunu buraya yaz
        String filePath = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Commands.txt.txt"; 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Dosya okunurken hata oluştu: " + e.getMessage());
        }
    }
}



