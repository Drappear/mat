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
    files = [...files];
    files.forEach(previewFile);
  }

  function previewFile(file) {
    console.log(file);
    fileList.appendChild(renderFile(file));
  }

  // 파일 추가시 목록 생성
  function renderFile(file) {
    let fileDOM = document.createElement("div");
    fileDOM.className = "file";
    fileDOM.innerHTML = `
      <div class="thumbnail">
        <img src="https://img.icons8.com/pastel-glyph/2x/image-file.png" alt="파일타입 이미지" class="image">
      </div>
      <div class="details">
        <header class="header">
          <span class="name">${file.name}</span>
          <span class="size">${file.size}</span>
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
