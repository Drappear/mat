const af = document.querySelector("#actionForm");
const reviewForm = document.querySelector("#reviewForm");
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
$(".star-rating > .star").click(function () {
  $(this).parent().children("span").removeClass("on");
  $(this).addClass("on").prevAll("span").addClass("on");
});

$(".taste-score > .star").click(function () {
  let val = $(this).attr("data-value");

  document.querySelector("#tasteScore").value = val;

  return false;
});

$(".price-score > .star").click(function () {
  let val = $(this).attr("data-value");

  document.querySelector("#priceScore").value = val;

  return false;
});

$(".service-score > .star").click(function () {
  let val = $(this).attr("data-value");

  document.querySelector("#serviceScore").value = val;

  return false;
});

// 영화의 전체 리뷰 가져오기
// const reviewLoaded = () => {
//   fetch(`/reviews/${did}/all`)
//     .then((response) => response.json())
//     .then((data) => {
//       console.log(data);
//       // 도착한 data 화면에 보여주기

//       document.querySelector(".review-cnt").innerHTML = data.length;

//       if (data.length > 0) {
//         reviewList.classList.remove("hidden");
//       }

//       let result = "";

//       data.forEach((review) => {
//         result += `<div class="d-flex justify-content-between my-2 border-bottom py-2 review-row" data-rno="${review.reviewNo}">`;
//         result += `  <div class="flex-grow-1 align-self-center">`;
//         result += `    <div>`;
//         result += `      <span class="font-semibold">${review.text}</span>`;
//         result += `    </div>`;
//         result += `    <div class="small text-muted">`;
//         result += `      <span class="d-inline-block mr-3">${review.nickName}</span>`;
//         result += `      <span class="grade">${review.grade}</span><div class="starrr"></div>`;
//         result += `    </div>`;
//         result += `    <div class="text-muted">`;
//         result += `      평점 : <span class="small">${formatDate(review.regDate)}</span>`;
//         result += `    </div>`;
//         result += `  </div>`;

//         // 리뷰 작성자 == 로그인 사용자
//         if (review.email === loginUser) {
//           result += `  <div class="d-flex flex-column align-self-center">`;
//           result += `    <div class="mb-2">`;
//           result += `      <button class="btn btn-outline-danger btn-sm">삭제</button>`;
//           result += `    </div>`;
//           result += `    <div class="mb-2">`;
//           result += `      <button class="btn btn-outline-success btn-sm">수정</button>`;
//           result += `    </div>`;
//           result += `  </div>`;
//         }
//         result += `</div>`;
//       });

//       // 리뷰 영역에 보여주기
//       reviewList.innerHTML = result;
//     });
// };

// 리뷰 등록
reviewForm.addEventListener("submit", (e) => {
  e.preventDefault();

  const content = reviewForm.content.value;
  const tasteScore = reviewForm.tasteScore.value;
  const priceScore = reviewForm.priceScore.value;
  const serviceScore = reviewForm.serviceScore.value;
  const mid = reviewForm.mid.value;

  const review = {
    content: content,
    tasteScore: tasteScore || 1,
    priceScore: priceScore || 1,
    serviceScore: serviceScore || 1,
    did: did,
    mid: mid,
    // nickname: nickname,
  };
  // 신규 작성
  fetch(`/review/${did}`, {
    headers: {
      "content-type": "application/json",
      "X-CSRF-TOKEN": csrfValue,
    },
    method: "post",
    body: JSON.stringify(review),
  })
    .then((response) => response.text())
    .then((data) => {
      if (data) {
        alert("리뷰 작성 완료.");
        reviewForm.content.value = "";
        reviewForm.querySelector(".taste-score > .star:first-child").click();
        reviewForm.querySelector(".price-score > .star:first-child").click();
        reviewForm.querySelector(".service-score > .star:first-child").click();

        // 리뷰 새로고침
        // reviewLoaded();
      }
    });
});

// 수정
// fetch(`/reviews/${mno}/${reviewNo}`, {
//   headers: {
//     "content-type": "application/json",
//     "X-CSRF-TOKEN": csrfValue,
//   },
//   method: "put",
//   body: JSON.stringify(review),
// })
//   .then((response) => response.text())
//   .then((data) => {
//     console.log(data + " 번 리뷰 수정");

//     if (data) {
//       alert("리뷰가 수정되었습니다.");
//       reviewForm.reviewNo.value = "";
//       reviewForm.email.value = "";
//       reviewForm.nickName.value = "";
//       reviewForm.text.value = "";
//       reviewForm.querySelector(".btn-outline-danger").innerHTML = "리뷰 등록";
//       reviewLoaded();
//     }
//   });
