const addCartForm = document.querySelector("#addCartForm");

document.querySelector("#addToCartBtn").addEventListener("click", (e) => {
  e.preventDefault();

  addCartForm.quantity.value =
    document.querySelector("[name='quantity']").value;

  addCartForm.submit();
});
