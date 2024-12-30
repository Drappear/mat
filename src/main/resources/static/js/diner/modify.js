const cf = document.querySelector("#createForm");
const delBtn = document.querySelector("#delBtn");

if(delBtn){
  delBtn.addEventListener("click",() => {
    if (!confirm("삭제 하시겠습니까?")) {
      return;
    }    

    cf.action = "/diner/delete";
    cf.submit();
  });
}