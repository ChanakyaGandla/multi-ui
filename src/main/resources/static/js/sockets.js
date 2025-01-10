const socket = new WebSocket("ws://localhost:8080/socket");

socket.onopen = () => {
    console.log("WebSocket connected");
    socket.send("I have joined!");
    console.log("weeeeeee");
}
socket.onmessage = (event) => {
    console.log(event.data);
    var d = JSON.parse(event.data);
    redrawBoard(d.s, d.t);
    redrawPlayer(d.a);
};

const setup = function(){

}



const redrawPlayer = function(d){
    const p = document.getElementById("p");
    p.textContent = d;
    p.className   = "c"+d;
}

const redrawBoard = function(d,t){
    const b  = document.getElementById("board");
    const rows  = b.querySelectorAll("div");
    for(let y = 0; y < rows.length; y++){
        const row = rows[y].querySelectorAll("button");
        for(let x = 0; x < row.length; x++ ){
            row[x].querySelector("i").textContent = d[y][x];
            row[x].className = "c" + d[y][x];
            row[x].className += " " + t[y][x];
        }
    }
}
const buttons = document.getElementById("board").querySelectorAll("button");

for(let b of buttons){
    b.addEventListener("click",function(e){
       // alert();
    });
}
