# CodeLearnApp — Guide de finalisation

Ce zip contient le code déjà écrit (frontend RN, backend Spring Boot, workflows CI/CD).
Il manque uniquement ce qui DOIT être généré par les outils officiels (dossier `android/`
natif). Suis ces étapes dans l'ordre, dans Termux.

---

## 1. Créer le repo GitHub

Sur github.com → New repository → `codelearn-app`. Ne coche aucune option
(pas de README, pas de .gitignore) : on les a déjà dans ce zip.

## 2. Préparer Termux

```bash
pkg install nodejs-lts openjdk-17 git -y
```

## 3. Générer le squelette React Native natif

```bash
cd ~
npx @react-native-community/cli init CodeLearnApp --version latest
```

Ignore les erreurs liées à CocoaPods/iOS (normal sur Termux, pas de Mac).
Vérifie que `~/CodeLearnApp/android/build.gradle` existe bien.

## 4. Fusionner avec les fichiers de ce zip

```bash
cd ~/CodeLearnApp
rm -rf App.js src   # on remplace par nos versions
```

Copie depuis ce zip vers `~/CodeLearnApp/` :
- `frontend/App.js` → `~/CodeLearnApp/App.js`
- `frontend/src/` → `~/CodeLearnApp/src/`

Puis installe la dépendance manquante :
```bash
npm install react-native-webview
```

## 5. Générer le keystore de signature

```bash
cd android/app
keytool -genkeypair -v -storetype PKCS12 \
  -keystore release.keystore \
  -alias codelearn-key \
  -keyalg RSA -keysize 2048 -validity 10000
```

Note bien le mot de passe que tu choisis (storePassword = keyPassword si tu veux simplifier).

```bash
base64 -w 0 release.keystore > ~/keystore_base64.txt
cat ~/keystore_base64.txt
```

Copie tout le texte affiché (une seule ligne longue).

## 6. Appliquer le bloc de signature

Ouvre `android/app/build.gradle` et applique les modifications décrites dans
`android_setup/signing_block_to_insert.gradle` (fourni dans ce zip).

## 7. Réorganiser en structure monorepo

Ce zip est structuré en `frontend/` + `backend/` (pour que les deux workflows
CI/CD ne se déclenchent que sur les changements pertinents). Donc :

```bash
mkdir -p ~/codelearn-app
mv ~/CodeLearnApp ~/codelearn-app/frontend
```

Puis copie depuis ce zip vers `~/codelearn-app/` :
- `backend/` (dossier complet)
- `.github/` (dossier complet)
- `.gitignore`
- `README.md` (celui-ci, optionnel de le garder)

Structure finale attendue :
```
codelearn-app/
├── .github/workflows/
│   ├── build-apk.yml
│   └── build-backend.yml
├── .gitignore
├── frontend/         (React Native, généré + fusionné)
│   ├── android/
│   ├── App.js
│   ├── src/
│   └── package.json
└── backend/          (Spring Boot)
    ├── pom.xml
    ├── Dockerfile
    └── src/
```

## 8. Pousser sur GitHub

```bash
cd ~/codelearn-app
git init
git remote add origin https://github.com/TON_USERNAME/codelearn-app.git
git add .
git commit -m "Initial scaffold: frontend RN + backend Spring Boot + CI/CD"
git branch -M main
git push -u origin main
```

Utilise un Personal Access Token GitHub comme mot de passe si demandé
(Settings → Developer settings → Personal access tokens).

## 9. Ajouter les secrets GitHub Actions

Repo → Settings → Secrets and variables → Actions → New repository secret :
- `KEYSTORE_BASE64` → contenu de `~/keystore_base64.txt`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS` → `codelearn-key`
- `KEY_PASSWORD`

(`GITHUB_TOKEN` pour le workflow backend est automatique, rien à faire.)

## 10. Vérifier les builds

Onglet **Actions** du repo :
- **Build Android APK** doit passer au vert → Artifacts → télécharge `app-release`.
- **Build & Push Backend Image** doit passer au vert → l'image est visible dans
  l'onglet **Packages** de ton profil GitHub (`ghcr.io/TON_USERNAME/codelearn-api`).

---

## Important : héberger le backend

Le workflow backend **build et publie une image Docker sur GHCR**, mais GitHub
Actions ne fait pas tourner de service en continu. Pour que l'API réponde à
l'app RN, il faut ensuite exécuter cette image quelque part (`docker pull` +
`docker run` sur un serveur/VPS que tu contrôles, avec un déploiement SSH
déclenché par le workflow si tu veux automatiser ça plus tard).

En attendant d'avoir cet hébergement, tu peux tester en local avec Termux :
```bash
# nécessite proot-distro ou un JDK Termux avec Maven — sinon build via CI
# et scp/docker load l'image sur ta machine hôte pour tester en local réseau
```

---

## Débogage courant

- **Le workflow APK échoue sur `gradlew: permission denied`** → ajoute une étape
  `run: chmod +x android/gradlew` avant le build.
- **Erreur SDK Android manquant** → ajoute `- uses: android-actions/setup-android@v3`
  avant l'étape de build dans `build-apk.yml`.
- **`index.android.bundle` introuvable au runtime** → vérifie que le dossier
  `android/app/src/main/assets/` existe bien avant `npx react-native bundle`
  (le workflow le crée déjà avec `mkdir -p`, mais vérifie si tu as modifié la commande).
