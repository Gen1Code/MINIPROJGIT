<html>
<head>
</head>
<style>
table, tr, th{
        border: 1px solid black;
        border-collapse: collapse;
        width: 800px;
        height: 40px;
}


</style>
<script>

    function draw(data){
        document.getElementById("LB").innerHTML = data;
    }

    function refresh(){
        var xmlhttp=new XMLHttpRequest();
        var url="LB";
        xmlhttp.onreadystatechange=function(){
        if(this.readyState==4&&this.status==200){
            data=this.responseText;
            draw(data);
        }
        };
        xmlhttp.open("GET",url,true);
        xmlhttp.send();
    }


</script>
<body onload="refresh()">
<button id = "btn" onclick="refresh();">Refresh</button>
<a href="index.jsp">Go Back</a>
<div><h1></h1></div>
<table id="LB"></table>
<div></div>
</body>
</html>
