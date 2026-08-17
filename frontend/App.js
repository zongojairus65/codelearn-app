import React from 'react';
import { SafeAreaView, StatusBar, StyleSheet } from 'react-native';
import LiveCodeScreen from './src/screens/LiveCodeScreen';

export default function App() {
  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="light-content" backgroundColor="#121212" />
      <LiveCodeScreen />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: '#121212' },
});
