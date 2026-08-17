import React from 'react';
import { StatusBar, StyleSheet } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from './src/screens/HomeScreen';
import LiveCodeScreen from './src/screens/LiveCodeScreen';

const Stack = createNativeStackNavigator();

export default function App() {
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
              name="LiveCode"
              component={LiveCodeScreen}
              options={({ route }) => ({ title: route.params?.exercise?.title ?? 'Exercice' })}
            />
          </Stack.Navigator>
        </NavigationContainer>
      </SafeAreaView>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  safeArea: { flex: 1, backgroundColor: '#121212' },
});
