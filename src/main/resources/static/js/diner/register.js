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
    formData.append("uploadPageName", "diner");
    formData.append("uploadId", "diner");

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
        data.forEach(previewFile);
      });
  }

  function previewFile(file) {
    console.log(file);
    fileList.appendChild(renderFile(file));
  }

  function renderFile(file) {
    let previewDiv = document.createElement("div");
    previewDiv.className = "file";
    previewDiv.innerHTML = `
      <div class="thumbnail">
        <img src="https://img.icons8.com/pastel-glyph/2x/image-file.png" alt="파일타입 이미지" class="image">
      </div>
      <div class="details" data-name="${file.fileName}" data-path="${file.folderPath}" data-uuid="${file.uuid}">
        <header class="header">
          <span class="name">${file.fileName}</span>
        </header>        
      </div>
    `;
    return previewDiv;
  }

  return {
    handleFiles,
  };
}

const dropFile = new DropFile("drop-file", "files");

const fileInput = document.querySelector("#chooseFile");
const filePreviewDiv = document.querySelector(".filePreview");

fileInput.addEventListener("change", () => {
  const reader = new FileReader();
  reader.onload = ({ target }) => {
    filePreviewDiv.src = target.result;
  };
  reader.readAsDataURL(fileInput.files[0]);
});

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
    result += `<input type="hidden" name="dinerImageDtos[${idx}].imgCate" value="1" />`;
  });

  e.target.insertAdjacentHTML("beforeend", result);

  // 폼 내용 확인
  console.log(e.target.innerHTML);

  e.target.submit();
});
