import React, { useState, useCallback, useRef, useEffect } from 'react';
import { View, TextInput, StyleSheet, KeyboardAvoidingView, Platform, Pressable, Text, ScrollView, Animated } from 'react-native';
import { WebView } from 'react-native-webview';
import { EXERCISES } from '../data/exercises';
import AiChatPanel from '../components/AiChatPanel';
import { getOrCreateUserId } from '../services/userService';

const API_BASE_URL = 'https://codelearn-app-production-3ae0.up.railway.app';

function buildDocument({ html, css, js }) {
  return `<!DOCTYPE html><html><head><style>${css}</style></head>
<body>${html}<script>${js}<\/script></body></html>`;
}

export default function LiveCodeScreen({ route, navigation }) {
  const exercise = route?.params?.exercise ?? EXERCISES[0];
  const chapterId = route?.params?.chapterId ?? null;

  const [code, setCode] = useState(exercise.defaultCode);
  const [srcDoc, setSrcDoc] = useState(buildDocument(exercise.defaultCode));
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [progressInfo, setProgressInfo] = useState(null);
  const [savingProgress, setSavingProgress] = useState(false);
  const timeoutRef = useRef(null);
  const buttonScale = useRef(new Animated.Value(1)).current;
  const resultAnim = useRef(new Animated.Value(0)).current;

  const updateField = useCallback((field, value) => {
    const next = { ...code, [field]: value };
    setCode(next);
    if (timeoutRef.current) clearTimeout(timeoutRef.current);
    timeoutRef.current = setTimeout(() => setSrcDoc(buildDocument(next)), 400);
  }, [code]);

  useEffect(() => {
    if (result) {
      resultAnim.setValue(0);
      Animated.timing(resultAnim, {
        toValue: 1,
        duration: 350,
        useNativeDriver: true,
      }).start();
    }
  }, [result]);

  useEffect(() => {
    if (result?.passed) {
      saveProgress();
    }
  }, [result]);

  const onPressIn = () => {
    Animated.spring(buttonScale, { toValue: 0.97, useNativeDriver: true }).start();
  };
  const onPressOut = () => {
    Animated.spring(buttonScale, { toValue: 1, useNativeDriver: true }).start();
  };

  const validateExercise = useCallback(async () => {
    setLoading(true);
    setResult(null);
    setProgressInfo(null);
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

  const saveProgress = async () => {
    try {
      setSavingProgress(true);
      const userId = await getOrCreateUserId();
      if (!userId) return;
      const res = await fetch(`${API_BASE_URL}/api/progress/complete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          userId,
          exerciseId: exercise.id,
          score: result?.score ?? 0,
        }),
      });
      if (!res.ok) return;
      const data = await res.json();
      setProgressInfo(data);
    } catch (err) {
      // silencieux : la progression pourra se resynchroniser au retour sur le chemin
    } finally {
      setSavingProgress(false);
    }
  };

  const handleNext = () => {
    // Retour au chemin du chapitre : LessonDetail se charge d'afficher la leçon suivante
    navigation.navigate('ChapterPath', { chapterId });
  };

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
        <AiChatPanel exerciseContext={`${exercise.title} : ${exercise.description}`} />

        <Animated.View style={{ transform: [{ scale: buttonScale }] }}>
          <Pressable
            style={styles.button}
            onPress={validateExercise}
            onPressIn={onPressIn}
            onPressOut={onPressOut}
            disabled={loading}
          >
            <Text style={styles.buttonText}>{loading ? 'Validation...' : 'Valider'}</Text>
          </Pressable>
        </Animated.View>

        {result && (
          <Animated.View
            style={[
              styles.resultBox,
              {
                opacity: resultAnim,
                transform: [
                  { translateY: resultAnim.interpolate({ inputRange: [0, 1], outputRange: [12, 0] }) },
                ],
              },
            ]}
          >
            <ScrollView>
              <Text style={[styles.resultTitle, { color: result.passed ? '#22c55e' : '#ef4444' }]}>
                {result.passed ? '✓ Exercice réussi' : '✗ Pas encore correct'} (score: {result.score})
              </Text>
              {result.messages && result.messages.map((m, i) => (
                <Text key={i} style={styles.resultMessage}>{m}</Text>
              ))}
              {result.passed && progressInfo && (
                <Text style={styles.xpText}>+{progressInfo.xpEarned} XP · Niveau {progressInfo.level}</Text>
              )}
            </ScrollView>
          </Animated.View>
        )}

        {result?.passed && (
          <Pressable
            style={[styles.nextButton, savingProgress && styles.nextButtonDisabled]}
            onPress={handleNext}
            disabled={savingProgress}
          >
            <Text style={styles.nextButtonText}>
              {savingProgress
                ? 'Enregistrement...'
                : progressInfo?.chapterCompleted
                ? 'Chapitre terminé 🎉'
                : 'Continuer →'}
            </Text>
          </Pressable>
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
  actionBar: { maxHeight: 380, padding: 8, backgroundColor: '#1e1e1e' },
  button: {
    backgroundColor: '#2563eb',
    borderRadius: 8,
    paddingVertical: 12,
    alignItems: 'center',
  },
  buttonText: { color: '#fff', fontWeight: '600', fontSize: 15 },
  resultBox: { marginTop: 8, maxHeight: 100 },
  resultTitle: { fontWeight: 'bold', fontSize: 14, marginBottom: 4 },
  resultMessage: { color: '#d4d4d4', fontSize: 12, marginBottom: 2 },
  xpText: { color: '#fbbf24', fontSize: 13, fontWeight: '600', marginTop: 4 },
  nextButton: {
    backgroundColor: '#22c55e',
    borderRadius: 8,
    paddingVertical: 12,
    alignItems: 'center',
    marginTop: 8,
  },
  nextButtonDisabled: { opacity: 0.6 },
  nextButtonText: { color: '#fff', fontWeight: '700', fontSize: 15 },
});
