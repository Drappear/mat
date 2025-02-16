document.addEventListener("DOMContentLoaded", () => {
  const addCartForm = document.querySelector("#addCartForm");

  // 상품의 잔여수량과 비교하는 코드
  document.querySelector(".pro-qty").addEventListener("click", () => {
    //선택수량 가져오기
    const count = parseInt(document.querySelector("[name='quantity']").value);

    if (quantity < count) {
      alert("구매 가능한 수량을 초과하였습니다.");
      return;
    }
  });

  document.querySelector("#addToCartBtn").addEventListener("click", (e) => {
    e.preventDefault();

    // 사용자 입력값 설정
    const quantity = document.querySelector("[name='quantity']").value;

    addCartForm.quantity.value = quantity;

    addCartForm.submit();
  });
});
