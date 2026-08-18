import React, { useState, useCallback, useRef } from 'react';
import { View, Text, TextInput, Pressable, StyleSheet, ScrollView, Animated, ActivityIndicator } from 'react-native';

const API_BASE_URL = 'https://codelearn-app-production-3ae0.up.railway.app';

export default function AiChatPanel({ exerciseContext }) {
  const [visible, setVisible] = useState(false);
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const panelAnim = useRef(new Animated.Value(0)).current;

  const toggle = useCallback(() => {
    const next = !visible;
    setVisible(next);
    Animated.timing(panelAnim, {
      toValue: next ? 1 : 0,
      duration: 250,
      useNativeDriver: true,
    }).start();
  }, [visible]);

  const send = useCallback(async () => {
    if (!input.trim() || loading) return;
    const userMessage = input.trim();
    setMessages((prev) => [...prev, { role: 'user', text: userMessage }]);
    setInput('');
    setLoading(true);
    try {
      const res = await fetch(`${API_BASE_URL}/api/ai/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ exerciseContext, userMessage }),
      });
      const data = await res.json();
      setMessages((prev) => [...prev, { role: 'ai', text: data.reply }]);
    } catch (err) {
      setMessages((prev) => [...prev, { role: 'ai', text: `Erreur : ${err.message}` }]);
    } finally {
      setLoading(false);
    }
  }, [input, loading, exerciseContext]);

  return (
    <View>
      <Pressable style={styles.toggleButton} onPress={toggle}>
        <Text style={styles.toggleButtonText}>{visible ? '✕ Fermer l\'aide' : '💬 Besoin d\'aide ?'}</Text>
      </Pressable>

      {visible && (
        <Animated.View
          style={[
            styles.panel,
            {
              opacity: panelAnim,
              transform: [{ translateY: panelAnim.interpolate({ inputRange: [0, 1], outputRange: [10, 0] }) }],
            },
          ]}
        >
          <ScrollView style={styles.messages}>
            {messages.map((m, i) => (
              <View
                key={i}
                style={[styles.bubble, m.role === 'user' ? styles.bubbleUser : styles.bubbleAi]}
              >
                <Text style={styles.bubbleText}>{m.text}</Text>
              </View>
            ))}
            {loading && <ActivityIndicator color="#2563eb" style={{ marginVertical: 8 }} />}
          </ScrollView>
          <View style={styles.inputRow}>
            <TextInput
              style={styles.input}
              value={input}
              onChangeText={setInput}
              placeholder="Pose ta question..."
              placeholderTextColor="#666"
              onSubmitEditing={send}
            />
            <Pressable style={styles.sendButton} onPress={send} disabled={loading}>
              <Text style={styles.sendButtonText}>Envoyer</Text>
            </Pressable>
          </View>
        </Animated.View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  toggleButton: {
    backgroundColor: '#2a2a2a',
    borderRadius: 8,
    paddingVertical: 8,
    alignItems: 'center',
    marginBottom: 8,
  },
  toggleButtonText: { color: '#a1a1aa', fontSize: 13, fontWeight: '600' },
  panel: {
    backgroundColor: '#1a1a1a',
    borderRadius: 8,
    padding: 8,
    maxHeight: 220,
    marginBottom: 8,
  },
  messages: { maxHeight: 140, marginBottom: 8 },
  bubble: { borderRadius: 8, padding: 8, marginBottom: 6, maxWidth: '85%' },
  bubbleUser: { backgroundColor: '#2563eb', alignSelf: 'flex-end' },
  bubbleAi: { backgroundColor: '#2a2a2a', alignSelf: 'flex-start' },
  bubbleText: { color: '#fff', fontSize: 13 },
  inputRow: { flexDirection: 'row', gap: 6 },
  input: {
    flex: 1,
    backgroundColor: '#2a2a2a',
    color: '#fff',
    borderRadius: 6,
    paddingHorizontal: 10,
    paddingVertical: 8,
    fontSize: 13,
  },
  sendButton: {
    backgroundColor: '#2563eb',
    borderRadius: 6,
    paddingHorizontal: 14,
    justifyContent: 'center',
  },
  sendButtonText: { color: '#fff', fontSize: 13, fontWeight: '600' },
});
