import React, { useState, useCallback, useRef } from 'react';
import { View, TextInput, StyleSheet, KeyboardAvoidingView, Platform, Button, Text, ScrollView } from 'react-native';
import { WebView } from 'react-native-webview';

const DEFAULT_CODE = {
  html: '<h1>Bonjour !</h1>\n<p>Modifie ce code.</p>',
  css: 'h1 { color: #2563eb; font-family: sans-serif; }',
  js: "document.querySelector('h1').addEventListener('click', () => alert('Clic !'));",
};

// Adresse du backend Spring Boot. À adapter selon ton déploiement.
// - Emulateur Android : 10.0.2.2 pointe vers le localhost de ta machine hôte.
// - Device physique : remplace par l'IP locale de ta machine, ou l'URL publique du backend.
const API_BASE_URL = 'http://10.0.2.2:8080';

function buildDocument({ html, css, js }) {
  return `<!DOCTYPE html><html><head><style>${css}</style></head>
<body>${html}<script>${js}<\/script></body></html>`;
}

export default function LiveCodeScreen() {
  const [code, setCode] = useState(DEFAULT_CODE);
  const [srcDoc, setSrcDoc] = useState(buildDocument(DEFAULT_CODE));
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const timeoutRef = useRef(null);

  const updateField = useCallback((field, value) => {
    const next = { ...code, [field]: value };
    setCode(next);
    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    timeoutRef.current = setTimeout(() => setSrcDoc(buildDocument(next)), 400);
  }, [code]);

  const validateExercise = useCallback(async () => {
    setLoading(true);
    setResult(null);
    try {
      const res = await fetch(`${API_BASE_URL}/api/exercises/validate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          exerciseId: 1,
          html: code.html,
          css: code.css,
          js: code.js,
        }),
      });
      const data = await res.json();
      setResult(data);
    } catch (err) {
      setResult({ passed: false, messages: [`Erreur réseau : ${err.message}`], score: 0 });
    } finally {
      setLoading(false);
    }
  }, [code]);

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <View style={styles.editorPane}>
        <TextInput
          style={styles.input}
          multiline
          value={code.html}
          onChangeText={(v) => updateField('html', v)}
          placeholder="HTML"
          placeholderTextColor="#666"
          autoCapitalize="none"
          autoCorrect={false}
        />
        <TextInput
          style={styles.input}
          multiline
          value={code.css}
          onChangeText={(v) => updateField('css', v)}
          placeholder="CSS"
          placeholderTextColor="#666"
          autoCapitalize="none"
          autoCorrect={false}
        />
        <TextInput
          style={styles.input}
          multiline
          value={code.js}
          onChangeText={(v) => updateField('js', v)}
          placeholder="JavaScript"
          placeholderTextColor="#666"
          autoCapitalize="none"
          autoCorrect={false}
        />
      </View>

      <WebView
        style={styles.preview}
        originWhitelist={['*']}
        source={{ html: srcDoc }}
        javaScriptEnabled
      />

      <View style={styles.actionBar}>
        <Button title={loading ? 'Validation...' : 'Valider'} onPress={validateExercise} disabled={loading} />
        {result && (
          <ScrollView style={styles.resultBox}>
            <Text style={[styles.resultTitle, { color: result.passed ? '#22c55e' : '#ef4444' }]}>
              {result.passed ? '✓ Exercice réussi' : '✗ Pas encore correct'} (score: {result.score})
            </Text>
            {result.messages && result.messages.map((m, i) => (
              <Text key={i} style={styles.resultMessage}>{m}</Text>
            ))}
          </ScrollView>
        )}
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#121212' },
  editorPane: { flex: 1.2, backgroundColor: '#1e1e1e' },
  input: {
    flex: 1,
    color: '#d4d4d4',
    fontFamily: Platform.OS === 'android' ? 'monospace' : 'Menlo',
    fontSize: 13,
    padding: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#333',
    textAlignVertical: 'top',
  },
  preview: { flex: 1 },
  actionBar: { maxHeight: 160, padding: 8, backgroundColor: '#1e1e1e' },
  resultBox: { marginTop: 8 },
  resultTitle: { fontWeight: 'bold', fontSize: 14, marginBottom: 4 },
  resultMessage: { color: '#d4d4d4', fontSize: 12, marginBottom: 2 },
});
