const BASE_URL = "http://localhost:8080";

export function connectToBattle(onMessage, onOpen) {
  const eventSource = new EventSource(`${BASE_URL}/battle/stream`);

  eventSource.onopen = () => {
    console.log("🟢 SSE conectado");
    if (onOpen) onOpen();
  };

  eventSource.onmessage = (event) => {
    const data = JSON.parse(event.data);
    onMessage(data);
  };

  eventSource.onerror = () => {
    console.warn("⚠️ SSE intentando reconectar...");
    // ❌ NO cerrar ni marcar desconectado
  };

  return eventSource;
}

export async function getEstadoInicial() {
  const res = await fetch(`${BASE_URL}/api/pokemon/vida`);
  return await res.json();
}

export async function atacar(atacante, dano) {
  try {
    // CORRECCIÓN: Usamos GET y pasamos los parámetros en la URL (?atacante=...&dano=...)
    const res = await fetch(
      `${BASE_URL}/api/pokemon/ataque?atacante=${atacante}&dano=${dano}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      },
    );

    const data = await res.json();
    console.log("🔥 Ataque aplicado:", data);
  } catch (error) {
    console.error("❌ Error al atacar:", error);
  }
}
