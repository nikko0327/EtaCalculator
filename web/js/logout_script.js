$(document).ready(function() {

    //Disable back button on login page
    //works on chrome well
    window.history.forward();

    //this checks if browser is IE
    if($.browser.msie) {
        $(".container").empty();

        msg = "<div class='masthead'><h3 class='muted'>Eta Calculator</h3></div><hr>";
        msg += "<p class='text-error'>This site is compatible with Chrome or Firefox browsers</p>";
        msg += "<br>";
        msg += "<p>Please download Chrome: <a href='https://www.google.com/intl/en/chrome/browser/' target='_blank'>Download</a></p>";
        msg += "<p>Please download Firefox: <a href='http://www.mozilla.org/en-US/firefox/new/' target='_blank'>Download</a></p>";

        $(".container").append(msg);

        return;
    }

    $.post("logout");
});
