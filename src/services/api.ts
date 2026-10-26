// Função auxiliar de chamada
export const apiFecth = async (endpoint: string, options?: RequestInit) => {
    const response = await fetch(endpoint, {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...options?.headers,
        },
    })

    if (!response.ok){
        const errorText = await response.text(); // <--- Funciona como um console.log pra verificação do erro de retorno vindo do Java
        throw new Error(`Erro na requisição: ${response.status} - ${errorText}`);
    }

    return response.status !== 204 ? await response.json() : null
}