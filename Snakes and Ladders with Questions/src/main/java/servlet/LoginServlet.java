package servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/LoginServlet"}
)
public class LoginServlet extends HttpServlet {

    int PlayerCount = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        JSONParser parser = new JSONParser();


        String name = request.getParameter("Name").trim();

        JSONObject data = new JSONObject();
        FileReader fileR = new FileReader("src/main/webapp/data.json");
        try {
            data = (JSONObject) parser.parse(fileR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fileR.close();
        String dest = "index.jsp";
        if (PlayerCount <=3){
            ((JSONObject)((JSONArray)data.get("Players")).get(PlayerCount)).put("name",name);
            ((JSONObject)((JSONArray)data.get("Players")).get(PlayerCount)).put("cell",1);
            request.getSession().setAttribute("name", name);
        }else{
            dest = "full.jsp";
        }
        FileWriter fileW = new FileWriter("src/main/webapp/data.json");
        fileW.write(data.toString());
        fileW.close();
        PlayerCount = PlayerCount + 1;

        response.sendRedirect(dest);



    }
}
