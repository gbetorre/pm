// Fixed to top megamenù
var headerHeight = $("#idHeader").height();
$(window).scroll(function(e) {
  var scroll = $(window).scrollTop();
  if (scroll >= headerHeight) {
      $('#divMenu').addClass("fixedTop");
  } else {
      $('#divMenu').removeClass("fixedTop");
  }
});

// Function go to top
// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function() {scrollFunction()};
function scrollFunction() {
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
    document.getElementById("goTop").style.display = "block";
  } else {
    document.getElementById("goTop").style.display = "none";
  }
}

$('a[href="#top"]').click(function(){
    $('html, body').animate({scrollTop:0}, 'slow');
    return false;
});