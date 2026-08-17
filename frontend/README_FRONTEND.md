# Frontend — instructions d'intégration

Ce dossier ne contient QUE le code applicatif (App.js, src/). Il manque volontairement :
- `package.json`, `index.js`, `metro.config.js`, `babel.config.js`
- Le dossier `android/` (Gradle, manifest, gradle-wrapper.jar...)
- Le dossier `ios/`

Ces fichiers doivent être générés par la CLI officielle React Native, car ils contiennent
des binaires et des configurations liées à ta version exacte de React Native / Node / Java.
Les copier à la main depuis un zip mène presque toujours à des versions incompatibles entre
elles (cause n°1 des builds Gradle qui échouent).

## Étapes

1. Dans Termux :
   ```bash
   npx @react-native-community/cli init CodeLearnApp --version latest
   cd CodeLearnApp
   ```
2. Supprime le `App.js` et le dossier `src/` générés par défaut.
3. Copie PAR-DESSUS les fichiers `App.js` et `src/` fournis dans ce zip.
4. Installe la dépendance WebView :
   ```bash
   npm install react-native-webview
   ```
5. Continue avec les étapes du dossier `android_setup/` de ce zip (keystore, signingConfigs).
