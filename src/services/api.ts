export const apiFetch = async <T = unknown>(endpoint: string, options?: RequestInit): Promise<T | null> => {
    const response = await fetch(endpoint, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...options?.headers,
        },
    })

    if (!response.ok){
        const errorText = (await response.text()).trim();
        throw new Error(errorText || 'Não foi possível concluir a solicitação.');
    }

    return response.status !== 204 ? await response.json() as T : null;
}