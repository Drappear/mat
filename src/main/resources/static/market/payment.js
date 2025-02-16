document.addEventListener("DOMContentLoaded", function () {
  const paymentButton = document.getElementById("paymentButton");

  if (!paymentButton) return;

  paymentButton.addEventListener("click", function () {
    const totalPrice = parseInt(paymentButton.dataset.amount, 10);
    const buyerEmail = paymentButton.dataset.email;
    const buyerName = paymentButton.dataset.name;
    const buyerTel = paymentButton.dataset.tel;
    const buyerAddr = paymentButton.dataset.addr;
    const buyerPostcode = paymentButton.dataset.postcode;

    IMP.init("your_imp_code"); // ✅ 아임포트 가맹점 식별 코드 추가

    IMP.request_pay(
      {
        pg: "kakaopay",
        pay_method: "card",
        merchant_uid: "ORDER_" + new Date().getTime(),
        name: paymentButton.dataset.name,
        amount: totalPrice,
        buyer_email: buyerEmail,
        buyer_name: buyerName,
        buyer_tel: buyerTel,
        buyer_addr: buyerAddr,
        buyer_postcode: buyerPostcode,
      },
      function (response) {
        if (response.success) {
          fetch("/market/payment/verify", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              imp_uid: response.imp_uid,
              merchant_uid: response.merchant_uid,
              amount: response.paid_amount,
            }),
          })
            .then((res) => res.json())
            .then((data) => {
              if (data.success) {
                alert("결제가 성공적으로 처리되었습니다.");
                window.location.href = "/market/order-complete";
              } else {
                alert("결제 검증 실패: " + data.message);
              }
            })
            .catch((error) => console.error("Error:", error));
        } else {
          alert("결제 실패: " + response.error_msg);
        }
      }
    );
  });
});
