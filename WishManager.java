package kidtask;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WishManager {
    private List<Wish> wishes;
    private static final String WISHES_FILE = "src/Wish.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public WishManager() {
        System.out.println("WishManager başlatılıyor...");
        wishes = new ArrayList<>();
        loadWishes();
        System.out.println("WishManager başlatıldı. Toplam istek sayısı: " + wishes.size());
    }

    private void loadWishes() {
        System.out.println("İstekler yükleniyor...");
        File wishesFile = new File(WISHES_FILE);
        if (!wishesFile.exists()) {
            System.out.println("UYARI: İstek dosyası bulunamadı. Yeni dosya oluşturulacak.");
            try {
                wishesFile.createNewFile();
            } catch (IOException e) {
                System.out.println("HATA: İstek dosyası oluşturulamadı: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(WISHES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 4) {
                    String wishType = parts[0];
                    String wishId = parts[1];
                    String title = parts[2].replace("\"", "");
                    String description = parts[3].replace("\"", "");
                    
                    Wish wish = new Wish(wishId, title, description, false);
                    
                    // Eğer WISH2 ise ve başlangıç/bitiş zamanları varsa
                    if (wishType.equals("WISH2") && parts.length >= 6) {
                        LocalDateTime startTime = LocalDateTime.parse(parts[4] + " " + parts[5], DATE_FORMATTER);
                        LocalDateTime endTime = LocalDateTime.parse(parts[6] + " " + parts[7], DATE_FORMATTER);
                        wish.setStartTime(startTime);
                        wish.setEndTime(endTime);
                    }
                    
                    wishes.add(wish);
                    System.out.println("İstek yüklendi: " + wish.getTitle());
                }
            }
        } catch (IOException e) {
            System.out.println("HATA: İstekler yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveWishes() {
        System.out.println("İstekler kaydediliyor...");
        try (PrintWriter writer = new PrintWriter(new FileWriter(WISHES_FILE))) {
            for (Wish wish : wishes) {
                StringBuilder line = new StringBuilder();
                
                // WISH1 veya WISH2 belirleme
                if (wish.getStartTime() != null && wish.getEndTime() != null) {
                    line.append("WISH2 ");
                } else {
                    line.append("WISH1 ");
                }
                
                line.append(wish.getId()).append(" ")
                    .append("\"").append(wish.getTitle()).append("\" ")
                    .append("\"").append(wish.getDescription()).append("\" ");
                
                // Eğer başlangıç/bitiş zamanları varsa
                if (wish.getStartTime() != null && wish.getEndTime() != null) {
                    line.append(wish.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" ")
                        .append(wish.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))).append(" ")
                        .append(wish.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" ")
                        .append(wish.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                
                writer.println(line.toString());
                System.out.println("İstek kaydedildi: " + wish.getTitle());
            }
            System.out.println("Tüm istekler başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("HATA: İstekler kaydedilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Wish> getAllWishes() {
        return new ArrayList<>(wishes);
    }

    public Wish getWishById(String id) {
        for (Wish wish : wishes) {
            if (wish.getId().equals(id)) {
                return wish;
            }
        }
        return null;
    }

    public void addWish(Wish wish) {
        wishes.add(wish);
        saveWishes();
    }
}
