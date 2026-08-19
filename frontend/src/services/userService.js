import AsyncStorage from '@react-native-async-storage/async-storage';

const API_BASE = 'https://codelearn-app-production-3ae0.up.railway.app';
const USER_ID_KEY = 'codelearn_user_id';

export async function getOrCreateUserId() {
  try {
    const storedId = await AsyncStorage.getItem(USER_ID_KEY);
    if (storedId) {
      return parseInt(storedId, 10);
    }

    const res = await fetch(`${API_BASE}/api/users/anonymous`, { method: 'POST' });
    if (!res.ok) throw new Error('Erreur de création utilisateur');
    const data = await res.json();

    await AsyncStorage.setItem(USER_ID_KEY, String(data.userId));
    return data.userId;
  } catch (err) {
    console.error('Erreur getOrCreateUserId:', err);
    return null;
  }
}

export async function getUserStats(userId) {
  try {
    const res = await fetch(`${API_BASE}/api/users/${userId}/stats`);
    if (!res.ok) throw new Error('Erreur de chargement stats');
    return await res.json();
  } catch (err) {
    console.error('Erreur getUserStats:', err);
    return null;
  }
}
