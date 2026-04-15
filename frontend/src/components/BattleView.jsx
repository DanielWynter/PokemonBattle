import { useEffect, useState } from "react";
import {
  connectToBattle,
  getEstadoInicial,
  atacar,
} from "../services/battleService";
import PokemonCard from "./PokemonCard";

export default function BattleView() {
  const [vida1, setVida1] = useState(100);
  const [vida2, setVida2] = useState(100);
  const [conectado, setConectado] = useState(false);

  useEffect(() => {
    let eventSource;

    // estado inicial
    getEstadoInicial().then((data) => {
      setVida1(data.vida1);
      setVida2(data.vida2);
    });

    // conexión SSE
    eventSource = connectToBattle(
      (data) => {
        setVida1(data.vida1);
        setVida2(data.vida2);
      },
      () => setConectado(true),
    );

    return () => eventSource.close();
  }, []);

  return (
    <div style={{ textAlign: "center" }}>
      <h1>🔥 Batalla Pokémon</h1>

      <p style={{ color: conectado ? "green" : "red" }}>
        {conectado ? "🟢 Conectado" : "🔴 Conectando..."}
      </p>

      <div style={{ display: "flex", justifyContent: "center", gap: "50px" }}>
        <PokemonCard name="Pikachu ⚡" vida={vida1} />
        <PokemonCard name="Charmander 🔥" vida={vida2} />
      </div>

      <div style={{ marginTop: "30px" }}>
        <button onClick={() => atacar("p1", 10)}>⚡ Pikachu ataca</button>

        <button onClick={() => atacar("p2", 10)}>🔥 Charmander ataca</button>
      </div>
    </div>
  );
}
