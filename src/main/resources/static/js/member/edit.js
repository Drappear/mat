document.querySelector("#profileImage").addEventListener("change", (e) => {
  const files = e.target.files;

  let formData = new FormData();
  formData.append("uploadFiles", files[0]);

  fetch("/member/image", {
    method: "post",
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      // 첨부 파일 화면 출력
      showUploadImages(data);
    });
});
document.getElementById("profilePhoto").addEventListener("click", function () {
  var imageUrl = this.getAttribute("data-file"); // 클릭한 이미지의 URL 가져오기
  var modalImage = document.getElementById("modalImage");
  modalImage.src = "/profile/display?fileName=" + imageUrl; // 모달에 이미지를 로드
});

let isNicknameChecked = false; // 닉네임 중복 확인 여부 플래그

function checkDuplicateNickname() {
  const nicknameInput = document.getElementById("nickname");
  const nickname = nicknameInput.value.trim();
  const currentNickname = nicknameInput
    .getAttribute("data-current-nickname")
    .trim();
  const message = document.getElementById("nickname-message");

  // 중복 확인 플래그 초기화
  isNicknameChecked = false;

  switch (true) {
    // 닉네임이 비어있으면
    case nickname === "":
      message.textContent = "닉네임을 입력해주세요.";
      message.style.color = "red";
      // console.log("닉네임이 비어있음");

      return;

    // 중복 확인 API 호출
    default:
      fetch(`/member/check-duplicate-nickname?nickname=${nickname}`)
        .then((response) => response.json())
        .then((isDuplicate) => {
          if (isDuplicate && nickname !== currentNickname) {
            // 중복된 닉네임일 경우
            message.textContent = "이미 사용 중인 닉네임입니다.";
            message.style.color = "red";
            isNicknameChecked = false;
            // console.log("입력된 닉네임:", nickname);
            // console.log("현재 닉네임:", currentNickname);
            // console.log("비교 결과:", nickname === currentNickname);
          } else if (nickname === currentNickname) {
            message.textContent =
              "닉네임 변경을 원치 않으시면 저장을 눌러주세요.";
            message.style.color = "green";
            isNicknameChecked = true;
            // console.log("닉네임이 현재 닉네임과 동일");
          } else {
            // 사용 가능한 닉네임일 경우
            message.textContent = "사용 가능한 닉네임입니다.";
            message.style.color = "green";
            isNicknameChecked = true;
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          message.textContent = "중복 확인 중 오류가 발생했습니다.";
          message.style.color = "red";
          isNicknameChecked = false;
        });
  }
}

// 폼 제출 시 중복 확인 여부를 검증
function validateForm() {
  if (!isNicknameChecked) {
    alert("닉네임 중복 확인을 해주세요.");
    return false; // 폼 제출 막기
  }
  return true; // 검증 완료 시 폼 제출 허용
}
