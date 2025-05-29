# todo-app

## 🚀 Teknolojiler

### ✅ Ana Teknolojiler
![Java](https://img.shields.io/badge/Java-17-blue)  
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-brightgreen)  
![Spring Data Couchbase](https://img.shields.io/badge/Spring_Data_Couchbase-3.5.0-green)  
![Couchbase](https://img.shields.io/badge/Couchbase-5.5.0-darkred)  
![Couchbase Server](https://img.shields.io/badge/Couchbase_Server-7.6.2-orange)  
![Docker](https://img.shields.io/badge/Docker-24.0-blue?logo=docker)

### ☁️ Couchbase Bağlantısı
- Host: `localhost` (Docker için varsayılan localhost:8091 portu) (host.docker.internal)
- Port: `8091` (Couchbase Server Web Konsolu)
- Bucket: `todo-bucket`

### 🔐 Güvenlik & Veri Dönüşümü
![JJWT](https://img.shields.io/badge/JJWT-0.12.6-lightgrey)  
![MapStruct](https://img.shields.io/badge/MapStruct-1.6.3-lightgrey)

### 📘 API Dokümantasyonu
![Springdoc](https://img.shields.io/badge/Springdoc-2.8.8-orange)  
![Swagger Core](https://img.shields.io/badge/Swagger_Core-2.2.30-orange)

### ⚙️ Build & Test
![Maven](https://img.shields.io/badge/Maven-3.8.1-purple)  
![JUnit Jupiter](https://img.shields.io/badge/JUnit_Jupiter-5.12.2-yellow)  
![Mockito Core](https://img.shields.io/badge/Mockito_Core-5.17.0-yellow)  
![Mockito JUnit Jupiter](https://img.shields.io/badge/Mockito_JUnit_Jupiter-5.17.0-yellow)

---

## Proje Hakkında
**todo-app**, Spring Boot ve Couchbase tabanlı, REST mimarisi kullanan bir To-Do List backend uygulamasıdır. 
Proje Dockerize edilmiştir ve Docker Hub üzerinde yayınlanmıştır.(**https://hub.docker.com/repository/docker/buraktufekeri123456/todo-app**)  
Kullanıcılar RESTful API’ler aracılığıyla sisteme kayıt olabilir, giriş yapabilir ve kendi yapılacaklar listelerini oluşturup yönetebilirler.  
Kimlik doğrulama JWT ile sağlanırken, yetkilendirme Spring Security altyapısıyla **rol ve role bağlı eylemler (actions)** düzeyinde gerçekleştirilir.  
Springdoc/OpenAPI entegrasyonu sayesinde Swagger UI üzerinden tüm REST endpointleri detaylı bir şekilde test edilebilir.

## Önemli Bilgi
- Bu uygulama **Couchbase Server'ı içermez**.
- Uygulama, çalışırken **harici bir Couchbase Server'a bağlanır**.
- Bu nedenle, uygulamayı çalıştırmadan önce bir Couchbase Server'ın erişilebilir olması gerekir.

## Roller ve Yetkilendirme
### Projede 2 ana rol bulunmaktadır
| Roller       | Aksiyonlar                                                   |
|--------------|--------------------------------------------------------------|
| `ROLE_USER`  | CREATE_TODO, READ_OWN_TODO, UPDATE_OWN_TODO, DELETE_OWN_TODO |
| `ROLE_ADMIN` | READ_ALL_TODOS, DELETE_ANY_TODO, MANAGE_USERS                |  

## Projedeki Temel Gereksinimler
- Kullanıcı kayıt ve giriş işlemleri
- To-Do listelerinin oluşturulması, güncellenmesi, silinmesi ve sorgulanması
- Her kullanıcı kendi listelerine erişebilir, admin tüm listeyi görebilir
- JWT tabanlı güvenlik ve yetkilendirme
- Birim testler ile kod kalitesinin kontrolü
- Docker desteği

## Başlangıç Verileri (Initial Setup)
Uygulama ilk kez ayağa kalktığında aşağıdaki veriler otomatik olarak sisteme yüklenir
- Tanımlı tüm `Action` (yetki eylemleri) değerleri `actionService.saveActions()` ile kaydedilir.
- Bu action'lar ilgili `Role`'lere atanır ve `roleService.saveRoles()` ile kaydedilir.
- Sistem yöneticisi (`admin`) kullanıcısı `userService.saveAdmin()` ile otomatik olarak oluşturulur.

Bu sayede, uygulama ilk açıldığında temel yetkilendirme sistemi ve yönetici hesabı hazır durumdadır.

## Kurulum ve Çalıştırma
### 1. Docker Compose ile Komple Çalıştırma (Önerilen)
- Bu yöntemle, hem Couchbase Server hem de uygulama container olarak çalışır.  
**Adımlar:**
1. Projeyi klonlayın:
   - git clone https://github.com/buraktufekeri/todo-app
   - cd todo-app
2. Docker Compose ile başlatın
   - docker compose up --build
3. Couchbase Admin paneline şu adresten erişebilirsiniz:
   - http://localhost:8091
   - Kullanıcı adı: admin
   - Şifre: password
4. Uygulama **http://localhost:8080** portunda çalışır.
5. Swagger linki: **http://localhost:8080/swagger-ui/index.html**

### 2.Docker Hub’dan Image Çekip Çalıştırma
- Eğer sadece backend uygulamasını çalıştırmak istiyorsanız:
  - Ön koşul:Bilgisayarınızda veya ağınızda erişilebilir bir Couchbase Server çalışıyor olmalı.
  - Docker Komutu
    docker run -e ADMIN_USERNAME=admin \
    -e ADMIN_PASSWORD=password \
    -e COUCHBASE_CONNECTION_STRING=host.docker.internal \
    -e COUCHBASE_USERNAME=admin \
    -e COUCHBASE_PASSWORD=password \
    -e COUCHBASE_BUCKET_NAME=todo-bucket \
    -e JWT_SECRET="SwIGrE3aJFgVaGvkdqzhrgzUV4YKSSzSbzaWvjQktH8=" \
    -p 8080:8080 \
    buraktufekeri123456/todo-app:latest

**Notlar**
- COUCHBASE_CONNECTION_STRING değişkeninde Couchbase server adresini kendi ortamınıza göre ayarlayın (localhost, host.docker.internal, vs.). 
- Eğer Couchbase server çalışmıyorsa veya erişilemiyorsa, uygulama başlatılamaz.
- Uygulama **todo-bucket** adlı bir bucket ile çalışır. Eğer Couchbase Server’ınızı yeni kurduysanız, bu bucket’ı oluşturduğunuzdan emin olun.

### 3.Diğer Çalıştırma Yöntemleri

#### Gereksinimler
- Java 17 yüklü olmalı
- Maven yüklü olmalı
- Couchbase Server kurulumu ve çalışıyor olmalı
- Gerekli environment değişkenleri ayarlanmalı

#### Environment Variables (Ortam Değişkenleri)
- Proje çalışma ve test aşamasında aşağıdaki ortam değişkenlerinin tanımlanması gerekir

| Değişken Adı                  | Açıklama                         | Örnek Değer                                  |
|-------------------------------|----------------------------------|----------------------------------------------|
| `ADMIN_USERNAME`              | Admin kullanıcı adı              | admin                                        |
| `ADMIN_PASSWORD`              | Admin şifresi                    | password                                     |
| `COUCHBASE_CONNECTION_STRING` | Couchbase bağlantı adresi        | localhost                                    |
| `COUCHBASE_USERNAME`          | Couchbase bağlantı kullanıcı adı | admin                                        |
| `COUCHBASE_PASSWORD`          | Couchbase bağlantı şifresi       | password                                     |
| `COUCHBASE_BUCKET_NAME`       | Couchbase bucket-name            | todo-bucket                                  |
| `JWT_SECRET`                  | JWT için gizli anahtar           | SwIGrE3aJFgVaGvkdqzhrgzUV4YKSSzSbzaWvjQktH8= |

- **Güvenlik Notu:** JWT için güvenli bir imzalama anahtarı (JWT Secret Key) oluşturmak için aşağıdaki komutu çalıştırabilirsiniz
- openssl rand -base64 32

#### Projeyi kaynak koddan direkt çalıştırmak için:
- ADMIN_USERNAME=admin ADMIN_PASSWORD=password COUCHBASE_CONNECTION_STRING=localhost COUCHBASE_USERNAME=admin COUCHBASE_PASSWORD=password COUCHBASE_BUCKET_NAME=todo-bucket JWT_SECRET=SwIGrE3aJFgVaGvkdqzhrgzUV4YKSSzSbzaWvjQktH8= mvn spring-boot:run

#### Alternatif olarak paket oluşturup jar ile çalıştırmak için:
- ADMIN_USERNAME=admin ADMIN_PASSWORD=password COUCHBASE_CONNECTION_STRING=localhost COUCHBASE_USERNAME=admin COUCHBASE_PASSWORD=password COUCHBASE_BUCKET_NAME=todo-bucket JWT_SECRET=SwIGrE3aJFgVaGvkdqzhrgzUV4YKSSzSbzaWvjQktH8= mvn clean install
- java -jar target/todo-app-0.0.1-SNAPSHOT.jar

#### Testlerin Çalıştırılması
##### Unit testleri Maven ile çalıştırmak için:
- ADMIN_USERNAME=admin ADMIN_PASSWORD=password COUCHBASE_CONNECTION_STRING=localhost COUCHBASE_USERNAME=admin COUCHBASE_PASSWORD=password COUCHBASE_BUCKET_NAME=todo-bucket JWT_SECRET=SwIGrE3aJFgVaGvkdqzhrgzUV4YKSSzSbzaWvjQktH8= mvn test

## API Dokümantasyonu (Swagger)
### Projeyi çalıştırdıktan sonra Swagger UI aşağıdaki adreste açılabilir
- http://localhost:8080/swagger-ui/index.html
- Burada tüm endpointler ve modeller detaylı olarak incelenebilir.

### İletişim
- Bu proje Burak Tüfekeri tarafından geliştirilmiştir.
- Herhangi bir sorunuz ya da öneriniz için iletişime geçebilirsiniz.
- **Email:** buraktufekeri@gmail.com
- **GitHub:** https://github.com/buraktufekeri/todo-app
- **Docker:** https://hub.docker.com/repository/docker/buraktufekeri123456/todo-app
