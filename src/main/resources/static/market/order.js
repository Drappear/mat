// '구매하기' 버튼 클릭 시
const actionForm = document.querySelector("actionform");
document.querySelector(".site-btn").addEventListener("click", () => {
  // actionForm action 수정
  if (!confirm("주문 하시겠습니까?")) {
    return;
  }

  actionForm.action = "/market/order";
  actionForm.submit();
});
