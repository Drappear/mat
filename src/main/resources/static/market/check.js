document.addEventListener("DOMContentLoaded", function () {
  // 총합을 표시할 요소
  const totalElement = document.querySelector(".shoping__checkout ul li span");

  // 전체 선택 체크박스
  const chkAll = document.getElementById("chk_all");

  // 행 체크박스
  const rowCheckboxes = document.querySelectorAll("input[name='chk1']");

  // 체크박스 상태에 따라 총합 계산
  function calculateTotal() {
    let totalSum = 0;

    // 모든 행을 순회하며 총액 합산
    rowCheckboxes.forEach(function (checkbox) {
      const row = checkbox.closest("tr");
      const rowTotalElement = row.querySelector(".shoping__cart__total");

      // 체크된 경우에만 합산
      if (checkbox.checked) {
        let rowTotalText = rowTotalElement.textContent.trim(); // 공백 제거
        let rowTotal = parseFloat(rowTotalText.replace(/,/g, "")) || 0; // 쉼표 제거 후 숫자로 변환
        totalSum += rowTotal;
      }
    });

    // 총합 표시
    if (totalElement) {
      totalElement.textContent = totalSum.toLocaleString(); // 쉼표 추가하여 표시
    }
  }

  // 전체 선택 체크박스 클릭 시
  chkAll.addEventListener("change", function () {
    const isChecked = chkAll.checked;

    // 모든 행 체크박스 상태 동기화
    rowCheckboxes.forEach(function (checkbox) {
      checkbox.checked = isChecked;
    });

    // 총합 재계산
    calculateTotal();
  });

  // 행 체크박스 상태 변경 시 전체 선택 체크박스 상태 업데이트
  rowCheckboxes.forEach(function (checkbox) {
    checkbox.addEventListener("change", function () {
      // 전체 체크 여부 확인
      const allChecked = Array.from(rowCheckboxes).every((chk) => chk.checked);
      const noneChecked = Array.from(rowCheckboxes).every(
        (chk) => !chk.checked
      );

      // 전체 선택 체크박스 업데이트
      chkAll.checked = allChecked;
      chkAll.indeterminate = !allChecked && !noneChecked;

      // 총합 재계산
      calculateTotal();
    });
  });

  // 초기 총합 계산
  calculateTotal();
});
