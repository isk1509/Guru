import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.sql.*;

public class StokKartiIslemleri {

    private Connection con = null;
    private PreparedStatement pst = null;

    private static final String GET_ALL_CARDS = "select * from StokKartlari";
    private static final String ADD_CARD = "insert into StokKartlari(Stok_Kodu,Stok_Adı,Stok_Tipi,Birimi,Barkodu,KDV_Tipi,Açıklama)values(?,?,?,?,?,?,?)";
    private static final String DELETE_CARD = "delete from StokKartlari  where Stok_Kodu = ?";
    private static final String UPDATE_CARD = "update StokKartlari set Stok_Kodu = ?,Stok_Adı = ?,Stok_Tipi = ?,Birimi = ?,Barkodu = ?,KDV_Tipi = ?,Açıklama = ? where Stok_Kodu = ?";
    private static final String COPY_CARD = "insert into StokKartlari(Stok_Kodu,Stok_Adı,Stok_Tipi,Birimi,Barkodu,KDV_Tipi,Açıklama)values(?,?,?,?,?,?,?)";


    public void Connect()
    {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db_ismi;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver bulunamadı...");
        }

        try
        {
            con = DriverManager.getConnection(url, Database.kullanci_adi, Database.parola);
            System.out.println("Bağlantı Başarılı...");
        } catch (SQLException ex)
        {
            System.out.println("Bağlantı başarısız...");
        }
    }

    public void TableLoad(JTable table)
    {
        try
        {
            pst = con.prepareStatement(GET_ALL_CARDS);
            ResultSet rs = pst.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void AddCard(String stokKodu,String stokAdi,int stokTipi,String birimi, String barkodu, double kdv,String aciklama)
    {
        try
        {
        pst = con.prepareStatement(ADD_CARD);
        pst.setString(1, stokKodu);
        pst.setString(2, stokAdi);
        pst.setInt(3, stokTipi);
        pst.setString(4, birimi);
        pst.setString(5, barkodu);
        pst.setDouble(6, kdv);
        pst.setString(7, aciklama);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(null, "Stok Kartı Başarıyla Kaydedildi.","KAYIT BAŞARILI",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e1)
        {
            JOptionPane.showMessageDialog(null,"Lütfen kayıtlı olmayan bir stok kodu giriniz.","HATA",JOptionPane.ERROR_MESSAGE);
        }

    }

    public void DeleteCard(String id)
    {
        try
        {
            pst = con.prepareStatement(DELETE_CARD);
            pst.setString(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Ürün Silindi");
        }

        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
    }


    public void UpdateCard(String stokKodu,String stokAdi,int stokTipi,String birimi, String barkodu, double kdv,String aciklama)
    {
        try
        {
        pst = con.prepareStatement(UPDATE_CARD);
        pst.setString(1, stokKodu);
        pst.setString(2, stokAdi);
        pst.setInt(3, stokTipi);
        pst.setString(4, birimi);
        pst.setString(5, barkodu);
        pst.setDouble(6, kdv);
        pst.setString(7, aciklama);
        pst.setString(8, stokKodu);

        pst.executeUpdate();

        JOptionPane.showMessageDialog(null, "Stok Kartı Başarıyla Güncellendi","GÜNCELLEME BAŞARILI",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null,"Lütfen kayıtlı olmayan bir stok kodu giriniz.","HATA",JOptionPane.WARNING_MESSAGE);
        }

    }

    public void CopyCard(String stokKodu,String stokAdi,int stokTipi,String birimi, String barkodu, double kdv,String aciklama)
    {
        try
        {

            pst = con.prepareStatement(COPY_CARD);
            pst.setString(1, stokKodu);
            pst.setString(2, stokAdi);
            pst.setInt(3, stokTipi);
            pst.setString(4, birimi);
            pst.setString(5, barkodu);
            pst.setDouble(6, kdv);
            pst.setString(7, aciklama);


            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Stok kartı başarıyla kopyalandı.","KOPYALAMA BAŞARILI",JOptionPane.INFORMATION_MESSAGE);

        }
        catch (SQLException e1)
        {
            JOptionPane.showMessageDialog(null,"Kopyalama iptal edilmiştir !\nLütfen kayıtlı olmayan bir stok kodu giriniz.","HATA",JOptionPane.WARNING_MESSAGE);
        }

    }


}
