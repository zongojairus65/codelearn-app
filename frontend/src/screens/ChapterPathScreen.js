import React, { useEffect, useState, useCallback } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
  Dimensions,
} from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import { getOrCreateUserId } from '../services/userService';

const API_BASE = 'https://codelearn-app-production-3ae0.up.railway.app';
const SCREEN_WIDTH = Dimensions.get('window').width;
const BUBBLE_SIZE = 64;
const ZIGZAG_OFFSETS = [-60, -20, 20, 60, 20, -20];

export default function ChapterPathScreen({ route, navigation }) {
  const { chapterId, chapterTitle } = route.params;
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [loadingLessonId, setLoadingLessonId] = useState(null);

  useFocusEffect(
    useCallback(() => {
      fetchProgress();
    }, [chapterId])
  );

  const fetchProgress = async () => {
    try {
      setLoading(true);
      setError(null);
      const userId = await getOrCreateUserId();
      const res = await fetch(`${API_BASE}/api/progress/chapters/${chapterId}?userId=${userId}`);
      if (!res.ok) throw new Error('Erreur de chargement');
      const data = await res.json();
      setLessons(data);
    } catch (err) {
      setError('Impossible de charger les leçons.');
    } finally {
      setLoading(false);
    }
  };

  const getBubbleStyle = (status) => {
    if (status === 'COMPLETED') return styles.bubbleCompleted;
    if (status === 'UNLOCKED') return styles.bubbleUnlocked;
    return styles.bubbleLocked;
  };

  const getBubbleIcon = (status) => {
    if (status === 'COMPLETED') return '✓';
    if (status === 'UNLOCKED') return '★';
    return '🔒';
  };

  const handlePressLesson = async (lesson, status) => {
    if (status === 'LOCKED' || loadingLessonId) return;
    try {
      setLoadingLessonId(lesson.id);
      const res = await fetch(`${API_BASE}/api/lessons/${lesson.id}/exercises`);
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
      setError('Impossible de charger l\'exercice.');
    } finally {
      setLoadingLessonId(null);
    }
  };

  if (loading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#2563eb" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {error && (
        <View style={styles.errorBanner}>
          <Text style={styles.errorText}>{error}</Text>
        </View>
      )}

      <ScrollView contentContainerStyle={styles.pathContainer}>
        {lessons.map((lesson, index) => {
          const offset = ZIGZAG_OFFSETS[index % ZIGZAG_OFFSETS.length];
          const isLoadingThis = loadingLessonId === lesson.id;

          return (
            <View key={lesson.id} style={styles.lessonRow}>
              <View style={[styles.bubbleWrapper, { marginLeft: SCREEN_WIDTH / 2 - BUBBLE_SIZE / 2 + offset }]}>
                <TouchableOpacity
                  style={[styles.bubble, getBubbleStyle(lesson.status)]}
                  onPress={() => handlePressLesson(lesson, lesson.status)}
                  activeOpacity={lesson.status === 'LOCKED' ? 1 : 0.7}
                  disabled={lesson.status === 'LOCKED'}
                >
                  {isLoadingThis ? (
                    <ActivityIndicator size="small" color="#fff" />
                  ) : (
                    <Text style={styles.bubbleIcon}>{getBubbleIcon(lesson.status)}</Text>
                  )}
                </TouchableOpacity>
                <Text style={styles.lessonLabel} numberOfLines={2}>
                  {lesson.orderIndex}. {lesson.title}
                </Text>
              </View>
            </View>
          );
        })}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#121212' },
  pathContainer: { paddingVertical: 30, paddingBottom: 60 },
  lessonRow: { marginBottom: 24 },
  bubbleWrapper: { alignItems: 'center', width: BUBBLE_SIZE + 40 },
  bubble: {
    width: BUBBLE_SIZE,
    height: BUBBLE_SIZE,
    borderRadius: BUBBLE_SIZE / 2,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 3,
  },
  bubbleCompleted: { backgroundColor: '#22c55e', borderColor: '#16a34a' },
  bubbleUnlocked: { backgroundColor: '#2563eb', borderColor: '#1d4ed8' },
  bubbleLocked: { backgroundColor: '#2a2a2a', borderColor: '#1e1e1e' },
  bubbleIcon: { fontSize: 24, color: '#fff' },
  lessonLabel: {
    color: '#a1a1aa',
    fontSize: 12,
    textAlign: 'center',
    marginTop: 6,
    width: 90,
  },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#121212' },
  errorBanner: { backgroundColor: '#7f1d1d', padding: 10 },
  errorText: { color: '#fff', textAlign: 'center', fontSize: 13 },
});
