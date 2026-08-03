package fungsi;

import java.util.Date;
import java.util.function.Supplier;
import javax.swing.Timer;

/**
 * Utility untuk menjalankan jam real-time pada komponen widget.Tanggal.
 * Tanggal akan auto-update ke waktu sekarang setiap detik, SELAMA kondisi
 * yang diberikan bernilai true (misal: hanya saat mode input baru,
 * berhenti otomatis saat user memilih/mengedit data lama).
 *
 * @author novalar
 */
public class UtilJam {

    /**
     * Menjalankan jam berjalan pada komponen Tanggal.
     * @param tanggal   komponen widget.Tanggal yang mau di-update
     * @param aktifJika kondisi: true = jam terus jalan, false = berhenti update
     */
    public static void jalan(widget.Tanggal tanggal, Supplier<Boolean> aktifJika) {
        Timer timer = new Timer(1000, e -> {
            if (aktifJika.get()) {
                tanggal.setDate(new Date());
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}