document.addEventListener("DOMContentLoaded", function () {
  // 총합을 표시할 요소
  const totalElement = document.querySelector(".shoping__checkout ul li span");

  // 체크박스 상태에 따라 총합 계산
  function calculateTotal() {
    let totalSum = 0;

    // 모든 행을 순회하며 총액 합산
    document.querySelectorAll("tbody tr").forEach(function (row) {
      const checkbox = row.querySelector("input[name='chk1']");
      const rowTotalElement = row.querySelector(".shoping__cart__total");

      // 체크된 경우에만 합산
      if (checkbox && checkbox.checked) {
        totalSum += parseInt(rowTotalElement.textContent) || 0;
      }
    });

    // 모든 체크박스가 선택되지 않은 경우, 모든 행의 총합 계산
    if (totalSum === 0) {
      document.querySelectorAll("tbody tr").forEach(function (row) {
        const rowTotalElement = row.querySelector(".shoping__cart__total");
        totalSum += parseInt(rowTotalElement.textContent) || 0;
      });
    }

    // 총합 표시
    if (totalElement) {
      totalElement.textContent = totalSum; // 소수점 없이 정수로 표시
    }
  }

  // 체크박스 상태 변경 시 총합 재계산
  document.querySelectorAll("input[name='chk1']").forEach(function (checkbox) {
    checkbox.addEventListener("change", calculateTotal);
  });

  // 초기 총합 계산
  calculateTotal();
});
