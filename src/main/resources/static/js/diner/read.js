const modalOpenButton = document.querySelector("#modalOpenButton");
const reviewCloseBtn = document.querySelector("#reviewCloseBtn");
const modal = document.querySelector("#modalContainer");

modalOpenButton.addEventListener("click", () => {
  modal.classList.remove("hidden");
});

reviewCloseBtn.addEventListener("click", () => {
  modal.classList.add("hidden");
});

window.addEventListener("click", function (event) {
  if (event.target === modal) {
    modal.classList.add("hidden");
  }
});
