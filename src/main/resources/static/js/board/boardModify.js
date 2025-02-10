document.addEventListener("DOMContentLoaded", function () {
  const imageInput = document.querySelector("#image");
  const deleteImageCheckbox = document.querySelector("#deleteImage");

  // 이미지 선택 시 hidden 필드 값 업데이트
  if (imageInput && deleteImageCheckbox) {
    imageInput.addEventListener("input", function () {
      if (imageInput.files && imageInput.files.length > 0) {
        console.log("File selected:", imageInput.files[0].name);
        deleteImageCheckbox.value = "true"; // 파일이 선택되었을 경우 값 설정
      } else {
        console.log("No file selected.");
        deleteImageCheckbox.value = "false"; // 파일 선택 취소 시 값 재설정
      }
    });
  }
});
