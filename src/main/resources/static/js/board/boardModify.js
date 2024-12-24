// 기존 이미지 삭제 처리
function deleteCurrentImage() {
  // 이미지 미리보기 제거
  const imagePreview = document.getElementById("currentImagePreview");
  const deleteButton = document.getElementById("deleteImageButton");

  if (imagePreview) {
    imagePreview.remove(); // 이미지 미리보기 제거
  }
  if (deleteButton) {
    deleteButton.remove(); // X 버튼 제거
  }

  // 삭제 플래그 설정
  const deleteImageFlag = document.getElementById("deleteImageFlag");
  if (deleteImageFlag) {
    deleteImageFlag.value = "true"; // 삭제 플래그 활성화
  }

  // 새로운 이미지 업로드를 활성화
  const imageInput = document.getElementById("image");
  if (imageInput) {
    imageInput.disabled = false; // 업로드 활성화
  }
}

// 페이지 로드 시 초기 상태 설정
window.onload = function () {
  const deleteImageFlag = document.getElementById("deleteImageFlag");
  const imageInput = document.getElementById("image");

  // 기존 이미지가 있을 경우, 새로운 이미지 업로드 비활성화
  if (deleteImageFlag && deleteImageFlag.value === "false" && imageInput) {
    imageInput.disabled = true; // 기존 이미지가 있으면 업로드 비활성화
  }
};
