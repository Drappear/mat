import config from "./config.js";
const { API_KEY } = config;

function loadKakaoMapScript() {
  return new Promise((resolve, reject) => {
    const script = document.createElement("script");
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&autoload=false&libraries=services`;
    script.onload = () => resolve();
    script.onerror = () => reject(new Error("Failed to load Kakao Map API script"));
    document.head.appendChild(script);
  });
}
// Kakao API가 로드된 후에 실행될 함수
function initializeMap() {
  // K// Kakao API가 로드된 후에 호출됨
  kakao.maps.load(function () {
    var mapContainer = document.getElementById("map"),
      mapOptions = {
        center: new kakao.maps.LatLng(33.4423379727783, 126.571449734542),
        level: 3,
      };

    var map = new kakao.maps.Map(mapContainer, mapOptions);

    // 주소-좌표 변환 객체
    var geocoder = new kakao.maps.services.Geocoder();

    // 주  주소로 좌표를 검색합니다
    geocoder.addressSearch(dinerAddr, function (result, status) {
      //  정상적으로 검색이 완료됐으면
      if (status === kakao.maps.services.Status.OK) {
        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({ map: map, position: coords });

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
      }
    }); //addressSearch
  }); //load
}

// 스크립트를 로드하고, 로드가 완료된 후 지도를 초기화
loadKakaoMapScript()
  .then(initializeMap)
  .catch((error) => console.error(error));
