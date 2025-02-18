document.addEventListener("DOMContentLoaded", function () {
  const orderBtn = document.getElementById("orderBtn");
  const checkboxes = document.querySelectorAll("input[name='chk1']");

  orderBtn.addEventListener("click", function (event) {
    event.preventDefault();

    let selectedIds = [];
    let selectedQuantities = [];

    checkboxes.forEach(function (checkbox) {
      if (checkbox.checked) {
        const row = checkbox.closest("tr");
        const quantityInput = row.querySelector(".quantity-input");

        if (!quantityInput.value) {
          alert("수량을 입력해주세요.");
          return;
        }

        selectedIds.push(checkbox.value);
        selectedQuantities.push(quantityInput.value); // 수량 데이터 수집
      }
    });

    if (selectedIds.length === 0) {
      alert("주문할 상품을 선택하세요.");
      return;
    }

    // 체크된 상품의 ID & 수량을 함께 전달
    const url =
      "/market/order?selectedCartItemIds=" +
      selectedIds.join(",") +
      "&selectedQuantities=" +
      selectedQuantities.join(",");
    window.location.href = url;
  });
});
