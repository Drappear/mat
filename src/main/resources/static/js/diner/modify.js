const cf = document.querySelector("#createForm");
const delBtn = document.querySelector("#delBtn");
const listBtn = document.querySelector("#listBtn");
const fileInput = document.querySelector("[type='file']");

if (delBtn) {
  delBtn.addEventListener("click", () => {
    if (!confirm("삭제 하시겠습니까?")) {
      return;
    }

    cf.action = "/diner/delete";
    cf.submit();
  });
}
listBtn.addEventListener("click", () => {
  location.href = "/diner/list";
});

const removeImageBtn = document.querySelectorAll("#imageFilesDiv ul li a");
// x를 누르면 삭제 요청
removeImageBtn.forEach((element) => {
  element.addEventListener("click", (e) => {
    // a태그 기능 중지
    e.preventDefault();
    // x 눌러진 태그 요소 찾기
    // href 값 가져오기
    const element = e.target.closest("a");

    // 삭제할 이미지
    const removeLi = e.target.closest("li");

    if (confirm("정말로 이미지를 삭제하시겠습니까?")) {
      // 삭제 할 이미지 경로 추출
      const filePath = element.getAttribute("href");
      let formData = new FormData();
      formData.append("filePath", filePath);

      fetch("/dfup/remove", {
        method: "post",
        headers: {
          "X-CSRF-TOKEN": csrfValue,
        },
        body: formData,
      })
        .then((response) => {
          if (!response.ok) throw new Error("에러 발생");
          return response.text();
        })
        .then((data) => {
          // 화면 이미지 제거
          if (data) removeLi.remove();
        });
    }
  });
});

function showUploadImages(files) {
  // 이미지 보여줄 영역 찾아오기
  const output = document.querySelector("#imageFilesDiv ul");

  let tags = "";
  files.forEach((file) => {
    console.log(file.imageURL);

    tags += `<li>`;
    tags += `<img th:attr="data-file=${file.imageURL}" th:src="|/dfup/display?fileName=${file.imageURL}|" `;
    tags += ` th:if="${file.path != null}" `;
    tags += ` alt="" class="block" width="120px" height="100px"/>`;
    tags += `<a th:href="${file.imageURL}" data-file="">`;
    tags += `<i class="fa-solid fa-xmark"></i>`;
    tags += `</a>`;
    tags += `</li>`;
  });
  output.insertAdjacentHTML("beforeend", tags);
}

fileInput.addEventListener("change", (e) => {
  const files = e.target.files;

  let formData = new FormData();
  for (let index = 0; index < files.length; index++) {
    formData.append("uploadFiles", files[index]);
  }
  formData.append("uploadPage", "diner");
  formData.append("did", did);

  fetch("/dfup/upload", {
    method: "post",
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      // 첨부 파일 화면 출력
      showUploadImages(data);
    });
});
