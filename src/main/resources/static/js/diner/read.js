const af = document.querySelector("#actionForm");
const modBtn = document.querySelector("#modBtn");
const listBtn = document.querySelector("#listBtn");

modBtn.addEventListener("click", () => {
  af.method = "get";
  af.action = "/diner/modify";
  af.submit();
});

listBtn.addEventListener("click", () => {
  af.querySelector("[name='did']").remove();
  af.method = "get";
  af.action = "/diner/list";
  af.submit();
});

// 리뷰 이미지
// function showUploadReviewImages(files) {
//   // 이미지 보여줄 영역 찾아오기
//   const output = document.querySelector(".review-input-img-preview ul");

//   let tags = "";

//   files.forEach((file) => {
//     tags += `<li data-name="${file.fileName}" data-path="${file.folderPath}" data-uuid="${file.uuid}">`;
//     tags += `  <div>`;
//     tags += `   <a href="">`;
//     tags += `       <img src= "/upload/display?fileName=${file.thumbImageURL}" class="block"`;
//     tags += `   </a>`;
//     tags += `   <span class="text-sm d-inlin-block mx-1">${file.fileName}</span>`;
//     tags += `   <a href="${file.imageURL}" data-file="">`;
//     tags += `       <i class="fa-solid fa-xmark"></i>`;
//     tags += `   </a>`;
//     tags += `  </div>`;
//     tags += `</li>`;
//   });
//   output.insertAdjacentHTML("beforeend", tags);
// }

// fileInput.addEventListener("change", (e) => {
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
//       showUploadReviewImages(data);
//     });
// });

// 별점 기능
$('.star-rating > .star').click(function() {
  $(this).parent().children('span').removeClass('on');
  $(this).addClass('on').prevAll('span').addClass('on');
});