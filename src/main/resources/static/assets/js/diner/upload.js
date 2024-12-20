function DropFile(dropAreaId, fileListId) {
  let dropArea = document.getElementById(dropAreaId);
  let fileList = document.getElementById(fileListId);

  // 브라우저에 파일 드랍시 새 탭 열림 방지
  function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
  }

  // 업로드 박스 하이라이팅
  function highlight(e) {
    preventDefaults(e);
    dropArea.classList.add("highlight");
  }

  // 업로드 박스 하이라이팅 해제
  function unhighlight(e) {
    preventDefaults(e);
    dropArea.classList.remove("highlight");
  }

  // 업로드 박스 하이라이팅
  dropArea.addEventListener("dragenter", highlight, false);
  dropArea.addEventListener("dragover", highlight, false);
  dropArea.addEventListener("dragleave", unhighlight, false);
  dropArea.addEventListener("drop", handleDrop, false);

  // 파일 드랍시 파일 배열 생성
  function handleDrop(e) {
    unhighlight(e);
    let dt = e.dataTransfer;
    let files = dt.files;

    handleFiles(files);

    const fileList = document.getElementById(fileListId);
    if (fileList) {
      fileList.scrollTo({ top: fileList.scrollHeight });
    }
  }
  function handleFiles(files) {
    let formData = new FormData();
    for (let index = 0; index < files.length; index++) {
      formData.append("uploadFiles", files[index]);
    }

    fetch("/upload/upload", {
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
        data.forEach(previewFile);
      });
  }

  function previewFile(file) {
    console.log(file);
    fileList.appendChild(renderFile(file));
  }

  function renderFile(file) {
    let fileDOM = document.createElement("div");
    fileDOM.className = "file";
    fileDOM.innerHTML = `
      <div class="thumbnail">
        <img src="https://img.icons8.com/pastel-glyph/2x/image-file.png" alt="파일타입 이미지" class="image">
      </div>
      <div class="details" data-name="${file.fileName}" data-path="${file.folderPath}" data-uuid="${file.uuid}">
        <header class="header">
          <span class="name">${file.fileName}</span>
        </header>        
      </div>
    `;
    return fileDOM;
  }

  return {
    handleFiles,
  };
}

const dropFile = new DropFile("drop-file", "files");
// const fileInput = document.querySelector("#chooseFile");

// fileInput.addEventListener("change", (e) => {
//   console.log("file input change");

//   const files = e.target.files;
//   let formData = new FormData();
//   for (let index = 0; index < files.length; index++) {
//     formData.append("uploadFiles", files[index]);
//   }

//   fetch("/upload/upload", {
//     method: "post",
//     headers: {
//       "X-CSRF-TOKEN": csrfValue,
//     },
//     body: formData,
//   })
//     .then((response) => response.json())
//     .then((data) => {
//       console.log(data);

//       // 첨부 파일 화면 출력
//       // showUploadImages(data);
//     });
// });

// function showUploadImages(files) {
//   // 이미지 보여줄 영역 찾아오기
//   const output = document.querySelector("div .file");

//   let tags = "";

//   files.forEach((file) => {
//     tags += `<div class="thumbnail">`;
//     tags += `<img src="https://img.icons8.com/pastel-glyph/2x/image-file.png" alt="파일타입 이미지" class="image">`;
//     tags += `</div>`;
//     tags += `<div class="details" data-name="${file.fileName}" data-path="${file.folderPath}" data-uuid="${file.uuid}">`;
//     tags += `<header class="header">`;
//     tags += `<span class="name">${file.name}</span>`;
//     tags += `<span class="size">${file.size}</span>`;
//     tags += `</header>`;
//     tags += `</div>`;
//   });
//   output.insertAdjacentHTML("beforeend", tags);
// }

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
