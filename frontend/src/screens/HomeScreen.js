import React from 'react';
import { View, Text, FlatList, Pressable, StyleSheet } from 'react-native';
import Animated, { FadeInDown } from 'react-native-reanimated';
import { EXERCISES } from '../data/exercises';

export default function HomeScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Text style={styles.header}>Exercices</Text>
      <FlatList
        data={EXERCISES}
        keyExtractor={(item) => String(item.id)}
        contentContainerStyle={styles.list}
        renderItem={({ item, index }) => (
          <Animated.View entering={FadeInDown.delay(index * 80).duration(400)}>
            <Pressable
              style={({ pressed }) => [
                styles.card,
                pressed && styles.cardPressed,
              ]}
              onPress={() => navigation.navigate('LiveCode', { exercise: item })}
            >
              <Text style={styles.cardTitle}>{item.title}</Text>
              <Text style={styles.cardDescription}>{item.description}</Text>
            </Pressable>
          </Animated.View>
        )}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#121212', paddingTop: 16 },
  header: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
    paddingHorizontal: 16,
    marginBottom: 12,
  },
  list: { paddingHorizontal: 16, paddingBottom: 24 },
  card: {
    backgroundColor: '#1e1e1e',
    borderRadius: 10,
    padding: 16,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: '#2a2a2a',
  },
  cardPressed: {
    backgroundColor: '#262626',
    transform: [{ scale: 0.98 }],
  },
  cardTitle: { color: '#fff', fontSize: 16, fontWeight: '600', marginBottom: 4 },
  cardDescription: { color: '#a1a1aa', fontSize: 13 },
});
