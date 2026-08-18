import React, { useState, useRef } from 'react';
import { View, Text, TextInput, Pressable, StyleSheet, ScrollView, ActivityIndicator, Animated } from 'react-native';

const API_BASE_URL = 'https://codelearn-app-production-3ae0.up.railway.app';
const DIFFICULTIES = ['débutant', 'intermédiaire', 'avancé'];

export default function GenerateExerciseScreen({ navigation }) {
  const [topic, setTopic] = useState('');
  const [difficulty, setDifficulty] = useState('débutant');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [exercise, setExercise] = useState(null);
  const resultAnim = useRef(new Animated.Value(0)).current;

  const generate = async () => {
    if (!topic.trim() || loading) return;
    setLoading(true);
    setError(null);
    setExercise(null);
    try {
      const res = await fetch(`${API_BASE_URL}/api/ai/generate-exercise`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ topic: topic.trim(), difficulty }),
      });
      if (!res.ok) throw new Error('Erreur serveur');
      const data = await res.json();
      setExercise(data);
      resultAnim.setValue(0);
      Animated.timing(resultAnim, { toValue: 1, duration: 350, useNativeDriver: true }).start();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const startExercise = () => {
    navigation.navigate('LiveCode', {
      exercise: {
        id: Date.now(),
        title: exercise.title,
        description: exercise.description,
        defaultCode: { html: exercise.html, css: exercise.css, js: exercise.js },
      },
    });
  };

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.content}>
      <Text style={styles.label}>Sujet de l'exercice</Text>
      <TextInput
        style={styles.input}
        value={topic}
        onChangeText={setTopic}
        placeholder="ex: les formulaires, les animations CSS..."
        placeholderTextColor="#666"
      />

      <Text style={styles.label}>Difficulté</Text>
      <View style={styles.difficultyRow}>
        {DIFFICULTIES.map((d) => (
          <Pressable
            key={d}
            style={[styles.difficultyChip, difficulty === d && styles.difficultyChipActive]}
            onPress={() => setDifficulty(d)}
          >
            <Text style={[styles.difficultyText, difficulty === d && styles.difficultyTextActive]}>{d}</Text>
          </Pressable>
        ))}
      </View>

      <Pressable style={styles.generateButton} onPress={generate} disabled={loading || !topic.trim()}>
        {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.generateButtonText}>Générer l'exercice</Text>}
      </Pressable>

      {error && <Text style={styles.error}>Erreur : {error}</Text>}

      {exercise && (
        <Animated.View
          style={[
            styles.resultCard,
            {
              opacity: resultAnim,
              transform: [{ translateY: resultAnim.interpolate({ inputRange: [0, 1], outputRange: [12, 0] }) }],
            },
          ]}
        >
          <Text style={styles.resultTitle}>{exercise.title}</Text>
          <Text style={styles.resultDescription}>{exercise.description}</Text>
          <Pressable style={styles.startButton} onPress={startExercise}>
            <Text style={styles.startButtonText}>Commencer cet exercice →</Text>
          </Pressable>
        </Animated.View>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#121212' },
  content: { padding: 16 },
  label: { color: '#a1a1aa', fontSize: 13, marginBottom: 6, marginTop: 12 },
  input: {
    backgroundColor: '#1e1e1e',
    color: '#fff',
    borderRadius: 8,
    padding: 12,
    fontSize: 14,
    borderWidth: 1,
    borderColor: '#2a2a2a',
  },
  difficultyRow: { flexDirection: 'row', gap: 8 },
  difficultyChip: {
    paddingVertical: 8,
    paddingHorizontal: 14,
    borderRadius: 20,
    backgroundColor: '#1e1e1e',
    borderWidth: 1,
    borderColor: '#2a2a2a',
  },
  difficultyChipActive: { backgroundColor: '#2563eb', borderColor: '#2563eb' },
  difficultyText: { color: '#a1a1aa', fontSize: 13 },
  difficultyTextActive: { color: '#fff', fontWeight: '600' },
  generateButton: {
    backgroundColor: '#2563eb',
    borderRadius: 8,
    paddingVertical: 14,
    alignItems: 'center',
    marginTop: 20,
  },
  generateButtonText: { color: '#fff', fontWeight: '600', fontSize: 15 },
  error: { color: '#ef4444', marginTop: 12, fontSize: 13 },
  resultCard: {
    backgroundColor: '#1e1e1e',
    borderRadius: 10,
    padding: 16,
    marginTop: 20,
    borderWidth: 1,
    borderColor: '#2a2a2a',
  },
  resultTitle: { color: '#fff', fontSize: 17, fontWeight: '700', marginBottom: 6 },
  resultDescription: { color: '#a1a1aa', fontSize: 13, marginBottom: 14, lineHeight: 18 },
  startButton: {
    backgroundColor: '#22c55e',
    borderRadius: 8,
    paddingVertical: 12,
    alignItems: 'center',
  },
  startButtonText: { color: '#0a0a0a', fontWeight: '700', fontSize: 14 },
});
