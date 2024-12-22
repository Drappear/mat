const af = document.querySelector("#actionForm");
const modBtn = document.querySelector("#modBtn");
const listBtn = document.querySelector("#listBtn");
const reviewBtn = document.querySelector("#reviewBtn");

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