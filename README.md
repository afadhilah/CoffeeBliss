# Coffee Bliss - Aplikasi Digital Membership

Aplikasi **Coffee Bliss Digital Membership** adalah aplikasi Android modern yang dikembangkan untuk menggantikan sistem kartu member fisik (kertas/plastik) menjadi kartu member digital berbasis aplikasi. Aplikasi ini membantu pemilik toko kopi Coffee Bliss dalam mengelola loyalitas pelanggan, mencatat transaksi penjualan, melacak perolehan poin secara otomatis, serta menyediakan sistem penukaran poin hadiah (rewards) langsung dari genggaman pengguna.

---

## Fitur Utama

1. **Registrasi Member Baru**: Pendaftaran modern dengan validasi email secara *real-time* sebelum disimpan ke database lokal.
2. **Daftar Member & Pencarian**: Halaman utama yang menampilkan daftar seluruh anggota aktif dilengkapi pencarian interaktif.
3. **Digital Membership Card**: Kartu anggota digital eksklusif dengan ID unik (`CB-XXXXX`), tingkatan keanggotaan (*Tiering*), sisa poin, dan generator kode QR dinamis.
4. **Kalkulasi Poin Otomatis**: Setiap transaksi kelipatan Rp10.000 otomatis dihitung menjadi 1 poin (pembulatan ke bawah).
5. **Riwayat Transaksi & Log Hadiah**: Pencatatan riwayat pembelian dan penukaran poin secara transparan untuk mempermudah verifikasi barista.
6. **Penukaran Poin (Redeem Rewards)**: Menu interaktif untuk menukarkan poin dengan produk menu spesial (Espresso, Cappuccino, Latte) dengan proteksi jumlah poin secara dinamis.

---

## Tumpukan Teknologi (Tech Stack)

* **Bahasa Pemrograman**: Kotlin (v1.9+)
* **UI Framework**: Jetpack Compose (Material 3) dengan tema kustom CoffeeBliss.
* **Database Lokal**: Room Database (ORM untuk SQLite).
* **Pola Arsitektur**: MVVM (Model-View-ViewModel) dengan StateFlow untuk reaktivitas UI.
* **Navigasi**: Navigation Compose untuk alur navigasi antar-layar yang *type-safe*.
* **Utilitas**: Custom QR Code Bitmap Generator (berjalan 100% luring).

---

## Skema Database (Room Entities)

Aplikasi ini menggunakan tiga tabel database yang saling berelasi:

### 1. Tabel `members`
* `id` (INTEGER, Primary Key, Auto-Increment)
* `name` (TEXT)
* `email` (TEXT)
* `phone` (TEXT)
* `points` (INTEGER)

### 2. Tabel `transactions`
* `id` (INTEGER, Primary Key, Auto-Increment)
* `memberId` (INTEGER, Foreign Key terelasi ke `members.id`)
* `amount` (REAL)
* `pointEarned` (INTEGER)
* `date` (TEXT)

### 3. Tabel `redeems`
* `id` (INTEGER, Primary Key, Auto-Increment)
* `memberId` (INTEGER, Foreign Key terelasi ke `members.id`)
* `rewardName` (TEXT)
* `pointsDeducted` (INTEGER)
* `date` (TEXT)

---

## Aturan Bisnis (Business Logic)

* **Skema Member Tier**:
  * **Bronze**: 0 - 49 Poin
  * **Silver**: 50 - 99 Poin
  * **Gold**: $\ge$ 100 Poin
* **Opsi Penukaran Hadiah**:
  * **Espresso**: 50 Poin
  * **Cappuccino**: 100 Poin
  * **Latte Gratis**: 150 Poin

---


## Cara Menjalankan Aplikasi

1. Buka folder proyek ini menggunakan **Android Studio** (Koala atau yang lebih baru).
2. Tunggu proses sinkronisasi Gradle selesai.
3. Jalankan aplikasi menggunakan Emulator Android (API level 26+) atau perangkat fisik Android yang terhubung.
4. Untuk melihat Laporan Proyek atau UI Showcase Mockup, cukup klik dua kali berkas `laporan_proyek.html` atau `index.html` untuk membukanya langsung di peramban web (*browser*) Anda.
