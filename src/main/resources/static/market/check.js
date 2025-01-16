document.addEventListener("DOMContentLoaded", function () {
  updateCartTotals();

  // 수량 변경 이벤트
  document.querySelectorAll(".pro-qty input").forEach(function (input) {
    input.addEventListener("input", function () {
      updateRowTotal(input);
    });
  });

  // 체크박스 선택 이벤트 후가
  document.querySelectorAll("input[name='chk1']").forEach(function (checkbox) {
    checkbox.addEventListener("change", updateSelectedTotal);
  });

  function updateCartTotals() {
    document.querySelectorAll("tr[th\\:each]").forEach(function (row) {
      updateRowTotal(row.querySelector(".pro-qty input"));
    });
    updateSelectedTotal(); // 초기 합계 업데이트
  }
  function updateRowTotal(input) {
    if (!input) return;

    let row = input.closest("tr");
    let priceElement = row.querySelector(".shoping__cart__price");
    let totalElement = row.querySelector(".shoping__cart__total");

    let price =
      parseFloat(priceElement.textContent.trim().replace(/,/g, "")) || 0;
    let quantity = parseInt(input.value.trim()) || 1;
    let total = price * quantity;

    totalElement.textContent = total.toLocaleString(); // 숫자 형식 변환

    updateSelectedTotal();
  }

  function updateSelectedTotal() {
    let totalSum = 0;

    document.querySelectorAll("tr[th\\:each]").forEach(function (row) {
      let checkbox = row.querySelector("input[name='chk1']");
      let totalElement = row.querySelector(".shoping__cart__total");

      if (checkbox && checkbox.checked && totalElement) {
        let total = parseFloat(totalElement.textContent.replace(/,/g, "")) || 0;
        totalSum += total;
      }
    });

    // 장바구니 총합 업데이트
    let cartTotalElement = document.querySelector(
      ".shoping__checkout ul li span"
    );
    if (cartTotalElement) {
      cartTotalElement.textContent = totalSum.toLocaleString();
    }
  }
});
