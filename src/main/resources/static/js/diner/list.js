/*-----------------------
        Categories Slider
    ------------------------*/
$(".diners__slider").owlCarousel({
  loop: true,
  margin: 0,
  items: 1,
  dots: false,
  nav: true,
  navText: ["<span class='fa fa-angle-left'><span/>", "<span class='fa fa-angle-right'><span/>"],
  animateOut: "fadeOut",
  animateIn: "fadeIn",
  smartSpeed: 1200,
  autoHeight: false,
  autoplay: true,
  responsive: {
    0: {
      items: 1,
    },

    480: {
      items: 1,
    },

    768: {
      items: 1,
    },

    992: {
      items: 1,
    },
  },
});
