document.addEventListener("DOMContentLoaded", function () {
  const imageInput = document.querySelector("#image");
  const deleteImageCheckbox = document.querySelector("#deleteImage");
  const form = document.querySelector("form");

  if (imageInput && deleteImageCheckbox) {
    // 파일 선택 시 체크박스 체크
    imageInput.addEventListener("change", function () {
      if (imageInput.files && imageInput.files.length > 0) {
        deleteImageCheckbox.checked = true; // 선택된 경우 체크
        console.log("File selected:", imageInput.files[0].name); // 디버깅용
      } else {
        deleteImageCheckbox.checked = false; // 파일 선택 취소 시 해제
        console.log("No file selected."); // 디버깅용
      }
    });

    // 폼 제출 시 파일 상태 검증
    form.addEventListener("submit", function (event) {
      if (imageInput.files && imageInput.files.length > 0) {
        console.log("Submitting with file:", imageInput.files[0].name);
      } else {
        console.log("No file selected during submission.");
        deleteImageCheckbox.checked = false; // 파일이 없으면 체크박스 해제
      }
    });
  }
});
