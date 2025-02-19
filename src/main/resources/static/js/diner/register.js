const fileInput = document.querySelector("#uploadDinerFileInput");

function handleFiles(files) {
  let formData = new FormData();
  for (let index = 0; index < files.length; index++) {
    formData.append("uploadFiles", files[index]);
  }
  formData.append("uploadPageName", "diner");

  fetch("/dfup/upload", {
    method: "post",
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      data = [...data];
    });
}

// 작성 클릭시
document.querySelector("#createForm").addEventListener("submit", (e) => {
  // form submit 중지
  e.preventDefault();

  // 첨부파일 정보 수집 : details
  const attachInfos = document.querySelectorAll(".details");
  let result = "";
  attachInfos.forEach((obj, idx) => {
    result += `<input type="hidden" name="dinerImageDtos[${idx}].path" value="${obj.dataset.path}" />`;
    result += `<input type="hidden" name="dinerImageDtos[${idx}].uuid" value="${obj.dataset.uuid}" />`;
    result += `<input type="hidden" name="dinerImageDtos[${idx}].imgName" value="${obj.dataset.name}" />`;
  });

  e.target.insertAdjacentHTML("beforeend", result);

  // 폼 내용 확인
  console.log(e.target.innerHTML);

  e.target.submit();
});
