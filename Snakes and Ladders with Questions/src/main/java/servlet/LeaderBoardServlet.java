package servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@WebServlet(
        name = "LeaderBoardServlet",
        urlPatterns = {"/LB"}
)
public class LeaderBoardServlet extends HttpServlet {

    String data;
    String line;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        BufferedReader fileR = new BufferedReader(new FileReader("src/main/webapp/stats.txt"));
        data = "<tr><th>Player Name</th><th>Games Won</th><th>Games Played</th><th>Win to Loss %</th></tr>";
        while((line = fileR.readLine()) != null){
            String[] linesplit = line.split(",");
            String lastcol = String.valueOf(Math.round((Float.parseFloat(linesplit[1])/ Float.parseFloat(linesplit[2]))*100));
            data = data +"<tr><th>"+linesplit[0]+"</th><th>"+linesplit[1]+"</th><th>"+linesplit[2]+"</th><th>"+ lastcol +"</th></tr>";
        }

        data = data.trim();
        fileR.close();
        resp.getWriter().write(data);

    }

}
