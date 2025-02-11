document.addEventListener("DOMContentLoaded", () => {
  // 삭제 폼 가져오기
  const removeForm = document.querySelector("#removeForm");

  if (!removeForm) {
    console.error("removeForm 요소를 찾을 수 없습니다.");
    return;
  }

  // 모든 삭제 버튼에 클릭 이벤트 추가
  const removeButtons = document.querySelectorAll(".icon_close");

  removeButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      e.preventDefault();

      // 클릭된 버튼의 data-id 값 가져오기
      const cartItemId = button.getAttribute("data-id");

      if (!cartItemId) {
        console.error("삭제할 cartitemid를 찾을 수 없습니다.");
        return;
      }

      // hidden input에 cartitemid 설정
      removeForm.querySelector("[name='cartitemid']").value = cartItemId;

      // 삭제 확인 및 폼 제출
      if (confirm("정말로 삭제하시겠습니까?")) {
        removeForm.action = "/market/remove";
        removeForm.submit();
      }
    });
  });
});
