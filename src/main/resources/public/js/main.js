
var http = new XMLHttpRequest();

http.onreadystatechange = function () {

    if (this.readyState !== 4 || this.status !== 200) {
        return;
    }

    var response = JSON.parse(this.responseText);

    if (response.count > 0) {
        document.getElementById("request-count").innerHTML = response.count;
        document.getElementById("request-count").classList.remove("d-none");
    }

};

document.addEventListener("DOMContentLoaded", function(){
    http.open("GET", "/query/requests/count");
    http.send();
});