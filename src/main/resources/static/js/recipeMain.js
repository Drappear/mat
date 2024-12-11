// error (preload 페이지로 빠지는 문제 try-catch)

document.addEventListener("DOMContentLoaded", function () {
  console.log("DOMContentLoaded 이벤트가 정상적으로 실행되었습니다.");

  // Example Data
  document.addEventListener("DOMContentLoaded", function () {
    const cardData = [
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "[슈퍼푸드샐러드] 질리언 앱산 주는 슈퍼푸드 샐러드",
        author: "싱글맘대장",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 20",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "치킨보다 교촌 허니콤보 만들기",
        author: "태평체질",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 1,972",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "일본요리 만들기 일본 겨울 가정식요리 츄우",
        author: "일본사는누아",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 313",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "즉석,오이 부추 무침(백종원임방)",
        author: "탱글스윗",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 3,836",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "이사할때 콩나물무침 레시피/ 하얀 콩나물무침 만들기",
        author: "써니지",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 2,005",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "매운맛콩나물무침 레시피 매일반찬",
        author: "라피네",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 2,275",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title:
          "시금치무침(시금치데치기 방법/ 시금치나물 요리 나물반찬 한접시레시피)",
        author: "ariel1007",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 5,090",
      },
      {
        imgSrc: "https://placehold.co/300x300/png",
        title: "오이고추된장무침(사계절 먹기좋은 만찬 고추무침 5분 레시피)",
        author: "ariel1007",
        profileImg: "https://placehold.co/40x40/png",
        views: "조회수 19,910",
      },
    ];

    // `cardGroup` 요소 가져오기
    const cardGroup = document.getElementById("cardGroup");

    // 카드 데이터가 로드되었는지 확인
    if (!cardGroup) {
      console.error("cardGroup 요소를 찾을 수 없습니다.");
      return;
    }

    // 데이터 반복문으로 HTML 생성
    const result = cardData
      .map((review) => {
        return `
        <div class="flex flex-col bg-white border p-4 rounded-md">
          <img src="${review.imgSrc}" alt="${review.title}" class="w-[300px] h-[300px] rounded-md object-cover mb-2">
          <p class="text-lime-950 text-sm mb-1">${review.title}</p>
          <div class="flex items-center space-x-2 text-neutral-500 text-xs">
            <img src="${review.profileImg}" alt="${review.author}" class="w-[20px] h-[20px] rounded-full object-cover">
            <span>${review.author}</span>
            <span>${review.views}</span>
          </div>
        </div>`;
      })
      .join(""); // 배열을 문자열로 변환

    // 결과 HTML을 카드 그룹에 삽입
    cardGroup.innerHTML = result;
  });
});
