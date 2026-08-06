const avatarInicial = document.querySelector("#avatar-inicial");
const heroInicial = document.querySelector(".hero-inicial");
const heroExpandido = document.querySelector(".hero-expandido");


avatarInicial.addEventListener("mouseenter", () => {
    avatarInicial.src = "img/index-main2nd.png";
});

avatarInicial.addEventListener("mouseleave", () => {
    avatarInicial.src = "img/index-main.png";
});

avatarInicial.addEventListener("click", () => {
    heroInicial.style.display = "none";
    heroExpandido.style.display = "flex";
});