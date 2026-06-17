# 🚀 GitHub Actions - Build & Release

Dokumentasi untuk GitHub Actions workflows yang telah dikonfigurasi untuk project CatatanKeuanganPribadi.

## 📋 Workflows Available

### 1. **Build and Release APK** (`build-and-release.yml`)
Workflow untuk compile dan release app dengan GitHub Releases.

**Trigger:**
- Push ke branch: `main`, `master`, `develop`
- Push tag dengan format `v*` (contoh: `v1.0.0`)
- Manual trigger (workflow_dispatch)

**Output:**
- ✅ Release APK
- ✅ Release AAB (Android App Bundle)
- ✅ GitHub Release dengan artifacts

**Cara Menggunakan:**
```bash
# Tag untuk release
git tag v1.0.0
git push origin v1.0.0
```

---

### 2. **Build Debug APK** (`build-debug.yml`)
Workflow untuk compile debug APK di setiap push (untuk testing).

**Trigger:**
- Push ke semua branch
- Pull Request

**Output:**
- ✅ Debug APK
- ✅ Artifacts tersimpan 7 hari

**Cara Menggunakan:**
```bash
# Hanya push normal, akan auto-trigger
git push origin feature-branch
```

---

## 📦 Artifacts

### Release Build (Recommended untuk Production)
- **APK**: `app-release.apk` - Siap untuk upload ke Google Play Store
- **AAB**: `app-release.aab` - Format modern untuk Google Play Store

### Debug Build (Untuk Testing)
- **APK**: `app-debug.apk` - Untuk testing di device/emulator

---

## 🔍 Cara Download Artifacts

### Dari GitHub Release (untuk Release APK/AAB)
1. Buka repository di GitHub
2. Klik **Releases** di sidebar kanan
3. Pilih release yang diinginkan
4. Download APK/AAB dari Release Assets

### Dari Actions (untuk Debug APK)
1. Buka repository di GitHub
2. Klik **Actions** tab
3. Pilih workflow run yang diinginkan
4. Scroll ke bawah ke **Artifacts**
5. Download APK

---

## 🎯 Build Status

Monitor status build di **Actions** tab di GitHub repository Anda.

- 🟢 **Success** - Build berhasil
- 🔴 **Failed** - Ada error di build
- 🟡 **In Progress** - Sedang build

---

## 📝 Persyaratan

✅ **Sudah Dikonfigurasi:**
- Java 11 (via setup-java action)
- Android SDK (via setup-android action)
- Gradle caching untuk faster builds

✅ **File-file Penting:**
- `build.gradle.kts` - Build configuration
- `gradle.properties` - Gradle properties
- `local.properties` - Local Android SDK path (auto-generated)

---

## 🛠️ Customization

### Mengubah Trigger Events

Edit file workflow (`.github/workflows/build-and-release.yml`):

```yaml
on:
  push:
    branches:
      - main
      - custom-branch  # Tambah branch lainnya
  workflow_dispatch:
```

### Mengubah Output Path

Jika struktur build output berbeda, update paths di:

```yaml
- name: Upload APK
  uses: actions/upload-artifact@v4
  with:
    path: app/build/outputs/apk/release/app-release.apk
```

---

## ✅ Troubleshooting

| Problem | Solution |
|---------|----------|
| **Build Failed** | Check **Actions** logs untuk error details |
| **APK tidak ditemukan** | Pastikan `build.gradle.kts` sudah benar |
| **Permission Denied** | `gradlew` sudah set executable by action |
| **Gradle Cache Issues** | Cache akan auto-clear setelah beberapa runs |

---

## 📚 Dokumentasi Tambahan

- [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin)
- [GitHub Actions Docs](https://docs.github.com/actions)
- [Android GitHub Actions](https://github.com/android-actions)

---

**Last Updated**: 2026-04-28  
**Project**: CatatanKeuanganPribadi  
**Status**: ✅ Ready for Production
