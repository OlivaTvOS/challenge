const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || '/api').replace(/\/$/, '');

export const apiFetch = async <T = unknown>(endpoint: string, options?: RequestInit): Promise<T | null> => {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
  });

  if (!response.ok) {
    const errorText = (await response.text()).trim();
    throw new Error(errorText || 'Nao foi possivel concluir a solicitacao.');
  }

  if (response.status === 204) {
    return null;
  }

  return await response.json() as T;
};
