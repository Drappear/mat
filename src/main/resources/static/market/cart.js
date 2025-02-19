document.addEventListener("DOMContentLoaded", () => {
  // 장바구니 페이지에서만 실행
  if (!document.querySelector(".shoping-cart")) return;

  console.log("cart.js 실행됨");

  // 장바구니의 모든 상품 행을 가져옴
  const cartItems = document.querySelectorAll("tr[data-cart-id]");

  cartItems.forEach((item) => {
    // 상품의 현재 수량(input 값)과 최대 구매 가능 수량(data-quantity)을 가져오기
    const quantityInput = item.querySelector(".quantity-input");
    const maxQuantity = parseInt(item.getAttribute("data-quantity")); // 상품의 전체 재고 수량
    const price = parseInt(
      item.querySelector(".shoping__cart__price").innerText
    ); // 상품 단가
    const totalElement = item.querySelector(".shoping__cart__total"); // 총합 표시 엘리먼트

    // `+` 버튼 및 `-` 버튼 가져오기
    const plusButton = item.querySelector(".pro-qty .inc");
    const minusButton = item.querySelector(".pro-qty .dec");

    // 기존 이벤트 제거를 위한 새로운 버튼 생성
    const newPlusButton = plusButton.cloneNode(true);
    const newMinusButton = minusButton.cloneNode(true);
    plusButton.replaceWith(newPlusButton);
    minusButton.replaceWith(newMinusButton);

    // 수량 및 총합 업데이트 함수
    const updateTotal = () => {
      const currentQuantity = parseInt(quantityInput.value);
      totalElement.innerText = (currentQuantity * price).toLocaleString(); // 총합 업데이트
    };

    // `+` 버튼 클릭 시
    newPlusButton.addEventListener("click", (event) => {
      event.preventDefault(); // 기본 동작 중단
      event.stopPropagation(); // 이벤트 전파 차단
      console.log("+ 버튼 클릭됨");

      const currentQuantity = parseInt(quantityInput.value);
      if (currentQuantity + 1 > maxQuantity) {
        alert(
          `"${
            item.querySelector("h5").innerText
          }" 최대 ${maxQuantity}개까지 구매할 수 있습니다.`
        );
        return; // 조건 충족 시 동작 중단
      }
      quantityInput.value = currentQuantity + 1; // 수량 증가
      updateTotal(); // 총합 업데이트
    });

    // `-` 버튼 클릭 시
    newMinusButton.addEventListener("click", (event) => {
      event.preventDefault(); // 기본 동작 중단
      event.stopPropagation(); // 이벤트 전파 차단
      console.log("버튼 클릭됨");

      const currentQuantity = parseInt(quantityInput.value);
      if (currentQuantity - 1 < 1) {
        alert("최소 1개 이상 구매해야 합니다.");
        return; // 조건 충족 시 동작 중단
      }
      quantityInput.value = currentQuantity - 1; // 수량 감소
      updateTotal(); // 총합 업데이트
    });

    // 초기 총합 설정
    updateTotal();
  });
});
