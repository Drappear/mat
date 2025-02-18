document.addEventListener("DOMContentLoaded", () => {
  console.log("✅ details.js 실행됨");

  // `quantity` 클래스가 있는 요소 가져오기
  const productDetails = document.querySelector(".quantity");
  if (!productDetails) {
    console.error("❌ 상품 정보가 없습니다.");
    return;
  }

  // `data-quantity` 속성에서 최대 구매 가능 수량 가져오기
  const maxQuantity = parseInt(productDetails.dataset.quantity, 10); // ✅ dataset.quantity 사용
  console.log(`✅ 최대 구매 가능 수량: ${maxQuantity}`);

  if (isNaN(maxQuantity) || maxQuantity < 1) {
    console.error("❌ 최대 구매 가능 수량을 가져오는 데 실패했습니다.");
    return;
  }

  // 수량 입력 필드
  const quantityInput = document.querySelector("[name='quantity']");

  // `+`, `-` 버튼
  const plusButton = document.querySelector(".pro-qty .inc");
  const minusButton = document.querySelector(".pro-qty .dec");

  // `+` 버튼 클릭 시 (수량 증가)
  plusButton.addEventListener("click", (event) => {
    event.preventDefault();
    event.stopPropagation();

    let currentQuantity = parseInt(quantityInput.value, 10) || 1;

    if (currentQuantity >= maxQuantity) {
      alert(`⚠️ 최대 ${maxQuantity}개까지 구매할 수 있습니다.`);
      quantityInput.value = maxQuantity;
      return;
    }

    quantityInput.value = currentQuantity + 1;
  });

  // `-` 버튼 클릭 시 (수량 감소)
  minusButton.addEventListener("click", (event) => {
    event.preventDefault();
    event.stopPropagation();

    let currentQuantity = parseInt(quantityInput.value, 10) || 1;

    if (currentQuantity <= 1) {
      alert("⚠️ 최소 1개 이상 구매해야 합니다.");
      quantityInput.value = 1;
      return;
    }

    quantityInput.value = currentQuantity - 1;
  });

  // 사용자가 직접 입력할 경우 검사
  quantityInput.addEventListener("change", () => {
    let currentQuantity = parseInt(quantityInput.value, 10);

    if (isNaN(currentQuantity) || currentQuantity < 1) {
      alert("⚠️ 최소 1개 이상 구매해야 합니다.");
      quantityInput.value = 1;
      return;
    }

    if (currentQuantity > maxQuantity) {
      alert(`⚠️ 최대 ${maxQuantity}개까지 구매할 수 있습니다.`);
      quantityInput.value = maxQuantity;
    }
  });

  // ✅ ADD TO CART 버튼 기능 추가
  const addToCartBtn = document.querySelector("#addToCartBtn");
  const addCartForm = document.querySelector("#addCartForm");

  if (addToCartBtn && addCartForm) {
    addToCartBtn.addEventListener("click", (e) => {
      e.preventDefault();

      // 선택한 수량 가져오기
      const selectedQuantity = parseInt(quantityInput.value, 10);

      // 입력값 검증 (최대/최소)
      if (selectedQuantity < 1) {
        alert("⚠️ 최소 1개 이상 선택해야 합니다.");
        return;
      }

      if (selectedQuantity > maxQuantity) {
        alert(`⚠️ 최대 ${maxQuantity}개까지 구매할 수 있습니다.`);
        quantityInput.value = maxQuantity;
        return;
      }

      // ✅ 장바구니 폼에 값 설정 후 제출
      addCartForm.quantity.value = selectedQuantity;
      addCartForm.submit();
    });
  } else {
    console.error("❌ ADD TO CART 버튼 또는 장바구니 폼이 존재하지 않습니다.");
  }
});
