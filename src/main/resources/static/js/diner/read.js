const af = document.querySelector("#actionForm");
const reviewForm = document.querySelector("#reviewForm");
const modBtn = document.querySelector("#modBtn");
const listBtn = document.querySelector("#listBtn");
const reviewFileInput = document.querySelector("#reviewFile");
const reviewList = document.querySelector(".review-two-column-list");

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
function showUploadReviewImages(files) {
  // 이미지 보여줄 영역 찾아오기
  const output = document.querySelector(".review-input-img-preview ul");

  let tags = "";

  files.forEach((file) => {
    tags += `<li data-name="${file.fileName}" data-path="${file.folderPath}" data-uuid="${file.uuid}">`;
    tags += `  <div>`;
    tags += `   <a href="">`;
    tags += `       <img src= "/dfup/display?fileName=${file.imageURL}" class="block" width="150px" height="120px"`;
    tags += `   </a>`;
    tags += `   <div class="review-file-name text-sm d-inline-block mx-1">${file.fileName}</div>`;
    tags += `   <a href="${file.imageURL}" data-file="">`;
    tags += `       <i class="fa-solid fa-xmark"></i>`;
    tags += `   </a>`;
    tags += `  </div>`;
    tags += `</li>`;
  });
  output.insertAdjacentHTML("beforeend", tags);
}

reviewFileInput.addEventListener("change", (e) => {
  const files = e.target.files;

  let formData = new FormData();
  for (let index = 0; index < files.length; index++) {
    formData.append("uploadFiles", files[index]);
  }

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

      // 첨부 파일 화면 출력
      showUploadReviewImages(data);
    });
});

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

// 전체 리뷰 가져오기
const reviewLoaded = () => {
  fetch(`/review/${did}/all`)
    .then((response) => response.json())
    .then((data) => {
      // 도착한 data 화면에 보여주기

      //document.querySelector(".review-cnt").innerHTML = data.length;

      if (data.length > 0) {
        reviewList.classList.remove("hidden");
      }

      let result = "";

      data.dtoList.forEach((review) => {
        console.log(review);

        result += `
        <li class="review-list-item">
          <div class="review-reviewer-img">
            <img src="https://placehold.co/50x50" alt="" />
          </div>
          <table class="review-table">
            <tr>
              <td>작성자${review.nickname}</td>
              <td th:text="${review.regDate}">작성일</td>
            </tr>
            <tr>
              <td>총점</td>
              <td th:text="${review.tasteScore}">맛</td>
              <td th:text="${review.priceScore}">가격</td>
              <td th:text="${review.serviceScore}">서비스</td>
            </tr>
          </table>
          <div class="row" th:text="${review.content}">review text</div>
          <div class="row">
            `;
        review.dinerImageDtos.forEach(
          `<img th:src="|/dfup/display?fileName=${review.reviewImageURL}|" th:if="${
            path != null
          }" width="80px" height="60px"/>`
        );
        `
          </div>
        </li>
          `;
      });

      //리뷰 영역에 보여주기
      reviewList.insertAdjacentHTML("beforeend", result);
    });
};

reviewLoaded();

// 작성 클릭 시
reviewForm.addEventListener("submit", (e) => {
  // form submit 중지
  e.preventDefault();

  // data-name="" data-path="" data-uuid=""
  // 요소.dataset.name
  const attachInfos = document.querySelectorAll(".review-input-img-preview ul li");

  let dinerImageDtos = [];

  attachInfos.forEach((obj, idx) => {
    dinerImageDtos[idx] = {
      path: obj.dataset.path,
      uuid: obj.dataset.uuid,
      imgName: obj.dataset.name,
      imgCate: 2,
      did: did,
    };
  });

  // 리뷰 등록

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
    dinerImageDtos: dinerImageDtos,
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
        $("#review-img-preview").html("");

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
