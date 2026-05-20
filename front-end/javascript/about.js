const cards = document.querySelectorAll(".info-card");
const nextBtn = document.querySelector(".next");
const prevBtn = document.querySelector(".prev");

let currentCard = 0;

function showCard(index){
    cards.forEach(card => {
        card.classList.remove("active");
    });
    cards[index].classList.add("active");
}

nextBtn.addEventListener("click", () => {
    currentCard++;
    if(currentCard >= cards.length){
        currentCard = 0;
    }
    showCard(currentCard);
});

prevBtn.addEventListener("click", () => {
    currentCard--;
    if(currentCard < 0){
        currentCard = cards.length - 1;
    }
    showCard(currentCard);
});