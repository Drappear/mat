document.addEventListener("DOMContentLoaded", function () {
  const orderBtn = document.getElementById("orderBtn");

  orderBtn.addEventListener("click", async function (event) {
    event.preventDefault(); // ✅ 기본 form 제출 방지

    const urlParams = new URLSearchParams(window.location.search);
    let selectedIds = urlParams.get("selectedCartItemIds");
    let selectedQuantities = urlParams.get("selectedQuantities");

    if (!selectedIds || !selectedQuantities) {
      alert("주문할 상품을 선택하세요.");
      return;
    }

    // ✅ 주문 정보 가져오기
    const amount = parseInt(
      document.getElementById("totalAmount").innerText.replace(",", ""),
      10
    );
    const recipientName = document.getElementById("recipientName").value;
    const phoneNumber = document.getElementById("phoneNumber").value;
    const zipcode = document.getElementById("zipcode").value;
    const addr = document.getElementById("addr").value;
    const detailAddr = document.getElementById("detailAddr").value || ""; // ✅ undefined 방지
    const email = document.getElementById("email").value;

    if (!recipientName || !phoneNumber || !zipcode || !addr || !email) {
      alert("모든 정보를 입력해주세요.");
      return;
    }

    const orderData = {
      selectedCartItemIds: selectedIds,
      selectedQuantities: selectedQuantities,
      recipientName,
      phoneNumber,
      email,
      zipcode,
      addr,
      detailAddr,
      totalAmount: amount,
    };

    console.log("전송할 orderData:", orderData);

    // ✅ CSRF 토큰 가져오기
    const csrfTokenMeta = document.querySelector("meta[name='_csrf']");
    const csrfHeaderMeta = document.querySelector("meta[name='_csrf_header']");
    const csrfToken = csrfTokenMeta
      ? csrfTokenMeta.getAttribute("content")
      : "";
    const csrfHeader = csrfHeaderMeta
      ? csrfHeaderMeta.getAttribute("content")
      : "";

    try {
      const response = await fetch("/market/order", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          [csrfHeader]: csrfToken,
        },
        body: JSON.stringify(orderData),
      });

      const data = await response.json();
      console.log("서버 응답:", data);

      if (data.orderId) {
        const generatedOrderUid = "ORDER_" + data.orderId; // 주문 ID 기반 orderUid 생성

        // orderUid를 input 태그에 저장
        document.getElementById("orderUid").value = generatedOrderUid;

        //alert(`주문이 완료되었습니다. 주문 ID: ${data.orderId}`);

        // 자동 결제 실행 (orderUid를 merchant_uid로 사용)
        startPayment(
          generatedOrderUid,
          amount,
          email,
          recipientName,
          phoneNumber,
          addr,
          detailAddr, // 상세 주소 추가
          zipcode
        );
      } else {
        alert("주문 실패: " + data.error);
      }
    } catch (error) {
      console.error("주문 요청 실패:", error);
      alert("서버 오류로 주문을 처리할 수 없습니다.");
    }
  });

  function startPayment(
    merchantUid,
    amount,
    email,
    recipientName,
    phoneNumber,
    addr,
    detailAddr,
    zipcode
  ) {
    IMP.init("imp11066522"); // 본인의 아임포트 가맹점 식별 코드 사용

    console.log("주문 ID (orderUid):", merchantUid);
    console.log("결제 금액:", amount);
    console.log(
      "전달된 주소 정보:",
      "addr:",
      addr,
      "| detailAddr:",
      detailAddr,
      "| zipcode:",
      zipcode
    );

    IMP.request_pay(
      {
        pg: "html5_inicis", // PG사 설정
        pay_method: "card",
        merchant_uid: merchantUid, // orderUid를 merchant_uid로 사용
        name: "상품 결제",
        amount: amount,
        buyer_email: email,
        buyer_name: recipientName, //
        buyer_tel: phoneNumber, // phoneNumber → tel
        buyer_addr: addr.trim() + " " + detailAddr.trim(), // addr + detailAddr 포함
        buyer_postcode: zipcode.trim(),
      },
      function (response) {
        if (response.success) {
          console.log("결제 성공:", response);
          verifyPayment(
            response.imp_uid,
            response.merchant_uid,
            response.paid_amount
          );
        } else {
          console.error("결제 실패:", response.error_msg);
          alert("결제 실패: " + response.error_msg);
        }
      }
    );
  }

  function verifyPayment(impUid, merchantUid, paidAmount) {
    console.log(
      "결제 검증 요청: impUid=",
      impUid,
      "merchantUid=",
      merchantUid,
      "amount=",
      paidAmount
    );

    fetch("/market/payment/verify", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        "X-CSRF-TOKEN": document.querySelector("meta[name='_csrf']").content,
      },
      body: JSON.stringify({
        imp_uid: impUid,
        merchant_uid: merchantUid,
        amount: paidAmount,
      }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.success) {
          alert("결제가 성공적으로 처리되었습니다!");

          const orderId = data.orderId; // 응답에서 `orderId` 가져오기
          if (!orderId) {
            console.error("orderId가 없습니다.");
            alert("주문 ID를 찾을 수 없습니다.");
            return;
          }

          window.location.href = `/market/orderComplete?orderid=${orderId}`; // `orderId` 포함
        } else {
          console.error("결제 검증 실패:", data.message);
          alert("결제 검증 실패: " + data.message);
        }
      })
      .catch((error) => {
        console.error("결제 검증 요청 오류:", error);
        alert("결제 검증 요청 중 오류가 발생했습니다.");
      });
  }
});
