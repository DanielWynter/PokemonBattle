export default function PokemonCard({ name, vida }) {
  return (
    <div
      style={{
        border: "2px solid black",
        borderRadius: "10px",
        padding: "20px",
        width: "200px",
        textAlign: "center",
        backgroundColor: "#f5f5f5",
      }}
    >
      <h2>{name}</h2>
      <p>❤️ Vida: {vida}</p>

      <div
        style={{
          height: "20px",
          background: "#ddd",
          borderRadius: "10px",
          overflow: "hidden",
        }}
      >
        <div
          style={{
            width: `${vida}%`,
            height: "100%",
            background: "green",
            transition: "0.3s",
          }}
        />
      </div>
    </div>
  );
}
