<html>
<head>

</head>
<%
        String nameS = (String) session.getAttribute("name");
        try{
        if(nameS.equals("null")){
            response.sendRedirect("login.jsp");
        }
        }catch(NullPointerException e){
            response.sendRedirect("login.jsp");
        }
        %>

<script>

    var UpdateInterval = null;
    function updateMe(){
        if(UpdateInterval != null){
            window.clearInterval(UpdateInterval);
        }
        UpdateInterval = window.setInterval(update, 500);

    }

    var data = {};
    function drawBG(data, ctx){
        var img = new Image();
        img.src = "t-n-1196-snakes-and-ladders-board-game_ver_1.png";
        img.onload = function(){ctx.drawImage(img,150,75,700,700);

            var img3 = new Image();
            img3.src = "Dice_"+data.Dice.face+".png";
            img3.onload = function() {ctx.drawImage(img3, 40, 340,100, 100);}
            var YourPlayerIndex;
            var YourPlayerColour;
            for (var i = 0;i<data.Players.length;i++){
                var placement = data.Players[i].cell;
                var row = (Math.floor((placement-1) / 10));
                var col;

                if(row%2 == 0){
                    col = 10-(placement-1)%10;
                }else{
                    col = ((placement-1)%10)+1;
                }

                ctx.beginPath();
                ctx.arc(850  + (20 * (i%3)) - (col*67.5), 750 - (20 * Math.floor(i/3)) - (row*67.5) , 10, 0, 2 * Math.PI);
                ctx.fillStyle = data.Players[i].colour;
                ctx.fill();
                if(data.Players[i].name == "${name}"){
                    YourPlayerIndex = i;
                    YourPlayerColour = data.Players[i].colour;
                    document.getElementById("question").innerHTML = data.Question.Q;
                    if (YourPlayerIndex == data.Turn){
                        document.getElementById("btn").style.display = "block";
                        if(data.Question.AD){
                            document.getElementById("btn2").style.display = "none";
                            document.getElementById("Answer").style.display = "none";
                            if(data.Question.AC){
                                document.getElementById("question").innerHTML = "You got it Correct +2";
                             }else{
                                document.getElementById("question").innerHTML = "You got it Wrong";
                            }
                        }else{
                            document.getElementById("btn2").style.display = "block";
                            document.getElementById("Answer").style.display = "block";
                        }

                    }else{

                        if(data.Question.AC && data.Question.AD){
                            document.getElementById("question").innerHTML = "<span style=\"color: "+data.Players[data.Turn].color+"\"> Player "+data.Players[data.Turn].name+"</span> got it Correct +2";
                        }else if(data.Question.AD){
                            document.getElementById("question").innerHTML = "<span style=\"color: "+data.Players[data.Turn].color+"\"> Player "+data.Players[data.Turn].name+"</span> got it Wrong";
                        }
                        document.getElementById("btn").style.display = "none";
                        document.getElementById("btn2").style.display = "none";
                        document.getElementById("Answer").style.display = "none";
                    }
                }




            }
            document.getElementById("PlayerName").innerHTML = "${name} you are<span style=\"color: "+YourPlayerColour+"\"> Player "+(YourPlayerIndex + 1)+"</span>";
            if (data.Winner !="none"){
                document.getElementById("won").innerHTML = data.Winner + " won last Game"
            }

        }
    }

    function draw(dataTBD){
        var canvas = document.getElementById('canvas');
        var ctx = canvas.getContext('2d');
        console.log(data);
        canvas.width = 1000;
        canvas.height = 900;
        drawBG(data, ctx);
    }

    function roll(){
        var xmlhttp = new XMLHttpRequest();
        var url = "hello"+window.location.search;
        xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            data = JSON.parse(this.responseText);
            draw(data);
        }
        };
        xmlhttp.open("GET", url, true);
        xmlhttp.send();
    }

    function update(){
        var xmlhttp = new XMLHttpRequest();
        var url = "update";
        xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            data = JSON.parse(this.responseText);
            draw(data);
        }
        };
        xmlhttp.open("GET", url, true);
        xmlhttp.send();
    }

    function start(){
        var xmlhttp=new XMLHttpRequest();
        var url="start";
        xmlhttp.onreadystatechange=function(){
        if(this.readyState==4&&this.status==200){
        data=JSON.parse(this.responseText);
        draw(data);
        }
        };
        xmlhttp.open("GET",url,true);
        xmlhttp.send();
    }

    function Answered(){
        var xmlhttp = new XMLHttpRequest();
        var url = "hello";
        xmlhttp.open("POST", url);
        var params = document.getElementById("Answer").value;

        xmlhttp.onreadystatechange  = function() {
        if (xmlhttp.readyState === 4){
            data=JSON.parse(this.responseText);
            console.log(data);
            draw(data);
        }
        };
        xmlhttp.send(JSON.stringify(params));
    }

</script>
<div>
        <h3 id="PlayerName"></h3>
        <button id = "btn" onclick="roll();">Roll Dice</button>
        <a href="LeaderBoard.jsp">Leader Board</a>
        <p id="won"></p>
</div>
<body onload="start();updateMe();">
        <canvas id="canvas">Game</canvas>
        <h1 id="question"></h1>
        <input id="Answer" type="text" pattern="[A-Z0-9a-z]+" placeholder="Answer" name="Answer">
        <button id="btn2" onclick="Answered();">Enter</button>
</body>
</html>
