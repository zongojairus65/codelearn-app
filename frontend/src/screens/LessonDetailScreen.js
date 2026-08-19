import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
  Pressable,
} from 'react-native';

const API_BASE = 'https://codelearn-app-production-3ae0.up.railway.app';

export default function LessonDetailScreen({ route, navigation }) {
  const { lessonId, chapterId } = route.params;
  const [theory, setTheory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [startingExercise, setStartingExercise] = useState(false);

  useEffect(() => {
    fetchTheory();
  }, [lessonId]);

  const fetchTheory = async () => {
    try {
      setLoading(true);
      setError(null);
      const res = await fetch(`${API_BASE}/api/lessons/${lessonId}/theory`);
      if (!res.ok) throw new Error('Erreur de chargement');
      const data = await res.json();
      setTheory(data);
    } catch (err) {
      setError("Impossible de charger l'explication.");
    } finally {
      setLoading(false);
    }
  };

  const handleStartExercise = async () => {
    if (startingExercise) return;
    try {
      setStartingExercise(true);
      const res = await fetch(`${API_BASE}/api/lessons/${lessonId}/exercises`);
      if (!res.ok) throw new Error('Erreur');
      const exercises = await res.json();
      if (!exercises.length) {
        setError('Aucun exercice pour cette leçon.');
        return;
      }
      const ex = exercises[0];
      const mappedExercise = {
        id: ex.id,
        title: ex.title,
        description: ex.instructions,
        defaultCode: {
          html: ex.starterHtml || '',
          css: ex.starterCss || '',
          js: ex.starterJs || '',
        },
      };
      navigation.navigate('LiveCode', { exercise: mappedExercise, chapterId });
    } catch (err) {
      setError("Impossible de charger l'exercice.");
    } finally {
      setStartingExercise(false);
    }
  };

  if (loading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#2563eb" />
        <Text style={styles.loadingText}>Préparation de la leçon...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        {error && <Text style={styles.errorText}>{error}</Text>}

        {theory && (
          <>
            <Text style={styles.title}>{theory.title}</Text>
            <Text style={styles.theoryText}>{theory.theory}</Text>
          </>
        )}
      </ScrollView>

      <View style={styles.footer}>
        <Pressable
          style={[styles.startButton, startingExercise && styles.startButtonDisabled]}
          onPress={handleStartExercise}
          disabled={startingExercise}
        >
          {startingExercise ? (
            <ActivityIndicator color="#fff" />
          ) : (
            <Text style={styles.startButtonText}>Lancer l'exercice →</Text>
          )}
        </Pressable>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#121212' },
  content: { padding: 20, paddingBottom: 40 },
  title: {
    color: '#fff',
    fontSize: 22,
    fontWeight: '700',
    marginBottom: 16,
  },
  theoryText: {
    color: '#d4d4d4',
    fontSize: 15,
    lineHeight: 24,
  },
  footer: {
    padding: 16,
    backgroundColor: '#1e1e1e',
    borderTopWidth: 1,
    borderTopColor: '#2a2a2a',
  },
  startButton: {
    backgroundColor: '#2563eb',
    borderRadius: 8,
    paddingVertical: 14,
    alignItems: 'center',
  },
  startButtonDisabled: { opacity: 0.6 },
  startButtonText: { color: '#fff', fontWeight: '700', fontSize: 15 },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#121212' },
  loadingText: { color: '#a1a1aa', fontSize: 13, marginTop: 12 },
  errorText: { color: '#f87171', fontSize: 14, marginBottom: 12 },
});
