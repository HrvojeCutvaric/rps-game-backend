# Upute za pokretanje aplikacije
## Preduvjeti za pokretanje aplikacije

Za uspješno pokretanje aplikacije potrebno je imati instalirano sljedeće:

### Opći preduvjeti

- Operacijski sustav koji podržava Docker i Docker Desktop (Linux, macOS ili Windows)

### Docker okruženje

- **Docker** (verzija 20.x ili novija)
- **Docker Compose** (verzija 2.x ili novija)

### Provjera instalacije

```bash
docker --version
docker compose version
```

### Android aplikacija

- **Android Studio**
- Android SDK (instaliran putem Android Studija)
- Android emulator ili fizički Android uređaj


## Pokretanje backend aplikacije i baze podataka

Backend aplikacija i PostgreSQL baza podataka pokreću se pomoću alata **Docker Compose**.

### Koraci

1. Pozicionirati se u **glavni direktorij projekta** (gdje se nalazi `docker-compose.yml`):

```bash
cd <glavni-direktorij-projekta>
```
2. Pokrenuti backend aplikaciju i bazu podataka pomoću alata Docker Compose sljedećom naredbom:

```bash
docker compose up --build -d
```
## Provjera rada backend aplikacije
Za provjeru ispravnog rada backend aplikacije može se koristiti endpoint za [health check](http://localhost:8080/health).

## Pokretanje Android aplikacije

Nakon što su backend i baza podataka pokrenuti, može se pokrenuti Android aplikacija.

### Koraci

1. Otvoriti **Android Studio**
2. Odabrati opciju **Open an existing project**
3. Odabrati direktorij: `android/`
4. Pričekati da se projekt sinkronizira (Gradle sync)
5. Pokrenuti aplikaciju:
   - Na emulatoru ili
   - Na fizičkom Android uređaju
  
Nakon što je aplikacija pokrenuta na Android uređaju može se početi koristi.

## Zaustavljanje aplikacije

Za zaustavljanje backend aplikacije i baze podataka koristiti sljedeću naredbu u glavnom direktoriju projekta:
```bash
docker compose down
```




