package rekammedis;

// Bridging packages
import bridging.ApiINACBG;

// Jackson JSON
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// iText PDF
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;

// Fungsi packages
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;

// Java AWT
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

// Java IO
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

// Java Net
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

// Java NIO
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Java SQL
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Java Text & Time
import java.text.SimpleDateFormat;

// Java Util
import java.util.Base64;
import java.util.Date;

// Javax ImageIO & Swing
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

// Kepegawaian
import kepegawaian.DlgCariPegawai2;

// Rekam Medis
// import rekammedis.RMSirirajScore;
// import rekammedis.RMPneumoniaSeverityIndex;
// import rekammedis.RMCURB65;
// Apache Commons HttpClient
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

// Apache HttpClient 5
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

// Apache PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

// JSON
import org.json.JSONObject;

// SIMRS Khanza
import simrskhanza.DlgCariPasien;

/**
 *
 * @author windiarto
 */
public class RMGenerateKlaim extends javax.swing.JDialog {

    private validasi Valid = new validasi();
    private final sekuel Sequel = new sekuel();
    private PreparedStatement ps, ps2, pskoders, psbayi;
    private ResultSet rs, rs2, rs3, rs4, rs5, rs6, rs7, rs8, rsracikan, rsverif, rsdokter, rskode, rsbayi, rsresume, rslab, rsrad, rsbayigabung, rsruang;
    private Connection koneksi = koneksiDB.condb();
    private int i = 0, urut = 0, w = 0, s = 0, urutdpjp = 0, x = 0;
    private double biayaperawatan = 0, total = 0, inapdrpasien = 0;
    private String lokal, filterdpjp = "", kddpjp = "", dpjp = "", dokterrujukan = "", polirujukan = "", keputusan = "", ke1 = "", ke2 = "", ke3 = "", ke4 = "", ke5 = "", ke6 = "", file = "", caripenjab = "", inputString = "", petugasbilling = "", bayi1 = "", bayi2 = "",
            bayi3 = "", tgl_masuk = "", tgl_keluar = "", billing1 = "", billing2 = "", json = "";
    private StringBuilder htmlContent;
    private HttpClient http = new HttpClient();
    private GetMethod get;
    private DlgCariPasien pasien = new DlgCariPasien(null, true);
    public DlgCariPegawai2 pegawai = new DlgCariPegawai2(null, true);
    public ApiINACBG inacbg = new ApiINACBG();
    private boolean esign = false, sertisign = false;
    private boolean prosesPdfBerjalan = false;
    private boolean prosesTampilBerjalan = false;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;

    /**
     * Creates new form DlgLhtBiaya
     *
     * @param parent
     * @param modal
     */
    public RMGenerateKlaim(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(885, 674);
        WindowURLSertisign.setSize(570, 100);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        lokal = formatter.format(date);

        NoRM.setDocument(new batasInput((byte) 20).getKata(NoRM));
        NoRawat.setDocument(new batasInput((byte) 20).getKata(NoRawat));

        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (pasien.getTable().getSelectedRow() != -1) {
                    NoRM.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 0).toString());
                    NmPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 1).toString());
                    Jk.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 3).toString());
                    TempatLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 4).toString());
                    TanggalLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 5).toString());
                    IbuKandung.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 6).toString());
                    Alamat.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 7).toString());
                    GD.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 8).toString());
                    StatusNikah.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 10).toString());
                    Agama.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 11).toString());
                    Pendidikan.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 15).toString());
                    Bahasa.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 26).toString());
                    CacatFisik.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(), 32).toString());
                }
                NoRM.requestFocus();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    pasien.dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (pegawai.getTable().getSelectedRow() != -1) {
                    KdPeg2.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(), 0).toString());
                    TPegawai2.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(), 1).toString());
                    KdPeg2.requestFocus();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTMLRiwayatPerawatan.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
        styleSheet.addRule(".isi2 td{border-right: 1px solid #e2e7dd;font: 9px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 9px;border: white;}");
        Document doc = kit.createDefaultDocument();
        LoadHTMLRiwayatPerawatan.setDocument(doc);
        LoadHTMLRiwayatPerawatan.setEditable(false);
        LoadHTMLRiwayatPerawatan.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        ChkAccor.setSelected(true);
        isMenu();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Pekerjaan = new widget.TextBox();
        WindowURLSertisign = new javax.swing.JDialog();
        internalFrame9 = new widget.InternalFrame();
        jLabel43 = new widget.Label();
        panelisi6 = new widget.panelisi();
        BtnCloseUrl = new widget.Button();
        BtnBukaURL = new widget.Button();
        jLabel40 = new widget.Label();
        URLSertisign = new widget.TextBox();
        BtnDownloadFile = new widget.Button();
        BtnDownloadBukaFile = new widget.Button();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnGeneratePDF = new javax.swing.JMenuItem();
        MnGeneratePDFSertiSign = new javax.swing.JMenuItem();
        Tanggal = new widget.Tanggal();
        Status = new javax.swing.JTextField();
        Umur = new javax.swing.JTextField();
        internalFrame1 = new widget.InternalFrame();
        panelGlass5 = new widget.panelisi();
        R4 = new widget.RadioButton();
        NoRawat = new widget.TextBox();
        jLabel41 = new widget.Label();
        KdPeg2 = new widget.TextBox();
        TPegawai2 = new widget.TextBox();
        BtnSeekPegawai1 = new widget.Button();
        BtnCari1 = new widget.Button();
        BtnAll = new widget.Button();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        BtnPrint1 = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        internalFrame2 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        LoadHTMLRiwayatPerawatan = new widget.editorpane();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        ScrollMenu = new widget.ScrollPane();
        FormMenu = new widget.PanelBiasa();
        chkRawatJalan = new widget.CekBox();
        chkRawatInap = new widget.CekBox();
        chkSepBpjs = new widget.CekBox();
        chkResume = new widget.CekBox();
        chkOperasiVK = new widget.CekBox();
        chkTriase = new widget.CekBox();
        chkFormAssesment = new widget.CekBox();
        chkAsuhanKeperawatanRanapNeonatus = new widget.CekBox();
        chkKonsultasiMedik = new widget.CekBox();
        chkUjiFungsiKFR = new widget.CekBox();
        chkLayananKedokteranFisikRehabilitasi = new widget.CekBox();
        chkLayananProgramKFR = new widget.CekBox();
        chkHasilRad = new widget.CekBox();
        chkHasilLab = new widget.CekBox();
        chkHasilUSG = new widget.CekBox();
        chkBerkasDigital = new widget.CekBox();
        chkSuratKontrolBiasa = new widget.CekBox();
        chkSuratKontrol = new widget.CekBox();
        chkRiwayatSOAPIE = new widget.CekBox();
        chkSPR = new widget.CekBox();
        chkSuratKelahiranBayi = new widget.CekBox();
        chkTransferAntarRuang = new widget.CekBox();
        chkBilling = new widget.CekBox();
        chkResep = new widget.CekBox();
        chkPemberianObat = new widget.CekBox();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label17 = new widget.Label();
        NoRM = new widget.TextBox();
        NmPasien = new widget.TextBox();
        BtnPasien = new widget.Button();
        label20 = new widget.Label();
        Jk = new widget.TextBox();
        label21 = new widget.Label();
        TempatLahir = new widget.TextBox();
        label22 = new widget.Label();
        Alamat = new widget.TextBox();
        label23 = new widget.Label();
        GD = new widget.TextBox();
        label24 = new widget.Label();
        IbuKandung = new widget.TextBox();
        TanggalLahir = new widget.TextBox();
        label25 = new widget.Label();
        Agama = new widget.TextBox();
        StatusNikah = new widget.TextBox();
        label26 = new widget.Label();
        Pendidikan = new widget.TextBox();
        label27 = new widget.Label();
        label28 = new widget.Label();
        Bahasa = new widget.TextBox();
        label29 = new widget.Label();
        CacatFisik = new widget.TextBox();

        Pekerjaan.setEditable(false);
        Pekerjaan.setName("Pekerjaan"); // NOI18N
        Pekerjaan.setPreferredSize(new java.awt.Dimension(100, 23));

        WindowURLSertisign.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowURLSertisign.setModal(true);
        WindowURLSertisign.setName("WindowURLSertisign"); // NOI18N
        WindowURLSertisign.setUndecorated(true);
        WindowURLSertisign.setResizable(false);

        internalFrame9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ URL File Hasil Tanda Tangan Sertisign ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout());

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("%");
        jLabel43.setName("jLabel43"); // NOI18N
        internalFrame9.add(jLabel43, java.awt.BorderLayout.CENTER);

        panelisi6.setName("panelisi6"); // NOI18N
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 44));
        panelisi6.setLayout(null);

        BtnCloseUrl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); // NOI18N
        BtnCloseUrl.setMnemonic('T');
        BtnCloseUrl.setText("Tutup");
        BtnCloseUrl.setToolTipText("Alt+T");
        BtnCloseUrl.setName("BtnCloseUrl"); // NOI18N
        BtnCloseUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseUrlActionPerformed(evt);
            }
        });
        panelisi6.add(BtnCloseUrl);
        BtnCloseUrl.setBounds(450, 40, 100, 30);

        BtnBukaURL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnBukaURL.setMnemonic('B');
        BtnBukaURL.setText("Buka URL");
        BtnBukaURL.setToolTipText("Alt+B");
        BtnBukaURL.setName("BtnBukaURL"); // NOI18N
        BtnBukaURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBukaURLActionPerformed(evt);
            }
        });
        panelisi6.add(BtnBukaURL);
        BtnBukaURL.setBounds(10, 40, 105, 30);

        jLabel40.setText("URL :");
        jLabel40.setName("jLabel40"); // NOI18N
        panelisi6.add(jLabel40);
        jLabel40.setBounds(0, 10, 40, 23);

        URLSertisign.setEditable(false);
        URLSertisign.setHighlighter(null);
        URLSertisign.setName("URLSertisign"); // NOI18N
        panelisi6.add(URLSertisign);
        URLSertisign.setBounds(44, 10, 505, 23);

        BtnDownloadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnDownloadFile.setMnemonic('D');
        BtnDownloadFile.setText("Download File");
        BtnDownloadFile.setToolTipText("Alt+D");
        BtnDownloadFile.setName("BtnDownloadFile"); // NOI18N
        BtnDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadFile);
        BtnDownloadFile.setBounds(125, 40, 130, 30);

        BtnDownloadBukaFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/preview-16x16.png"))); // NOI18N
        BtnDownloadBukaFile.setMnemonic('F');
        BtnDownloadBukaFile.setText("Download & Buka File");
        BtnDownloadBukaFile.setToolTipText("Alt+F");
        BtnDownloadBukaFile.setName("BtnDownloadBukaFile"); // NOI18N
        BtnDownloadBukaFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDownloadBukaFileActionPerformed(evt);
            }
        });
        panelisi6.add(BtnDownloadBukaFile);
        BtnDownloadBukaFile.setBounds(265, 40, 175, 30);

        internalFrame9.add(panelisi6, java.awt.BorderLayout.CENTER);

        WindowURLSertisign.getContentPane().add(internalFrame9, java.awt.BorderLayout.CENTER);

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnGeneratePDF.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDF.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDF.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDF.setText("Jadikan File PDF ");
        MnGeneratePDF.setName("MnGeneratePDF"); // NOI18N
        MnGeneratePDF.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDF);

        MnGeneratePDFSertiSign.setBackground(new java.awt.Color(255, 255, 254));
        MnGeneratePDFSertiSign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnGeneratePDFSertiSign.setForeground(new java.awt.Color(50, 50, 50));
        MnGeneratePDFSertiSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnGeneratePDFSertiSign.setText("Jadikan File PDF SertiSign");
        MnGeneratePDFSertiSign.setName("MnGeneratePDFSertiSign"); // NOI18N
        MnGeneratePDFSertiSign.setPreferredSize(new java.awt.Dimension(220, 26));
        MnGeneratePDFSertiSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnGeneratePDFSertiSignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnGeneratePDFSertiSign);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-09-2026 12:18:05" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);

        Status.setText("jTextField1");
        Status.setName("Status"); // NOI18N

        Umur.setText("jTextField1");
        Umur.setName("Umur"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Riwayat/Rincian Tindakan/Terapi Pasien ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        R4.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R4);
        R4.setSelected(true);
        R4.setText("Nomor :");
        R4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R4.setName("R4"); // NOI18N
        R4.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass5.add(R4);

        NoRawat.setMinimumSize(new java.awt.Dimension(100, 24));
        NoRawat.setName("NoRawat"); // NOI18N
        NoRawat.setPreferredSize(new java.awt.Dimension(150, 23));
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });
        panelGlass5.add(NoRawat);

        jLabel41.setText("DPJP Ranap :");
        jLabel41.setName("jLabel41"); // NOI18N
        jLabel41.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass5.add(jLabel41);

        KdPeg2.setEnabled(false);
        KdPeg2.setHighlighter(null);
        KdPeg2.setName("KdPeg2"); // NOI18N
        KdPeg2.setPreferredSize(new java.awt.Dimension(50, 24));
        KdPeg2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KdPeg2ActionPerformed(evt);
            }
        });
        KdPeg2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPeg2KeyPressed(evt);
            }
        });
        panelGlass5.add(KdPeg2);

        TPegawai2.setEditable(false);
        TPegawai2.setEnabled(false);
        TPegawai2.setHighlighter(null);
        TPegawai2.setName("TPegawai2"); // NOI18N
        TPegawai2.setPreferredSize(new java.awt.Dimension(100, 24));
        TPegawai2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TPegawai2ActionPerformed(evt);
            }
        });
        panelGlass5.add(TPegawai2);

        BtnSeekPegawai1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekPegawai1.setMnemonic('4');
        BtnSeekPegawai1.setToolTipText("ALt+4");
        BtnSeekPegawai1.setEnabled(false);
        BtnSeekPegawai1.setName("BtnSeekPegawai1"); // NOI18N
        BtnSeekPegawai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekPegawai1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnSeekPegawai1);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('2');
        BtnCari1.setToolTipText("Alt+2");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnCari1);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setEnabled(false);
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnAll);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("PRINT ");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(80, 23));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("KELUAR");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass5.add(BtnKeluar);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/file-edit-16x16.png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setText("Set Status TTE");
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(150, 23));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint1);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame2.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame2.setBorder(null);
        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        LoadHTMLRiwayatPerawatan.setBorder(null);
        LoadHTMLRiwayatPerawatan.setComponentPopupMenu(jPopupMenu1);
        LoadHTMLRiwayatPerawatan.setName("LoadHTMLRiwayatPerawatan"); // NOI18N
        Scroll.setViewportView(LoadHTMLRiwayatPerawatan);

        internalFrame2.add(Scroll, java.awt.BorderLayout.CENTER);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(275, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout());

        ChkAccor.setBackground(new java.awt.Color(255, 250, 250));
        ChkAccor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(250, 255, 248)));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.EAST);

        ScrollMenu.setBorder(null);
        ScrollMenu.setName("ScrollMenu"); // NOI18N
        ScrollMenu.setOpaque(true);
        ScrollMenu.setPreferredSize(new java.awt.Dimension(255, 3000));

        FormMenu.setBackground(new java.awt.Color(255, 255, 255));
        FormMenu.setBorder(null);
        FormMenu.setName("FormMenu"); // NOI18N
        FormMenu.setPreferredSize(new java.awt.Dimension(255, 3000));
        FormMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 1));

        chkRawatJalan.setSelected(true);
        chkRawatJalan.setText("Rawat Jalan");
        chkRawatJalan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkRawatJalan.setName("chkRawatJalan"); // NOI18N
        chkRawatJalan.setOpaque(false);
        chkRawatJalan.setPreferredSize(new java.awt.Dimension(245, 22));
        chkRawatJalan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkRawatJalanItemStateChanged(evt);
            }
        });
        FormMenu.add(chkRawatJalan);

        chkRawatInap.setText("Rawat Inap");
        chkRawatInap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkRawatInap.setName("chkRawatInap"); // NOI18N
        chkRawatInap.setOpaque(false);
        chkRawatInap.setPreferredSize(new java.awt.Dimension(245, 22));
        chkRawatInap.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkRawatInapItemStateChanged(evt);
            }
        });
        FormMenu.add(chkRawatInap);

        chkSepBpjs.setText("SEP BPJS");
        chkSepBpjs.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSepBpjs.setName("chkSepBpjs"); // NOI18N
        chkSepBpjs.setOpaque(false);
        chkSepBpjs.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSepBpjs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSepBpjsActionPerformed(evt);
            }
        });
        FormMenu.add(chkSepBpjs);

        chkResume.setText("Resume");
        chkResume.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkResume.setName("chkResume"); // NOI18N
        chkResume.setOpaque(false);
        chkResume.setPreferredSize(new java.awt.Dimension(245, 22));
        chkResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkResumeActionPerformed(evt);
            }
        });
        FormMenu.add(chkResume);

        chkOperasiVK.setText("Laporan Operasi/VK");
        chkOperasiVK.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkOperasiVK.setName("chkOperasiVK"); // NOI18N
        chkOperasiVK.setOpaque(false);
        chkOperasiVK.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkOperasiVK);

        chkTriase.setText("Triase IGD");
        chkTriase.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkTriase.setName("chkTriase"); // NOI18N
        chkTriase.setOpaque(false);
        chkTriase.setPreferredSize(new java.awt.Dimension(245, 22));
        chkTriase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTriaseActionPerformed(evt);
            }
        });
        FormMenu.add(chkTriase);

        chkFormAssesment.setText("Asuhan Medis IGD");
        chkFormAssesment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkFormAssesment.setName("chkFormAssesment"); // NOI18N
        chkFormAssesment.setOpaque(false);
        chkFormAssesment.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkFormAssesment);

        chkAsuhanKeperawatanRanapNeonatus.setText("Awal Keperawatan Ranap Neonatus");
        chkAsuhanKeperawatanRanapNeonatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkAsuhanKeperawatanRanapNeonatus.setName("chkAsuhanKeperawatanRanapNeonatus"); // NOI18N
        chkAsuhanKeperawatanRanapNeonatus.setOpaque(false);
        chkAsuhanKeperawatanRanapNeonatus.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkAsuhanKeperawatanRanapNeonatus);

        chkKonsultasiMedik.setText("Konsultasi Medik");
        chkKonsultasiMedik.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkKonsultasiMedik.setName("chkKonsultasiMedik"); // NOI18N
        chkKonsultasiMedik.setOpaque(false);
        chkKonsultasiMedik.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkKonsultasiMedik);

        chkUjiFungsiKFR.setText("Uji Fungsi/Prosedur KFR");
        chkUjiFungsiKFR.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkUjiFungsiKFR.setName("chkUjiFungsiKFR"); // NOI18N
        chkUjiFungsiKFR.setOpaque(false);
        chkUjiFungsiKFR.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkUjiFungsiKFR);

        chkLayananKedokteranFisikRehabilitasi.setText("Layanan Kedokteran Fisik Rehabilitasi");
        chkLayananKedokteranFisikRehabilitasi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkLayananKedokteranFisikRehabilitasi.setName("chkLayananKedokteranFisikRehabilitasi"); // NOI18N
        chkLayananKedokteranFisikRehabilitasi.setOpaque(false);
        chkLayananKedokteranFisikRehabilitasi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkLayananKedokteranFisikRehabilitasi);

        chkLayananProgramKFR.setText("Layanan Program KFR");
        chkLayananProgramKFR.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkLayananProgramKFR.setName("chkLayananProgramKFR"); // NOI18N
        chkLayananProgramKFR.setOpaque(false);
        chkLayananProgramKFR.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkLayananProgramKFR);

        chkHasilRad.setText("Hasil Radiologi");
        chkHasilRad.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilRad.setName("chkHasilRad"); // NOI18N
        chkHasilRad.setOpaque(false);
        chkHasilRad.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilRad);

        chkHasilLab.setText("Hasil Laborat");
        chkHasilLab.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilLab.setName("chkHasilLab"); // NOI18N
        chkHasilLab.setOpaque(false);
        chkHasilLab.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilLab);

        chkHasilUSG.setText("Hasil USG");
        chkHasilUSG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkHasilUSG.setName("chkHasilUSG"); // NOI18N
        chkHasilUSG.setOpaque(false);
        chkHasilUSG.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkHasilUSG);

        chkBerkasDigital.setText("Berkas Upload File");
        chkBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkBerkasDigital.setName("chkBerkasDigital"); // NOI18N
        chkBerkasDigital.setOpaque(false);
        chkBerkasDigital.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkBerkasDigital);

        chkRiwayatSOAPIE.setText("Riwayat S.O.A.P.I.E");
        chkRiwayatSOAPIE.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkRiwayatSOAPIE.setName("chkRiwayatSOAPIE"); // NOI18N
        chkRiwayatSOAPIE.setOpaque(false);
        chkRiwayatSOAPIE.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkRiwayatSOAPIE);

        chkSuratKontrolBiasa.setText("Surat Kontrol");
        chkSuratKontrolBiasa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSuratKontrolBiasa.setName("chkSuratKontrolBiasa"); // NOI18N
        chkSuratKontrolBiasa.setOpaque(false);
        chkSuratKontrolBiasa.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkSuratKontrolBiasa);

        chkSuratKontrol.setText("SKDP BPJS");
        chkSuratKontrol.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSuratKontrol.setName("chkSuratKontrol"); // NOI18N
        chkSuratKontrol.setOpaque(false);
        chkSuratKontrol.setPreferredSize(new java.awt.Dimension(245, 22));
        chkSuratKontrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSuratKontrolActionPerformed(evt);
            }
        });
        FormMenu.add(chkSuratKontrol);

        chkSPR.setText("SPR Surat Permintaan Rawat");
        chkSPR.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSPR.setName("chkSPR"); // NOI18N
        chkSPR.setOpaque(false);
        chkSPR.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkSPR);

        chkSuratKelahiranBayi.setText("Surat Kelahiran Bayi");
        chkSuratKelahiranBayi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkSuratKelahiranBayi.setName("chkSuratKelahiranBayi"); // NOI18N
        chkSuratKelahiranBayi.setOpaque(false);
        chkSuratKelahiranBayi.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkSuratKelahiranBayi);

        chkTransferAntarRuang.setText("Transfer Pasien Antar Ruang");
        chkTransferAntarRuang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkTransferAntarRuang.setName("chkTransferAntarRuang"); // NOI18N
        chkTransferAntarRuang.setOpaque(false);
        chkTransferAntarRuang.setPreferredSize(new java.awt.Dimension(245, 22));
        chkTransferAntarRuang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTransferAntarRuangActionPerformed(evt);
            }
        });
        FormMenu.add(chkTransferAntarRuang);

        chkBilling.setText("Billing");
        chkBilling.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkBilling.setName("chkBilling"); // NOI18N
        chkBilling.setOpaque(false);
        chkBilling.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkBilling);

        chkResep.setText("Resep");
        chkResep.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkResep.setName("chkResep"); // NOI18N
        chkResep.setOpaque(false);
        chkResep.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkResep);

        chkPemberianObat.setText("Jadwal Pemberian Obat");
        chkPemberianObat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chkPemberianObat.setName("chkPemberianObat"); // NOI18N
        chkPemberianObat.setOpaque(false);
        chkPemberianObat.setPreferredSize(new java.awt.Dimension(245, 22));
        FormMenu.add(chkPemberianObat);

        ScrollMenu.setViewportView(FormMenu);

        PanelAccor.add(ScrollMenu, java.awt.BorderLayout.CENTER);

        internalFrame2.add(PanelAccor, java.awt.BorderLayout.WEST);

        TabRawat.addTab("Generate Berkas Klaim", internalFrame2);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setSelected(true);
        ChkInput.setText(".: Tampilkan/Sembunyikan Data Pasien");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 104));
        FormInput.setLayout(null);

        label17.setText("Pasien :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label17);
        label17.setBounds(5, 10, 55, 23);

        NoRM.setName("NoRM"); // NOI18N
        NoRM.setPreferredSize(new java.awt.Dimension(100, 23));
        NoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRMKeyPressed(evt);
            }
        });
        FormInput.add(NoRM);
        NoRM.setBounds(64, 10, 100, 23);

        NmPasien.setEditable(false);
        NmPasien.setName("NmPasien"); // NOI18N
        NmPasien.setPreferredSize(new java.awt.Dimension(220, 23));
        FormInput.add(NmPasien);
        NmPasien.setBounds(167, 10, 220, 23);

        BtnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPasien.setMnemonic('3');
        BtnPasien.setToolTipText("Alt+3");
        BtnPasien.setName("BtnPasien"); // NOI18N
        BtnPasien.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPasienActionPerformed(evt);
            }
        });
        BtnPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPasienKeyPressed(evt);
            }
        });
        FormInput.add(BtnPasien);
        BtnPasien.setBounds(390, 10, 28, 23);

        label20.setText("J.K. :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label20);
        label20.setBounds(436, 10, 30, 23);

        Jk.setEditable(false);
        Jk.setName("Jk"); // NOI18N
        Jk.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Jk);
        Jk.setBounds(470, 10, 40, 23);

        label21.setText("Tempat & Tgl.Lahir :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label21);
        label21.setBounds(523, 10, 110, 23);

        TempatLahir.setEditable(false);
        TempatLahir.setName("TempatLahir"); // NOI18N
        TempatLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TempatLahir);
        TempatLahir.setBounds(637, 10, 140, 23);

        label22.setText("Alamat :");
        label22.setName("label22"); // NOI18N
        label22.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label22);
        label22.setBounds(5, 40, 55, 23);

        Alamat.setEditable(false);
        Alamat.setName("Alamat"); // NOI18N
        Alamat.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Alamat);
        Alamat.setBounds(64, 40, 354, 23);

        label23.setText("G.D. :");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label23);
        label23.setBounds(436, 40, 30, 23);

        GD.setEditable(false);
        GD.setName("GD"); // NOI18N
        GD.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(GD);
        GD.setBounds(470, 40, 40, 23);

        label24.setText("Nama Ibu Kandung :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label24);
        label24.setBounds(523, 40, 110, 23);

        IbuKandung.setEditable(false);
        IbuKandung.setName("IbuKandung"); // NOI18N
        IbuKandung.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(IbuKandung);
        IbuKandung.setBounds(637, 40, 225, 23);

        TanggalLahir.setEditable(false);
        TanggalLahir.setName("TanggalLahir"); // NOI18N
        TanggalLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TanggalLahir);
        TanggalLahir.setBounds(779, 10, 83, 23);

        label25.setText("Agama :");
        label25.setName("label25"); // NOI18N
        label25.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label25);
        label25.setBounds(5, 70, 55, 23);

        Agama.setEditable(false);
        Agama.setName("Agama"); // NOI18N
        Agama.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Agama);
        Agama.setBounds(64, 70, 100, 23);

        StatusNikah.setEditable(false);
        StatusNikah.setName("StatusNikah"); // NOI18N
        StatusNikah.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(StatusNikah);
        StatusNikah.setBounds(245, 70, 100, 23);

        label26.setText("Stts.Nikah :");
        label26.setName("label26"); // NOI18N
        label26.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label26);
        label26.setBounds(176, 70, 65, 23);

        Pendidikan.setEditable(false);
        Pendidikan.setName("Pendidikan"); // NOI18N
        Pendidikan.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Pendidikan);
        Pendidikan.setBounds(429, 70, 80, 23);

        label27.setText("Pendidikan :");
        label27.setName("label27"); // NOI18N
        label27.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label27);
        label27.setBounds(355, 70, 70, 23);

        label28.setText("Bahasa :");
        label28.setName("label28"); // NOI18N
        label28.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label28);
        label28.setBounds(520, 70, 50, 23);

        Bahasa.setEditable(false);
        Bahasa.setName("Bahasa"); // NOI18N
        Bahasa.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Bahasa);
        Bahasa.setBounds(574, 70, 100, 23);

        label29.setText("Cacat Fisik :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label29);
        label29.setBounds(683, 70, 70, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setName("CacatFisik"); // NOI18N
        CacatFisik.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(757, 70, 105, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        } else {
//            Valid.pindah(evt,Tgl1,NoRM);
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

private void NoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRMKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
        isPasien();
    } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
        isPasien();
        BtnKeluar.requestFocus();
    } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
        BtnPasienActionPerformed(null);
    } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        isPasien();
        BtnPrint.requestFocus();
    }
}//GEN-LAST:event_NoRMKeyPressed

private void BtnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPasienActionPerformed
    if (akses.getpasien() == true) {
        pasien.isCek();
        pasien.emptTeks();
        pasien.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(true);
    }
}//GEN-LAST:event_BtnPasienActionPerformed

private void BtnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPasienKeyPressed
    //Valid.pindah(evt,Tgl2,TKd);
}//GEN-LAST:event_BtnPasienKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if (NoRM.getText().trim().isEmpty() || NmPasien.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pasien masih kosong...!!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    panggilLaporan(LoadHTMLRiwayatPerawatan.getText());
                    break;
                default:
                    break;
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        if (NoRM.getText().trim().isEmpty() || NmPasien.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Pasien masih kosong...!!!");
        } else {
            jadwalkanTampilPerawatan();
        }
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCari1ActionPerformed(null);
        }
    }//GEN-LAST:event_NoRawatKeyPressed

    private void KdPeg2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KdPeg2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPeg2ActionPerformed

    private void KdPeg2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPeg2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            Sequel.cariIsi("select pegawai.nama from pegawai where pegawai.nik=?", TPegawai2, KdPeg2.getText());
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            BtnSeekPegawai1ActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
    }//GEN-LAST:event_KdPeg2KeyPressed

    private void TPegawai2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TPegawai2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TPegawai2ActionPerformed

    private void BtnSeekPegawai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPegawai1ActionPerformed
        akses.setform("RMRiwayatPrawatan");
        pegawai.emptTeks();
        pegawai.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        pegawai.setLocationRelativeTo(internalFrame1);
        pegawai.setVisible(true);
    }//GEN-LAST:event_BtnSeekPegawai1ActionPerformed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed

    }//GEN-LAST:event_BtnAllActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        BtnCari1ActionPerformed(null);
    }//GEN-LAST:event_TabRawatMouseClicked

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
        isMenu();
    }//GEN-LAST:event_ChkAccorActionPerformed

    private void chkResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkResumeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkResumeActionPerformed

    private void chkTriaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTriaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkTriaseActionPerformed

    private void BtnCloseUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseUrlActionPerformed
        URLSertisign.setText("");
        WindowURLSertisign.dispose();
    }//GEN-LAST:event_BtnCloseUrlActionPerformed

    private void BtnBukaURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBukaURLActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(URLSertisign.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "File belum tersedia, silahkan tunggu beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnBukaURLActionPerformed

    private void BtnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("RPP" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "RPP" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane, "File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadFileActionPerformed

    private void BtnDownloadBukaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadBukaFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("RPP" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "RPP" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf");
            Desktop.getDesktop().browse(new File("RPP" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf").toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane, "File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadBukaFileActionPerformed

    private JDialog buatDialogLoadingPdf(String pesan) {
        JDialog dialog = new JDialog(this, false);
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
        javax.swing.JLabel label = new javax.swing.JLabel(pesan);
        javax.swing.JLabel detail = new javax.swing.JLabel("Mohon tunggu, sistem sedang menyiapkan dokumen klaim.");
        javax.swing.JProgressBar progress = new javax.swing.JProgressBar();

        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
        progress.setIndeterminate(true);
        label.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
        detail.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11));
        panel.setBackground(Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(46, 139, 87)),
                javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        panel.add(label, java.awt.BorderLayout.NORTH);
        panel.add(progress, java.awt.BorderLayout.CENTER);
        panel.add(detail, java.awt.BorderLayout.SOUTH);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        return dialog;
    }

    private void jalankanProsesPdfDenganLoading(final String pesan, final Runnable proses) {
        if (prosesPdfBerjalan) {
            JOptionPane.showMessageDialog(null, "Proses pembuatan PDF masih berjalan, mohon tunggu sebentar...!!!");
            return;
        }

        prosesPdfBerjalan = true;
        final JDialog dialog = buatDialogLoadingPdf(pesan);
        final Cursor cursorLama = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MnGeneratePDF.setEnabled(false);
        MnGeneratePDFSertiSign.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Exception error;

            @Override
            protected Void doInBackground() {
                try {
                    proses.run();
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();
                prosesPdfBerjalan = false;
                MnGeneratePDF.setEnabled(true);
                MnGeneratePDFSertiSign.setEnabled(true);
                setCursor(cursorLama);
                if (error != null) {
                    System.out.println("Notifikasi : " + error);
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Proses pembuatan PDF gagal : " + error.getMessage());
                }
            }
        };
        worker.execute();
        dialog.setVisible(true);
    }

    private boolean kontenKlaimSudahSiapUntukPdf() {
        String isi = LoadHTMLRiwayatPerawatan.getText();
        return isi != null
                && isi.trim().length() > 0
                && !isi.contains("Memuat berkas klaim")
                && !isi.contains("Pasien masih kosong");
    }

    private void pastikanKontenKlaimSiapUntukPdf() {
        if (!kontenKlaimSudahSiapUntukPdf()) {
            long mulai = System.currentTimeMillis();
            tampilPerawatan();
            System.out.println("Generate Klaim PDF reload data : " + (System.currentTimeMillis() - mulai) + " ms");
        } else {
            System.out.println("Generate Klaim PDF memakai HTML yang sudah tampil.");
        }
    }

    private File buatFilePreviewPdfKlaim() {
        File folderTemp = new File(System.getProperty("java.io.tmpdir"), "generate-klaim");
        if (!folderTemp.exists()) {
            folderTemp.mkdirs();
        }

        File filePdf = new File(folderTemp, ambilNomorSepUntukNamaFile() + ".pdf");
        if (filePdf.exists()) {
            filePdf.delete();
        }
        return filePdf;
    }

    private String ambilNomorSepUntukNamaFile() {
        String noRawat = NoRawat.getText().trim();
        String noSep = Sequel.cariIsi(
                "select no_sep from bridging_sep where no_rawat=? "
                + "order by case when jnspelayanan like '1%' or jnspelayanan like '%Ranap%' then 0 else 1 end, "
                + "tglsep desc, no_sep desc limit 1", noRawat);
        String sumber = noSep == null || noSep.trim().equals("") ? noRawat.replaceAll("[^0-9]", "") : noSep.trim();
        sumber = sumber.replaceAll("[\\\\/:*?\"<>|]", "");
        return sumber.equals("") ? "generate-klaim" : sumber;
    }

    private void MnGeneratePDFSertiSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFSertiSignActionPerformed

        if (SwingUtilities.isEventDispatchThread()) {
            R4.setSelected(true);
            if (NoRawat.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "No.Rawat masih kosong...!!!");
                NoRawat.requestFocus();
                return;
            }
            jalankanProsesPdfDenganLoading("Sedang membuat PDF SertiSign...", new Runnable() {
                @Override
                public void run() {
                    MnGeneratePDFSertiSignActionPerformed(null);
                }
            });
            return;
        }

        R4.setSelected(true);
        if (NoRawat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No.Rawat masih kosong...!!!");
            NoRawat.requestFocus();
        } else {
            long mulaiProsesPdf = System.currentTimeMillis();
            try {
                esign = false;
                sertisign = true;
                pastikanKontenKlaimSiapUntukPdf();
                if (sertisign == true) {
                    File g = new File("file.css");
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(
                            "@page { size: legal; margin: 15mm 10mm; } "
                            + "body { font-family: 'Segoe UI', Tahoma, Arial, sans-serif; font-size: 10px; line-height: 1.1; margin: 0; padding: 0; color: #333; } "
                            + "table { border-collapse: collapse; width: 100%; margin-bottom: 4px; } "
                            + "td, th { padding: 2px 4px; vertical-align: top; font-size: 9px; } "
                            // ==== TAMBAHKAN CSS PAGE BREAK INI ====
                            + ".pagebreak { page-break-after: always; clear: both; } "
                            + ".berkas-page { page-break-before: always; break-before: page; } "
                            + ".berkas-page-end { page-break-after: auto; break-after: auto; clear: both; } "
                            + ".page-break-after { page-break-after: always; } "
                            + ".page-break-before { page-break-before: always; } "
                            + ".page-break-avoid { page-break-inside: avoid; } "
                            + ".no-page-break { page-break-inside: avoid; } "
                            + "@media print { "
                            + "  .pagebreak { page-break-after: always !important; } "
                            + "  .berkas-page { page-break-before: always !important; break-before: page !important; } "
                            + "  .berkas-page-end { page-break-after: auto !important; break-after: auto !important; } "
                            + "  .page-break-after { page-break-after: always !important; } "
                            + "  .page-break-before { page-break-before: always !important; } "
                            + "  .page-break-avoid { page-break-inside: avoid !important; } "
                            + "} "
                            // ====================================

                            + ".main-table { border: 1px solid #000; margin-bottom: 5px; } "
                            + ".main-table td { border: none; padding: 3px 5px; font-size: 9px; } "
                            + ".main-table .label { font-weight: bold; width: 25%; } "
                            + ".main-table .colon { width: 2%; text-align: center; } "
                            + ".main-table .value { width: 73%; } "
                            + ".header-section { text-align: center; margin-bottom: 6px; } "
                            + ".header-section .title { font-size: 12px; font-weight: bold; margin-bottom: 2px; } "
                            + ".header-section .subtitle { font-size: 11px; font-weight: bold; } "
                            + ".logo-section { text-align: left; vertical-align: middle; } "
                            + ".barcode-section { text-align: right; vertical-align: middle; } "
                            + ".isi td { border-right: 1px solid #e2e7dd; font: 8.5px Tahoma; height: 11px; border-bottom: 1px solid #e2e7dd; background: #ffffff; color: #323232; padding: 1px 3px; } "
                            + ".isi a { text-decoration: none; color: #8b9b95; padding: 0; font-family: Tahoma; font-size: 8.5px; } "
                            + ".footer-text { font-size: 8px; margin-top: 3px; } "
                            + ".disclaimer { font-style: italic; margin-top: 3px; font-size: 8px; } "
                            + ".signature-section { margin-top: 20px; } "
                            + ".signature-table { border: none; } "
                            + ".signature-table td { border: none; padding: 8px; } "
                            + ".signature-area { text-align: center; min-height: 150px; } "
                            + ".signature-title { font-weight: bold; font-size: 10px; margin-bottom: 8px; } "
                            + ".signature-role { font-size: 9px; margin-bottom: 40px; } "
                            + ".signature-marker { font-size: 12px; font-weight: normal; margin: 40px 0; } "
                            + ".signature-name { font-size: 9px; font-weight: bold; border-top: 1px solid #000; padding-top: 6px; margin-top: 20px; } "
                            + ".center { text-align: center; } "
                            + ".bold { font-weight: bold; } "
                            + ".no-border { border: none !important; } "
                            + ".qr-code { width: 65px; height: 65px; margin: 6px 0; } "
                            + ".spacer { margin-top: 4px; } "
                            + ".small-spacer { margin-top: 2px; } "
                            + ".compact { margin-bottom: 2px; } "
                            + ".sep-section { margin-bottom: 4px; } "
                            + ".srk-section { margin-bottom: 4px; } "
                            + "h1, h2, h3, h4, h5, h6 { margin: 2px 0; font-size: 11px; } "
                            + "p { margin: 1px 0; font-size: 9px; line-height: 1.1; } "
                            + "div { margin: 1px 0; line-height: 1.1; } "
                            + ".document-title { font-size: 11px; font-weight: bold; margin: 2px 0; } "
                            + ".document-content { font-size: 9px; line-height: 1.1; } "
                            + ".bpjs-header { font-size: 10px; font-weight: bold; margin: 2px 0; } "
                            + ".bpjs-content { font-size: 9px; margin: 1px 0; } "
                            + "img { max-width: 100%; height: auto; } "
                            + ".logo-img { width: auto; height: auto; max-width: 80px; max-height: 80px; } "
                            + ".barcode-img { width: auto; height: auto; max-width: 120px; max-height: 40px; } "
                            + ".qr-img { width: 65px; height: 65px; } "
                            + ".tight-spacing { margin: 0; padding: 0; line-height: 1.0; } "
                            + ".minimal-margin { margin: 1px 0; } "
                            + ".compressed-content { margin: 0; padding: 1px; line-height: 1.0; }"
                    );
                    bg.close();

                    PdfWriter pdf = new PdfWriter("Klaim" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf");

                    // Ambil konten HTML dari LoadHTMLRiwayatPerawatan
                    StringBuilder htmlContent = new StringBuilder(LoadHTMLRiwayatPerawatan.getText());

                    // Tambahkan tanda tangan berdasarkan status rawat
                    try {
                        PreparedStatement ps = koneksi.prepareStatement(
                                "select reg_periksa.no_rawat, reg_periksa.kd_dokter, dokter.nm_dokter, reg_periksa.status_lanjut "
                                + "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "
                                + "where reg_periksa.no_rawat=?"
                        );
                        ps.setString(1, NoRawat.getText().trim());
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            // Tambahkan section signature tanpa kotak dan garis
                            htmlContent.append(
                                    "<div style='margin-top: 30px;'>"
                                    + "<table width='100%' border='0' cellpadding='0' cellspacing='0'>"
                                    + "<tr>"
                                    + "<td width='30%'></td>"
                                    + "<td width='70%' style='text-align: center; padding: 20px;'>"
                                    + "<div style='font-weight: bold; font-size: 11px; margin-bottom: 5px;'>"
                                    + "Tanda Tangan/Verifikasi"
                                    + "</div>"
                                    + "<div style='font-size: 10px; margin-bottom: 10px;'>"
                                    + "Dokter Poli<br><br><br><br>"
                                    + "#1A<br>"
                            );

                            if (rs.getString("status_lanjut").equals("Ralan")) {
                                if (akses.getkode().equals(rs.getString("kd_dokter"))) {
                                    htmlContent.append(
                                            "<div style='font-size: 10px; font-weight: bold; margin-top: 30px;'>"
                                            + rs.getString("nm_dokter")
                                            + "</div>"
                                    );
                                } else {
                                    sertisign = false;
                                    JOptionPane.showMessageDialog(null, "Harus dilakukan oleh Dokter Poli yang menangani pasien...!!!!");
                                }
                            } else if (rs.getString("status_lanjut").equals("Ranap")) {
                                System.out.println("ESign : " + esign + " Sertisign : " + sertisign);
                                try {
                                    PreparedStatement ps3 = koneksi.prepareStatement(
                                            "select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=?"
                                    );
                                    ps3.setString(1, rs.getString("no_rawat"));
                                    ResultSet rs3 = ps3.executeQuery();

                                    if (rs3.next()) {
                                        // Tutup div sebelumnya
                                        htmlContent.append("</div></td></tr></table>");

                                        // Buat section baru untuk multiple DPJP tanpa kotak
                                        htmlContent.append(
                                                "<div style='text-align: center; margin-top: 30px; padding: 20px;'>"
                                                + "<div style='font-weight: bold; font-size: 11px; margin-bottom: 10px;'>"
                                                + "Tanda Tangan/Verifikasi"
                                                + "</div>"
                                                + "<table width='100%' border='0' cellpadding='0' cellspacing='0'>"
                                                + "<tr>"
                                        );

                                        int urutdpjp = 1;
                                        int totalDpjp = 0;

                                        // Hitung total DPJP
                                        do {
                                            totalDpjp++;
                                        } while (rs3.next());

                                        // Reset ResultSet
                                        rs3.close();
                                        ps3.close();
                                        ps3 = koneksi.prepareStatement(
                                                "select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=?"
                                        );
                                        ps3.setString(1, rs.getString("no_rawat"));
                                        rs3 = ps3.executeQuery();

                                        sertisign = false;
                                        int cellWidth = 100 / totalDpjp;

                                        while (rs3.next()) {
                                            htmlContent.append(
                                                    "<td width='" + cellWidth + "%' style='text-align: center; vertical-align: middle; padding: 15px;'>"
                                                    + "<div style='font-size: 10px; margin-bottom: 5px;'>"
                                                    + "Dokter DPJP " + urutdpjp + "<br>"
                                                    + "#1A<br>"
                                                    + "</div>"
                                            );

                                            if (rs3.getString("kd_dokter").equals(akses.getkode())) {
                                                htmlContent.append(
                                                        "<div style='font-size: 10px; font-weight: bold; margin-top: 30px;'>"
                                                        + rs3.getString("nm_dokter")
                                                        + "</div>"
                                                );
                                                sertisign = true;
                                            } else {
                                                GetMethod get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + rs3.getString("kd_dokter").replace(" ", "_"));
                                                http.executeMethod(get);
                                                htmlContent.append(
                                                        "<img width='80' height='80' src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + rs3.getString("kd_dokter") + ".png' style='margin: 20px 0;'/>"
                                                        + "<div style='font-size: 10px; font-weight: bold; margin-top: 15px;'>"
                                                        + rs3.getString("nm_dokter")
                                                        + "</div>"
                                                );
                                            }

                                            htmlContent.append("</td>");
                                            urutdpjp++;
                                        }

                                        htmlContent.append("</tr></table></div>");
                                        rs3.close();
                                        ps3.close();
                                    } else {
                                        if (akses.getkode().equals(rs.getString("kd_dokter"))) {
                                            htmlContent.append(
                                                    "</div>"
                                                    + "<div style='font-size: 10px; margin-bottom: 10px;'>"
                                                    + "Dokter DPJP<br>"
                                                    + "#1A<br>"
                                                    + "</div>"
                                                    + "<div style='font-size: 10px; font-weight: bold; margin-top: 30px;'>"
                                                    + rs.getString("nm_dokter")
                                                    + "</div>"
                                            );
                                        } else {
                                            sertisign = false;
                                            JOptionPane.showMessageDialog(null, "Harus dilakukan oleh Dokter Poli yang menangani pasien...!!!!");
                                        }
                                    }
                                } catch (Exception ex) {
                                    System.out.println("Error getting DPJP: " + ex);
                                }
                            }

                            // Tutup struktur untuk single signature (jika bukan multiple DPJP)
                            if (!rs.getString("status_lanjut").equals("Ranap")
                                    || Sequel.cariIsi("select count(*) from dpjp_ranap where no_rawat='" + rs.getString("no_rawat") + "'").equals("0")) {
                                htmlContent.append("</div></td></tr></table>");
                            }

                            htmlContent.append("</div>");
                        }
                        rs.close();
                        ps.close();
                    } catch (Exception ex) {
                        System.out.println("Error processing signature: " + ex);
                    }

                    // Jika sertisign false, jangan lanjutkan proses
                    if (!sertisign) {
                        return;
                    }

                    // Convert HTML to PDF
                    ConverterProperties converterProperties = new ConverterProperties();
                    converterProperties.setImmediateFlush(false);
                    long mulaiConvert = System.currentTimeMillis();
                    HtmlConverter.convertToPdf(
                            htmlContent.toString()
                                    .replaceAll("<head>", "<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />")
                                    .replaceAll((getClass().getResource("/picture/")) + "", "./gambar/"),
                            pdf,
                            converterProperties
                    );
                    System.out.println("Generate Klaim PDF SertiSign convert iText : " + (System.currentTimeMillis() - mulaiConvert) + " ms");

                    File f = new File("Klaim" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf");
                    String documentName = f.getName();

                    try {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpPost post = new HttpPost(koneksiDB.URLAPISERTISIGN());
                        post.addHeader("apikey", koneksiDB.APIKEYSERTISIGN());
                        post.addHeader("Accept", "application/json");
                        json = Sequel.cariIsi("select dokter.email from dokter where dokter.kd_dokter=?", akses.getkode());
                        kddpjp = Sequel.cariIsi("select pegawai.no_ktp from pegawai where pegawai.nik=?", akses.getkode());
                        dpjp = Sequel.cariIsi("select pegawai.nama from pegawai where pegawai.nik=?", akses.getkode());
                        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                                .addBinaryBody("document", f, ContentType.APPLICATION_PDF, f.getName())
                                .addTextBody("signer", json)
                                .addTextBody("keyword", "#1A")
                                .addTextBody("qrValue", "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs() + "\nDitandatangani secara elektronik oleh " + dpjp + "\nID " + kddpjp + "\n" + Tanggal.getSelectedItem().toString())
                                .addTextBody("w", "60")
                                .addTextBody("h", "60");
                        HttpEntity entity = entityBuilder.build();
                        post.setEntity(entity);
                        System.out.println("URL Kirim File : " + koneksiDB.URLAPISERTISIGN());

                        try ( CloseableHttpResponse response = httpClient.execute(post)) {
                            if (response.getCode() == 200) {
                                json = EntityUtils.toString(response.getEntity());
                                System.out.println("Respon Kirim File : " + json);
                                root = mapper.readTree(json);
                                String transactionId = root.path("data").path("transaction_id").asText();
                                System.out.println("Id File : " + transactionId);
                                System.out.println("URL Callback File : " + koneksiDB.URLDOKUMENSERTISIGN() + "/" + transactionId + ".pdf");

                                String downloadUrl = koneksiDB.URLDOKUMENSERTISIGN() + "/" + transactionId + ".pdf";

                                String sep = Sequel.cariIsi("select no_sep from bridging_sep where no_rawat = '" + NoRawat.getText() + "' order by case when jnspelayanan like '1%' or jnspelayanan like '%Ranap%' then 0 else 1 end, tglsep desc, no_sep desc limit 1");
                                String tanggal = Sequel.cariIsi("select tglsep from bridging_sep where no_rawat = '" + NoRawat.getText() + "' order by case when jnspelayanan like '1%' or jnspelayanan like '%Ranap%' then 0 else 1 end, tglsep desc, no_sep desc limit 1");
                                String norawat = NoRawat.getText().trim();
                                String kode = Sequel.cariIsi("select kode from master_berkas_digital where nama = 'riwayat'");
                                String tanggaljam = getCurrentDateTime();
                                String inacbg = "INA-CBG";

                                String statusTteId = insertStatusTte(norawat, sep, transactionId, documentName,
                                        json, dpjp, kddpjp, downloadUrl);

                                autoDownloadWithRetry(downloadUrl, 10, sep, tanggal, norawat, kode,
                                        tanggaljam, inacbg, transactionId, statusTteId);

                                URLSertisign.setText(downloadUrl);
                                WindowURLSertisign.setAlwaysOnTop(true);
                                WindowURLSertisign.setLocationRelativeTo(internalFrame1);
                                WindowURLSertisign.setVisible(true);

                            } else {
                                System.out.println("Notifikasi : " + EntityUtils.toString(response.getEntity()));
                            }
                        } catch (Exception a) {
                            System.out.println("Notifikasi : " + a);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                System.out.println("Generate Klaim PDF SertiSign total : " + (System.currentTimeMillis() - mulaiProsesPdf) + " ms");
            }
        }


    }//GEN-LAST:event_MnGeneratePDFSertiSignActionPerformed

    private void chkRawatJalanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkRawatJalanItemStateChanged
        if (chkRawatJalan.isSelected() == true) {
            chkRawatInap.setSelected(false);
            chkTriase.setSelected(true);
            chkFormAssesment.setSelected(true);
            chkAsuhanKeperawatanRanapNeonatus.setSelected(false);
            chkTransferAntarRuang.setSelected(false);
            chkKonsultasiMedik.setSelected(true);
            chkUjiFungsiKFR.setSelected(true);
            chkLayananKedokteranFisikRehabilitasi.setSelected(true);
            chkLayananProgramKFR.setSelected(true);
            chkOperasiVK.setSelected(true);
            //chkLembarEklaim.setSelected(true);
            chkSepBpjs.setSelected(true);
            chkRiwayatSOAPIE.setSelected(false);
            chkSuratKontrol.setSelected(false);
            chkSuratKontrolBiasa.setSelected(true);
            chkResume.setSelected(true);
            chkHasilLab.setSelected(true);
            chkHasilRad.setSelected(true);
            chkHasilUSG.setSelected(true);
            chkResep.setSelected(true);
            chkBilling.setSelected(true);
            chkSPR.setSelected(false);
            chkSuratKelahiranBayi.setSelected(false);
        } else {
            chkTriase.setSelected(false);
            chkFormAssesment.setSelected(false);
            chkAsuhanKeperawatanRanapNeonatus.setSelected(false);
            chkTransferAntarRuang.setSelected(false);
            chkKonsultasiMedik.setSelected(false);
            chkUjiFungsiKFR.setSelected(false);
            chkLayananKedokteranFisikRehabilitasi.setSelected(false);
            chkLayananProgramKFR.setSelected(false);
            chkOperasiVK.setSelected(false);
            //chkLembarEklaim.setSelected(false);
            chkSepBpjs.setSelected(false);
            chkRiwayatSOAPIE.setSelected(false);
            chkSuratKontrol.setSelected(false);
            chkSuratKontrolBiasa.setSelected(false);
            chkResume.setSelected(false);
            chkHasilLab.setSelected(false);
            chkHasilRad.setSelected(false);
            chkHasilUSG.setSelected(false);
            chkResep.setSelected(false);
            chkBilling.setSelected(false);
            chkSPR.setSelected(false);
            chkSuratKelahiranBayi.setSelected(false);
        }
    }//GEN-LAST:event_chkRawatJalanItemStateChanged

    private void chkRawatInapItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkRawatInapItemStateChanged
        if (chkRawatInap.isSelected() == true) {
            chkRawatJalan.setSelected(false);
            chkTriase.setSelected(true);
            chkFormAssesment.setSelected(true);
            chkAsuhanKeperawatanRanapNeonatus.setSelected(true);
            chkTransferAntarRuang.setSelected(false);
            chkKonsultasiMedik.setSelected(true);
            chkUjiFungsiKFR.setSelected(true);
            chkLayananKedokteranFisikRehabilitasi.setSelected(false);
            chkLayananProgramKFR.setSelected(false);
            chkOperasiVK.setSelected(true);
            //chkLembarEklaim.setSelected(true);
            chkResume.setSelected(true);
            chkSepBpjs.setSelected(true);
            chkRiwayatSOAPIE.setSelected(false);
            chkSuratKontrol.setSelected(false);
            chkSuratKontrolBiasa.setSelected(true);
            chkHasilLab.setSelected(true);
            chkHasilRad.setSelected(true);
            chkHasilUSG.setSelected(true);
            chkBilling.setSelected(true);
            chkSPR.setSelected(true);
            chkSuratKelahiranBayi.setSelected(true);
        } else {
            chkTriase.setSelected(false);
            chkFormAssesment.setSelected(false);
            chkAsuhanKeperawatanRanapNeonatus.setSelected(false);
            chkTransferAntarRuang.setSelected(false);
            chkKonsultasiMedik.setSelected(false);
            chkUjiFungsiKFR.setSelected(false);
            chkLayananKedokteranFisikRehabilitasi.setSelected(false);
            chkLayananProgramKFR.setSelected(false);
            chkOperasiVK.setSelected(false);
            //chkLembarEklaim.setSelected(false);
            chkResume.setSelected(false);
            chkSepBpjs.setSelected(false);
            chkRiwayatSOAPIE.setSelected(false);
            chkSuratKontrol.setSelected(false);
            chkSuratKontrolBiasa.setSelected(false);
            chkHasilLab.setSelected(false);
            chkHasilRad.setSelected(false);
            chkHasilUSG.setSelected(false);
            chkBilling.setSelected(false);
            chkSPR.setSelected(false);
            chkSuratKelahiranBayi.setSelected(false);
        }
    }//GEN-LAST:event_chkRawatInapItemStateChanged

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void MnGeneratePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnGeneratePDFActionPerformed
        if (SwingUtilities.isEventDispatchThread()) {
            R4.setSelected(true);
            if (NoRawat.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "No.Rawat masih kosong...!!!");
                NoRawat.requestFocus();
                return;
            }
            jalankanProsesPdfDenganLoading("Sedang membuat file PDF...", new Runnable() {
                @Override
                public void run() {
                    MnGeneratePDFActionPerformed(null);
                }
            });
            return;
        }

        R4.setSelected(true);
        if (NoRawat.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "No.Rawat masih kosong...!!!");
            NoRawat.requestFocus();
        } else {
            long mulaiProsesPdf = System.currentTimeMillis();
            try {
                esign = false;
                sertisign = false;
                pastikanKontenKlaimSiapUntukPdf();

                // Buat file CSS dengan ukuran kertas Legal - OPTIMIZED UNTUK PDF
                File g = new File("file.css");
                BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                bg.write(
                        // CSS Dasar untuk kompatibilitas dengan iText PDF
                        ".isi td{border-right: 1px solid #e2e7dd;font: 12px tahoma;height:16px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"
                        + ".isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 12px;border: white;}"
                        + // Font base yang seragam
                        "body{font-family: Tahoma;font-size: 12px; margin: 0; padding: 10px;}"
                        + "table{font-size: 12px; border-collapse: collapse;}"
                        + "td{font-size: 12px;padding: 4px; vertical-align: top;}"
                        + "th{font-size: 12px;padding: 4px; vertical-align: top;}"
                        + "h1,h2,h3,h4{font-size: 14px; margin: 5px 0;}"
                        + "p{font-size: 12px; margin: 3px 0;}"
                        + "div{font-size: 12px;}"
                        + "span{font-size: 12px;}"
                        + // CSS khusus untuk SEP BPJS - OPTIMIZED UNTUK PDF
                        ".page-section {width: 100%; margin: 0 auto; page-break-inside: avoid;}"
                        + ".sep {width: 100%; max-width: 100%; border-collapse: collapse; font-size: 10px;}"
                        + ".sep td {padding: 2px; font-size: 10px; vertical-align: top; border: none;}"
                        + ".sep th {padding: 2px; font-size: 10px; vertical-align: top; border: none;}"
                        + ".sep img {max-width: 100%; height: auto;}"
                        + // CSS khusus untuk laporan terapi obat
                        ".terapi-header {font-family: Tahoma; font-size: 12px; font-weight: bold; text-align: center;}"
                        + ".terapi-subheader {font-family: Tahoma; font-size: 10px; font-weight: bold; text-align: center;}"
                        + ".terapi-table {border-collapse: collapse; font-family: Tahoma; font-size: 8.5px; width: 100%;}"
                        + ".terapi-table td, .terapi-table th {border: 1px solid black; font-family: Tahoma; font-size: 8.5px; padding: 3px; vertical-align: top;}"
                        + ".terapi-table th {background-color: #f0f0f0; font-weight: bold;}"
                        + ".terapi-keterangan {font-family: Tahoma; font-size: 8px; font-style: italic; color: #666;}"
                        + ".terapi-legend {font-family: Tahoma; font-size: 8.5px; margin-top: 10px;}"
                        + // Page Break CSS untuk iText PDF - IMPROVED
                        ".pagebreak{page-break-before: always !important; page-break-after: auto !important; page-break-inside: avoid !important; display: block !important; height: 1px !important; margin: 0px !important; padding: 0px !important; border: none !important; clear: both !important;}"
                        + "p.pagebreak{page-break-before: always !important; page-break-after: auto !important; page-break-inside: avoid !important; display: block !important; height: 1px !important; margin: 0px !important; padding: 0px !important; border: none !important; font-size: 1px !important; line-height: 1px !important; clear: both !important;}"
                        + "div.pagebreak{page-break-before: always !important; page-break-after: auto !important; page-break-inside: avoid !important; display: block !important; height: 1px !important; margin: 0px !important; padding: 0px !important; border: none !important; clear: both !important;}"
                        + ".berkas-page{page-break-before: always !important; break-before: page !important; display: block !important; clear: both !important;}"
                        + ".berkas-page:first-of-type{page-break-before: auto !important; break-before: auto !important;}"
                        + ".berkas-page-end{display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; border: none !important;}"
                        + // Media print untuk fallback - Legal Size dengan margin optimal
                        "@page{size: legal portrait; margin: 1.5cm 1cm 1cm 1cm;}"
                        + "@media print {"
                        + "  @page{size: legal portrait; margin: 1.5cm 1cm 1cm 1cm;}"
                        + "  body {margin: 0; padding: 0;}"
                        + "  .pagebreak{page-break-before: always !important; display: block !important; height: 1px !important; margin: 0px !important; padding: 0px !important; clear: both !important;}"
                        + "  p.pagebreak{page-break-before: always !important; display: block !important; height: 1px !important; margin: 0px !important; padding: 0px !important; font-size: 1px !important; clear: both !important;}"
                        + "  div.pagebreak{page-break-before: always !important; display: block !important; height: 1px !important; margin: 0px !important; padding: 0px !important; clear: both !important;}"
                        + "  .berkas-page{page-break-before: always !important; break-before: page !important; display: block !important; clear: both !important;}"
                        + "  .berkas-page:first-of-type{page-break-before: auto !important; break-before: auto !important;}"
                        + "  .berkas-page-end{display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; border: none !important;}"
                        + "  .page-section {page-break-inside: avoid; width: 100%; margin: 0;}"
                        + "}"
                        + // Alternative page break menggunakan CSS3 untuk browser modern
                        ".pagebreak{break-before: page !important; break-after: auto !important; break-inside: avoid !important; clear: both !important;}"
                        + "p.pagebreak{break-before: page !important; break-after: auto !important; break-inside: avoid !important; clear: both !important;}"
                        + "div.pagebreak{break-before: page !important; break-after: auto !important; break-inside: avoid !important; clear: both !important;}"
                        + // Styling untuk fieldset agar tidak mempengaruhi layout PDF
                        "fieldset {border: 1px solid #000; padding: 8px; margin: 5px 0; page-break-inside: avoid;}"
                        + "legend {font-weight: bold; font-size: 12px;}"
                );
                bg.close();

                // Generate PDF ke folder temp agar tidak otomatis menumpuk di folder aplikasi/jar.
                File filePdfKlaim = buatFilePreviewPdfKlaim();
                PdfWriter pdf = new PdfWriter(filePdfKlaim);

                // Preprocess HTML untuk convert page break menjadi format yang dikenali iText - IMPROVED
                String processedHTML = LoadHTMLRiwayatPerawatan.getText()
                        .replaceAll("<head>", "<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />")
                        // Berbagai format page break yang mungkin ada
                        .replaceAll("<p class='pagebreak'></p>", "<div style='page-break-before: always; height: 1px; clear: both; margin: 0; padding: 0; font-size: 1px;'>&nbsp;</div>")
                        .replaceAll("<p class=\"pagebreak\"></p>", "<div style='page-break-before: always; height: 1px; clear: both; margin: 0; padding: 0; font-size: 1px;'>&nbsp;</div>")
                        .replaceAll("<div class='pagebreak'></div>", "<div style='page-break-before: always; height: 1px; clear: both; margin: 0; padding: 0; font-size: 1px;'>&nbsp;</div>")
                        .replaceAll("<div class=\"pagebreak\"></div>", "<div style='page-break-before: always; height: 1px; clear: both; margin: 0; padding: 0; font-size: 1px;'>&nbsp;</div>")
                        .replaceAll("class='pagebreak'", "style='page-break-before: always; height: 1px; clear: both; margin: 0; padding: 0; font-size: 1px;'")
                        .replaceAll("class=\"pagebreak\"", "style='page-break-before: always; height: 1px; clear: both; margin: 0; padding: 0; font-size: 1px;'")
                        // Perbaiki path gambar
                        .replaceAll((getClass().getResource("/picture/")) + "", "./gambar/")
                        // Tambahkan wrapper untuk keseluruhan content agar lebih terkontrol
                        .replaceAll("<body>", "<body style='font-family: Tahoma; font-size: 12px; margin: 0; padding: 10px;'>")
                        .replaceAll("<body ", "<body style='font-family: Tahoma; font-size: 12px; margin: 0; padding: 10px;' ");

                ConverterProperties converterProperties = new ConverterProperties();
                converterProperties.setImmediateFlush(false);
                long mulaiConvert = System.currentTimeMillis();
                HtmlConverter.convertToPdf(processedHTML, pdf, converterProperties);
                System.out.println("Generate Klaim PDF convert iText : " + (System.currentTimeMillis() - mulaiConvert) + " ms");

                // Buka file PDF temp sebagai preview biasa.
                Desktop.getDesktop().browse(filePdfKlaim.toURI());

            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                e.printStackTrace();
            } finally {
                System.out.println("Generate Klaim PDF total : " + (System.currentTimeMillis() - mulaiProsesPdf) + " ms");
            }
        }
    }//GEN-LAST:event_MnGeneratePDFActionPerformed

    private void chkSepBpjsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSepBpjsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSepBpjsActionPerformed

    private void chkFormAssesmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFormAssesmentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkFormAssesmentActionPerformed

    private void chkFormLembarTerapiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFormLembarTerapiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkFormLembarTerapiActionPerformed

    private void chkResepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkResepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkResepActionPerformed

    private void chkSuratKontrolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuratKontrolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSuratKontrolActionPerformed

    private void chkTransferAntarRuangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTransferAntarRuangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkTransferAntarRuangActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMGenerateKlaim dialog = new RMGenerateKlaim(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.TextBox Agama;
    private widget.TextBox Alamat;
    private widget.TextBox Bahasa;
    private widget.Button BtnAll;
    private widget.Button BtnBukaURL;
    private widget.Button BtnCari1;
    private widget.Button BtnCloseUrl;
    private widget.Button BtnDownloadBukaFile;
    private widget.Button BtnDownloadFile;
    private widget.Button BtnKeluar;
    private widget.Button BtnPasien;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSeekPegawai1;
    private widget.TextBox CacatFisik;
    private widget.CekBox ChkAccor;
    private widget.CekBox ChkInput;
    private widget.panelisi FormInput;
    private widget.PanelBiasa FormMenu;
    private widget.TextBox GD;
    private widget.TextBox IbuKandung;
    private widget.TextBox Jk;
    private widget.TextBox KdPeg2;
    private widget.editorpane LoadHTMLRiwayatPerawatan;
    private javax.swing.JMenuItem MnGeneratePDF;
    private javax.swing.JMenuItem MnGeneratePDFSertiSign;
    private widget.TextBox NmPasien;
    private widget.TextBox NoRM;
    private widget.TextBox NoRawat;
    private widget.PanelBiasa PanelAccor;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Pekerjaan;
    private widget.TextBox Pendidikan;
    private widget.RadioButton R4;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane ScrollMenu;
    private javax.swing.JTextField Status;
    private widget.TextBox StatusNikah;
    private widget.TextBox TPegawai2;
    private javax.swing.JTabbedPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.TextBox TanggalLahir;
    private widget.TextBox TempatLahir;
    private widget.TextBox URLSertisign;
    private javax.swing.JTextField Umur;
    private javax.swing.JDialog WindowURLSertisign;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.CekBox chkAsuhanKeperawatanRanapNeonatus;
    private widget.CekBox chkBerkasDigital;
    private widget.CekBox chkBilling;
    private widget.CekBox chkFormAssesment;
    private widget.CekBox chkHasilLab;
    private widget.CekBox chkHasilRad;
    private widget.CekBox chkHasilUSG;
    private widget.CekBox chkKonsultasiMedik;
    private widget.CekBox chkLayananKedokteranFisikRehabilitasi;
    private widget.CekBox chkLayananProgramKFR;
    private widget.CekBox chkOperasiVK;
    private widget.CekBox chkPemberianObat;
    private widget.CekBox chkRawatInap;
    private widget.CekBox chkRawatJalan;
    private widget.CekBox chkResep;
    private widget.CekBox chkResume;
    private widget.CekBox chkRiwayatSOAPIE;
    private widget.CekBox chkSPR;
    private widget.CekBox chkSepBpjs;
    private widget.CekBox chkSuratKelahiranBayi;
    private widget.CekBox chkSuratKontrol;
    private widget.CekBox chkSuratKontrolBiasa;
    private widget.CekBox chkTransferAntarRuang;
    private widget.CekBox chkTriase;
    private widget.CekBox chkUjiFungsiKFR;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame9;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel43;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label17;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label25;
    private widget.Label label26;
    private widget.Label label27;
    private widget.Label label28;
    private widget.Label label29;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelisi6;
    // End of variables declaration//GEN-END:variables

    public void setNoRm(String norm, String nama, String status, String norawat) {
        NoRM.setText(norm);
        NmPasien.setText(nama);
        NoRawat.setText(norawat);
        isPasien();
        if (status.equals("Rawat Jalan")) {
            applyRawatJalanDefaults();
            Status.setText("Rawat Jalan");
        } else {
            applyRawatInapDefaults();
            Status.setText("Rawat Inap");
        }
        isMenu();
        jadwalkanTampilPerawatan();
    }

    public void setNoRawat(String norawat) {
        NoRawat.setText(norawat);
    }

    public void setDefaultRawatJalan() {
        applyRawatJalanDefaults();
        Status.setText("Rawat Jalan");
        jadwalkanTampilPerawatan();
    }

    public void setDefaultRawatInap() {
        applyRawatInapDefaults();
        Status.setText("Rawat Inap");
        jadwalkanTampilPerawatan();
    }

    private void jadwalkanTampilPerawatan() {
        if (prosesTampilBerjalan) {
            return;
        }

        prosesTampilBerjalan = true;
        final JDialog dialog = buatDialogLoadingPdf("Memuat berkas klaim...");
        final Cursor cursorLama = getCursor();
        final int tabTerpilih = TabRawat.getSelectedIndex();
        BtnCari1.setEnabled(false);
        BtnPrint.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (tabTerpilih == 0) {
            clear();
        }

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Exception error;

            @Override
            protected Void doInBackground() {
                try {
                    switch (tabTerpilih) {
                        case 0:
                            tampilPerawatan();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();
                prosesTampilBerjalan = false;
                BtnCari1.setEnabled(true);
                BtnPrint.setEnabled(true);
                setCursor(cursorLama);
                if (error != null) {
                    System.out.println("Notifikasi : " + error);
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Gagal memuat berkas klaim : " + error.getMessage());
                }
            }
        };
        worker.execute();
        dialog.setVisible(true);
    }

    private void prosesTampilPerawatan() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        BtnCari1.setEnabled(false);
        try {
            switch (TabRawat.getSelectedIndex()) {
                case 0:
                    clear();
                    tampilPerawatan();
                    break;
                default:
                    break;
            }
        } finally {
            BtnCari1.setEnabled(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void tampilkanLoadingGenerateKlaim() {
        LoadHTMLRiwayatPerawatan.setText(
                "<html>"
                + "<body style='font-family: Arial, sans-serif; background: #ffffff; color: #333;'>"
                + "<div style='padding: 28px; border: 1px solid #2e8b57; margin: 12px;'>"
                + "<div style='font-size: 16px; font-weight: bold; margin-bottom: 8px;'>Memuat berkas klaim...</div>"
                + "<div style='font-size: 12px;'>Mohon tunggu, sistem sedang mengambil data dan menyiapkan tampilan dokumen.</div>"
                + "</div>"
                + "</body>"
                + "</html>");
    }

    private void clear() {
        KdPeg2.setText("");
        TPegawai2.setText("");
        KdPeg2.setEnabled(false);
        TPegawai2.setEnabled(false);
        BtnAll.setEnabled(false);
//        BtnSeekPegawai1.setEnabled(false);
    }

    private void isPasien() {
        try {
            ps = koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tmp_lahir,pasien.tgl_lahir,pasien.agama,"
                    + "bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,pasien.gol_darah,pasien.nm_ibu,pasien.stts_nikah,pasien.pnd, "
                    + "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,pasien.pekerjaan "
                    + "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "
                    + "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "
                    + "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "
                    + "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "
                    + "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "
                    + "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1, NoRM.getText().trim());
                rs = ps.executeQuery();
                if (rs.next()) {
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NmPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TempatLahir.setText(rs.getString("tmp_lahir"));
                    TanggalLahir.setText(rs.getString("tgl_lahir"));
                    Alamat.setText(rs.getString("alamat"));
                    GD.setText(rs.getString("gol_darah"));
                    IbuKandung.setText(rs.getString("nm_ibu"));
                    Agama.setText(rs.getString("agama"));
                    StatusNikah.setText(rs.getString("stts_nikah"));
                    Pendidikan.setText(rs.getString("pnd"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    CacatFisik.setText(rs.getString("nama_cacat"));
                    Pekerjaan.setText(rs.getString("pekerjaan"));
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    private void isMenu() {
        if (ChkAccor.isSelected() == true) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(275, HEIGHT));
            FormMenu.setVisible(true);
            ChkAccor.setVisible(true);
        } else if (ChkAccor.isSelected() == false) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(15, HEIGHT));
            FormMenu.setVisible(false);
            ChkAccor.setVisible(true);
        }
    }

    private void applyRawatJalanDefaults() {
        chkRawatJalan.setSelected(true);
        chkRawatInap.setSelected(false);
        chkTriase.setSelected(true);
        chkFormAssesment.setSelected(true);
        chkAsuhanKeperawatanRanapNeonatus.setSelected(false);
        chkTransferAntarRuang.setSelected(false);
        chkKonsultasiMedik.setSelected(true);
        chkUjiFungsiKFR.setSelected(true);
        chkLayananKedokteranFisikRehabilitasi.setSelected(true);
        chkLayananProgramKFR.setSelected(true);
        chkOperasiVK.setSelected(true);
        //chkLembarEklaim.setSelected(true);
        chkSepBpjs.setSelected(true);
        chkRiwayatSOAPIE.setSelected(false);
        chkSuratKontrol.setSelected(false);
        chkSuratKontrolBiasa.setSelected(true);
        chkResume.setSelected(true);
        chkHasilLab.setSelected(true);
        chkHasilRad.setSelected(true);
        chkHasilUSG.setSelected(true);
        chkResep.setSelected(true);
        chkBilling.setSelected(true);
        chkSPR.setSelected(false);
        chkSuratKelahiranBayi.setSelected(false);
    }

    private void applyRawatInapDefaults() {
        chkRawatJalan.setSelected(false);
        chkRawatInap.setSelected(true);
        chkTriase.setSelected(true);
        chkFormAssesment.setSelected(true);
        chkAsuhanKeperawatanRanapNeonatus.setSelected(true);
        chkTransferAntarRuang.setSelected(false);
        chkKonsultasiMedik.setSelected(true);
        chkUjiFungsiKFR.setSelected(true);
        chkLayananKedokteranFisikRehabilitasi.setSelected(false);
        chkLayananProgramKFR.setSelected(false);
        chkOperasiVK.setSelected(true);
        //chkLembarEklaim.setSelected(true);
        chkResume.setSelected(true);
        chkSepBpjs.setSelected(true);
        chkRiwayatSOAPIE.setSelected(false);
        chkSuratKontrol.setSelected(false);
        chkSuratKontrolBiasa.setSelected(true);
        chkHasilLab.setSelected(true);
        chkHasilRad.setSelected(true);
        chkHasilUSG.setSelected(true);
        chkBilling.setSelected(true);
        chkSPR.setSelected(true);
        chkSuratKelahiranBayi.setSelected(true);
    }

    public void tampilPerawatan() {
        try {
            htmlContent = new StringBuilder();
            if (R4.isSelected() == true) {
                ps = koneksi.prepareStatement(
                        "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"
                        + "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"
                        + "reg_periksa.umurdaftar,reg_periksa.sttsumur,reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,pasien.nm_pasien,pasien.no_rkm_medis,date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,IF (pasien.jk = 'L', 'LAKI-LAKI', 'PEREMPUAN') as jk "
                        + "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "
                        + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "
                        + "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis  "
                        + "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }

            try {
                i = 0;
                if (R4.isSelected() == true) {
                    ps.setString(1, NoRM.getText().trim());
                    ps.setString(2, NoRawat.getText().trim());
                }
                urut = 1;
                rs = ps.executeQuery();

                // Tambahkan pembuka container HTML
                htmlContent.append("<div class='main-container'>");

                while (rs.next()) {
                    try {
                        dokterrujukan = "";
                        polirujukan = "";
                        rs2 = koneksi.prepareStatement(
                                "select poliklinik.nm_poli,dokter.nm_dokter from rujukan_internal_poli "
                                + "inner join poliklinik on rujukan_internal_poli.kd_poli=poliklinik.kd_poli "
                                + "inner join dokter on rujukan_internal_poli.kd_dokter=dokter.kd_dokter "
                                + "where no_rawat='" + rs.getString("no_rawat") + "'").executeQuery();
                        while (rs2.next()) {
                            polirujukan = polirujukan + ", " + rs2.getString("nm_poli");
                            dokterrujukan = dokterrujukan + ", " + rs2.getString("nm_dokter");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting rujukan internal: " + e.getMessage());
                    } finally {
                        if (rs2 != null) {
                            try {
                                rs2.close();
                            } catch (SQLException e) {
                                System.out.println("Error closing rs2: " + e.getMessage());
                            }
                        }
                    }

                    // Hapus table kosong yang tidak berguna
                    // htmlContent.append("<table width='100%' border='0' align='center' cellpadding='0' cellspacing='0' class=''></table>");
                    if (Status.getText().equals("Rawat Jalan")) {
                        // Tambahkan pembuka section untuk rawat jalan
                        htmlContent.append("<div class='rawat-jalan-section'>");

                        if (chkRawatJalan.isSelected() == true) {

                            // 1. SEP
                            menampilkanSEPBPJS(rs.getString("no_rawat"));

                            // 2. Resume
                            menampilkanResumeRawatJalan(rs.getString("no_rawat"));

                            // 3. Triase
                            menampilkanTriaseIGD(rs.getString("no_rawat"));

                            // 4. Assessment IGD
                            menampilkanAsuhanMedisIGD(rs.getString("no_rawat"));

                            // 5. Hasil Penunjang
                            menampilkanHasilLab(rs.getString("no_rawat"));

                            // 6. Hasil Radiologi
                            menampilkanHasilRadiologi(rs.getString("no_rawat"));

                            // Hasil USG
                            menampilkanHasilPemeriksaanUSG(rs.getString("no_rawat"));

                            // 7. Pemberian Obat
                            menampilkanBerkasDigital(rs.getString("no_rawat"));

                            // 8. Resep
                            menampilkanResep(rs.getString("no_rawat"));

                            // 9. Billing
                            menampilkanBilling(rs.getString("no_rawat"));

                            // 10. Surat Rujukan / Surat Kontrol
                            menampilkanSuratKontrol(rs.getString("no_rawat"));
                            
                            // 11. Surat Kontrol Biasa
                            menampilkanSuratKontrolBiasa(rs.getString("no_rawat"));

                            // 12. Form Fisio/KFR
                            menampilkanUjiFungsiKFR(rs.getString("no_rawat"));
                            
                            // 13. Layanan Kedokteran Fisik Rehabilitasi
                            menampilkanLayananKedokteranFisikRehabilitasi(rs.getString("no_rawat"));
                            
                            // 14. Layanan Program KFR
                            menampilkanLayananProgramKFR(rs.getString("no_rawat"));
                            
                            // 15. Konsultasi Medik
                            menampilkanKonsultasiMedik(rs.getString("no_rawat"));

                        }

                        // Tutup section rawat jalan
                        htmlContent.append("</div>");

                    } else {

                        // Tambahkan pembuka section untuk rawat inap
                        htmlContent.append("<div class='rawat-inap-section'>");

                        // 1. SEP
                        menampilkanSEPBPJS(rs.getString("no_rawat"));

                        // 2. Resume
                        menampilkanRESUMEBPJS(rs.getString("no_rawat"));

                        // 5. Laporan Operasi
                        menampilkanLaporanOperasiVK(rs.getString("no_rawat"));

                        // 3. Triase dan Assessment IGD
                        menampilkanTriaseIGD(rs.getString("no_rawat"));
                        
                        // 4. Asuhan Medis IGD
                        menampilkanAsuhanMedisIGD(rs.getString("no_rawat"));

                        // 4. Awal Keperawatan Ranap Neonatus
                        menampilkanAsuhanKeperawatanRawatInapNeonatus(rs.getString("no_rawat"));

                        // 5. Konsultasi Medik
                        menampilkanKonsultasiMedik(rs.getString("no_rawat"));

                        // Form Fisio/KFR bila ada
                        menampilkanUjiFungsiKFR(rs.getString("no_rawat"));

                        // 6. Hasil Laboratorium
                        menampilkanHasilLab(rs.getString("no_rawat"));

                        // 7. Hasil Radiologi
                        menampilkanHasilRadiologi(rs.getString("no_rawat"));

                        // Riwayat S.O.A.P.I.E Rawat Inap
                        menampilkanRiwayatSOAPRawatInap(rs.getString("no_rawat"));

                        // Hasil USG
                        menampilkanHasilPemeriksaanUSG(rs.getString("no_rawat"));

                        // 8. Resep
                        menampilkanBerkasDigital(rs.getString("no_rawat"));

                        // 7. Billing
                        menampilkanBilling(rs.getString("no_rawat"));

                        // Transfer Antar Ruang
                        menampilkanTransferAntarRuang(rs.getString("no_rawat"));

                        // 8. SPR
                        menampilkanSPRRawatInap(rs.getString("no_rawat"));

                        // 9. Surat Keterangan
                        menampilkanSuratKontrol(rs.getString("no_rawat"));
                        
                        // 10. Surat Kontrol Biasa
                        menampilkanSuratKontrolBiasa(rs.getString("no_rawat"));
                        
                        // 11. Surat Kelahiran Bayi
                        menampilkanSuratKelahiranBayi(rs.getString("no_rawat"));

                        // Tutup section rawat inap
                        htmlContent.append("</div>");
                    }
                }

                // Tutup container HTML
                htmlContent.append("</div>");

                LoadHTMLRiwayatPerawatan.setText(
                        "<html>"
                        + htmlContent.toString()
                        + "</html>");

            } catch (SQLException e) {
                System.out.println("SQLException in tampilPerawatan: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("General Exception in tampilPerawatan: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing rs: " + e.getMessage());
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing ps: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException in tampilPerawatan outer: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("General Exception in tampilPerawatan outer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void menampilkanRiwayatSOAPRawatInap(String norawat) {
        if (!chkRiwayatSOAPIE.isSelected()) {
            return;
        }

        String sql = "select pemeriksaan_ranap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"
                + "if(pasien.jk='L','Laki-Laki','Perempuan') as jk,date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,"
                + "pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.suhu_tubuh,"
                + "pemeriksaan_ranap.tensi,pemeriksaan_ranap.nadi,pemeriksaan_ranap.respirasi,pemeriksaan_ranap.tinggi,"
                + "pemeriksaan_ranap.berat,pemeriksaan_ranap.spo2,pemeriksaan_ranap.gcs,pemeriksaan_ranap.kesadaran,"
                + "pemeriksaan_ranap.keluhan,pemeriksaan_ranap.pemeriksaan,pemeriksaan_ranap.alergi,"
                + "pemeriksaan_ranap.penilaian,pemeriksaan_ranap.rtl,pemeriksaan_ranap.instruksi,"
                + "pemeriksaan_ranap.evaluasi,pemeriksaan_ranap.nip,pegawai.nama,pegawai.jbtn "
                + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik "
                + "where pemeriksaan_ranap.no_rawat=? "
                + "order by pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat";

        try ( PreparedStatement psSOAP = koneksi.prepareStatement(sql)) {
            psSOAP.setString(1, norawat);
            try ( ResultSet rsSOAP = psSOAP.executeQuery()) {
                if (!rsSOAP.next()) {
                    return;
                }

                htmlContent.append(
                        "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>"
                        + "<fieldset style='border: none; padding: 0; margin: 0;'>");
                Copsurat();
                htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>RIWAYAT S.O.A.P.I.E RAWAT INAP</div>")
                        .append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; font-size: 13px;'>");
                appendInfoRow("No. RM", nilai(rsSOAP, "no_rkm_medis"), "Jenis Kelamin", nilai(rsSOAP, "jk"));
                appendInfoRow("Nama Pasien", nilai(rsSOAP, "nm_pasien"), "Tanggal Lahir", nilai(rsSOAP, "tgl_lahir"));
                appendInfoRow("No. Rawat", nilai(rsSOAP, "no_rawat"), "Status", "Ranap");
                htmlContent.append("</table>");

                do {
                    appendRiwayatSOAPRecord(rsSOAP);
                } while (rsSOAP.next());

                htmlContent.append("</fieldset></div>");
                appendPageBreakAfterDocument();
            }
        } catch (Exception e) {
            System.out.println("Notif Riwayat SOAPIE Rawat Inap : " + e);
        }
    }

    private void appendRiwayatSOAPRecord(ResultSet rsSOAP) throws SQLException {
        htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; font-size: 13px; margin-top: 8px;'>")
                .append("<tr>")
                .append("<td colspan='4' style='background: #f5f5f5; border: 1px solid #d9d9d9; padding: 6px 8px; font-weight: bold;'>")
                .append("Tanggal : ").append(formatMultiline(nilai(rsSOAP, "tgl_perawatan"))).append(" ").append(formatMultiline(nilai(rsSOAP, "jam_rawat")))
                .append(" &nbsp;&nbsp; | &nbsp;&nbsp; Dokter/Paramedis : ").append(formatMultiline(nilai(rsSOAP, "nip"))).append(" - ").append(formatMultiline(nilai(rsSOAP, "nama")))
                .append(nilai(rsSOAP, "jbtn").equals("") ? "" : " (" + formatMultiline(nilai(rsSOAP, "jbtn")) + ")")
                .append("</td>")
                .append("</tr>");
        appendSOAPIERow("Subjek", nilai(rsSOAP, "keluhan"));
        appendSOAPIERow("Objek", buildObjekSOAPRawatInap(rsSOAP));
        appendSOAPIERow("Asesmen", nilai(rsSOAP, "penilaian"));
        appendSOAPIERow("Plan", nilai(rsSOAP, "rtl"));
        appendSOAPIERow("Instruksi/Implementasi", nilai(rsSOAP, "instruksi"));
        appendSOAPIERow("Evaluasi", nilai(rsSOAP, "evaluasi"));
        htmlContent.append("</table>");
    }

    private void appendSOAPIERow(String label, String value) {
        htmlContent.append("<tr>")
                .append("<td width='22%' valign='top' style='padding: 6px 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5; font-weight: bold;'>")
                .append(escapeHtml(label)).append("</td>")
                .append("<td width='78%' valign='top' colspan='3' style='padding: 6px 8px; border: 1px solid #e0e0e0;'>: ")
                .append(formatMultiline(value)).append("</td>")
                .append("</tr>");
    }

    private String buildObjekSOAPRawatInap(ResultSet rsSOAP) throws SQLException {
        StringBuilder objek = new StringBuilder();
        appendSOAPLine(objek, nilai(rsSOAP, "pemeriksaan"));
        appendSOAPLine(objek, "Alergi", nilai(rsSOAP, "alergi"));
        appendSOAPLine(objek, "Suhu(C)", nilai(rsSOAP, "suhu_tubuh"));
        appendSOAPLine(objek, "Tensi", nilai(rsSOAP, "tensi"));
        appendSOAPLine(objek, "Nadi(/menit)", nilai(rsSOAP, "nadi"));
        appendSOAPLine(objek, "Respirasi(/menit)", nilai(rsSOAP, "respirasi"));
        appendSOAPLine(objek, "Tinggi(Cm)", nilai(rsSOAP, "tinggi"));
        appendSOAPLine(objek, "Berat(Kg)", nilai(rsSOAP, "berat"));
        appendSOAPLine(objek, "SpO2(%)", nilai(rsSOAP, "spo2"));
        appendSOAPLine(objek, "GCS(E,V,M)", nilai(rsSOAP, "gcs"));
        appendSOAPLine(objek, "Kesadaran", nilai(rsSOAP, "kesadaran"));
        return objek.toString();
    }

    private void appendSOAPLine(StringBuilder builder, String value) {
        if (value != null && !value.trim().equals("")) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(value.trim());
        }
    }

    private void appendSOAPLine(StringBuilder builder, String label, String value) {
        if (value != null && !value.trim().equals("")) {
            appendSOAPLine(builder, label + " : " + value.trim());
        }
    }

    private void menampilkanTransferAntarRuang(String norawat) {
        if (!chkTransferAntarRuang.isSelected()) {
            return;
        }

        String sql = "select transfer_pasien_antar_ruang.tanggal_masuk,transfer_pasien_antar_ruang.tanggal_pindah,"
                + "transfer_pasien_antar_ruang.asal_ruang,transfer_pasien_antar_ruang.ruang_selanjutnya,"
                + "transfer_pasien_antar_ruang.diagnosa_utama,transfer_pasien_antar_ruang.diagnosa_sekunder,"
                + "transfer_pasien_antar_ruang.indikasi_pindah_ruang,transfer_pasien_antar_ruang.keterangan_indikasi_pindah_ruang,"
                + "transfer_pasien_antar_ruang.prosedur_yang_sudah_dilakukan,transfer_pasien_antar_ruang.obat_yang_telah_diberikan,"
                + "transfer_pasien_antar_ruang.metode_pemindahan_pasien,transfer_pasien_antar_ruang.peralatan_yang_menyertai,"
                + "transfer_pasien_antar_ruang.keterangan_peralatan_yang_menyertai,transfer_pasien_antar_ruang.pemeriksaan_penunjang_yang_dilakukan,"
                + "transfer_pasien_antar_ruang.pasien_keluarga_menyetujui,transfer_pasien_antar_ruang.nama_menyetujui,"
                + "transfer_pasien_antar_ruang.hubungan_menyetujui,transfer_pasien_antar_ruang.keluhan_utama_sebelum_transfer,"
                + "transfer_pasien_antar_ruang.keadaan_umum_sebelum_transfer,transfer_pasien_antar_ruang.td_sebelum_transfer,"
                + "transfer_pasien_antar_ruang.nadi_sebelum_transfer,transfer_pasien_antar_ruang.rr_sebelum_transfer,"
                + "transfer_pasien_antar_ruang.suhu_sebelum_transfer,transfer_pasien_antar_ruang.keluhan_utama_sesudah_transfer,"
                + "transfer_pasien_antar_ruang.keadaan_umum_sesudah_transfer,transfer_pasien_antar_ruang.td_sesudah_transfer,"
                + "transfer_pasien_antar_ruang.nadi_sesudah_transfer,transfer_pasien_antar_ruang.rr_sesudah_transfer,"
                + "transfer_pasien_antar_ruang.suhu_sesudah_transfer,transfer_pasien_antar_ruang.nip_menyerahkan,"
                + "petugasmenyerahkan.nama as petugasmenyerahkan,transfer_pasien_antar_ruang.nip_menerima,"
                + "petugasmenerima.nama as petugasmenerima "
                + "from transfer_pasien_antar_ruang inner join petugas as petugasmenyerahkan "
                + "on transfer_pasien_antar_ruang.nip_menyerahkan=petugasmenyerahkan.nip "
                + "inner join petugas as petugasmenerima on transfer_pasien_antar_ruang.nip_menerima=petugasmenerima.nip "
                + "where transfer_pasien_antar_ruang.no_rawat=? order by transfer_pasien_antar_ruang.tanggal_pindah";

        try ( PreparedStatement psTransfer = koneksi.prepareStatement(sql)) {
            psTransfer.setString(1, norawat);
            try ( ResultSet rsTransfer = psTransfer.executeQuery()) {
                while (rsTransfer.next()) {
                    appendTransferAntarRuangRecord(rsTransfer);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Transfer Antar Ruang : " + e);
        }
    }

    private void appendTransferAntarRuangRecord(ResultSet rsTransfer) throws SQLException {
        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>")
                .append("<fieldset style='border: none; padding: 0; margin: 0;'>");

        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 15px;'>TRANSFER PASIEN ANTAR RUANG</div>")
                .append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");

        appendInfoRow("No. RM", rs.getString("no_rkm_medis"), "Tanggal Lahir", rs.getString("tgl_lahir"));
        appendInfoRow("Nama Pasien", rs.getString("nm_pasien"), "Tanggal Pindah", nilai(rsTransfer, "tanggal_pindah"));
        appendInfoRow("Jenis Kelamin", rs.getString("jk"), "Asal Ruang", nilai(rsTransfer, "asal_ruang"));
        appendInfoRow("Tanggal Masuk", nilai(rsTransfer, "tanggal_masuk"), "Ruang Selanjutnya", nilai(rsTransfer, "ruang_selanjutnya"));

        appendTransferSectionRow("I. INDIKASI");
        appendSingleRow("Diagnosa Utama", nilai(rsTransfer, "diagnosa_utama"));
        appendSingleRow("Diagnosa Sekunder", nilai(rsTransfer, "diagnosa_sekunder"));
        appendSingleRow("Indikasi Pindah", gabungTransfer(nilai(rsTransfer, "indikasi_pindah_ruang"), nilai(rsTransfer, "keterangan_indikasi_pindah_ruang")));
        appendInfoRow("Metode", nilai(rsTransfer, "metode_pemindahan_pasien"), "Peralatan", nilai(rsTransfer, "peralatan_yang_menyertai"));
        appendInfoRow("Keterangan Metode", "", "Keterangan Peralatan", nilai(rsTransfer, "keterangan_peralatan_yang_menyertai"));
        appendSingleRow("Persetujuan Pasien/Keluarga", gabungPersetujuanTransfer(rsTransfer));

        appendTransferSectionRow("II. PEMERIKSAAN SEBELUM TRANSFER");
        appendSingleRow("Keluhan Utama Sebelum Transfer", nilai(rsTransfer, "keluhan_utama_sebelum_transfer"));
        appendInfoRow("Keadaan Umum Sebelum Transfer", nilai(rsTransfer, "keadaan_umum_sebelum_transfer"), "TD Sebelum Transfer", nilai(rsTransfer, "td_sebelum_transfer"));
        appendInfoRow("Nadi Sebelum Transfer", nilai(rsTransfer, "nadi_sebelum_transfer"), "RR Sebelum Transfer", nilai(rsTransfer, "rr_sebelum_transfer"));
        appendInfoRow("Suhu Sebelum Transfer", nilai(rsTransfer, "suhu_sebelum_transfer"), "", "");

        appendTransferSectionRow("III. PEMERIKSAAN SESUDAH TRANSFER");
        appendSingleRow("Keluhan Utama Sesudah Transfer", nilai(rsTransfer, "keluhan_utama_sesudah_transfer"));
        appendInfoRow("Keadaan Umum Sesudah Transfer", nilai(rsTransfer, "keadaan_umum_sesudah_transfer"), "TD Sesudah Transfer", nilai(rsTransfer, "td_sesudah_transfer"));
        appendInfoRow("Nadi Sesudah Transfer", nilai(rsTransfer, "nadi_sesudah_transfer"), "RR Sesudah Transfer", nilai(rsTransfer, "rr_sesudah_transfer"));
        appendInfoRow("Suhu Sesudah Transfer", nilai(rsTransfer, "suhu_sesudah_transfer"), "", "");

        appendTransferSectionRow("IV. OBAT DAN PENUNJANG");
        appendSingleRow("Obat Telah Diberikan", nilai(rsTransfer, "obat_yang_telah_diberikan"));
        appendSingleRow("Terapi Rawat Inap", nilai(rsTransfer, "prosedur_yang_sudah_dilakukan"));
        appendSingleRow("Penunjang", nilai(rsTransfer, "pemeriksaan_penunjang_yang_dilakukan"));

        appendTransferAntarRuangSignature(rsTransfer);
        htmlContent.append("</table></fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private String gabungTransfer(String value, String keterangan) {
        if (keterangan == null || keterangan.trim().equals("")) {
            return value;
        }
        if (value == null || value.trim().equals("")) {
            return keterangan;
        }
        return value + ", " + keterangan;
    }

    private void appendTransferSectionRow(String title) {
        htmlContent.append("<tr><td colspan='4' style='background: #ececec; color: #111; padding: 7px 8px; border: 1px solid #d9d9d9; font-weight: bold;'>")
                .append(escapeHtml(title)).append("</td></tr>");
    }

    private String gabungPersetujuanTransfer(ResultSet rsTransfer) throws SQLException {
        String persetujuan = nilai(rsTransfer, "pasien_keluarga_menyetujui");
        String nama = nilai(rsTransfer, "nama_menyetujui");
        String hubungan = nilai(rsTransfer, "hubungan_menyetujui");
        if (!nama.trim().equals("")) {
            persetujuan = gabungTransfer(persetujuan, nama + (hubungan.trim().equals("") ? "" : " (" + hubungan + ")"));
        }
        return persetujuan;
    }

    private void appendTransferAntarRuangSignature(ResultSet rsTransfer) throws SQLException {
        String kodeMenyerahkan = nilai(rsTransfer, "nip_menyerahkan").replace(" ", "_");
        String kodeMenerima = nilai(rsTransfer, "nip_menerima").replace(" ", "_");

        generateQrPetugasTransfer(kodeMenyerahkan, "menyerahkan");
        generateQrPetugasTransfer(kodeMenerima, "menerima");

        htmlContent.append("<tr>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 6px 8px; border: 1px solid #d9d9d9; background-color: #f5f5f5; font-weight: bold;'>Petugas Yang Mengirim</td>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 6px 8px; border: 1px solid #d9d9d9; background-color: #f5f5f5; font-weight: bold;'>Petugas Yang Menerima</td>")
                .append("</tr><tr>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 8px; border: 1px solid #d9d9d9;'>");

        if (!kodeMenyerahkan.equals("")) {
            htmlContent.append("<img width='85' height='85' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                    .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(urlEncode(kodeMenyerahkan)).append(".png' onerror=\"this.style.display='none';\"/><br>");
        }

        htmlContent.append(escapeHtml(nilai(rsTransfer, "petugasmenyerahkan"))).append("</td>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 8px; border: 1px solid #d9d9d9;'>");

        if (!kodeMenerima.equals("")) {
            htmlContent.append("<img width='85' height='85' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                    .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(urlEncode(kodeMenerima)).append(".png' onerror=\"this.style.display='none';\"/><br>");
        }

        htmlContent.append(escapeHtml(nilai(rsTransfer, "petugasmenerima"))).append("</td></tr>");
    }

    private void generateQrPetugasTransfer(String kodePetugas, String posisi) {
        if (kodePetugas == null || kodePetugas.trim().equals("")) {
            return;
        }

        try {
            get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + urlEncode(kodePetugas));
            http.executeMethod(get);
        } catch (Exception e) {
            System.out.println("Error generating QR petugas transfer " + posisi + ": " + e.getMessage());
        }
    }

    private void panggilLaporan(String teks) {
        try {
            File g = new File("fileklaim.css");
            try ( BufferedWriter bg = new BufferedWriter(new FileWriter(g))) {
                bg.write(
                        "/* Global Font Sizes - Normal Readable */\n"
                        + "* { font-size: 12px !important; font-family: Arial, sans-serif !important; }\n"
                        + "body { font-size: 12px !important; line-height: 1.4 !important; margin: 15px !important; }\n"
                        + "/* Table Fonts - Standard Size */\n"
                        + "table, table *, td, th, tr { font-size: 11px !important; }\n"
                        + "table.sep, table.sep * { font-size: 10px !important; }\n"
                        + "td, th { padding: 3px !important; font-size: 11px !important; }\n"
                        + "/* Headers - Appropriate Hierarchy */\n"
                        + "h1 { font-size: 16px !important; margin: 6px 0 !important; font-weight: bold !important; }\n"
                        + "h2 { font-size: 14px !important; margin: 5px 0 !important; font-weight: bold !important; }\n"
                        + "h3 { font-size: 13px !important; margin: 4px 0 !important; font-weight: bold !important; }\n"
                        + "/* Paragraph and Text - Standard Size */\n"
                        + "p { font-size: 12px !important; margin: 3px 0 !important; }\n"
                        + "span { font-size: 12px !important; }\n"
                        + "div { font-size: 12px !important; margin: 2px 0 !important; }\n"
                        + "strong, b { font-size: 12px !important; font-weight: bold !important; }\n"
                        + "i { font-size: 12px !important; }\n"
                        + "/* SBPK Normal Font - Slightly Larger but Not Excessive */\n"
                        + ".sbpk-normal, .sbpk-normal *, .sbpk-normal table, .sbpk-normal td, .sbpk-normal th, "
                        + ".sbpk-normal p, .sbpk-normal div, .sbpk-normal span, .sbpk-normal strong, .sbpk-normal b { "
                        + "  font-size: 13px !important; "
                        + "}\n"
                        + ".sbpk-normal h1 { font-size: 17px !important; }\n"
                        + ".sbpk-normal h2 { font-size: 15px !important; }\n"
                        + ".sbpk-normal h3 { font-size: 14px !important; }\n"
                        + "/* SBPK Components */\n"
                        + ".sbpk-title { font-size: 15px !important; font-weight: bold !important; }\n"
                        + ".sbpk-table, .sbpk-table *, .sbpk-table td, .sbpk-table th { font-size: 12px !important; }\n"
                        + ".sbpk-box, .sbpk-box *, .sbpk-box strong, .sbpk-box span { font-size: 12px !important; }\n"
                        + "/* Medical Records - Standard Professional Size */\n"
                        + ".riwayat-perawatan, .riwayat-perawatan * { font-size: 12px !important; }\n"
                        + ".riwayat-perawatan table, .riwayat-perawatan table * { font-size: 11px !important; }\n"
                        + ".riwayat-perawatan h1 { font-size: 15px !important; }\n"
                        + ".riwayat-perawatan h2 { font-size: 14px !important; }\n"
                        + ".riwayat-perawatan h3 { font-size: 13px !important; }\n"
                        + "/* Container Styles */\n"
                        + "fieldset { border: 1px solid #000; margin: 8px 0; padding: 8px; font-size: 12px !important; width: 100% !important; box-sizing: border-box !important; }\n"
                        + "div[style*='border'] { padding: 5px !important; font-size: 12px !important; }\n"
                        + "/* Table Specific */\n"
                        + "table { border-collapse: collapse; width: 100% !important; margin: 5px 0; box-sizing: border-box !important; }\n"
                        + "table[border='1'] { border: 1px solid #000; }\n"
                        + "table[border='1'] td, table[border='1'] th { border: 1px solid #000; }\n"
                        + "th { font-weight: bold; background-color: #f0f0f0; }\n"
                        + "/* Print Styles - Appropriate for Print */\n"
                        + "@page { margin: 1.5cm 1cm 1cm 1cm; size: legal portrait; }\n"
                        + ".page-section { margin-bottom: 0 !important; font-size: 12px !important; break-after: page !important; page-break-after: always !important; }\n"
                        + ".berkas-page { display: block !important; clear: both !important; break-before: page !important; page-break-before: always !important; margin: 0 auto !important; box-sizing: border-box !important; }\n"
                        + ".berkas-page:first-of-type { break-before: auto !important; page-break-before: auto !important; }\n"
                        + ".berkas-page-end { display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; break-after: auto !important; page-break-after: auto !important; }\n"
                        + ".pagebreak { display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; break-before: page !important; page-break-before: always !important; }\n"
                        + "@media print {\n"
                        + "  * { font-size: 11px !important; }\n"
                        + "  body { font-size: 11px !important; margin: 0 !important; padding: 0 !important; }\n"
                        + "  .sbpk-normal * { font-size: 12px !important; }\n"
                        + "  .riwayat-perawatan * { font-size: 11px !important; }\n"
                        + "  table * { font-size: 10px !important; }\n"
                        + "  h1 { font-size: 14px !important; }\n"
                        + "  h2 { font-size: 13px !important; }\n"
                        + "  .page-section { break-after: page !important; page-break-after: always !important; page-break-inside: avoid !important; }\n"
                        + "  .berkas-page { display: block !important; clear: both !important; break-before: page !important; page-break-before: always !important; margin-top: 0 !important; }\n"
                        + "  .berkas-page:first-of-type { break-before: auto !important; page-break-before: auto !important; }\n"
                        + "  .berkas-page-end { display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; break-after: auto !important; page-break-after: auto !important; }\n"
                        + "  .pagebreak { break-before: page !important; page-break-before: always !important; height: 0 !important; margin: 0 !important; padding: 0 !important; clear: both !important; }\n"
                        + "  .pagebreak:first-child, p.pagebreak:first-child, div.pagebreak:first-child { page-break-before: avoid !important; }\n"
                        + "  .avoid-page-break { page-break-before: avoid; }\n"
                        + "  @page { margin: 1.5cm 1cm 1cm 1cm; size: legal portrait; }\n"
                        + "}\n"
                        + "/* Image Control */\n"
                        + "img { max-width: 100%; height: auto; }\n"
                        + "/* Alignment Preserve */\n"
                        + "[style*='text-align:center'] { text-align: center !important; }\n"
                        + "[style*='text-align:right'] { text-align: right !important; }\n"
                        + "[style*='text-align:left'] { text-align: left !important; }\n"
                        + "/* Background and Border Preserve */\n"
                        + "[style*='background-color:#f0f0f0'] { background-color: #f0f0f0 !important; }\n"
                        + "[style*='border: 1px solid black'] { border: 1px solid #000 !important; }\n"
                        + "/* Width Preserve */\n"
                        + "[width='100%'] { width: 100% !important; }\n"
                        + "[width='50%'] { width: 50% !important; }\n"
                        + "[width='70%'] { width: 70% !important; }\n"
                        + "[width='30%'] { width: 30% !important; }\n"
                        + "/* Override for Common Elements */\n"
                        + "center { font-size: 13px !important; }\n"
                        + "small { font-size: 11px !important; }\n"
                        + ".page-section { margin-bottom: 0 !important; font-size: 12px !important; }\n"
                );
            }

            File f = new File("generateberkasklaim.html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));

            // HTML content dengan CSS yang sudah dinormalisasi
            String htmlContent = teks.replaceAll(
                    "<head>",
                    "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "<link href=\"fileklaim.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\"/>"
                    + "<meta http-equiv=\"Access-Control-Allow-Origin\" content=\"*\" />"
                    + "<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>"
                    + "<style>"
                    + "/* Final Override - Balanced Professional Font Sizes */\n"
                    + "* { font-size: 12px !important; }\n"
                    + "body { zoom: 1.0 !important; font-size: 12px !important; width: 100% !important; }\n"
                    + "table { width: 100% !important; box-sizing: border-box !important; }\n"
                    + "table * { font-size: 11px !important; }\n"
                    + "fieldset { width: 100% !important; box-sizing: border-box !important; }\n"
                    + "p { width: 100% !important; }\n"
                    + "h1 { font-size: 16px !important; font-weight: bold !important; }\n"
                    + "h2 { font-size: 14px !important; font-weight: bold !important; }\n"
                    + "h3 { font-size: 13px !important; font-weight: bold !important; }\n"
                    + "/* SBPK Balanced Override */\n"
                    + ".sbpk-normal, .sbpk-normal * { font-size: 13px !important; }\n"
                    + ".sbpk-table, .sbpk-table * { font-size: 12px !important; }\n"
                    + ".sbpk-box, .sbpk-box * { font-size: 12px !important; }\n"
                    + ".sbpk-title { font-size: 15px !important; font-weight: bold !important; }\n"
                    + "fieldset.sbpk-normal * { font-size: 13px !important; }\n"
                    + "fieldset.sbpk-normal table * { font-size: 12px !important; }\n"
                    + "/* Medical Records Balanced Size */\n"
                    + ".riwayat-perawatan, .riwayat-perawatan * { font-size: 12px !important; }\n"
                    + ".riwayat-perawatan table * { font-size: 11px !important; }\n"
                    + ".riwayat-perawatan h1 { font-size: 15px !important; }\n"
                    + ".riwayat-perawatan h2 { font-size: 14px !important; }\n"
                    + "/* Consistent sizing */\n"
                    + "center { font-size: 13px !important; }\n"
                    + "strong, b { font-size: inherit !important; }\n"
                    + "span { font-size: inherit !important; }\n"
                    + "div { font-size: inherit !important; }\n"
                    + "/* Ensure readability */\n"
                    + "p { font-size: 12px !important; }\n"
                    + "td, th { font-size: 11px !important; }\n"
                    + "@page { size: legal portrait; margin: 1.5cm 1cm 1cm 1cm; }\n"
                    + ".page-section { break-after: page !important; page-break-after: always !important; page-break-inside: avoid !important; margin-bottom: 0 !important; }\n"
                    + ".berkas-page { display: block !important; clear: both !important; break-before: page !important; page-break-before: always !important; margin-top: 0 !important; box-sizing: border-box !important; }\n"
                    + ".berkas-page:first-of-type { break-before: auto !important; page-break-before: auto !important; }\n"
                    + ".berkas-page-end { display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; break-after: auto !important; page-break-after: auto !important; }\n"
                    + ".pagebreak { display: block !important; clear: both !important; height: 0 !important; margin: 0 !important; padding: 0 !important; break-before: page !important; page-break-before: always !important; }\n"
                    + "@media print { .page-section { break-after: page !important; page-break-after: always !important; } .berkas-page { break-before: page !important; page-break-before: always !important; } .berkas-page:first-of-type { break-before: auto !important; page-break-before: auto !important; } .berkas-page-end { break-after: auto !important; page-break-after: auto !important; } .pagebreak { break-before: page !important; page-break-before: always !important; } }\n"
                    + "</style>"
            );

            bw.write(htmlContent);
            bw.close();
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void isForm() {
        ScrollMenu.getVerticalScrollBar().setUnitIncrement(20);
        if (ChkInput.isSelected() == true) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 126));
            FormInput.setVisible(true);
            ChkInput.setVisible(true);
        } else if (ChkInput.isSelected() == false) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 20));
            FormInput.setVisible(false);
            ChkInput.setVisible(true);
        }
    }

    private void menampilkanAsuhanMedisIGD(String norawat) {
        if (!chkFormAssesment.isSelected()) {
            return;
        }

        String sql = "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,"
                + "if(pasien.jk='L','Laki-Laki','Perempuan') as jk,date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,"
                + "date_format(penilaian_medis_igd.tanggal,'%d-%m-%Y %H:%i:%s') as tanggal,"
                + "penilaian_medis_igd.kd_dokter,penilaian_medis_igd.anamnesis,penilaian_medis_igd.hubungan,"
                + "penilaian_medis_igd.keluhan_utama,penilaian_medis_igd.rps,penilaian_medis_igd.rpk,"
                + "penilaian_medis_igd.rpd,penilaian_medis_igd.rpo,penilaian_medis_igd.alergi,"
                + "penilaian_medis_igd.keadaan,penilaian_medis_igd.gcs,penilaian_medis_igd.kesadaran,"
                + "penilaian_medis_igd.td,penilaian_medis_igd.nadi,penilaian_medis_igd.rr,"
                + "penilaian_medis_igd.suhu,penilaian_medis_igd.spo,penilaian_medis_igd.bb,"
                + "penilaian_medis_igd.tb,penilaian_medis_igd.kepala,penilaian_medis_igd.mata,"
                + "penilaian_medis_igd.gigi,penilaian_medis_igd.leher,penilaian_medis_igd.thoraks,"
                + "penilaian_medis_igd.abdomen,penilaian_medis_igd.ekstremitas,penilaian_medis_igd.genital,"
                + "penilaian_medis_igd.ket_fisik,penilaian_medis_igd.ket_lokalis,penilaian_medis_igd.ekg,"
                + "penilaian_medis_igd.rad,penilaian_medis_igd.lab,penilaian_medis_igd.diagnosis,"
                + "penilaian_medis_igd.tata,dokter.nm_dokter "
                + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join penilaian_medis_igd on reg_periksa.no_rawat=penilaian_medis_igd.no_rawat "
                + "inner join dokter on penilaian_medis_igd.kd_dokter=dokter.kd_dokter "
                + "where penilaian_medis_igd.no_rawat=? order by penilaian_medis_igd.tanggal";

        try ( PreparedStatement psMedis = koneksi.prepareStatement(sql)) {
            psMedis.setString(1, norawat);
            try ( ResultSet rsMedis = psMedis.executeQuery()) {
                if (!rsMedis.next()) {
                    return;
                }

                htmlContent.append(
                        "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>"
                        + "<fieldset style='border: none; padding: 0; margin: 0;'>");
                Copsurat();
                htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>PENILAIAN AWAL MEDIS IGD</div>");

                do {
                    appendAsuhanMedisIgdRecord(rsMedis);
                } while (rsMedis.next());

                htmlContent.append("</fieldset></div>");
            }
        } catch (Exception e) {
            System.out.println("Notif Asuhan Medis IGD : " + e);
        }
    }

    private void menampilkanUjiFungsiKFR(String norawat) {
        if (!chkUjiFungsiKFR.isSelected()) {
            return;
        }

        String sql = "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,"
                + "date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,"
                + "date_format(uji_fungsi_kfr.tanggal,'%d/%m/%Y %H:%i:%s') as tanggal,"
                + "date_format(uji_fungsi_kfr.tanggal,'%d-%m-%Y') as tanggal_ttd,"
                + "uji_fungsi_kfr.diagnosis_fungsional,uji_fungsi_kfr.diagnosis_medis,"
                + "uji_fungsi_kfr.hasil_didapat,uji_fungsi_kfr.kesimpulan,uji_fungsi_kfr.rekomedasi,"
                + "uji_fungsi_kfr.kd_dokter,dokter.nm_dokter "
                + "from uji_fungsi_kfr inner join reg_periksa on uji_fungsi_kfr.no_rawat=reg_periksa.no_rawat "
                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join dokter on uji_fungsi_kfr.kd_dokter=dokter.kd_dokter "
                + "where uji_fungsi_kfr.no_rawat=? order by uji_fungsi_kfr.tanggal";

        try ( PreparedStatement psKFR = koneksi.prepareStatement(sql)) {
            psKFR.setString(1, norawat);
            try ( ResultSet rsKFR = psKFR.executeQuery()) {
                while (rsKFR.next()) {
                    appendUjiFungsiKFRRecord(rsKFR);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Uji Fungsi/Prosedur KFR : " + e);
        }
    }

    private void appendUjiFungsiKFRRecord(ResultSet rsKFR) throws SQLException {
        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>");
        htmlContent.append("<fieldset style='border: none; padding: 0; margin: 0;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>LEMBAR TINDAKAN UJI FUNGSI/PROSEDUR KFR</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("No. RM", rsKFR.getString("no_rkm_medis"), "Jenis Kelamin", rsKFR.getString("jk"));
        appendInfoRow("Nama Pasien", rsKFR.getString("nm_pasien"), "Tanggal Lahir", rsKFR.getString("tgl_lahir"));
        appendInfoRow("Tanggal Pemeriksaan", rsKFR.getString("tanggal"), "Dokter", rsKFR.getString("nm_dokter"));
        appendSingleRow("Diagnosis Fungsional", rsKFR.getString("diagnosis_fungsional"));
        appendSingleRow("Diagnosis Medis", rsKFR.getString("diagnosis_medis"));
        htmlContent.append("</table>");

        appendSectionTitle("INSTRUMEN UJI FUNGSI/PROSEDUR KFR");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendKfrNarrativeRow("Hasil yang didapat", rsKFR.getString("hasil_didapat"), 150);
        appendKfrNarrativeRow("Kesimpulan", rsKFR.getString("kesimpulan"), 120);
        appendKfrNarrativeRow("Rekomendasi", rsKFR.getString("rekomedasi"), 120);
        htmlContent.append("</table>");

        appendUjiFungsiKFRSignature(rsKFR);
        htmlContent.append("</fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private void appendKfrNarrativeRow(String label, String value, int minHeight) {
        htmlContent.append("<tr>")
                .append("<td width='24%' valign='top' style='padding: 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5; font-weight: bold;'>")
                .append(escapeHtml(label)).append("</td>")
                .append("<td width='76%' valign='top' style='padding: 8px; border: 1px solid #e0e0e0; min-height: ")
                .append(minHeight).append("px; height: ").append(minHeight).append("px;'>: ")
                .append(formatMultiline(value)).append("</td>")
                .append("</tr>");
    }

    private void appendUjiFungsiKFRSignature(ResultSet rsKFR) throws SQLException {
        String kodeDokter = rsKFR.getString("kd_dokter") == null ? "" : rsKFR.getString("kd_dokter").replace(" ", "_");
        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Uji Fungsi KFR: " + e.getMessage());
        }

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; margin-top: 22px; font-size: 11px;'>");
        htmlContent.append("<tr>");
        htmlContent.append("<td width='60%'>&nbsp;</td>");
        htmlContent.append("<td width='40%' align='center' style='font-size: 11px !important;'>");
        htmlContent.append(escapeHtml(akses.getkabupatenrs())).append(", ").append(escapeHtml(rsKFR.getString("tanggal_ttd")));
        htmlContent.append("</td>");
        htmlContent.append("</tr>");
        htmlContent.append("<tr>");
        htmlContent.append("<td>&nbsp;</td>");
        htmlContent.append("<td align='center' style='padding-top: 8px;'>");
        htmlContent.append("<img width='105' height='105' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/");
        htmlContent.append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kodeDokter)).append(".png' onerror=\"this.style.display='none';\"/>");
        htmlContent.append("</td>");
        htmlContent.append("</tr>");
        htmlContent.append("<tr>");
        htmlContent.append("<td>&nbsp;</td>");
        htmlContent.append("<td align='center' style='font-size: 11px !important;'>").append(escapeHtml(rsKFR.getString("nm_dokter"))).append("</td>");
        htmlContent.append("</tr>");
        htmlContent.append("</table>");
    }

    private void menampilkanLayananKedokteranFisikRehabilitasi(String norawat) {
        if (!chkLayananKedokteranFisikRehabilitasi.isSelected()) {
            return;
        }

        String sql = "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,"
                + "if(pasien.jk='L','Laki-Laki','Perempuan') as jk,date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,"
                + "date_format(layanan_kedokteran_fisik_rehabilitasi.tanggal,'%d/%m/%Y %H:%i:%s') as tanggal,"
                + "date_format(layanan_kedokteran_fisik_rehabilitasi.tanggal,'%d-%m-%Y') as tanggal_ttd,"
                + "layanan_kedokteran_fisik_rehabilitasi.kd_dokter,dokter.nm_dokter,"
                + "layanan_kedokteran_fisik_rehabilitasi.pendamping,layanan_kedokteran_fisik_rehabilitasi.keterangan_pendamping,"
                + "layanan_kedokteran_fisik_rehabilitasi.anamnesa,layanan_kedokteran_fisik_rehabilitasi.pemeriksaan_fisik,"
                + "layanan_kedokteran_fisik_rehabilitasi.diagnosa_medis,layanan_kedokteran_fisik_rehabilitasi.diagnosa_fungsi,"
                + "layanan_kedokteran_fisik_rehabilitasi.tatalaksana,layanan_kedokteran_fisik_rehabilitasi.anjuran,"
                + "layanan_kedokteran_fisik_rehabilitasi.evaluasi,layanan_kedokteran_fisik_rehabilitasi.suspek_penyakit_kerja,"
                + "layanan_kedokteran_fisik_rehabilitasi.keterangan_suspek_penyakit_kerja,"
                + "kelurahan.nm_kel,kecamatan.nm_kec,kabupaten.nm_kab,propinsi.nm_prop,poliklinik.nm_poli,pasien.alamat,"
                + "bukti_layanan_kedokteran_fisik_rehabilitasi.photo "
                + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join kelurahan on kelurahan.kd_kel=pasien.kd_kel "
                + "inner join kecamatan on kecamatan.kd_kec=pasien.kd_kec "
                + "inner join kabupaten on kabupaten.kd_kab=pasien.kd_kab "
                + "inner join propinsi on propinsi.kd_prop=pasien.kd_prop "
                + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "
                + "inner join layanan_kedokteran_fisik_rehabilitasi on reg_periksa.no_rawat=layanan_kedokteran_fisik_rehabilitasi.no_rawat "
                + "inner join dokter on layanan_kedokteran_fisik_rehabilitasi.kd_dokter=dokter.kd_dokter "
                + "left join bukti_layanan_kedokteran_fisik_rehabilitasi on layanan_kedokteran_fisik_rehabilitasi.no_rawat=bukti_layanan_kedokteran_fisik_rehabilitasi.no_rawat "
                + "where layanan_kedokteran_fisik_rehabilitasi.no_rawat=? order by layanan_kedokteran_fisik_rehabilitasi.tanggal";

        try ( PreparedStatement psLayananKFR = koneksi.prepareStatement(sql)) {
            psLayananKFR.setString(1, norawat);
            try ( ResultSet rsLayananKFR = psLayananKFR.executeQuery()) {
                while (rsLayananKFR.next()) {
                    appendLayananKedokteranFisikRehabilitasiRecord(rsLayananKFR);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Layanan Kedokteran Fisik Rehabilitasi : " + e);
        }
    }

    private void appendLayananKedokteranFisikRehabilitasiRecord(ResultSet rsLayananKFR) throws SQLException {
        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>");
        htmlContent.append("<fieldset style='border: none; padding: 0; margin: 0;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 15px;'>LEMBAR FORMULIR RAWAT JALAN LAYANAN KEDOKTERAN FISIK &amp; REHABILITASI</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("No. RM", rsLayananKFR.getString("no_rkm_medis"), "Jenis Kelamin", rsLayananKFR.getString("jk"));
        appendInfoRow("Nama Pasien", rsLayananKFR.getString("nm_pasien"), "Tanggal", rsLayananKFR.getString("tanggal"));
        appendInfoRow("Alamat Pasien", alamatLayananKFR(rsLayananKFR), "Asal Poli", rsLayananKFR.getString("nm_poli"));
        appendInfoRow("Tanggal Lahir", rsLayananKFR.getString("tgl_lahir"), "Dokter", rsLayananKFR.getString("nm_dokter"));
        appendSingleRow("Pendamping", gabungPendampingLayananKFR(rsLayananKFR));
        htmlContent.append("</table>");

        appendSectionTitle("II. DIISI OLEH DOKTER Sp. KFR");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("Anamnesa", rsLayananKFR.getString("anamnesa"));
        appendSingleRow("Pemeriksaan Fisik &amp; Uji Fungsi", rsLayananKFR.getString("pemeriksaan_fisik"));
        appendSingleRow("Diagnosis Medis (ICD - 10)", rsLayananKFR.getString("diagnosa_medis"));
        appendSingleRow("Diagnosis Fungsi (ICD - 10)", rsLayananKFR.getString("diagnosa_fungsi"));
        appendSingleRow("Tata Laksana KFR (ICD - 9 CM)", rsLayananKFR.getString("tatalaksana"));
        appendSingleRow("Anjuran", rsLayananKFR.getString("anjuran"));
        appendSingleRow("Evaluasi", rsLayananKFR.getString("evaluasi"));
        appendSingleRow("Suspek Penyakit Akibat Kerja", gabungSuspekLayananKFR(rsLayananKFR));
        htmlContent.append("</table>");

        String photo = rsLayananKFR.getString("photo") == null ? "" : rsLayananKFR.getString("photo").trim();
        if (!photo.equals("") && !photo.equals("-")) {
            appendSectionTitle("III. BUKTI PELAYANAN");
            htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
                    + "<tr><td align='center' style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + "<img alt='Bukti Pelayanan' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                    .append(koneksiDB.HYBRIDWEB()).append("/layanankedokteranfisikrehabilitasi/").append(escapeHtml(photo))
                    .append("' style='width: 94%; max-height: 430px; object-fit: contain;' onerror=\"this.style.display='none';\"/>"
                            + "</td></tr></table>");
        }

        appendLayananKedokteranFisikRehabilitasiSignature(rsLayananKFR);
        htmlContent.append("</fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private String alamatLayananKFR(ResultSet rsLayananKFR) throws SQLException {
        return gabungNama(rsLayananKFR.getString("alamat"), rsLayananKFR.getString("nm_kel"),
                rsLayananKFR.getString("nm_kec"), rsLayananKFR.getString("nm_kab"), rsLayananKFR.getString("nm_prop"));
    }

    private String gabungPendampingLayananKFR(ResultSet rsLayananKFR) throws SQLException {
        String pendamping = rsLayananKFR.getString("pendamping");
        String keterangan = rsLayananKFR.getString("keterangan_pendamping");
        if (keterangan == null || keterangan.trim().equals("")) {
            return pendamping;
        }
        return pendamping + ", " + keterangan;
    }

    private String gabungSuspekLayananKFR(ResultSet rsLayananKFR) throws SQLException {
        String suspek = rsLayananKFR.getString("suspek_penyakit_kerja");
        String keterangan = rsLayananKFR.getString("keterangan_suspek_penyakit_kerja");
        if (keterangan == null || keterangan.trim().equals("")) {
            return suspek;
        }
        return suspek + ", " + keterangan;
    }

    private void appendLayananKedokteranFisikRehabilitasiSignature(ResultSet rsLayananKFR) throws SQLException {
        String kodeDokter = rsLayananKFR.getString("kd_dokter") == null ? "" : rsLayananKFR.getString("kd_dokter").trim().replace(" ", "_");
        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Layanan KFR: " + e.getMessage());
        }

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; margin-top: 10px; font-size: 11px;'>"
                + "<tr>"
                + "<td width='50%' align='center' style='border-top: 1px solid #d9d9d9; padding-top: 8px;'>Tanggal dan Jam<br><br>")
                .append(escapeHtml(rsLayananKFR.getString("tanggal")))
                .append("</td>"
                        + "<td width='50%' align='center' style='border-top: 1px solid #d9d9d9; padding-top: 8px;'>Nama Dokter dan Tanda Tangan<br>")
                .append("<img width='85' height='85' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kodeDokter)).append(".png' onerror=\"this.style.display='none';\"/>")
                .append("<br>").append(escapeHtml(rsLayananKFR.getString("nm_dokter")))
                .append("</td>"
                        + "</tr>"
                        + "</table>");
    }

    private void menampilkanLayananProgramKFR(String norawat) {
        if (!chkLayananProgramKFR.isSelected()) {
            return;
        }

        String sql = "select layanan_program_kfr.no_rawat,layanan_program_kfr.tanggal,"
                + "date_format(layanan_program_kfr.tanggal,'%d/%m/%Y %H:%i:%s') as tanggal_format,"
                + "date_format(layanan_program_kfr.tanggal,'%d-%m-%Y') as tanggal_ttd,"
                + "layanan_program_kfr.no_rawat_layanan,layanan_program_kfr.program,"
                + "layanan_program_kfr.nip,petugas.nama as nm_petugas,"
                + "pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                + "pasien.jk,date_format(pasien.tgl_lahir,'%d/%m/%Y') as tgl_lahir,"
                + "layanan_kedokteran_fisik_rehabilitasi.diagnosa_medis,"
                + "layanan_kedokteran_fisik_rehabilitasi.tatalaksana,layanan_kedokteran_fisik_rehabilitasi.evaluasi,"
                + "layanan_kedokteran_fisik_rehabilitasi.kd_dokter,dokter.nm_dokter,"
                + "bukti_layanan_program_kfr.photo "
                + "from layanan_program_kfr "
                + "inner join reg_periksa on layanan_program_kfr.no_rawat=reg_periksa.no_rawat "
                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join petugas on layanan_program_kfr.nip=petugas.nip "
                + "inner join layanan_kedokteran_fisik_rehabilitasi on layanan_kedokteran_fisik_rehabilitasi.no_rawat=layanan_program_kfr.no_rawat_layanan "
                + "inner join dokter on layanan_kedokteran_fisik_rehabilitasi.kd_dokter=dokter.kd_dokter "
                + "left join bukti_layanan_program_kfr on bukti_layanan_program_kfr.no_rawat=layanan_program_kfr.no_rawat "
                + "where layanan_program_kfr.no_rawat_layanan=? or layanan_program_kfr.no_rawat=? "
                + "order by layanan_program_kfr.tanggal";

        try ( PreparedStatement psProgramKFR = koneksi.prepareStatement(sql)) {
            psProgramKFR.setString(1, norawat);
            psProgramKFR.setString(2, norawat);
            try ( ResultSet rsProgramKFR = psProgramKFR.executeQuery()) {
                if (rsProgramKFR.next()) {
                    appendLayananProgramKFRDocument(rsProgramKFR);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Layanan Program KFR : " + e);
        }
    }

    private void appendLayananProgramKFRDocument(ResultSet rsProgramKFR) throws SQLException {
        String kodeDokter = rsProgramKFR.getString("kd_dokter") == null ? "" : rsProgramKFR.getString("kd_dokter").trim().replace(" ", "_");
        String namaDokter = rsProgramKFR.getString("nm_dokter");
        generateQrDokterProgramKFR(kodeDokter);

        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>");
        htmlContent.append("<fieldset style='border: none; padding: 0; margin: 0;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 15px;'>LEMBAR PROGRAM KFR</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("No. RM", rsProgramKFR.getString("no_rkm_medis"), "Jenis Kelamin", rsProgramKFR.getString("jk"));
        appendInfoRow("Nama Pasien", rsProgramKFR.getString("nm_pasien"), "Tgl.Lahir/Umur", rsProgramKFR.getString("tgl_lahir") + " / " + rsProgramKFR.getString("umurdaftar") + " " + rsProgramKFR.getString("sttsumur"));
        appendSingleRow("Diagnosa", rsProgramKFR.getString("diagnosa_medis"));
        appendSingleRow("Permintaan Terapi", gabungPermintaanTerapiProgramKFR(rsProgramKFR));
        htmlContent.append("</table>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 10px; border: 1px solid #d9d9d9; margin-top: 2px;'>"
                + "<tr style='background: #ececec; font-weight: bold; text-align: center;'>"
                + "<td width='5%' rowspan='2' style='border: 1px solid #d9d9d9;'>No.</td>"
                + "<td width='35%' rowspan='2' style='border: 1px solid #d9d9d9;'>Program</td>"
                + "<td width='18%' rowspan='2' style='border: 1px solid #d9d9d9;'>Tanggal</td>"
                + "<td colspan='3' style='border: 1px solid #d9d9d9;'>Bukti Layanan</td>"
                + "</tr>"
                + "<tr style='background: #ececec; font-weight: bold; text-align: center;'>"
                + "<td width='14%' style='border: 1px solid #d9d9d9;'>Pasien</td>"
                + "<td width='14%' style='border: 1px solid #d9d9d9;'>Dokter</td>"
                + "<td width='14%' style='border: 1px solid #d9d9d9;'>Terapis</td>"
                + "</tr>");

        int nomor = 1;
        do {
            appendLayananProgramKFRRow(rsProgramKFR, nomor++);
        } while (rsProgramKFR.next());

        htmlContent.append("</table>");
        appendLayananProgramKFRSignature(kodeDokter, namaDokter);
        htmlContent.append("</fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private void appendLayananProgramKFRRow(ResultSet rsProgramKFR, int nomor) throws SQLException {
        String kodeTerapis = rsProgramKFR.getString("nip") == null ? "" : rsProgramKFR.getString("nip").trim().replace(" ", "_");
        generateQrPetugasProgramKFR(kodeTerapis);
        String photo = rsProgramKFR.getString("photo") == null ? "" : rsProgramKFR.getString("photo").trim();

        htmlContent.append("<tr>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 4px;'>").append(nomor).append("</td>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 4px;'>").append(formatMultiline(rsProgramKFR.getString("program"))).append("</td>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 4px;'>").append(escapeHtml(rsProgramKFR.getString("tanggal_format"))).append("</td>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 4px;'>");
        if (!photo.equals("") && !photo.equals("-")) {
            htmlContent.append("<img alt='Bukti Pasien' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                    .append(koneksiDB.HYBRIDWEB()).append("/layananprogramkfr/").append(escapeHtml(photo))
                    .append("' style='width: 72px; height: 72px; object-fit: cover;' onerror=\"this.style.display='none';\"/>");
        }
        htmlContent.append("</td>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 4px;'>")
                .append("<img width='72' height='72' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(rsProgramKFR.getString("kd_dokter") == null ? "" : rsProgramKFR.getString("kd_dokter").trim().replace(" ", "_"))).append(".png' onerror=\"this.style.display='none';\"/>")
                .append("</td>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 4px;'>")
                .append("<img width='72' height='72' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kodeTerapis)).append(".png' onerror=\"this.style.display='none';\"/>")
                .append("</td>")
                .append("</tr>");
    }

    private String gabungPermintaanTerapiProgramKFR(ResultSet rsProgramKFR) throws SQLException {
        String tatalaksana = rsProgramKFR.getString("tatalaksana");
        String evaluasi = rsProgramKFR.getString("evaluasi");
        return bersihkanRingkasProgramKFR(tatalaksana) + ". " + bersihkanRingkasProgramKFR(evaluasi);
    }

    private String bersihkanRingkasProgramKFR(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\t", "").replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", "; ").trim();
    }

    private void generateQrDokterProgramKFR(String kodeDokter) {
        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Program KFR: " + e.getMessage());
        }
    }

    private void generateQrPetugasProgramKFR(String kodePetugas) {
        try {
            if (!kodePetugas.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + urlEncode(kodePetugas));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR petugas Program KFR: " + e.getMessage());
        }
    }

    private void appendLayananProgramKFRSignature(String kodeDokter, String namaDokter) {
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9; border-top: 0;'>")
                .append("<tr>")
                .append("<td width='50%' align='center' style='border: 1px solid #d9d9d9;'>Tanggal dan Jam</td>")
                .append("<td width='50%' align='center' style='border: 1px solid #d9d9d9;'>Nama Dokter dan Tanda Tangan</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 10px;'>").append(escapeHtml(lokal)).append("</td>")
                .append("<td align='center' style='border: 1px solid #d9d9d9; padding: 6px;'>")
                .append("<img width='72' height='72' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kodeDokter)).append(".png' onerror=\"this.style.display='none';\"/>")
                .append("<br>").append(escapeHtml(namaDokter))
                .append("</td>")
                .append("</tr>")
                .append("</table>");
    }

    private void menampilkanKonsultasiMedik(String norawat) {
        if (!chkKonsultasiMedik.isSelected()) {
            return;
        }

        String sql = "select konsultasi_medik.no_permintaan,konsultasi_medik.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"
                + "if(pasien.jk='L','Laki-Laki','Perempuan') as jk,concat(reg_periksa.umurdaftar,' ',reg_periksa.sttsumur) as umur,"
                + "date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,date_format(konsultasi_medik.tanggal,'%d-%m-%Y %H:%i:%s') as tanggalkonsultasi,"
                + "date_format(konsultasi_medik.tanggal,'%d/%m/%Y') as tanggal_konsul_ttd,konsultasi_medik.jenis_permintaan,"
                + "konsultasi_medik.kd_dokter,dokterkonsul.nm_dokter as dokterkonsul,konsultasi_medik.kd_dokter_dikonsuli,dokterdikonsuli.nm_dokter as dokterdikonsuli,"
                + "konsultasi_medik.diagnosa_kerja as diagnosakerjakonsul,konsultasi_medik.uraian_konsultasi,"
                + "ifnull(date_format(jawaban_konsultasi_medik.tanggal,'%d-%m-%Y %H:%i:%s'),'') as tanggaljawaban,"
                + "ifnull(date_format(jawaban_konsultasi_medik.tanggal,'%d/%m/%Y'),'') as tanggal_jawaban_ttd,"
                + "ifnull(jawaban_konsultasi_medik.diagnosa_kerja,'') as diagnosakerjajawaban,"
                + "ifnull(jawaban_konsultasi_medik.uraian_jawaban,'') as uraian_jawaban "
                + "from konsultasi_medik inner join reg_periksa on konsultasi_medik.no_rawat=reg_periksa.no_rawat "
                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join dokter as dokterkonsul on konsultasi_medik.kd_dokter=dokterkonsul.kd_dokter "
                + "inner join dokter as dokterdikonsuli on konsultasi_medik.kd_dokter_dikonsuli=dokterdikonsuli.kd_dokter "
                + "left join jawaban_konsultasi_medik on jawaban_konsultasi_medik.no_permintaan=konsultasi_medik.no_permintaan "
                + "where konsultasi_medik.no_rawat=? order by konsultasi_medik.tanggal";

        try ( PreparedStatement psKonsultasi = koneksi.prepareStatement(sql)) {
            psKonsultasi.setString(1, norawat);
            try ( ResultSet rsKonsultasi = psKonsultasi.executeQuery()) {
                while (rsKonsultasi.next()) {
                    appendKonsultasiMedikRecord(rsKonsultasi);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Konsultasi Medik : " + e);
        }
    }

    private void appendKonsultasiMedikRecord(ResultSet rsKonsultasi) throws SQLException {
        String adaJawaban = nilai(rsKonsultasi, "tanggaljawaban") + nilai(rsKonsultasi, "diagnosakerjajawaban") + nilai(rsKonsultasi, "uraian_jawaban");

        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>");
        htmlContent.append("<fieldset style='border: none; padding: 0; margin: 0;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 14px;'>KONSULTASI MEDIK</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("Nama Pasien", rsKonsultasi.getString("nm_pasien"), "JK", rsKonsultasi.getString("jk"));
        appendInfoRow("No. R.M.", rsKonsultasi.getString("no_rkm_medis"), "Umur", rsKonsultasi.getString("umur"));
        appendInfoRow("Tanggal Lahir", rsKonsultasi.getString("tgl_lahir"), "No.Konsultasi", rsKonsultasi.getString("no_permintaan"));
        appendSingleRow("Tanggal Konsultasi", rsKonsultasi.getString("tanggalkonsultasi"));
        appendSingleRow("Permintaan", rsKonsultasi.getString("jenis_permintaan"));
        appendSingleRow("Kepada Yth. Teman Sejawat", rsKonsultasi.getString("dokterdikonsuli"));
        appendSingleRow("Diagnosa Kerja", rsKonsultasi.getString("diagnosakerjakonsul"));
        appendSingleRow("Uraian Konsultasi", rsKonsultasi.getString("uraian_konsultasi"));
        htmlContent.append("</table>");

        appendKonsultasiMedikSignature("Dokter Yang Konsul", rsKonsultasi.getString("tanggal_konsul_ttd"), rsKonsultasi.getString("kd_dokter"), rsKonsultasi.getString("dokterkonsul"));

        if (!adaJawaban.trim().equals("")) {
            appendSectionTitle("JAWABAN");
            htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
            appendSingleRow("Tanggal Jawaban", rsKonsultasi.getString("tanggaljawaban"));
            appendSingleRow("Kepada Yth. Teman Sejawat", rsKonsultasi.getString("dokterkonsul"));
            appendSingleRow("Diagnosa Kerja", rsKonsultasi.getString("diagnosakerjajawaban"));
            appendSingleRow("Jawaban Konsultasi", rsKonsultasi.getString("uraian_jawaban"));
            htmlContent.append("</table>");

            appendKonsultasiMedikSignature("Dokter Yang Dikonsuli", rsKonsultasi.getString("tanggal_jawaban_ttd"), rsKonsultasi.getString("kd_dokter_dikonsuli"), rsKonsultasi.getString("dokterdikonsuli"));
        }

        htmlContent.append("</fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private void appendKonsultasiMedikSignature(String label, String tanggal, String kodeDokterAsli, String namaDokter) {
        String kodeDokter = kodeDokterAsli == null ? "" : kodeDokterAsli.trim().replace(" ", "_");
        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Konsultasi Medik: " + e.getMessage());
        }

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; margin-top: 10px; font-size: 11px;'>");
        htmlContent.append("<tr><td width='60%'>&nbsp;</td><td width='40%' align='center'>");
        htmlContent.append(escapeHtml(akses.getkabupatenrs()));
        if (tanggal != null && !tanggal.trim().equals("")) {
            htmlContent.append(", ").append(escapeHtml(tanggal));
        }
        htmlContent.append("<br>").append(escapeHtml(label)).append("<br>");
        if (!kodeDokter.equals("")) {
            htmlContent.append("<img width='90' height='90' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                    .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kodeDokter)).append(".png' onerror=\"this.style.display='none';\"/>");
        }
        htmlContent.append("<br>").append(escapeHtml(namaDokter));
        htmlContent.append("</td></tr></table>");
    }

    private void menampilkanLaporanOperasiVK(String norawat) {
        if (!chkOperasiVK.isSelected()) {
            return;
        }

        String sql = "select operasi.no_rawat,reg_periksa.status_lanjut,pasien.no_rkm_medis,pasien.nm_pasien,"
                + "concat(reg_periksa.umurdaftar,' ',reg_periksa.sttsumur) as umur,"
                + "date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,"
                + "if(pasien.jk='L','Laki-Laki','Perempuan') as jk,"
                + "date_format(operasi.tgl_operasi,'%d-%m-%Y %H:%i:%s') as tgl_operasi,"
                + "date_format(operasi.tgl_operasi,'%d/%m/%Y') as tgl_operasi_ttd,"
                + "date_format(operasi.tgl_operasi,'%H:%i:%s') as jam_operasi,"
                + "operasi.tgl_operasi as tgl_operasi_raw,operasi.jenis_anasthesi,operasi.operator1,operasi.operator2,operasi.operator3,"
                + "operasi.asisten_operator1,operasi.asisten_operator2,operasi.asisten_operator3,operasi.instrumen,operasi.dokter_anak,"
                + "operasi.perawaat_resusitas,operasi.dokter_anestesi,operasi.asisten_anestesi,operasi.asisten_anestesi2,"
                + "operasi.bidan,operasi.bidan2,operasi.bidan3,operasi.perawat_luar,operasi.omloop,operasi.dokter_pjanak,operasi.dokter_umum,"
                + "(select group_concat(po2.nm_perawatan order by po2.nm_perawatan separator ', ') from operasi op2 inner join paket_operasi po2 on op2.kode_paket=po2.kode_paket where op2.no_rawat=operasi.no_rawat and op2.tgl_operasi=operasi.tgl_operasi) as nm_perawatan,"
                + "dokterop.nm_dokter as nm_operator1,dokterop2.nm_dokter as nm_operator2,"
                + "dokterop3.nm_dokter as nm_operator3,dokteranak.nm_dokter as nm_dokter_anak,dokteranestesi.nm_dokter as nm_dokter_anestesi,"
                + "dokterpjanak.nm_dokter as nm_dokter_pjanak,dokterumum.nm_dokter as nm_dokter_umum,"
                + "petugasas1.nama as nm_asisten_operator1,petugasas2.nama as nm_asisten_operator2,petugasas3.nama as nm_asisten_operator3,"
                + "petugasinstrumen.nama as nm_instrumen,petugasresusitas.nama as nm_perawaat_resusitas,"
                + "petugasanestesi.nama as nm_asisten_anestesi,petugasanestesi2.nama as nm_asisten_anestesi2,"
                + "petugasbidan.nama as nm_bidan,petugasbidan2.nama as nm_bidan2,petugasbidan3.nama as nm_bidan3,"
                + "petugasluar.nama as nm_perawat_luar,petugasomloop.nama as nm_omloop,"
                + "laporan_operasi.diagnosa_preop,laporan_operasi.diagnosa_postop,laporan_operasi.jaringan_dieksekusi,"
                + "date_format(laporan_operasi.selesaioperasi,'%d-%m-%Y %H:%i:%s') as selesaioperasi,"
                + "laporan_operasi.permintaan_pa,laporan_operasi.laporan_operasi,laporan_operasi.nomor_implan "
                + "from operasi inner join reg_periksa on operasi.no_rawat=reg_periksa.no_rawat "
                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join paket_operasi on operasi.kode_paket=paket_operasi.kode_paket "
                + "left join laporan_operasi on laporan_operasi.no_rawat=operasi.no_rawat and laporan_operasi.tanggal=operasi.tgl_operasi "
                + "left join dokter as dokterop on operasi.operator1=dokterop.kd_dokter "
                + "left join dokter as dokterop2 on operasi.operator2=dokterop2.kd_dokter "
                + "left join dokter as dokterop3 on operasi.operator3=dokterop3.kd_dokter "
                + "left join dokter as dokteranak on operasi.dokter_anak=dokteranak.kd_dokter "
                + "left join dokter as dokteranestesi on operasi.dokter_anestesi=dokteranestesi.kd_dokter "
                + "left join dokter as dokterpjanak on operasi.dokter_pjanak=dokterpjanak.kd_dokter "
                + "left join dokter as dokterumum on operasi.dokter_umum=dokterumum.kd_dokter "
                + "left join petugas as petugasas1 on operasi.asisten_operator1=petugasas1.nip "
                + "left join petugas as petugasas2 on operasi.asisten_operator2=petugasas2.nip "
                + "left join petugas as petugasas3 on operasi.asisten_operator3=petugasas3.nip "
                + "left join petugas as petugasinstrumen on operasi.instrumen=petugasinstrumen.nip "
                + "left join petugas as petugasresusitas on operasi.perawaat_resusitas=petugasresusitas.nip "
                + "left join petugas as petugasanestesi on operasi.asisten_anestesi=petugasanestesi.nip "
                + "left join petugas as petugasanestesi2 on operasi.asisten_anestesi2=petugasanestesi2.nip "
                + "left join petugas as petugasbidan on operasi.bidan=petugasbidan.nip "
                + "left join petugas as petugasbidan2 on operasi.bidan2=petugasbidan2.nip "
                + "left join petugas as petugasbidan3 on operasi.bidan3=petugasbidan3.nip "
                + "left join petugas as petugasluar on operasi.perawat_luar=petugasluar.nip "
                + "left join petugas as petugasomloop on operasi.omloop=petugasomloop.nip "
                + "where operasi.no_rawat=? and operasi.kode_paket=(select min(opx.kode_paket) from operasi opx where opx.no_rawat=operasi.no_rawat and opx.tgl_operasi=operasi.tgl_operasi) "
                + "order by operasi.tgl_operasi";

        try ( PreparedStatement psOperasi = koneksi.prepareStatement(sql)) {
            psOperasi.setString(1, norawat);
            try ( ResultSet rsOperasi = psOperasi.executeQuery()) {
                while (rsOperasi.next()) {
                    appendLaporanOperasiVKRecord(rsOperasi);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Laporan Operasi/VK : " + e);
        }
    }

    private void appendLaporanOperasiVKRecord(ResultSet rsOperasi) throws SQLException {
        OperasiPemeriksaan pemeriksaan = cariPemeriksaanSebelumOperasi(rsOperasi.getString("no_rawat"), rsOperasi.getString("tgl_operasi_raw"), rsOperasi.getString("status_lanjut"));
        String ruang = cariRuangOperasi(rsOperasi.getString("no_rawat"), rsOperasi.getString("status_lanjut"));
        String nomorImplan = rsOperasi.getString("nomor_implan") == null ? "" : rsOperasi.getString("nomor_implan").trim();
        String fileNomorImplan = safeQrBarcode(nomorImplan);

        try {
            if (!fileNomorImplan.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode2.php?barcode=" + urlEncode(fileNomorImplan));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR nomor implan Laporan Operasi: " + e.getMessage());
        }

        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>");
        htmlContent.append("<fieldset style='border: none; padding: 0; margin: 0;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-style: italic; font-size: 16px;'>LAPORAN OPERASI</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("Nama Pasien", rsOperasi.getString("nm_pasien"), "Tindakan", rsOperasi.getString("nm_perawatan"));
        appendInfoRow("Umur", rsOperasi.getString("umur"), "Nomor Implan", nomorImplan);
        appendInfoRow("Tgl Lahir", rsOperasi.getString("tgl_lahir"), "Ruang", ruang);
        appendInfoRow("No. Rekam Medis", rsOperasi.getString("no_rkm_medis"), "Jenis Kelamin", rsOperasi.getString("jk"));
        htmlContent.append("</table>");

        appendSectionTitle("PRE SURGICAL ASSESMENT");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("Tanggal", pemeriksaan.tanggal, "Waktu", pemeriksaan.jam);
        appendInfoRow("Dokter Bedah", rsOperasi.getString("nm_operator1"), "Alergi", pemeriksaan.alergi);
        appendSingleRow("Keluhan", pemeriksaan.keluhan);
        appendSingleRow("Pemeriksaan", buatPemeriksaanOperasi(pemeriksaan));
        appendSingleRow("Penilaian", pemeriksaan.penilaian);
        appendSingleRow("Tindak Lanjut", pemeriksaan.rtl);
        htmlContent.append("</table>");

        appendSectionTitle("POST SURGICAL REPORT");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("Tanggal &amp; Waktu", rsOperasi.getString("tgl_operasi"), "Tipe/Jenis Anastesi", rsOperasi.getString("jenis_anasthesi"));
        appendInfoRow("Dokter Bedah", rsOperasi.getString("nm_operator1"), "Asisten Bedah", rsOperasi.getString("nm_asisten_operator1"));
        appendInfoRow("Dokter Bedah 2", rsOperasi.getString("nm_operator2"), "Asisten Bedah 2", rsOperasi.getString("nm_asisten_operator2"));
        appendInfoRow("Perawat Resusitas", rsOperasi.getString("nm_perawaat_resusitas"), "Dokter Anastesi", rsOperasi.getString("nm_dokter_anestesi"));
        appendInfoRow("Instrumen", rsOperasi.getString("nm_instrumen"), "Asisten Anastesi", rsOperasi.getString("nm_asisten_anestesi"));
        appendInfoRow("Dokter Anak", rsOperasi.getString("nm_dokter_anak"), "Bidan", gabungNama(rsOperasi.getString("nm_bidan"), rsOperasi.getString("nm_bidan2"), rsOperasi.getString("nm_bidan3")));
        appendInfoRow("Dokter Umum", rsOperasi.getString("nm_dokter_umum"), "Onloop", rsOperasi.getString("nm_omloop"));
        appendInfoRow("Dikirim ke Pemeriksaan PA", rsOperasi.getString("permintaan_pa"), "Selesai Operasi", rsOperasi.getString("selesaioperasi"));
        appendSingleRow("Diagnosa Pre-Op / Pre Operation Diagnosis", rsOperasi.getString("diagnosa_preop"));
        appendSingleRow("Jaringan Yang di-Eksisi/-Insisi", rsOperasi.getString("jaringan_dieksekusi"));
        appendSingleRow("Diagnosa Post-Op / Post Operation Diagnosis", rsOperasi.getString("diagnosa_postop"));
        if (!nomorImplan.equals("")) {
            appendSingleHtmlRow("Nomor Implan", escapeHtml(nomorImplan) + "<br><img width='80' height='80' src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + escapeHtml(fileNomorImplan) + ".png'/>");
        }
        htmlContent.append("</table>");

        appendSectionTitle("REPORT ( PROCEDURES, SPECIFIC FINDINGS AND COMPLICATIONS )");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("", rsOperasi.getString("laporan_operasi"));
        htmlContent.append("</table>");

        appendLaporanOperasiSignature(rsOperasi);
        htmlContent.append("</fieldset></div>");
    }

    private OperasiPemeriksaan cariPemeriksaanSebelumOperasi(String norawat, String tanggalOperasi, String statusLanjut) {
        OperasiPemeriksaan pemeriksaan = new OperasiPemeriksaan();
        String tabel = "Ranap".equalsIgnoreCase(statusLanjut) ? "pemeriksaan_ranap" : "pemeriksaan_ralan";
        String sql = "select tgl_perawatan,jam_rawat,suhu_tubuh,tensi,nadi,respirasi,tinggi,berat,gcs,keluhan,pemeriksaan,alergi,rtl,penilaian "
                + "from " + tabel + " where no_rawat=? and concat(tgl_perawatan,' ',jam_rawat)<=? "
                + "order by tgl_perawatan desc,jam_rawat desc limit 1";

        try ( PreparedStatement psPemeriksaan = koneksi.prepareStatement(sql)) {
            psPemeriksaan.setString(1, norawat);
            psPemeriksaan.setString(2, tanggalOperasi);
            try ( ResultSet rsPemeriksaan = psPemeriksaan.executeQuery()) {
                if (rsPemeriksaan.next()) {
                    pemeriksaan.tanggal = rsPemeriksaan.getString("tgl_perawatan");
                    pemeriksaan.jam = rsPemeriksaan.getString("jam_rawat");
                    pemeriksaan.suhu = rsPemeriksaan.getString("suhu_tubuh");
                    pemeriksaan.tensi = rsPemeriksaan.getString("tensi");
                    pemeriksaan.nadi = rsPemeriksaan.getString("nadi");
                    pemeriksaan.respirasi = rsPemeriksaan.getString("respirasi");
                    pemeriksaan.tinggi = rsPemeriksaan.getString("tinggi");
                    pemeriksaan.berat = rsPemeriksaan.getString("berat");
                    pemeriksaan.gcs = rsPemeriksaan.getString("gcs");
                    pemeriksaan.keluhan = rsPemeriksaan.getString("keluhan");
                    pemeriksaan.pemeriksaan = rsPemeriksaan.getString("pemeriksaan");
                    pemeriksaan.alergi = rsPemeriksaan.getString("alergi");
                    pemeriksaan.rtl = rsPemeriksaan.getString("rtl");
                    pemeriksaan.penilaian = rsPemeriksaan.getString("penilaian");
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Pemeriksaan Laporan Operasi/VK : " + e);
        }

        return pemeriksaan;
    }

    private String cariRuangOperasi(String norawat, String statusLanjut) {
        if ("Ranap".equalsIgnoreCase(statusLanjut)) {
            return Sequel.cariIsi("select bangsal.nm_bangsal from bangsal inner join kamar on bangsal.kd_bangsal=kamar.kd_bangsal inner join kamar_inap on kamar_inap.kd_kamar=kamar.kd_kamar where kamar_inap.no_rawat=? order by kamar_inap.tgl_masuk desc limit 1", norawat);
        }

        return Sequel.cariIsi("select poliklinik.nm_poli from poliklinik inner join reg_periksa on reg_periksa.kd_poli=poliklinik.kd_poli where reg_periksa.no_rawat=?", norawat);
    }

    private String buatPemeriksaanOperasi(OperasiPemeriksaan pemeriksaan) {
        String hasil = pemeriksaan.pemeriksaan == null ? "" : pemeriksaan.pemeriksaan.trim();
        hasil = tambahBarisPemeriksaan(hasil, "Suhu Tubuh.(C)", pemeriksaan.suhu);
        hasil = tambahBarisPemeriksaan(hasil, "Tensi", pemeriksaan.tensi);
        hasil = tambahBarisPemeriksaan(hasil, "Tinggi (Cm)", pemeriksaan.tinggi);
        hasil = tambahBarisPemeriksaan(hasil, "Berat (Kg)", pemeriksaan.berat);
        hasil = tambahBarisPemeriksaan(hasil, "Nadi (/Mnt)", pemeriksaan.nadi);
        hasil = tambahBarisPemeriksaan(hasil, "Respirasi (/Mnt)", pemeriksaan.respirasi);
        hasil = tambahBarisPemeriksaan(hasil, "GCS (E,V,M)", pemeriksaan.gcs);
        return hasil;
    }

    private String tambahBarisPemeriksaan(String isi, String label, String value) {
        if (value == null || value.trim().equals("")) {
            return isi;
        }

        return (isi == null || isi.trim().equals("") ? "" : isi + "\n") + label + " : " + value;
    }

    private String gabungNama(String... daftarNama) {
        String hasil = "";
        for (String nama : daftarNama) {
            if (nama != null && !nama.trim().equals("") && !nama.trim().equals("-")) {
                hasil = hasil + (hasil.equals("") ? "" : ", ") + nama.trim();
            }
        }
        return hasil;
    }

    private void appendSingleHtmlRow(String label, String htmlValue) {
        htmlContent.append("<tr>")
                .append("<td width='24%' style='padding: 6px 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5; font-weight: bold;'>")
                .append(label).append("</td>")
                .append("<td colspan='3' width='76%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'>: ")
                .append(htmlValue == null || htmlValue.trim().equals("") ? "-" : htmlValue)
                .append("</td>")
                .append("</tr>");
    }

    private void appendLaporanOperasiSignature(ResultSet rsOperasi) throws SQLException {
        String kodeDokter = rsOperasi.getString("operator1") == null ? "" : rsOperasi.getString("operator1").replace(" ", "_");
        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Laporan Operasi/VK: " + e.getMessage());
        }

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; margin-top: 10px; font-size: 11px;'>");
        htmlContent.append("<tr><td width='60%'>&nbsp;</td><td width='40%' align='center'>");
        htmlContent.append(escapeHtml(akses.getkabupatenrs())).append(", ").append(escapeHtml(rsOperasi.getString("tgl_operasi_ttd")));
        htmlContent.append("<br>Dokter Bedah<br>");
        htmlContent.append("<img width='95' height='95' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/");
        htmlContent.append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kodeDokter)).append(".png' onerror=\"this.style.display='none';\"/>");
        htmlContent.append("<br>").append(escapeHtml(rsOperasi.getString("nm_operator1")));
        htmlContent.append("</td></tr></table>");
    }

    private static class OperasiPemeriksaan {

        String tanggal = "";
        String jam = "";
        String suhu = "";
        String tensi = "";
        String nadi = "";
        String respirasi = "";
        String tinggi = "";
        String berat = "";
        String gcs = "";
        String keluhan = "";
        String pemeriksaan = "";
        String alergi = "";
        String rtl = "";
        String penilaian = "";
    }

    private void menampilkanSuratKontrolBiasa(String norawat) {
        if (!chkSuratKontrolBiasa.isSelected()) {
            return;
        }

        String sql = "select skdp_bpjs.tahun,skdp_bpjs.no_rkm_medis,pasien.nm_pasien,skdp_bpjs.diagnosa,"
                + "skdp_bpjs.terapi,skdp_bpjs.alasan1,skdp_bpjs.alasan2,skdp_bpjs.rtl1,skdp_bpjs.rtl2,"
                + "date_format(skdp_bpjs.tanggal_datang,'%d-%m-%Y %H:%i:%s') as tanggal_datang,"
                + "date_format(skdp_bpjs.tanggal_datang,'%d-%m-%Y') as tanggal_datang_ttd,"
                + "date_format(skdp_bpjs.tanggal_rujukan,'%d-%m-%Y %H:%i:%s') as tanggal_rujukan,"
                + "date_format(skdp_bpjs.tanggal_rujukan,'%d-%m-%Y') as tanggal_rujukan_ttd,"
                + "skdp_bpjs.no_antrian,skdp_bpjs.kd_dokter,dokter.nm_dokter,skdp_bpjs.status,"
                + "skdp_bpjs.no_antrian as no_surat,booking_registrasi.kd_poli,poliklinik.nm_poli "
                + "from reg_periksa inner join skdp_bpjs on skdp_bpjs.no_rkm_medis=reg_periksa.no_rkm_medis "
                + "inner join pasien on skdp_bpjs.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join dokter on skdp_bpjs.kd_dokter=dokter.kd_dokter "
                + "left join booking_registrasi on booking_registrasi.no_rkm_medis=skdp_bpjs.no_rkm_medis "
                + "and booking_registrasi.tanggal_periksa=date(skdp_bpjs.tanggal_datang) "
                + "and booking_registrasi.kd_dokter=skdp_bpjs.kd_dokter "
                + "left join poliklinik on booking_registrasi.kd_poli=poliklinik.kd_poli "
                + "where reg_periksa.no_rawat=? and date(skdp_bpjs.tanggal_datang)=reg_periksa.tgl_registrasi "
                + "order by skdp_bpjs.tanggal_rujukan desc,skdp_bpjs.no_antrian desc limit 1";

        try ( PreparedStatement psKontrolBiasa = koneksi.prepareStatement(sql)) {
            psKontrolBiasa.setString(1, norawat);
            try ( ResultSet rsKontrolBiasa = psKontrolBiasa.executeQuery()) {
                if (rsKontrolBiasa.next()) {
                    appendSuratKontrolBiasaRecord(rsKontrolBiasa);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Surat Kontrol Biasa : " + e);
        }
    }

    private void appendSuratKontrolBiasaRecord(ResultSet rsKontrolBiasa) throws SQLException {
        String kdDokter = rsKontrolBiasa.getString("kd_dokter") == null ? "" : rsKontrolBiasa.getString("kd_dokter").trim().replace(" ", "_");
        String tanggalDatang = rsKontrolBiasa.getString("tanggal_datang");
        String poliTujuan = rsKontrolBiasa.getString("nm_poli");

        try {
            if (!kdDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kdDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR Surat Kontrol Biasa: " + e.getMessage());
        }

        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>");
        htmlContent.append("<fieldset style='border: none; padding: 0; margin: 0;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>SURAT KETERANGAN</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 12px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("NO. SURAT", rsKontrolBiasa.getString("no_surat"));
        appendSingleRow("NO. RM", rsKontrolBiasa.getString("no_rkm_medis"));
        appendSingleRow("NAMA", rsKontrolBiasa.getString("nm_pasien"));
        appendSingleRow("DIAGNOSA", rsKontrolBiasa.getString("diagnosa"));
        appendSingleRow("TERAPI", rsKontrolBiasa.getString("terapi"));
        htmlContent.append("</table>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 12px; border-left: 1px solid #d9d9d9; border-right: 1px solid #d9d9d9;'>");
        appendSingleRow("", "Tanggal surat rujukan " + rsKontrolBiasa.getString("tanggal_rujukan"));
        appendSingleRow("", "Belum dapat dikembalikan ke Fasilitas Perujuk dengan alasan");
        appendSingleRow("1", rsKontrolBiasa.getString("alasan1"));
        appendSingleRow("2", rsKontrolBiasa.getString("alasan2"));
        appendSingleRow("", "Rencana tindak lanjut yang akan diberikan pada kunjungan berikutnya");
        appendSingleRow("1", rsKontrolBiasa.getString("rtl1"));
        appendSingleRow("2", rsKontrolBiasa.getString("rtl2"));
        appendSingleRow("", "Surat keterangan ini digunakan untuk 1 (satu) kali kunjungan dengan diagnosa di atas pada :");
        appendSingleRow("Tanggal", (tanggalDatang == null ? "-" : tanggalDatang) + " di " + (poliTujuan == null || poliTujuan.trim().equals("") ? "-" : poliTujuan));
        htmlContent.append("</table>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 12px; border: 1px solid #d9d9d9; border-top: 0;'>");
        htmlContent.append("<tr><td width='58%'>&nbsp;</td><td width='42%' align='center' style='padding-top: 8px;'>");
        htmlContent.append(escapeHtml(akses.getkabupatenrs())).append(",<br>Dokter<br>");
        htmlContent.append("<img width='110' height='110' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/");
        htmlContent.append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(kdDokter)).append(".png' onerror=\"this.style.display='none';\"/>");
        htmlContent.append("<br>( ").append(escapeHtml(rsKontrolBiasa.getString("nm_dokter"))).append(" )");
        htmlContent.append("</td></tr></table>");
        htmlContent.append("</fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private void menampilkanSuratKontrol(String norawat) {
        if (!chkSuratKontrol.isSelected()) {
            return;
        }

        String sql = "select bridging_sep.no_rawat,bridging_sep.no_sep,bridging_sep.no_kartu,bridging_sep.nomr,"
                + "bridging_sep.nama_pasien,date_format(bridging_sep.tanggal_lahir,'%d %M %Y') as tanggal_lahir,"
                + "if(bridging_sep.jkel='L','LAKI-LAKI','PEREMPUAN') as jkel,bridging_sep.diagawal,bridging_sep.nmdiagnosaawal,"
                + "bridging_sep.noskdp,bridging_sep.kddpjp,bridging_sep.nmdpdjp,bridging_sep.nmpolitujuan,"
                + "date_format(bridging_sep.tglsep,'%e %M %Y') as tglsep,"
                + "date_format(coalesce(bridging_surat_kontrol_bpjs.tgl_surat,bridging_sep.tglsep),'%e %M %Y') as tgl_surat,"
                + "date_format(coalesce(bridging_surat_kontrol_bpjs.tgl_rencana,bridging_sep.tglsep),'%e %M %Y') as tgl_rencana,"
                + "coalesce(bridging_surat_kontrol_bpjs.no_surat,bridging_sep.noskdp) as no_surat,"
                + "coalesce(bridging_surat_kontrol_bpjs.kd_dokter_bpjs,bridging_sep.kddpjp) as kd_dokter_bpjs,"
                + "coalesce(bridging_surat_kontrol_bpjs.nm_dokter_bpjs,bridging_sep.nmdpdjp) as nm_dokter_bpjs,"
                + "coalesce(bridging_surat_kontrol_bpjs.nm_poli_bpjs,bridging_sep.nmpolitujuan) as nm_poli_bpjs "
                + "from bridging_sep left join bridging_surat_kontrol_bpjs on bridging_surat_kontrol_bpjs.no_sep=bridging_sep.no_sep "
                + "where bridging_sep.no_rawat=? and coalesce(bridging_surat_kontrol_bpjs.no_surat,bridging_sep.noskdp,'')<>'' "
                + "order by bridging_surat_kontrol_bpjs.tgl_rencana";

        try ( PreparedStatement psKontrol = koneksi.prepareStatement(sql)) {
            psKontrol.setString(1, norawat);
            try ( ResultSet rsKontrol = psKontrol.executeQuery()) {
                while (rsKontrol.next()) {
                    appendSuratKontrolRecord(rsKontrol);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Surat Kontrol : " + e);
        }
    }

    private void appendSuratKontrolRecord(ResultSet rsKontrol) throws SQLException {
        String noSurat = rsKontrol.getString("no_surat");
        String barcodeSurat = noSurat == null ? "" : noSurat.replace(" ", "_");
        String qrDpjp = safeQrBarcode(makeSuratKontrolFinger(rsKontrol));

        try {
            if (!barcodeSurat.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/generatebarcodesep.php?sepbpjs=" + urlEncode(barcodeSurat));
                http.executeMethod(get);
            }
            if (!qrDpjp.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode2.php?barcode=" + urlEncode(qrDpjp));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR DPJP Surat Kontrol: " + e.getMessage());
        }

        htmlContent.append(
                "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%;'>"
                + "<fieldset style='border: none; padding: 0; margin: 0;'>"
                + "<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 12px;'>"
                + "<tr>"
                + "<td width='32%' style='padding: 16px 10px 8px 10px; vertical-align: top;'>"
                + "<img alt='BPJS Kesehatan' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/images/bpjslogo.png' style='width: 210px; max-height: 60px;'/>")
                .append("</td>"
                        + "<td width='42%' style='padding: 16px 10px 8px 10px; vertical-align: top;'>"
                        + "<div style='font-size: 20px !important; font-weight: bold;'>SURAT RENCANA KONTROL</div>"
                        + "<div style='font-size: 16px !important;'>").append(escapeHtml(akses.getnamars())).append("</div>"
                + "</td>"
                + "<td width='26%' style='padding: 16px 10px 8px 10px; vertical-align: top;'>"
                + "<div style='font-size: 16px !important;'>No. ").append(escapeHtml(noSurat)).append("</div>"
                + "<div style='font-size: 16px !important;'>Tgl. ").append(escapeHtml(rsKontrol.getString("tgl_rencana"))).append("</div>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td colspan='2' style='padding: 28px 10px 6px 10px; vertical-align: top;'>"
                + "<table width='100%' border='0' cellpadding='2px' cellspacing='0' style='border-collapse: collapse;'>"
                + "<tr><td width='28%' style='font-size: 14px !important;'>Kepada Yth</td><td width='4%' style='font-size: 14px !important;'>:</td><td width='68%' style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("nm_dokter_bpjs"))).append("</td></tr>"
                + "<tr><td></td><td></td><td style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("nm_poli_bpjs"))).append("</td></tr>"
                + "</table>"
                + "<div style='font-size: 14px !important; margin-top: 12px;'>Mohon Pemeriksaan dan Penanganan Lebih Lanjut :</div>"
                + "<table width='100%' border='0' cellpadding='2px' cellspacing='0' style='border-collapse: collapse;'>"
                + "<tr><td width='28%' style='font-size: 14px !important;'>No. Kartu</td><td width='4%' style='font-size: 14px !important;'>:</td><td width='68%' style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("no_kartu"))).append("</td></tr>"
                + "<tr><td style='font-size: 14px !important;'>Nama Pasien</td><td style='font-size: 14px !important;'>:</td><td style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("nama_pasien"))).append(" ( ").append(escapeHtml(rsKontrol.getString("jkel"))).append(" )</td></tr>"
                + "<tr><td style='font-size: 14px !important;'>Tgl. Lahir</td><td style='font-size: 14px !important;'>:</td><td style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("tanggal_lahir"))).append("</td></tr>"
                + "<tr><td style='font-size: 14px !important;'>Diagnosa Awal</td><td style='font-size: 14px !important;'>:</td><td style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("nmdiagnosaawal"))).append("</td></tr>"
                + "<tr><td style='font-size: 14px !important;'>Tgl. Entri</td><td style='font-size: 14px !important;'>:</td><td style='font-size: 14px !important;'>").append(escapeHtml(rsKontrol.getString("tgl_surat"))).append("</td></tr>"
                + "</table>"
                + "<div style='font-size: 14px !important; margin-top: 10px;'>Demikian atas bantuannya diucapkan banyak terima kasih</div>"
                + "</td>"
                + "<td style='padding: 28px 10px 6px 10px; text-align: center; vertical-align: top;'>"
                + "<img alt='Barcode Surat Kontrol' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/berkasrawat/temp/barcode_").append(escapeHtml(barcodeSurat)).append(".png' style='width: 230px; height: 48px;' onerror=\"this.style.display='none';\"/>"
                + "<div style='margin-top: 95px; font-size: 14px !important;'>Mengetahui</div>"
                + "<img width='110' height='110' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(escapeHtml(qrDpjp)).append(".png' onerror=\"this.style.display='none';\"/>"
                + "<div style='border-top: 1px solid #111; width: 170px; margin: 10px auto 0 auto;'>&nbsp;</div>"
                + "</td>"
                + "</tr>"
                + "<tr><td colspan='3' style='padding: 42px 10px 18px 10px; font-size: 11px !important;'>Tgl. Cetak&nbsp;&nbsp;").append(escapeHtml(lokal)).append("</td></tr>"
                + "</table>"
                + "</fieldset></div>");
        appendPageBreakAfterDocument();
    }

    private void appendAsuhanMedisIgdRecord(ResultSet rsMedis) throws SQLException {
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("No. RM", rsMedis.getString("no_rkm_medis"), "Jenis Kelamin", rsMedis.getString("jk"));
        appendInfoRow("Nama Pasien", rsMedis.getString("nm_pasien"), "Tanggal", rsMedis.getString("tanggal"));
        appendInfoRow("Tanggal Lahir", rsMedis.getString("tgl_lahir"), "Anamnesis", gabungAnamnesis(rsMedis.getString("anamnesis"), rsMedis.getString("hubungan")));
        htmlContent.append("</table>");

        appendSectionTitle("I. RIWAYAT KESEHATAN");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("Keluhan Utama", rsMedis.getString("keluhan_utama"));
        appendSingleRow("Riwayat Penyakit Sekarang", rsMedis.getString("rps"));
        appendInfoRow("Riwayat Penyakit Dahulu", rsMedis.getString("rpd"), "Riwayat Penyakit Keluarga", rsMedis.getString("rpk"));
        appendInfoRow("Riwayat Pengobatan", rsMedis.getString("rpo"), "Riwayat Alergi", rsMedis.getString("alergi"));
        htmlContent.append("</table>");

        appendSectionTitle("II. PEMERIKSAAN FISIK");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("Keadaan Umum", rsMedis.getString("keadaan"), "Kesadaran", rsMedis.getString("kesadaran"));
        appendInfoRow("GCS(E,V,M)", rsMedis.getString("gcs"), "Tanda Vital", "TD : " + rsMedis.getString("td") + " mmHg, N : " + rsMedis.getString("nadi") + " x/m, R : " + rsMedis.getString("rr") + " x/m, S : " + rsMedis.getString("suhu") + " C, SPO2 : " + rsMedis.getString("spo") + " %");
        appendInfoRow("BB", rsMedis.getString("bb") + " Kg", "TB", rsMedis.getString("tb") + " Cm");
        appendInfoRow("Kepala", rsMedis.getString("kepala"), "Thoraks", rsMedis.getString("thoraks"));
        appendInfoRow("Mata", rsMedis.getString("mata"), "Abdomen", rsMedis.getString("abdomen"));
        appendInfoRow("Gigi &amp; Mulut", rsMedis.getString("gigi"), "Genital &amp; Anus", rsMedis.getString("genital"));
        appendInfoRow("Leher", rsMedis.getString("leher"), "Ekstremitas", rsMedis.getString("ekstremitas"));
        appendSingleRow("Keterangan Fisik", rsMedis.getString("ket_fisik"));
        htmlContent.append("</table>");

        appendSectionTitle("III. STATUS LOKALIS");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
                + "<tr><td colspan='4' align='center' style='padding: 8px; border: 1px solid #e0e0e0;'>"
                + "<img alt='Gambar Lokalis' src='").append(getClass().getResource("/picture/semua.png")).append("' style='width: 100%; height: auto; object-fit: contain;'/>"
                + "</td></tr>");
        appendSingleRow("Keterangan", rsMedis.getString("ket_lokalis"));
        htmlContent.append("</table>");

        appendSectionTitle("IV. PEMERIKSAAN PENUNJANG");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendThreeColumnRow("EKG", rsMedis.getString("ekg"), "Radiologi", rsMedis.getString("rad"), "Laboratorium", rsMedis.getString("lab"));
        htmlContent.append("</table>");

        appendSectionTitle("V. DIAGNOSIS");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("", rsMedis.getString("diagnosis"));
        htmlContent.append("</table>");

        appendSectionTitle("VI. TATALAKSANA");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("", rsMedis.getString("tata"));
        htmlContent.append("</table>");

        appendAsuhanMedisIgdSignature(rsMedis);
    }

    private void appendAsuhanMedisIgdSignature(ResultSet rsMedis) throws SQLException {
        String kodeDokter = rsMedis.getString("kd_dokter") == null ? "" : rsMedis.getString("kd_dokter").replace(" ", "_");
        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Asuhan Medis IGD: " + e.getMessage());
        }

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; margin-top: 10px;'>"
                + "<tr>"
                + "<td width='50%' align='center' style='border-top: 1px solid #d9d9d9; padding-top: 8px;'>Tanggal dan Jam<br><br>")
                .append(escapeHtml(rsMedis.getString("tanggal")))
                .append("</td>"
                        + "<td width='50%' align='center' style='border-top: 1px solid #d9d9d9; padding-top: 8px;'>Nama Dokter dan Tanda Tangan<br>")
                .append("<img width='90' height='90' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(kodeDokter).append(".png'/>")
                .append("<br>").append(escapeHtml(rsMedis.getString("nm_dokter")))
                .append("</td></tr></table>");
    }

    private void menampilkanAsuhanKeperawatanRawatInapNeonatus(String norawat) {
        if (!chkAsuhanKeperawatanRanapNeonatus.isSelected()) {
            return;
        }

        String sql = "select penilaian_awal_keperawatan_ranap_neonatus.*,date_format(penilaian_awal_keperawatan_ranap_neonatus.tanggal,'%d-%m-%Y %H:%i:%s') as tanggal_format,"
                + "reg_periksa.no_rkm_medis,pasien.nm_pasien,if(pasien.jk='L','Laki-Laki','Perempuan') as jk,date_format(pasien.tgl_lahir,'%d-%m-%Y') as tgl_lahir,"
                + "reg_periksa.umurdaftar,reg_periksa.sttsumur,pengkaji1.nama as pengkaji1,pengkaji2.nama as pengkaji2,dokter.nm_dokter "
                + "from penilaian_awal_keperawatan_ranap_neonatus inner join reg_periksa on penilaian_awal_keperawatan_ranap_neonatus.no_rawat=reg_periksa.no_rawat "
                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "left join petugas as pengkaji1 on penilaian_awal_keperawatan_ranap_neonatus.nip1=pengkaji1.nip "
                + "left join petugas as pengkaji2 on penilaian_awal_keperawatan_ranap_neonatus.nip2=pengkaji2.nip "
                + "left join dokter on penilaian_awal_keperawatan_ranap_neonatus.kd_dokter=dokter.kd_dokter "
                + "where penilaian_awal_keperawatan_ranap_neonatus.no_rawat=? limit 1";

        try ( PreparedStatement psNeonatus = koneksi.prepareStatement(sql)) {
            psNeonatus.setString(1, norawat);
            try ( ResultSet rsNeonatus = psNeonatus.executeQuery()) {
                if (rsNeonatus.next()) {
                    appendAsuhanKeperawatanRawatInapNeonatusRecord(rsNeonatus, norawat);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Asuhan Keperawatan Rawat Inap Neonatus : " + e);
        }
    }

    private void appendAsuhanKeperawatanRawatInapNeonatusRecord(ResultSet rsNeonatus, String norawat) throws SQLException {
        String kodePetugas = nilai(rsNeonatus, "nip1").replace(" ", "_");
        try {
            if (!kodePetugas.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + urlEncode(kodePetugas));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR petugas Awal Keperawatan Ranap Neonatus: " + e.getMessage());
        }

        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box; font-size: 11px;'>");
        Copsurat();
        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 15px;'>PENILAIAN AWAL KEPERAWATAN RAWAT INAP NEONATUS</div>");

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("No. RM", nilai(rsNeonatus, "no_rkm_medis"), "Tanggal Kunjungan", nilai(rsNeonatus, "tanggal_format"));
        appendInfoRow("Nama Pasien", nilai(rsNeonatus, "nm_pasien"), "Diperoleh Dari", nilai(rsNeonatus, "diperoleh_dari"));
        appendInfoRow("Tanggal Lahir", nilai(rsNeonatus, "tgl_lahir"), "Hubungan Dengan Pasien", nilai(rsNeonatus, "hubungan_dengan_pasien"));
        appendInfoRow("Jenis Kelamin", nilai(rsNeonatus, "jk"), "Cara Masuk", nilai(rsNeonatus, "cara_masuk"));
        appendInfoRow("Umur", nilai(rsNeonatus, "umurdaftar") + " " + nilai(rsNeonatus, "sttsumur"), "Asal Pasien", nilai(rsNeonatus, "asal_pasien"));
        appendInfoRow("DPJP", nilai(rsNeonatus, "kd_dokter") + " " + nilai(rsNeonatus, "nm_dokter"), "Pengkaji", nilai(rsNeonatus, "nip1") + " " + nilai(rsNeonatus, "pengkaji1"));
        htmlContent.append("</table>");

        appendNeonatusSection("I. RIWAYAT KESEHATAN");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("Keluhan Utama", nilai(rsNeonatus, "keluhan_utama"));
        appendSingleRow("Riwayat Prenatal", "Riwayat Obstetri : G " + nilai(rsNeonatus, "prenatal_g") + " P " + nilai(rsNeonatus, "prenatal_p") + " A " + nilai(rsNeonatus, "prenatal_a") + " UK " + nilai(rsNeonatus, "prenatal_uk")
                + "\nRiwayat Penyakit Ibu : " + gabungNeonatus(rsNeonatus, "prenatal_riwayat_penyakit_ibu", "prenatal_riwayat_penyakit_ibu_keterangan")
                + "\nRiwayat Pengobatan Ibu Selama Hamil : " + nilai(rsNeonatus, "prenatal_riwayat_pengobatan_ibu_selama_hamil")
                + "\nPernah Dirawat : " + gabungNeonatus(rsNeonatus, "prenatal_pernah_dirawat", "prenatal_pernah_dirawat_keterangan")
                + "\nStatus Gizi Ibu : " + nilai(rsNeonatus, "prenatal_status_gizi_ibu"));
        appendSingleRow("Riwayat Intranatal", "Riwayat Obstetri : G " + nilai(rsNeonatus, "intranatal_g") + " P " + nilai(rsNeonatus, "intranatal_p") + " A " + nilai(rsNeonatus, "intranatal_a")
                + "\nKondisi Saat Lahir : " + nilai(rsNeonatus, "intranatal_kondisi_lahir")
                + "\nCara Persalinan : " + gabungNeonatus(rsNeonatus, "intranatal_cara_persalinan", "intranatal_cara_persalinan_keterangan")
                + "\nAPGAR : " + nilai(rsNeonatus, "intranatal_apgar") + ", Letak : " + nilai(rsNeonatus, "intranatal_letak") + ", Tali Pusat : " + nilai(rsNeonatus, "intranatal_tali_pusat")
                + "\nKetuban : " + nilai(rsNeonatus, "intranatal_ketuban")
                + "\nAntopometri BBL : BB " + nilai(rsNeonatus, "intranatal_bb") + " gr, PB " + nilai(rsNeonatus, "intranatal_pb") + " cm, LK " + nilai(rsNeonatus, "intranatal_lk") + " cm, LD " + nilai(rsNeonatus, "intranatal_ld") + " cm, LP " + nilai(rsNeonatus, "intranatal_lp") + " cm");
        appendInfoRow("Risiko Infeksi Mayor", gabungNeonatus(rsNeonatus, "risiko_infeksi_mayor", "risiko_infeksi_mayor_keterangan"), "Risiko Infeksi Minor", gabungNeonatus(rsNeonatus, "risiko_infeksi_minor", "risiko_infeksi_minor_keterangan"));
        appendSingleRow("Kebutuhan Biologis", "Nutrisi : " + gabungNeonatus(rsNeonatus, "kebutuhan_biologis_nutrisi", "kebutuhan_biologis_nutrisi_keterangan")
                + ", Frekuensi : " + nilai(rsNeonatus, "kebutuhan_biologis_nutrisi_frekuensi") + " cc / " + nilai(rsNeonatus, "kebutuhan_biologis_nutrisi_kali") + " x"
                + "\nBAK : " + gabungNeonatus(rsNeonatus, "kebutuhan_biologis_bak", "kebutuhan_biologis_bak_keterangan")
                + "\nBAB : " + gabungNeonatus(rsNeonatus, "kebutuhan_biologis_bab", "kebutuhan_biologis_bab_keterangan"));
        appendSingleRow("Alergi/Reaksi Orang Tua", "Alergi Obat : " + gabungNeonatus(rsNeonatus, "alergi_obat", "alergi_obat_keterangan") + ", Reaksi : " + nilai(rsNeonatus, "alergi_obat_reaksi")
                + "\nAlergi Makanan : " + gabungNeonatus(rsNeonatus, "alergi_makanan", "alergi_makanan_keterangan") + ", Reaksi : " + nilai(rsNeonatus, "alergi_makanan_reaksi")
                + "\nAlergi Lainnya : " + gabungNeonatus(rsNeonatus, "alergi_lainnya", "alergi_lainnya_keterangan") + ", Reaksi : " + nilai(rsNeonatus, "alergi_lainnya_reaksi"));
        appendInfoRow("Riwayat Penyakit Keluarga", gabungNeonatus(rsNeonatus, "riwayat_penyakit_keluarga", "riwayat_penyakit_keluarga_keterangan"), "Riwayat Imunisasi", gabungNeonatus(rsNeonatus, "riwayat_imunisasi", "riwayat_imunisasi_keterangan"));
        appendInfoRow("Riwayat Tranfusi Darah", gabungNeonatus(rsNeonatus, "riwayat_tranfusi_darah", "riwayat_tranfusi_darah_keterangan") + ", Reaksi : " + gabungNeonatus(rsNeonatus, "riwayat_tranfusi_darah_reaksi", "riwayat_tranfusi_darah_reaksi_keterangan"), "Kebiasaan Ibu", "Obat : " + gabungNeonatus(rsNeonatus, "kebiasan_ibu_obat_diminum", "kebiasan_ibu_obat_diminum_keterangan") + "\nNarkoba : " + gabungNeonatus(rsNeonatus, "kebiasan_ibu_narkoba", "kebiasan_ibu_narkoba_keterangan") + "\nMerokok : " + gabungNeonatus(rsNeonatus, "kebiasan_ibu_merokok", "kebiasan_ibu_merokok_keterangan") + "\nAlkohol : " + gabungNeonatus(rsNeonatus, "kebiasan_ibu_alkohol", "kebiasan_ibu_alkohol_keterangan"));
        htmlContent.append("</table>");

        appendNeonatusSection("II. PEMERIKSAAN FISIK");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("Tanda Vital & Antropometri", "Kesadaran : " + nilai(rsNeonatus, "kesadaran") + ", Keadaan Umum : " + nilai(rsNeonatus, "keadaan_umum") + ", GCS(E,V,M) : " + nilai(rsNeonatus, "gcs")
                + "\nTD : " + nilai(rsNeonatus, "td") + " mmHg, Suhu : " + nilai(rsNeonatus, "suhu") + " C, HR : " + nilai(rsNeonatus, "hr") + " x/menit, RR : " + nilai(rsNeonatus, "rr") + " x/menit, SpO2 : " + nilai(rsNeonatus, "spo2") + " %, Down Score : " + nilai(rsNeonatus, "down_score")
                + "\nBB : " + nilai(rsNeonatus, "bb") + " Kg, TB : " + nilai(rsNeonatus, "tb") + " cm, LK : " + nilai(rsNeonatus, "lk") + " cm, LD : " + nilai(rsNeonatus, "ld") + " cm, LP : " + nilai(rsNeonatus, "lp") + " cm"
                + "\nGolongan Darah Bayi : " + nilai(rsNeonatus, "gd_bayi") + ", Ibu : " + nilai(rsNeonatus, "gd_ibu") + ", Ayah : " + nilai(rsNeonatus, "gd_ayah"));
        appendSingleRow("Sistem Susunan Saraf Pusat", "Gerak Bayi : " + nilai(rsNeonatus, "saraf_pusat_gerak_bayi")
                + "\nKepala : " + gabungNeonatus(rsNeonatus, "saraf_pusat_kepala", "saraf_pusat_kepala_keterangan")
                + "\nUbun-ubun : " + gabungNeonatus(rsNeonatus, "saraf_pusat_ubunubun", "saraf_pusat_ubunubun_keterangan")
                + "\nWajah : " + gabungNeonatus(rsNeonatus, "saraf_pusat_wajah", "saraf_pusat_wajah_keterangan")
                + "\nKejang : " + gabungNeonatus(rsNeonatus, "saraf_pusat_kejang", "saraf_pusat_kejang_keterangan")
                + "\nRefleks : " + gabungNeonatus(rsNeonatus, "saraf_pusat_refleks", "saraf_pusat_refleks_keterangan")
                + "\nTangis Bayi : " + gabungNeonatus(rsNeonatus, "saraf_pusat_tangisbayi", "saraf_pusat_tangisbayi_keterangan"));
        appendInfoRow("Kardiovaskuler", "Denyut Nadi : " + nilai(rsNeonatus, "kardiovaskular_denyutnadi") + "\nSirkulasi : " + gabungNeonatus(rsNeonatus, "kardiovaskular_sirkulasi", "kardiovaskular_sirkulasi_keterangan") + "\nPulsasi : " + gabungNeonatus(rsNeonatus, "kardiovaskular_pulsasi", "kardiovaskular_pulsasi_keterangan"), "Respirasi", "Pola Napas : " + nilai(rsNeonatus, "respirasi_polanafas") + "\nJenis Pernapasan : " + gabungNeonatus(rsNeonatus, "respirasi_jenispernapasan", "respirasi_jenispernapasan_keterangan") + "\nRetraksi : " + nilai(rsNeonatus, "respirasi_retraksi") + "\nAir Entry : " + nilai(rsNeonatus, "respirasi_airentry") + "\nMerintih : " + nilai(rsNeonatus, "respirasi_merintih") + "\nSuara Napas : " + nilai(rsNeonatus, "respirasi_suara_napas"));
        appendInfoRow("Gastrointestinal", "Mulut : " + gabungNeonatus(rsNeonatus, "gastrointestinal_mulut", "gastrointestinal_mulut_keterangan") + "\nLidah : " + gabungNeonatus(rsNeonatus, "gastrointestinal_lidah", "gastrointestinal_lidah_keterangan") + "\nTenggorokan : " + gabungNeonatus(rsNeonatus, "gastrointestinal_tenggorakan", "gastrointestinal_tenggorakan_keterangan") + "\nAbdomen : " + gabungNeonatus(rsNeonatus, "gastrointestinal_abdomen", "gastrointestinal_abdomen_keterangan") + "\nBAB : " + gabungNeonatus(rsNeonatus, "gastrointestinal_bab", "gastrointestinal_bab_keterangan") + "\nWarna BAB : " + gabungNeonatus(rsNeonatus, "gastrointestinal_warnabab", "gastrointestinal_warnabab_keterangan") + "\nBAK : " + gabungNeonatus(rsNeonatus, "gastrointestinal_bak", "gastrointestinal_bak_keterangan") + "\nWarna BAK : " + gabungNeonatus(rsNeonatus, "gastrointestinal_bakwarna", "gastrointestinal_bakwarna_keterangan"), "Neurologi", "Posisi Mata : " + nilai(rsNeonatus, "neurologi_posisi_mata") + "\nKelopak Mata : " + gabungNeonatus(rsNeonatus, "neurologi_kelopak_mata", "neurologi_kelopak_mata_keterangan") + "\nBesar Pupil : " + nilai(rsNeonatus, "neurologi_besar_pupil") + "\nKonjungtiva : " + gabungNeonatus(rsNeonatus, "neurologi_konjugtiva", "neurologi_konjugtiva_keterangan") + "\nSklera : " + gabungNeonatus(rsNeonatus, "neurologi_sklera", "neurologi_sklera_keterangan") + "\nPendengaran : " + gabungNeonatus(rsNeonatus, "neurologi_pendengaran", "neurologi_pendengaran_keterangan") + "\nPenciuman : " + gabungNeonatus(rsNeonatus, "neurologi_penciuman", "neurologi_penciuman_keterangan"));
        appendSingleRow("Integument, Reproduksi, Muskuloskeletal", "Warna Kulit : " + gabungNeonatus(rsNeonatus, "integument_warna_kulit", "integument_warna_kulit_keterangan")
                + "\nVernic Kaseosa : " + gabungNeonatus(rsNeonatus, "integument_vernic_kaseosa", "integument_vernic_kaseosa_keterangan") + ", Turgor : " + nilai(rsNeonatus, "integument_turgor") + ", Lanugo : " + nilai(rsNeonatus, "integument_lanugo") + ", Kulit : " + nilai(rsNeonatus, "integument_kulit") + ", Risiko Dekubitas : " + nilai(rsNeonatus, "integument_risiko_dekubitas")
                + "\nReproduksi : " + gabungNeonatus(rsNeonatus, "reproduksi", "reproduksi_keterangan")
                + "\nRekoil Telinga : " + gabungNeonatus(rsNeonatus, "muskuloskeletal_rekoil_telinga", "muskuloskeletal_rekoil_telinga_keterangan") + ", Lengan : " + gabungNeonatus(rsNeonatus, "muskuloskeletal_lengan", "muskuloskeletal_lengan_keterangan") + ", Tungkai : " + gabungNeonatus(rsNeonatus, "muskuloskeletal_tungkai", "muskuloskeletal_tungkai_keterangan") + ", Garis Telapak Kaki : " + nilai(rsNeonatus, "muskuloskeletal_telapak_kaki"));
        htmlContent.append("</table>");

        appendNeonatusSection("III. RIWAYAT PSIKOLOGIS - SOSIAL - EKONOMI - BUDAYA - SPIRITUAL (ORANGTUA)");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("", "Kondisi Psikologis : " + nilai(rsNeonatus, "kondisi_psikologis") + ", Gangguan Jiwa Di Masa Lalu : " + nilai(rsNeonatus, "gangguan_jiwa") + ", Menerima Kondisi Bayi : " + nilai(rsNeonatus, "menerima_kondisi_bayi")
                + "\nStatus Menikah : " + nilai(rsNeonatus, "status_menikah") + ", Masalah Pernikahan : " + gabungNeonatus(rsNeonatus, "masalah_pernikahan", "masalah_pernikahan_keterangan") + ", Pekerjaan : " + nilai(rsNeonatus, "pekerjaan")
                + "\nAgama : " + nilai(rsNeonatus, "agama") + ", Nilai Kepercayaan/Budaya : " + gabungNeonatus(rsNeonatus, "nilai_kepercayaan", "nilai_kepercayaan_keterangan") + ", Suku : " + nilai(rsNeonatus, "suku") + ", Pendidikan : " + nilai(rsNeonatus, "pendidikan") + ", Pembayaran : " + nilai(rsNeonatus, "pembayaran")
                + "\nTinggal Bersama : " + gabungNeonatus(rsNeonatus, "tinggal_bersama", "tinggal_bersama_keterangan") + ", Hubungan Keluarga : " + nilai(rsNeonatus, "hubungan_keluarga") + ", Respon Emosi : " + nilai(rsNeonatus, "respon_emosi"));
        htmlContent.append("</table>");

        appendNeonatusSection("IV. KEBUTUHAN KOMUNIKASI DAN BELAJAR/EDUKASI (ORANGTUA)");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendSingleRow("", "Bahasa Sehari-hari : " + nilai(rsNeonatus, "bahasa_sehari_hari") + ", Kemampuan Baca & Tulis : " + nilai(rsNeonatus, "kemampuan_bacatulis") + ", Butuh Penterjemah : " + gabungNeonatus(rsNeonatus, "butuh_penterjemah", "butuh_penterjemah_keterangan")
                + "\nTerdapat Hambatan Belajar : " + nilai(rsNeonatus, "terdapat_hambatan_belajar") + ", Hambatan Belajar : " + gabungNeonatus(rsNeonatus, "hambatan_belajar", "hambatan_belajar_keterangan") + ", Cara Bicara : " + nilai(rsNeonatus, "hambatan_cara_bicara") + ", Bahasa Isyarat : " + nilai(rsNeonatus, "hambatan_bahasa_isyarat")
                + "\nCara Belajar Disukai : " + nilai(rsNeonatus, "cara_belajar_disukai") + ", Kesediaan Menerima Informasi : " + gabungNeonatus(rsNeonatus, "kesediaan_menerima_informasi", "kesediaan_menerima_informasi_keterangan")
                + "\nPemahaman Nutrisi/Diet : " + nilai(rsNeonatus, "pemahaman_nutrisi") + ", Penyakit : " + nilai(rsNeonatus, "pemahaman_penyakit") + ", Pengobatan : " + nilai(rsNeonatus, "pemahaman_pengobatan") + ", Perawatan : " + nilai(rsNeonatus, "pemahaman_perawatan"));
        htmlContent.append("</table>");

        appendNeonatusSkrining(rsNeonatus);

        appendNeonatusSection("VIII. PERENCANAAN PULANG (DISCHARGE PLANNING)");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendInfoRow("Informasi Perencanaan Pulang", nilai(rsNeonatus, "informasi_perencanaan_pulang"), "Lama Rawat Rata-rata", nilai(rsNeonatus, "lama_ratarata"));
        appendInfoRow("Perencanaan Pulang", nilai(rsNeonatus, "perencanaan_pulang"), "Kondisi Klinis Saat Pulang", nilai(rsNeonatus, "kondisi_klinis_pulang"));
        appendInfoRow("Perawatan Lanjutan di Rumah", nilai(rsNeonatus, "perawatan_lanjutan_dirumah"), "Transportasi Pulang", nilai(rsNeonatus, "cara_transportasi_pulang") + ", " + nilai(rsNeonatus, "transportasi_digunakan"));
        appendInfoRow("Masalah Keperawatan", ambilMasalahKeperawatanNeonatus(norawat), "Rencana Keperawatan", ambilRencanaKeperawatanNeonatus(norawat) + "\n" + nilai(rsNeonatus, "rencana"));
        htmlContent.append("</table>");

        appendNeonatusSignature(rsNeonatus, kodePetugas);
        htmlContent.append("</div>");
        appendPageBreakAfterDocument();
    }

    private void appendNeonatusSkrining(ResultSet rsNeonatus) throws SQLException {
        appendNeonatusSection("V. SKRINING GIZI");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendAssessmentScoreRow("1. Masalah Minum (ASI/PASI)", nilai(rsNeonatus, "masalah_gizi1"), nilai(rsNeonatus, "nilai_gizi1"));
        appendAssessmentScoreRow("2. Penurunan Berat Badan > 10% Dari BBL", nilai(rsNeonatus, "masalah_gizi2"), nilai(rsNeonatus, "nilai_gizi2"));
        appendAssessmentScoreRow("3. Penyakit / Kelainan Yang Menyertai", nilai(rsNeonatus, "masalah_gizi3"), nilai(rsNeonatus, "nilai_gizi3"));
        appendAssessmentScoreRow("Keterangan : " + nilai(rsNeonatus, "keterangan_gizi"), "TOTAL", nilai(rsNeonatus, "totalgizi"));
        htmlContent.append("</table>");

        appendNeonatusSection("VI. PENILAIAN RISIKO JATUH (SKALA HUMPTY DUMPTY)");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendAssessmentScoreRow("1. Umur", nilai(rsNeonatus, "penilaian_humptydumpty_skala1"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai1"));
        appendAssessmentScoreRow("2. Jenis Kelamin", nilai(rsNeonatus, "penilaian_humptydumpty_skala2"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai2"));
        appendAssessmentScoreRow("3. Diagnosa", nilai(rsNeonatus, "penilaian_humptydumpty_skala3"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai3"));
        appendAssessmentScoreRow("4. Gangguan Kognitif", nilai(rsNeonatus, "penilaian_humptydumpty_skala4"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai4"));
        appendAssessmentScoreRow("5. Faktor Lingkungan", nilai(rsNeonatus, "penilaian_humptydumpty_skala5"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai5"));
        appendAssessmentScoreRow("6. Efek Obat", nilai(rsNeonatus, "penilaian_humptydumpty_skala6"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai6"));
        appendAssessmentScoreRow("7. Penggunaan Obat", nilai(rsNeonatus, "penilaian_humptydumpty_skala7"), nilai(rsNeonatus, "penilaian_humptydumpty_nilai7"));
        appendAssessmentScoreRow("Keterangan : " + nilai(rsNeonatus, "penilaian_humptydumpty_hasil"), "TOTAL", nilai(rsNeonatus, "penilaian_humptydumpty_totalnilai"));
        htmlContent.append("</table>");

        appendNeonatusSection("VII. PENILAIAN TINGKAT NYERI (SKALA NIPS)");
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");
        appendAssessmentScoreRow("1. Ekspresi Wajah", nilai(rsNeonatus, "skala_nips1"), nilai(rsNeonatus, "skala_nips1_nilai"));
        appendAssessmentScoreRow("2. Tangisan", nilai(rsNeonatus, "skala_nips2"), nilai(rsNeonatus, "skala_nips2_nilai"));
        appendAssessmentScoreRow("3. Pola Nafas", nilai(rsNeonatus, "skala_nips3"), nilai(rsNeonatus, "skala_nips3_nilai"));
        appendAssessmentScoreRow("4. Tungkai", nilai(rsNeonatus, "skala_nips4"), nilai(rsNeonatus, "skala_nips4_nilai"));
        appendAssessmentScoreRow("5. Tingkat Kesadaran", nilai(rsNeonatus, "skala_nips5"), nilai(rsNeonatus, "skala_nips5_nilai"));
        appendAssessmentScoreRow("Keterangan : " + nilai(rsNeonatus, "skala_nips_keterangan"), "TOTAL", nilai(rsNeonatus, "skala_nips_total"));
        htmlContent.append("</table>");
    }

    private void appendAssessmentScoreRow(String parameter, String hasil, String skor) {
        htmlContent.append("<tr>")
                .append("<td width='65%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(formatMultiline(parameter)).append("</td>")
                .append("<td width='25%' style='padding: 5px 8px; border: 1px solid #e0e0e0; text-align: center;'>").append(formatMultiline(hasil)).append("</td>")
                .append("<td width='10%' style='padding: 5px 8px; border: 1px solid #e0e0e0; text-align: center;'>").append(formatMultiline(skor)).append("</td>")
                .append("</tr>");
    }

    private void appendNeonatusSignature(ResultSet rsNeonatus, String kodePetugas) throws SQLException {
        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9; margin-top: 8px;'>")
                .append("<tr><td colspan='2' align='center' style='background: #ececec; border: 1px solid #d9d9d9; font-weight: bold;'>YANG MELAKUKAN PENGKAJIAN</td></tr>")
                .append("<tr>")
                .append("<td width='50%' align='center' style='border: 1px solid #d9d9d9; padding: 8px;'>Tanggal dan Jam<br><br>")
                .append(escapeHtml(nilai(rsNeonatus, "tanggal_format"))).append("</td>")
                .append("<td width='50%' align='center' style='border: 1px solid #d9d9d9; padding: 8px;'>Tanda Tangan dan Nama Perawat<br>");
        if (!kodePetugas.equals("")) {
            htmlContent.append("<img width='90' height='90' src='http://")
                    .append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                    .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/")
                    .append(escapeHtml(kodePetugas)).append(".png' onerror=\"this.style.display='none';\"/><br>");
        }
        htmlContent.append(escapeHtml(nilai(rsNeonatus, "pengkaji1")))
                .append("</td></tr></table>");
    }

    private void appendNeonatusSection(String title) {
        appendSectionTitle(title);
    }

    private String gabungNeonatus(ResultSet rsData, String field, String keteranganField) throws SQLException {
        String value = nilai(rsData, field);
        String keterangan = nilai(rsData, keteranganField);
        if (keterangan.trim().equals("")) {
            return value;
        }
        if (value.trim().equals("")) {
            return keterangan;
        }
        return value + ", " + keterangan;
    }

    private String ambilMasalahKeperawatanNeonatus(String norawat) {
        StringBuilder hasil = new StringBuilder();
        String sql = "select master_masalah_keperawatan_neonatus.nama_masalah "
                + "from master_masalah_keperawatan_neonatus inner join penilaian_awal_keperawatan_ranap_neonatus_masalah "
                + "on penilaian_awal_keperawatan_ranap_neonatus_masalah.kode_masalah=master_masalah_keperawatan_neonatus.kode_masalah "
                + "where penilaian_awal_keperawatan_ranap_neonatus_masalah.no_rawat=? order by penilaian_awal_keperawatan_ranap_neonatus_masalah.kode_masalah";
        try ( PreparedStatement psMasalah = koneksi.prepareStatement(sql)) {
            psMasalah.setString(1, norawat);
            try ( ResultSet rsMasalah = psMasalah.executeQuery()) {
                while (rsMasalah.next()) {
                    if (hasil.length() > 0) {
                        hasil.append("\n");
                    }
                    hasil.append(rsMasalah.getString("nama_masalah"));
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Masalah Keperawatan Neonatus : " + e);
        }
        return hasil.toString();
    }

    private String ambilRencanaKeperawatanNeonatus(String norawat) {
        StringBuilder hasil = new StringBuilder();
        String sql = "select master_rencana_keperawatan_neonatus.rencana_keperawatan "
                + "from master_rencana_keperawatan_neonatus inner join penilaian_awal_keperawatan_ranap_neonatus_rencana "
                + "on penilaian_awal_keperawatan_ranap_neonatus_rencana.kode_rencana=master_rencana_keperawatan_neonatus.kode_rencana "
                + "where penilaian_awal_keperawatan_ranap_neonatus_rencana.no_rawat=? order by penilaian_awal_keperawatan_ranap_neonatus_rencana.kode_rencana";
        try ( PreparedStatement psRencana = koneksi.prepareStatement(sql)) {
            psRencana.setString(1, norawat);
            try ( ResultSet rsRencana = psRencana.executeQuery()) {
                while (rsRencana.next()) {
                    if (hasil.length() > 0) {
                        hasil.append("\n");
                    }
                    hasil.append(rsRencana.getString("rencana_keperawatan"));
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Rencana Keperawatan Neonatus : " + e);
        }
        return hasil.toString();
    }

    private String nilai(ResultSet rsData, String field) throws SQLException {
        String value = rsData.getString(field);
        return value == null ? "" : value;
    }

    private void appendSectionTitle(String title) {
        htmlContent.append("<div style='background: #f5f5f5; color: #111; padding: 6px 8px; margin: 0; border-left: 1px solid #d9d9d9; border-right: 1px solid #d9d9d9; border-top: 1px solid #d9d9d9; font-weight: bold;'>")
                .append(escapeHtml(title)).append("</div>");
    }

    private void appendInfoRow(String label1, String value1, String label2, String value2) {
        htmlContent.append("<tr>")
                .append("<td width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5;'>").append(label1).append("</td>")
                .append("<td width='30%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(formatMultiline(value1)).append("</td>")
                .append("<td width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5;'>").append(label2).append("</td>")
                .append("<td width='30%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(formatMultiline(value2)).append("</td>")
                .append("</tr>");
    }

    private void appendSingleRow(String label, String value) {
        htmlContent.append("<tr>");
        if (label.equals("")) {
            htmlContent.append("<td colspan='4' style='padding: 6px 8px; border: 1px solid #e0e0e0;'>")
                    .append(formatMultiline(value)).append("</td>");
        } else {
            htmlContent.append("<td width='24%' style='padding: 6px 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5; font-weight: bold;'>")
                    .append(label).append("</td>")
                    .append("<td colspan='3' width='76%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'>: ")
                    .append(formatMultiline(value)).append("</td>");
        }
        htmlContent.append("</tr>");
    }

    private void appendThreeColumnRow(String label1, String value1, String label2, String value2, String label3, String value3) {
        htmlContent.append("<tr>")
                .append("<td width='33%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>").append(label1).append(" : </strong>").append(formatMultiline(value1)).append("</td>")
                .append("<td width='33%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>").append(label2).append(" : </strong>").append(formatMultiline(value2)).append("</td>")
                .append("<td width='34%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>").append(label3).append(" : </strong>").append(formatMultiline(value3)).append("</td>")
                .append("</tr>");
    }

    private String formatMultiline(String value) {
        return escapeHtml(value).replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", "<br>");
    }

    private void appendPageBreakAfterDocument() {
        htmlContent.append("<div class='berkas-page-end' style='display: block; clear: both; height: 0; margin: 0; padding: 0; font-size: 1px; line-height: 1px; overflow: hidden;'></div>");
    }

    private String gabungAnamnesis(String anamnesis, String hubungan) {
        String hasil = anamnesis == null ? "" : anamnesis.trim();
        if (hubungan != null && !hubungan.trim().equals("")) {
            hasil = hasil + ", " + hubungan.trim();
        }
        return hasil;
    }

    private String escapeHtml(String value) {
        if (value == null || value.trim().equals("")) {
            return "-";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    private String makeSuratKontrolFinger(ResultSet rsKontrol) throws SQLException {
        String namaDpjp = rsKontrol.getString("nm_dokter_bpjs") == null ? "" : rsKontrol.getString("nm_dokter_bpjs").trim();
        String kodeDpjp = rsKontrol.getString("kd_dokter_bpjs") == null ? "" : rsKontrol.getString("kd_dokter_bpjs").trim();
        String tanggalKontrol = rsKontrol.getString("tgl_rencana") == null ? "" : rsKontrol.getString("tgl_rencana").trim();

        if (namaDpjp.equals("") && kodeDpjp.equals("")) {
            return "";
        }

        return "Dikeluarkan di " + akses.getnamars() + ", Kabupaten/Kota " + akses.getkabupatenrs()
                + "\nDitandatangani secara elektronik oleh " + namaDpjp
                + "\nID " + kodeDpjp
                + "\n" + tanggalKontrol;
    }

    private String safeQrBarcode(String value) {
        if (value == null || value.trim().equals("")) {
            return "";
        }

        return value.trim()
                .replace("\r\n", "_")
                .replace("\n\r", "_")
                .replace("\r", "_")
                .replace("\n", "_")
                .replace(" ", "_")
                .replace("/", "garing")
                .replace("#", "cross");
    }

    private String urlEncode(String value) {
        if (value == null) {
            return "";
        }

        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    private void menampilkanSPRRawatInap(String norawat) {
        if (!chkSPR.isSelected()) {
            return;
        }

        String sql = "select permintaan_ranap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"
                + "if(pasien.jk='L','LAKI-LAKI','PEREMPUAN') as jk,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                + "pasien.no_tlp,penjab.png_jawab,poliklinik.nm_poli,dokter.nm_dokter,"
                + "date_format(permintaan_ranap.tanggal,'%d/%m/%Y') as tanggal_display,"
                + "permintaan_ranap.tanggal,permintaan_ranap.kd_kamar,kamar.kd_bangsal,"
                + "bangsal.nm_bangsal,kamar.trf_kamar,permintaan_ranap.diagnosa,"
                + "permintaan_ranap.catatan,reg_periksa.kd_dokter "
                + "from permintaan_ranap inner join reg_periksa on permintaan_ranap.no_rawat=reg_periksa.no_rawat "
                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "
                + "inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "
                + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "
                + "inner join kamar on permintaan_ranap.kd_kamar=kamar.kd_kamar "
                + "inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "
                + "where reg_periksa.no_rawat=?";

        try ( PreparedStatement psSPR = koneksi.prepareStatement(sql)) {
            psSPR.setString(1, norawat);
            try ( ResultSet rsSPR = psSPR.executeQuery()) {
                if (rsSPR.next()) {
                    appendSPRRawatInapRecord(rsSPR);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SPR Rawat Inap : " + e);
        }
    }

    private void appendSPRRawatInapRecord(ResultSet rsSPR) throws SQLException {
        String kdDokter = rsSPR.getString("kd_dokter") == null ? "" : rsSPR.getString("kd_dokter").trim().replace(" ", "_");

        try {
            if (!kdDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kdDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR SPR Rawat Inap: " + e.getMessage());
        }

        htmlContent.append(
                "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 18px 24px 28px 24px; margin: 6px auto; background: #ffffff; width: 94%; box-sizing: border-box;'>"
                + "<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; font-size: 14px;'>"
                + "<tr>"
                + "<td width='14%' align='center' valign='top'><img width='72' height='72' src='http://")
                .append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                .append(koneksiDB.HYBRIDWEB()).append("/images/logo.png'/></td>")
                .append("<td width='86%' align='center' valign='top'>"
                        + "<div style='font-size: 18px !important; font-weight: bold;'>")
                .append(escapeHtml(akses.getnamars())).append("</div>"
                + "<div style='font-size: 13px !important;'>")
                .append(escapeHtml(akses.getalamatrs())).append(", ")
                .append(escapeHtml(akses.getkabupatenrs())).append(", ")
                .append(escapeHtml(akses.getpropinsirs())).append("</div>"
                + "<div style='font-size: 13px !important;'>")
                .append(escapeHtml(akses.getkontakrs())).append("</div>"
                + "<div style='font-size: 13px !important;'>E-mail : ")
                .append(escapeHtml(akses.getemailrs())).append("</div>"
                + "</td>"
                + "</tr>"
                + "<tr><td colspan='2' style='border-top: 3px double #111; height: 10px;'></td></tr>"
                + "<tr><td colspan='2' align='center' style='font-size: 18px !important; padding: 4px 0 22px 0;'>FORMULIR PEMINTAAN RAWAT INAP</td></tr>"
                + "<tr><td colspan='2' style='font-size: 15px !important; padding-bottom: 10px;'>Yang bertanda tangan di bawah ini :</td></tr>"
                + "</table>"
                + "<table width='78%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 14px; margin-left: 34px;'>");

        appendSPRPlainRow("Dokter Yang Memeriksa", rsSPR.getString("nm_dokter"));

        htmlContent.append("</table>"
                + "<div style='font-size: 15px !important; padding: 12px 0 8px 0;'>Menyatakan bahwa :</div>"
                + "<table width='78%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 14px; margin-left: 34px;'>");

        appendSPRPlainRow("Nama Pasien", rsSPR.getString("nm_pasien"));
        appendSPRPlainRow("No.Rekam Medis", rsSPR.getString("no_rkm_medis"));
        appendSPRPlainRow("Jenis Kelamin", rsSPR.getString("jk"));
        appendSPRPlainRow("Umur", rsSPR.getString("umurdaftar") + " " + rsSPR.getString("sttsumur"));
        appendSPRPlainRow("No.Telephone/HP", rsSPR.getString("no_tlp"));
        appendSPRPlainRow("Asal Poli/Unit", rsSPR.getString("nm_poli"));
        appendSPRPlainRow("Cara Bayar/Asuransi", rsSPR.getString("png_jawab"));
        appendSPRPlainRow("Diagnosa Awal", rsSPR.getString("diagnosa"));

        htmlContent.append("</table>"
                + "<div style='font-size: 15px !important; padding-top: 14px;'>Memerlukan perawatan inap di ")
                .append(escapeHtml(akses.getnamars())).append("</div>"
                + "<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 14px; margin-top: 28px;'>"
                + "<tr>"
                + "<td width='55%'>&nbsp;</td>"
                + "<td width='45%' align='center' style='font-size: 14px !important;'>")
                .append(escapeHtml(akses.getkabupatenrs())).append(", ")
                .append(escapeHtml(rsSPR.getString("tanggal_display"))).append("</td>"
                + "</tr>"
                + "<tr><td></td><td align='center' style='font-size: 14px !important;'>Dokter Penanggung Jawab</td></tr>"
                + "<tr><td></td><td align='center' style='padding-top: 12px;'>"
                + "<img width='125' height='125' src='http://")
                .append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/")
                .append(escapeHtml(kdDokter)).append(".png' onerror=\"this.style.display='none';\"/>"
                + "</td></tr>"
                + "<tr><td></td><td align='center' style='font-size: 14px !important; padding-top: 8px;'>")
                .append(escapeHtml(rsSPR.getString("nm_dokter"))).append("</td></tr>"
                // + "<tr><td></td><td align='center'><div style='border-top: 1px solid #111; width: 280px; margin: 2px auto 0 auto;'>&nbsp;</div></td></tr>"
                // + "<tr><td></td><td align='center' style='font-size: 14px !important;'>Nama &amp; tanda tangan</td></tr>"
                + "</table>"
                + "</div>");
        appendPageBreakAfterDocument();
    }

    private void appendSPRPlainRow(String label, String value) {
        htmlContent.append("<tr>")
                .append("<td width='34%' style='font-size: 14px !important;'>").append(escapeHtml(label)).append("</td>")
                .append("<td width='4%' style='font-size: 14px !important;'>:</td>")
                .append("<td width='62%' style='font-size: 14px !important;'>").append(formatMultiline(value)).append("</td>")
                .append("</tr>");
    }

    private void menampilkanSuratKelahiranBayi(String norawat) {
        if (!chkSuratKelahiranBayi.isSelected()) {
            return;
        }

        String sql = "select pasien.no_rkm_medis,pasien.nm_pasien,"
                + "if(pasien.jk='L','Laki-laki','Perempuan') as jk,pasien.tgl_lahir,"
                + "pasien_bayi.jam_lahir,pasien.nm_ibu,pasien_bayi.nama_ayah,"
                + "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,"
                + "pasien_bayi.berat_badan,pasien_bayi.panjang_badan,pegawai.nama as nm_penolong,"
                + "pasien_bayi.penolong as kd_penolong,pasien_bayi.no_skl "
                + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "inner join pasien_bayi on pasien.no_rkm_medis=pasien_bayi.no_rkm_medis "
                + "inner join pegawai on pasien_bayi.penolong=pegawai.nik "
                + "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "
                + "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "
                + "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "
                + "where reg_periksa.no_rawat=? limit 1";

        try ( PreparedStatement psBayi = koneksi.prepareStatement(sql)) {
            psBayi.setString(1, norawat);
            try ( ResultSet rsBayi = psBayi.executeQuery()) {
                if (rsBayi.next()) {
                    appendSuratKelahiranBayiRecord(rsBayi);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Surat Kelahiran Bayi : " + e);
        }
    }

    private void appendSuratKelahiranBayiRecord(ResultSet rsBayi) throws SQLException {
        String tglLahir = formatTanggalIndonesia(rsBayi.getString("tgl_lahir"));
        String jamLahir = rsBayi.getString("jam_lahir") == null ? "" : rsBayi.getString("jam_lahir");
        String kodeDokter = rsBayi.getString("kd_penolong") == null ? "" : rsBayi.getString("kd_penolong").replace(" ", "_");

        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Surat Kelahiran Bayi: " + e.getMessage());
        }

        htmlContent.append("<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 18px 46px 34px 46px; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box; min-height: 960px;'>");
        htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; font-size: 13px;'>")
                .append("<tr>")
                .append("<td width='18%' align='center' valign='top'><img width='58' height='58' src='http://")
                .append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                .append(koneksiDB.HYBRIDWEB()).append("/images/logo.png'/></td>")
                .append("<td width='82%' align='center' valign='top'>")
                .append("<div style='font-size: 17px !important; font-weight: bold;'>").append(escapeHtml(akses.getnamars())).append("</div>")
                .append("<div style='font-size: 12px !important;'>").append(escapeHtml(akses.getalamatrs())).append(", ")
                .append(escapeHtml(akses.getkabupatenrs())).append(", ").append(escapeHtml(akses.getpropinsirs())).append("</div>")
                .append("<div style='font-size: 12px !important;'>").append(escapeHtml(akses.getkontakrs())).append("</div>")
                .append("<div style='font-size: 12px !important;'>E-mail : ").append(escapeHtml(akses.getemailrs())).append("</div>")
                .append("</td>")
                .append("</tr>")
                .append("<tr><td colspan='2' style='border-top: 3px double #111; height: 8px;'></td></tr>")
                .append("</table>");

        htmlContent.append("<div align='center' style='font-size: 17px !important; text-decoration: underline; font-weight: bold;'>SURAT KETERANGAN KELAHIRAN</div>")
                .append("<div align='center' style='font-size: 13px !important; margin-bottom: 44px;'>Nomor : ")
                .append(escapeHtml(rsBayi.getString("no_skl"))).append("</div>");

        htmlContent.append("<div style='font-size: 14px !important; margin-bottom: 12px;'>Yang bertanda tangan di bawah ini menerangkan bahwa :</div>")
                .append("<table width='86%' border='0' cellpadding='4px' cellspacing='0' style='border-collapse: collapse; font-size: 14px; margin-bottom: 28px;'>");
        appendSuratKelahiranRow("Nama Ibu", rsBayi.getString("nm_ibu"));
        appendSuratKelahiranRow("Nama Ayah", rsBayi.getString("nama_ayah"));
        appendSuratKelahiranRow("Alamat", rsBayi.getString("alamat"));
        htmlContent.append("</table>");

        htmlContent.append("<div style='font-size: 14px !important; line-height: 1.9;'>Telah melahirkan seorang anak : ")
                .append(escapeHtml(rsBayi.getString("jk")))
                .append(" Pada Tanggal : ").append(escapeHtml(tglLahir))
                .append(" Pukul ").append(escapeHtml(jamLahir)).append("</div>")
                .append("<div style='font-size: 14px !important; line-height: 1.9;'>dengan Berat ")
                .append(escapeHtml(rsBayi.getString("berat_badan")))
                .append(" Gram, Panjang Badan ")
                .append(escapeHtml(rsBayi.getString("panjang_badan")))
                .append(" cm</div>")
                .append("<div style='font-size: 14px !important; line-height: 1.9;'>Yang diberi nama : ")
                .append(escapeHtml(rsBayi.getString("nm_pasien"))).append("</div>");

        String qrDokterHtml = kodeDokter.equals("") ? "" : "<img width='115' height='115' src='http://"
                + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/"
                + koneksiDB.HYBRIDWEB() + "/penggajian/temp/"
                + escapeHtml(kodeDokter) + ".png' onerror=\"this.style.display='none';\"/>";

        htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 14px; margin-top: 76px;'>")
                .append("<tr><td width='55%'>&nbsp;</td><td width='45%' align='center'>")
                .append(escapeHtml(akses.getkabupatenrs())).append(", ").append(escapeHtml(tglLahir)).append("</td></tr>")
                .append("<tr><td></td><td align='center'>Dokter</td></tr>")
                .append("<tr><td></td><td align='center' style='padding-top: 10px;'>")
                .append(qrDokterHtml)
                .append("</td></tr>")
                .append("<tr><td></td><td align='center' style='font-size: 14px !important; padding-top: 8px;'>")
                .append(escapeHtml(rsBayi.getString("nm_penolong"))).append("</td></tr>")
                .append("</table>")
                .append("</div>");
        appendPageBreakAfterDocument();
    }

    private void appendSuratKelahiranRow(String label, String value) {
        htmlContent.append("<tr>")
                .append("<td width='22%' valign='top' style='font-size: 14px !important;'>").append(escapeHtml(label)).append("</td>")
                .append("<td width='4%' valign='top' style='font-size: 14px !important;'>:</td>")
                .append("<td width='74%' valign='top' style='font-size: 14px !important;'>").append(formatMultiline(value)).append("</td>")
                .append("</tr>");
    }

    private String formatTanggalIndonesia(String tanggal) {
        if (tanggal == null || tanggal.trim().equals("")) {
            return "";
        }

        try {
            return Valid.SetTgl3(tanggal.length() >= 10 ? tanggal.substring(0, 10) : tanggal);
        } catch (Exception e) {
            return tanggal;
        }
    }

    private void menampilkanTriaseIGD(String norawat) {
        try {
            if (chkTriase.isSelected() == true) {
                // ==================== TRIASE PRIMER ====================
                try {
                    rs2 = koneksi.prepareStatement(
                            "SELECT data_triase_igdprimer.keluhan_utama, data_triase_igdprimer.kebutuhan_khusus, "
                            + "data_triase_igdprimer.catatan, data_triase_igdprimer.plan, DATE_FORMAT(data_triase_igdprimer.tanggaltriase,'%d-%m-%Y %H:%i:%s') as tanggaltriase, "
                            + "data_triase_igdprimer.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, "
                            + "data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, "
                            + "data_triase_igd.nyeri, data_triase_igd.cara_masuk, data_triase_igd.alat_transportasi, "
                            + "data_triase_igd.alasan_kedatangan, data_triase_igd.keterangan_kedatangan, "
                            + "data_triase_igd.kode_kasus, master_triase_macam_kasus.macam_kasus, pegawai.nama "
                            + "FROM data_triase_igdprimer "
                            + "INNER JOIN data_triase_igd ON data_triase_igd.no_rawat = data_triase_igdprimer.no_rawat "
                            + "INNER JOIN master_triase_macam_kasus ON data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus "
                            + "INNER JOIN pegawai ON data_triase_igdprimer.nik = pegawai.nik "
                            + "WHERE data_triase_igd.no_rawat = '" + norawat + "'"
                    ).executeQuery();

                    if (rs2.next()) {
                        htmlContent.append(
                                "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff;'>"
                        );

                        Copsurat();

                        htmlContent.append(
                                "<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>TRIASE IGD PRIMER</div>"
                        );

                        htmlContent.append(buildPatientInfoSection());
                        htmlContent.append(buildTriasePrimerContent(rs2, norawat));
                        htmlContent.append(buildTriaseFooter(rs2, "PRIMER", "Zona Merah"));

                        htmlContent.append("</div>");
                    }

                } catch (Exception e) {
                    System.out.println("Error Data Triase IGD Primer: " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                }

                // ==================== TRIASE SEKUNDER ====================
                try {
                    rs2 = koneksi.prepareStatement(
                            "SELECT data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, "
                            + "data_triase_igdsekunder.plan, DATE_FORMAT(data_triase_igdsekunder.tanggaltriase,'%d-%m-%Y %H:%i:%s') as tanggaltriase, "
                            + "data_triase_igdsekunder.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, "
                            + "data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, "
                            + "data_triase_igd.nyeri, data_triase_igd.cara_masuk, data_triase_igd.alat_transportasi, "
                            + "data_triase_igd.alasan_kedatangan, data_triase_igd.keterangan_kedatangan, "
                            + "data_triase_igd.kode_kasus, master_triase_macam_kasus.macam_kasus, pegawai.nama "
                            + "FROM data_triase_igdsekunder "
                            + "INNER JOIN data_triase_igd ON data_triase_igd.no_rawat = data_triase_igdsekunder.no_rawat "
                            + "INNER JOIN master_triase_macam_kasus ON data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus "
                            + "INNER JOIN pegawai ON data_triase_igdsekunder.nik = pegawai.nik "
                            + "WHERE data_triase_igd.no_rawat = '" + norawat + "'"
                    ).executeQuery();

                    if (rs2.next()) {
                        htmlContent.append(
                                "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff;'>"
                        );

                        Copsurat();

                        htmlContent.append(
                                "<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>TRIASE IGD SEKUNDER</div>"
                        );

                        htmlContent.append(buildPatientInfoSection());
                        htmlContent.append(buildTriaseSekunderContent(rs2, norawat));
                        htmlContent.append(buildTriaseFooter(rs2, "SEKUNDER", ""));

                        htmlContent.append("</div>");
                    }

                } catch (Exception e) {
                    System.out.println("Error Data Triase IGD Sekunder: " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                }
            }

            // Handle chkTriase1 dengan struktur yang sama...
            // handleChkTriase1(norawat);
        } catch (Exception e) {
            System.out.println("Error Triase IGD: " + e);
        }
    }

// ==================== HELPER METHODS ====================
    private String buildPatientInfoSection() throws SQLException {
        StringBuilder patientInfo = new StringBuilder();

        patientInfo.append(
                "<div style='padding: 6px 8px 0 8px;'>"
                + "<table style='width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
        );

        String[][] patientData = {
            {"No. Rekam Medis", rs.getString("no_rkm_medis")},
            {"Nama Pasien", rs.getString("nm_pasien")},
            {"Tanggal Lahir", rs.getString("tgl_lahir")},
            {"No. Rawat", rs.getString("no_rawat")},
            {"No. Registrasi", rs.getString("no_reg")},
            {"Tanggal Registrasi", rs.getString("tgl_registrasi") + " " + rs.getString("jam_reg")},
            {"Unit/Poliklinik", rs.getString("nm_poli") + polirujukan},
            {"Dokter Poli", rs.getString("nm_dokter") + dokterrujukan}
        };

        for (int i = 0; i < patientData.length; i++) {
            String bgColor = (i % 2 == 0) ? "#ffffff" : "#f5f5f5";
            patientInfo.append(String.format(
                    "<tr style='background-color: %s;'>"
                    + "<td style='padding: 5px 8px; font-weight: 600; width: 31%%; border-bottom: 1px solid #e0e0e0;'>%s</td>"
                    + "<td style='padding: 5px 4px; width: 4%%; text-align: center; border-bottom: 1px solid #e0e0e0;'>:</td>"
                    + "<td style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>%s</td>"
                    + "</tr>",
                    bgColor, patientData[i][0], patientData[i][1]
            ));
        }

        patientInfo.append("</table></div>");
        return patientInfo.toString();
    }

    private String buildTriasePrimerContent(ResultSet rs2, String norawat) throws SQLException {
        StringBuilder content = new StringBuilder();

        content.append(
                "<div style='padding: 6px 8px 0 8px;'>"
                + "<table style='width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
        );

        String[][] kedatanganInfo = {
            {"Cara Masuk", rs2.getString("cara_masuk")},
            {"Transportasi", rs2.getString("alat_transportasi")},
            {"Alasan Kedatangan", rs2.getString("alasan_kedatangan")},
            {"Keterangan Kedatangan", rs2.getString("keterangan_kedatangan")},
            {"Macam Kasus", rs2.getString("macam_kasus")}
        };

        for (int i = 0; i < kedatanganInfo.length; i++) {
            String bgColor = (i % 2 == 0) ? "#ffffff" : "#f5f5f5";
            content.append(String.format(
                    "<tr style='background-color: %s;'>"
                    + "<td style='padding: 5px 8px; font-weight: 600; width: 30%%; border-bottom: 1px solid #e0e0e0;'>%s</td>"
                    + "<td style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>%s</td>"
                    + "</tr>",
                    bgColor, kedatanganInfo[i][0], kedatanganInfo[i][1]
            ));
        }
        content.append("</table></div>");

        content.append(
                "<div style='padding: 6px 8px 0 8px;'>"
                + "<table style='width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
        );

        content.append(
                "<tr style='background-color: #ffffff;'>"
                + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>Keluhan Utama</td>"
                + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>"
                + rs2.getString("keluhan_utama").replaceAll("(\r\n|\r|\n|\n\r)", "<br>")
                + "</td></tr>"
        );

        content.append(
                "<tr style='background-color: #f5f5f5;'>"
                + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>Tanda Vital</td>"
                + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>"
                + buildVitalSignsDisplay(rs2)
                + "</td></tr>"
        );

        content.append(
                "<tr style='background-color: #ffffff;'>"
                + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>Kebutuhan Khusus</td>"
                + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>"
                + rs2.getString("kebutuhan_khusus")
                + "</td></tr>"
        );

        content.append("</table></div>");
        content.append(buildSkalaAssessments(norawat, new int[]{1, 2}));

        return content.toString();
    }

    private String buildTriaseSekunderContent(ResultSet rs2, String norawat) throws SQLException {
        StringBuilder content = new StringBuilder();

        content.append(
                "<div style='padding: 6px 8px 0 8px;'>"
                + "<table style='width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
        );

        String[][] kedatanganInfo = {
            {"Cara Masuk", rs2.getString("cara_masuk")},
            {"Transportasi", rs2.getString("alat_transportasi")},
            {"Alasan Kedatangan", rs2.getString("alasan_kedatangan")},
            {"Keterangan Kedatangan", rs2.getString("keterangan_kedatangan")},
            {"Macam Kasus", rs2.getString("macam_kasus")}
        };

        for (int i = 0; i < kedatanganInfo.length; i++) {
            String bgColor = (i % 2 == 0) ? "#ffffff" : "#f5f5f5";
            content.append(String.format(
                    "<tr style='background-color: %s;'>"
                    + "<td style='padding: 5px 8px; font-weight: 600; width: 30%%; border-bottom: 1px solid #e0e0e0;'>%s</td>"
                    + "<td style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>%s</td>"
                    + "</tr>",
                    bgColor, kedatanganInfo[i][0], kedatanganInfo[i][1]
            ));
        }
        content.append("</table></div>");

        content.append(
                "<div style='padding: 6px 8px 0 8px;'>"
                + "<table style='width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
        );

        content.append(
                "<tr style='background-color: #ffffff;'>"
                + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>Anamnesa Singkat</td>"
                + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>"
                + rs2.getString("anamnesa_singkat").replaceAll("(\r\n|\r|\n|\n\r)", "<br>")
                + "</td></tr>"
        );

        content.append(
                "<tr style='background-color: #f5f5f5;'>"
                + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>Tanda Vital</td>"
                + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>"
                + buildVitalSignsDisplay(rs2)
                + "</td></tr>"
        );

        content.append("</table></div>");
        content.append(buildSkalaAssessments(norawat, new int[]{3, 4, 5}));

        return content.toString();
    }

    private String buildVitalSignsDisplay(ResultSet rs2) throws SQLException {
        return String.format(
                "<div style='display: flex; flex-wrap: wrap; gap: 6px; align-items: center; font-size: 11px;'>"
                + "<span style='display: inline-block; margin: 2px; padding: 4px 7px; background: #f5f5f5; border-radius: 3px; color: #000; white-space: nowrap;'>Suhu: <b>%s°C</b></span>"
                + "<span style='display: inline-block; margin: 2px; padding: 4px 7px; background: #f5f5f5; border-radius: 3px; color: #000; white-space: nowrap;'>Nyeri: <b>%s</b></span>"
                + "<span style='display: inline-block; margin: 2px; padding: 4px 7px; background: #f5f5f5; border-radius: 3px; color: #000; white-space: nowrap;'>Tensi: <b>%s</b></span>"
                + "<span style='display: inline-block; margin: 2px; padding: 4px 7px; background: #f5f5f5; border-radius: 3px; color: #000; white-space: nowrap;'>Nadi: <b>%s/menit</b></span>"
                + "<span style='display: inline-block; margin: 2px; padding: 4px 7px; background: #f5f5f5; border-radius: 3px; color: #000; white-space: nowrap;'>SpO₂: <b>%s%%</b></span>"
                + "<span style='display: inline-block; margin: 2px; padding: 4px 7px; background: #f5f5f5; border-radius: 3px; color: #000; white-space: nowrap;'>RR: <b>%s/menit</b></span>"
                + "</div>",
                rs2.getString("suhu"), rs2.getString("nyeri"), rs2.getString("tekanan_darah"),
                rs2.getString("nadi"), rs2.getString("saturasi_o2"), rs2.getString("pernapasan")
        );
    }

    private String buildSkalaAssessments(String norawat, int[] skalaNumbers) {
        StringBuilder assessments = new StringBuilder();

        for (int skalaNum : skalaNumbers) {
            assessments.append(buildSingleSkalaAssessment(norawat, skalaNum));
        }

        return assessments.toString();
    }

    private String buildSingleSkalaAssessment(String norawat, int skalaNumber) {
        StringBuilder content = new StringBuilder();
        String skalaTable = "master_triase_skala" + skalaNumber;
        String detailTable = "data_triase_igddetail_skala" + skalaNumber;
        String skalaField = "pengkajian_skala" + skalaNumber;
        String skalaKey = "kode_skala" + skalaNumber;

        String[] colors = {"", "#AA0000", "#FF0000", "#C8C800", "#00AA00", "#969696"};
        String[] labels = {"", "Immediate/Segera", "Emergensi", "Urgensi", "Semi Urgensi/Urgensi Rendah", "Non Urgensi"};

        try {
            rs3 = koneksi.prepareStatement(
                    "SELECT mp.kode_pemeriksaan, mp.nama_pemeriksaan "
                    + "FROM master_triase_pemeriksaan mp "
                    + "INNER JOIN " + skalaTable + " ms ON mp.kode_pemeriksaan = ms.kode_pemeriksaan "
                    + "INNER JOIN " + detailTable + " dt ON ms." + skalaKey + " = dt." + skalaKey + " "
                    + "WHERE dt.no_rawat = '" + norawat + "' "
                    + "GROUP BY mp.kode_pemeriksaan ORDER BY mp.kode_pemeriksaan"
            ).executeQuery();

            if (rs3.next()) {
                content.append(
                        "<div style='padding: 6px 8px 0 8px; margin-top: 4px;'>"
                        + "<div style='border: 1px solid " + colors[skalaNumber] + "; background: #f8f8f8; padding: 0; margin: 0;'>"
                        + "<div style='background: " + colors[skalaNumber] + "; color: #000; padding: 6px 8px; font-size: 11px; font-weight: bold;'>" + labels[skalaNumber] + "</div>"
                        + "<table style='width: 100%; border-collapse: collapse; font-size: 11px;'>"
                );

                content.append(
                        "<tr style='background: #eaeaea;'>"
                        + "<th style='padding: 6px 8px; text-align: left; width: 35%; border-bottom: 1px solid #d9d9d9;'>Pemeriksaan</th>"
                        + "<th style='padding: 6px 8px; text-align: left; border-bottom: 1px solid #d9d9d9;'>Detail Pengkajian</th>"
                        + "</tr>"
                );

                rs3.beforeFirst();
                int rowIndex = 0;
                while (rs3.next()) {
                    String bgColor = (rowIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                    content.append(
                            "<tr style='background-color: " + bgColor + ";'>"
                            + "<td style='padding: 6px 8px; font-weight: 600; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>"
                            + rs3.getString("nama_pemeriksaan")
                            + "</td>"
                            + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>"
                    );

                    rs4 = koneksi.prepareStatement(
                            "SELECT ms." + skalaField + " FROM " + skalaTable + " ms "
                            + "INNER JOIN " + detailTable + " dt ON ms." + skalaKey + " = dt." + skalaKey + " "
                            + "WHERE ms.kode_pemeriksaan = '" + rs3.getString("kode_pemeriksaan") + "' "
                            + "AND dt.no_rawat = '" + norawat + "' "
                            + "ORDER BY dt." + skalaKey
                    ).executeQuery();

                    boolean first = true;
                    while (rs4.next()) {
                        if (!first) {
                            content.append("<br>");
                        }
                        content.append(
                                "<span style='display: inline-block; margin: 1px 3px 1px 0; padding: 3px 5px; background: #ffffff; color: #000; border: 1px solid " + colors[skalaNumber] + "; font-size: 10px;'>"
                                + rs4.getString(skalaField)
                                + "</span>"
                        );
                        first = false;
                    }

                    content.append("</td></tr>");
                    rowIndex++;
                }

                content.append("</table></div></div>");
                keputusan = colors[skalaNumber];
            }

        } catch (Exception e) {
            System.out.println("Error Skala " + skalaNumber + ": " + e);
        } finally {
            try {
                if (rs4 != null) {
                    rs4.close();
                }
                if (rs3 != null) {
                    rs3.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing ResultSet: " + e);
            }
        }

        return content.toString();
    }

    private String buildTriaseFooter(ResultSet rs2, String triaseType, String zonLabel) throws SQLException {
        StringBuilder footer = new StringBuilder();

        String zoneColor = "#f4f4f4";
        String zoneText = zonLabel == null ? "" : zonLabel;
        if (zonLabel != null && zonLabel.toLowerCase().contains("zona merah")) {
            zoneColor = "#ff4d4d";
            zoneText = "Zona Merah Ruang Kritis";
        }

        footer.append(
                "<div style='padding: 6px 8px 0 8px;'>"
                + "<table style='width: 100%; border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
                + "<tr>"
                + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>Plan/Keputusan</td>"
                + "<td style='padding: 6px 8px; background: " + zoneColor + " !important; color: #111; font-weight: 600; border-bottom: 1px solid #e0e0e0; -webkit-print-color-adjust: exact; print-color-adjust: exact;'>"
                + zoneText + " " + rs2.getString("plan")
                + "</td>"
                + "</tr>"
        );

        String[][] officerData = {
            {"Tanggal & Jam", rs2.getString("tanggaltriase")},
            {"Catatan", rs2.getString("catatan")},
            {"Dokter/Petugas IGD", rs2.getString("nik") + " - " + rs2.getString("nama")}
        };

        for (int i = 0; i < officerData.length; i++) {
            String bgColor = (i % 2 == 0) ? "#ffffff" : "#f5f5f5";
            footer.append(
                    "<tr style='background-color: " + bgColor + ";'>"
                    + "<td style='padding: 6px 8px; font-weight: 600; width: 30%; border-bottom: 1px solid #e0e0e0; vertical-align: top;'>" + officerData[i][0] + "</td>"
                    + "<td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>" + officerData[i][1] + "</td>"
                    + "</tr>"
            );
        }
        footer.append("</table></div>");

        String kdDokter = Sequel.cariIsi("SELECT kd_dokter FROM dokter WHERE kd_dokter='" + rs2.getString("nik") + "'");

        if (!kdDokter.equals("")) {
            try {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + rs2.getString("nik").replace(" ", "_"));
                http.executeMethod(get);
            } catch (IOException e) {
                System.out.println("Error generating QR code: " + e);
            }
        } else {
            try {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + rs2.getString("nik").replace(" ", "_"));
                http.executeMethod(get);
            } catch (IOException e) {
                System.out.println("Error generating QR code: " + e);
            }
        }

        footer.append(
                "<div style='padding: 8px 8px 10px 8px; text-align: center;'>"
                + "<div style='display: inline-block; text-align: center; padding: 6px 10px; background: #f5f5f5; border: 1px solid #d9d9d9; border-radius: 3px; font-size: 11px;'>"
                + "<div style='font-weight: 600; margin-bottom: 5px;'>Tanda Tangan/Verifikasi</div>"
                + "<img src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + rs2.getString("nik").replace(" ", "_") + ".png' "
                + "style='width: 60px; height: 60px; border: 1px solid #d9d9d9; background: white; padding: 3px;' alt='QR Code Dokter/Petugas'/>"
                + "<div style='margin-top: 5px; font-weight: 600;'>" + rs2.getString("nama") + "</div>"
                + "</div>"
                + "</div>"
        );

        return footer.toString();
    }

// ==================== ADDITIONAL HELPER METHOD FOR chkTriase1 ====================
    private void handleChkTriase1(String norawat) {
        try {
            if (chkTriase.isSelected() == true) {
                // ==================== TRIASE PRIMER untuk chkTriase1 ====================
                try {
                    rs2 = koneksi.prepareStatement(
                            "SELECT data_triase_igdprimer.keluhan_utama, data_triase_igdprimer.kebutuhan_khusus, "
                            + "data_triase_igdprimer.catatan, data_triase_igdprimer.plan, DATE_FORMAT(data_triase_igdprimer.tanggaltriase,'%d-%m-%Y %H:%i:%s') as tanggaltriase, "
                            + "data_triase_igdprimer.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, "
                            + "data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, "
                            + "data_triase_igd.nyeri, data_triase_igd.cara_masuk, data_triase_igd.alat_transportasi, "
                            + "data_triase_igd.alasan_kedatangan, data_triase_igd.keterangan_kedatangan, "
                            + "data_triase_igd.kode_kasus, master_triase_macam_kasus.macam_kasus, pegawai.nama "
                            + "FROM data_triase_igdprimer "
                            + "INNER JOIN data_triase_igd ON data_triase_igd.no_rawat = data_triase_igdprimer.no_rawat "
                            + "INNER JOIN master_triase_macam_kasus ON data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus "
                            + "INNER JOIN pegawai ON data_triase_igdprimer.nik = pegawai.nik "
                            + "WHERE data_triase_igd.no_rawat = '" + norawat + "'"
                    ).executeQuery();

                    if (rs2.next()) {
                        htmlContent.append(
                                ""
                                + "<p class='pagebreak'>"
                                + "<div style='border: 2px solid #dc3545; border-radius: 8px; padding: 20px; margin: 10px; background: #ffffff; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>"
                        );

                        Copsurat();

                        // Header Section dengan warna berbeda untuk chkTriase1
                        htmlContent.append(
                                "<div style='background: linear-gradient(135deg, #dc3545, #c82333); color: white; padding: 15px; margin: -20px -20px 20px -20px; border-radius: 6px 6px 0 0;'>"
                                + "<h2 style='margin: 0; text-align: center; font-size: 24px; letter-spacing: 1px;'>TRIASE IGD PRIMER (PRIORITAS)</h2>"
                                + "</div>"
                        );

                        // Patient Information
                        htmlContent.append(buildPatientInfoSection());

                        // Triase Content Section
                        htmlContent.append(buildTriasePrimerContent(rs2, norawat));

                        // Footer Section
                        htmlContent.append(buildTriaseFooter(rs2, "PRIMER", "Zona Merah"));

                        htmlContent.append("</div></p>");
                    }

                } catch (Exception e) {
                    System.out.println("Error Data Triase IGD Primer (chkTriase1): " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                }

                // ==================== TRIASE SEKUNDER untuk chkTriase1 ====================
                try {
                    rs2 = koneksi.prepareStatement(
                            "SELECT data_triase_igdsekunder.anamnesa_singkat, data_triase_igdsekunder.catatan, "
                            + "data_triase_igdsekunder.plan, DATE_FORMAT(data_triase_igdsekunder.tanggaltriase,'%d-%m-%Y %H:%i:%s') as tanggaltriase, "
                            + "data_triase_igdsekunder.nik, data_triase_igd.tekanan_darah, data_triase_igd.nadi, "
                            + "data_triase_igd.pernapasan, data_triase_igd.suhu, data_triase_igd.saturasi_o2, "
                            + "data_triase_igd.nyeri, data_triase_igd.cara_masuk, data_triase_igd.alat_transportasi, "
                            + "data_triase_igd.alasan_kedatangan, data_triase_igd.keterangan_kedatangan, "
                            + "data_triase_igd.kode_kasus, master_triase_macam_kasus.macam_kasus, pegawai.nama "
                            + "FROM data_triase_igdsekunder "
                            + "INNER JOIN data_triase_igd ON data_triase_igd.no_rawat = data_triase_igdsekunder.no_rawat "
                            + "INNER JOIN master_triase_macam_kasus ON data_triase_igd.kode_kasus = master_triase_macam_kasus.kode_kasus "
                            + "INNER JOIN pegawai ON data_triase_igdsekunder.nik = pegawai.nik "
                            + "WHERE data_triase_igd.no_rawat = '" + norawat + "'"
                    ).executeQuery();

                    if (rs2.next()) {
                        htmlContent.append(
                                ""
                                + "<p class='pagebreak'>"
                                + "<div style='border: 2px solid #fd7e14; border-radius: 8px; padding: 20px; margin: 10px; background: #ffffff; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>"
                        );

                        Copsurat();

                        // Header Section untuk Sekunder chkTriase1
                        htmlContent.append(
                                "<div style='background: linear-gradient(135deg, #fd7e14, #e55a00); color: white; padding: 15px; margin: -20px -20px 20px -20px; border-radius: 6px 6px 0 0;'>"
                                + "<h2 style='margin: 0; text-align: center; font-size: 24px; letter-spacing: 1px;'>TRIASE IGD SEKUNDER (PRIORITAS)</h2>"
                                + "</div>"
                        );

                        // Patient Information
                        htmlContent.append(buildPatientInfoSection());

                        // Triase Content Section untuk Sekunder
                        htmlContent.append(buildTriaseSekunderContent(rs2, norawat));

                        // Footer Section untuk Sekunder
                        htmlContent.append(buildTriaseFooter(rs2, "SEKUNDER", ""));

                        htmlContent.append("</div></p>");
                    }

                } catch (Exception e) {
                    System.out.println("Error Data Triase IGD Sekunder (chkTriase1): " + e);
                } finally {
                    if (rs2 != null) {
                        rs2.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error chkTriase1: " + e);
        }
    }

// ==================== UTILITY METHODS ====================
    /**
     * Method untuk membersihkan dan memformat text input dari database
     */
    private String formatTextContent(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "<em style='color: #6c757d;'>Tidak ada data</em>";
        }
        return text.replaceAll("(\r\n|\r|\n|\n\r)", "<br>").trim();
    }

    /**
     * Method untuk generate color berdasarkan prioritas
     */
    private String getPriorityColor(int priority) {
        String[] colors = {
            "#6c757d", // Default/No priority
            "#AA0000", // Highest priority (Immediate)
            "#FF0000", // High priority (Emergency)  
            "#C8C800", // Medium priority (Urgent)
            "#00AA00", // Low priority (Semi-urgent)
            "#969696" // Lowest priority (Non-urgent)
        };

        if (priority >= 0 && priority < colors.length) {
            return colors[priority];
        }
        return colors[0]; // Default color
    }

    /**
     * Method untuk generate summary stats (opsional)
     */
    private String generateTriaseSummary(String norawat) {
        StringBuilder summary = new StringBuilder();

        summary.append(
                "<div style='background: linear-gradient(135deg, #6f42c1, #5a2d91); color: white; padding: 15px; margin: 20px 0; border-radius: 8px;'>"
                + "<h3 style='margin: 0 0 10px 0; font-size: 18px;'>Ringkasan Triase</h3>"
                + "<div style='display: flex; justify-content: space-around; flex-wrap: wrap;'>"
                + "<div style='text-align: center; margin: 5px;'>"
                + "<div style='font-size: 24px; font-weight: bold;'>Waktu Proses</div>"
                + "<div style='font-size: 12px;'>Durasi</div>"
                + "</div>"
                + "<div style='text-align: center; margin: 5px;'>"
                + "<div style='font-size: 24px; font-weight: bold;'>Prioritas</div>"
                + "<div style='font-size: 12px;'>Tingkat</div>"
                + "</div>"
                + "<div style='text-align: center; margin: 5px;'>"
                + "<div style='font-size: 24px; font-weight: bold;'>Status</div>"
                + "<div style='font-size: 12px;'>Kondisi</div>"
                + "</div>"
                + "</div>"
                + "</div>"
        );

        return summary.toString();
    }

    /* private void menampilkanLembarEklaim(String norawat) throws SQLException {
        if (chkLembarEklaim.isSelected() == true) {
                // Query untuk mendapatkan berkas digital (PDF dan gambar)
            String sql = "SELECT master_berkas_digital.nama, berkas_digital_perawatan.lokasi_file FROM berkas_digital_perawatan INNER JOIN "
                    + "master_berkas_digital ON berkas_digital_perawatan.kode = master_berkas_digital.kode "
                    + "WHERE berkas_digital_perawatan.no_rawat = ? AND master_berkas_digital.nama LIKE '%eklaim%'";
            try {
                PreparedStatement ps = koneksi.prepareStatement(sql);
                ps.setString(1, norawat);
                ResultSet rs = ps.executeQuery();
                int w = 1;
                boolean hasContent = false; // Flag untuk cek apakah ada content

                while (rs.next()) {
                    hasContent = true;
                    String fileName = rs.getString("lokasi_file");
                    String fileURL = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/" + fileName;

                    // Proses file PDF
                    if (fileName.toLowerCase().endsWith(".pdf")) {
                        try {
                            processPdfFile(fileURL, rs.getString("nama"), w);
                        } catch (Exception e) {
                            htmlContent.append("<p>Error loading PDF: ").append(e.getMessage()).append("</p>");
                        }
                    } // Proses file gambar (JPG, JPEG)
                    else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                        htmlContent.append("<img alt='Berkas Digital' src='").append(fileURL).append("' style='width: 100%; height: auto; max-width: 100%;'/>");
                    }
                    w++;
                }

                // ===== TAMBAHAN PAGE BREAK DI AKHIR =====
                if (hasContent) {
                    // Tambahkan page break hanya jika ada content yang ditampilkan
                    htmlContent.append("<div class='pagebreak'></div>");
                    htmlContent.append("<div style='page-break-after: always; height: 1px; clear: both;'></div>");
                }
                // ========================================

                // Tutup resource
                rs.close();
                ps.close();
            } catch (SQLException e) {
                System.out.println("Error Berkas Digital: " + e.getMessage());
                e.printStackTrace();
            }
        } 
            String noSEP = Sequel.cariIsi("select no_sep from bridging_sep where no_rawat='" + rs.getString("no_rawat") + "'");

            finalclaim(noSEP);

            String Dir = "./berkasklaim/" + noSEP + ".pdf";
            File fileSAVE = new File(Dir);

            htmlContent.append("<br><br><br>"
                    + "<p class='pagebreak'>"
                    + "<fieldset>"
                    + "<iframe src='" + fileSAVE + "?#toolbar=0&view=FitH' "
                    + "style='width:1050px; height:1340px;' frameborder='0'></iframe>"
                    + "</p></fieldset>");
        }
    }*/
    public void finalclaim(String nosep) {
        try {

            // Create a human-readable JSON string
            String requestJson = "{\n"
                    + " \"metadata\": {\n"
                    + " \"method\": \"claim_print\"\n"
                    + " },\n"
                    + " \"data\": {\n"
                    + " \"nomor_sep\": \"" + nosep + "\"\n"
                    + " }\n"
                    + "}";

            // Use the JSON string in your request
            String request = requestJson;

            JSONObject response = inacbg.request(request);

            // Extract the 'data' field from the response
            if (response.has("data")) {
                String data = response.getString("data");
                try {
                    // Decode the Base64 data
//                    byte[] decodedBytes = Base64.getDecoder().decode(data);

                    byte[] byteArray = Base64.getDecoder().decode(data);
                    InputStream inputStream = new ByteArrayInputStream(byteArray);
                    String Dir = "./berkasklaim/" + nosep + ".pdf";
                    File file = new File(Dir);

                    file.delete();

                    Path path = Paths.get(Dir);
                    Files.copy(inputStream, path);
                    System.out.println("PDF saved successfully at " + path);
//                    Desktop desktop = Desktop.getDesktop();
//                    desktop.open(file);

                } catch (Exception e) {
                    System.err.println("Error decoding Base64 data: " + e.getMessage());
                    e.printStackTrace();
                }

            } else {
                System.out.println("response : " + response);

                System.err.println("\n The 'data' field is missing in the response.");
            }

        } catch (Exception e) {
            System.err.println("Error in cetakKlaim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String[] CODE128_PATTERNS = {
        "212222", "222122", "222221", "121223", "121322", "131222", "122213", "122312", "132212", "221213",
        "221312", "231212", "112232", "122132", "122231", "113222", "123122", "123221", "223211", "221132",
        "221231", "213212", "223112", "312131", "311222", "321122", "321221", "312212", "322112", "322211",
        "212123", "212321", "232121", "111323", "131123", "131321", "112313", "132113", "132311", "211313",
        "231113", "231311", "112133", "112331", "132131", "113123", "113321", "133121", "313121", "211331",
        "231131", "213113", "213311", "213131", "311123", "311321", "331121", "312113", "312311", "332111",
        "314111", "221411", "431111", "111224", "111422", "121124", "121421", "141122", "141221", "112214",
        "112412", "122114", "122411", "142112", "142211", "241211", "221114", "413111", "241112", "134111",
        "111242", "121142", "121241", "114212", "124112", "124211", "411212", "421112", "421211", "212141",
        "214121", "412121", "111143", "111341", "131141", "114113", "114311", "411113", "411311", "113141",
        "114131", "311141", "411131", "211412", "211214", "211232", "2331112"
    };

    private String buatBarcodeSepLokal(String barcodeSep, String barcodeFilename) {
        try {
            BufferedImage image = buatGambarCode128B(barcodeSep, 2, 42, 10);
            File fileBarcode = new File(System.getProperty("java.io.tmpdir"),
                    "barcode_" + safeBarcodeFileName(barcodeFilename) + ".png");
            ImageIO.write(image, "png", fileBarcode);
            return fileBarcode.toURI().toString();
        } catch (Exception e) {
            System.out.println("Error generating local barcode SEP: " + e.getMessage());
            return "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/"
                    + koneksiDB.HYBRIDWEB() + "/berkasrawat/temp/barcode_" + barcodeFilename + ".png";
        }
    }

    private String safeBarcodeFileName(String namaFile) {
        return namaFile == null ? "" : namaFile.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private BufferedImage buatGambarCode128B(String text, int scale, int height, int quietZone) {
        if (text == null || text.trim().equals("")) {
            throw new IllegalArgumentException("Nomor SEP kosong");
        }

        String barcodeText = text.trim();
        int checksum = 104;
        int[] codes = new int[barcodeText.length() + 3];
        int totalCodes = 0;
        codes[totalCodes++] = 104;

        for (int i = 0; i < barcodeText.length(); i++) {
            char c = barcodeText.charAt(i);
            if (c < 32 || c > 127) {
                throw new IllegalArgumentException("Karakter barcode tidak valid: " + c);
            }

            int code = c - 32;
            codes[totalCodes++] = code;
            checksum += code * (i + 1);
        }

        codes[totalCodes++] = checksum % 103;
        codes[totalCodes++] = 106;

        int modules = quietZone * 2;
        for (int i = 0; i < totalCodes; i++) {
            String pattern = CODE128_PATTERNS[codes[i]];
            for (int j = 0; j < pattern.length(); j++) {
                modules += Character.digit(pattern.charAt(j), 10);
            }
        }

        BufferedImage image = new BufferedImage(modules * scale, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(Color.BLACK);

        int x = quietZone * scale;
        for (int i = 0; i < totalCodes; i++) {
            String pattern = CODE128_PATTERNS[codes[i]];
            boolean bar = true;
            for (int j = 0; j < pattern.length(); j++) {
                int width = Character.digit(pattern.charAt(j), 10) * scale;
                if (bar) {
                    g.fillRect(x, 0, width, height);
                }
                x += width;
                bar = !bar;
            }
        }

        g.dispose();
        return image;
    }

    private void menampilkanSEPBPJS(String norawat) {
        try {
            if (chkSepBpjs.isSelected() == true) {
                rs2 = koneksi.prepareStatement(
                        "SELECT bridging_sep.no_sep,bridging_sep.katarak,bridging_sep.no_rawat,bridging_sep.nomr,bridging_sep.nama_pasien,date_format(bridging_sep.tglsep,'%d-%m-%Y') as tglsep,date_format(bridging_sep.tglrujukan,'%d-%m-%Y') as tglrujukan,"
                        + "bridging_sep.no_rujukan,bridging_sep.kdppkrujukan,bridging_sep.nmppkrujukan,bridging_sep.kdppkpelayanan,bridging_sep.nmppkpelayanan,"
                        + "IF( bridging_sep.jnspelayanan like '1%' or bridging_sep.jnspelayanan like '%Ranap%', 'Rawat Inap', 'Rawat Jalan' ) as jnspelayanan,bridging_sep.catatan,bridging_sep.diagawal,bridging_sep.nmdiagnosaawal,"
                        + "bridging_sep.kdpolitujuan,bridging_sep.nmpolitujuan,bridging_sep.klsrawat,IF( bridging_sep.lakalantas = '0', 'Kasus Kecelakaan', 'Bukan Kasus Kecelakaan' ),concat( bridging_sep.nmkec, ', ', bridging_sep.nmkab, ', ',bridging_sep.nmprop ) AS lokasilaka,bridging_sep.`user`,date_format(bridging_sep.tanggal_lahir,'%d-%m-%Y') as tanggal_lahir,bridging_sep.peserta,bridging_sep.jkel,bridging_sep.no_kartu,"
                        + "bridging_sep.asal_rujukan,bridging_sep.eksekutif,bridging_sep.cob,bridging_sep.notelep,IF(bridging_sep.tujuankunjungan = '0','Konsultasi dokter(pertama)','Kunjungan Kontrol(ulangan)') as tujuankunjungan,bridging_sep.flagprosedur,"
                        + "IF(bridging_sep.klsnaik!='',bridging_sep.klsnaik,'Sesuai Kelas Hak') as klsnaik,bridging_sep.pembiayaan,bridging_sep.nmdpdjp,bridging_sep.lakalantas,bpjs_prb.prb,date_format(adddate(bridging_sep.tglrujukan, interval '3' month), '%d-%m-%Y') as berlaku FROM bridging_sep LEFT JOIN bpjs_prb "
                        + "ON bridging_sep.no_sep = bpjs_prb.no_sep where bridging_sep.no_rawat='" + norawat + "' "
                        + "order by case when bridging_sep.jnspelayanan like '1%' or bridging_sep.jnspelayanan like '%Ranap%' then 0 else 1 end, bridging_sep.tglsep desc, bridging_sep.no_sep desc limit 1").executeQuery();
                if (rs2.next()) {
                    if (!rs2.getString("no_rawat").isEmpty()) {

                        // Generate barcode untuk No. SEP
                        String barcodeSep = rs2.getString("no_sep");
                        String barcodeFilename = barcodeSep.replace(" ", "_");
                        String barcodeImageSrc = buatBarcodeSepLokal(barcodeSep, barcodeFilename);
                        try {
                            get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/generatebarcodesep.php?sepbpjs=" + barcodeFilename);
                            int barcodeStatus = http.executeMethod(get);
                            System.out.println("Barcode generation status: " + barcodeStatus);
                            Thread.sleep(500);
                        } catch (Exception e) {
                            System.out.println("Error generating barcode: " + e.getMessage());
                        }

                        // Start SEP section - LAYOUT MELEBAR PENUH
                        htmlContent.append("<div class='page-section' style='width: 100%; margin: 0 auto;'>");
                        htmlContent.append("<fieldset style='border: 1px solid #000; padding: 5px; margin: 0;'>");
                        htmlContent.append("<table class='sep' width='100%' style='font-size:9px; padding:0; border:0px solid #fff; line-height:1.1; border-collapse: collapse;' border='0' cellpadding='1' cellspacing='0'>");

                        // Header dengan logo BPJS, title, dan barcode - UKURAN DIPERKECIL
                        htmlContent.append("<tr><td colspan='6'><table border='0' cellpadding='0' cellspacing='0' style='line-height:1.0; width: 100%;'><tr>")
                                .append("<td width='20%' style='text-align: left;'><img alt='Image' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/images/bpjslogo.png' width='100' height='40'></td>")
                                .append("<td width='60%' style='text-align:center; vertical-align: middle;'><div style='font-weight:bold;font-size:11px;margin:0;padding:0;'><b>SURAT ELEGIBILITAS PESERTA</b></div><div style='font-size:9px;margin:0;padding:0;'><b>").append(akses.getnamars().toUpperCase()).append("</b></div></td>")
                                .append("<td width='20%' style='text-align:right; vertical-align: middle;'><img alt='Barcode' src='").append(barcodeImageSrc).append("' width='120' height='30' onerror=\"this.style.display='none';\"></td>")
                                .append("</tr></table></td></tr>");

                        // Katarak warning dan PRB - UKURAN FONT DIKURANGI
                        htmlContent.append("<tr><td colspan='6' style='padding:1px 0;'><table style='margin:0; width: 100%;' border='0' cellpadding='0' cellspacing='0'><tr>")
                                .append("<td width='70%' style='font-size: 9px;'><b>").append((rs2.getString("katarak").equals("1.Ya") ? "*** KATARAK : YA ***" : "")).append("</b></td>")
                                .append("<td width='30%' style='text-align:right; font-size: 9px;'><b>").append((rs2.getString("prb") == null ? "" : "PRB : " + rs2.getString("prb"))).append("</b></td>")
                                .append("</tr></table></td></tr>");

                        // Detail data pasien - FONT SIZE DIKURANGI, PADDING DIKURANGI
                        htmlContent.append("<tr><td colspan='6' style='padding:0;'><table width='100%' style='line-height:1.1; border-spacing:0; font-size: 9px;' border='0' cellpadding='1' cellspacing='0'>");

                        // Baris 1: No. SEP dan Peserta
                        htmlContent.append("<tr>")
                                .append("<td width='16%' style='padding:1px;'>No. SEP</td><td width='1%' style='padding:1px;'>:</td><td width='33%' style='padding:1px;'><b>").append(rs2.getString("no_sep")).append("</b></td>")
                                .append("<td width='16%' style='padding:1px;'>Peserta</td><td width='1%' style='padding:1px;'>:</td><td width='33%' style='padding:1px;'>").append(rs2.getString("peserta")).append("</td>")
                                .append("</tr>");

                        // Baris 2: Tgl. SEP dan Jns. Rawat
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Tgl. SEP</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("tglsep")).append("</td>")
                                .append("<td style='padding:1px;'>Jns. Rawat</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("jnspelayanan")).append("</td>")
                                .append("</tr>");

                        // Baris 3: No. Kartu dan Jns.Kunjungan
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>No. Kartu</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("no_kartu")).append(" (MR: ").append(rs2.getString("nomr")).append(")</td>")
                                .append("<td style='padding:1px;'>Jns.Kunjungan</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("tujuankunjungan")).append("</td>")
                                .append("</tr>");

                        // Baris 4: Nama Peserta (span seluruh kolom)
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Nama Peserta</td><td style='padding:1px;'>:</td><td colspan='4' style='padding:1px;'>").append(rs2.getString("nama_pasien")).append("</td>")
                                .append("</tr>");

                        // Baris 5: Tgl. Lahir dan Poli Perujuk
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Tgl. Lahir</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("tanggal_lahir")).append(" (").append((rs2.getString("jkel").equals("P") ? "PEREMPUAN" : "LAKI-LAKI")).append(")</td>")
                                .append("<td style='padding:1px;'>Poli Perujuk</td><td style='padding:1px;'>:</td><td style='padding:1px;'>-</td>")
                                .append("</tr>");

                        // Baris 6: No. Telepon dan Kls. Hak
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>No. Telepon</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("notelep")).append("</td>")
                                .append("<td style='padding:1px;'>Kls. Hak</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("klsrawat").replaceAll("1", "Kelas 1").replaceAll("2", "Kelas 2").replaceAll("3", "Kelas 3")).append("</td>")
                                .append("</tr>");

                        // Baris 7: Sub/Spesialis dan Kls. Rawat
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Sub/Spesialis</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("nmpolitujuan")).append("</td>")
                                .append("<td style='padding:1px;'>Kls. Rawat</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("klsnaik").replaceAll("1", "Kelas VVIP").replaceAll("2", "Kelas VIP").replaceAll("3", "Kelas 1").replaceAll("4", "Kelas 2").replaceAll("5", "Kelas 3").replaceAll("6", "ICCU").replaceAll("7", "ICU").replaceAll("8", "Diatas Kelas 1")).append("</td>")
                                .append("</tr>");

                        // Baris 8: Dokter dan Penjamin
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Dokter</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("nmdpdjp")).append("</td>")
                                .append("<td style='padding:1px;'>Penjamin</td><td style='padding:1px;'>:</td><td style='padding:1px;'>").append(rs2.getString("lakalantas").replaceAll("0", "BPJS Kesehatan").replaceAll("1", "Jasa Raharja").replaceAll("2", "Jasa Raharja & BPJS Ketenagakerjaan/Taspen").replaceAll("3", "BPJS Ketenagakerjaan, Taspen, dll")).append("</td>")
                                .append("</tr>");

                        // Baris 9: Faskes Perujuk (span seluruh kolom)
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Faskes Perujuk</td><td style='padding:1px;'>:</td><td colspan='4' style='padding:1px;'>").append(rs2.getString("nmppkrujukan")).append("</td>")
                                .append("</tr>");

                        // Baris 10: Diagnosa Awal (span seluruh kolom)
                        htmlContent.append("<tr>")
                                .append("<td style='padding:1px;'>Diagnosa Awal</td><td style='padding:1px;'>:</td><td colspan='4' style='padding:1px;'>").append(rs2.getString("nmdiagnosaawal")).append("</td>")
                                .append("</tr>");

                        htmlContent.append("</table></td></tr>");

                        // Bagian bawah dengan catatan dan QR Code - UKURAN DIKURANGI
                        htmlContent.append("<tr><td colspan='6' style='padding:2px 0;'><table width='100%' style='line-height:1.1; font-size: 9px;' border='0' cellpadding='1' cellspacing='0'><tr>")
                                .append("<td width='16%' valign='top' style='padding:1px;'>Catatan</td><td width='1%' valign='top' style='padding:1px;'>:</td><td width='58%' valign='top' style='padding:1px;'>").append(rs2.getString("catatan")).append("</td>")
                                .append("<td width='25%' style='text-align:center; padding:1px; vertical-align: top;'>Pasien/Keluarga Pasien");

                        // Generate QR Code dengan ukuran yang lebih kecil
                        String qrFilename = rs2.getString("no_sep").replace(" ", "_");
                        try {
                            get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/generateqrcodesep.php?sepbpjs=" + qrFilename);
                            int qrStatus = http.executeMethod(get);
                            System.out.println("QR Code generation status: " + qrStatus);
                            Thread.sleep(500);

                            htmlContent.append("<br><img alt='QR Code' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/berkasrawat/temp/").append(qrFilename).append(".png' width='60' height='60' onerror=\"this.style.display='none';\"><br>");
                        } catch (Exception qrError) {
                            System.out.println("Error generating QR Code: " + qrError.getMessage());
                            htmlContent.append("<br><small>QR Code tidak dapat dibuat</small><br>");
                        }
                        htmlContent.append("<div style='margin:1px 0; font-size: 8px;'>").append(rs2.getString("nama_pasien")).append("</div>");

                        htmlContent.append("</td></tr></table></td></tr>");

                        // Footer - UKURAN FONT DIKURANGI
                        htmlContent.append("<tr><td colspan='6' style='padding:2px 0; font-size: 8px;'><i>*Saya Menyetujui BPJS Kesehatan menggunakan informasi Medis Pasien jika diperlukan.<br>");
                        htmlContent.append("*SEP bukan sebagai bukti penjaminan peserta<br>");
                        htmlContent.append("Cetakan ke 1 ").append(rs2.getString("tglsep")).append("</i></td></tr>");

                        htmlContent.append("</table></fieldset>");
                        htmlContent.append("</div>"); // close page-section

                        // Dokumen berikutnya sudah mengatur page-break sendiri.
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif SEP BPJS : " + e);
        }
    }

    // Method untuk Resume Medis Pasien (Rawat Jalan)
    private void menampilkanResumeRawatJalan(String norawat) {
        if (chkResume.isSelected() == true) {
            try {
                rs2 = koneksi.prepareStatement(
                        "select resume_pasien.kd_dokter,dokter.nm_dokter,resume_pasien.keluhan_utama,resume_pasien.jalannya_penyakit,"
                        + "resume_pasien.pemeriksaan_penunjang,resume_pasien.hasil_laborat,resume_pasien.diagnosa_utama,resume_pasien.kd_diagnosa_utama,"
                        + "resume_pasien.diagnosa_sekunder,resume_pasien.kd_diagnosa_sekunder,resume_pasien.diagnosa_sekunder2,resume_pasien.kd_diagnosa_sekunder2,"
                        + "resume_pasien.diagnosa_sekunder3,resume_pasien.kd_diagnosa_sekunder3,resume_pasien.diagnosa_sekunder4,resume_pasien.kd_diagnosa_sekunder4,"
                        + "resume_pasien.prosedur_utama,resume_pasien.kd_prosedur_utama,resume_pasien.prosedur_sekunder,resume_pasien.kd_prosedur_sekunder,"
                        + "resume_pasien.prosedur_sekunder2,resume_pasien.kd_prosedur_sekunder2,resume_pasien.prosedur_sekunder3,resume_pasien.kd_prosedur_sekunder3,"
                        + "resume_pasien.kondisi_pulang,resume_pasien.obat_pulang,reg_periksa.almt_pj "
                        + "from resume_pasien inner join dokter on resume_pasien.kd_dokter=dokter.kd_dokter "
                        + "inner join reg_periksa on resume_pasien.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "where resume_pasien.no_rawat='" + rs.getString("no_rawat") + "'").executeQuery();
                if (rs2.next()) {
                    htmlContent.append(
                            ""
                            + "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                            + "<fieldset style='border: none; padding: 0; margin: 0;'>");
                    Copsurat();
                    htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>RESUME MEDIS PASIEN</div>");

                    // Tabel informasi pasien
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
                            + "<tr style='background-color: #f5f5f5;'><td width='25%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Nama Pasien</td><td width='70%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs.getString("nm_pasien")).append("</td></tr>"
                            + "<tr><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>No. RM</td><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs.getString("no_rkm_medis")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Tgl Lahir</td><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs.getString("tgl_lahir")).append("</td></tr>"
                            + "<tr><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Poli/Unit</td><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs.getString("nm_poli")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Alamat</td><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs2.getString("almt_pj")).append("</td></tr>"
                            + "<tr><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Dokter</td><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs2.getString("nm_dokter")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Tgl Periksa</td><td style='padding: 5px 8px; border: 1px solid #e0e0e0;'>: ").append(rs.getString("tgl_registrasi")).append(" ").append(rs.getString("jam_reg")).append("</td></tr>"
                            + "</table><br>");

                    // Keluhan utama dan riwayat penyakit
                    String keluhanUtama = rs2.getString("keluhan_utama");
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Keluhan utama dan riwayat penyakit yang positif:</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(keluhanUtama != null ? keluhanUtama.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Jalannya penyakit selama perawatan
                    String jalanyaPenyakit = rs2.getString("jalannya_penyakit");
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Jalannya penyakit selama perawatan:</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(jalanyaPenyakit != null ? jalanyaPenyakit.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Pemeriksaan penunjang yang positif
                    String pemeriksaanPenunjang = rs2.getString("pemeriksaan_penunjang");
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Pemeriksaan penunjang yang positif:</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(pemeriksaanPenunjang != null ? pemeriksaanPenunjang.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Hasil laboratorium yang positif
                    String hasilLaborat = rs2.getString("hasil_laborat");
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Hasil laboratorium yang positif:</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(hasilLaborat != null ? hasilLaborat.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Diagnosa Akhir
                    htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #e5e5e5; font-weight: bold;'>"
                            + "<td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>Diagnosa Akhir</td>"
                            + "<td width='15%' align='center' style='padding: 6px 8px; border: 1px solid #e0e0e0;'>Kode ICD</td>"
                            + "</tr>");

                    int diagnosisIndex = 0;

                    // Diagnosa Utama
                    String diagnosisUtama = rs2.getString("diagnosa_utama");
                    String kdDiagnosisUtama = rs2.getString("kd_diagnosa_utama");
                    if (diagnosisUtama != null && !diagnosisUtama.trim().isEmpty()) {
                        String rowBg = (diagnosisIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                        htmlContent.append("<tr style='background-color: ").append(rowBg).append(";'>")
                                .append("<td style='padding: 6px 8px; border: 1px solid #e0e0e0; padding-left: 20px;'>- Diagnosa Utama : ").append(diagnosisUtama).append("</td>")
                                .append("<td align='center' style='padding: 6px 8px; border: 1px solid #e0e0e0;'> ").append(kdDiagnosisUtama != null ? kdDiagnosisUtama : "").append(" </td>")
                                .append("</tr>");
                        diagnosisIndex++;
                    }

                    // Diagnosa Sekunder
                    String diagnosisSekunder = rs2.getString("diagnosa_sekunder");
                    String kdDiagnosisSekunder = rs2.getString("kd_diagnosa_sekunder");
                    if (diagnosisSekunder != null && !diagnosisSekunder.trim().isEmpty()) {
                        String rowBg = (diagnosisIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                        htmlContent.append("<tr style='background-color: ").append(rowBg).append(";'>")
                                .append("<td style='padding: 6px 8px; border: 1px solid #e0e0e0; padding-left: 20px;'>- Diagnosa Sekunder : ").append(diagnosisSekunder).append("</td>")
                                .append("<td align='center' style='padding: 6px 8px; border: 1px solid #e0e0e0;'> ").append(kdDiagnosisSekunder != null ? kdDiagnosisSekunder : "").append(" </td>")
                                .append("</tr>");
                        diagnosisIndex++;
                    }

                    String diagnosisSekunder2 = rs2.getString("diagnosa_sekunder2");
                    String kdDiagnosisSekunder2 = rs2.getString("kd_diagnosa_sekunder2");
                    if (diagnosisSekunder2 != null && !diagnosisSekunder2.trim().isEmpty()) {
                        String rowBg = (diagnosisIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                        htmlContent.append("<tr style='background-color: ").append(rowBg).append(";'>")
                                .append("<td style='padding: 6px 8px; border: 1px solid #e0e0e0; padding-left: 40px;'>: ").append(diagnosisSekunder2).append("</td>")
                                .append("<td align='center' style='padding: 6px 8px; border: 1px solid #e0e0e0;'> ").append(kdDiagnosisSekunder2 != null ? kdDiagnosisSekunder2 : "").append(" </td>")
                                .append("</tr>");
                        diagnosisIndex++;
                    }

                    String diagnosisSekunder3 = rs2.getString("diagnosa_sekunder3");
                    String kdDiagnosisSekunder3 = rs2.getString("kd_diagnosa_sekunder3");
                    if (diagnosisSekunder3 != null && !diagnosisSekunder3.trim().isEmpty()) {
                        String rowBg = (diagnosisIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                        htmlContent.append("<tr style='background-color: ").append(rowBg).append(";'>")
                                .append("<td style='padding: 6px 8px; border: 1px solid #e0e0e0; padding-left: 40px;'>: ").append(diagnosisSekunder3).append("</td>")
                                .append("<td align='center' style='padding: 6px 8px; border: 1px solid #e0e0e0;'> ").append(kdDiagnosisSekunder3 != null ? kdDiagnosisSekunder3 : "").append(" </td>")
                                .append("</tr>");
                        diagnosisIndex++;
                    }

                    String diagnosisSekunder4 = rs2.getString("diagnosa_sekunder4");
                    String kdDiagnosisSekunder4 = rs2.getString("kd_diagnosa_sekunder4");
                    if (diagnosisSekunder4 != null && !diagnosisSekunder4.trim().isEmpty()) {
                        String rowBg = (diagnosisIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                        htmlContent.append("<tr style='background-color: ").append(rowBg).append(";'>")
                                .append("<td style='padding: 6px 8px; border: 1px solid #e0e0e0; padding-left: 40px;'>: ").append(diagnosisSekunder4).append("</td>")
                                .append("<td align='center' style='padding: 6px 8px; border: 1px solid #e0e0e0;'> ").append(kdDiagnosisSekunder4 != null ? kdDiagnosisSekunder4 : "").append(" </td>")
                                .append("</tr>");
                        diagnosisIndex++;
                    }

                    htmlContent.append("</table><br>");

                    // Prosedur/Tindakan Utama
                    String prosedurUtama = rs2.getString("prosedur_utama");
                    String kdProsedurUtama = rs2.getString("kd_prosedur_utama");
                    if (prosedurUtama != null && !prosedurUtama.trim().isEmpty()) {
                        htmlContent.append("<tr><td style='padding-left: 20px;'>- Prosedur/Tindakan Utama : ").append(prosedurUtama).append("</td>"
                                + "<td align='center'> ").append(kdProsedurUtama != null ? kdProsedurUtama : "").append(" </td></tr>");
                    }

                    // Prosedur/Tindakan Sekunder
                    String prosedurSekunder = rs2.getString("prosedur_sekunder");
                    String kdProsedurSekunder = rs2.getString("kd_prosedur_sekunder");
                    if (prosedurSekunder != null && !prosedurSekunder.trim().isEmpty()) {
                        htmlContent.append("<tr><td style='padding-left: 20px;'>- Prosedur/Tindakan Sekunder : ").append(prosedurSekunder).append("</td>"
                                + "<td align='center'> ").append(kdProsedurSekunder != null ? kdProsedurSekunder : "").append(" </td></tr>");
                    }

                    // Prosedur/Tindakan Sekunder 2
                    String prosedurSekunder2 = rs2.getString("prosedur_sekunder2");
                    String kdProsedurSekunder2 = rs2.getString("kd_prosedur_sekunder2");
                    if (prosedurSekunder2 != null && !prosedurSekunder2.trim().isEmpty()) {
                        htmlContent.append("<tr><td style='padding-left: 40px;'>: ").append(prosedurSekunder2).append("</td>"
                                + "<td align='center'> ").append(kdProsedurSekunder2 != null ? kdProsedurSekunder2 : "").append(" </td></tr>");
                    }

                    // Prosedur/Tindakan Sekunder 3
                    String prosedurSekunder3 = rs2.getString("prosedur_sekunder3");
                    String kdProsedurSekunder3 = rs2.getString("kd_prosedur_sekunder3");
                    if (prosedurSekunder3 != null && !prosedurSekunder3.trim().isEmpty()) {
                        htmlContent.append("<tr><td style='padding-left: 40px;'>: ").append(prosedurSekunder3).append("</td>"
                                + "<td align='center'> ").append(kdProsedurSekunder3 != null ? kdProsedurSekunder3 : "").append(" </td></tr>");
                    }

                    htmlContent.append("</table><br>");

                    // Status pulang - disesuaikan dengan struktur tabel yang tersedia
                    String kondisiPulang = rs2.getString("kondisi_pulang");
                    String obatPulang = rs2.getString("obat_pulang");
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0'>"
                            + "<tr><td width='50%'><strong>Kondisi pasien pulang: ").append(kondisiPulang != null ? kondisiPulang : "-").append("</strong></td>"
                            + "<td width='50%'><strong>Tanggal Kontrol: </strong></td></tr>"
                            + "</table><br>");

                    // Terapi Pulang
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0'>"
                            + "<tr><td><strong>Terapi Pulang:</strong></td></tr>"
                            + "<tr><td style='padding-left: 20px;'>").append(obatPulang != null ? obatPulang.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Tanda tangan dokter sederhana
                    htmlContent.append("<table width='100%' border='0' cellpadding='10px' cellspacing='0'>"
                            + "<tr><td width='50%'></td><td width='50%' align='center'>Dokter Penanggung Jawab</td></tr>"
                            + "<tr><td></td><td align='center'>");

                    // QR Code untuk dokter
                    get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + rs2.getString("kd_dokter").replace(" ", "_"));
                    http.executeMethod(get);

                    htmlContent.append("<img width='90' height='90' src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + rs2.getString("kd_dokter") + ".png'/><br>")
                            .append(rs2.getString("nm_dokter"));
                    htmlContent.append("</td></tr>"
                            + "<tr><td></td><td align='center'>"
                            + "</td></tr>"
                            + "</table>");

                    htmlContent.append("</fieldset></div>");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi Resume Rawat Jalan : " + e);
            } finally {
                if (rs2 != null) {
                    try {
                        rs2.close();
                    } catch (Exception e) {
                        System.out.println("Error closing rs2: " + e);
                    }
                }
            }
        }
    }

    private void menampilkanHasilLab(String norawat) {
        if (chkHasilLab.isSelected() == true) {
            if (!Sequel.cariIsi("SELECT COUNT(no_rawat) FROM periksa_lab WHERE no_rawat=?", norawat).equals("0")) {
                try {
                    htmlContent.append(
                            ""
                            + "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                            + "<fieldset style='border: none; padding: 0; margin: 0;'>");

                    Copsurat();
                    htmlContent.append(
                            "<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>HASIL LABORATORIUM</div>");

                    // Tabel informasi pasien
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>No.Rekam Medis</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("no_rkm_medis")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>Nama Pasien</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("nm_pasien")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>Tgl. Lahir</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("tgl_lahir")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>No.Rawat</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("no_rawat")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>No.Registrasi</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("no_reg")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>Tanggal Registrasi</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("tgl_registrasi")).append(" ").append(rs.getString("jam_reg")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>Unit/Poliklinik</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("nm_poli")).append(polirujukan).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>Dokter Poli</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("nm_dokter")).append(dokterrujukan).append("</td></tr>");

                    // Informasi DPJP Ranap jika rawat inap
                    if (rs.getString("status_lanjut").equals("Ranap")) {
                        try {
                            rs3 = koneksi.prepareStatement(
                                    "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='" + rs.getString("no_rawat") + "'").executeQuery();
                            if (rs3.next()) {
                                htmlContent.append(
                                        "<tr>"
                                        + "<td valign='top' width='20%'>DPJP Ranap</td>"
                                        + "<td valign='top' width='1%' align='center'>:</td>"
                                        + "<td valign='top' width='79%'>");
                                rs3.beforeFirst();
                                urutdpjp = 1;
                                while (rs3.next()) {
                                    htmlContent.append(urutdpjp).append(". ").append(rs3.getString("nm_dokter")).append("&nbsp;&nbsp;");
                                    urutdpjp++;
                                }
                                htmlContent.append("</td></tr>");
                            }
                        } catch (SQLException e) {
                            System.out.println("Error getting DPJP Ranap: " + e.getMessage());
                        } finally {
                            if (rs3 != null) {
                                try {
                                    rs3.close();
                                } catch (SQLException e) {
                                    System.out.println("Error closing rs3: " + e.getMessage());
                                }
                            }
                        }
                    }

                    // Informasi tambihan pasien
                    htmlContent.append("<tr><td valign='top' width='20%'>Cara Bayar</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("png_jawab")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Penanggung Jawab</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("p_jawab")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Alamat P.J.</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("almt_pj")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Hubungan P.J.</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("hubunganpj")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Status</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("status_lanjut")).append("</td></tr>"
                            + "</table>");

                    // Pemeriksaan Laboratorium PK & MB
                    try {
                        rs4 = koneksi.prepareStatement(
                                "select periksa_lab.tgl_periksa,periksa_lab.jam from periksa_lab where periksa_lab.kategori<>'PA' and periksa_lab.no_rawat='" + rs.getString("no_rawat") + "' "
                                + "group by concat(periksa_lab.no_rawat,periksa_lab.tgl_periksa,periksa_lab.jam) order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                        if (rs4.next()) {
                            htmlContent.append(
                                    "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                    + "<tr><td valign='top' colspan='5'><b>Pemeriksaan Laboratorium PK & MB</b></td></tr>"
                                    + "<tr align='center'>"
                                    + "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>"
                                    + "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>"
                                    + "<td valign='top' width='25%' bgcolor='#FFFAF8'>Nama Pemeriksaan</td>"
                                    + "<td valign='top' width='40%' bgcolor='#FFFAF8'>Hasil</td>"
                                    + "<td valign='top' width='17%' bgcolor='#FFFAF8'>Nilai Rujukan</td>"
                                    + "</tr>");
                            rs4.beforeFirst();
                            w = 1;
                            while (rs4.next()) {
                                try {
                                    rs2 = koneksi.prepareStatement(
                                            "select periksa_lab.kd_jenis_prw, "
                                            + "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "
                                            + "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "
                                            + "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "
                                            + "where periksa_lab.kategori<>'PA' and periksa_lab.no_rawat='" + rs.getString("no_rawat") + "' "
                                            + "and periksa_lab.tgl_periksa='" + rs4.getString("tgl_periksa") + "' and periksa_lab.jam='" + rs4.getString("jam") + "'").executeQuery();
                                    s = 1;
                                    while (rs2.next()) {
                                        if (s == 1) {
                                            htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(rs4.getString("tgl_periksa")).append(" ").append(rs4.getString("jam")).append("</td><td valign='top'><b>").append(rs2.getString("nm_perawatan")).append("</b></td><td valign='top'></td><td valign='top'></td></tr>");
                                        } else {
                                            htmlContent.append("<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'><b>").append(rs2.getString("nm_perawatan")).append("</b></td><td valign='top'></td><td valign='top'></td></tr>");
                                        }

                                        try {
                                            rs3 = koneksi.prepareStatement(
                                                    "select template_laboratorium.Pemeriksaan, detail_periksa_lab.nilai,"
                                                    + "template_laboratorium.satuan,detail_periksa_lab.nilai_rujukan,detail_periksa_lab.biaya_item,"
                                                    + "detail_periksa_lab.keterangan from detail_periksa_lab inner join "
                                                    + "template_laboratorium on detail_periksa_lab.id_template=template_laboratorium.id_template "
                                                    + "where detail_periksa_lab.no_rawat='" + rs.getString("no_rawat") + "' and "
                                                    + "detail_periksa_lab.kd_jenis_prw='" + rs2.getString("kd_jenis_prw") + "' and "
                                                    + "detail_periksa_lab.tgl_periksa='" + rs4.getString("tgl_periksa") + "' and "
                                                    + "detail_periksa_lab.jam='" + rs4.getString("jam") + "' order by detail_periksa_lab.kd_jenis_prw,template_laboratorium.urut ").executeQuery();
                                            if (rs3.next()) {
                                                rs3.beforeFirst();
                                                while (rs3.next()) {
                                                    String pemeriksaan = rs3.getString("Pemeriksaan") != null ? rs3.getString("Pemeriksaan") : "";
                                                    String keterangan = rs3.getString("keterangan") != null ? rs3.getString("keterangan") : "";
                                                    String nilai = rs3.getString("nilai") != null ? rs3.getString("nilai") : "";
                                                    String satuan = rs3.getString("satuan") != null ? rs3.getString("satuan") : "";
                                                    String nilaiRujukan = rs3.getString("nilai_rujukan") != null ? rs3.getString("nilai_rujukan") : "";

                                                    htmlContent.append("<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'>&nbsp;&nbsp;&nbsp;&nbsp;").append(pemeriksaan + keterangan.replaceAll("(H|L|R|NR|N|P)", "<font color='red'>*</font></b>")).append("</td><td valign='top' " + keterangan.replaceAll("(H|L|R|NR|N|P)", "bgcolor='#ffaba8'") + ">&nbsp;&nbsp;&nbsp;&nbsp;<b>").append(nilai).append(" ").append(satuan).append("</b></td><td valign='top'>&nbsp;&nbsp;&nbsp;&nbsp;<b>").append(nilaiRujukan).append("</b></td></tr>");
                                                }
                                            }
                                        } catch (SQLException e) {
                                            System.out.println("Error getting lab details: " + e.getMessage());
                                        } finally {
                                            if (rs3 != null) {
                                                try {
                                                    rs3.close();
                                                } catch (SQLException e) {
                                                    System.out.println("Error closing rs3: " + e.getMessage());
                                                }
                                            }
                                        }
                                        s++;
                                    }

                                    try {
                                        rs3 = koneksi.prepareStatement("select saran,kesan from saran_kesan_lab where no_rawat='" + rs.getString("no_rawat") + "' and tgl_periksa='" + rs4.getString("tgl_periksa") + "' and jam='" + rs4.getString("jam") + "'").executeQuery();
                                        if (rs3.next()) {
                                            String kesan = rs3.getString("kesan") != null ? rs3.getString("kesan") : "";
                                            String saran = rs3.getString("saran") != null ? rs3.getString("saran") : "";
                                            htmlContent.append("<tr><td valign='top'>Kesan</td><td valign='top' colspan='4'>: <b>").append(kesan).append("</b></td></tr><tr><td valign='top'>Saran</td><td valign='top' colspan='4'>: <b>").append(saran).append("</b></td></tr>");
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Error getting saran kesan: " + e.getMessage());
                                    } finally {
                                        if (rs3 != null) {
                                            try {
                                                rs3.close();
                                            } catch (SQLException e) {
                                                System.out.println("Error closing rs3: " + e.getMessage());
                                            }
                                        }
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Error in lab processing: " + e.getMessage());
                                } finally {
                                    if (rs2 != null) {
                                        try {
                                            rs2.close();
                                        } catch (SQLException e) {
                                            System.out.println("Error closing rs2: " + e.getMessage());
                                        }
                                    }
                                }
                                w++;
                            }
                            htmlContent.append("</table>");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting lab PK & MB: " + e.getMessage());
                    } finally {
                        if (rs4 != null) {
                            try {
                                rs4.close();
                            } catch (SQLException e) {
                                System.out.println("Error closing rs4: " + e.getMessage());
                            }
                        }
                    }

                    // Pemeriksaan Laboratorium PA
                    try {
                        rs2 = koneksi.prepareStatement(
                                "select periksa_lab.tgl_periksa,periksa_lab.jam,periksa_lab.kd_jenis_prw, "
                                + "jns_perawatan_lab.nm_perawatan,petugas.nama,periksa_lab.biaya,periksa_lab.dokter_perujuk,dokter.nm_dokter "
                                + "from periksa_lab inner join jns_perawatan_lab on periksa_lab.kd_jenis_prw=jns_perawatan_lab.kd_jenis_prw "
                                + "inner join petugas on periksa_lab.nip=petugas.nip inner join dokter on periksa_lab.kd_dokter=dokter.kd_dokter "
                                + "where periksa_lab.kategori='PA' and periksa_lab.no_rawat='" + rs.getString("no_rawat") + "' order by periksa_lab.tgl_periksa,periksa_lab.jam").executeQuery();
                        if (rs2.next()) {
                            htmlContent.append(
                                    "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                    + "<tr><td valign='top' colspan='5'><b>Pemeriksaan Laboratorium PA</b></td></tr>"
                                    + "<tr align='center'>"
                                    + "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>"
                                    + "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>"
                                    + "<td valign='top' width='36%' bgcolor='#FFFAF8'>Nama Pemeriksaan</td>"
                                    + "<td valign='top' width='18%' bgcolor='#FFFAF8'>Dokter PJ</td>"
                                    + "<td valign='top' width='17%' bgcolor='#FFFAF8'>Petugas</td>"
                                    + "</tr>");
                            rs2.beforeFirst();
                            w = 1;
                            while (rs2.next()) {
                                htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(rs2.getString("tgl_periksa")).append(" ").append(rs2.getString("jam")).append("</td><td valign='top'><b>").append(rs2.getString("nm_perawatan")).append("</b></td><td valign='top'>").append(rs2.getString("nm_dokter")).append("</td><td valign='top'>").append(rs2.getString("nama")).append("</td></tr>");
                                try {
                                    rs3 = koneksi.prepareStatement(
                                            "select diagnosa_klinik,makroskopik,mikroskopik,kesimpulan,kesan from detail_periksa_labpa "
                                            + "where no_rawat='" + rs.getString("no_rawat") + "' and kd_jenis_prw='" + rs2.getString("kd_jenis_prw") + "' and "
                                            + "tgl_periksa='" + rs2.getString("tgl_periksa") + "' and jam='" + rs2.getString("jam") + "'").executeQuery();
                                    if (rs3.next()) {
                                        file = Sequel.cariIsi("select photo from detail_periksa_labpa_gambar where no_rawat='" + rs.getString("no_rawat") + "' and kd_jenis_prw='" + rs2.getString("kd_jenis_prw") + "' and tgl_periksa='" + rs2.getString("tgl_periksa") + "' and jam='" + rs2.getString("jam") + "'");

                                        String diagnosisKlinik = rs3.getString("diagnosa_klinik") != null ? rs3.getString("diagnosa_klinik") : "";
                                        String makroskopik = rs3.getString("makroskopik") != null ? rs3.getString("makroskopik") : "";
                                        String mikroskopik = rs3.getString("mikroskopik") != null ? rs3.getString("mikroskopik") : "";
                                        String kesimpulan = rs3.getString("kesimpulan") != null ? rs3.getString("kesimpulan") : "";
                                        String kesan = rs3.getString("kesan") != null ? rs3.getString("kesan") : "";

                                        htmlContent.append("<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'>Diagnosa Klinis</td><td valign='top' colspan='3'>: ").append(diagnosisKlinik).append("</td></tr>"
                                                + "<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'>Makroskopik</td><td valign='top' colspan='3'>: ").append(makroskopik).append("</td></tr>"
                                                + "<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'>Mikroskopik</td><td valign='top' colspan='3'>: ").append(mikroskopik).append("</td></tr>"
                                                + "<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'>Kesimpulan</td><td valign='top' colspan='3'>: ").append(kesimpulan).append("</td></tr>"
                                                + "<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top'>Kesan</td><td valign='top' colspan='3'>: ").append(kesan).append("</td></tr>");

                                        if (file != null && !file.isEmpty()) {
                                            htmlContent.append("<tr><td valign='top' align='center'></td><td valign='top'></td><td valign='top' colspan='4' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/labpa/").append(file).append("'><img alt='Gambar PA' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/labpa/").append(file).append("' width='450' height='450'/></a></td></tr>");
                                        }
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Error getting PA details: " + e.getMessage());
                                } finally {
                                    if (rs3 != null) {
                                        try {
                                            rs3.close();
                                        } catch (SQLException e) {
                                            System.out.println("Error closing rs3: " + e.getMessage());
                                        }
                                    }
                                }
                                w++;
                            }
                            htmlContent.append("</table>");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting lab PA: " + e.getMessage());
                    } finally {
                        if (rs2 != null) {
                            try {
                                rs2.close();
                            } catch (SQLException e) {
                                System.out.println("Error closing rs2: " + e.getMessage());
                            }
                        }
                    }

                    // Tanda tangan Penanggung Jawab Lab dan Petugas
                    try {
                        rs3 = koneksi.prepareStatement(
                                "select set_pjlab.kd_dokterlab,dokter.nm_dokter from set_pjlab inner join dokter on set_pjlab.kd_dokterlab=dokter.kd_dokter").executeQuery();
                        if (rs3.next()) {
                            htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                    + "<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>");

                            rs3.beforeFirst();
                            while (rs3.next()) {
                                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + rs3.getString("kd_dokterlab").replace(" ", "_"));
                                http.executeMethod(get);
                                htmlContent.append("<tr><td border='0' align='center'>Penangung Jawab Laboratorium <br><img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(rs3.getString("kd_dokterlab")).append(".png'/><br>").append(rs3.getString("nm_dokter")).append("</td>");

                                try {
                                    rs4 = koneksi.prepareStatement(
                                            "select periksa_lab.nip,petugas.nama from periksa_lab inner join petugas on periksa_lab.nip=petugas.nip where no_rawat='" + rs.getString("no_rawat") + "' ORDER BY periksa_lab.tgl_periksa, periksa_lab.jam limit 1").executeQuery();
                                    if (rs4.next()) {
                                        get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + rs4.getString("nip").replace(" ", "_"));
                                        http.executeMethod(get);
                                        htmlContent.append("<td border='0' align='center'>Petugas Laboratorium <br><img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(rs4.getString("nip")).append(".png'/><br>").append(rs4.getString("nama")).append("</td>");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Error getting petugas: " + e.getMessage());
                                } finally {
                                    if (rs4 != null) {
                                        try {
                                            rs4.close();
                                        } catch (SQLException e) {
                                            System.out.println("Error closing rs4: " + e.getMessage());
                                        }
                                    }
                                }
                                htmlContent.append("</tr>");
                            }
                            htmlContent.append("</table>");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting lab signature: " + e.getMessage());
                    } finally {
                        if (rs3 != null) {
                            try {
                                rs3.close();
                            } catch (SQLException e) {
                                System.out.println("Error closing rs3: " + e.getMessage());
                            }
                        }
                    }

                    htmlContent.append("</fieldset></div>");

                } catch (SQLException e) {
                    System.out.println("SQLException in menampilkanHasilLab: " + e.getMessage());
                    e.printStackTrace();
                    // Optional: Tambahkan pesan error ke HTML jika diperlukan
                    htmlContent.append("<p>Error: Tidak dapat menampilkan hasil laboratorium. Silakan coba lagi.</p>");
                } catch (Exception e) {
                    System.out.println("General Exception in menampilkanHasilLab: " + e.getMessage());
                    e.printStackTrace();
                    // Optional: Tambahkan pesan error ke HTML jika diperlukan
                    htmlContent.append("<p>Error: Terjadi kesalahan sistem. Silakan hubungi administrator.</p>");
                }
            }
        }
    }

    private void menampilkanHasilRadiologi(String norawat) {
        if (chkHasilRad.isSelected() == true) {
            try {
                if (!Sequel.cariIsi("SELECT COUNT(no_rawat) FROM periksa_radiologi WHERE no_rawat=?", rs.getString("no_rawat")).equals("0")) {
                    htmlContent.append(
                            ""
                            + "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                            + "<fieldset style='border: none; padding: 0; margin: 0;'>");

                    Copsurat();
                    htmlContent.append(
                            "<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>HASIL RADIOLOGI</div>");

                    // Tabel informasi pasien
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>No.Rekam Medis</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("no_rkm_medis")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Nama Pasien</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("nm_pasien")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Tgl. Lahir</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("tgl_lahir")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>No.Rawat</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("no_rawat")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>No.Registrasi</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("no_reg")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Tanggal Registrasi</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("tgl_registrasi")).append(" ").append(rs.getString("jam_reg")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Unit/Poliklinik</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("nm_poli")).append(polirujukan).append("</td></tr>"
                            + "<tr><td valign='top' width='20%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>Dokter Poli</td><td valign='top' width='1%' align='center' style='padding: 5px 4px; border: 1px solid #e0e0e0;'>:</td><td valign='top' width='79%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'>").append(rs.getString("nm_dokter")).append(dokterrujukan).append("</td></tr>");

                    // Informasi DPJP Ranap jika rawat inap
                    if (rs.getString("status_lanjut").equals("Ranap")) {
                        try {
                            rs3 = koneksi.prepareStatement(
                                    "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='" + rs.getString("no_rawat") + "'").executeQuery();
                            if (rs3.next()) {
                                htmlContent.append(
                                        "<tr>"
                                        + "<td valign='top' width='20%'>DPJP Ranap</td>"
                                        + "<td valign='top' width='1%' align='center'>:</td>"
                                        + "<td valign='top' width='79%'>");
                                rs3.beforeFirst();
                                urutdpjp = 1;
                                while (rs3.next()) {
                                    htmlContent.append(urutdpjp).append(". ").append(rs3.getString("nm_dokter")).append("&nbsp;&nbsp;");
                                    urutdpjp++;
                                }
                                htmlContent.append("</td></tr>");
                            }
                        } catch (SQLException e) {
                            System.out.println("Error getting DPJP Ranap: " + e.getMessage());
                        } finally {
                            if (rs3 != null) {
                                try {
                                    rs3.close();
                                } catch (SQLException e) {
                                    System.out.println("Error closing rs3: " + e.getMessage());
                                }
                            }
                        }
                    }

                    // Informasi tambahan pasien
                    htmlContent.append("<tr><td valign='top' width='20%'>Cara Bayar</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("png_jawab")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Penanggung Jawab</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("p_jawab")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Alamat P.J.</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("almt_pj")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Hubungan P.J.</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("hubunganpj")).append("</td></tr>"
                            + "<tr><td valign='top' width='20%'>Status</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("status_lanjut")).append("</td></tr>"
                            + "</table>");

                    // Pemeriksaan Radiologi
                    try {
                        rs2 = koneksi.prepareStatement(
                                "select periksa_radiologi.tgl_periksa,periksa_radiologi.jam,periksa_radiologi.kd_jenis_prw, "
                                + "jns_perawatan_radiologi.nm_perawatan,petugas.nama,periksa_radiologi.biaya,periksa_radiologi.dokter_perujuk,"
                                + "dokter.nm_dokter,concat("
                                + "if(periksa_radiologi.proyeksi<>'',concat('Proyeksi : ',periksa_radiologi.proyeksi,', '),''),"
                                + "if(periksa_radiologi.kV<>'',concat('kV : ',periksa_radiologi.kV,', '),''),"
                                + "if(periksa_radiologi.mAS<>'',concat('mAS : ',periksa_radiologi.mAS,', '),''),"
                                + "if(periksa_radiologi.FFD<>'',concat('FFD : ',periksa_radiologi.FFD,', '),''),"
                                + "if(periksa_radiologi.BSF<>'',concat('BSF : ',periksa_radiologi.BSF,', '),''),"
                                + "if(periksa_radiologi.inak<>'',concat('Inak : ',periksa_radiologi.inak,', '),''),"
                                + "if(periksa_radiologi.jml_penyinaran<>'',concat('Jml Penyinaran : ',periksa_radiologi.jml_penyinaran,', '),''),"
                                + "if(periksa_radiologi.dosis<>'',concat('Dosis Radiasi : ',periksa_radiologi.dosis),'')) as proyeksi "
                                + "from periksa_radiologi inner join jns_perawatan_radiologi on periksa_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                                + "inner join petugas on periksa_radiologi.nip=petugas.nip inner join dokter on periksa_radiologi.kd_dokter=dokter.kd_dokter "
                                + "where periksa_radiologi.no_rawat='" + rs.getString("no_rawat") + "' order by periksa_radiologi.tgl_periksa,periksa_radiologi.jam").executeQuery();
                        if (rs2.next()) {
                            htmlContent.append(
                                    "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                    + "<tr><td valign='top' colspan='5'><b>Pemeriksaan Radiologi</b></td></tr>"
                                    + "<tr align='center'>"
                                    + "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>"
                                    + "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>"
                                    + "<td valign='top' width='36%' bgcolor='#FFFAF8'>Nama Pemeriksaan</td>"
                                    + "<td valign='top' width='18%' bgcolor='#FFFAF8'>Dokter PJ</td>"
                                    + "<td valign='top' width='17%' bgcolor='#FFFAF8'>Petugas</td>"
                                    + "</tr>");
                            rs2.beforeFirst();
                            w = 1;
                            while (rs2.next()) {
                                String tglPeriksa = rs2.getString("tgl_periksa") != null ? rs2.getString("tgl_periksa") : "";
                                String jam = rs2.getString("jam") != null ? rs2.getString("jam") : "";
                                String nmPerawatan = rs2.getString("nm_perawatan") != null ? rs2.getString("nm_perawatan") : "";
                                String proyeksi = rs2.getString("proyeksi") != null ? rs2.getString("proyeksi") : "";
                                String nmDokter = rs2.getString("nm_dokter") != null ? rs2.getString("nm_dokter") : "";
                                String nama = rs2.getString("nama") != null ? rs2.getString("nama") : "";

                                htmlContent.append("<tr align='center'><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(tglPeriksa).append(" ").append(jam).append("</td><td valign='top'>").append(nmPerawatan).append("<br>").append(proyeksi).append("</td><td valign='top'>").append(nmDokter).append("</td><td valign='top'>").append(nama).append("</td></tr>");
                                w++;
                            }
                            htmlContent.append("</table>");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error getting periksa radiologi: " + e.getMessage());
                    } finally {
                        if (rs2 != null) {
                            try {
                                rs2.close();
                            } catch (SQLException e) {
                                System.out.println("Error closing rs2: " + e.getMessage());
                            }
                        }
                    }

                    // Hasil pemeriksaan radiologi
                    try {
                        rs2 = koneksi.prepareStatement(
                                "select tgl_periksa,jam, hasil from hasil_radiologi where no_rawat='" + rs.getString("no_rawat") + "' order by tgl_periksa,jam").executeQuery();
                        if (rs2.next()) {
                            htmlContent.append(
                                    "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                    + "<tr><td valign='top' colspan='3'>Bacaan/Hasil Radiologi</td></tr>"
                                    + "<tr align='center'>"
                                    + "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>"
                                    + "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>"
                                    + "<td valign='top' width='81%' bgcolor='#FFFAF8'>Hasil Pemeriksaan</td>"
                                    + "</tr>");
                            rs2.beforeFirst();
                            w = 1;
                            while (rs2.next()) {
                                String tglPeriksa = rs2.getString("tgl_periksa") != null ? rs2.getString("tgl_periksa") : "";
                                String jam = rs2.getString("jam") != null ? rs2.getString("jam") : "";
                                String hasil = rs2.getString("hasil") != null ? rs2.getString("hasil") : "";

                                htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(tglPeriksa).append(" ").append(jam).append("</td><td valign='top'>").append(hasil.replaceAll("(\r\n|\r|\n|\n\r)", "<br>")).append("</td></tr>");
                                w++;
                            }
                            htmlContent.append("</table>");
                        }

                        // Tanda tangan Penanggung Jawab Radiologi dan Petugas
                        try {
                            rs3 = koneksi.prepareStatement(
                                    "select set_pjlab.kd_dokterrad,dokter.nm_dokter from set_pjlab inner join dokter on set_pjlab.kd_dokterrad=dokter.kd_dokter").executeQuery();
                            if (rs3.next()) {
                                htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                        + "<tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr>");

                                rs3.beforeFirst();
                                while (rs3.next()) {
                                    get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + rs3.getString("kd_dokterrad").replace(" ", "_"));
                                    http.executeMethod(get);
                                    htmlContent.append("<tr><td border='0' align='center'>Penangung Jawab Radiologi <br><img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(rs3.getString("kd_dokterrad")).append(".png'/><br>").append(rs3.getString("nm_dokter")).append("</td>");

                                    try {
                                        rs4 = koneksi.prepareStatement(
                                                "select periksa_radiologi.nip,petugas.nama from periksa_radiologi inner join petugas on periksa_radiologi.nip=petugas.nip where no_rawat='" + rs.getString("no_rawat") + "' ORDER BY periksa_radiologi.tgl_periksa,periksa_radiologi.jam limit 1").executeQuery();
                                        if (rs4.next()) {
                                            get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + rs4.getString("nip").replace(" ", "_"));
                                            http.executeMethod(get);
                                            htmlContent.append("<td border='0' align='center'>Petugas Radiologi <br><img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(rs4.getString("nip")).append(".png'/><br>").append(rs4.getString("nama")).append("</td>");
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Error getting petugas radiologi: " + e.getMessage());
                                    } finally {
                                        if (rs4 != null) {
                                            try {
                                                rs4.close();
                                            } catch (SQLException e) {
                                                System.out.println("Error closing rs4: " + e.getMessage());
                                            }
                                        }
                                    }
                                    htmlContent.append("</tr>");
                                }
                                htmlContent.append("</table>");
                            }
                        } catch (SQLException e) {
                            System.out.println("Error getting radiologi signature: " + e.getMessage());
                        } finally {
                            if (rs3 != null) {
                                try {
                                    rs3.close();
                                } catch (SQLException e) {
                                    System.out.println("Error closing rs3: " + e.getMessage());
                                }
                            }
                        }

                    } catch (SQLException e) {
                        System.out.println("Error getting hasil radiologi: " + e.getMessage());
                    } finally {
                        if (rs2 != null) {
                            try {
                                rs2.close();
                            } catch (SQLException e) {
                                System.out.println("Error closing rs2: " + e.getMessage());
                            }
                        }
                    }

                    // Gambar pemeriksaan radiologi
                    if (chkHasilRad.isSelected() == true) {
                        try {
                            rs2 = koneksi.prepareStatement(
                                    "select tgl_periksa,jam, lokasi_gambar from gambar_radiologi where no_rawat='" + rs.getString("no_rawat") + "' order by tgl_periksa,jam").executeQuery();
                            if (rs2.next()) {
                                htmlContent.append(
                                        "<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                                        + "<tr><td valign='top' colspan='3'>Gambar Radiologi</td></tr>"
                                        + "<tr align='center'>"
                                        + "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>"
                                        + "<td valign='top' width='10%' bgcolor='#FFFAF8'>Tanggal</td>"
                                        + "<td valign='top' width='86%' bgcolor='#FFFAF8'>Gambar Radiologi</td>"
                                        + "</tr>");
                                rs2.beforeFirst();
                                w = 1;
                                while (rs2.next()) {
                                    String tglPeriksa = rs2.getString("tgl_periksa") != null ? rs2.getString("tgl_periksa") : "";
                                    String jam = rs2.getString("jam") != null ? rs2.getString("jam") : "";
                                    String lokasiGambar = rs2.getString("lokasi_gambar") != null ? rs2.getString("lokasi_gambar") : "";

                                    htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td><td valign='top'>").append(tglPeriksa).append(" ").append(jam).append("</td><td valign='top' align='center'><a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/radiologi/").append(lokasiGambar).append("'><img alt='Gambar Radiologi' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/radiologi/").append(lokasiGambar).append("' width='88%' height='auto'/></a></td></tr>");
                                    w++;
                                }
                                htmlContent.append("</table>");
                            }
                        } catch (SQLException e) {
                            System.out.println("Error getting gambar radiologi: " + e.getMessage());
                        } finally {
                            if (rs2 != null) {
                                try {
                                    rs2.close();
                                } catch (SQLException e) {
                                    System.out.println("Error closing rs2: " + e.getMessage());
                                }
                            }
                        }
                    }
                    htmlContent.append("</fieldset></div>");
                }
            } catch (SQLException e) {
                System.out.println("SQLException in menampilkanHasilRadiologi: " + e.getMessage());
                e.printStackTrace();
                // Optional: Tambahkan pesan error ke HTML jika diperlukan
                htmlContent.append("<p>Error: Tidak dapat menampilkan hasil radiologi. Silakan coba lagi.</p>");
            } catch (Exception e) {
                System.out.println("General Exception in menampilkanHasilRadiologi: " + e.getMessage());
                e.printStackTrace();
                // Optional: Tambahkan pesan error ke HTML jika diperlukan
                htmlContent.append("<p>Error: Terjadi kesalahan sistem. Silakan hubungi administrator.</p>");
            }
        }
    }

    private void menampilkanHasilPemeriksaanUSG(String norawat) {
        if (!chkHasilUSG.isSelected()) {
            return;
        }

        String sql = "select hasil_pemeriksaan_usg.kd_dokter,dokter.nm_dokter,hasil_pemeriksaan_usg.diagnosa_klinis,"
                + "hasil_pemeriksaan_usg.kiriman_dari,hasil_pemeriksaan_usg.hta,hasil_pemeriksaan_usg.kantong_gestasi,"
                + "hasil_pemeriksaan_usg.ukuran_bokongkepala,hasil_pemeriksaan_usg.jenis_prestasi,"
                + "hasil_pemeriksaan_usg.diameter_biparietal,hasil_pemeriksaan_usg.panjang_femur,"
                + "hasil_pemeriksaan_usg.lingkar_abdomen,hasil_pemeriksaan_usg.tafsiran_berat_janin,"
                + "hasil_pemeriksaan_usg.usia_kehamilan,hasil_pemeriksaan_usg.plasenta_berimplatansi,"
                + "hasil_pemeriksaan_usg.derajat_maturitas,hasil_pemeriksaan_usg.jumlah_air_ketuban,"
                + "hasil_pemeriksaan_usg.indek_cairan_ketuban,hasil_pemeriksaan_usg.kelainan_kongenital,"
                + "hasil_pemeriksaan_usg.peluang_sex,hasil_pemeriksaan_usg.kesimpulan,hasil_pemeriksaan_usg.tanggal "
                + "from hasil_pemeriksaan_usg inner join dokter on hasil_pemeriksaan_usg.kd_dokter=dokter.kd_dokter "
                + "where hasil_pemeriksaan_usg.no_rawat=? order by hasil_pemeriksaan_usg.tanggal";

        try ( PreparedStatement psUSG = koneksi.prepareStatement(sql)) {
            psUSG.setString(1, norawat);
            try ( ResultSet rsUSG = psUSG.executeQuery()) {
                while (rsUSG.next()) {
                    htmlContent.append(
                            "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                            + "<fieldset style='border: none; padding: 0; margin: 0;'>");

                    Copsurat();
                    htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 15px;'>HASIL PEMERIKSAAN ULTRASONOGRAFI (USG) KEBIDANAN</div>")
                            .append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");

                    appendInfoRow("No. RM", rs.getString("no_rkm_medis"), "Tanggal Lahir", rs.getString("tgl_lahir"));
                    appendSingleRow("Nama Pasien", rs.getString("nm_pasien"));
                    appendSingleRow("Kiriman Dari", rsUSG.getString("kiriman_dari"));
                    appendSingleRow("Diagnosa Klinis", rsUSG.getString("diagnosa_klinis"));
                    appendSingleRow("HTA", rsUSG.getString("hta"));
                    appendSingleRow("Jenis Prestasi", rsUSG.getString("jenis_prestasi"));
                    appendSingleRow("Kantong Gestasi (GS) Ukuran", rsUSG.getString("kantong_gestasi"));
                    appendSingleRow("Ukuran Bokong - Kepala (CRL)", rsUSG.getString("ukuran_bokongkepala"));
                    appendSingleRow("Diameter Biparietal (DBP)", rsUSG.getString("diameter_biparietal"));
                    appendSingleRow("Panjang Femur (FL)", rsUSG.getString("panjang_femur"));
                    appendSingleRow("Lingkar Abdomen (AC)", rsUSG.getString("lingkar_abdomen"));
                    appendSingleRow("Tafsiran Berat Janin (TBJ)", rsUSG.getString("tafsiran_berat_janin"));
                    appendSingleRow("Usia Kehamilan Sesuai", rsUSG.getString("usia_kehamilan"));
                    appendSingleRow("Plasenta Berimplantasi Di", rsUSG.getString("plasenta_berimplatansi"));
                    appendSingleRow("Derajat Maturitas Plasenta", rsUSG.getString("derajat_maturitas"));
                    appendSingleRow("Jumlah Air Ketuban", rsUSG.getString("jumlah_air_ketuban"));
                    appendSingleRow("Indeks Cairan Ketuban (ICK)", rsUSG.getString("indek_cairan_ketuban"));
                    appendSingleRow("Kelainan Kongenital Mayor", rsUSG.getString("kelainan_kongenital"));
                    appendSingleRow("Peluang Sex", rsUSG.getString("peluang_sex"));
                    appendSingleRow("Kesimpulan", rsUSG.getString("kesimpulan"));

                    String photoUSG = Sequel.cariIsi("select hasil_pemeriksaan_usg_gambar.photo from hasil_pemeriksaan_usg_gambar where hasil_pemeriksaan_usg_gambar.no_rawat=?", norawat);
                    if (photoUSG != null && !photoUSG.trim().equals("")) {
                        htmlContent.append("<tr>")
                                .append("<td width='24%' style='padding: 6px 8px; border: 1px solid #e0e0e0; background-color: #f5f5f5; font-weight: bold;'>Photo USG</td>")
                                .append("<td colspan='3' width='76%' align='center' style='padding: 8px; border: 1px solid #e0e0e0;'>")
                                .append("<a href='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusg/").append(urlEncode(photoUSG)).append("'>")
                                .append("<img alt='Gambar USG' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/hasilpemeriksaanusg/").append(urlEncode(photoUSG)).append("' style='max-width: 88%; height: auto;'/>")
                                .append("</a></td></tr>");
                    }

                    appendHasilUSGSignature(rsUSG);
                    htmlContent.append("</table></fieldset></div>");
                    appendPageBreakAfterDocument();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Hasil Pemeriksaan USG : " + e);
        }
    }

    private void appendHasilUSGSignature(ResultSet rsUSG) throws SQLException {
        String kodeDokter = rsUSG.getString("kd_dokter") == null ? "" : rsUSG.getString("kd_dokter").trim().replace(" ", "_");
        String namaDokter = rsUSG.getString("nm_dokter") == null ? "" : rsUSG.getString("nm_dokter");
        String tanggal = rsUSG.getString("tanggal") == null ? "" : rsUSG.getString("tanggal");

        try {
            if (!kodeDokter.equals("")) {
                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + urlEncode(kodeDokter));
                http.executeMethod(get);
            }
        } catch (Exception e) {
            System.out.println("Error generating QR dokter Hasil USG: " + e.getMessage());
        }

        htmlContent.append("<tr>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 6px 8px; border: 1px solid #d9d9d9;'>Tanggal dan Jam</td>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 6px 8px; border: 1px solid #d9d9d9;'>Nama Dokter dan Tanda Tangan</td>")
                .append("</tr>")
                .append("<tr>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 18px 8px; border: 1px solid #d9d9d9;'>").append(formatMultiline(tanggal)).append(tanggal.equals("") ? "" : " WIB").append("</td>")
                .append("<td colspan='2' width='50%' align='center' style='padding: 8px; border: 1px solid #d9d9d9;'>");

        if (!kodeDokter.equals("")) {
            htmlContent.append("<img width='90' height='90' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(urlEncode(kodeDokter)).append(".png'/><br>");
        }

        htmlContent.append(escapeHtml(namaDokter)).append("</td></tr>");
    }

    // Method untuk menampilkan resep saja
    private void menampilkanResep(String norawat) {
        try {
            if (chkResep.isSelected() == true) {
                PreparedStatement psHeader = null;
                ResultSet rsHeader = null;
                PreparedStatement psResep = null;

                try {
                    // ================================================================
                    // 1) Ambil data header (SEP, berat, alergi, poli, umur, dll)
                    //    HANYA SEKALI per no_rawat, TIDAK ikut di-loop bareng resep.
                    // ================================================================
                    String noSep = "-";
                    String berat = null;
                    String alergi = null;
                    String poli = "-";
                    String umurdaftar = null;
                    String sttsumur = "";
                    String pJawab = "-";

                    psHeader = koneksi.prepareStatement(
                            "SELECT bs.no_sep, pr.berat, pr.alergi, bs.nmpolitujuan, "
                            + "rp.umurdaftar, rp.sttsumur, rp.p_jawab "
                            + "FROM reg_periksa rp "
                            + "LEFT JOIN bridging_sep bs ON rp.no_rawat = bs.no_rawat "
                            + "LEFT JOIN pemeriksaan_ralan pr ON rp.no_rawat = pr.no_rawat "
                            + "WHERE rp.no_rawat = ? "
                            + "LIMIT 1"
                    );
                    psHeader.setString(1, norawat);
                    rsHeader = psHeader.executeQuery();
                    if (rsHeader.next()) {
                        noSep = rsHeader.getString("no_sep");
                        berat = rsHeader.getString("berat");
                        alergi = rsHeader.getString("alergi");
                        poli = rsHeader.getString("nmpolitujuan");
                        umurdaftar = rsHeader.getString("umurdaftar");
                        sttsumur = rsHeader.getString("sttsumur");
                        pJawab = rsHeader.getString("p_jawab");
                    }

                    psResep = koneksi.prepareStatement(
                            "SELECT DISTINCT ro.no_resep, ro.tgl_perawatan, ro.jam, "
                            + "d.nm_dokter, d.kd_dokter "
                            + "FROM resep_obat ro "
                            + "INNER JOIN dokter d ON ro.kd_dokter = d.kd_dokter "
                            + "WHERE ro.no_rawat = ? "
                            + "ORDER BY ro.tgl_perawatan, ro.jam"
                    );
                    psResep.setString(1, norawat);
                    rs2 = psResep.executeQuery();

                    if (rs2.next()) {
                        htmlContent.append(
                                "<p class='pagebreak'></p>" // Page break di awal
                        );

                        Copsurat();

                        // Informasi pasien
                        htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>");
                        htmlContent.append("<tr>");

                        // Kolom kiri
                        htmlContent.append("<td width='50%' valign='top' style='padding: 0; border-right: 1px solid #d9d9d9;'>");
                        htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse;'>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td width='38%' style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>Nama Pasien</td><td width='5%' style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("nm_pasien")).append("</td></tr>");
                        htmlContent.append("<tr><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>No. RM</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("no_rkm_medis")).append("</td></tr>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>No. Rawat</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("no_rawat")).append("</td></tr>");
                        htmlContent.append("<tr><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>Tanggal Lahir</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs.getString("tgl_lahir")).append("</td></tr>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>Umur</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(umurdaftar != null ? umurdaftar + " " + sttsumur : "-").append("</td></tr>");
                        htmlContent.append("<tr><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>Jenis Pasien</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(pJawab != null ? pJawab : "-").append("</td></tr>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px;'>Pemberi Resep</td><td style='padding: 6px 4px;'>:</td><td style='padding: 6px 8px;'>dr. ").append(rs2.getString("nm_dokter")).append("</td></tr>");
                        htmlContent.append("</table>");
                        htmlContent.append("</td>");

                        // Kolom kanan
                        htmlContent.append("<td width='50%' valign='top'>");
                        htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse;'>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td width='38%' style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>No. Resep</td><td width='5%' style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(rs2.getString("no_resep")).append("</td></tr>");
                        htmlContent.append("<tr><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>Alergi</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(alergi != null ? alergi : "-").append("</td></tr>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>BB</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(berat != null ? berat + " Kg" : "-").append("</td></tr>");
                        htmlContent.append("<tr><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>Poli</td><td style='padding: 6px 4px; border-bottom: 1px solid #e0e0e0;'>:</td><td style='padding: 6px 8px; border-bottom: 1px solid #e0e0e0;'>").append(poli != null ? poli : "-").append("</td></tr>");
                        htmlContent.append("<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px;'>No SEP</td><td style='padding: 6px 4px;'>:</td><td style='padding: 6px 8px;'>").append(noSep != null ? noSep : "-").append("</td></tr>");
                        htmlContent.append("</table>");
                        htmlContent.append("</td>");

                        htmlContent.append("</tr>");
                        htmlContent.append("</table>");

                        htmlContent.append("<hr>");
                        htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>RESEP</div>");

                        rs2.beforeFirst();
                        while (rs2.next()) {
                            String currentNoResep = rs2.getString("no_resep");

                            // Tampilkan obat
                            htmlContent.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; margin-top: 12px; font-size: 11px;'>");
                            htmlContent.append("<tr style='background-color: #e5e5e5; font-weight: bold; border-bottom: 1px solid #d9d9d9;'>")
                                    .append("<td width='3%' style='padding: 8px 6px; border-right: 1px solid #d9d9d9; text-align: center;'>R/</td>")
                                    .append("<td width='67%' style='padding: 8px 8px; border-right: 1px solid #d9d9d9;'>Nama Obat</td>")
                                    .append("<td width='30%' align='right' style='padding: 8px 8px;'>Jumlah</td>")
                                    .append("</tr>");

                            int noUrut = 1;
                            int obatIndex = 0;

                            // Query untuk obat non-racikan
                            try {
                                PreparedStatement psObat = koneksi.prepareStatement(
                                        "SELECT databarang.nama_brng, aturan_pakai.aturan, detail_pemberian_obat.jml, kodesatuan.satuan "
                                        + "FROM resep_obat "
                                        + "INNER JOIN aturan_pakai ON resep_obat.no_rawat = aturan_pakai.no_rawat "
                                        + "    AND resep_obat.tgl_perawatan = aturan_pakai.tgl_perawatan "
                                        + "    AND resep_obat.jam = aturan_pakai.jam "
                                        + "INNER JOIN databarang ON databarang.kode_brng = aturan_pakai.kode_brng "
                                        + "INNER JOIN detail_pemberian_obat ON resep_obat.no_rawat = detail_pemberian_obat.no_rawat "
                                        + "    AND resep_obat.tgl_perawatan = detail_pemberian_obat.tgl_perawatan "
                                        + "    AND resep_obat.jam = detail_pemberian_obat.jam "
                                        + "    AND detail_pemberian_obat.kode_brng = databarang.kode_brng "
                                        + "INNER JOIN kodesatuan ON kodesatuan.kode_sat = databarang.kode_sat "
                                        + "WHERE resep_obat.no_resep = ? AND aturan_pakai.aturan <> '' "
                                        + "ORDER BY databarang.nama_brng"
                                );
                                psObat.setString(1, currentNoResep);
                                ResultSet rsObat = psObat.executeQuery();
                                while (rsObat.next()) {
                                    String rowBg = (obatIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                                    htmlContent.append("<tr style='background-color: ").append(rowBg).append("; border-bottom: 1px solid #e0e0e0;'>");
                                    htmlContent.append("<td width='3%' valign='top' style='padding: 6px 8px; border-right: 1px solid #e0e0e0;'>R/</td>");
                                    htmlContent.append("<td width='67%' valign='top' style='padding: 6px 8px; border-right: 1px solid #e0e0e0;'>").append(rsObat.getString("nama_brng"));
                                    htmlContent.append("<br>").append(rsObat.getString("aturan")).append("</td>");
                                    htmlContent.append("<td width='30%' valign='top' align='right' style='padding: 6px 8px;'>").append(rsObat.getString("jml")).append(" ").append(rsObat.getString("satuan")).append("</td>");
                                    htmlContent.append("</tr>");
                                    noUrut++;
                                    obatIndex++;
                                }
                                rsObat.close();
                                psObat.close();
                            } catch (Exception e) {
                                System.out.println("Error Query Obat Non-Racikan: " + e);
                            }

                            // Query untuk obat racikan
                            try {
                                PreparedStatement psRacikan = koneksi.prepareStatement(
                                        "SELECT obat_racikan.no_racik, obat_racikan.nama_racik, obat_racikan.aturan_pakai, "
                                        + "obat_racikan.jml_dr, metode_racik.nm_racik, "
                                        + "obat_racikan.tgl_perawatan, obat_racikan.jam "
                                        + "FROM resep_obat "
                                        + "INNER JOIN obat_racikan ON resep_obat.no_rawat = obat_racikan.no_rawat "
                                        + "    AND resep_obat.tgl_perawatan = obat_racikan.tgl_perawatan "
                                        + "    AND resep_obat.jam = obat_racikan.jam "
                                        + "INNER JOIN metode_racik ON obat_racikan.kd_racik = metode_racik.kd_racik "
                                        + "WHERE resep_obat.no_resep = ? "
                                        + "ORDER BY obat_racikan.no_racik"
                                );
                                psRacikan.setString(1, currentNoResep);
                                ResultSet rsRacikan = psRacikan.executeQuery();

                                while (rsRacikan.next()) {
                                    String rincianObat = "";
                                    // Get detail obat racikan
                                    try {
                                        PreparedStatement psDetailRacikan = koneksi.prepareStatement(
                                                "SELECT databarang.nama_brng, detail_pemberian_obat.jml "
                                                + "FROM detail_pemberian_obat "
                                                + "INNER JOIN databarang ON detail_pemberian_obat.kode_brng = databarang.kode_brng "
                                                + "INNER JOIN detail_obat_racikan ON detail_pemberian_obat.kode_brng = detail_obat_racikan.kode_brng "
                                                + "    AND detail_pemberian_obat.tgl_perawatan = detail_obat_racikan.tgl_perawatan "
                                                + "    AND detail_pemberian_obat.jam = detail_obat_racikan.jam "
                                                + "    AND detail_pemberian_obat.no_rawat = detail_obat_racikan.no_rawat "
                                                + "WHERE detail_pemberian_obat.tgl_perawatan = ? "
                                                + "    AND detail_pemberian_obat.jam = ? "
                                                + "    AND detail_pemberian_obat.no_rawat = ? "
                                                + "    AND detail_obat_racikan.no_racik = ? "
                                                + "ORDER BY databarang.kode_brng"
                                        );
                                        psDetailRacikan.setString(1, rsRacikan.getString("tgl_perawatan"));
                                        psDetailRacikan.setString(2, rsRacikan.getString("jam"));
                                        psDetailRacikan.setString(3, norawat);
                                        psDetailRacikan.setString(4, rsRacikan.getString("no_racik"));
                                        ResultSet rsDetailRacikan = psDetailRacikan.executeQuery();

                                        while (rsDetailRacikan.next()) {
                                            rincianObat += rsDetailRacikan.getString("nama_brng") + " " + rsDetailRacikan.getString("jml") + ", ";
                                        }

                                        if (rincianObat.length() > 0) {
                                            rincianObat = rincianObat.substring(0, rincianObat.length() - 2); // Remove last comma
                                        }

                                        rsDetailRacikan.close();
                                        psDetailRacikan.close();
                                    } catch (Exception e) {
                                        System.out.println("Error Detail Racikan: " + e);
                                    }

                                    String rowBg = (obatIndex % 2 == 0) ? "#ffffff" : "#f5f5f5";
                                    htmlContent.append("<tr style='background-color: ").append(rowBg).append("; border-bottom: 1px solid #e0e0e0;'>");
                                    htmlContent.append("<td width='3%' valign='top' style='padding: 6px 8px; border-right: 1px solid #e0e0e0;'>R/</td>");
                                    htmlContent.append("<td width='67%' valign='top' style='padding: 6px 8px; border-right: 1px solid #e0e0e0;'>").append(rsRacikan.getString("nama_racik"));
                                    if (!rincianObat.isEmpty()) {
                                        htmlContent.append(" (").append(rincianObat).append(")");
                                    }
                                    htmlContent.append("<br>").append(rsRacikan.getString("aturan_pakai")).append("</td>");
                                    htmlContent.append("<td width='30%' valign='top' align='right' style='padding: 6px 8px;'>").append(rsRacikan.getString("jml_dr")).append(" ").append(rsRacikan.getString("nm_racik")).append("</td>");
                                    htmlContent.append("</tr>");
                                    noUrut++;
                                    obatIndex++;
                                }
                                rsRacikan.close();
                                psRacikan.close();
                            } catch (Exception e) {
                                System.out.println("Error Query Racikan: " + e);
                            }

                            htmlContent.append("</table>");

                            // Tambahkan tanggal dan tanda tangan dokter
                            htmlContent.append("<br><br>");
                            htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0'>");
                            htmlContent.append("<tr>");
                            htmlContent.append("<td width='50%'></td>");
                            htmlContent.append("<td width='50%' align='center'>");
                            htmlContent.append("Bogor, ").append(rs2.getString("tgl_perawatan")).append("<br><br>");

                            // Generate QR code untuk dokter
                            get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + rs2.getString("kd_dokter").replace(" ", "_"));
                            http.executeMethod(get);

                            htmlContent.append("<img width='90' height='90' src='http://")
                                    .append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/")
                                    .append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(rs2.getString("kd_dokter")).append(".png'/><br>")
                                    .append(rs2.getString("nm_dokter"));
                            htmlContent.append("</td>");
                            htmlContent.append("</tr>");
                            htmlContent.append("</table>");
                        }
                        htmlContent.append("</fieldset>"); // Page break dipindah ke akhir
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi Resep : " + e);
                } finally {
                    if (rsHeader != null) {
                        try {
                            rsHeader.close();
                        } catch (Exception e) {
                            System.out.println("Error closing rsHeader: " + e);
                        }
                    }
                    if (psHeader != null) {
                        try {
                            psHeader.close();
                        } catch (Exception e) {
                            System.out.println("Error closing psHeader: " + e);
                        }
                    }
                    if (rs2 != null) {
                        try {
                            rs2.close();
                        } catch (Exception e) {
                            System.out.println("Error closing rs2: " + e);
                        }
                    }
                    if (psResep != null) {
                        try {
                            psResep.close();
                        } catch (Exception e) {
                            System.out.println("Error closing psResep: " + e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif Resep : " + e);
        }
    }

    private void menampilkanBilling(String norawat) {
        if (chkBilling.isSelected() == true) {
            try {
                rs2 = koneksi.prepareStatement(
                        "select billing.no,billing.nm_perawatan,billing.pemisah,if(billing.biaya=0,'',billing.biaya),if(billing.jumlah=0,'',billing.jumlah),if(billing.tambahan=0,'',billing.tambahan),if(billing.totalbiaya=0,'',billing.totalbiaya),billing.totalbiaya from billing where billing.no_rawat='" + norawat + "'").executeQuery();
                if (rs2.next()) {
                    rs3 = koneksi.prepareStatement(
                            "select setting.nama_instansi,setting.alamat_instansi,setting.kabupaten,setting.propinsi,setting.kontak,setting.email,setting.logo from setting").executeQuery();
                    if (rs3.next()) {
                        htmlContent.append(
                                ""
                                + "<div class='berkas-page' style='page-break-before: always; break-before: page; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                                + "<fieldset style='border: none; padding: 0; margin: 0;'>");
                        Copsurat();
                    }

                    htmlContent.append(
                            "<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>BILLING</div>"
                            + "<table width='100%' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse; font-size: 11px; border: 1px solid #d9d9d9;'>");

                    total = 0;
                    do {
                        total += rs2.getDouble(8);
                        htmlContent.append("<tr style='background-color: #fff;'>")
                                .append("<td width='18%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append(rs2.getString(1)).append("</font></td>")
                                .append("<td width='40%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append(rs2.getString(2)).append("</font></td>")
                                .append("<td width='2%' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append(rs2.getString(3)).append("</font></td>")
                                .append("<td width='10%' align='right' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append((rs2.getString(4).equals("") ? "" : Valid.SetAngka(rs2.getDouble(4)))).append("</font></td>")
                                .append("<td width='5%' align='right' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append(rs2.getString(5)).append("</font></td>")
                                .append("<td width='10%' align='right' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append(rs2.getString(6)).append("</font></td>")
                                .append("<td width='15%' align='right' style='padding: 5px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma' size='2'>").append((rs2.getString(7).equals("") ? "" : Valid.SetAngka(rs2.getDouble(7)))).append("</font></td>")
                                .append("</tr>");
                    } while (rs2.next());

                    htmlContent.append("<tr style='background-color: #f5f5f5;'>")
                            .append("<td width='18%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'><b>TOTAL BIAYA</b></font></td>")
                            .append("<td width='40%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'><b>:</b></font></td>")
                            .append("<td width='2%' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'></font></td>")
                            .append("<td width='10%' align='right' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'></font></td>")
                            .append("<td width='5%' align='right' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'></font></td>")
                            .append("<td width='10%' align='right' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'></font></td>")
                            .append("<td width='15%' align='right' style='padding: 6px 8px; border: 1px solid #e0e0e0;'><font color='111111' face='Tahoma'><b>Rp. ").append(Valid.SetAngka(total)).append("</b></font></td>")
                            .append("</tr>");

                    htmlContent.append("<tr>")
                            .append("<td colspan='7' style='padding: 12px 0 0 0;'>")
                            .append("<table width='100%' bgcolor='#ffffff' border='0' cellspacing='0' cellpadding='0'>")
                            .append("<tr>")
                            .append("<td width='55%' align='center'>&nbsp;</td>")
                            .append("<td width='45%' align='center' style='padding-right: 18px;'>")
                            .append("</td>")
                            .append("</tr>")
                            .append("</table>")
                            .append("</td>")
                            .append("</tr>");

                    try {
                        get = null;
                        pskoders = koneksi.prepareStatement("select kode_ppk from setting");
                        try {
                            rskode = pskoders.executeQuery();
                            if (rskode.next()) {
                                petugasbilling = cariPetugasClosingBilling(norawat);
                                if ((petugasbilling == null || petugasbilling.trim().isEmpty()) && !rskode.getString("kode_ppk").equals("0117R073")) {
                                    inputString = "" + Sequel.cariIsi("SELECT keterangan FROM `jurnal` WHERE no_bukti=? and keterangan LIKE 'PIUTANG PASIEN%'", norawat);
                                    String[] parts = inputString.split(" ");
                                    for (String part : parts) {
                                        if (part.matches("\\d{4}\\.\\d{2}\\.\\d{2}\\.\\d{2}")) {
                                            System.out.println("Petugas : " + part);
                                            petugasbilling = part;
                                            break;
                                        }
                                    }
                                }
                                if ((petugasbilling == null || petugasbilling.trim().isEmpty()) && !rskode.getString("kode_ppk").equals("0117R073")) {
                                    petugasbilling = akses.getkode();
                                }
                                String qrPetugasTempUrl = "";
                                String namaPetugasKasir = "Petugas Closing Tidak Ditemukan";
                                if (petugasbilling != null && !petugasbilling.trim().isEmpty()) {
                                    String kodePetugasBilling = petugasbilling.trim().replace(" ", "_");
                                    String safePetugasBilling = urlEncode(kodePetugasBilling);
                                    String qrPetugasGenerateUrl = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcodepetugas.php?kodepetugas=" + safePetugasBilling;
                                    long cacheBuster = System.currentTimeMillis();
                                    qrPetugasTempUrl = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + safePetugasBilling + ".png?t=" + cacheBuster;
                                    get = new GetMethod(qrPetugasGenerateUrl);
                                    namaPetugasKasir = Sequel.cariIsi("select petugas.nama FROM petugas WHERE nip=?", petugasbilling);
                                    if (namaPetugasKasir == null || namaPetugasKasir.trim().isEmpty()) {
                                        namaPetugasKasir = petugasbilling;
                                    }
                                }
                                htmlContent.append("<tr>")
                                        .append("<td colspan='7' style='padding: 8px 0 0 0;'>")
                                        .append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>")
                                        .append("<tr>")
                                        .append("<td width='55%'>&nbsp;</td>")
                                        .append("<td width='45%' align='center' style='padding-right: 18px;'>")
                                        .append("<div style='font-size: 12px; font-weight: bold; margin-bottom: 8px; text-align: center;'>Kasir</div>")
                                        .append(qrPetugasTempUrl.equals("") ? "" : "<img width='120' src='" + qrPetugasTempUrl + "' style='display: block; margin: 0 auto; border: 1px solid #d9d9d9; background: #fff; padding: 2px;' onerror=\"this.style.display='none';\"/><br>")
                                        .append("<div style='font-size: 11px; text-align: center; margin-top: 4px;'>( " + namaPetugasKasir + " )</div>")
                                        .append("</td>")
                                        .append("</tr>")
                                        .append("</table>")
                                        .append("</td>")
                                        .append("</tr>");
                            }
                        } catch (Exception e) {
                            System.out.println("Notif : " + e);
                        } finally {
                            if (rskode != null) {
                                try {
                                    rskode.close();
                                } catch (SQLException e) {
                                    System.out.println("Error closing rskode: " + e.getMessage());
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                    }

                    if (get != null) {
                        try {
                            http.executeMethod(get);
                        } catch (Exception e) {
                            System.out.println("Error generating QR petugas billing: " + e.getMessage());
                        }
                    }

                    htmlContent.append("</table>")
                            .append("</fieldset>")
                            .append("</div>");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs2 != null) {
                    try {
                        rs2.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing rs2: " + e.getMessage());
                    }
                }
                if (rs3 != null) {
                    try {
                        rs3.close();
                    } catch (SQLException e) {
                        System.out.println("Error closing rs3: " + e.getMessage());
                    }
                }
            }
        }
    }

    private String cariPetugasClosingBilling(String norawat) {
        String sql = "select trackersql.usere from trackersql "
                + "where replace(lower(trackersql.sqle),'|','') like ? "
                + "and (replace(lower(trackersql.sqle),'|','') like ? "
                + "or replace(lower(trackersql.sqle),'|','') like ?) "
                + "order by trackersql.tanggal desc limit 1";

        try ( PreparedStatement psClosing = koneksi.prepareStatement(sql)) {
            psClosing.setString(1, "%" + norawat.toLowerCase() + "%");
            psClosing.setString(2, "%update reg_periksa set status_bayar=%sudah bayar%");
            psClosing.setString(3, "%update reg_periksa set stts=%sudah%");
            try ( ResultSet rsClosing = psClosing.executeQuery()) {
                if (rsClosing.next()) {
                    return rsClosing.getString("usere");
                }
            }
        } catch (Exception e) {
            System.out.println("Notif petugas closing billing : " + e);
        }

        sql = "select trackersql.usere from trackersql "
                + "where replace(lower(trackersql.sqle),'|','') like ? "
                + "and (replace(lower(trackersql.sqle),'|','') like ? "
                + "or replace(lower(trackersql.sqle),'|','') like ?) "
                + "order by trackersql.tanggal desc limit 1";
        try ( PreparedStatement psClosing = koneksi.prepareStatement(sql)) {
            psClosing.setString(1, "%" + norawat.toLowerCase() + "%");
            psClosing.setString(2, "%insert into tagihan_sadewa%");
            psClosing.setString(3, "%insert into detail_nota%");
            try ( ResultSet rsClosing = psClosing.executeQuery()) {
                if (rsClosing.next()) {
                    return rsClosing.getString("usere");
                }
            }
        } catch (Exception e) {
            System.out.println("Notif petugas nota billing : " + e);
        }
        return "";
    }

    public void Copsurat2() {
        try {
            rs3 = koneksi.prepareStatement(
                    "select setting.nama_instansi,setting.alamat_instansi,setting.kabupaten,setting.propinsi,setting.kontak,setting.email,setting.logo from setting").executeQuery();
            if (rs3.next()) {
                htmlContent.append("<table width='100%' bgcolor='#ffffff' border='0' cellspacing='0' cellpadding='0' style='width: 100%; table-layout: fixed; box-sizing: border-box;'>"
                        + "<tr>"
                        // Logo Kabupaten (kiri)
                        + "<td width='15%' align='center'>"
                        + "<img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/images/logo.png'/></td>"
                        // Konten tengah (nama instansi, alamat, dll)
                        + "<td width='70%' align='center'><font color='000000' face='Tahoma'> PEMERINTAH KABUPATEN BEKASI <br/> DINAS KESEHATAN <br/>").append(rs3.getString("nama_instansi")).append("</font><br><font color='000000' face='Tahoma'>").append(rs3.getString("alamat_instansi")).append(", ").append(rs3.getString("kabupaten")).append(", ").append(rs3.getString("propinsi")).append("<br/>").append(rs3.getString("kontak")).append(", E-mail : ").append(rs3.getString("email")).append("</font>"
                        + "</td>"
                        // Logo Rumah Sakit (kanan)
                        + "<td width='15%' align='center'>"
                        + "<img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/images/logo2.png'/></td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td colspan='3'>"
                        + "<hr/>"
                        + "</td>"
                        + "</tr>"
                        + "</table>");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Tambahkan untuk debugging
        }
    }

// Helper method untuk menghitung jumlah diagnosa yang terisi
    private int getFilledDiagnosisCount(ResultSet rs) throws SQLException {
        int count = 1; // diagnosa utama selalu ada

        if (rs.getString("diagnosa_sekunder") != null && !rs.getString("diagnosa_sekunder").isEmpty()) {
            count++;
        }
        if (rs.getString("diagnosa_sekunder2") != null && !rs.getString("diagnosa_sekunder2").isEmpty()) {
            count++;
        }
        if (rs.getString("diagnosa_sekunder3") != null && !rs.getString("diagnosa_sekunder3").isEmpty()) {
            count++;
        }
        if (rs.getString("diagnosa_sekunder4") != null && !rs.getString("diagnosa_sekunder4").isEmpty()) {
            count++;
        }

        return count;
    }

// Helper method untuk menghitung jumlah diagnosa sekunder yang terisi
    private int getFilledSecondaryDiagnosisCount(ResultSet rs) throws SQLException {
        int count = 0;

        if (rs.getString("diagnosa_sekunder") != null && !rs.getString("diagnosa_sekunder").isEmpty()) {
            count++;
        }
        if (rs.getString("diagnosa_sekunder2") != null && !rs.getString("diagnosa_sekunder2").isEmpty()) {
            count++;
        }
        if (rs.getString("diagnosa_sekunder3") != null && !rs.getString("diagnosa_sekunder3").isEmpty()) {
            count++;
        }
        if (rs.getString("diagnosa_sekunder4") != null && !rs.getString("diagnosa_sekunder4").isEmpty()) {
            count++;
        }

        return count;
    }

// Helper method untuk menghitung jumlah prosedur sekunder yang terisi
    private int getFilledSecondaryProceduresCount(ResultSet rs) throws SQLException {
        int count = 0;

        if (rs.getString("prosedur_sekunder") != null && !rs.getString("prosedur_sekunder").isEmpty()) {
            count++;
        }
        if (rs.getString("prosedur_sekunder2") != null && !rs.getString("prosedur_sekunder2").isEmpty()) {
            count++;
        }
        if (rs.getString("prosedur_sekunder3") != null && !rs.getString("prosedur_sekunder3").isEmpty()) {
            count++;
        }

        return count;
    }

    //RESUME RAWAT INAP 
    private void menampilkanRESUMEBPJS(String norawat) {
        if (chkResume.isSelected() == true) {
            //RESUME RANAP SAJA
            try {
                rs2 = koneksi.prepareStatement(
                        "select resume_pasien_ranap.kd_dokter,dokter.nm_dokter,resume_pasien_ranap.diagnosa_awal,resume_pasien_ranap.alasan,resume_pasien_ranap.keluhan_utama,resume_pasien_ranap.pemeriksaan_fisik,"
                        + "resume_pasien_ranap.jalannya_penyakit,resume_pasien_ranap.pemeriksaan_penunjang,resume_pasien_ranap.hasil_laborat,resume_pasien_ranap.tindakan_dan_operasi,resume_pasien_ranap.obat_di_rs,"
                        + "resume_pasien_ranap.diagnosa_utama,resume_pasien_ranap.kd_diagnosa_utama,resume_pasien_ranap.diagnosa_sekunder,resume_pasien_ranap.kd_diagnosa_sekunder,resume_pasien_ranap.diagnosa_sekunder2,"
                        + "resume_pasien_ranap.kd_diagnosa_sekunder2,resume_pasien_ranap.diagnosa_sekunder3,resume_pasien_ranap.kd_diagnosa_sekunder3,resume_pasien_ranap.diagnosa_sekunder4,"
                        + "resume_pasien_ranap.kd_diagnosa_sekunder4,resume_pasien_ranap.prosedur_utama,resume_pasien_ranap.kd_prosedur_utama,resume_pasien_ranap.prosedur_sekunder,resume_pasien_ranap.kd_prosedur_sekunder,"
                        + "resume_pasien_ranap.prosedur_sekunder2,resume_pasien_ranap.kd_prosedur_sekunder2,resume_pasien_ranap.prosedur_sekunder3,resume_pasien_ranap.kd_prosedur_sekunder3,resume_pasien_ranap.alergi,"
                        + "resume_pasien_ranap.diet,resume_pasien_ranap.lab_belum,resume_pasien_ranap.edukasi,resume_pasien_ranap.cara_keluar,resume_pasien_ranap.ket_keluar,resume_pasien_ranap.keadaan,"
                        + "resume_pasien_ranap.ket_keadaan,resume_pasien_ranap.dilanjutkan,resume_pasien_ranap.ket_dilanjutkan,resume_pasien_ranap.kontrol,resume_pasien_ranap.obat_pulang,pasien.pekerjaan,reg_periksa.almt_pj, "
                        + "(select concat(kamar_inap.kd_kamar,' ',bangsal.nm_bangsal) from kamar_inap inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat=resume_pasien_ranap.no_rawat order by kamar_inap.tgl_masuk desc,kamar_inap.jam_masuk desc limit 1) as ruang_ranap, "
                        + "(select concat(date_format(kamar_inap.tgl_masuk,'%d-%m-%Y'),' ',kamar_inap.jam_masuk) from kamar_inap where kamar_inap.no_rawat=resume_pasien_ranap.no_rawat order by kamar_inap.tgl_masuk asc,kamar_inap.jam_masuk asc limit 1) as tgl_masuk_ranap, "
                        + "(select concat(date_format(kamar_inap.tgl_keluar,'%d-%m-%Y'),' ',kamar_inap.jam_keluar) from kamar_inap where kamar_inap.no_rawat=resume_pasien_ranap.no_rawat and kamar_inap.tgl_keluar<>'0000-00-00' order by kamar_inap.tgl_keluar desc,kamar_inap.jam_keluar desc limit 1) as tgl_keluar_ranap "
                        + "from resume_pasien_ranap inner join dokter on resume_pasien_ranap.kd_dokter=dokter.kd_dokter "
                        + "inner join reg_periksa on resume_pasien_ranap.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                        + "where resume_pasien_ranap.no_rawat='" + rs.getString("no_rawat") + "'").executeQuery();
                if (rs2.next()) {
                    String ruangResumeRanap = rs2.getString("ruang_ranap") == null || rs2.getString("ruang_ranap").trim().equals("") ? rs.getString("nm_poli") : rs2.getString("ruang_ranap");
                    String tanggalMasukResumeRanap = rs2.getString("tgl_masuk_ranap") == null || rs2.getString("tgl_masuk_ranap").trim().equals("") ? rs.getString("tgl_registrasi") : rs2.getString("tgl_masuk_ranap");
                    String tanggalKeluarResumeRanap = rs2.getString("tgl_keluar_ranap") == null || rs2.getString("tgl_keluar_ranap").trim().equals("") ? "-" : rs2.getString("tgl_keluar_ranap");
                    htmlContent.append(
                            ""
                            + "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 0; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                            + "<fieldset style='border: none; padding: 0; margin: 0;'>");
                    Copsurat();
                    htmlContent.append("<div style='background: #ececec; color: #111; padding: 8px 10px; margin: 0; border-bottom: 1px solid #2e8b57; text-align: center; font-weight: bold; font-size: 16px;'>RESUME MEDIS PASIEN</div>");

                    // Tabel informasi pasien dalam format 2 kolom
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>"
                            + "<tr>"
                            + "<td valign='top' width='50%'>"
                            + "<table width='100%' border='0' cellpadding='3px' cellspacing='0'>"
                            + "<tr><td width='30%'>Nama Pasien</td><td width='5%'>:</td><td width='65%'>").append(rs.getString("nm_pasien")).append("</td></tr>"
                            + "<tr><td>Umur</td><td>:</td><td>").append(rs.getString("umurdaftar")).append(" ").append(rs.getString("sttsumur")).append("</td></tr>"
                            + "<tr><td>Tgl Lahir</td><td>:</td><td>").append(rs.getString("tgl_lahir")).append("</td></tr>"
                            + "<tr><td>Pekerjaan</td><td>:</td><td>").append(rs2.getString("pekerjaan")).append("</td></tr>"
                            + "<tr><td>Alamat</td><td>:</td><td>").append(rs2.getString("almt_pj")).append("</td></tr>"
                            + "</table>"
                            + "</td>"
                            + "<td valign='top' width='50%'>"
                            + "<table width='100%' border='0' cellpadding='3px' cellspacing='0'>"
                            + "<tr><td width='30%'>No. Rekam Medis</td><td width='5%'>:</td><td width='65%'>").append(rs.getString("no_rkm_medis")).append("</td></tr>"
                            + "<tr><td>Ruang</td><td>:</td><td>").append(ruangResumeRanap).append("</td></tr>"
                            + "<tr><td>Jenis Kelamin</td><td>:</td><td>").append(rs.getString("jk").equals("L") ? "Laki-laki" : "Perempuan").append("</td></tr>"
                            + "<tr><td>Tanggal Masuk</td><td>:</td><td>").append(tanggalMasukResumeRanap).append("</td></tr>"
                            + "<tr><td>Tanggal Keluar</td><td>:</td><td>").append(tanggalKeluarResumeRanap).append("</td></tr>"
                            + "</table>"
                            + "</td>"
                            + "</tr>"
                            + "</table>");

                    // Konten khusus rawat inap
                    htmlContent.append("<br>");

                    // Diagnosa Awal Masuk
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Diagnosa Awal Masuk :</strong> ").append(rs2.getString("diagnosa_awal")).append("</td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Alasan Masuk Dirawat :</strong><br>").append(rs2.getString("alasan") != null ? rs2.getString("alasan").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Keluhan Utama Riwayat Penyakit
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Keluhan Utama Riwayat Penyakit :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("keluhan_utama") != null ? rs2.getString("keluhan_utama").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Pemeriksaan Fisik
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Pemeriksaan Fisik :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("pemeriksaan_fisik") != null ? rs2.getString("pemeriksaan_fisik").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Jalannya Penyakit Selama Perawatan
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Jalannya Penyakit Selama Perawatan :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("jalannya_penyakit") != null ? rs2.getString("jalannya_penyakit").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Pemeriksaan Penunjang Rad Terpenting
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Pemeriksaan Penunjang Radiologi Terpenting :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("pemeriksaan_penunjang") != null ? rs2.getString("pemeriksaan_penunjang").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Pemeriksaan Penunjang Lab Terpenting
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Pemeriksaan Penunjang Laboratorium Terpenting :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("hasil_laborat") != null ? rs2.getString("hasil_laborat").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Tindakan/Operasi Selama Perawatan
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Tindakan/Operasi Selama Perawatan :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("tindakan_dan_operasi") != null ? rs2.getString("tindakan_dan_operasi").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Obat-obatan Selama Perawatan
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Obat-obatan Selama Perawatan :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("obat_di_rs") != null ? rs2.getString("obat_di_rs").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Diagnosa Akhir untuk Rawat Inap
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' width='85%'><strong>Diagnosa Akhir</strong></td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' width='15%' align='center'><strong>Kode ICD</strong></td></tr>");

                    // Diagnosa Utama
                    if (rs2.getString("diagnosa_utama") != null && !rs2.getString("diagnosa_utama").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Diagnosa Utama :</strong> ").append(rs2.getString("diagnosa_utama")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_diagnosa_utama")).append("</td></tr>");
                    }

                    // Diagnosa Sekunder 1-4
                    if (rs2.getString("diagnosa_sekunder") != null && !rs2.getString("diagnosa_sekunder").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Diagnosa Sekunder 1 :</strong> ").append(rs2.getString("diagnosa_sekunder")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_diagnosa_sekunder")).append("</td></tr>");
                    }
                    if (rs2.getString("diagnosa_sekunder2") != null && !rs2.getString("diagnosa_sekunder2").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Diagnosa Sekunder 2 :</strong> ").append(rs2.getString("diagnosa_sekunder2")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_diagnosa_sekunder2")).append("</td></tr>");
                    }
                    if (rs2.getString("diagnosa_sekunder3") != null && !rs2.getString("diagnosa_sekunder3").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Diagnosa Sekunder 3 :</strong> ").append(rs2.getString("diagnosa_sekunder3")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_diagnosa_sekunder3")).append("</td></tr>");
                    }
                    if (rs2.getString("diagnosa_sekunder4") != null && !rs2.getString("diagnosa_sekunder4").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Diagnosa Sekunder 4 :</strong> ").append(rs2.getString("diagnosa_sekunder4")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_diagnosa_sekunder4")).append("</td></tr>");
                    }

                    // Prosedur/Tindakan untuk Rawat Inap
                    if (rs2.getString("prosedur_utama") != null && !rs2.getString("prosedur_utama").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Prosedur/Tindakan Utama :</strong> ").append(rs2.getString("prosedur_utama")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_prosedur_utama")).append("</td></tr>");
                    }
                    if (rs2.getString("prosedur_sekunder") != null && !rs2.getString("prosedur_sekunder").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Prosedur/Tindakan Sekunder 1 :</strong> ").append(rs2.getString("prosedur_sekunder")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_prosedur_sekunder")).append("</td></tr>");
                    }
                    if (rs2.getString("prosedur_sekunder2") != null && !rs2.getString("prosedur_sekunder2").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Prosedur/Tindakan Sekunder 2 :</strong> ").append(rs2.getString("prosedur_sekunder2")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_prosedur_sekunder2")).append("</td></tr>");
                    }
                    if (rs2.getString("prosedur_sekunder3") != null && !rs2.getString("prosedur_sekunder3").isEmpty()) {
                        htmlContent.append("<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>- <strong>Prosedur/Tindakan Sekunder 3 :</strong> ").append(rs2.getString("prosedur_sekunder3")).append("</td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' align='center'>").append(rs2.getString("kd_prosedur_sekunder3")).append("</td></tr>");
                    }

                    htmlContent.append("</table><br>");

                    // Alergi Obat
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Alergi / Reaksi Obat :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("alergi") != null ? rs2.getString("alergi").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Diet Selama Perawatan
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Diet Selama Perawatan :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("diet") != null ? rs2.getString("diet").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Hasil Lab Yang Belum Selesai (Pending)
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Hasil Lab Yang Belum Selesai (Pending) :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("lab_belum") != null ? rs2.getString("lab_belum").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Instruksi/Anjuran Dan Edukasi (Follow Up)
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Instruksi/Anjuran Dan Edukasi (Follow Up) :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("edukasi") != null ? rs2.getString("edukasi").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Status pulang untuk rawat inap
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' width='33%'><strong>Keadaan Pulang :</strong></td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' width='33%'><strong>Cara Keluar :</strong></td><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' width='34%'><strong>Dilanjutkan :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("keadaan")).append(rs2.getString("ket_keadaan") == null || rs2.getString("ket_keadaan").isEmpty() ? "" : ", " + rs2.getString("ket_keadaan")).append("</td>"
                            + "<td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("cara_keluar")).append(rs2.getString("ket_keluar") == null || rs2.getString("ket_keluar").isEmpty() ? "" : ", " + rs2.getString("ket_keluar")).append("</td>"
                            + "<td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("dilanjutkan")).append(rs2.getString("ket_dilanjutkan") == null || rs2.getString("ket_dilanjutkan").isEmpty() ? "" : ", " + rs2.getString("ket_dilanjutkan")).append("</td></tr>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;' colspan='3'><strong>Tanggal Kontrol :</strong> ").append(rs2.getString("kontrol")).append("</td></tr>"
                            + "</table><br>");

                    // Obat-obatan Waktu Pulang
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' style='border-collapse: collapse; border: 1px solid #d9d9d9; font-size: 11px;'>"
                            + "<tr style='background-color: #f5f5f5;'><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'><strong>Obat-obatan Waktu Pulang :</strong></td></tr>"
                            + "<tr><td style='padding: 6px 8px; border: 1px solid #e0e0e0;'>").append(rs2.getString("obat_pulang") != null ? rs2.getString("obat_pulang").replaceAll("(\r\n|\r|\n|\n\r)", "<br>") : "-").append("</td></tr>"
                            + "</table><br>");

                    // Footer dengan tanda tangan untuk rawat inap - semua DPJP harus tampil
                    if (R4.isSelected() == true) {
                        if (rs.getString("status_lanjut").equals("Ranap")) {
                            String dpjpHtml = buildDpjpSignatureHtml(rs.getString("no_rawat"));
                            if (!dpjpHtml.isEmpty()) {
                                htmlContent.append(dpjpHtml);
                            }
                        }
                    }

                    htmlContent.append("</fieldset></div>");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi Resume Rawat Inap : " + e);
            } finally {
                if (rs2 != null) {
                    try {
                        rs2.close();
                    } catch (Exception e) {
                        System.out.println("Error closing rs2: " + e);
                    }
                }
            }
        }
    }

    private String buildDpjpSignatureHtml(String noRawat) {
        java.util.ArrayList<String[]> dpjpList = new java.util.ArrayList<>();
        String sql = "select dpjp_ranap.kd_dokter, dokter.nm_dokter from dpjp_ranap "
                + "inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter "
                + "where dpjp_ranap.no_rawat=? order by dpjp_ranap.kd_dokter";

        try ( PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, noRawat);
            try ( ResultSet rsDpjp = ps.executeQuery()) {
                while (rsDpjp.next()) {
                    dpjpList.add(new String[]{
                        rsDpjp.getString("kd_dokter"),
                        rsDpjp.getString("nm_dokter")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error ambil DPJP signature: " + e);
            return "";
        }

        if (dpjpList.isEmpty()) {
            return "";
        }

        StringBuilder html = new StringBuilder();
        html.append("<table width='100%' border='0' cellpadding='8px' cellspacing='0' style='border-collapse: collapse; margin-top: 12px; clear: both; page-break-inside: avoid; break-inside: avoid;'>")
                .append("<tr><td style='text-align: center; padding-bottom: 12px;'><strong>Tanda Tangan / Verifikasi DPJP</strong></td></tr>")
                .append("<tr><td align='center'><table width='100%' border='0' cellpadding='12px' cellspacing='0' style='border-collapse: collapse;'><tr>");

        int cellWidth = Math.max(1, 100 / dpjpList.size());
        for (int i = 0; i < dpjpList.size(); i++) {
            String[] dpjp = dpjpList.get(i);
            String kdDokter = dpjp[0] != null ? dpjp[0] : "";
            String nmDokter = dpjp[1] != null ? dpjp[1] : "-";

            try {
                GetMethod get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + kdDokter.replace(" ", "_"));
                http.executeMethod(get);
            } catch (Exception e) {
                System.out.println("Error generate QR DPJP: " + e);
            }

            html.append("<td align='center' valign='top' style='width: ")
                    .append(cellWidth).append("%; padding: 0; vertical-align: top;'>")
                    .append("<div style='border: 1px solid #d9d9d9; padding: 8px; border-radius: 4px;'>")
                    .append("<div style='font-size: 10px; font-weight: bold; margin-bottom: 4px;'>Dokter DPJP ")
                    .append((i + 1)).append("</div>")
                    .append("<img width='70' height='70' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/penggajian/temp/").append(kdDokter).append(".png' style='margin: 4px 0;'/>")
                    .append("<div style='font-size: 9px; margin-top: 4px; word-wrap: break-word; line-height: 1.4;'>").append(nmDokter).append("</div>")
                    .append("</div>")
                    .append("</td>");
        }

        html.append("</tr></table></td></tr></table>");
        return html.toString();
    }

    // Method untuk format tanggal dari YYYY-MM-DD ke DD MMMM YYYY (dengan jam jika ada)
    private String formatTanggal(String tanggalDb) {
        if (tanggalDb == null || tanggalDb.trim().isEmpty()) {
            return "";
        }

        try {
            String[] bulanIndonesia = {
                "", "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
            };

            String jam = "";
            String tanggalSaja = tanggalDb;

            // Cek apakah ada jam (format datetime YYYY-MM-DD HH:mm:ss)
            if (tanggalDb.contains(" ")) {
                String[] bagianDateTime = tanggalDb.split(" ");
                tanggalSaja = bagianDateTime[0];
                if (bagianDateTime.length > 1) {
                    jam = " " + bagianDateTime[1]; // Simpan bagian jam
                }
            }

            String[] bagianTanggal = tanggalSaja.split("-");
            if (bagianTanggal.length == 3) {
                int tahun = Integer.parseInt(bagianTanggal[0]);
                int bulan = Integer.parseInt(bagianTanggal[1]);
                int hari = Integer.parseInt(bagianTanggal[2]);

                if (bulan >= 1 && bulan <= 12) {
                    return hari + " " + bulanIndonesia[bulan] + " " + tahun + jam;
                }
            }
        } catch (Exception e) {
            System.out.println("Error formatting date: " + e);
        }

        // Jika gagal format, kembalikan tanggal asli
        return tanggalDb;
    }

// Helper method untuk validasi data null
    private String validasi(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "-";
        }
        return str.trim();
    }

    private void menampilkanBerkasDigital(String norawat) {
        if (!chkBerkasDigital.isSelected()) {
            return;
        }

        String sql = "SELECT master_berkas_digital.nama, berkas_digital_perawatan.lokasi_file "
                + "FROM berkas_digital_perawatan "
                + "INNER JOIN master_berkas_digital ON berkas_digital_perawatan.kode = master_berkas_digital.kode "
                + "WHERE berkas_digital_perawatan.no_rawat = ? "
                + "ORDER BY berkas_digital_perawatan.kode";

        try ( PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, norawat);
            try ( ResultSet rs2 = ps.executeQuery()) {
                if (rs2.next()) {
                    htmlContent.append(
                            ""
                            + "<div class='berkas-page' style='page-break-before: always; break-before: page; border: 1px solid #2e8b57; border-radius: 4px; padding: 12px; margin: 6px 0; background: #ffffff; width: 100%; box-sizing: border-box;'>"
                            + "<fieldset style='border: none; padding: 0; margin: 0;'>"
                            + "<h2 align='center'>BERKAS DIGITAL PERAWATAN</h2>");

                    // Tabel informasi pasien
                    htmlContent.append("<table width='100%' border='0' cellpadding='3px' cellspacing='0' class='tbl_form'>")
                            .append("<tr><td valign='top' width='20%'>No.Rekam Medis</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("no_rkm_medis")).append("</td></tr>")
                            .append("<tr><td valign='top' width='20%'>Nama Pasien</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("nm_pasien")).append("</td></tr>")
                            .append("<tr><td valign='top' width='20%'>Tgl. Lahir</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("tgl_lahir")).append("</td></tr>")
                            .append("<tr><td valign='top' width='20%'>No.Rawat</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>").append(rs.getString("no_rawat")).append("</td></tr>");

                    if ("Ranap".equals(rs.getString("status_lanjut"))) {
                        String sqlDpjp = "SELECT dokter.nm_dokter FROM dpjp_ranap "
                                + "INNER JOIN dokter ON dpjp_ranap.kd_dokter = dokter.kd_dokter "
                                + "WHERE dpjp_ranap.no_rawat = ?";
                        try ( PreparedStatement psDpjp = koneksi.prepareStatement(sqlDpjp)) {
                            psDpjp.setString(1, rs.getString("no_rawat"));
                            try ( ResultSet rs3 = psDpjp.executeQuery()) {
                                if (rs3.next()) {
                                    htmlContent.append("<tr><td valign='top' width='20%'>DPJP Ranap</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>");
                                    int urutdpjp = 1;
                                    do {
                                        htmlContent.append(urutdpjp).append(". ").append(rs3.getString("nm_dokter")).append("&nbsp;&nbsp;");
                                        urutdpjp++;
                                    } while (rs3.next());
                                    htmlContent.append("</td></tr>");
                                }
                            }
                        }
                    }

                    htmlContent.append("<tr><td valign='top' width='20%'>Cara Bayar</td><td valign='top' width='1%' align='center'>:</td><td valign='top' width='79%'>")
                            .append(rs.getString("png_jawab")).append("</td></tr>")
                            .append("</table><br>");

                    // Tabel berkas digital
                    htmlContent.append("<table width='100%' border='1' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>")
                            .append("<tr align='center'>")
                            .append("<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>")
                            .append("<td valign='top' width='16%' bgcolor='#FFFAF8'>Nama Berkas</td>")
                            .append("<td valign='top' width='80%' bgcolor='#FFFAF8'>Berkas Digital</td>")
                            .append("</tr>");

                    int w = 1;
                    do {
                        String fileName = rs2.getString("lokasi_file");
                        String fileURL = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/" + fileName;

                        htmlContent.append("<tr><td valign='top' align='center'>").append(w).append("</td>")
                                .append("<td valign='top' align='center'>").append(rs2.getString("nama")).append("</td>");

                        if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                            htmlContent.append("<td valign='top' align='center'>")
                                    .append("<a href='").append(fileURL).append("'>")
                                    .append("<img alt='Berkas Digital' src='").append(fileURL).append("' style='width: 100%; height: auto; max-width: 100%;'/></a></td>");
                        } else if (fileName.toLowerCase().endsWith(".pdf")) {
                            htmlContent.append("<td valign='top' align='center'>");
                            processPdfFile(fileURL, rs2.getString("nama"), w);
                            htmlContent.append("</td>");
                        } else {
                            htmlContent.append("<td valign='top'><a href='").append(fileURL).append("'>")
                                    .append(rs2.getString("nama")).append("_")
                                    .append(fileName.replace("pages/upload/", ""))
                                    .append("</a></td>");
                        }
                        htmlContent.append("</tr>");
                        w++;
                    } while (rs2.next());

                    htmlContent.append("</table>");
                    htmlContent.append("</fieldset></div>");
                }
            }
        } catch (SQLException e) {
            System.out.println("Notif Berkas Digital: " + e.getMessage());
        }
    }

//    private void menampilkanFormAssesment(String norawat) {
//        try {
//            if (chkFormAssesment.isSelected() == true) {
//                rs2 = koneksi.prepareStatement(
//                        "SELECT lembar_reassesment.no_rawat, DATE_FORMAT(lembar_reassesment.tanggal,'%d-%m-%Y %H:%i:%s') as tanggal, "
//                        + "lembar_reassesment.subjective, lembar_reassesment.objective, lembar_reassesment.assesment, "
//                        + "lembar_reassesment.goal, lembar_reassesment.tindakan, lembar_reassesment.edukasi, "
//                        + "lembar_reassesment.frekuensi, lembar_reassesment.rencana, dokter.nm_dokter "
//                        + "FROM lembar_reassesment "
//                        + "INNER JOIN dokter ON lembar_reassesment.kd_dokter = dokter.kd_dokter "
//                        + "WHERE lembar_reassesment.no_rawat = '" + norawat + "' "
//                        + "ORDER BY lembar_reassesment.tanggal DESC"
//                ).executeQuery();
//
//                while (rs2.next()) {
//                    htmlContent.append(
//                            ""
//                            + "<p class='pagebreak'>"
//                            + "<div style='border: 2px solid #17a2b8; border-radius: 8px; padding: 20px; margin: 10px; background: #ffffff; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>"
//                    );
//
//                    Copsurat();
//
//                    htmlContent.append(
//                            "<div style='background: linear-gradient(135deg, #17a2b8, #117a8b); color: white; padding: 15px; margin: -20px -20px 20px -20px; border-radius: 6px 6px 0 0;'>"
//                            + "<h2 style='margin: 0; text-align: center; font-size: 24px; letter-spacing: 1px;'>FORM ASSESMENT REHABILITASI MEDIK</h2>"
//                            + "</div>"
//                    );
//
//                    htmlContent.append(buildPatientInfoSection());
//
//                    htmlContent.append(
//                            "<div style='margin-top: 20px;'>"
//                            + "<table style='width: 100%; border-collapse: collapse;'>"
//                            + "<tr><td style='width: 200px; padding: 8px; font-weight: bold; vertical-align: top;'>Tanggal</td><td style='padding: 8px;'>: " + rs2.getString("tanggal") + "</td></tr>"
//                            + "<tr><td style='padding: 8px; font-weight: bold; vertical-align: top;'>Dokter</td><td style='padding: 8px;'>: " + rs2.getString("nm_dokter") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>SUBJECTIVE</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("subjective").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>OBJECTIVE</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("objective").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>ASSESMENT</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("assesment").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>GOAL</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("goal").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>TINDAKAN</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("tindakan").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>EDUKASI</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("edukasi").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>FREKUENSI & DURASI</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("frekuensi").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #17a2b8;'>RENCANA TINDAK LANJUT</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("rencana").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "</table>"
//                            + "</div>"
//                    );
//
//                    htmlContent.append("</div></p>");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error Data Form Assesment: " + e);
//        } finally {
//            if (rs2 != null) {
//                try {
//                    rs2.close();
//                } catch (SQLException e) {
//                    System.out.println("Error closing rs2 in Form Assesment: " + e.getMessage());
//                }
//            }
//        }
//    }
//    private void menampilkanFormLembarTerapi(String norawat) {
//        try {
//            if (chkFormLembarTerapi.isSelected() == true) {
//                rs2 = koneksi.prepareStatement(
//                        "SELECT lembar_terapi.no_rawat, DATE_FORMAT(lembar_terapi.tanggal,'%d-%m-%Y %H:%i:%s') as tanggal, "
//                        + "lembar_terapi.subjective, lembar_terapi.objective, lembar_terapi.assesment, "
//                        + "lembar_terapi.goal, lembar_terapi.tindakan, lembar_terapi.edukasi, "
//                        + "lembar_terapi.frekuensi, lembar_terapi.rencana, dokter.nm_dokter "
//                        + "FROM lembar_terapi "
//                        + "INNER JOIN dokter ON lembar_terapi.kd_dokter = dokter.kd_dokter "
//                        + "WHERE lembar_terapi.no_rawat = '" + norawat + "' "
//                        + "ORDER BY lembar_terapi.tanggal DESC"
//                ).executeQuery();
//
//                while (rs2.next()) {
//                    htmlContent.append(
//                            ""
//                            + "<p class='pagebreak'>"
//                            + "<div style='border: 2px solid #6f42c1; border-radius: 8px; padding: 20px; margin: 10px; background: #ffffff; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>"
//                    );
//
//                    Copsurat();
//
//                    htmlContent.append(
//                            "<div style='background: linear-gradient(135deg, #6f42c1, #5a32a3); color: white; padding: 15px; margin: -20px -20px 20px -20px; border-radius: 6px 6px 0 0;'>"
//                            + "<h2 style='margin: 0; text-align: center; font-size: 24px; letter-spacing: 1px;'>FORM LEMBAR TERAPI REHABILITASI MEDIK</h2>"
//                            + "</div>"
//                    );
//
//                    htmlContent.append(buildPatientInfoSection());
//
//                    htmlContent.append(
//                            "<div style='margin-top: 20px;'>"
//                            + "<table style='width: 100%; border-collapse: collapse;'>"
//                            + "<tr><td style='width: 200px; padding: 8px; font-weight: bold; vertical-align: top;'>Tanggal</td><td style='padding: 8px;'>: " + rs2.getString("tanggal") + "</td></tr>"
//                            + "<tr><td style='padding: 8px; font-weight: bold; vertical-align: top;'>Dokter</td><td style='padding: 8px;'>: " + rs2.getString("nm_dokter") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>SUBJECTIVE</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("subjective").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>OBJECTIVE</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("objective").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>ASSESMENT</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("assesment").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>GOAL</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("goal").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>TINDAKAN</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("tindakan").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>EDUKASI</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("edukasi").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>FREKUENSI & DURASI</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("frekuensi").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 15px 0px 5px 0px; font-weight: bold; color: #6f42c1;'>RENCANA TINDAK LANJUT</td></tr>"
//                            + "<tr><td colspan='2' style='padding: 8px; border: 1px solid #ddd; background: #f8f9fa;'>" + rs2.getString("rencana").replaceAll("\n", "<br>") + "</td></tr>"
//                            + "</table>"
//                            + "</div>"
//                    );
//
//                    htmlContent.append("</div></p>");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error Data Form Lembar Terapi: " + e);
//        } finally {
//            if (rs2 != null) {
//                try {
//                    rs2.close();
//                } catch (SQLException e) {
//                    System.out.println("Error closing rs2 in Form Lembar Terapi: " + e.getMessage());
//                }
//            }
//        }
//    }
    private void processPdfFile(String fileURL, String fileName, int index) {
        try {
            URL url = new URL(fileURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            try ( InputStream inputStream = new BufferedInputStream(connection.getInputStream());  PDDocument document = PDDocument.load(inputStream)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int page = 0; page < document.getNumberOfPages(); page++) {
                    // Gunakan DPI 150 untuk kualitas lebih baik
                    BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150);
                    // Simpan gambar sebagai file sementara
                    String tempImagePath = saveImageAsTemporaryFile(image, fileName + "_Page_" + (page + 1));
                    // Menyesuaikan ukuran gambar melebar penuh di HTML
                    htmlContent.append("<img src='file:///" + tempImagePath + "' style='width: 100%; height: auto; max-width: 100%;'/><br>");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat memproses file PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String saveImageAsTemporaryFile(BufferedImage image, String fileName) {
        try {
            // Direktori penyimpanan sementara
            String tempDir = "tempFile";
            Path tempDirPath = Paths.get(tempDir);

            // Membuat direktori jika belum ada
            if (!Files.exists(tempDirPath)) {
                Files.createDirectories(tempDirPath);
            }

            // Membersihkan nama file dari karakter ilegal
            String cleanFileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");

            // Menambahkan timestamp untuk membuat nama file unik
            long timestamp = System.currentTimeMillis();
            File tempFile = new File(tempDir, cleanFileName + "_" + timestamp + ".png");

            // Menyimpan gambar dalam format PNG
            ImageIO.write(image, "png", tempFile);

            // Mengembalikan path dalam format HTML-friendly
            return tempFile.getAbsolutePath().replace("\\", "/");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan file sementara: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void autoDownloadWithRetry(String downloadUrl, int maxRetries, String sep, String tanggal,
            String norawat, String kode, String tanggaljam, String inacbg,
            String transactionId, String statusTteId) {
        Timer retryTimer = new Timer(3000, null); // Check setiap 3 detik
        final int[] attemptCount = {0};

        ActionListener retryAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptCount[0]++;
                System.out.println("Percobaan download ke-" + attemptCount[0]);

                // Update retry count di database
                updateRetryCount(transactionId, attemptCount[0]);

                try {
                    URL url = new URL(downloadUrl);
                    ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                    String fileName = "RPP" + NoRawat.getText().trim().replaceAll("/", "") + ".pdf";
                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                    fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                    fileOutputStream.close();
                    readableByteChannel.close();

                    System.out.println("Auto Download Berhasil : " + fileName);

                    // Update status ke SIGNED
                    updateStatusTte(transactionId, "SIGNED", null);

                    // Tutup window URLSertisign otomatis
                    SwingUtilities.invokeLater(() -> {
                        WindowURLSertisign.setVisible(false);
                    });

                    // Stop timer jika berhasil
                    ((Timer) e.getSource()).stop();

                    // AUTO UPLOAD KE NEXTCLOUD
                    autoUploadToNextCloud(fileName, sep, tanggal, norawat, kode, tanggaljam, inacbg, transactionId);

                } catch (IOException ex) {
                    System.out.println("Percobaan ke-" + attemptCount[0] + " gagal: " + ex.getMessage());

                    // Jika sudah mencapai max retry, stop timer
                    if (attemptCount[0] >= maxRetries) {
                        ((Timer) e.getSource()).stop();
                        System.out.println("Max retry tercapai, download dihentikan");

                        // Update status ke FAILED
                        updateStatusTte(transactionId, "FAILED", "Max retry reached: " + ex.getMessage());

                        // Notifikasi gagal dengan always on top
                        SwingUtilities.invokeLater(() -> {
                            JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            dialog.setModal(true);
                            JOptionPane.showMessageDialog(dialog,
                                    "❌ Auto Download Gagal!\n\n"
                                    + "Gagal setelah " + maxRetries + " percobaan.\n"
                                    + "Silakan download manual dari window yang terbuka.",
                                    "Download Gagal",
                                    JOptionPane.WARNING_MESSAGE);
                        });
                    }
                }
            }
        };

        retryTimer.addActionListener(retryAction);
        retryTimer.start();
    }

    private void updateRetryCount(String transactionId, int retryCount) {
        try {
            Sequel.mengedit("status_tte", "transaction_id=?", "retry_count=?, updated_at=NOW()",
                    2, new String[]{String.valueOf(retryCount), transactionId});
        } catch (Exception e) {
            System.err.println("Error update retry count: " + e.getMessage());
        }
    }

    private String insertStatusTte(String norawat, String sep, String transactionId, String documentName,
            String signerEmail, String signerName, String signerId, String urlCallback) {
        try {
            String statusTteId = Sequel.cariIsi("SELECT UUID()"); // Generate unique ID

            Sequel.menyimpantf("status_tte (no_rawat, no_sep, transaction_id, document_name, status, "
                    + "url_callback, signer_email, signer_name, signer_id, created_by)",
                    "?,?,?,?,?,?,?,?,?,?",
                    "status_tte", 10,
                    new String[]{norawat, sep, transactionId, documentName, "PROCESSING",
                        urlCallback, signerEmail, signerName, signerId, akses.getkode()});

            System.out.println("Status TTE berhasil disimpan untuk transaction: " + transactionId);
            return statusTteId;

        } catch (Exception e) {
            System.err.println("Error insert status_tte: " + e.getMessage());
            return "";
        }
    }

    private void updateStatusTte(String transactionId, String status, String errorMessage) {
        try {
            if (errorMessage != null) {
                Sequel.mengedit("status_tte", "transaction_id=?",
                        "status=?, error_message=?, updated_at=NOW()",
                        3, new String[]{status, errorMessage, transactionId});
            } else {
                String signedAt = status.equals("SIGNED") ? ", signed_at=NOW()" : "";
                Sequel.mengedit("status_tte", "transaction_id=?",
                        "status=?, updated_at=NOW()" + signedAt,
                        2, new String[]{status, transactionId});
            }
            System.out.println("Status TTE updated: " + transactionId + " -> " + status);
        } catch (Exception e) {
            System.err.println("Error update status TTE: " + e.getMessage());
        }
    }

    private void autoUploadToNextCloud(String fileName, String sep, String tanggal, String norawat,
            String kode, String tanggaljam, String inacbg, String transactionId) {
        // Jalankan upload di background thread agar tidak freeze UI
        /*   SwingWorker<Boolean, String> uploadWorker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                publish("Memulai upload ke NextCloud...");

                try {
                    // Handle SEP kosong - ganti dengan no rawat yang diformat
                    String cleanSep = sep;
                    if (sep == null || sep.trim().isEmpty()) {
                        // Format no rawat dari 2025/06/14/000001 menjadi 20250614000001
                        cleanSep = norawat.replaceAll("/", "");
                        publish("SEP kosong, menggunakan no rawat sebagai pengganti: " + cleanSep);
                    } else {
                        // Bersihkan SEP dari karakter slash jika ada
                        cleanSep = sep.replaceAll("/", "");
                    }

                    // Buat instance UploadPDFTte
                    UploadPDFTte uploader = new UploadPDFTte(fileName, cleanSep, tanggal, norawat, kode, tanggaljam, inacbg);

                    // Lakukan upload
                    boolean uploadSuccess = uploader.upload();

                    if (uploadSuccess) {
                        publish("Upload berhasil ke: " + uploader.getUploadedUrl());

                        // Update berkas_tte dengan status signed
                        updateBerkasTte(norawat, cleanSep, fileName, uploader.getUploadedUrl(),
                                tanggal, kode, tanggaljam, inacbg, true);

                        return true;
                    } else {
                        publish("Upload gagal ke NextCloud");

                        // Tetap insert ke berkas_tte tapi dengan status belum signed
                        updateBerkasTte(norawat, cleanSep, fileName, "",
                                tanggal, kode, tanggaljam, inacbg, false);

                        return false;
                    }

                } catch (Exception e) {
                    publish("Error saat upload: " + e.getMessage());
                    System.err.println("Upload error: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Update console dengan progress message
                for (String message : chunks) {
                    System.out.println(message);
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();

                    if (success) {
                        // Notifikasi berhasil download + upload
                        SwingUtilities.invokeLater(() -> {
                            JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            dialog.setModal(true);
                            JOptionPane.showMessageDialog(dialog,
                                    "✅ Download & Upload Berhasil!\n\n"
                                    + "File: " + fileName + "\n"
                                    + "Lokasi: " + new File(fileName).getAbsolutePath() + "\n"
                                    + "Status: Tersimpan di NextCloud & Database",
                                    "Proses Selesai",
                                    JOptionPane.INFORMATION_MESSAGE);
                        });
                    } else {
                        // Notifikasi download berhasil tapi upload gagal
                        SwingUtilities.invokeLater(() -> {
                            JDialog dialog = new JDialog();
                            dialog.setAlwaysOnTop(true);
                            dialog.setModal(true);
                            JOptionPane.showMessageDialog(dialog,
                                    "⚠️ Download Berhasil, Upload Gagal!\n\n"
                                    + "File: " + fileName + "\n"
                                    + "Lokasi: " + new File(fileName).getAbsolutePath() + "\n"
                                    + "Status: Tersimpan lokal, gagal upload ke NextCloud",
                                    "Upload Gagal",
                                    JOptionPane.WARNING_MESSAGE);
                        });
                    }

                } catch (Exception e) {
                    System.err.println("Error dalam upload worker: " + e.getMessage());

                    // Notifikasi error
                    SwingUtilities.invokeLater(() -> {
                        JDialog dialog = new JDialog();
                        dialog.setAlwaysOnTop(true);
                        dialog.setModal(true);
                        JOptionPane.showMessageDialog(dialog,
                                "❌ Error Saat Upload!\n\n"
                                + "File: " + fileName + "\n"
                                + "Download: Berhasil\n"
                                + "Upload: Error - " + e.getMessage(),
                                "Error Upload",
                                JOptionPane.ERROR_MESSAGE);
                    });
                }
            }
        };

        // Jalankan upload worker
        uploadWorker.execute(); */
    }

    private void updateBerkasTte(String norawat, String sep, String fileName, String filePath,
            String tanggal, String kode, String tanggaljam, String inacbg, boolean isSigned) {
        try {
            // Ambil file size
            File file = new File(fileName);
            long fileSize = file.exists() ? file.length() : 0;

            // Tentukan status rawat berdasarkan data
            String statusRawat = getStatusRawat(sep, norawat);

            // Clean file name
            String cleanFileName = fileName;
            if (fileName.contains("/")) {
                cleanFileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }

            String uploadedAt = getCurrentDateTime();
            String signedAt = isSigned ? uploadedAt : null;

            // PERBAIKAN: Validasi dan format tanggal untuk field datetime
            String validTanggal = validateAndFormatDateTime(tanggaljam);

            System.out.println("=== DEBUG BERKAS TTE (RMRiwayatPerawatan) ===");
            System.out.println("Original tanggal: '" + tanggal + "'");
            System.out.println("Original tanggaljam: '" + tanggaljam + "'");
            System.out.println("Valid tanggal: '" + validTanggal + "'");
            System.out.println("Uploaded at: '" + uploadedAt + "'");
            System.out.println("SEP: '" + sep + "'");
            System.out.println("NoRawat: '" + norawat + "'");
            System.out.println("Kode: '" + kode + "'");

            Sequel.menyimpantf(
                    "berkas_tte (no_rawat, no_sep, tanggal, kode, nama_file, file_path, file_size, "
                    + "status, kategori, jenis_dokumen, uploaded_at, uploaded_by, is_signed, signed_at)",
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?",
                    "berkas_tte",
                    14,
                    new String[]{norawat, sep, validTanggal, kode, cleanFileName, filePath,
                        String.valueOf(fileSize), statusRawat, inacbg, "RPP",
                        uploadedAt, akses.getkode(), isSigned ? "1" : "0", signedAt}
            );

            System.out.println("Berkas TTE berhasil disimpan: " + cleanFileName);

        } catch (Exception e) {
            System.err.println("Error insert berkas_tte: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String validateAndFormatDateTime(String dateTimeStr) {
        try {
            // Jika string kosong atau null, gunakan waktu sekarang
            if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
                System.out.println("DateTime kosong, menggunakan waktu sekarang");
                return getCurrentDateTime();
            }

            // Coba berbagai format yang mungkin ada
            String[] possibleFormats = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd",
                "dd-MM-yyyy HH:mm:ss",
                "dd-MM-yyyy",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd",
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy"
            };

            for (String format : possibleFormats) {
                try {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(format);
                    java.time.LocalDateTime parsedDate;

                    if (format.contains("HH:mm:ss")) {
                        parsedDate = java.time.LocalDateTime.parse(dateTimeStr.trim(), formatter);
                    } else {
                        java.time.LocalDate date = java.time.LocalDate.parse(dateTimeStr.trim(), formatter);
                        parsedDate = date.atStartOfDay(); // Set waktu ke 00:00:00
                    }

                    // Return dalam format MySQL datetime
                    return parsedDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                } catch (Exception e) {
                    // Lanjut ke format berikutnya
                    continue;
                }
            }

            // Jika semua format gagal, gunakan waktu sekarang
            System.err.println("Gagal parse tanggal: '" + dateTimeStr + "', menggunakan waktu sekarang");
            return getCurrentDateTime();

        } catch (Exception e) {
            System.err.println("Error validating datetime: " + e.getMessage());
            return getCurrentDateTime();
        }
    }

    private String getStatusRawat(String sep, String norawat) {
        try {
            String statusRawat = "";
            if (sep != null && !sep.trim().isEmpty()) {
                statusRawat = Sequel.cariIsi(
                        "SELECT CASE jnspelayanan WHEN '1' THEN 'Rawat-Inap' WHEN '2' THEN 'Rawat-Jalan' ELSE 'Unknown' END "
                        + "FROM bridging_sep WHERE no_sep=?", sep);
            }

            if (statusRawat.equals("Unknown") || statusRawat.equals("")) {
                statusRawat = Sequel.cariIsi(
                        "SELECT CASE jnspelayanan WHEN '1' THEN 'Rawat-Inap' WHEN '2' THEN 'Rawat-Jalan' ELSE 'Unknown' END "
                        + "FROM bridging_sep WHERE no_rawat=? ORDER BY CASE WHEN jnspelayanan LIKE '1%' OR jnspelayanan LIKE '%Ranap%' THEN 0 ELSE 1 END, tglsep DESC, no_sep DESC LIMIT 1", norawat);

                if (statusRawat.equals("Unknown") || statusRawat.equals("")) {
                    statusRawat = "Rawat-Inap"; // Default
                }
            }
            return statusRawat;
        } catch (Exception e) {
            System.err.println("Error getting status rawat: " + e.getMessage());
            return "Rawat-Inap"; // Default
        }
    }

    private String getCurrentDateTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public void Copsurat() {
        try {
            rs3 = koneksi.prepareStatement(
                    "select setting.nama_instansi,setting.alamat_instansi,setting.kabupaten,setting.propinsi,setting.kontak,setting.email,setting.logo from setting").executeQuery();
            if (rs3.next()) {
                htmlContent.append("<table width='100%' bgcolor='#ffffff' border='0' cellspacing='0' cellpadding='0' style='width: 100%; table-layout: fixed; box-sizing: border-box;'>"
                        + "<tr>"
                        // Logo tunggal (kiri)
                        + "<td width='15%' align='center'>"
                        + "<img width='80' height='80' src='http://").append(koneksiDB.HOSTHYBRIDWEB()).append(":").append(koneksiDB.PORTWEB()).append("/").append(koneksiDB.HYBRIDWEB()).append("/images/logo.png'/></td>"
                        // Konten tengah (hanya nama instansi dan alamat)
                        + "<td width='85%' align='center'><font color='000000' face='Tahoma'>").append(rs3.getString("nama_instansi")).append("</font><br><font color='000000' face='Tahoma'>").append(rs3.getString("alamat_instansi")).append(", ").append(rs3.getString("kabupaten")).append(", ").append(rs3.getString("propinsi")).append("<br/>").append(rs3.getString("kontak")).append(", E-mail : ").append(rs3.getString("email")).append("</font>"
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td colspan='2'>"
                        + "<hr/>"
                        + "</td>"
                        + "</tr>"
                        + "</table>");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Tambahkan untuk debugging
        }
    }

    /**
     * Helper method untuk mengambil nilai dari tabel dengan berbagai
     * kemungkinan nama kolom
     */
    private String getTableValue(javax.swing.table.TableModel model, int row, String... possibleColumns) {
        for (String columnName : possibleColumns) {
            try {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (model.getColumnName(col).toLowerCase().contains(columnName.toLowerCase())) {
                        Object value = model.getValueAt(row, col);
                        return value != null ? value.toString() : "";
                    }
                }
            } catch (Exception e) {
                // Continue to next possible column
            }
        }
        return "";
    }

    /**
     * Helper method untuk memotong string sesuai panjang yang diinginkan
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Calculate data completeness untuk PPK analysis
     */
    private double calculateDataCompleteness(String data) {
        String[] requiredFields = {
            "KELUHAN", "DIAGNOSA", "PEMERIKSAAN", "TINDAKAN", "VITAL SIGNS",
            "NAMA PASIEN", "JENIS KELAMIN", "TANGGAL", "ICD", "DOKTER"
        };

        int foundFields = 0;
        String upperData = data.toUpperCase();

        for (String field : requiredFields) {
            if (upperData.contains(field)) {
                foundFields++;
            }
        }

        return (double) foundFields / requiredFields.length * 100;
    }

    /**
     * Validasi data untuk PPK analysis
     */
    private boolean validateDataForPPKAnalysis(String data) {
        if (data == null || data.trim().isEmpty()) {
            System.out.println("❌ PPK Data validation failed: Empty data");
            return false;
        }

        String upperData = data.toUpperCase();

        // Essential fields untuk PPK analysis
        String[] essentialFields = {"DIAGNOSA", "KELUHAN", "PEMERIKSAAN", "DOKTER"};

        for (String field : essentialFields) {
            if (!upperData.contains(field)) {
                System.out.println("❌ PPK Data validation failed: Missing " + field);
                return false;
            }
        }

        // Check minimum data length
        if (data.length() < 500) {
            System.out.println("❌ PPK Data validation failed: Data too minimal for comprehensive PPK analysis");
            return false;
        }

        System.out.println("✅ PPK Data validation passed");
        return true;
    }
}
