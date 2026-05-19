/* AVATAR CONTATO */

const avatar = document.querySelector("#avatar-contato")

avatar.addEventListener("mouseenter", () => {
    avatar.src = "../img/phone 2nd.png";
});

avatar.addEventListener("mouseleave", () => {
    avatar.src = "../img/phone first.png";
});