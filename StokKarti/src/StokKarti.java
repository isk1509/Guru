import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.sql.*;


public class StokKarti extends JFrame {

    private static final String SEARCH_CARD = "select * from StokKartlari where Stok_Kodu = ?";

    StokKartiIslemleri islemler = new StokKartiIslemleri();

    private JTable kartTable;
    private JTextField stokKoduTxt;
    private JTextField stokAdiTxt;
    private JComboBox stokTipiCb;
    private JComboBox birimCb;
    private JFormattedTextField tarihDate;
    private JComboBox kdvCb;
    private JButton kaydetButton;
    private JButton araButton;
    private JTextField stokKoduArama;
    private JPanel anaPanel;
    private JTextArea aciklamaTxtArea;
    private JTextField barkodTxt;
    private JButton silButton;
    private JButton guncelleButton;
    private JButton kopyalaButton;


   Connection con;
   PreparedStatement pst;



    public StokKarti()
    {
        add(anaPanel);
        setSize(1400,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("STOK KARTI");
        setVisible(Boolean.TRUE);

        setLocationRelativeTo(null);
        ImageIcon logo = new ImageIcon("src/warehouse.png");
        setIconImage(logo.getImage());


        islemler.Connect();
        islemler.TableLoad(kartTable);

        kaydetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (stokKoduTxt.getText().isEmpty() || stokAdiTxt.getText().isEmpty() || barkodTxt.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Lütfen tüm alanları doldurun!","HATA",JOptionPane.WARNING_MESSAGE);
                }
                else
                {

                   String stokKodu = stokKoduTxt.getText();
                   String stokAdi = stokAdiTxt.getText();
                   String birimi = birimCb.getSelectedItem().toString();
                   String barkodu = barkodTxt.getText();
                   String aciklama = aciklamaTxtArea.getText();

                   int stokTipi=  Integer.valueOf(stokTipiCb.getSelectedItem().toString());
                   double kdv = Double.valueOf(kdvCb.getSelectedItem().toString());


                   islemler.AddCard(stokKodu,stokAdi,stokTipi,birimi,barkodu,kdv,aciklama);
                   islemler.TableLoad(kartTable);

                   stokKoduTxt.setText("");
                   stokAdiTxt.setText("");
                   birimCb.setSelectedIndex(0);
                   barkodTxt.setText("");
                   aciklamaTxtArea.setText("");
                   tarihDate.setText("");
                   stokTipiCb.setSelectedIndex(0);
                   kdvCb.setSelectedIndex(0);

                   stokKoduTxt.requestFocus();
                }
            }

    });

        kartTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);

                int selectedrow = kartTable.getSelectedRow();
                stokKoduTxt.setText(kartTable.getValueAt(selectedrow,0).toString());
                stokAdiTxt.setText(kartTable.getValueAt(selectedrow,1).toString());
                String subject1 = kartTable.getValueAt(selectedrow,2).toString();

                switch (subject1){
                    case "1":
                        stokTipiCb.setSelectedIndex(0);
                        break;
                    case "2":
                        stokTipiCb.setSelectedIndex(1);
                        break;
                    case "3":
                        stokTipiCb.setSelectedIndex(2);
                        break;
                    case "4":
                        stokTipiCb.setSelectedIndex(3);
                        break;
                    case "5":
                        stokTipiCb.setSelectedIndex(4);
                        break;


                }
                String subject2 = kartTable.getValueAt(selectedrow,3).toString();
                switch (subject2){
                    case "birim1":
                        birimCb.setSelectedIndex(0);
                        break;
                    case "birim2":
                        birimCb.setSelectedIndex(1);
                        break;
                    case "birim3":
                        birimCb.setSelectedIndex(2);
                        break;
                    case "birim4":
                        birimCb.setSelectedIndex(3);
                        break;
                    case "birim5":
                        birimCb.setSelectedIndex(4);
                        break;
                }
                barkodTxt.setText(kartTable.getValueAt(selectedrow,4).toString());
                String subject3 = kartTable.getValueAt(selectedrow,5).toString();
                switch (subject3){
                    case "8.0":
                        kdvCb.setSelectedIndex(0);
                        break;
                    case "16.0":
                        kdvCb.setSelectedIndex(1);
                        break;
                }
                aciklamaTxtArea.setText(kartTable.getValueAt(selectedrow,6).toString());
                tarihDate.setText(kartTable.getValueAt(selectedrow,7).toString());



            }
        });

        silButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(stokKoduTxt.getText().isEmpty() || stokAdiTxt.getText().isEmpty() || barkodTxt.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Lütfen slinecek bir stok kartı seçin!","HATA",JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    String id;
                    id = stokKoduTxt.getText();

                    islemler.DeleteCard(id);
                    islemler.TableLoad(kartTable);

                    stokKoduTxt.setText("");
                    stokAdiTxt.setText("");
                    stokTipiCb.setSelectedIndex(0);
                    birimCb.setSelectedIndex(0);
                    barkodTxt.setText("");
                    kdvCb.setSelectedIndex(0);
                    aciklamaTxtArea.setText("");
                    tarihDate.setText("");
                    stokKoduTxt.requestFocus();
                }
            }
        });

        araButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (stokKoduArama.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Lütfen arama alanına stok kodu girin!","HATA",JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    try
                    {
                        String id = stokKoduArama.getText();

                        con = DriverManager.getConnection("jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db_ismi, Database.kullanci_adi, Database.parola);

                        pst = con.prepareStatement(SEARCH_CARD);
                        pst.setString(1, id);
                        ResultSet rs = pst.executeQuery();

                        if(rs.next())
                        {
                            String stokKodu = rs.getString(1);
                            String stokAdi = rs.getString(2);
                            String stokTipi = rs.getString(3);
                            String birimi = rs.getString(4);
                            String barkodu = rs.getString(5);
                            String kdvTipi = rs.getString(6);
                            String aciklama = rs.getString(7);
                            String olusturmaTarihi = rs.getString(8);


                            stokKoduTxt.setText(stokKodu);
                            stokAdiTxt.setText(stokAdi);
                            barkodTxt.setText(barkodu);
                            aciklamaTxtArea.setText(aciklama);
                            tarihDate.setText(olusturmaTarihi);


                            switch (stokTipi)
                            {
                                case "1":
                                    stokTipiCb.setSelectedIndex(0);
                                    break;
                                case "2":
                                    stokTipiCb.setSelectedIndex(1);
                                    break;
                                 case "3":
                                    stokTipiCb.setSelectedIndex(2);
                                     break;
                                 case "4":
                                    stokTipiCb.setSelectedIndex(3);
                                    break;
                                 case "5":
                                    stokTipiCb.setSelectedIndex(4);
                                    break;

                            }


                            switch (birimi)
                            {
                                case "birim1":
                                    birimCb.setSelectedIndex(0);
                                    break;
                                case "birim2":
                                    birimCb.setSelectedIndex(1);
                                    break;
                                 case "birim3":
                                    birimCb.setSelectedIndex(2);
                                    break;
                                case "birim4":
                                     birimCb.setSelectedIndex(3);
                                    break;
                                 case "birim5":
                                    birimCb.setSelectedIndex(4);
                                    break;
                            }
                            switch (kdvTipi)
                            {
                                case "8.0":
                                    kdvCb.setSelectedIndex(0);
                                    break;
                                case "16.0":
                                    kdvCb.setSelectedIndex(1);
                                    break;
                            }
                        }
                        else
                        {

                            stokKoduTxt.setText("");
                            stokAdiTxt.setText("");
                            stokTipiCb.setSelectedIndex(0);
                            birimCb.setSelectedIndex(0);
                            barkodTxt.setText("");
                            kdvCb.setSelectedIndex(0);
                            aciklamaTxtArea.setText("");
                            tarihDate.setText("");
                            JOptionPane.showMessageDialog(null,"Aradığınız Stok Kodu bulunamamıştır.");
                        }
                    }
                    catch (SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        });

        guncelleButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(stokKoduTxt.getText().isEmpty() || stokAdiTxt.getText().isEmpty() || barkodTxt.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Lütfen güncellenecek bir stok kartı seçin!","HATA",JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    String stokKodu = stokKoduTxt.getText();
                    String  stokAdi = stokAdiTxt.getText();
                    String  birimi = birimCb.getSelectedItem().toString();
                    String  barkodu = barkodTxt.getText();
                    String aciklama = aciklamaTxtArea.getText();

                    int stokTipi=  Integer.valueOf(stokTipiCb.getSelectedItem().toString());
                    double kdv = Double.valueOf(kdvCb.getSelectedItem().toString());

                    islemler.UpdateCard(stokKodu,stokAdi,stokTipi,birimi,barkodu,kdv,aciklama);
                    islemler.TableLoad(kartTable);

                    stokKoduTxt.setText("");
                    stokAdiTxt.setText("");
                    stokTipiCb.setSelectedIndex(0);
                    birimCb.setSelectedIndex(0);
                    barkodTxt.setText("");
                    kdvCb.setSelectedIndex(0);
                    aciklamaTxtArea.setText("");

                    stokKoduTxt.requestFocus();
                }
            }
        });

        kopyalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                JFrame f = new JFrame();

                String stokKodu;

                String stokAdi = stokAdiTxt.getText();
                String birimi = birimCb.getSelectedItem().toString();
                String barkodu = barkodTxt.getText();
                String aciklama = aciklamaTxtArea.getText();
                int stokTipi=  Integer.valueOf(stokTipiCb.getSelectedItem().toString());
                double kdv = Double.valueOf(kdvCb.getSelectedItem().toString());

                JTextField stkd = new JTextField();
                String message = "Stok Kodunu Girin:";
                if(stokAdiTxt.getText().isEmpty() || barkodTxt.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null,"Lütfen kopyalanacak bir stok kartı seçin!","HATA",JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    int result = JOptionPane.showOptionDialog(f, new Object[] {message, stkd},
                            "STOK KARTI KOPYALAMA", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

                    if (result == JOptionPane.OK_OPTION)
                    {
                        if (stkd.getText().isEmpty())
                        {
                            JOptionPane.showMessageDialog(null,"Lütfen stok kodunu girin!","HATA",JOptionPane.WARNING_MESSAGE);
                        }

                        else
                        {
                            stokKodu = stkd.getText();
                            islemler.CopyCard(stokKodu,stokAdi,stokTipi,birimi,barkodu,kdv,aciklama);
                            islemler.TableLoad(kartTable);

                            stokKoduTxt.setText("");
                            stokAdiTxt.setText("");
                            birimCb.setSelectedIndex(0);
                            barkodTxt.setText("");
                            aciklamaTxtArea.setText("");
                            tarihDate.setText("");
                            stokTipiCb.setSelectedIndex(0);
                            kdvCb.setSelectedIndex(0);
                            stokKoduTxt.requestFocus();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Kopyalama iptal edilmiştir.","İPTAL",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }


        });
        stokKoduArama.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                super.keyTyped(e);
                DefaultTableModel model = (DefaultTableModel) kartTable.getModel();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
                kartTable.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(stokKoduArama.getText().trim()));
            }
        });
    }
}
