package com.bh183.NiaAntari;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.constraintlayout.solver.ArrayRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_Film";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_AKTOR= "Aktor";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_SINOPSIS = "Sinopsis";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFromat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_AKTOR + " TEXT, "
                + KEY_GENRE+ " TEXT, " + KEY_SINOPSIS + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());

        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());
        db.insert(TABLE_FILM, null, cv);
    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL,  dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format( dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR,  dataFilm.getAktor ());
        cv.put(KEY_GENRE,  dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS,  dataFilm.getSinopsis ());
        cv.put(KEY_LINK,  dataFilm.getLink());

        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm ())});

        db.close();
    }

    public void  hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try {
                    tempDate = sdFromat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Film tempFilm = new Film (
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }

        return dataFilm;

    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return  location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //Menambah data film ke-1
        try {
            tempDate = sdFromat.parse("15/08/2019 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }



        Film film1 = new Film (
                idFilm,
                "BUMI MANUSIA",
                tempDate,
                storeImageFile(R.drawable.film1),
                  "Iqbaal Ramadhan sebagai Minke\n" +
                        "Mawar Eva de Jongh sebagai Annelies\n" +
                        "Sha Ine Febriyanti sebagai Ontosoroh/Sanikem\n" +
                        "Amanda Khairunnisa sebagai Sanikem muda\n" +
                        "Giorgino Abraham sebagai Robert Mellema\n" +
                        "Bryan Domani sebagai Jan Dapperste/Panji Darman\n" +
                        "Jerome Kurnia sebagai Robert Suurhof\n" +
                        "Donny Damara sebagai Bupati B, ayah Minke\n" +
                        "Ayu Laksmi sebagai Ibu Minke\n" +
                        "Dewi Irawan sebagai Mevrouw Telinga\n" +
                        "Chew Kin Wah sebagai Ah Tjong\n" +
                        "Kelly Tandiono sebagai maiko\n" +
                        "Christian Sugiono sebagai Kommers\n" +
                        "Hans de Kraker sebagai Jean Marais\n" +
                        "Ciara Brosnan sebagai May Marais\n" +
                        "Edward Suhadi sebagai Gendut Sipit\n" +
                        "Jeroen Lezer sebagai Martinet\n" +
                        "Rob Hammink sebagai Maarten Nijman\n" +
                        "Tom de Jong sebagai Herbert de la Croix\n" +
                        "Peter Sterk sebagai Herman Mellema\n" +
                        "Salome van Gruinsven sebagai Miriam de la Croix\n" +
                        "Dorien Verdouw sebagai Sarah de la Croix\n" +
                        "Angelica Reitsma sebagai Magda Peters\n" +
                        "Ton Feil sebagai kepala HBS\n" +
                        "Whani Darmawan sebagai Darsam\n" +
                        "Robert Prein sebagai Maurits Mellema\n" +
                        "Derk Visser sebagai Sersan Hammerstee\n" +
                        "Arjan Onderdenwijngaard sebagai hakim pribumi\n" +
                        "Peter van Luijk sebagai Meneer Telinga\n" +
                        "Annisa Hertami sebagai Parjiyah",
                "Drama Sejarah",
                "Pada suatu hari di Surabaya, Minke diajak Robert Suurhof (Jerome Kurniawan) melawat ke rumah keluarga Mellema, Boerderij Buitenzorg di Wonokromo. Kedatangan Minke disambut dengan penuh kecurigaan oleh Robert Mellema (Giorgino Abraham) yang justru menyambut Suurhof dengan penuh keakraban, tetapi sebaliknya dengan adiknya Annelies Mellema (Mawar Eva de Jongh) serta ibunya Ontosoroh (Sha Ine Febriyanti) yang menerima Minke dengan gembira. Minke mulai menjalin hubungan mesra dengan Annelies dan Ontosoroh, walau Annelies sempat merasa belum terbiasa dengan Minke.\n" +
                        "\n" +
                        "Keesokan harinya, Minke yang saat itu bersekolah di Hogereburgerschool (HBS) berkhayal Ontosoroh menghampirinya ketika Magda Peters (Angelica Reitsma) menerangkan pelajaran, sehingga Magda menyadarkan Minke yang diikuti dengan tertawaan kawan-kawannya, termasuk Suurhof. Sepulang sekolah, Minke menghampiri kawannya berkebangsaan Prancis bernama Jean Marais (Hans de Kraker) yang melukis dan anaknya May Marais (Ciara Brosnan). Keesokan harinya, Annelies menceritakan kehidupan ibunya, Sanikem (Amanda Khairunnisa), yang kemudian mengganti namanya menjadi Ontosoroh. Minke terilhami dan menulis artikel di koran Surabaya dengan nama samaran Max Tollenaar. Malam harinya, Minke tiba-tiba ditangkap polisi karena tulisannya tempo hari yang lalu.\n" +
                        "\n" +
                        "Minke akhirnya kembali ke rumah dan disambut dengan kemarahan ayahnya (Donny Damara) karena berhubungan dengan Annelies; hubungan itu dinilai ayahnya meninggalkan budaya dan tradisi Jawa. Pada saat yang sama di Wonokromo, Ontosoroh menenangkan Annelies yang menangisi kepergian Minke, tetapi Annelies langsung pergi meninggalkan Ontosoroh.\n" +
                        "\n" +
                        "Kembali ke Wonokromo, Minke mulai dihadapkan dengan perkara yang sudah lama mengganggu hatinya, yang tak lain antara jurang pemisah antara kaum yang \"terperintah\" (bumiputra) dan \"memerintah\" (Eropa), serta hubungannya dengan Annelies. Keesokan harinya, ayah Minke diangkat menjadi bupati. Beberapa hari kemudian, Minke meninggalkan ayahnya ke rumah Annelies dan merasa dibuntuti Gendut Sipit (Edward Suhadi) di kereta api yang ditumpangi. Di sekolah, Magda menyatakan keingintahuannya akan Max Tollenaar, yang kemudian dibocorkan Suurhof, tetapi Magda justru memuji kepiawaian Minke dalam menulis. Suurhof yang merasa tidak terima dengan pujian Magda menghina Minke dan kemudian Panji Darman (Bryan Domani), yang dibalas dengan pukulan Panji. Karena perkelahian itu, kepala sekolah memanggil mereka.\n" +
                        "\n" +
                        "Annelies yang berkeliling pertanian tiba-tiba pingsan, sehingga Annelies dirawat Martinet. Minke tidur sekamar dan bersetubuh dengan Annelies. Keesokan harinya, ketika Minke bersiap ke sekolah, Martinet menyebut Minke bukanlah orang pertama yang menyetubuhi Annelies karena sebelumnya Robert pernah memperkosa Annelies. Ketika berangkat ke sekolah, Minke tiba-tiba meminta Darsam kembali ke rumah Annelies dan memutuskan menghabiskan waktu bersama Annelies di sana.\n" +
                        "\n" +
                        "Suatu hari, Gendut Sipit didapati penjaga rumah Annelies sedang memata-matai rumah itu, sehingga memancing Darsam, Minke, dan Annelies mengejarnya hingga rumah pelacuran. Di sana, Darsam menemukan Herman yang tewas karena keracunan dan maiko melarikan diri.\n" +
                        "\n" +
                        "Pada akhirnya, Minke harus mengikhlaskan keberangkatan Annelies ke Belanda yang disebabkan karena pernikahan Ontosoroh dan Herman diputuskan tidak sah oleh hakim pengadilan, sehingga Annelies harus diserahkan kepada walinya di Belanda. Beberapa hari kemudian, Minke yang membawa buku berdiri di depan tebing pantai, diiringi dengan senandika dari Minke.",
                "https://www.youtube.com/watch?v=2BYJaVz_wpM"
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data film ke-2
        try {
            tempDate = sdFromat.parse("25/01/2018 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film (
                idFilm,
                "Dilan 1990",
                tempDate,
                storeImageFile(R.drawable.film2),
                "Iqbaal Ramadhan sebagai Abdul Dilan\n" +
                        "Vanesha Prescilla sebagai Milea Adnan Hussain semasa SMA\n" +
                        "Sissy Priscillia sebagai Milea Adnan Hussain dewasa (narator)\n" +
                        "Giulio Parengkuan sebagai Anhar, teman geng Dilan.\n" +
                        "Omara Esteghlal sebagai Piyan, teman geng Dilan.\n" +
                        "Zulfa Maharani sebagai Rani, teman geng Milea.\n" +
                        "Yoriko Angeline sebagai Wati, teman geng Milea.\n" +
                        "Andryos Aryanto sebagai Nandan, teman geng Milea.\n" +
                        "Adhisty Zara (Zara JKT48) sebagai Disa, adik Dilan.\n" +
                        "Ira Wibowo sebagai bunda Dilan\n" +
                        "Farhan sebagai Kolonel Adnan, ayah Milea\n" +
                        "Happy Salma sebagai ibu Milea\n" +
                        "Brandon Salim sebagai Benni, pacar Milea di Jakarta.\n" +
                        "Teuku Rifnu Wikana sebagai Pak Suripto, guru BP.\n" +
                        "Refal Hady sebagai Kang Adi, guru les Milea.\n" +
                        "Moira Tabina Zayn (Moira) sebagai Airin, adik Milea.\n" +
                        "Gusti Rayhan sebagai Akew, sahabat Dilan.\n" +
                        "Yati Surachman sebagai Bi' Asih, tukang pijat.\n" +
                        "Tike Priatnakusumah sebagai Bi' E'em, penjaga kantin sekolah.\n" +
                        "Teddy Snada sebagai Kepala Sekolah\n" +
                        "Iang Darmawan sebagai Pak Rahmat\n" +
                        "Aris Nugraha sebagai Pak Atam\n" +
                        "Ridwan Kamil sebagai Pak Guru\n" +
                        "Joe Project P sebagai Mas Ato\n" +
                        "Ayu Inten sebagai Ibu Sri\n" +
                        "Ira Ratih sebagai Ibu Rini\n" +
                        "Polo Reza sebagai Burhan\n" +
                        "Ribka Uli Ozara Tambunan sebagai Susi\n" +
                        "Jubran Martawidjaja sebagai Agus\n" +
                        "Azzura Pinkania Imanda (Azzura Pinkan) sebagai Revi",
                "Drama Romantis",
                "Pada September 1990, Milea dan keluarganya pindah dari Jakarta ke Bandung. Saat hendak masuk di sebuah SMA, Milea bertemu dengan Dilan sang panglima geng motor. Dilan tak memperkenalkan dirinya, namun dengan sangat percaya diri segera meramal kalau Milea akan naik motor bersamanya dan menjadi pacarnya. Dilan, entah bagaimana caranya, mengetahui segala tentang Milea, bahkan alamat rumah dan nomor teleponnya. Singkat cerita, Dilan merayu-rayu Milea dengan memberikan berbagai hadiah yang bermakna, misalnya buku teka-teki silang yang sudah diisi supaya \"tidak perlu pusing karena harus mengisinya.\" Pada titik ini, Milea masih memiliki seorang pacar bernama Benni, yang ia tinggalkan secara fisik di Jakarta. Milea sendiri merasa tidak nyaman karena Benni adalah lelaki yang pencemburu dan kasar. Kepercayaan diri Dilan yang berlebih sempat membuat Nandan, sang sahabat yang juga menyukai Milea, tidak nyaman. Meski begitu, Milea mulai menyukai Dilan.\n" +
                        "\n" +
                        "Saat kelompok Milea maju ke lomba Cerdas Cermat antar sekolah yang dihelat di kantor pusat TVRI di Jakarta, tiba-tiba saja Benni muncul ke hadapan Milea. Milea, yang sedang makan berdua saja dengan Nandan karena ditinggal teman mereka yang pergi ke kamar mandi, terlibat cekcok dengan Benni yang mengira Nandan merusak hubungan asmaranya. Benni menghajar Nandan sebelum dilerai oleh Milea. Benni mengata-ngatai Milea dengan sebutan genit berkali-kali, sehingga Milea memutuskan hubungan mereka. Benni marah besar, melanjutkan makiannya dengan menyebut Milea \"pelacur\". Sekembalinya ke Bandung, Milea ditelepon Benni, yang kemudian memohon maaf. Milea sudah memaafkannya, namun menolak ajakan untuk kembali berpacaran. Benni mengeluarkan lagi makian \"setan\" dan \"pelacur\", yang dibalas Milea dengan menutup telepon. Setelahnya, hubungan Dilan dan Milea makin dekat saja, walau belum pernah ada kata cinta terucap. Mereka pulang sekolah berboncengan, sesekali bergandengan tangan, dan bertelepon malam-malam. Saking dekatnya, Milea berhasil membujuk Dilan supaya tidak lagi terlibat dalam tawuran antar geng.\n" +
                        "\n" +
                        "Milea juga membangun hubungan baik dengan bunda Dilan. Satu waktu, Milea ditunjuki kamar Dilan yang berantakan dan diajak merapikannya. Sang bunda juga menunjukkan puisi-puisi cinta yang dibuat Dilan untuk Milea.\n" +
                        "\n" +
                        "Milea harus menghadapi Kang Adi, mahasiswa yang merupakan guru les privatnya dan sang adik. Tampak bahwa Kang Adi menaksir Milea. Dia bahkan membawa Milea mengunjungi kampus tempatnya belajar, Institut Teknologi Bandung (ITB). Ketika Dilan mengetahui bahwa Milea pergi berdua dengan Kang Adi, ia mengirim puisi kekecewaan yang membuat Milea menyesal. Milea mencari Dilan ke rumahnya dan sekolah. Di sekolah ia bertemu Anhar, salah satu rekan geng Dilan, yang sedang mabuk. Anhar kemudian menamparnya. Setelah mengetahui peristiwa ini, Dilan menghajar Anhar habis-habisan. Mereka kemudian dilerai oleh guru BP dan kepala sekolah serta siswa-siswa lain. Mereka dan Milea pun dibawa ke ruang Kepala Sekolah. Setelah meninggalkan ruangan, Dilan dan Milea menuju warung Bi' E'em, di mana mereka resmi memulai hubungan pacaran.",
                "https://www.youtube.com/watch?v=X_b-wNkz4DU"
        );

        tambahFilm(film2, db);
        idFilm++;

        //Data film ke-3
        try {
            tempDate = sdFromat.parse("24/01/2019 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film (
                idFilm,
                "Orang Kaya Baru",
                tempDate,
                storeImageFile(R.drawable.film3),
                "Cut Mini sebagai Ibu\n" +
                        "Lukman Sardi sebagai Bapak\n" +
                        "Raline Shah sebagai Tika\n" +
                        "Derby Romero sebagai Duta\n" +
                        "Fatih Unru sebagai Dodi\n" +
                        "Refal Hady sebagai Bayu\n" +
                        "Verdi Solaiman sebagai Ilham Said (pengacara)\n" +
                        "Millane Fernandez sebagai Risha\n" +
                        "Melayu Nicole sebagai Sasha\n" +
                        "Tarzan sebagai Anto Suganda (politikus)\n" +
                        "Reza Nangin sebagai Rafi\n" +
                        "Dea Panendra sebagai Monika\n" +
                        "Rania Putrisari sebagai Linda\n" +
                        "Jasmine Kusuma Carroll sebagai Lala\n" +
                        "Arvitta Ludya sebagai Tiwi\n" +
                        "Sumaisy Djaitov Yanda sebagai penjaga keamanan\n" +
                        "Bintang sebagai Ardi\n" +
                        "Dorman Borisman sebagai pengajar teater\n" +
                        "Pak Tarno sebagai tukang es doger\n" +
                        "Lucky Perdana sebagai pengantin pria",
                "Komedii Keluarga",
                "Film ini mengisahkan sebuah keluarga yang mendadak menjadi kaya raya. Awalnya keluarga tersebut merupakan keluarga sederhana yang hidupnya pas-pasan namun tetap kompak. Lukman Sardi berperan sebagai Bapak, Cut Mini sebagai Ibu.\n" +
                        "\n" +
                        "Mereka memiliki tiga orang anak yang diperankan oleh Derby Romero sebagai Duta, Raline Shah sebagai Tika, dan Fatih Unru sebagai Dodi. Meskipun bukan keluarga kaya, mereka mampu menjalani hari dengan menyenangkan. Bapak selalu bahagia meski tidak punya uang.\n" +
                        "\n" +
                        "Ibu selalu memasak dan antar jemput Dodi menggunakan motor lama. Sementara Tika pulang dan pergi kuliah menggunakan metromini. Bahkan terkadang untuk urusan makan, Duta, Tika dan Dodi sampai masuk ke kondangan orang yang sebetulnya tidak mereka kenal.\n" +
                        "\n" +
                        "Kehidupan keluarga tersebut berubah ketika Bapak meninggal dunia. Bapak menjadi sosok panutan dalam keluarga tersebut. Bapak ternyata meninggalkan warisan harta yang cukup banyak kepada istri dan anaknya. Selama ini Bapak merahasiakan harta yang dimilikinya tersebut.\n" +
                        "\n" +
                        "Kemudian keluarga tersebut berubah menjadi 'Orang Kaya Baru' karena warisan peninggalan Bapak. Kehidupan mereka sangat bergelimang harta. Saat si bungsu ditegur seorang penjaga toko karena menyentuh banyak barang, Ibu lalu sanggup membeli semua barang yang disentuh tersebut. Apa yang mereka inginkan dengan mudah dapat mereka beli. " +
                        "Meski sudah menjadi kaya, masalah justru datang menghampiri keluarga tersebut.",
                "https://www.youtube.com/watch?v=ZY4clGa250c"
        );

        tambahFilm(film3, db);
        idFilm++;

        // Data film ke-4
        try {
            tempDate = sdFromat.parse("04/06/2019 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film4 = new Film (
                idFilm,
                "Hit & Run",
                tempDate,
                storeImageFile(R.drawable.film4),
                "Joe Taslim sebagai Tegar\n" +
                        "Jefri Nichol sebagai Jefri\n" +
                        "Tatjana Saphira sebagai Meisa\n" +
                        "Yayan Ruhian sebagai Choki\n" +
                        "Chandra Liow sebagai Lio\n" +
                        "Nadya Arina sebagai Mila",
                "Komedi Laga",
                "Tegar Saputra, seorang polisi selebriti yang kemana-mana selalu diikuti kamera karena memiliki acara reality shownya sendiri. " +
                        "Tegar ditugaskan untuk menangkap Coki (Yayan Ruhian), seorang gembong narkoba yang baru kabur dari penjara. Sayangnya, di misi kali ini " +
                        "Tegar yang individualis harus dipasangkan dengan Lio, seorang tukang tipu. Tegar yang terbiasa beraksi sendirian kini harus berusaha menyelesaikan misinya bersama Lio yang justru membuat susah. " +
                        "Aksi Tegar dan Lio mencari Coki ditemanin Meisa seorang penyanyi dan Jefri.",
                "https://www.youtube.com/watch?v=M1bk-POU7yA"
        );

        tambahFilm(film4, db);
        idFilm++;

        //Data film ke-5
        try {
            tempDate = sdFromat.parse ( "29/11/2018" );
        } catch (ParseException er) {
            er.printStackTrace ();
        }
        Film film5 = new Film (
                idFilm,
                "Keluarga Cemara",
                tempDate,
                storeImageFile ( R.drawable.film5 ),
                "Ringgo Agus Rahman sebagai Abah\n" +
                        "Nirina Zubir sebagai Emak\n" +
                        "Adhisty Zara (Zara JKT48) sebagai Euis\n" +
                        "Widuri Sasono sebagai Cemara\n" +
                        "Ariyo Wahab sebagai Fajar\n" +
                        "Asri Welas sebagai Ceu Salmah\n" +
                        "Joshia Frederico sebagai Andi\n" +
                        "Kafin Sulthan sebagai Deni\n" +
                        "Kawai Labiba M.A. sebagai Ima\n" +
                        "Yasamin Jasem sebagai Rindu\n" +
                        "Abdurrahman Arif sebagai Kang Romly\n" +
                        "Maudy Koesnaedi sebagai Tante Pressier\n" +
                        "Andrew Trigg sebagai Luc\n" +
                        "Melati Putri Rahel Sesilia (Melati JKT48) sebagai Bianca\n" +
                        "Eve Antoinette Ichwan (Eve JKT48) sebagai Via\n" +
                        "Citra Ayu Pranajaya (Citra Ayu) sebagai Fika\n" +
                        "Thalia Ivanka Elizabeth (Vanka JKT48) sebagai Diva\n" +
                        "Gading Marten sebagai Pak Mario, guru Bahasa Inggris.\n" +
                        "Aci Resti sebagai Nita, sekretaris Abah di PT Bangun Damai\n" +
                        "Widi Mulia sebagai guru seni SD Pertiwi\n" +
                        "Arief Didu sebagai J. K. Tobing, pengacara gugatan Abah\n" +
                        "Sumaisy Djaitov Yanda (Bang Tigor) sebagai Mandor",
                "Drama Keluarga",
                "Sebuah keluarga inti yang tinggal di Jakarta harus menghadapi kenyataan bahwa harta benda mereka ludes akibat ditipu salah satu anggota " +
                        "keluarga besar mereka. Pindah ke desa di Kabupaten Bogor, Abah dan keluarga harus beradaptasi dengan segala ketidaknyamanan yang tak pernah dialami sebelumnya. Permasalahan datang silih berganti, " +
                        "tetapi keluarga ini tetap bertahan dalam keadaan gegar budaya.",
                "https://www.youtube.com/watch?v=sGaeDzD_3o0"
        );

        tambahFilm (film5,db );

    }
}
