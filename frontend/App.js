import React, { useEffect, useState } from 'react';
import { StatusBar, StyleSheet, View, ActivityIndicator } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from './src/screens/HomeScreen';
import ChapterPathScreen from './src/screens/ChapterPathScreen';
import LessonDetailScreen from './src/screens/LessonDetailScreen';
import LiveCodeScreen from './src/screens/LiveCodeScreen';
import GenerateExerciseScreen from './src/screens/GenerateExerciseScreen';
import { getOrCreateUserId } from './src/services/userService';

const Stack = createNativeStackNavigator();

export default function App() {
  const [ready, setReady] = useState(false);

  useEffect(() => {
    getOrCreateUserId().finally(() => setReady(true));
  }, []);

  if (!ready) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#2563eb" />
      </View>
    );
  }

  return (
    <SafeAreaProvider>
      <SafeAreaView style={styles.safeArea}>
        <StatusBar barStyle="light-content" backgroundColor="#121212" />
        <NavigationContainer>
          <Stack.Navigator
            screenOptions={{
              headerStyle: { backgroundColor: '#1e1e1e' },
              headerTintColor: '#fff',
              headerTitleStyle: { fontWeight: '600' },
              animation: 'slide_from_right',
              contentStyle: { backgroundColor: '#121212' },
            }}
          >
            <Stack.Screen
              name="Home"
              component={HomeScreen}
              options={{ title: 'CodeLearn' }}
            />
            <Stack.Screen
              name="ChapterPath"
              component={ChapterPathScreen}
              options={({ route }) => ({ title: route.params?.chapterTitle ?? 'Chapitre' })}
            />
            <Stack.Screen
              name="LessonDetail"
              component={LessonDetailScreen}
              options={({ route }) => ({ title: route.params?.lessonTitle ?? 'Leçon' })}
            />
            <Stack.Screen
              name="LiveCode"
              component={LiveCodeScreen}
              options={({ route }) => ({ title: route.params?.exercise?.title ?? 'Exercice' })}
            />
            <Stack.Screen
              name="GenerateExercise"
              component={GenerateExerciseScreen}
              options={{ title: 'Générer un exercice' }}
            />
          </Stack.Navigator>
        </NavigationContainer>
      </SafeAreaView>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: '#121212' },
  loadingContainer: { flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: '#121212' },
});
