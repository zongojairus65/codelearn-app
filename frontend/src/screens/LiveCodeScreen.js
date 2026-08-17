import React, { useState, useCallback, useRef } from 'react';
import { View, TextInput, StyleSheet, KeyboardAvoidingView, Platform, Pressable, Text, ScrollView } from 'react-native';
import { WebView } from 'react-native-webview';
import Animated, { FadeInUp, FadeIn } from 'react-native-reanimated';
import { EXERCISES } from '../data/exercises';

const API_BASE_URL = 'https://codelearn-app-production-3ae0.up.railway.app';

function buildDocument({ html, css, js }) {
  return `<!DOCTYPE html><html><head><style>${css}</style></head>
<body>${html}<script>${js}<\/script></body></html>`;
}

export default function LiveCodeScreen({ route }) {
  const exercise = route?.params?.exercise ?? EXERCISES[0];
  const [code, setCode] = useState(exercise.defaultCode);
  const [srcDoc, setSrcDoc] = useState(buildDocument(exercise.defaultCode));
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
          exerciseId: exercise.id,
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
  }, [code, exercise.id]);

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
        <Pressable
          style={({ pressed }) => [styles.button, pressed && styles.buttonPressed]}
          onPress={validateExercise}
          disabled={loading}
        >
          <Text style={styles.buttonText}>{loading ? 'Validation...' : 'Valider'}</Text>
        </Pressable>

        {result && (
          <Animated.View entering={FadeInUp.duration(300)} style={styles.resultBox}>
            <ScrollView>
              <Animated.Text
                entering={FadeIn.duration(400)}
                style={[styles.resultTitle, { color: result.passed ? '#22c55e' : '#ef4444' }]}
              >
                {result.passed ? '✓ Exercice réussi' : '✗ Pas encore correct'} (score: {result.score})
              </Animated.Text>
              {result.messages && result.messages.map((m, i) => (
                <Animated.Text
                  key={i}
                  entering={FadeInUp.delay(i * 60).duration(300)}
                  style={styles.resultMessage}
                >
                  {m}
                </Animated.Text>
              ))}
            </ScrollView>
          </Animated.View>
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
  actionBar: { maxHeight: 200, padding: 8, backgroundColor: '#1e1e1e' },
  button: {
    backgroundColor: '#2563eb',
    borderRadius: 8,
    paddingVertical: 12,
    alignItems: 'center',
  },
  buttonPressed: {
    backgroundColor: '#1d4ed8',
    transform: [{ scale: 0.98 }],
  },
  buttonText: { color: '#fff', fontWeight: '600', fontSize: 15 },
  resultBox: { marginTop: 8 },
  resultTitle: { fontWeight: 'bold', fontSize: 14, marginBottom: 4 },
  resultMessage: { color: '#d4d4d4', fontSize: 12, marginBottom: 2 },
});
