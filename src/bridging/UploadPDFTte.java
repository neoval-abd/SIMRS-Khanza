package bridging;

import fungsi.akses;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.core5.http.ContentType;

import fungsi.koneksiDB;
import fungsi.sekuel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UploadPDFTte {

    private final String fileName;
    private final String sep, norawat, kode, inacbg;
    private final String tanggal, tanggaljam;
    private final String username = koneksiDB.USERNEXTCLOUD();
    private final String password = koneksiDB.PASNEXTCLOUD();
    private final String baseUrl = koneksiDB.URLNEXTCLOUD() + username + "/";
    private final sekuel Sequel = new sekuel();
    
    // Tambahkan field untuk menyimpan URL hasil upload
    private String uploadedUrl = "";
    private boolean uploadSuccess = false;
    private String statusOverride = "";

    public UploadPDFTte(String fileName, String sep, String tanggal, String norawat, String kode, String tanggaljam, String inacbg) {
        this.fileName = fileName;
        this.sep = sep;
        this.tanggal = tanggal;
        this.norawat = norawat;
        this.kode = kode;
        this.tanggaljam = tanggaljam;
        this.inacbg = inacbg;
    }

    // Method untuk mendapatkan URL hasil upload
    public String getUploadedUrl() {
        return uploadedUrl;
    }

    public void setStatusOverride(String status) {
        this.statusOverride = status;
    }
    
    // Method untuk mengecek status upload
    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public boolean upload() {
        CloseableHttpClient httpClient = null;
        uploadSuccess = false;
        uploadedUrl = "";
        
        try {
            if (baseUrl.startsWith("https://")) {
                // Koneksi HTTPS dengan SSL TrustAll
                SSLContext sslContext = SSLContextBuilder.create()
                        .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                        .build();

                SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                        .setSslContext(sslContext)
                        .build();

                httpClient = HttpClients.custom()
                        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                                .setSSLSocketFactory(sslSocketFactory)
                                .build())
                        .build();
            } else {
                // Koneksi HTTP biasa
                httpClient = HttpClients.custom()
                        .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create().build())
                        .build();
            }

            // Ambil status rawat dari tabel bridging_sep - PERBAIKAN: gunakan no_rawat jika SEP tidak ditemukan
            String statusRawat = "";
            try {
                statusRawat = Sequel.cariIsi(
                        "SELECT CASE jnspelayanan WHEN '1' THEN 'Rawat-Inap' WHEN '2' THEN 'Rawat-Jalan' ELSE 'Unknown' END FROM bridging_sep WHERE no_sep=?", sep);
                
                // Jika status rawat tidak ditemukan berdasarkan SEP, coba dari no_rawat
                if (statusRawat.equals("Unknown") || statusRawat.equals("")) {
                    System.out.println("Status rawat tidak ditemukan untuk SEP: " + sep + ", mencoba dari no_rawat...");
                    statusRawat = Sequel.cariIsi(
                            "SELECT CASE jnspelayanan WHEN '1' THEN 'Rawat-Inap' WHEN '2' THEN 'Rawat-Jalan' ELSE 'Unknown' END FROM bridging_sep WHERE no_rawat=? LIMIT 1", norawat);
                    
                    // Jika masih tidak ditemukan, gunakan default
                    if (statusRawat.equals("Unknown") || statusRawat.equals("")) {
                        statusRawat = "Rawat-Inap"; // Default
                        System.out.println("Menggunakan status rawat default: " + statusRawat);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting status rawat: " + e.getMessage());
                statusRawat = "Rawat-Inap"; // Default jika error
            }
            
            if (!statusOverride.isEmpty()) {
                statusRawat = statusOverride;
            }

            String kategori = inacbg.equals("INA-CBG") ? "INA-CBG" : "NON-INACBG";

            // PERBAIKAN: Parse tanggal dengan format yang benar
            String[] pathParts;
            String year, month, day;
            
            try {
                // Cek format tanggal yang diterima (kemungkinan yyyy-mm-dd atau dd-mm-yyyy)
                if (tanggal.contains("-")) {
                    pathParts = tanggal.split("-");
                    if (pathParts[0].length() == 4) {
                        // Format yyyy-mm-dd
                        year = pathParts[0];
                        month = pathParts[1];
                        day = pathParts[2];
                    } else {
                        // Format dd-mm-yyyy
                        year = pathParts[2];
                        month = pathParts[1];
                        day = pathParts[0];
                    }
                } else {
                    // Fallback ke tahun sekarang jika format tidak dikenali
                    year = String.valueOf(LocalDateTime.now().getYear());
                    month = String.format("%02d", LocalDateTime.now().getMonthValue());
                    day = String.format("%02d", LocalDateTime.now().getDayOfMonth());
                }
                
                // Pastikan format 2 digit untuk bulan dan hari
                month = String.format("%02d", Integer.parseInt(month));
                day = String.format("%02d", Integer.parseInt(day));
                
            } catch (Exception e) {
                System.err.println("Error parsing date: " + tanggal + ", using current date");
                LocalDateTime now = LocalDateTime.now();
                year = String.valueOf(now.getYear());
                month = String.format("%02d", now.getMonthValue());
                day = String.format("%02d", now.getDayOfMonth());
            }

            // PERBAIKAN: Struktur folder yang benar tanpa double slash
            // SEP sudah dibersihkan dari slash di script utama
            String currentPath = baseUrl + year + "/" + kategori + "/" + month + "/" + statusRawat + "/" + day + "/" + sep + "/";
            
            System.out.println("=== UPLOAD DEBUG INFO ===");
            System.out.println("Base URL: " + baseUrl);
            System.out.println("Tanggal input: " + tanggal);
            System.out.println("Year: " + year + ", Month: " + month + ", Day: " + day);
            System.out.println("Status Rawat: " + statusRawat);
            System.out.println("SEP: " + sep);
            System.out.println("Current Path: " + currentPath);

            // PERBAIKAN: Buat struktur folder bertahap dengan path yang benar
            String[] folderPaths = {
                baseUrl + year + "/",
                baseUrl + year + "/" + kategori + "/",
                baseUrl + year + "/" + kategori + "/" + month + "/",
                baseUrl + year + "/" + kategori + "/" + month + "/" + statusRawat + "/",
                baseUrl + year + "/" + kategori + "/" + month + "/" + statusRawat + "/" + day + "/",
                currentPath
            };
            
            for (String folderPath : folderPaths) {
                createFolder(httpClient, folderPath);
            }

            File fileToUpload = new File(fileName);
            if (!fileToUpload.exists()) {
                System.err.println("File tidak ditemukan: " + fileName);
                return false;
            }

            String uploadUrl = currentPath + fileToUpload.getName();
            System.out.println("Upload URL: " + uploadUrl);
            
            uploadSuccess = uploadFile(httpClient, uploadUrl, fileToUpload, statusRawat);
            
            return uploadSuccess;

        } catch (Exception e) {
            System.err.println("Error upload PDF TTE: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException ex) {
                System.err.println("Gagal menutup koneksi HTTP: " + ex.getMessage());
            }
        }
    }

    private void createFolder(CloseableHttpClient httpClient, String folderUrl) {
        try {
            HttpPut mkcolRequest = new HttpPut(folderUrl) {
                @Override
                public String getMethod() {
                    return "MKCOL";
                }
            };

            mkcolRequest.setHeader("Authorization", "Basic "
                    + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));

            try (CloseableHttpResponse response = httpClient.execute(mkcolRequest)) {
                int status = response.getCode();
                if (status == 201) {
                    System.out.println("Folder dibuat: " + folderUrl);
                } else if (status == 405) {
                    System.out.println("Folder sudah ada: " + folderUrl);
                } else {
                    System.err.println("Gagal buat folder " + folderUrl + " (Status: " + status + ")");
                }
            }
        } catch (IOException e) {
            System.err.println("MKCOL error: " + e.getMessage());
        }
    }

    private boolean uploadFile(CloseableHttpClient httpClient, String uploadUrl, File file, String statusRawat) {
        try {
            HttpPut uploadRequest = new HttpPut(uploadUrl);
            uploadRequest.setHeader("Authorization", "Basic "
                    + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
            uploadRequest.setHeader("Content-Type", "application/pdf");

            HttpEntity entity = EntityBuilder.create()
                    .setBinary(Files.readAllBytes(file.toPath()))
                    .setContentType(ContentType.APPLICATION_PDF)
                    .build();

            uploadRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(uploadRequest)) {
                int statusCode = response.getCode();
                if (statusCode == 201 || statusCode == 204) {
                    System.out.println("Upload sukses ke: " + uploadUrl);

                    // Set uploadedUrl untuk bisa diambil dari luar
                    uploadedUrl = uploadUrl.substring(baseUrl.length());
                    String uploadedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    System.out.println("Path relatif (urlSaja): " + uploadedUrl);

                    // PERBAIKAN: Handle case dimana fileName mungkin tidak ada prefix "tmpPDF/"
                    String cleanFileName = fileName;
                    if (fileName.startsWith("tmpPDF/")) {
                        cleanFileName = fileName.substring("tmpPDF/".length());
                    } else if (fileName.contains("/")) {
                        cleanFileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    }

                    Sequel.menyimpantf(
                            "berkas_tte (no_rawat, no_sep, tanggal, kode, nama_file, file_path, status, kategori, uploaded_at, uploaded_by)",
                            "?,?,?,?,?,?,?,?,?,?",
                            "berkas",
                            10,
                            new String[]{norawat, sep, tanggaljam, kode, cleanFileName, uploadedUrl, statusRawat, inacbg, uploadedAt, akses.getkode()}
                    );

                    return true;
                } else {
                    System.err.println("Upload gagal (Status: " + statusCode + ")");
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Upload error: " + e.getMessage());
            return false;
        }
    }
}