$(window).bind('scroll', function () {
    if ($(window).scrollTop() > $('.header').outerHeight()) {
        $('.fixedmenu').addClass('fixed');
    } else {
        $('.fixedmenu').removeClass('fixed');
    }
});

